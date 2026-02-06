
interface PaymentStrategy {
    void pay(double amount);
}


class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing Credit Card Payment...");
        System.out.println("Step 1: Validate card number");
        System.out.println("Step 2: Check with fraud detection system");
        System.out.println("Step 3: Charge the card");
        System.out.println("Step 4: Wait for response from bank");
        System.out.println("Step 5: Save transaction to database");
        System.out.println("✓ Paid $" + amount + " using Credit Card");
        System.out.println("Receipt: Credit Card ending in ****-****-****-3456");
    }
}

class DebitCardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing Debit Card Payment...");
        System.out.println("Step 1: Validate debit card");
        System.out.println("Step 2: Check account balance");
        System.out.println("Step 3: Charge the card");
        System.out.println("Step 4: Send confirmation SMS");
        System.out.println("✓ Paid $" + amount + " using Debit Card");
        System.out.println("Receipt: Debit Card ending in ****-****-****-7890");
    }
}

class PaypalPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing PayPal Payment...");
        System.out.println("Step 1: Redirect to PayPal login page");
        System.out.println("Step 2: User authenticates on PayPal server");
        System.out.println("Step 3: User confirms payment amount");
        System.out.println("Step 4: PayPal sends callback with transaction ID");
        System.out.println("Step 5: Verify callback signature");
        System.out.println("Step 6: Save transaction to database");
        System.out.println("✓ Paid $" + amount + " using PayPal");
        System.out.println("Receipt: PayPal Account john@example.com");
    }
}

class UpiPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing UPI Payment...");
        System.out.println("Step 1: Validate UPI ID format");
        System.out.println("Step 2: Generate transaction ID and QR code");
        System.out.println("Step 3: Send OTP to mobile");
        System.out.println("Step 4: User enters OTP in UPI app");
        System.out.println("Step 5: NPCI processes the transaction");
        System.out.println("Step 6: Receive confirmation from bank");
        System.out.println("✓ Paid $" + amount + " using UPI");
        System.out.println("Receipt: UPI ID 9988776655@ybl");
    }
}

class NetBankingPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing Net Banking Payment...");
        System.out.println("Step 1: Identify which bank customer is using");
        System.out.println("Step 2: Redirect to bank's net banking portal");
        System.out.println("Step 3: Customer logs in with bank credentials");
        System.out.println("Step 4: Customer confirms the amount and beneficiary");
        System.out.println("Step 5: Bank processes the fund transfer");
        System.out.println("Step 6: Bank sends confirmation back");
        System.out.println("✓ Paid $" + amount + " using Net Banking");
        System.out.println("Receipt: HDFC Bank Transfer");
    }
}

class WalletPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing Wallet Payment...");
        System.out.println("Step 1: Check wallet balance");
        System.out.println("Step 2: Verify wallet is not frozen");
        System.out.println("Step 3: Deduct amount from wallet");
        System.out.println("Step 4: Add transaction to wallet history");
        System.out.println("Step 5: Send notification to customer");
        System.out.println("✓ Paid $" + amount + " using Wallet");
        System.out.println("Receipt: Wallet Balance Remaining: $500");
    }
}


class ShoppingCart {

    private String customerName;
    private double totalAmount;
    private PaymentStrategy paymentStrategy;

    public ShoppingCart(String name) {
        this.customerName = name;
        this.totalAmount = 0;
    }

    public void addItemPrice(double price) {
        totalAmount += price;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set!");
        }
        paymentStrategy.pay(totalAmount);
    }
}

// ============================================================
// MAIN CLASS (ONLY PUBLIC CLASS)
// ============================================================
public class WithStrategyPattern {

    public static void main(String[] args) {

        System.out.println("========== STRATEGY PATTERN (ONE FILE) ==========");

        ShoppingCart cart = new ShoppingCart("John Doe");
        cart.addItemPrice(50);
        cart.addItemPrice(25);
        cart.addItemPrice(25);

        cart.setPaymentStrategy(new CreditCardPayment());
        cart.checkout();

        System.out.println("\n--- Switching strategy at runtime ---\n");

        ShoppingCart cart2 = new ShoppingCart("John Doe");
        cart2.addItemPrice(100);
        cart2.addItemPrice(150);

        cart2.setPaymentStrategy(new UpiPayment());
        cart2.checkout();
    }
}
