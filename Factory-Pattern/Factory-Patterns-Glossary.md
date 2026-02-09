# Factory Patterns - Overview & Comparison

> **Navigation:** This is the overview document. For detailed explanations and code, see the individual pattern guides linked below.

---

## Core Terminology

| Term                 | Definition                                                                                                    | Example                                                              |
| -------------------- | ------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------- |
| **Product**          | The object being created. Defined by an interface or abstract class.                                          | `OTPSender` interface                                                |
| **Concrete Product** | Actual implementations of the Product interface.                                                              | `EmailOTP`, `SMSOTP`, `WhatsAppOTP`                                  |
| **Creator**          | The class responsible for creating products. May be a factory class or an abstract class with factory method. | `OTPFactory` (Simple), `OTPService` (Factory Method)                 |
| **Concrete Creator** | Subclass that implements the factory method to create specific products.                                      | `EmailOTPService`, `SMSOTPService`                                   |
| **Factory**          | A class/method that encapsulates object creation logic.                                                       | `OTPFactory.createOTPSender()`                                       |
| **Factory Method**   | A method (usually abstract) that subclasses override to create products.                                      | `createSender()` in `OTPService`                                     |
| **Abstract Factory** | An interface for creating **families** of related products.                                                   | `OTPFactory` with `createSMS()`, `createEmail()`, `createWhatsApp()` |
| **Client**           | Code that uses products but doesn't create them directly.                                                     | `AuthService` in our examples                                        |

---

## The Three Factory Patterns

| Pattern                                                                    | Description                       | Use When                                     |
| -------------------------------------------------------------------------- | --------------------------------- | -------------------------------------------- |
| [Simple Factory](./Simple-Factory-Pattern/Simple-Factory-Pattern.md)       | One factory class with switch/if  | Single product type, simple creation logic   |
| [Factory Method](./Factory-Method-Pattern/Factory-Method-Pattern.md)       | Inheritance-based creation        | Subclasses need to control creation          |
| [Abstract Factory](./Abstract-Factory-Pattern/Abstract-Factory-Pattern.md) | Factory of factories for families | Multiple related products that work together |

### UML Overview

<!-- TODO: Add factory-patterns-overview.png -->

---

## Comparison Table

| Aspect              | Simple Factory               | Factory Method                | Abstract Factory                |
| ------------------- | ---------------------------- | ----------------------------- | ------------------------------- |
| **Creates**         | One product type             | One product type              | Family of products              |
| **How**             | Static method with switch/if | Abstract method in subclasses | Interface with multiple methods |
| **Uses**            | Composition                  | Inheritance                   | Composition                     |
| **Products**        | Single interface             | Single interface              | Multiple interfaces             |
| **Adding Products** | Modify factory               | Create new subclass           | Create new factory              |
| **Complexity**      | Low                          | Medium                        | High                            |
| **GoF Pattern?**    | No (idiom)                   | Yes                           | Yes                             |

---

## Quick Code Comparison

```java
// SIMPLE FACTORY - One factory, switch statement
OTPSender sender = OTPFactory.create("email");
sender.sendOTP(otp);

// FACTORY METHOD - Inheritance, subclass creates its product
OTPService service = new EmailOTPService();
service.sendOTP(otp);  // internally calls createSender()

// ABSTRACT FACTORY - Factory creates family of products
OTPFactory factory = new IndiaOTPFactory();
factory.createSMSSender().sendSMS(otp);
factory.createEmailSender().sendEmail(otp);
// All products work together (same region)
```

---

## Decision Guide

```
                    START
                      │
                      ▼
         ┌────────────────────────┐
         │ Do you need FAMILIES   │
         │ of related products?   │
         └────────────────────────┘
                 │           │
                YES          NO
                 │           │
                 ▼           ▼
        ┌───────────┐  ┌─────────────────────┐
        │ ABSTRACT  │  │ Does creator have   │
        │ FACTORY   │  │ other behavior      │
        └───────────┘  │ besides creating?   │
                       └─────────────────────┘
                              │           │
                             YES          NO
                              │           │
                              ▼           ▼
                       ┌───────────┐  ┌───────────┐
                       │ FACTORY   │  │ SIMPLE    │
                       │ METHOD    │  │ FACTORY   │
                       └───────────┘  └───────────┘
```

---

## Key Takeaways

| Pattern          | One-Liner Summary                                                                    |
| ---------------- | ------------------------------------------------------------------------------------ |
| Simple Factory   | "Give me one product based on this key" → `OTPFactory.create("email")`               |
| Factory Method   | "Subclasses decide what to create" → `EmailOTPService.createSender()`                |
| Abstract Factory | "Create families that work together" → `IndiaOTPFactory` creates all Indian services |

---
