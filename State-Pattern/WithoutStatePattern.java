// WITHOUT STATE PATTERN IMPLEMENTATION


public class VendingMachineWithoutStatePattern {
    
    // Constants for different states
    private static final int IDLE = 0;
    private static final int HAS_MONEY = 1;
    private static final int DISPENSING = 2;
    private static final int OUT_OF_STOCK = 3;
    
    private int currentState;
    private int itemCount;
    private int moneyInserted;
    
    public VendingMachineWithoutStatePattern(int itemCount) {
        this.itemCount = itemCount;
        this.currentState = itemCount > 0 ? IDLE : OUT_OF_STOCK;
        this.moneyInserted = 0;
    }
    
    // Insert Money Action
    public void insertMoney(int amount) {
        if (currentState == IDLE) {
            moneyInserted += amount;
            System.out.println("Money inserted: $" + amount);
            System.out.println("Total amount: $" + moneyInserted);
            currentState = HAS_MONEY;
        } 
        else if (currentState == HAS_MONEY) {
            moneyInserted += amount;
            System.out.println("Additional money inserted: $" + amount);
            System.out.println("Total amount: $" + moneyInserted);
        } 
        else if (currentState == DISPENSING) {
            System.out.println("Please wait, already dispensing product");
        } 
        else if (currentState == OUT_OF_STOCK) {
            System.out.println("Machine is out of stock. Cannot accept money");
        }
    }
    
    // Select Product Action
    public void selectProduct(int productPrice) {
        if (currentState == IDLE) {
            System.out.println("Please insert money first");
        } 
        else if (currentState == HAS_MONEY) {
            if (moneyInserted >= productPrice) {
                System.out.println("Product selected. Price: $" + productPrice);
                currentState = DISPENSING;
                dispenseProduct(productPrice);
            } else {
                System.out.println("Insufficient money. Please insert $" + 
                                 (productPrice - moneyInserted) + " more");
            }
        } 
        else if (currentState == DISPENSING) {
            System.out.println("Already dispensing. Please wait");
        } 
        else if (currentState == OUT_OF_STOCK) {
            System.out.println("Out of stock");
        }
    }
    
    // Dispense Product Action
    private void dispenseProduct(int productPrice) {
        if (currentState == DISPENSING) {
            if (itemCount > 0) {
                itemCount--;
                System.out.println("Product dispensed!");
                
                int change = moneyInserted - productPrice;
                if (change > 0) {
                    System.out.println("Change returned: $" + change);
                }
                
                moneyInserted = 0;
                
                if (itemCount == 0) {
                    currentState = OUT_OF_STOCK;
                    System.out.println("Machine is now out of stock");
                } else {
                    currentState = IDLE;
                    System.out.println("Thank you! Items remaining: " + itemCount);
                }
            }
        } 
        else {
            System.out.println("Cannot dispense in current state");
        }
    }
    
    // Cancel/Refund Action
    public void cancelTransaction() {
        if (currentState == IDLE) {
            System.out.println("No transaction to cancel");
        } 
        else if (currentState == HAS_MONEY) {
            System.out.println("Transaction cancelled. Refunding $" + moneyInserted);
            moneyInserted = 0;
            currentState = IDLE;
        } 
        else if (currentState == DISPENSING) {
            System.out.println("Cannot cancel while dispensing");
        } 
        else if (currentState == OUT_OF_STOCK) {
            System.out.println("Machine is out of stock");
        }
    }
    
    // Get current state
    public String getCurrentState() {
        switch (currentState) {
            case IDLE: return "IDLE";
            case HAS_MONEY: return "HAS_MONEY";
            case DISPENSING: return "DISPENSING";
            case OUT_OF_STOCK: return "OUT_OF_STOCK";
            default: return "UNKNOWN";
        }
    }
    
    // Display status
    public void displayStatus() {
        System.out.println("\n--- Vending Machine Status ---");
        System.out.println("State: " + getCurrentState());
        System.out.println("Items remaining: " + itemCount);
        System.out.println("Money inserted: $" + moneyInserted);
        System.out.println("------------------------------\n");
    }
}


