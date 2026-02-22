# Decorator Pattern

---

## Definition

The **Decorator Pattern** is a structural design pattern that lets you **add new behaviors to objects dynamically** by wrapping them in decorator objects. Instead of modifying the original class or creating subclasses for every combination, you wrap objects in layers that add functionality.

**Key insight:** Decorator **wraps** an existing object and **delegates** to it while adding behavior. You can stack multiple decorators: `new EncryptedStream(new CompressedStream(new FileStream()))` - each layer adds its behavior.

---

## Type of Pattern

**Structural Pattern**

| Pattern Type   | Description                                                                                           |
| -------------- | ----------------------------------------------------------------------------------------------------- |
| Creational     | Deals with object creation mechanisms, trying to create objects in a manner suitable to the situation |
| **Structural** | Deals with how classes and objects are composed to form larger structures                             |
| Behavioral     | Deals with communication between objects and how they interact                                        |

---

## Key Terms

| Term                   | Definition                                                                                 |
| ---------------------- | ------------------------------------------------------------------------------------------ |
| **Component**          | The interface or abstract class defining the object that can be decorated (`Drink`)        |
| **Concrete Component** | The original object being decorated (`Whiskey`, `Vodka`)                                   |
| **Decorator**          | Abstract class that wraps a component and implements the same interface (`DrinkDecorator`) |
| **Concrete Decorator** | Specific decorator that adds behavior (`IceDecorator`, `CokeDecorator`, `LimeDecorator`)   |
| **Wrapping**           | The act of putting an object inside another object that delegates to it                    |

---

## Problem Statement

You're building a **Bar Ordering System**. In a real bar, there are **two distinct phases**:

### Phase 1: Pouring the Base Spirit (By the Bartender)

The bartender pours the base drink with specific configuration:

- **Spirit Type** - Whiskey, Vodka, Rum, Gin, Tequila
- **Brand** - Jack Daniels, Grey Goose, Bacardi, Tanqueray
- **Pour Size** - Single ($5.00), Double ($9.00)
- **Quality Tier** - Well ($4.00), Premium ($7.00), Top-shelf ($12.00)

Once poured, these properties are **FIXED**. You cannot change Whiskey into Vodka. You cannot change the brand after pouring. This is **construction** - configured once at creation time. _(This is where **Builder Pattern** shines)_

### Phase 2: Adding Mixers & Extras (At the Bar)

After the spirit is poured, the customer can add mixers and extras:

- **Ice** - +$0.00 (can add multiple times for "extra ice")
- **Coke/Cola** - +$1.00
- **Soda/Tonic** - +$1.00
- **Lime Wedge** - +$0.50
- **Orange Juice** - +$1.50

The customer decides these **at runtime**, can add **multiple of the same** (extra ice, double lime), and can even add more **later** ("actually, add some lime too"). This is **extension** - dynamic add-ons to an existing object. _(This is where **Decorator Pattern** shines)_

---

### The Problem We're Solving Here

**Focus: Phase 2 - Adding Mixers & Extras**

How do you handle drinks with dynamic add-ons (ice, coke, lime, etc.) in your code?

Let's see what approaches developers commonly think of - and why they fail.

---

## Approach 1: Subclass for Every Combination (Bad)

A junior developer's first thought: "Each configuration is a type, so let's create classes!"

```java
class Whiskey { }
class WhiskeyWithIce extends Whiskey { }
class WhiskeyWithCoke extends Whiskey { }
class WhiskeyWithIceAndCoke extends Whiskey { }
class WhiskeyWithIceAndLime extends Whiskey { }
class WhiskeyWithIceAndCokeAndLime extends Whiskey { }
class WhiskeyWithIceAndCokeAndLimeAndSoda extends Whiskey { }
// ... explosion continues!
```

**Problems:**

- **Class explosion** - N add-ons = 2^N classes
- **Compile-time only** - Can't add to an existing object at runtime
- **No multiples** - Can't do "extra ice" without more classes

---

## Approach 2: Boolean Flags in the Class (Bad)

Okay, so we need runtime flexibility. What if we add flags to the Whiskey class?

```java
class Whiskey {
    private boolean hasIce;
    private boolean hasCoke;
    private boolean hasLime;
    private boolean hasSoda;

    public void addIce() { this.hasIce = true; }
    public void addCoke() { this.hasCoke = true; }

    public double getCost() {
        double cost = 5.00;  // base spirit
        if (hasIce) cost += 0.00;
        if (hasCoke) cost += 1.00;
        if (hasLime) cost += 0.50;
        if (hasSoda) cost += 1.00;
        return cost;
    }
}
```

**Problems:**

- **Violates OCP** - New mixer = modify class
- **No multiples** - Boolean can't represent "extra ice"
- **Doesn't extend** - What about Vodka? Duplicate all this code, or make a parent class that still violates OCP

---

## Approach 3: Decorator Pattern (Good)

The key insight: **wrap the existing object** instead of replacing it or modifying its class.

```java
Drink order = new Whiskey();                // Bartender pours whiskey
order = new IceDecorator(order);            // Wrap it with ice - SAME object reference!
order = new CokeDecorator(order);           // Wrap again with coke
order = new LimeDecorator(order);           // One more wrap

System.out.println(order.getDescription()); // "Whiskey, Ice, Coke, Lime"
System.out.println(order.getCost());        // 6.50
```

**How this solves the "add to existing object" problem:**

- **Works with existing object** - The original `Whiskey` is inside, wrapped in layers
- **Add more anytime** - `order = new LimeDecorator(order);` - just wrap again!
- **Extra ice** - `new IceDecorator(new IceDecorator(order))` - wrap twice!
- **Open for extension** - New mixer? Create a new decorator class. Drink class unchanged.
- **Single Responsibility** - Each decorator handles ONE mixer
- **Runtime decisions** - Customer decides at the bar, not at compile time

---

**Flow:**

1. **Client** creates a **Concrete Component** (Whiskey, Vodka, Rum)
2. **Client** wraps it in one or more **Decorators** (IceDecorator, CokeDecorator, LimeDecorator)
3. **Client** calls methods on the outermost wrapper
4. Each **Decorator** adds its behavior, then **delegates** to the wrapped object

---

## UML Diagram (Standard)

<img width="2374" height="1571" alt="Standard UML Decorator" src="https://github.com/user-attachments/assets/43b7028c-42b4-4719-9e64-56703e68a4f2" />


---

## UML Diagram (Bar Example)

<img width="2531" height="1891" alt="Example UML Decorator" src="https://github.com/user-attachments/assets/74c419c7-0913-4099-898f-6362bf476cef" />


---

## Implementation

```java
// COMPONENT - interface defining the contract
interface Drink {
    double getCost();
    String getDescription();
}

// CONCRETE COMPONENT - the base object being decorated
class Whiskey implements Drink {
    @Override
    public double getCost() {
        return 5.00;
    }

    @Override
    public String getDescription() {
        return "Whiskey";
    }
}

// DECORATOR - abstract class wrapping a Drink
abstract class DrinkDecorator implements Drink {
    protected Drink drink;  // the wrapped object

    public DrinkDecorator(Drink drink) {
        this.drink = drink;
    }

    @Override
    public double getCost() {
        return drink.getCost();  // delegate to wrapped object
    }

    @Override
    public String getDescription() {
        return drink.getDescription();  // delegate to wrapped object
    }
}

// CONCRETE DECORATORS - each adds specific behavior
class IceDecorator extends DrinkDecorator {
    public IceDecorator(Drink drink) {
        super(drink);
    }

    @Override
    public double getCost() {
        return drink.getCost() + 0.00;  // ice is free
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + ", Ice";
    }
}

class CokeDecorator extends DrinkDecorator {
    public CokeDecorator(Drink drink) {
        super(drink);
    }

    @Override
    public double getCost() {
        return drink.getCost() + 1.00;
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + ", Coke";
    }
}

class LimeDecorator extends DrinkDecorator {
    public LimeDecorator(Drink drink) {
        super(drink);
    }

    @Override
    public double getCost() {
        return drink.getCost() + 0.50;
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + ", Lime";
    }
}
```

**Four components:**

1. **Component** (`Drink`) - interface that both original and decorators implement
2. **Concrete Component** (`Whiskey`) - the object being decorated
3. **Decorator** (`DrinkDecorator`) - abstract wrapper that delegates to wrapped object
4. **Concrete Decorators** (`IceDecorator`, `CokeDecorator`, `LimeDecorator`) - add specific behaviors

---

## Usage Examples

```java
// The spirit is already poured by the bartender (assume Whiskey for now)
// Customer adds mixers at the bar using decorators

// Neat whiskey - no mixers
Drink order1 = new Whiskey();
System.out.println(order1.getDescription() + " $" + order1.getCost());
// Output: Whiskey $5.0

// Whiskey with ice and coke (classic)
Drink order2 = new CokeDecorator(new IceDecorator(new Whiskey()));
System.out.println(order2.getDescription() + " $" + order2.getCost());
// Output: Whiskey, Ice, Coke $6.0

// Whiskey with EXTRA ICE - just wrap twice!
Drink order3 = new IceDecorator(new IceDecorator(new Whiskey()));
System.out.println(order3.getDescription() + " $" + order3.getCost());
// Output: Whiskey, Ice, Ice $5.0

// Customer changes mind mid-order: "Add some lime too!"
Drink order4 = new Whiskey();
order4 = new IceDecorator(order4);
order4 = new CokeDecorator(order4);
// Customer: "Actually, add some lime too"
order4 = new LimeDecorator(order4);  // No problem! Just wrap again.
System.out.println(order4.getDescription() + " $" + order4.getCost());
// Output: Whiskey, Ice, Coke, Lime $6.50
```

**Key observations:**

- Extra ice? Just wrap twice with `IceDecorator`
- Customer changes mind? Just wrap with another decorator
- This is **impossible** with Builder - once built, you can't add more!

---

## Real-World Use Cases

### 1. Java I/O Streams (Classic Example)

```java
InputStream in = new DataInputStream(           // Adds methods to read primitives (int, double)
    new BufferedInputStream(                    // Adds buffering for faster reads
        new GZIPInputStream(                    // Adds decompression
            new FileInputStream("data.gz"))));  // Base: reads bytes from file
```

Each wrapper adds one capability. Want compression? Wrap with `GZIPInputStream`. Want buffering? Wrap with `BufferedInputStream`. Stack as needed!

### 2. Java Collections

```java
List<String> list = new ArrayList<>();

// Wrap to make read-only
List<String> readOnly = Collections.unmodifiableList(list);

// Wrap to make thread-safe
List<String> threadSafe = Collections.synchronizedList(list);
```

Same list, different behaviors added by wrapping.

### 3. Python Decorators

```python
@cache              # Wrap: cache return values
@log_time           # Wrap: log how long it takes
def fetch_data(url):
    return requests.get(url)

# Same as: fetch_data = cache(log_time(fetch_data))
```

Python's `@decorator` syntax is literally this pattern built into the language!

### 4. Logging/Monitoring Wrappers

```java
// Base service
UserService service = new UserServiceImpl();

// Wrap with logging
service = new LoggingDecorator(service);      // Logs all method calls

// Wrap with metrics
service = new MetricsDecorator(service);      // Tracks response times

// Wrap with retry
service = new RetryDecorator(service);        // Retries on failure
```

Each decorator adds cross-cutting behavior without modifying the original service.

---

## When to Use Decorator Pattern

| Situation                                                    | Use Decorator? |
| ------------------------------------------------------------ | -------------- |
| Need to **add responsibilities dynamically** at runtime      | Yes            |
| Have **many independent features** that can be combined      | Yes            |
| Want to avoid **class explosion** from subclass combinations | Yes            |
| Need to **extend behavior without modifying** existing code  | Yes            |
| Behavior is **fixed at compile time**, not runtime           | No             |
| There's **only one or two** possible extensions              | No             |

---

## Common Beginner Confusions

### "How is Decorator different from Builder?"

This is a common confusion because both seem to "add things" to an object. But they solve **completely different problems** at **completely different times**.

**Using Our Bar Example:**

| Aspect           | Builder Pattern (Pouring the Drink)                     | Decorator Pattern (Adding Mixers)                    |
| ---------------- | ------------------------------------------------------- | ---------------------------------------------------- |
| **Pattern Type** | Creational                                              | Structural                                           |
| **When Used**    | **Before** the object exists (bartender pouring spirit) | **After** the object exists (customer adding mixers) |
| **What It Does** | **Constructs** the object with configuration            | **Wraps** existing object with new behaviors         |
| **Mutability**   | Once built, configuration is FIXED                      | Can add/remove decorators anytime                    |
| **Real Action**  | Choose whiskey vs vodka, brand, pour size, quality      | Add ice, coke, lime to existing drink                |
| **Reversible?**  | Can't unpour whiskey or change it to vodka              | Can "unwrap" (conceptually remove a mixer)           |

---

### "Why can't Builder handle the mixers (ice, coke, etc.)?"

Imagine trying to use Builder for mixers:

```java
// BUILDER APPROACH FOR MIXERS - This is WRONG!
Drink drink = new DrinkBuilder()
    .withSpirit("Whiskey")
    .withBrand("Jack Daniels")
    .withIce()            // Problem: What if customer wants EXTRA ice?
    .withIce()            // Calling twice doesn't make sense in Builder
    .withCoke()
    .build();

// Customer later says: "Actually, add some lime too"
// TOO LATE! Object is already built and immutable!
```

**Problems with Builder for mixers:**

1. **One-shot creation** - Builder creates an immutable object ONCE. Can't add more later.
2. **No "extra" concept** - `withIce().withIce()` doesn't naturally mean "extra ice"
3. **Must know everything upfront** - All decisions made before `build()` is called
4. **Customer interaction** - Customer decides mixers AFTER seeing the drink, not before

**Decorator handles this naturally:**

```java
// DECORATOR APPROACH FOR MIXERS - This is RIGHT!
Drink drink = new Whiskey();  // Already poured by bartender

// Customer adds mixers at the bar
drink = new IceDecorator(drink);
drink = new IceDecorator(drink);    // Extra ice! Just wrap again!
drink = new CokeDecorator(drink);

// Customer changes mind: "Add lime too"
drink = new LimeDecorator(drink); // No problem, wrap again!
```

---

### "Why can't Decorator replace Builder for pouring the drink?"

Imagine trying to use Decorator to configure the drink itself:

```java
// DECORATOR APPROACH FOR POURING - This is WRONG!
Drink drink = new BaseDrink();              // Just a generic drink?
drink = new WhiskeyDecorator(drink);        // This doesn't make sense!
drink = new JackDanielsDecorator(drink);    // Brand is inherent, not external
drink = new DoubleShotDecorator(drink);     // Pour size is fixed at creation
drink = new TopShelfDecorator(drink);       // Quality tier is inherent!
```

**Problems with Decorator for construction:**

1. **Decorator assumes object EXISTS** - You can't decorate something that doesn't exist yet
2. **Core identity vs add-ons** - Whiskey IS fundamentally different from Vodka. This isn't a "layer"
3. **Decorators are reversible** - But you can't "unpour" whiskey or change Jack Daniels to Grey Goose
4. **Configuration vs extension** - Spirit type, brand, pour size are CONFIGURATION, not behaviors to add

**Builder handles this naturally:**

```java
// BUILDER APPROACH FOR POURING - This is RIGHT!
Drink drink = new DrinkBuilder()
    .withSpirit("Whiskey")          // Fundamental identity
    .withBrand("Jack Daniels")      // Fixed at creation
    .withPourSize("Double")         // Can't change after poured
    .withQuality("Top-shelf")       // Inherent to the product
    .build();
```

---

### "The Golden Rule - When to Use Which?"

| Question to Ask                                         | Answer → Pattern |
| ------------------------------------------------------- | ---------------- |
| Does this define **WHAT the object IS**?                | **Builder**      |
| Does this **ADD SOMETHING** to an existing object?      | **Decorator**    |
| Is the decision made **BEFORE** the object exists?      | **Builder**      |
| Is the decision made **AFTER** the object exists?       | **Decorator**    |
| Is it **REVERSIBLE** (can be undone/removed)?           | **Decorator**    |
| Is it **PERMANENT** (part of the object's identity)?    | **Builder**      |
| Can you have **MULTIPLE of the same** (double, triple)? | **Decorator**    |
| Is it **CONFIGURATION** (options during creation)?      | **Builder**      |
| Is it **EXTENSION** (behaviors added later)?            | **Decorator**    |

**Bar Cheat Sheet:**

| Decision                      | Pattern   | Why                                 |
| ----------------------------- | --------- | ----------------------------------- |
| Whiskey vs Vodka vs Rum       | Builder   | Defines what the drink IS           |
| Jack Daniels vs Grey Goose    | Builder   | Brand is inherent, can't swap after |
| Single vs Double shot         | Builder   | Pour size fixed at creation         |
| Well vs Premium vs Top-shelf  | Builder   | Quality tier is inherent            |
| Add ice                       | Decorator | Added to existing drink             |
| Add coke                      | Decorator | Customer decides at the bar         |
| Add extra ice                 | Decorator | Wrap twice!                         |
| Add lime wedge                | Decorator | Can be added/removed (take it out)  |
| "Actually, add some soda too" | Decorator | Dynamic decision after drink exists |

---

### "Can Builder and Decorator work together?"

**YES! They're complementary, not competing!**

```java
// PHASE 1: Builder constructs the base drink (what the bartender pours)
Drink whiskey = new DrinkBuilder()
    .withSpirit("Whiskey")
    .withBrand("Jack Daniels")
    .withPourSize("Double")
    .withQuality("Top-shelf")
    .build();

// PHASE 2: Decorator adds mixers (what the customer requests at the bar)
Drink customerOrder = new LimeDecorator(
    new CokeDecorator(
        new IceDecorator(
            new IceDecorator(whiskey)  // Extra ice
        )
    )
);

System.out.println(customerOrder.getDescription());
// Output: Double Top-shelf Jack Daniels Whiskey, Ice, Ice, Coke, Lime

System.out.println(customerOrder.getCost());
// Output: $11.50 (Double Top-shelf $9 + Ice $0 + Ice $0 + Coke $1 + Lime $0.50)
```

**The patterns handle different phases of the object's lifecycle:**

1. **Builder** → Birth (construction with configuration)
2. **Decorator** → Life (extension with behaviors)

---

### "How is Decorator different from Inheritance?"

| Aspect           | Inheritance                              | Decorator                                   |
| ---------------- | ---------------------------------------- | ------------------------------------------- |
| **Binding Time** | Compile time - fixed in class definition | Runtime - can be changed dynamically        |
| **Flexibility**  | Static - can't change after compilation  | Dynamic - add/remove decorators at runtime  |
| **Combinations** | Class explosion for every combination    | Combine freely by stacking decorators       |
| **Coupling**     | Tight - subclass depends on parent       | Loose - decorator only depends on interface |
| **Single Point** | One behavior per subclass                | Each decorator handles one responsibility   |

---

## Composition Over Inheritance

Decorator pattern is a textbook example of **"favor composition over inheritance"** - a core OOP principle.

**Inheritance says:** "A WhiskeyWithCoke **IS-A** Whiskey"
**Composition says:** "A CokeDecorator **HAS-A** Drink (and adds to it)"

```java
// Inheritance - rigid, compile-time
class WhiskeyWithCoke extends Whiskey { }           // IS-A relationship
class WhiskeyWithCokeAndLime extends WhiskeyWithCoke { }  // Explosion begins...

// Composition - flexible, runtime
Drink order = new CokeDecorator(whiskey);     // HAS-A relationship
order = new LimeDecorator(order);             // Just wrap again!
```

**Why composition wins here:**

| Problem            | Inheritance                   | Composition (Decorator)  |
| ------------------ | ----------------------------- | ------------------------ |
| Add new feature    | Create new subclass hierarchy | Create one new decorator |
| Combine N features | 2^N classes                   | N decorator classes      |
| Change at runtime  | Impossible                    | Wrap/unwrap anytime      |
| Test in isolation  | Needs parent class            | Mock the interface       |

**Rule of thumb:** Use inheritance for **"is-a"** relationships (Dog is an Animal). Use composition/decorator for **"has-a"** or **"can-do"** relationships (Drink has coke, Stream can compress).

---

### "Isn't wrapping objects inefficient?"

For most applications, the overhead is negligible:

- **Modern JVMs** optimize method calls heavily
- **Memory per wrapper** is tiny (just one reference)
- **Benefits outweigh cost** - flexibility, maintainability, testability

Only avoid excessive wrapping in **performance-critical code** (gaming engines, high-frequency trading).

---

### "When should I NOT use Decorator?"

- **Fixed behavior** - If behavior never changes at runtime, use simpler approaches
- **Too many decorators** - If you have 20+ decorator layers, consider redesigning
- **Simple extensions** - If there's only one or two extensions, inheritance might be cleaner
- **Order-sensitive** - If decorator order matters a lot, it can be error-prone

---

## Builder vs Decorator - The Bar Summary

| Phase                 | Pattern   | Who Decides? | Example                                |
| --------------------- | --------- | ------------ | -------------------------------------- |
| **Pouring the drink** | Builder   | Bartender    | Spirit type, brand, pour size, quality |
| **Adding mixers**     | Decorator | Customer     | Ice, coke, lime, soda                  |

| Aspect       | Builder (Pouring)                 | Decorator (Adding)                    |
| ------------ | --------------------------------- | ------------------------------------- |
| Type         | Creational                        | Structural                            |
| Purpose      | Construct with configuration      | Add behavior to existing objects      |
| When         | Before object exists              | After object exists                   |
| Reversible   | Can't unpour whiskey              | Can take out the lime wedge           |
| Multiple     | Can't pick 2 spirits              | Extra ice = wrap twice                |
| Method       | `.withX().withY().build()`        | `new XDecorator(new YDecorator(obj))` |
| Result       | One configured object             | Object wrapped in layers              |
| Real Example | `StringBuilder`, `RequestBuilder` | `InputStream`, `BufferedReader`       |

**Remember:** Builder and Decorator aren't competitors - they handle **different phases** of an object's lifecycle. Use Builder to **create**, use Decorator to **extend**.

---

## Decorator Pattern vs Others (Quick Reference)

| Pattern       | Problem It Solves                 | Key Phrase              |
| ------------- | --------------------------------- | ----------------------- |
| **Decorator** | Add behavior to existing objects  | "Wrap and layer"        |
| **Builder**   | Complex object construction       | "Build it step by step" |
| **Factory**   | Which class to instantiate        | "Pick the right type"   |
| **Strategy**  | Swap algorithms at runtime        | "Switch the algorithm"  |
| **Proxy**     | Control access to an object       | "Stand in for it"       |
| **Adapter**   | Make incompatible interfaces work | "Translate between"     |

---

## Common Mistakes to Avoid

1. **Confusing with inheritance** - Decorator is NOT inheritance. Decorator wraps, inheritance extends.
2. **Heavy decorators** - Each decorator should do ONE thing. Don't put complex logic inside.
3. **Order dependency** - If decorator order matters, document it clearly or reconsider design.
4. **Too many layers** - If you're wrapping 10+ times, consider if Decorator is the right pattern.
5. **Decorator that modifies interfaces** - Decorators should implement the SAME interface as the component.
6. **Not using the abstract decorator** - The abstract decorator base class avoids code duplication.

---

## Code Files

See the `code/` folder:

- `WithoutDecorator.java` - Shows class explosion and OCP violations
- `WithDecorator.java` - Full implementation with Bar example

---
