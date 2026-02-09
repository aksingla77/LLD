// SENIOR'S CODE - With Factory Pattern
// Senior is building an AuthService that needs to send OTPs
// Senior only knows: OTPSender interface + OTPFactory

import java.util.Scanner;

public class AuthService {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Auth Service (With Factory) ===");
        System.out.print("Enter channel (email/sms/whatsapp): ");
        String channel = scanner.nextLine().toLowerCase();

        String otp = "847291";

        // Simple Factory: One line! Factory handles all the complexity
        OTPSender sender = OTPFactory.createOTPSender(channel);
        sender.sendOTP(otp);

        scanner.close();
    }
}

// BENEFITS:
// 1. Junior adds TelegramOTP → Senior's code unchanged!
// 2. Junior changes EmailOTP constructor → Senior's code unchanged!
// 3. Junior switches Twilio to MSG91 → Senior's code unchanged!
//
// Junior can change ANYTHING. Senior's code never breaks!
