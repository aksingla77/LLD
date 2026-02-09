# Simple Factory Pattern

> **See also:** [Factory Patterns Overview](../Factory-Patterns-Glossary.md) for comparison with Factory Method and Abstract Factory.

---

## Definition

A **single class** with a static method that creates objects based on input parameters.

> ⚠️ **Note:** Simple Factory is technically not a GoF design pattern, but a common programming idiom.

**In simple terms:** Don't create objects yourself — ask a factory to create them for you.

---

## Key Terms (For Beginners)

<details>
<summary><b>Click to expand definitions</b></summary>

| Term               | Definition                                                                                                                                                                    | Example                                                  |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------- |
| **Interface**      | A contract that defines what methods a class must have, without implementing them. Think of it as a "promise" of capabilities.                                                | `OTPSender` interface promises a `sendOTP()` method      |
| **Concrete Class** | A regular class that provides actual implementation of methods. You can create objects from it using `new`.                                                                   | `EmailOTP`, `SMSOTP`, `WhatsAppOTP` are concrete classes |
| **Instantiation**  | The process of creating an object from a class using the `new` keyword.                                                                                                       | `new EmailOTP()` creates an instance                     |
| **Client Code**    | The code that uses your classes/objects. It's the "consumer" of your functionality.                                                                                           | `AuthService` is a client of the factory                 |
| **Coupling**       | How dependent one class is on another. **Tight coupling** = hard to change. **Loose coupling** = flexible and maintainable.                                                   | Directly using `new EmailOTP()` = tight coupling         |

</details>

---

## Problem Statement

**Junior Developer** built an **OTP (One-Time Password) library** for sending OTPs via different channels:

- **Email** – Connect to SMTP server with host/port config
- **SMS** – Call Twilio API with API key and SID
- **WhatsApp** – Use WhatsApp Business API with ID and token

**Senior Developer** is building an **AuthService** (authentication service) that needs to send OTPs during login, signup, password reset, and 2FA verification.

The problem:

- Senior shouldn't need to know the constructor details of each OTP class
- Senior shouldn't need to change code when Junior adds new channels (e.g., Telegram)
- Senior shouldn't break when Junior refactors OTP class internals

---

## Basic Implementation of Simple Factory Pattern in Java

```java
// Step 1: Common Interface
public interface OTPSender {
    void sendOTP(String otp);
}

// Step 2: Concrete Implementations
public class EmailOTP implements OTPSender {
    public void sendOTP(String otp) { /* Email logic */ }
}

public class SMSOTP implements OTPSender {
    public void sendOTP(String otp) { /* SMS logic */ }
}

// Step 3: The Factory
public class OTPFactory {

    public static OTPSender createOTPSender(String channel) {
        switch (channel) {
            case "email": return new EmailOTP();
            case "sms":   return new SMSOTP();
            default: throw new IllegalArgumentException("Unknown: " + channel);
        }
    }
}

// Step 4: Client Code (Senior's AuthService uses the factory)
public class AuthService {
    public void authenticate(String channel, String otp) {
        OTPSender sender = OTPFactory.createOTPSender(channel);
        sender.sendOTP(otp);
    }
}
```

> **That's it!** Four things: common interface, concrete implementations, factory class with static method, and client uses factory instead of `new`.

---

## UML Diagram

<!-- TODO: Add simple-factory-uml.png -->

---

## Real-World Use Cases

### 🏦 Payment Gateways (E-commerce)

```java
PaymentProcessor processor = PaymentFactory.create("stripe");
processor.processPayment(amount);
```

**Companies using this:** Razorpay, Stripe, PayPal integrations. When your app supports multiple payment methods (UPI, Cards, Wallets), the factory decides which processor to create based on user selection.

### 📧 Notification Services

```java
NotificationSender sender = NotificationFactory.create("push");
sender.send(message);
```

**Real example:** Apps like Swiggy/Zomato send notifications via Email, SMS, Push, or WhatsApp. The factory creates the right sender based on user preferences or notification type.

### 🗄️ Database Connections (JDBC)

```java
Connection conn = DriverManager.getConnection(url);
// Returns MySQLConnection, PostgresConnection, etc. based on URL
```

**Used by:** Every Java application using JDBC. The `DriverManager` is a classic Simple Factory that creates the appropriate database driver.

### 📝 Logging Frameworks (SLF4J, Log4j)

```java
Logger logger = LoggerFactory.getLogger(MyClass.class);
// Returns ConsoleLogger, FileLogger, CloudLogger based on config
```

**Used by:** Almost every Java application. The logging framework factory creates different loggers based on configuration.

### 📄 Document Parsers

```java
DocumentParser parser = ParserFactory.create("pdf");
Document doc = parser.parse(file);
```

**Real example:** Google Drive, Adobe, Microsoft Office. When you upload a file, the factory creates the right parser (PDF, DOCX, XLSX) based on file extension.

### 🎮 Game Development

```java
Enemy enemy = EnemyFactory.create("zombie");
// Returns Zombie, Vampire, Dragon based on level/type
```

**Used in:** Games create different enemy types, weapons, or power-ups dynamically based on game state.

### ☁️ Cloud Storage Providers

```java
StorageService storage = StorageFactory.create("aws");
storage.upload(file);
```

**Real example:** Applications supporting AWS S3, Google Cloud Storage, Azure Blob. Switch providers without changing business logic.

---

## Adding a New Type (The Power of Factory)

Want to add **Telegram OTP**? **Only 2 changes needed:**

### 1. Create the new class

```java
public class TelegramOTP implements OTPSender {
    @Override
    public void sendOTP(String otp) {
        // Telegram Bot API logic here
        System.out.println("✈️ Sent OTP via Telegram: " + otp);
    }
}
```

### 2. Update the factory (just 1 line!)

```java
public class OTPFactory {

    public static OTPSender createOTPSender(String channel) {
        switch (channel) {
            case "email":    return new EmailOTP();
            case "sms":      return new SMSOTP();
            case "whatsapp": return new WhatsAppOTP();
            case "telegram": return new TelegramOTP();  // ✅ Just add this!
            default:
                throw new IllegalArgumentException("Unknown channel: " + channel);
        }
    }
}
```

**Client code (`AuthService`) remains unchanged!** This is the **Open/Closed Principle** in action.

---

## Factory Pattern vs Direct Instantiation

| Aspect                    | Direct `new`                   | Factory Pattern              |
| ------------------------- | ------------------------------ | ---------------------------- |
| **Coupling**              | Tight (knows concrete classes) | Loose (knows only interface) |
| **Adding New Types**      | Modify multiple places         | Modify only factory          |
| **Testability**           | Hard to mock                   | Easy to mock/substitute      |
| **Single Responsibility** | Creation logic scattered       | Creation logic centralized   |
| **Open/Closed Principle** | Violates                       | Follows                      |

---

## When to Use

- Single product type with multiple variants
- Creation logic is simple and centralized
- You want to hide concrete classes from clients

---

## Common Mistakes to Avoid

1. **Overusing Factory Pattern** – Don't use it when direct instantiation is simpler and sufficient.
2. **Exposing Concrete Classes** – If clients can still access concrete classes directly, the factory loses its purpose.
3. **God Factory** – Don't put all object creation in one massive factory. Group related objects.
4. **Forgetting Default Cases** – Always handle unknown types gracefully.

---

## Code Files

Check out the complete working examples in the `code/` folder:

- `without-factory/` – Tightly coupled approach
- `with-factory/` – Clean factory pattern implementation

---
