
// WITH DECORATOR PATTERN — Bar Example
// ===== COMPONENT — Interface defining the contract =====
interface Drink {

    double getCost();

    String getDescription();
}

// ===== CONCRETE COMPONENTS — Base spirit types =====
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

class Vodka implements Drink {

    @Override
    public double getCost() {
        return 5.00;
    }

    @Override
    public String getDescription() {
        return "Vodka";
    }

}

class Rum implements Drink {

    @Override
    public double getCost() {
        return 4.50;
    }

    @Override
    public String getDescription() {
        return "Rum";
    }
}

// ===== DECORATOR — Abstract wrapper implementing Drink =====
abstract class DrinkDecorator implements Drink {

    protected Drink drink;  // The wrapped drink

    public DrinkDecorator(Drink d) {
        this.drink = d;
    }

    @Override
    public double getCost() {
        return drink.getCost();  // Delegate to wrapped object
    }

    @Override
    public String getDescription() {
        return drink.getDescription();  // Delegate to wrapped object
    }
}

// ===== CONCRETE DECORATORS — Each adds specific behavior =====
class IceDecorator extends DrinkDecorator {

    public IceDecorator(Drink d) {
        super(d);
    }

    @Override
    public double getCost() {
        return drink.getCost() + 0.00;  // Ice is free
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + ", Ice";
    }
}

class CokeDecorator extends DrinkDecorator {

    public CokeDecorator(Drink d) {
        super(d);
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

    public LimeDecorator(Drink d) {
        super(d);
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

class SodaDecorator extends DrinkDecorator {

    public SodaDecorator(Drink d) {
        super(d);
    }

    @Override
    public double getCost() {
        return drink.getCost() + 1.00;
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + ", Soda";
    }
}

class OrangeJuiceDecorator extends DrinkDecorator {

    public OrangeJuiceDecorator(Drink d) {
        super(d);
    }

    @Override
    public double getCost() {
        return drink.getCost() + 1.50;
    }

    @Override
    public String getDescription() {
        return drink.getDescription() + ", Orange Juice";
    }
}

public class WithDecorator {

    public static void main(String[] args) {
        System.out.println("===== Bar with Decorator Pattern =====\n");

        // Customer at the bar — adding mixers to an existing drink
        Drink order = new Whiskey();
        System.out.println("Bartender pours: " + order.getDescription() + " = $" + order.getCost());

        // Customer: "Add some ice"
        order = new IceDecorator(order);
        System.out.println("Added ice: " + order.getDescription() + " = $" + order.getCost());

        // Customer: "And some coke"
        order = new CokeDecorator(order);
        System.out.println("Added coke: " + order.getDescription() + " = $" + order.getCost());

        // Customer: "Actually, add lime too" — can add anytime!
        order = new LimeDecorator(order);
        System.out.println("Added lime: " + order.getDescription() + " = $" + order.getCost());

        // Customer: "More ice please" — just wrap twice!
        order = new IceDecorator(order);
        System.out.println("Added more ice: " + order.getDescription() + " = $" + order.getCost());


        System.out.println("\n===== Other Drinks =====");

        Drink screwdriver = new OrangeJuiceDecorator(new IceDecorator(new Vodka()));
        System.out.println("Screwdriver: " + screwdriver.getDescription() + " = $" + screwdriver.getCost());

        Drink rumAndCoke = new CokeDecorator(new IceDecorator(new Rum()));
        System.out.println("Rum & Coke: " + rumAndCoke.getDescription() + " = $" + rumAndCoke.getCost());

        Drink vodkaSoda = new SodaDecorator(new LimeDecorator(new Vodka()));
        System.out.println("Vodka Soda: " + vodkaSoda.getDescription() + " = $" + vodkaSoda.getCost());

        // Extra ice = wrap twice
        Drink extraIce = new IceDecorator(new IceDecorator(new Whiskey()));
        System.out.println("Whiskey (extra ice): " + extraIce.getDescription() + " = $" + extraIce.getCost());
    }
}
