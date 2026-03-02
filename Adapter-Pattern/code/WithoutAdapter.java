// WITHOUT ADAPTER PATTERN

// THIRD-PARTY APIs

class PayPalAPI {
    public String makePayment(String paypalEmail, double amountInDollars) {
        System.out.println("[PayPal] Charging $" + amountInDollars + " to " + paypalEmail);
        return "Success";
    }
    public String provideRefund(String transactionId, double amountInDollars) {
        System.out.println("[PayPal] Refunding $" + amountInDollars);
        return "Success";
    }
}

class StripeAPI {
    public int charge(String cardToken, int amountInCents, String currency) {
        System.out.println("[Stripe] Charging " + amountInCents + " cents (" + currency + ")");
        return 200;
    }
    public int refundAmount(String chargeId, int amountInCents, String currency) {
        System.out.println("[Stripe] Refunding " + amountInCents + " cents (" + currency + ")");
        return 200;
    }
}

// PROBLEM: Conditional Hell - violates Open-Closed Principle

class CheckoutService {
    private PayPalAPI paypal = new PayPalAPI();
    private StripeAPI stripe = new StripeAPI();
    private String provider;

    public CheckoutService(String provider) {
        this.provider = provider;
    }

    public void processPayment(String customerId, int amount) {
        if (provider.equals("paypal")) {
            String result = paypal.makePayment(customerId + "@customer.com", (double) amount);
            System.out.println(result.equals("Success") ? "Payment successful" : "Payment failed");
        } else if (provider.equals("stripe")) {
            int status = stripe.charge("tok_" + customerId, amount * 100, "USD");
            System.out.println(status == 200 ? "Payment successful" : "Payment failed");
        }
        // Every new provider = more if-else!
    }

    public void processRefund(String transactionId, int amount) {
        if (provider.equals("paypal")) {
            String result = paypal.provideRefund(transactionId, (double) amount);
            System.out.println(result.equals("Success") ? "Refund successful" : "Refund failed");
        } else if (provider.equals("stripe")) {
            int status = stripe.refundAmount(transactionId, amount * 100, "USD");
            System.out.println(status == 200 ? "Refund successful" : "Refund failed");
        }
        // Same if-else repeated in every method!
    }
}

// MAIN

public class WithoutAdapter {
    public static void main(String[] args) {
        System.out.println("--- PayPal ---");
        CheckoutService paypal = new CheckoutService("paypal");
        paypal.processPayment("john_doe", 50);
        paypal.processRefund("txn123", 10);

        System.out.println("\n--- Stripe ---");
        CheckoutService stripe = new CheckoutService("stripe");
        stripe.processPayment("jane_doe", 30);
        stripe.processRefund("ch_456", 30);
    }
}
