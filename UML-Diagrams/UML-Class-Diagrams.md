# UML Class Diagrams

## Goal

By the end of this, you should be able to draw a complete UML class diagram in an LLD interview: classes, attributes, methods, access modifiers, and all relationship types.

---

## Opening

> In LLD interviews, before you write a single line of code, the interviewer expects you to **draw a class diagram**. It's how you communicate your design. Think of it as a blueprint, just like an architect draws a blueprint before building a house, you draw a class diagram before coding.

### UML Diagrams: Quick Overview

> UML has **14 diagrams** in total, split into **2 categories**:

| Category                 | Count | What it answers              | Diagrams                                                                                     |
| ------------------------ | ----- | ---------------------------- | -------------------------------------------------------------------------------------------- |
| **Structural** (static)  | 7     | "What exists in the system?" | **Class**, Object, Component, Deployment, Package, Composite Structure, Profile              |
| **Behavioral** (dynamic) | 7     | "What happens at runtime?"   | **Sequence**, Use Case, Activity, State Machine, Communication, Interaction Overview, Timing |

### Which ones matter for interviews?

| Priority  | Diagram              | Why                                                                  |
| --------- | -------------------- | -------------------------------------------------------------------- |
| MUST KNOW | **Class Diagram**    | Asked in ~99% of LLD interviews, draw classes & relationships        |
| MUST KNOW | **Sequence Diagram** | Some specific problems need it, e.g. "walk me through the flow of X" |
| Skip      | The remaining 12     | Know the names exist, that's it                                      |

> **Bottom line:** Master Class Diagrams + Sequence Diagrams, that covers almost all interviews. In this doc, we focus on class diagrams.

### So what exactly is a Class Diagram?

> A class diagram is a **static structural diagram** that shows the classes in your system, their attributes, methods, and the relationships between them. It's essentially the **blueprint of your code**.

---

## Part 1: Class Structure

> A class in UML is drawn as a **box divided into 3 sections**.

```
┌──────────────────────┐
│       Car            │  ← Class Name
├──────────────────────┤
│ - brand: String      │  ← Attributes (fields)
│ - model: String      │
│ - speed: int          │
├──────────────────────┤
│ + drive(): void      │  ← Methods
│ + brake(): void      │
│ + getSpeed(): int    │
└──────────────────────┘
```

### In Java:

```java
class Car {
    private String brand;
    private String model;
    private int speed;

    public void drive() { }
    public void brake() { }
    public int getSpeed() { return speed; }
}
```

### Each section explained:

1. **Top section** - Class name (PascalCase, e.g. `Car`, `PaymentService`)
2. **Middle section** - Attributes: `accessModifier name: Type`
3. **Bottom section** - Methods: `accessModifier name(params): ReturnType`

> **Interview tip:** You don't need to write every getter/setter. Focus on the important attributes and key methods that show your design decisions.

> **Do I need to show method parameters?** In interviews, **yes, for key methods**. If a method's parameter reveals a design decision (e.g., `+ parkVehicle(vehicle: Vehicle, spot: Spot): boolean`), include it. For obvious ones like `getSpeed(): int`, skip the params. Rule of thumb: **show params when they show relationships between classes, skip them for trivial getters/setters.**
>
> ```
> ├──────────────────────────────────────────┤
> │ + parkVehicle(vehicle: Vehicle): boolean │  ← show params (reveals design)
> │ + getSpeed(): int                        │  ← skip params (obvious)
> └──────────────────────────────────────────┘
> ```

---

## Part 2: Access Modifiers

> Those little symbols before each attribute/method? They tell us **who can access what**.

| Symbol | Modifier  | Who can access?        |
| ------ | --------- | ---------------------- |
| `+`    | public    | Everyone               |
| `-`    | private   | Only the class itself  |
| `#`    | protected | Class + its subclasses |

### Example on the diagram (same Car example):

```
┌────────────────────────────────┐
│             Car                │
├────────────────────────────────┤
│ - brand: String                │  ← private (only Car can access)
│ - speed: int                   │  ← private
│ # engineType: String           │  ← protected (Car + subclasses like SportsCar)
├────────────────────────────────┤
│ + drive(): void                │  ← public (anyone can call)
│ + brake(): void                │  ← public
│ + getSpeed(): int              │  ← public
│ - validateFuel(): boolean      │  ← private (internal helper, no one else needs this)
└────────────────────────────────┘
```

### In Java:

```java
class Car {
    private String brand;              // -
    private int speed;                 // -
    protected String engineType;       // #

    public void drive() { }           // +
    public void brake() { }           // +
    public int getSpeed() { return speed; }  // +
    private boolean validateFuel() { return true; }  // -
}
```

> **Connect to OOP:** This is encapsulation in action. `brand` and `speed` are private, outside code can't mess with them directly. `engineType` is protected, a `SportsCar` subclass might need to know it. `drive()` and `brake()` are public, that's the interface you expose to the world. You're showing the interviewer you know what to expose and what to hide.

---

## Part 3: Relationships - The Big Picture

> Now the interesting part. Classes don't exist in isolation. They **relate** to each other. There are a few types of relationships, and each has its own arrow.

> This can feel overwhelming ("which arrow is which?"), but here's a simple mental model.

### Quick overview (all arrows side by side):

> These 5 relationships fall into **2 groups**:

**Class-level relationships** - defined between **classes/interfaces themselves** (static, compile-time):

```
Inheritance:       ——————▷   (solid line, hollow triangle)    "Dog IS-A Animal"
Implementation:    - - - -▷  (dashed line, hollow triangle)   "Bird IMPLEMENTS Flyable"
```

**Object-level relationships** - defined between **instances** at runtime:

```
Association:       ——————>   (solid line, arrow)              "Student HAS-A Course"
Aggregation:       ◇————>    (hollow diamond)                 "Department HAS Students (loosely)"
Composition:       ◆————>    (filled diamond)                 "Car HAS Engine (tightly)"
```

> **Simple way to remember:** Class-level = "what **is** it?" (inheritance tree). Object-level = "what does it **have**?" (runtime wiring).

> **Interview reality - you only need 2 arrows in practice:**
>
> 1. **Inheritance arrow (------▷)** - use this for both `extends` AND `implements`. Most interviewers don't distinguish between solid/dashed. If a class extends another class or implements an interface, just draw the same hollow triangle arrow. Nobody will penalize you for not using a dashed line for interfaces.
> 2. **Composition arrow (◆------>)** - use this for any "has-a" relationship. In interviews, don't overthink aggregation vs composition. If one class holds a reference to another, just call it **composition** and use the filled diamond. The distinction between ◇ and ◆ rarely matters in practice.
>
> That's it, **inheritance + composition** cover 95% of what you'll draw in an LLD interview. The rest is academic polish.

> Let's break each one down.

---

## Part 4: Inheritance - "is-a"

> You already know this from OOP. When a class **extends** another class.

```
        ┌──────────┐
        │  Animal   │
        └────▲─────┘
             │  (solid line, hollow triangle)
             │
        ┌────┴─────┐
        │   Dog     │
        └──────────┘
```

> Dog **is a** Animal. Arrow points **from child to parent** (Dog → Animal).

### In Java:

```java
class Animal { }

class Dog extends Animal { }  // Dog IS-A Animal
```

---

## Part 5: Implementation - "implements"

> When a class implements an interface. Same triangle arrow, but **dashed line**.

```
     ┌─────────────────┐
     │  <<interface>>   │
     │      Shape        │
     └───────▲──────────┘
             ┊  (dashed line, hollow triangle)
        ┌────┊────┐
        ┊         ┊
  ┌─────┴────┐  ┌─┴──────────┐
  │ Rectangle │  │  Triangle   │
  └──────────┘  └────────────┘
```

> **Tip:** `<<interface>>` is written above the name — this is called a **stereotype** in UML.

> **Do I need to show interfaces in interviews?** **Yes — always.** Interfaces are a core part of LLD. They show the interviewer you're designing to abstractions, not implementations (Dependency Inversion Principle). Draw them exactly like a class box, but add `<<interface>>` above the name. They only have methods (no attributes), since interfaces can't hold state.

### In Java:

```java
interface Shape {
    double area();
}

class Rectangle implements Shape {
    public double area() { /* ... */ }
}

class Triangle implements Shape {
    public double area() { /* ... */ }
}
```

---

## Part 6: Object-level Relationships - Association, Aggregation, Composition

> All three look **almost identical in code** — one class holds a reference to another:
>
> ```java
> class A {
>     private B b;    // Association? Aggregation? Composition?
> }                   // Can't tell from code alone!
> ```
>
> The difference isn't in the code, it's in the **mental model**. Use this 2-step question:

### Mental model - 2 questions to decide the relationship:

```
Step 1: Is B a PART of A?  (whole-part relationship)
        │
        ├── NO  →  Association  (they just know about each other)
        │          e.g. Student ↔ Course — a course is NOT a part of a student
        │
        └── YES →  Step 2: Can B exist without A?
                    │
                    ├── YES →  Aggregation  (part can survive alone)
                    │          e.g. Car → Wheel — wheel IS a part, but can be reused
                    │
                    └── NO  →  Composition  (part dies with parent)
                               e.g. House → Room — room IS a part, can't exist alone
```

| Relationship    | Arrow   | Step 1: Is B a part of A? | Step 2: Can B exist without A? | Example            |
| --------------- | ------- | ------------------------- | ------------------------------ | ------------------ |
| **Association** | ------> | No, independent           | (doesn't apply)                | Student <-> Course |
| **Aggregation** | ◇-----> | Yes, B is part of A       | Yes, B survives                | Car -> Wheel       |
| **Composition** | ◆-----> | Yes, B is part of A       | No, B dies with A              | House -> Room      |

> Now let's see each one with a UML diagram.

---

### Association - "knows about"

> The simplest relationship. One class **knows about** another. Neither owns the other.

```
┌──────────┐         ┌──────────┐
│  Student  │ ——————> │  Course  │
└──────────┘         └──────────┘
```

> A Student is **associated** with a Course. They're **independent**, they just know about each other.

#### Multiplicity (how many?)

You write numbers near the ends of the line:

```
┌──────────┐   1    *   ┌──────────┐
│  Student  │ ────────> │  Course   │
└──────────┘            └──────────┘
```

| Notation      | Meaning                |
| ------------- | ---------------------- |
| `1`           | Exactly one            |
| `*` or `0..*` | Zero or more           |
| `1..*`        | One or more            |
| `0..1`        | Zero or one (optional) |

> A Student can enroll in **many** Courses. Each Course has **many** Students.

---

### Aggregation - "has-a, but loosely"

> A **special type of association**. It means "has-a" but the contained object can **exist independently**.

```
┌──────────┐          ┌──────────┐
│   Car     │ ◇——————> │  Wheel   │
└──────────┘          └──────────┘
```

> A Car **has** Wheels. But if the Car is scrapped, the Wheels can be removed and used on another car, they exist independently.

---

### Composition - "has-a, but tightly"

> Also a "has-a" relationship, but the contained object **cannot exist without the parent**.

```
┌──────────┐          ┌──────────┐
│  House    │ ◆——————> │   Room   │
└──────────┘          └──────────┘
```

> A House **has** Rooms. If the House is demolished, the Rooms are gone too. A Room has no meaning without its House.

---

### Aggregation vs Composition - Summary

|                        | Aggregation (◇) | Composition (◆)   |
| ---------------------- | --------------- | ----------------- |
| Relationship           | "has-a" (weak)  | "has-a" (strong)  |
| Is B a part of A?      | Yes             | Yes               |
| Can B exist without A? | Yes, B survives | No, B dies with A |
| Diamond                | Hollow ◇        | Filled ◆          |
| Example                | Car -> Wheel    | House -> Room     |

> **Interview honesty:** In real interviews, the line between aggregation and composition can be blurry. If you're unsure, just use composition. The interviewer cares more about your classes and methods than whether you used ◇ or ◆. But knowing the difference shows depth.

---

## Closing

> Quick recap of what to remember for interviews:

1. **Class box** = Name | Attributes | Methods
2. **Access modifiers** = `+` public, `-` private, `#` protected
3. **Relationships** - remember the arrows:
   - Inheritance: solid + hollow triangle
   - Interface: dashed + hollow triangle
   - Association: solid + arrow
   - Aggregation: hollow diamond (part can survive)
   - Composition: filled diamond (part can't survive)
4. **Start with classes, then draw relationships** - don't try to get arrows perfect first try
5. Practice by drawing diagrams for patterns we covered (Factory, Singleton, Observer, etc.)

> In the next videos, we'll cover SOLID principles and then jump into real-world LLD problems where you'll draw these diagrams from scratch.

---

## Tooling Note

Tools you can use to practice drawing UML:

| Tool            | Best for                             | Link             |
| --------------- | ------------------------------------ | ---------------- |
| **Excalidraw**  | Hand-drawn feel, great for teaching  | excalidraw.com   |
| **draw.io**     | Polished diagrams, free              | app.diagrams.net |
| **Mermaid**     | Code-based, renders in GitHub/Notion | mermaid.js.org   |
| **PlantUML**    | Code-based, very detailed            | plantuml.com     |
| **Pen + Paper** | Interviews!                          |                  |
