// JUNIOR'S CODE - Factory Method Pattern
// Junior owns: Interface + Implementations + Abstract Creator + Concrete Creators

// ============ PRODUCT INTERFACE ============
interface OTPSender {

    void send(String otp);
}

// ============ CONCRETE PRODUCTS ============
class EmailOTP implements OTPSender {

    public EmailOTP(String host, int port) {
    }

    @Override
    public void send(String otp) {
        System.out.println("   [EmailOTP] Sent OTP: " + otp);
    }
}

class SMSOTP implements OTPSender {

    public SMSOTP(String key, String sid) {
    }

    @Override
    public void send(String otp) {
        System.out.println("   [SMSOTP] Sent OTP: " + otp);
    }
}

class WhatsAppOTP implements OTPSender {

    public WhatsAppOTP(String id, String token) {
    }

    @Override
    public void send(String otp) {
        System.out.println("   [WhatsAppOTP] Sent OTP: " + otp);
    }
}

// ============ ABSTRACT CREATOR ============
// This is the "Factory Method" pattern - subclasses implement createSender()
abstract class OTPService {

    // Template method - uses the factory method
    public void sendOTP(String otp) {
        OTPSender sender = createSender();  // Factory Method call
        sender.send(otp);
    }

    // Factory Method - subclasses decide what to create
    protected abstract OTPSender createSender();
}

// ============ CONCRETE CREATORS ============
class EmailOTPService extends OTPService {

    @Override
    protected OTPSender createSender() {
        return new EmailOTP(Config.smtpHost, Config.smtpPort);
    }
}

class SMSOTPService extends OTPService {

    @Override
    protected OTPSender createSender() {
        return new SMSOTP(Config.twilioKey, Config.twilioSid);
    }
}

class WhatsAppOTPService extends OTPService {

    @Override
    protected OTPSender createSender() {
        return new WhatsAppOTP(Config.waBusinessId, Config.waToken);
    }
}

// KEY DIFFERENCE FROM SIMPLE FACTORY:
// - Simple Factory: One class with switch statement
// - Factory Method: Inheritance-based, each subclass creates its product
//
// Adding TelegramOTP? Just create:
// 1. TelegramOTP implements OTPSender
// 2. TelegramOTPService extends OTPService
// No changes to existing classes!
