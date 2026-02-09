// JUNIOR'S CODE - With Factory Pattern
// Junior owns: Interface + Implementations + Factory

// Step 1: Interface - This is what Senior sees
interface OTPSender {

    void sendOTP(String otp);
}

// Step 2: Implementations (Senior doesn't need to know about these)
class EmailOTP implements OTPSender {

    public EmailOTP(String host, int port) {
    }

    @Override
    public void sendOTP(String otp) {
        System.out.println("   [EmailOTP] Sent OTP: " + otp);
    }
}

class SMSOTP implements OTPSender {

    public SMSOTP(String key, String sid) {
    }

    @Override
    public void sendOTP(String otp) {
        System.out.println("   [SMSOTP] Sent OTP: " + otp);
    }
}

class WhatsAppOTP implements OTPSender {

    public WhatsAppOTP(String id, String token) {
    }

    @Override
    public void sendOTP(String otp) {
        System.out.println("   [WhatsAppOTP] Sent OTP: " + otp);
    }
}

// Step 3: Factory - Junior owns ALL construction logic
class OTPFactory {

    public static OTPSender createOTPSender(String channel) {
        switch (channel.toLowerCase()) {
            case "email" -> {
                return new EmailOTP(Config.smtpHost, Config.smtpPort);
            }
            case "sms" -> {
                return new SMSOTP(Config.twilioKey, Config.twilioSid);
            }
            case "whatsapp" -> {
                return new WhatsAppOTP(Config.waBusinessId, Config.waToken);
            }
            default ->
                throw new IllegalArgumentException("Unknown: " + channel);
        }
    }
}

// Junior gives Senior just 2 things:
// 1. OTPSender interface
// 2. OTPFactory.createOTPSender() method
//
// Senior doesn't need to know EmailOTP, SMSOTP, WhatsAppOTP exist!
