// WITH ADAPTER PATTERN

// TARGET INTERFACE

interface PaymentProcessor {
    boolean pay(String customerId, int amount);
    boolean refund(String transactionId, int amount);
}

// ADAPTEES (Third-party APIs)

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

// ADAPTERS

class PayPalAdapter implements PaymentProcessor {
    private final PayPalAPI paypal;

    public PayPalAdapter(PayPalAPI paypal) { this.paypal = paypal; }

    @Override
    public boolean pay(String customerId, int amount) {
        String result = paypal.makePayment(customerId + "@customer.com", (double) amount);
        return result.equals("Success");
    }

    @Override
    public boolean refund(String transactionId, int amount) {
        String result = paypal.provideRefund(transactionId, (double) amount);
        return result.equals("Success");
    }
}

class StripeAdapter implements PaymentProcessor {
    private final StripeAPI stripe;

    public StripeAdapter(StripeAPI stripe) { this.stripe = stripe; }

    @Override
    public boolean pay(String customerId, int amount) {
        int status = stripe.charge("tok_" + customerId, amount * 100, "USD");
        return status == 200;
    }

    @Override
    public boolean refund(String transactionId, int amount) {
        int status = stripe.refundAmount(transactionId, amount * 100, "USD");
        return status == 200;
    }
}

// CLIENT

class CheckoutService {
    private final PaymentProcessor processor;

    public CheckoutService(PaymentProcessor processor) {
        this.processor = processor;
    }

    public void checkout(String customerId, int amount) {
        boolean success = processor.pay(customerId, amount);
        System.out.println(success ? "Payment successful" : "Payment failed");
    }

    public void processRefund(String transactionId, int amount) {
        boolean success = processor.refund(transactionId, amount);
        System.out.println(success ? "Refund successful" : "Refund failed");
    }
}

// MAIN

public class WithAdapter {
    public static void main(String[] args) {
        System.out.println("--- PayPal ---");
        CheckoutService paypal = new CheckoutService(new PayPalAdapter(new PayPalAPI()));
        paypal.checkout("john_doe", 50);
        paypal.processRefund("txn123", 10);

        System.out.println("\n--- Stripe ---");
        CheckoutService stripe = new CheckoutService(new StripeAdapter(new StripeAPI()));
        stripe.checkout("jane_doe", 30);
        stripe.processRefund("ch_456", 30);
    }
}
