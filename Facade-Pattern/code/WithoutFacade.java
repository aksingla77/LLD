// WITHOUT FACADE PATTERN

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

// PROBLEM: Client must know and coordinate all subsystem components

public class WithoutFacade {
    public static void main(String[] args) {
        // Client creates and manages all components
        InventoryService inventory = new InventoryService();
        PaymentService payment = new PaymentService();
        InvoiceService invoice = new InvoiceService();
        ShippingService shipping = new ShippingService();
        NotificationService notification = new NotificationService();

        // Client must know the correct sequence of operations
        System.out.println("=== Placing Order ===");
        inventory.checkStock("PROD-123");
        payment.processPayment("CARD-****-1234");
        invoice.generateInvoice("ORD-001");
        shipping.scheduleDelivery("123 Main St, City");
        notification.sendConfirmation("customer@email.com");

        // Problems:
        // 1. Client tightly coupled to all 5 subsystem classes
        // 2. Complex coordination logic repeated wherever needed
        // 3. Changes in any subsystem require changes in all clients
    }
}
