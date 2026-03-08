# Adapter Pattern

---

## Definition

The **Adapter Pattern** is a structural design pattern that allows **incompatible interfaces to work together**. It acts as a bridge between two interfaces by wrapping an object with an incompatible interface and exposing a compatible one that clients expect.

**Type:** Structural Pattern

---

## Key Terms

| Term               | Definition                                                                        |
| ------------------ | --------------------------------------------------------------------------------- |
| **Target**         | The interface that the client expects (`PaymentProcessor`)                        |
| **Client**         | The code that uses the Target interface (`CheckoutService`)                       |
| **Adaptee**        | The existing class with an incompatible interface (`PayPalAPI`, `StripeAPI`)      |
| **Adapter**        | The class that bridges Target and Adaptee (`PayPalAdapter`, `StripeAdapter`)      |
| **Object Adapter** | Uses composition - holds a reference to the adaptee                               |
| **Class Adapter**  | Uses multiple inheritance - extends both Target and Adaptee (less common in Java) |

---

## Problem Statement

You're building an **E-commerce Payment System**. Your `CheckoutService` handles what happens when a customer clicks "Place Order" - it processes payment, logs the transaction, updates the database, and sends confirmation.

Now you need to integrate with **third-party payment providers** (PayPal, Stripe, etc.) to process payments. Here's the challenge:

### The Challenge

Each payment provider has a **completely different interface**:

**PayPal's API:**

```java
class PayPalAPI {
    String makePayment(String email, double amount) { ... }  // Returns "Success" or "Failed"
    String provideRefund(String transactionId, double amount) { ... }
}
```

**Stripe's API:**

```java
class StripeAPI {
    int charge(String cardToken, int amountInCents, String currency) { ... }  // Returns HTTP status code
    int refundAmount(String chargeId, int amountInCents, String currency) { ... }
}
```

**The challenge:**

- Different method names (`makePayment` vs `charge`)
- Different parameter types (email vs card token)
- Different amount formats (dollars vs cents)
- Different return types (String "Success" vs int 200)

---

## Approach 1: Direct Usage with Conditionals (Bad)

Direct usage with conditionals:

```java
class CheckoutService {
    private PayPalAPI paypal;
    private StripeAPI stripe;
    private String provider;

    public void processPayment(String customerId, int amount) {
        if (provider.equals("paypal")) {
            String result = paypal.makePayment(customerId + "@customer.com", (double) amount);
            System.out.println(result.equals("Success") ? "Payment successful" : "Payment failed");
        } else if (provider.equals("stripe")) {
            int status = stripe.charge("tok_" + customerId, amount * 100, "USD");
            System.out.println(status == 200 ? "Payment successful" : "Payment failed");
        }
        // Every new provider = modify this code!
    }
}
```

**Now imagine processRefund needs the same logic:**

```java
public void processRefund(String transactionId, int amount) {
    if (provider.equals("paypal")) {
        String result = paypal.provideRefund(transactionId, (double) amount);
        System.out.println(result.equals("Success") ? "Refund successful" : "Refund failed");
    } else if (provider.equals("stripe")) {
        int status = stripe.refundAmount(transactionId, amount * 100, "USD");
        System.out.println(status == 200 ? "Refund successful" : "Refund failed");
    }
    // Same if-else mess AGAIN!
}
```

**Problems:**

- **Violates OCP** - Adding a new payment provider requires modifying multiple classes
- **Scattered conversion logic** - Dollars to cents conversion copy-pasted everywhere
- **Duplication** - Same if-else structure repeated in every service

---

## Approach 2: Create a Common Interface (Incomplete)

Define your own clean interface:

```java
// Our ideal interface - clean, consistent
interface PaymentProcessor {
    boolean pay(String customerId, int amount);
    boolean refund(String transactionId, int amount);
}
```

This is exactly what our application **should** work with:

- Consistent method names
- Simple parameter types
- Unified return type (boolean)

**The problem:** We can't modify third-party code!

- You don't own PayPal's or Stripe's code
- Library updates would overwrite your changes
- Third-party providers won't implement your interface

---

## Approach 3: Adapter Pattern (Good)

The key insight: **wrap the incompatible object** with a class that translates its interface to the one you expect.

We can't change PayPal's code, but we CAN create a **wrapper class** that:

1. Implements OUR `PaymentProcessor` interface
2. Internally holds a reference to PayPal's API
3. Translates between the two

```java
// Adapter wraps PayPal and translates to PaymentProcessor interface
class PayPalAdapter implements PaymentProcessor {
    private PayPalAPI paypal;

    public PayPalAdapter(PayPalAPI paypal) {
        this.paypal = paypal;
    }

    @Override
    public boolean pay(String customerId, int amount) {
        // TRANSLATE: Our interface -> PayPal's format
        String result = paypal.makePayment(customerId + "@customer.com", (double) amount);
        return result.equals("Success");  // "Success" -> true
    }
}

// Now CheckoutService only knows about PaymentProcessor!
class CheckoutService {
    private PaymentProcessor processor;

    public CheckoutService(PaymentProcessor processor) {
        this.processor = processor;
    }

    public void checkout(String customerId, int amount) {
        boolean success = processor.pay(customerId, amount);
        System.out.println(success ? "Payment successful" : "Payment failed");
    }
}

// Usage
CheckoutService checkout = new CheckoutService(new PayPalAdapter(new PayPalAPI()));
checkout.checkout("customer123", 50);
```

**How this solves the problem:**

- **Client unchanged** - CheckoutService only knows about `PaymentProcessor`
- **Provider unchanged** - PayPal's API remains as-is
- **Translation isolated** - All format conversions happen inside the adapter
- **Easy to extend** - New provider? Create a new adapter class
- **Easy to test** - Mock `PaymentProcessor` interface

---

**Flow:**

1. **Client** calls methods on the **Target** interface (`PaymentProcessor`)
2. **Adapter** receives the call and **translates** it to the **Adaptee's** format
3. **Adaptee** (PayPal, Stripe) executes its native method
4. **Adapter** translates the result back to the Target's expected format
5. **Client** receives a response in the format it expects

---

## UML Diagram (Payment Example)

<!-- TODO: Add Payment Example specific UML diagram here -->
<img width="1356" height="1411" alt="adapter diagram" src="https://github.com/user-attachments/assets/adb757af-7782-4311-b2ba-a4baabf54508" />

---

## UML Diagram (Standard)

<!-- TODO: Add standard Adapter Pattern UML diagram here -->
<img width="1356" height="870" alt="adapter generic uml" src="https://github.com/user-attachments/assets/dc5f1a55-9c12-4d35-a1e1-ba0842ca0888" />


---

## Implementation

```java
// TARGET INTERFACE
interface PaymentProcessor {
    boolean pay(String customerId, int amount);
    boolean refund(String transactionId, int amount);
}

// ADAPTEE 1 - PayPal returns "Success" or "Failed"
class PayPalAPI {
    public String makePayment(String paypalEmail, double amountInDollars) {
        System.out.println("[PayPal] Charging $" + amountInDollars + " to " + paypalEmail);
        return "Success";
    }
    public String provideRefund(String transactionId, double amountInDollars) {
        System.out.println("[PayPal] Refunding $" + amountInDollars);
        return "Success";
    }
}

// ADAPTEE 2 - Stripe returns HTTP status codes
class StripeAPI {
    public int charge(String cardToken, int amountInCents, String currency) {
        System.out.println("[Stripe] Charging " + amountInCents + " cents (" + currency + ")");
        return 200;
    }
    public int refundAmount(String chargeId, int amountInCents, String currency) {
        System.out.println("[Stripe] Refunding " + amountInCents + " cents (" + currency + ")");
        return 200;
    }
}

// ADAPTER 1 - Translates PayPal's String response to boolean
class PayPalAdapter implements PaymentProcessor {
    private PayPalAPI paypal;

    public PayPalAdapter(PayPalAPI paypal) { this.paypal = paypal; }

    @Override
    public boolean pay(String customerId, int amount) {
        String result = paypal.makePayment(customerId + "@customer.com", (double) amount);
        return result.equals("Success");
    }

    @Override
    public boolean refund(String transactionId, int amount) {
        String result = paypal.provideRefund(transactionId, (double) amount);
        return result.equals("Success");
    }
}

// ADAPTER 2 - Translates Stripe's int status to boolean, dollars to cents
class StripeAdapter implements PaymentProcessor {
    private StripeAPI stripe;

    public StripeAdapter(StripeAPI stripe) { this.stripe = stripe; }

    @Override
    public boolean pay(String customerId, int amount) {
        int status = stripe.charge("tok_" + customerId, amount * 100, "USD");
        return status == 200;
    }

    @Override
    public boolean refund(String transactionId, int amount) {
        int status = stripe.refundAmount(transactionId, amount * 100, "USD");
        return status == 200;
    }
}
```

**Four components:**

1. **Target** (`PaymentProcessor`) - interface the client expects
2. **Client** (`CheckoutService`) - uses the Target interface
3. **Adaptee** (`PayPalAPI`, `StripeAPI`) - existing classes with incompatible interfaces
4. **Adapter** (`PayPalAdapter`, `StripeAdapter`) - translates Target calls to Adaptee calls

---

## Usage Examples

```java
// CLIENT - Only knows about PaymentProcessor interface
class CheckoutService {
    private PaymentProcessor processor;

    public CheckoutService(PaymentProcessor processor) {
        this.processor = processor;
    }

    public void checkout(String customerId, int amount) {
        boolean success = processor.pay(customerId, amount);
        System.out.println(success ? "Payment successful" : "Payment failed");
    }

    public void processRefund(String transactionId, int amount) {
        boolean success = processor.refund(transactionId, amount);
        System.out.println(success ? "Refund successful" : "Refund failed");
    }
}

// Usage with PayPal
CheckoutService paypal = new CheckoutService(new PayPalAdapter(new PayPalAPI()));
paypal.checkout("john_doe", 50);
// Output: [PayPal] Charging $50.0 to john_doe@customer.com
//         Payment successful

// Usage with Stripe
CheckoutService stripe = new CheckoutService(new StripeAdapter(new StripeAPI()));
stripe.checkout("jane_doe", 30);
// Output: [Stripe] Charging 3000 cents (USD)
//         Payment successful

// Switch providers - client code unchanged!
```

**Key observations:**

- **CheckoutService never changes** - regardless of which payment provider is used
- **Provider switch is easy** - just inject a different adapter
- **Translation is hidden** - client doesn't know about dollars to cents conversion
- **Testability** - can easily mock `PaymentProcessor` interface

---

## When to Use Adapter Pattern

| Situation                                                         | Use Adapter? |
| ----------------------------------------------------------------- | ------------ |
| Need to use a class but its interface doesn't match what you need | Yes          |
| Integrating third-party libraries with different interfaces       | Yes          |
| Working with legacy code that can't be modified                   | Yes          |
| Want to create a reusable class that works with unrelated classes | Yes          |
| You own both interfaces and can modify them                       | No           |
| The interfaces are already compatible                             | No           |

---

## Common Beginner Confusions

### "How is Adapter different from Decorator?"

| Aspect           | Adapter Pattern                             | Decorator Pattern                            |
| ---------------- | ------------------------------------------- | -------------------------------------------- |
| **Purpose**      | Makes incompatible interfaces work together | Adds new behaviors to existing objects       |
| **Interface**    | **Changes** the interface                   | **Keeps** the same interface                 |
| **Focus**        | Translation/Conversion                      | Enhancement/Extension                        |
| **Relationship** | Adaptee and Target are different types      | Component and Decorator share same interface |
| **Wrapping**     | Usually wraps ONE object                    | Can stack multiple decorators                |

```java
// ADAPTER - Changes interface
PaymentProcessor processor = new PayPalAdapter(paypalApi);  // PayPalAPI -> PaymentProcessor

// DECORATOR - Same interface, adds behavior
Drink drink = new IceDecorator(whiskey);  // Drink -> Drink (with ice added)
```

---

### "When should I use Object Adapter vs Class Adapter?"

| Type               | Implementation      | Pros                                | Cons                          |
| ------------------ | ------------------- | ----------------------------------- | ----------------------------- |
| **Object Adapter** | Composition (has-a) | More flexible, can adapt subclasses | Needs to delegate all methods |
| **Class Adapter**  | Inheritance (is-a)  | Can override adaptee behavior       | Limited by single inheritance |

**In Java**, use Object Adapter (composition) because Java doesn't support multiple inheritance.

```java
// OBJECT ADAPTER (Preferred in Java)
class PayPalAdapter implements PaymentProcessor {
    private PayPalAPI paypal;  // Composition - has-a

    public PayPalAdapter(PayPalAPI paypal) {
        this.paypal = paypal;
    }
}

// CLASS ADAPTER (Possible in C++/Python with multiple inheritance)
class PayPalAdapter extends PayPalAPI implements PaymentProcessor {
    // Inherits from both - not possible in Java
}
```

---

## Common Mistakes to Avoid

1. **Modifying the adaptee** - The whole point is that you can't modify it
2. **Too much logic in adapter** - Adapter should only translate
3. **Confusing with Decorator** - Adapter changes interface, Decorator keeps it the same
4. **Creating unnecessary adapters** - If interfaces are compatible, don't add an adapter layer
5. **Adapter doing more than translation** - Each adapter should only adapt one interface

---

## Code Files

See the `code/` folder:

- `WithoutAdapter.java` - Shows the problems with direct integration
- `WithAdapter.java` - Full implementation with Payment Gateway example

---

## Real-World Use Cases

### 1. Payment Gateway Integration

| Component   | Example                                                                   |
| ----------- | ------------------------------------------------------------------------- |
| **Problem** | Multiple payment providers (PayPal, Stripe, Razorpay) have different APIs |
| **Target**  | `PaymentProcessor`                                                        |
| **Adaptee** | `PayPalAPI`, `StripeAPI`, `RazorpaySDK`                                   |
| **Adapter** | `PayPalAdapter`, `StripeAdapter`, `RazorpayAdapter`                       |

### 2. Database Migration

| Component   | Example                                                      |
| ----------- | ------------------------------------------------------------ |
| **Problem** | Switching from MySQL to MongoDB - different query interfaces |
| **Target**  | `UserRepository`                                             |
| **Adaptee** | `MongoClient`, `MySQLConnection`                             |
| **Adapter** | `MongoUserAdapter`, `MySQLUserAdapter`                       |

### 3. Third-Party Logging Libraries

| Component   | Example                                                                |
| ----------- | ---------------------------------------------------------------------- |
| **Problem** | Swapping logging frameworks (Log4j -> SLF4J) without changing app code |
| **Target**  | `Logger`                                                               |
| **Adaptee** | `Log4jLogger`, `SLF4JLogger`                                           |
| **Adapter** | `Log4jAdapter`, `SLF4JAdapter`                                         |

### 4. Legacy System Integration

| Component   | Example                                                 |
| ----------- | ------------------------------------------------------- |
| **Problem** | New microservice needs to consume old SOAP-based system |
| **Target**  | `OrderService` (REST-style)                             |
| **Adaptee** | `LegacySOAPOrderClient`                                 |
| **Adapter** | `SOAPToRESTOrderAdapter`                                |

### 5. Cloud Storage Providers

| Component   | Example                                                             |
| ----------- | ------------------------------------------------------------------- |
| **Problem** | Support AWS S3, Google Cloud Storage, Azure Blob with one interface |
| **Target**  | `FileStorage`                                                       |
| **Adaptee** | `S3Client`, `GCSClient`, `AzureBlobClient`                          |
| **Adapter** | `S3Adapter`, `GCSAdapter`, `AzureAdapter`                           |

### 6. External API Versioning

| Component   | Example                                                  |
| ----------- | -------------------------------------------------------- |
| **Problem** | Third-party API updated to v2, but app expects v1 format |
| **Target**  | `WeatherServiceV1`                                       |
| **Adaptee** | `WeatherAPIv2`                                           |
| **Adapter** | `WeatherV2ToV1Adapter`                                   |

---
