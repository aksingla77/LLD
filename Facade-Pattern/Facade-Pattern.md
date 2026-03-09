# Facade Pattern

---

## Definition

The **Facade Pattern** is a structural design pattern that provides a **simplified interface** to a complex subsystem. It hides the complexity of multiple classes behind a single, easy-to-use class.

**Type:** Structural Pattern

---

## Key Terms

| Term          | Definition                                                               |
| ------------- | ------------------------------------------------------------------------ |
| **Facade**    | A single class that provides a simple interface to a complex subsystem   |
| **Subsystem** | A collection of classes/components with complex interactions             |
| **Client**    | The code that uses the Facade instead of dealing with subsystem directly |

---

## When to Use Facade Pattern

1. **Complex subsystem**: You have multiple classes working together, and clients need to coordinate them
2. **Simplify API**: You want to provide a simple interface to a library or framework
3. **Decoupling**: You want to reduce dependencies between clients and subsystem classes
4. **Legacy code**: You need a clean interface over messy/legacy code

---

## Problem Statement

You're building an **Order Processing System** for an e-commerce platform. When a customer places an order, the system must:

1. Check inventory for stock
2. Process payment
3. Generate invoice
4. Schedule shipping
5. Send confirmation email

Without a facade, the client must know about and coordinate all these subsystems.

---

## Without Facade (Bad)

Client must manage all subsystem components directly:

```java
public void placeOrder(Order order) {
    inventoryService.checkStock(order.getProductId());
    paymentService.processPayment(order.getPaymentDetails());
    invoiceService.generateInvoice(order.getId());
    shippingService.scheduleDelivery(order.getAddress());
    notificationService.sendConfirmation(order.getEmail());
}
```

**Problems:**

- Client tightly coupled to all subsystem classes
- Complex coordination logic exposed to client
- Changes in subsystem affect all clients

<img width="1536" height="537" alt="Screenshot 2026-03-09 130258" src="https://github.com/user-attachments/assets/2383ec95-9900-4d5f-a8ba-3ebdd56a955f" />

---

## With Facade (Good)

Facade hides all complexity:

```java
public void placeOrder(Order order) {
    orderFacade.placeOrder(order);  // One simple call!
}
```

**Benefits:**

- Simple, clean interface
- Client doesn't know about subsystem internals
- Easy to change subsystem without affecting clients

<img width="1562" height="790" alt="Screenshot 2026-03-09 130150" src="https://github.com/user-attachments/assets/1fb14d06-8ee1-42f3-b507-e6f2b9dc27c0" />


---

## Class Diagram

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ uses
       ▼
┌─────────────────────┐
│    OrderFacade      │
│  ─────────────────  │
│  + placeOrder()     │
│  + cancelOrder()    │
└─────────┬───────────┘
          │ delegates to
          ▼
┌───────────────────────────────────────────────────────────┐
│                       Subsystem                           │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌──────┐ │
│  │Inventory│ │ Payment │ │ Invoice │ │Shipping │ │Email │ │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └──────┘ │
└───────────────────────────────────────────────────────────┘
```

---

## Facade vs Other Patterns

| Pattern     | Key Difference                                                               |
| ----------- | ---------------------------------------------------------------------------- |
| **Factory** | Creates objects. Facade doesn't create — it **coordinates existing** objects |
| **Adapter** | Converts one interface to another. Facade **simplifies**, doesn't convert    |
| **Proxy**   | Controls access to one object. Facade **wraps multiple** objects             |

**Quick Test:** Ask yourself:

- Am I **creating** objects? → Factory
- Am I **converting** an interface? → Adapter
- Am I **simplifying** multiple classes into one call? → **Facade**

---

## Real-World Examples

### 1. Computer Startup (Everyday Example)

Press power button → Computer does 10+ things internally (BIOS, RAM check, OS load, drivers, services...)

**Power button = Facade.** One action, complex sequence hidden.

---

### 2. Spring JdbcTemplate (Database)

**Without Facade (Raw JDBC):**

```java
Connection conn = dataSource.getConnection();
PreparedStatement stmt = conn.prepareStatement("SELECT name FROM users WHERE id = ?");
stmt.setInt(1, userId);
ResultSet rs = stmt.executeQuery();
String name = rs.next() ? rs.getString("name") : null;
rs.close(); stmt.close(); conn.close();  // Must close in order, each can throw exception!
```

**With Facade:**

```java
String name = jdbcTemplate.queryForObject("SELECT name FROM users WHERE id = ?", String.class, userId);
```

**6+ lines → 1 line.** Connection, statement, result handling, cleanup — all hidden.

---

### 3. Email Service

**Without Facade:**

```java
SMTPConnection smtp = new SMTPConnection("smtp.gmail.com", 587);
smtp.authenticate(username, password);
smtp.setTLS(true);
MimeMessage msg = new MimeMessage();
msg.setFrom("sender@gmail.com");
msg.setTo("recipient@gmail.com");
msg.setSubject("Hello");
msg.setBody("Welcome!");
smtp.send(msg);
smtp.close();
```

**With Facade:**

```java
emailService.send("recipient@gmail.com", "Hello", "Welcome!");
```

Facade hides: SMTP connection, authentication, TLS setup, message formatting, cleanup.

---

### 4. AWS SDK

**Without:** Manual HTTP requests, credential signing, retries, XML parsing...

**With Facade:**

```java
s3Client.putObject("bucket", "file.txt", new File("local.txt"));  // One line
```
