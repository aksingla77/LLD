
// WITHOUT DECORATOR - Shows the problems
// ===== APPROACH 1: Subclass Explosion =====
// With 4 mixers, we'd need 2^4 = 16 classes for all combinations!
class Whiskey {

    public double getCost() {
        return 5.00;
    }

    public String getDescription() {
        return "Whiskey";
    }
}

class WhiskeyWithIce extends Whiskey {

    @Override
    public double getCost() {
        return super.getCost() + 0.00;  // Ice is free
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Ice";
    }
}

class WhiskeyWithCoke extends Whiskey {

    @Override
    public double getCost() {
        return super.getCost() + 1.00;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Coke";
    }
}

// What about ice AND coke? Need another class!
class WhiskeyWithIceAndCoke extends Whiskey {

    @Override
    public double getCost() {
        return super.getCost() + 0.00 + 1.00;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Ice, Coke";
    }
}

// What about ice, coke, AND lime? Another class!
class WhiskeyWithIceAndCokeAndLime extends Whiskey {

    @Override
    public double getCost() {
        return super.getCost() + 0.00 + 1.00 + 0.50;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Ice, Coke, Lime";
    }
}

// The explosion continues... for 4 mixers, we need 16 classes!
// ===== APPROACH 2: Boolean Flags (violates OCP) =====
class FlagBasedDrink {

    private boolean hasIce;
    private boolean hasCoke;
    private boolean hasLime;
    private boolean hasSoda;

    public FlagBasedDrink(boolean ice, boolean coke, boolean lime, boolean soda) {
        this.hasIce = ice;
        this.hasCoke = coke;
        this.hasLime = lime;
        this.hasSoda = soda;
    }

    // Problem: Every new mixer requires modifying this method!
    public double getCost() {
        double cost = 5.00;  // Base spirit
        if (hasIce) {
            cost += 0.00;
        }
        if (hasCoke) {
            cost += 1.00;
        }
        if (hasLime) {
            cost += 0.50;
        }
        if (hasSoda) {
            cost += 1.00;
        }
        // Adding a new mixer? Must modify this class!
        return cost;
    }

    // Same problem here
    public String getDescription() {
        StringBuilder desc = new StringBuilder("Whiskey");
        if (hasIce) {
            desc.append(", Ice");
        }
        if (hasCoke) {
            desc.append(", Coke");
        }
        if (hasLime) {
            desc.append(", Lime");
        }
        if (hasSoda) {
            desc.append(", Soda");
        }
        // Adding a new mixer? Must modify this class!
        return desc.toString();
    }
}

public class WithoutDecorator {

    public static void main(String[] args) {
        System.out.println("=== Problem 1: Subclass Explosion ===");

        // We need a specific class for each combination
        Whiskey drink1 = new WhiskeyWithIce();
        System.out.println(drink1.getDescription() + " = $" + drink1.getCost());

        Whiskey drink2 = new WhiskeyWithIceAndCoke();
        System.out.println(drink2.getDescription() + " = $" + drink2.getCost());

        // What if customer wants Ice + Lime but no Coke?
        // We'd need: WhiskeyWithIceAndLime class!
        // What about extra ice? We'd need: WhiskeyWithDoubleIce class!
        System.out.println("\n=== Problem 2: Boolean Flags (OCP Violation) ===");

        // Works, but adding new mixers requires modifying the class
        FlagBasedDrink flagDrink = new FlagBasedDrink(true, true, false, false);
        System.out.println(flagDrink.getDescription() + " = $" + flagDrink.getCost());

        // Problem: What if different base spirits have different prices?
        // Whiskey = $5, Vodka = $5, Premium Whiskey = $12, etc.?
        // The flag approach gets very messy very quickly!
        System.out.println("\n=== Summary of Problems ===");
        System.out.println("1. Subclass approach: Class explosion (2^N classes for N mixers)");
        System.out.println("2. Boolean flags: Violates OCP, must modify class for every new feature");
        System.out.println("3. Both: Can't easily do 'extra ice' or conditional mixers");
        System.out.println("4. Both: Hard to add new base spirits with different prices");
    }
}
