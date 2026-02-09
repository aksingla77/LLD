// SENIOR'S CODE - Abstract Factory Pattern
// Senior is building an AuthService that needs to send OTPs
// Senior works with factory interface, doesn't know concrete implementations

import java.util.Scanner;

class AuthService {

    private final OTPFactory factory;

    // Inject the factory - Senior doesn't know if it's India or USA
    public AuthService(OTPFactory factory) {
        this.factory = factory;
    }

    public void sendOTPViaAll(String otp) {
        System.out.println("\n   Sending OTP via all channels...\n");

        // All senders from same factory = guaranteed compatible!
        SMSSender sms = factory.createSMSSender();
        sms.sendSMS(otp);

        WhatsAppSender wa = factory.createWhatsAppSender();
        wa.sendWhatsApp(otp);

        EmailSender email = factory.createEmailSender();
        email.sendEmail(otp);
    }

    public void sendOTPVia(String channel, String otp) {
        switch (channel) {
            case "sms" ->
                factory.createSMSSender().sendSMS(otp);
            case "whatsapp" ->
                factory.createWhatsAppSender().sendWhatsApp(otp);
            case "email" ->
                factory.createEmailSender().sendEmail(otp);
            default ->
                throw new IllegalArgumentException("Unknown channel: " + channel);
        }
    }
}

public class SeniorCode {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== OTP Service (Abstract Factory Pattern) ===");
        System.out.print("Enter region (india/usa): ");
        String region = scanner.nextLine().toLowerCase();

        // Select the factory based on region
        OTPFactory factory = getFactory(region);

        // Create service with the selected factory
        AuthService service = new AuthService(factory);

        System.out.print("Enter channel (sms/whatsapp/email/all): ");
        String channel = scanner.nextLine().toLowerCase();

        String otp = "847291";

        if (channel.equals("all")) {
            service.sendOTPViaAll(otp);
        } else {
            service.sendOTPVia(channel, otp);
        }

        scanner.close();
    }

    private static OTPFactory getFactory(String region) {
        return switch (region) {
            case "india" ->
                new IndiaOTPFactory();
            case "usa" ->
                new USAOTPFactory();
            default ->
                throw new IllegalArgumentException("Unknown region: " + region);
        };
    }
}

// COMPARISON OF ALL THREE PATTERNS:
//
// Simple Factory:
//   OTPSender sender = OTPFactory.create("email");
//   - One factory, creates different types of ONE product
//
// Factory Method:
//   OTPService service = new EmailOTPService();
//   service.sendOTP(otp);  // subclass decides what to create
//   - Inheritance-based, each subclass creates its product
//
// Abstract Factory:
//   OTPFactory factory = new IndiaOTPFactory();
//   factory.createSMS();  factory.createEmail();  factory.createWhatsApp();
//   - Creates FAMILIES of related products
//   - Guarantees products work together
//
// USE ABSTRACT FACTORY WHEN:
// - You have multiple related products
// - Products must be used together (compatibility)
// - You want to swap entire families at once
