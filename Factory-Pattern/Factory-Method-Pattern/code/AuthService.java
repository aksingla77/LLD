// SENIOR'S CODE - Factory Method Pattern
// Senior's AuthService uses OTPService (abstract creator from Junior's library)

import java.util.Scanner;

public class AuthService {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Auth Service (Factory Method Pattern) ===");
        System.out.print("Enter channel (email/sms/whatsapp): ");
        String channel = scanner.nextLine().toLowerCase();

        // Senior picks the right service based on channel
        OTPService service = getService(channel);

        String otp = "847291";
        service.sendOTP(otp);  // Polymorphism in action!

        scanner.close();
    }

    // This could also be a Simple Factory if needed
    private static OTPService getService(String channel) {
        return switch (channel) {
            case "email" ->
                new EmailOTPService();
            case "sms" ->
                new SMSOTPService();
            case "whatsapp" ->
                new WhatsAppOTPService();
            default ->
                throw new IllegalArgumentException("Unknown channel: " + channel);
        };
    }
}

// COMPARISON:
// 
// Simple Factory:
//   OTPSender sender = OTPFactory.create("email");
//   sender.sendOTP(otp);
//
// Factory Method:
//   OTPService service = new EmailOTPService();
//   service.sendOTP(otp);  // internally calls createSender()
//
// Factory Method is useful when:
// - Creator has other logic besides just creating (template method)
// - You want to use inheritance for extensibility
// - Each creator might have different configuration/behavior
