# Singleton Pattern

## Definition

The **Singleton Pattern** is a creational design pattern that **restricts the instantiation of a class to a single instance** and provides a **global access point** to that instance. It ensures that no matter how many times you request the object, you always get the same instance, making it useful for managing shared resources like database connections, loggers, or configuration settings.

**In simple terms:** One class, one object, accessible everywhere.

---

## Type of Pattern

**Creational Pattern**

| Pattern Type | Description |
|--------------|-------------|
| **Creational** | Deals with object creation mechanisms, trying to create objects in a manner suitable to the situation. |
| Structural | Deals with how classes and objects are composed to form larger structures. |
| Behavioral | Deals with communication between objects and how they interact. |

---

## Problem Statement

You are building an application that needs to interact with a **database**. Multiple parts of your application (services, controllers, repositories) need database access, but you want:
- All components to share a **single database connection**
- Avoid opening multiple connections (which exhausts database resources)
- Ensure consistent connection configuration across the app

---

## Basic Implementation of Singleton Class in Java

```java
public class Singleton {
    
    private static Singleton instance;      // 1. Static instance
    
    private Singleton() {}                  // 2. Private constructor
    
    public static Singleton getInstance() { // 3. Public static access method
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```


> **That's it!** Three things: private static instance, private constructor, public static getInstance().

---


## UML Diagram: 


---

## Real-World Use Cases

- **Database Connection Pool** – Only one pool instance manages all database connections.
- **Logger** – A single logger instance writes logs across the entire application.
- **Configuration Manager** – One instance holds application-wide configuration settings.
- **Cache** – A single cache instance shared across the application.
- **Thread Pool** – One pool manages and reuses threads efficiently.

---

## Advanced: Thread-Safe Implementation

<details>
<summary><b>Click to expand (Optional Read)</b></summary>

### The Problem with Basic Implementation

The basic implementation is **not thread-safe**. If two threads call `getInstance()` simultaneously when `instance` is `null`, both might create separate instances.

### Solution 1: Synchronized Method

```java
public class Logger {
    private static Logger instance;
    
    private Logger() {}
    
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
}
```
**Drawback:** Synchronization overhead on every call.

---

### Solution 2: Double-Checked Locking (Recommended)

```java
public class Logger {
    private static volatile Logger instance;
    
    private Logger() {}
    
    public static Logger getInstance() {
        if (instance == null) {                    // First check (no locking)
            synchronized (Logger.class) {
                if (instance == null) {            // Second check (with locking)
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
}
```
**Benefit:** Synchronization only happens once during first creation.

---

### Solution 3: Bill Pugh Singleton (Best Practice)

```java
public class Logger {
    
    private Logger() {}
    
    // Inner static class - loaded only when getInstance() is called
    private static class LoggerHolder {
        private static final Logger INSTANCE = new Logger();
    }
    
    public static Logger getInstance() {
        return LoggerHolder.INSTANCE;
    }
}
```
**Benefit:** Thread-safe without synchronization overhead. Uses JVM's class loading mechanism.

</details>

---

## Why Not Global Objects?

### Global Object Approach

**Looks like it works:**
```java
// Global object approach
public class DatabaseConnection {
    public String host;
    public int port;

    public DatabaseConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }
}

// Create it once globally
public class AppConfig {
    public static final DatabaseConnection db = new DatabaseConnection("localhost", 5432);
}

// Use it everywhere
public class UserService {
    public void getUsers() {
        String server = AppConfig.db.host; // Works!
    }
}
```

**Why it still fails:**
```java
// Problem SOLVED by final: Reassignment blocked
// AppConfig.db = new DatabaseConnection("prod-server", 3306); // Compiler error!

// Problem NOT solved: Anyone can STILL create more instances!
DatabaseConnection db2 = new DatabaseConnection("localhost", 5433); // Still works!
DatabaseConnection db3 = new DatabaseConnection("localhost", 5434); // And again!

// Problem: No control over creation logic
// What if initialization needs validation or setup?

// Problem: Initialized at app startup, even if never used
```

**Summary:** Even with `final`, the public constructor allows anyone to create multiple instances. Only Singleton's **private constructor** truly prevents this.

---

### Why Singleton Wins

| Feature | Global Object | Singleton |
|---------|---------------|-----------|
| **Single Instance** | <span style="color:red">Anyone can create more </span> | <span style="color:green">Private constructor enforces it</span> |
| **Lazy Load** | <span style="color:red">Eager</span> | <span style="color:green">On demand</span> |
| **Testable** | <span style="color:red">Hard</span> | <span style="color:green">Easy to mock</span> |
| **Reassignment** | <span style="color:green">Cannot reassign (with `final`)</span> | <span style="color:green">Cannot reassign</span> |

**Key Takeaway:** `final` stops reassignment but doesn't prevent multiple instances. Only a private constructor does that.

---
