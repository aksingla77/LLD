# Factory Method Pattern

> **See also:** [Factory Patterns Overview](../Factory-Patterns-Glossary.md) for comparison with Simple Factory and Abstract Factory.

---

## What is Factory Method?

Factory Method defines an **interface for creating an object**, but lets **subclasses decide** which class to instantiate. It uses **inheritance** instead of a separate factory class.

---

## When to Use?

- When a class can't anticipate which objects it needs to create
- When you want subclasses to specify the objects they create
- When creator has other behavior besides creating (template method pattern)
- When you have parallel hierarchies (creators and products)

---

## OTP Example

In our example:

- **Abstract Creator**: `OTPService` with abstract `createSender()` method
- **Concrete Creators**: `EmailOTPService`, `SMSOTPService`, `WhatsAppOTPService`
- **Product Interface**: `OTPSender`
- **Concrete Products**: `EmailOTP`, `SMSOTP`, `WhatsAppOTP`

Each service subclass knows how to create its specific sender!

---

## UML Diagram

<!-- TODO: Add factory-method-uml.png -->

---

## Key Benefit

Adding a new channel (e.g., Telegram) = Create `TelegramOTPService` + `TelegramOTP`
No existing code changes!

---

## Real-World Use Cases

### 🍕 Food Delivery Apps (Swiggy, Zomato, Uber Eats)

```java
abstract class RestaurantPage {
    public void display() {
        Menu menu = createMenu();  // Factory Method
        menu.show();
    }
    protected abstract Menu createMenu();
}

class PizzaHutPage extends RestaurantPage {
    protected Menu createMenu() { return new PizzaHutMenu(); }
}
class DominosPage extends RestaurantPage {
    protected Menu createMenu() { return new DominosMenu(); }
}
```

Each restaurant subclass controls how its menu is created and displayed.

### 📱 Cross-Platform UI Frameworks (React Native, Flutter)

```java
abstract class Dialog {
    public void render() {
        Button btn = createButton();  // Factory Method
        btn.render();
    }
    protected abstract Button createButton();
}

class AndroidDialog extends Dialog {
    protected Button createButton() { return new MaterialButton(); }
}
class iOSDialog extends Dialog {
    protected Button createButton() { return new CupertinoButton(); }
}
```

**Real example:** UI frameworks let platform-specific subclasses decide which native components to create.

### 🏭 Java Collections Framework

```java
// Iterator pattern uses Factory Method internally
List<String> list = new ArrayList<>();
Iterator<String> iterator = list.iterator();  // Factory Method!
// ArrayList returns ArrayListIterator
// LinkedList returns LinkedListIterator
```

Each collection type creates its own specialized iterator.

### 📊 Report Generation Systems

```java
abstract class ReportGenerator {
    public void generateReport(Data data) {
        Report report = createReport();  // Factory Method
        report.build(data);
        report.export();
    }
    protected abstract Report createReport();
}

class PDFReportGenerator extends ReportGenerator {
    protected Report createReport() { return new PDFReport(); }
}
class ExcelReportGenerator extends ReportGenerator {
    protected Report createReport() { return new ExcelReport(); }
}
```

**Used by:** Business intelligence tools, analytics dashboards.

### 🎮 Game Character Creation

```java
abstract class Game {
    public void startGame() {
        Character hero = createCharacter();  // Factory Method
        hero.spawn();
    }
    protected abstract Character createCharacter();
}

class WarriorGame extends Game {
    protected Character createCharacter() { return new Warrior(); }
}
class StickManGame extends Game {
    protected Character createCharacter() { return new StickMan(); }
}
```

Each game mode creates its own hero type.

---

## Code Files

Check out the complete working examples in the `code/` folder:

- [JuniorCode.java](code/JuniorCode.java) – OTP sender implementations
- [AuthService.java](code/AuthService.java) – Client using factory method
- [Config.java](code/Config.java) – Configuration

---
