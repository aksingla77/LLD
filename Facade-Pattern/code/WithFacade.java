// WITH FACADE PATTERN

// SUBSYSTEM CLASSES

class InventoryService {
    public void checkStock(String productId) {
        System.out.println("[Inventory] Checking stock for product: " + productId);
    }
}

class PaymentService {
    public void processPayment(String paymentDetails) {
        System.out.println("[Payment] Processing payment: " + paymentDetails);
    }
}

class InvoiceService {
    public void generateInvoice(String orderId) {
        System.out.println("[Invoice] Generating invoice for order: " + orderId);
    }
}

class ShippingService {
    public void scheduleDelivery(String address) {
        System.out.println("[Shipping] Scheduling delivery to: " + address);
    }
}

class NotificationService {
    public void sendConfirmation(String email) {
        System.out.println("[Notification] Sending confirmation to: " + email);
    }
}

// FACADE - Provides simplified interface to the subsystem

class OrderFacade {
    private final InventoryService inventory;
    private final PaymentService payment;
    private final InvoiceService invoice;
    private final ShippingService shipping;
    private final NotificationService notification;

    public OrderFacade(InventoryService inventory, PaymentService payment,
                       InvoiceService invoice, ShippingService shipping,
                       NotificationService notification) {
        this.inventory = inventory;
        this.payment = payment;
        this.invoice = invoice;
        this.shipping = shipping;
        this.notification = notification;
    }

    // Simple method that hides all complexity
    public void placeOrder(String productId, String paymentDetails, 
                           String orderId, String address, String email) {
        System.out.println("=== Placing Order ===");
        inventory.checkStock(productId);
        payment.processPayment(paymentDetails);
        invoice.generateInvoice(orderId);
        shipping.scheduleDelivery(address);
        notification.sendConfirmation(email);
        System.out.println("=== Order Complete ===");
    }

    public void cancelOrder(String orderId, String email) {
        System.out.println("\n=== Cancelling Order ===");
        System.out.println("[System] Cancelling order: " + orderId);
        notification.sendConfirmation(email);
        System.out.println("=== Order Cancelled ===");
    }
}

// CLIENT - Uses the simple facade interface

public class WithFacade {
    public static void main(String[] args) {
        // Create subsystem components
        InventoryService inventory = new InventoryService();
        PaymentService payment = new PaymentService();
        InvoiceService invoice = new InvoiceService();
        ShippingService shipping = new ShippingService();
        NotificationService notification = new NotificationService();

        // Create facade
        OrderFacade orderFacade = new OrderFacade(inventory, payment, invoice, shipping, notification);

        // Client uses simple interface - no need to know subsystem details
        orderFacade.placeOrder("PROD-123", "CARD-****-1234", "ORD-001", 
                               "123 Main St, City", "customer@email.com");


        // Benefits:
        // 1. Client only depends on Facade, not 5 subsystem classes
        // 2. Complex coordination logic is encapsulated
        // 3. Subsystem changes don't affect clients
    }
}
