// SENIOR'S CODE - Without Factory Pattern
// Senior is building an AuthService that needs to send OTPs

import java.util.Scanner;

public class AuthService {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Auth Service (Without Factory) ===");
        System.out.print("Enter channel (email/sms/whatsapp): ");
        String channel = scanner.nextLine().toLowerCase();

        String otp = "847291";

        switch (channel) {
            case "email" -> {
                EmailOTP sender = new EmailOTP(Config.smtpHost, Config.smtpPort);
                sender.sendOTP(otp);
            }
            case "sms" -> {
                SMSOTP sender = new SMSOTP("sdkjhfkjsdf", Config.twilioSid);
                sender.sendOTP(otp);
            }
            case "whatsapp" -> {
                WhatsAppOTP sender = new WhatsAppOTP(Config.waBusinessId, Config.waToken);
                sender.sendOTP(otp);
            }
        }

        scanner.close();
    }
}
