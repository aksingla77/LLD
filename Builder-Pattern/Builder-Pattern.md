# Builder Pattern

---

## Definition

The **Builder Pattern** is a creational design pattern that lets you **construct complex objects incrementally**. Instead of a massive constructor with many parameters, you set each field via named methods and finalize with `build()`.

**Key insight:** Builder does NOT enforce order — `builder.url("...").method("POST")` and `builder.method("POST").url("...")` produce the same result. The benefit is **readability and flexibility**, not sequencing.

---

## Type of Pattern

**Creational Pattern**

| Pattern Type   | Description                                                                                           |
| -------------- | ----------------------------------------------------------------------------------------------------- |
| **Creational** | Deals with object creation mechanisms, trying to create objects in a manner suitable to the situation |
| Structural     | Deals with how classes and objects are composed to form larger structures                             |
| Behavioral     | Deals with communication between objects and how they interact                                        |

---

## Key Terms

| Term                        | Definition                                                                  |
| --------------------------- | --------------------------------------------------------------------------- |
| **Product**                 | The complex object being built (`HttpRequest`)                              |
| **Builder**                 | Separate class that constructs the product (`HttpRequestBuilder`)           |
| **Director**                | Optional class that bundles common build configurations (`RequestDirector`) |
| **Method Chaining**         | Each setter returns `this`, enabling `.withUrl().withMethod().build()`      |
| **Telescoping Constructor** | Anti-pattern: many constructor overloads with increasing parameters         |

---

## Problem Statement

You're building an **HTTP Client Library**. Users need to create HTTP requests with:

> **HTTP Request:** A message sent by a client to a server over the HTTP protocol, containing a URL (the resource location), a method (the action to perform — GET to read, POST to create, etc.), optional headers (metadata like authentication tokens), and an optional body (data payload for POST/PUT requests).

- **URL** (required)
- **Method** – GET, POST, etc. (default: GET)
- **Headers** – Content-Type, Authorization (optional)
- **Body** – JSON payload (optional)
- **Timeout** – in ms (default: 30000)

How do you let users create `HttpRequest` objects cleanly with multiple optional fields?

---

## Approach 1: Single Large Constructor (Bad)

```java
public class HttpRequest {
    public HttpRequest(String url, String method, Map<String, String> headers,
                       String body, int timeout) { ... }
}

// Usage — forced to pass everything, even if you only need URL
HttpRequest req = new HttpRequest(
    "https://api.zerotechdebt.com",
    "GET",      // just want default
    null,       // no headers needed
    null,       // no body needed
    30000       // just want default
);
```

**Problems:**

- Must pass all parameters even when you only need one
- Order matters — easy to swap `body` and `method` (both Strings)
- Unreadable — what does each `null` and number mean?

---

## Approach 2: Telescoping Constructors (Bad)

```java
public class HttpRequest {
    public HttpRequest(String url) { ... }
    public HttpRequest(String url, String method) { ... }
    public HttpRequest(String url, String method, String body) { ... }
    public HttpRequest(String url, String method, String body, int timeout) { ... }
    // Need more combinations? Add more constructors...
}

// Usage
HttpRequest req = new HttpRequest("https://api.zerotechdebt.com", "POST");
```

**Problems:**

- **Explosion of overloads** — with N optional params, you need many constructors to cover useful combinations
- **Can't skip middle params** — want just URL + timeout? No constructor for that, must pass method and body too
- **Still unreadable** — longer constructors have the same positional args problem as Approach 1

---

## Approach 3: Setters / JavaBean Pattern (Bad)

```java
public class HttpRequest {
    private String url;
    private String method;
    private String body;
    private int timeout;

    public void setUrl(String url) { this.url = url; }
    public void setMethod(String method) { this.method = method; }
    public void setBody(String body) { this.body = body; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
}

// Usage
HttpRequest req = new HttpRequest();
req.setUrl("https://api.zerotechdebt.com");
req.setMethod("POST");

// Later, someone else mutates it
req.setUrl("https://evil.com");  // Object changed after creation!
```

**Problems:**

- Object is mutable — can be changed anytime
- No validation — object can exist without required fields
- Not thread-safe

---

## Approach 4: Builder Pattern (Good)

```java
HttpRequest req = new HttpRequestBuilder()
    .withUrl("https://api.zerotechdebt.com/users")
    .withMethod("POST")
    .withHeader("Content-Type", "application/json")
    .withBody("{\"name\": \"Akshit Singla\", \"age\": 25}")
    .withTimeout(5000)
    .build();

// req is IMMUTABLE — no setters exist!
```

**Benefits:**

- **Readable** — named methods, not positional args
- **Flexible** — set only what you need, skip the rest
- **Immutable** — can't be changed after `build()`
- **Validated** — `build()` checks required fields

---

**Flow:**

1. **Client** creates a `Builder`
2. **Client** calls `withX()` methods (each returns `this` for chaining)
3. **Client** calls `build()` which validates and creates the `Product`
4. **Product** is immutable — no setters, only getters

---

## Implementation

```java
// PRODUCT — immutable, package-private constructor
class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeout;

    // Package-private: accessible by HttpRequestBuilder (same package)
    HttpRequest(String url, String method, Map<String, String> headers, String body, int timeout) {
        this.url = url;
        this.method = method;
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
        this.timeout = timeout;
    }

    public void showRequest() {
        System.out.println("HttpRequest[" + method + " " + url + ", headers=" + headers
                + ", body=" + (body != null ? body : "none") + ", timeout=" + timeout + "ms]");
    }
}

// BUILDER — separate class, fluent (returns this)
class HttpRequestBuilder {
    private String url;
    private String method = "GET";
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private int timeout = 30000;

    public HttpRequestBuilder withUrl(String url) { this.url = url; return this; }
    public HttpRequestBuilder withMethod(String method) { this.method = method; return this; }
    public HttpRequestBuilder withHeader(String k, String v) { this.headers.put(k, v); return this; }
    public HttpRequestBuilder withBody(String body) { this.body = body; return this; }
    public HttpRequestBuilder withTimeout(int ms) { this.timeout = ms; return this; }

    public HttpRequest build() {
        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("URL is required");
        }
        return new HttpRequest(url, method, headers, body, timeout);
    }
}

// DIRECTOR — bundles common configurations (optional)
class RequestDirector {
    public HttpRequest simpleGet(String url) {
        return new HttpRequestBuilder().withUrl(url).build();
    }

    public HttpRequest jsonPost(String url, String json) {
        return new HttpRequestBuilder()
                .withUrl(url)
                .withMethod("POST")
                .withHeader("Content-Type", "application/json")
                .withBody(json)
                .build();
    }
}
```

**Three components:**

1. **Product** (`HttpRequest`) — immutable, no setters
2. **Builder** (`HttpRequestBuilder`) — separate class, each method returns `this`
3. **Director** (`RequestDirector`) — optional, bundles common configurations

---

## Director: When You Have Common Patterns

Director is useful when you have repeated configurations:

```java
RequestDirector director = new RequestDirector();

// Instead of repeating builder chains everywhere:
HttpRequest req1 = director.simpleGet("https://api.zerotechdebt.com/users");
HttpRequest req2 = director.jsonPost("https://api.zerotechdebt.com/users", "{\"name\":\"Akshit Singla\"}");
```

Without Director, you'd repeat the same `.withMethod("POST").withHeader("Content-Type", "application/json")` chain everywhere.

---

## Usage Examples

```java
// Direct builder usage — readable, flexible
HttpRequest req = new HttpRequestBuilder()
    .withUrl("https://api.zerotechdebt.com/users")
    .withMethod("POST")
    .withHeader("Content-Type", "application/json")
    .withBody("{\"name\": \"Akshit Singla\", \"age\": 25}")
    .withTimeout(5000)
    .build();

// Order doesn't matter — same result
HttpRequest req2 = new HttpRequestBuilder()
    .withTimeout(5000)
    .withBody("{\"name\": \"Akshit Singla\", \"age\": 25}")
    .withMethod("POST")
    .withUrl("https://api.zerotechdebt.com/users")
    .build();

// Missing URL? build() throws exception
new HttpRequestBuilder().withMethod("GET").build(); // IllegalStateException!
```

---

## Real-World Use Cases

### 1. Java's Built-in HttpClient (Java 11+)

```java
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.zerotechdebt.com/data"))
    .header("Accept", "application/json")
    .timeout(Duration.ofSeconds(10))
    .GET()
    .build();
```

This is literally the Builder Pattern in the Java standard library!

### 2. OkHttp (Android/Java HTTP Client)

```java
Request request = new Request.Builder()
    .url("https://api.github.com/users/octocat")
    .addHeader("Authorization", "Bearer token")
    .post(RequestBody.create(json, mediaType))
    .build();
```

Used by millions of Android apps. Same Builder pattern.

### 3. StringBuilder / StringBuffer

```java
String result = new StringBuilder()
    .append("Hello")
    .append(" ")
    .append("World")
    .append("!")
    .toString();  // "Hello World!"
```

The most common builder you've already been using without knowing!

### 4. Android Notification Builder

```java
Notification notification = new NotificationCompat.Builder(context, channelId)
    .setContentTitle("New Message")
    .setContentText("You have a new message from Akshit")
    .setSmallIcon(R.drawable.ic_notification)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setAutoCancel(true)
    .build();
```

Every Android notification is built using Builder pattern.

### 5. Test Data Builders (Unit Testing)

```java
User testUser = User.builder()
    .name("Akshit Singla")
    .email("akshit@zerotechdebt.com")
    .role("ADMIN")
    .active(true)
    .build();
```

Used heavily in testing frameworks to create test objects with specific configurations.

### 6. Lombok's @Builder (Java)

```java
@Builder
public class Pizza {
    private String size;
    private boolean cheese;
    private boolean pepperoni;
    private boolean mushrooms;
}

// Lombok auto-generates the builder!
Pizza pizza = Pizza.builder()
    .size("Large")
    .cheese(true)
    .pepperoni(true)
    .build();
```

Lombok eliminates the boilerplate — generates the entire Builder for you.

---

## When to Use Builder Pattern

| Situation                                                        | Use Builder? |
| ---------------------------------------------------------------- | ------------ |
| Object has **4+ parameters**, many optional                      | ✅ Yes       |
| You need **immutable objects**                                   | ✅ Yes       |
| Constructor calls are **unreadable** (`new X(a, b, null, c, 0)`) | ✅ Yes       |
| Object creation requires **validation**                          | ✅ Yes       |
| Object has **2-3 required fields**, no optional ones             | ❌ No        |
| Object is simple with few fields                                 | ❌ No        |

---

## Common Beginner Confusions

### ❓ "How is Builder different from Factory?"

This is the #1 confusion. They solve **completely different problems**.

| Aspect      | Factory Pattern                                          | Builder Pattern                                               |
| ----------- | -------------------------------------------------------- | ------------------------------------------------------------- |
| **Problem** | Which **type** of object to create?                      | How to **configure** a complex object?                        |
| **Focus**   | **Selecting** the right class                            | **Constructing** an object step by step                       |
| **Input**   | Usually a type identifier (`"email"`, `"sms"`)           | Multiple optional parameters set via method calls             |
| **Output**  | One of many **different classes** (EmailOTP, SMSOTP)     | **Same class**, different configurations                      |
| **When**    | You have a **family of related classes** and need one    | You have **one class** with many optional fields              |
| **Analogy** | Restaurant menu — "I want a **burger**" (picks the type) | Burger customization — "No onions, extra cheese, medium rare" |

**Example to drive it home:**

```java
// FACTORY: "Which type of notification?"
NotificationSender sender = NotificationFactory.create("email");
//  → Returns EmailSender, SMSSender, or PushSender (different classes)

// BUILDER: "How should this email notification be configured?"
EmailNotification email = EmailNotification.newBuilder()
    .to("user@zerotechdebt.com")
    .subject("Welcome!")
    .body("Thanks for signing up")
    .cc("akshit@zerotechdebt.com")
    .priority("HIGH")
    .build();
//  → Returns one EmailNotification with specific configuration
```

**They can even work together:**

```java
// Factory picks the TYPE, Builder configures the INSTANCE
NotificationSender sender = NotificationFactory.create("email");
Notification notification = Notification.newBuilder()
    .title("Hello")
    .body("World")
    .priority("HIGH")
    .build();
sender.send(notification);
```

---

### ❓ "How is Builder different from Setters?"

| Aspect              | Setters (JavaBean)                          | Builder                                            |
| ------------------- | ------------------------------------------- | -------------------------------------------------- |
| **Immutability**    | ❌ Object can be changed anytime            | ✅ Object is frozen after `build()`                |
| **Validation**      | ❌ No single validation point               | ✅ `build()` validates before creating             |
| **Thread Safety**   | ❌ Object can be mutated from other threads | ✅ Immutable objects are inherently thread-safe    |
| **Object State**    | ❌ Can exist in incomplete/invalid state    | ✅ Either fully built or not built at all          |
| **Required Fields** | ❌ No way to enforce required fields        | ✅ `build()` throws if required fields are missing |

---

### ❓ "Isn't Builder just too much boilerplate code?"

Yes! Here's how to handle it:

1. **Lombok** – `@Builder` annotation generates everything
2. **IDE Generation** – IntelliJ can auto-generate Builder classes
3. **Kotlin/TypeScript** – Named/default params reduce the need for Builder

The boilerplate is a tradeoff for safety — worth it for shared objects, libraries, and APIs.

---

### ❓ "When should I NOT use Builder?"

- **Simple objects** – `Point(int x, int y)` doesn't need a builder
- **All fields required** – If every field must be set, use a constructor
- **Languages with named params** – Kotlin, Python, Swift have cleaner alternatives

---

## Builder Pattern vs Others (Quick Reference)

| Pattern       | Problem It Solves                   | Key Phrase              |
| ------------- | ----------------------------------- | ----------------------- |
| **Builder**   | Complex object construction         | "Build it step by step" |
| **Factory**   | Which class to instantiate          | "Pick the right type"   |
| **Singleton** | Only one instance allowed           | "One and only one"      |
| **Decorator** | Add behavior to existing objects    | "Wrap and layer"        |
| **Prototype** | Creating copies of existing objects | "Clone it"              |

---

## Common Mistakes to Avoid

1. **Using Builder for simple objects** – A class with 2-3 fields doesn't need a builder. Just use a constructor.
2. **Forgetting validation in `build()`** – The whole point is to guarantee a valid object. Always validate required fields.
3. **Making the Product mutable** – If the Product has setters, you've defeated the purpose. Keep it immutable.
4. **Not providing defaults** – Optional fields should have sensible defaults in the Builder.
5. **Putting business logic in Builder** – Builder should only construct. Don't add business logic inside it.

---

## Code Files

See the `code/` folder:

- `WithoutBuilder.java` – Shows telescoping constructors, mutability, and validation problems
- `WithBuilder.java` – Fluent builder with `HttpRequestBuilder` and `RequestDirector` example

---
