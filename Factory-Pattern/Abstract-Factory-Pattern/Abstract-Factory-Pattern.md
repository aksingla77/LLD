# Abstract Factory Pattern

> **See also:** [Factory Patterns Overview](../Factory-Patterns-Glossary.md) for comparison with Simple Factory and Factory Method.

---

## What is Abstract Factory?

Abstract Factory creates **families of related objects** without specifying their concrete classes. It's like a "factory of factories" that produces **compatible products**.

---

## When to Use?

- When you need to create families of related products
- When products from different families shouldn't mix
- When you want to swap entire product families easily

---

## OTP Example - Regional Providers

Different countries use different service providers:

- **India**: IndianSMS, IndianWhatsApp, IndianEmail
- **USA**: USASMS, USAWhatsApp, USAEmail

We need to ensure we don't mix Indian SMS with US WhatsApp configs!

---

## UML Diagrams

### General Pattern Structure
<img width="2659" height="1571" alt="Abstract Factory General" src="https://github.com/user-attachments/assets/182efaec-948e-4258-9346-43ed4806fcb3" />


### OTP Example (Regional Providers)

<img width="3097" height="1603" alt="Abstract Factory OTP" src="https://github.com/user-attachments/assets/a609f162-c179-43af-913f-0ffaaf4425a7" />

---

## Key Benefit

Switching from India to USA providers = Change ONE line (factory selection)
All products automatically become compatible US versions!

---

## Real-World Use Cases

### 🎨 Cross-Platform UI Toolkits (Swing, JavaFX, Qt)

```java
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
    TextField createTextField();
}

class WindowsUIFactory implements UIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
    public TextField createTextField() { return new WindowsTextField(); }
}

class MacUIFactory implements UIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
    public TextField createTextField() { return new MacTextField(); }
}
```

**Real example:** Java Swing's `LookAndFeel` system. Switch from Windows to Mac theme, and ALL components automatically match!

### 🏦 Banking Systems - Multi-Region Support

```java
interface BankingFactory {
    PaymentProcessor createPaymentProcessor();
    TaxCalculator createTaxCalculator();
    CurrencyFormatter createCurrencyFormatter();
}

class IndiaBankingFactory implements BankingFactory {
    public PaymentProcessor createPaymentProcessor() { return new UPIProcessor(); }
    public TaxCalculator createTaxCalculator() { return new GSTCalculator(); }
    public CurrencyFormatter createCurrencyFormatter() { return new INRFormatter(); }
}

class USABankingFactory implements BankingFactory {
    public PaymentProcessor createPaymentProcessor() { return new ACHProcessor(); }
    public TaxCalculator createTaxCalculator() { return new IRSCalculator(); }
    public CurrencyFormatter createCurrencyFormatter() { return new USDFormatter(); }
}
```

**Used by:** PayPal, Stripe, international banking apps. Ensures all region-specific components are compatible.

### 🚗 Vehicle Manufacturing (Maruti, Tata, Hyundai)

```java
interface CarFactory {
    Engine createEngine();
    Wheels createWheels();
    Interior createInterior();
}

class LuxuryCarFactory implements CarFactory {
    public Engine createEngine() { return new V8Engine(); }
    public Wheels createWheels() { return new AlloyWheels(); }
    public Interior createInterior() { return new LeatherInterior(); }
}

class EconomyCarFactory implements CarFactory {
    public Engine createEngine() { return new I4Engine(); }
    public Wheels createWheels() { return new SteelWheels(); }
    public Interior createInterior() { return new FabricInterior(); }
}
```

**Real example:** Automobile configurators ensure you can't mix luxury engine with economy interiors.

### 🎮 Game Theme Systems

```java
interface GameThemeFactory {
    Background createBackground();
    Character createCharacter();
    Music createMusic();
}

class MedievalThemeFactory implements GameThemeFactory {
    public Background createBackground() { return new CastleBackground(); }
    public Character createCharacter() { return new Knight(); }
    public Music createMusic() { return new OrchestralMusic(); }
}

class SciFiThemeFactory implements GameThemeFactory {
    public Background createBackground() { return new SpaceBackground(); }
    public Character createCharacter() { return new Astronaut(); }
    public Music createMusic() { return new ElectronicMusic(); }
}
```

**Used in:** Games with multiple themes/seasons. Switch theme = all assets match automatically.

### ☁️ Multi-Cloud Infrastructure (AWS, GCP, Azure)

```java
interface CloudFactory {
    ComputeService createCompute();
    StorageService createStorage();
    DatabaseService createDatabase();
}

class AWSFactory implements CloudFactory {
    public ComputeService createCompute() { return new EC2Service(); }
    public StorageService createStorage() { return new S3Service(); }
    public DatabaseService createDatabase() { return new RDSService(); }
}

class GCPFactory implements CloudFactory {
    public ComputeService createCompute() { return new GCEService(); }
    public StorageService createStorage() { return new GCSService(); }
    public DatabaseService createDatabase() { return new CloudSQLService(); }
}
```

**Used by:** Cloud-agnostic platforms like Terraform, Kubernetes. Switch cloud provider without changing application code!

---

## Code Files

Check out the complete working examples in the `code/` folder:

- [JuniorCode.java](code/JuniorCode.java) – Regional OTP sender implementations
- [SeniorCode.java](code/SeniorCode.java) – Client using abstract factory

---
