// JUNIOR'S CODE - Abstract Factory Pattern
// Junior owns: Product interfaces + Concrete products + Abstract Factory + Concrete Factories

// ============ PRODUCT INTERFACES ============
interface SMSSender {

    void sendSMS(String otp);
}

interface WhatsAppSender {

    void sendWhatsApp(String otp);
}

interface EmailSender {

    void sendEmail(String otp);
}

// ============ INDIA PRODUCTS (Family 1) ============
class IndianSMS implements SMSSender {

    public IndianSMS() {
        System.out.println("   [IndianSMS] Initialized with Indian SMS gateway");
    }

    @Override
    public void sendSMS(String otp) {
        System.out.println("   [IndianSMS] Sent SMS OTP: " + otp);
    }
}

class IndianWhatsApp implements WhatsAppSender {

    public IndianWhatsApp() {
        System.out.println("   [IndianWhatsApp] Connected to Indian WhatsApp Business");
    }

    @Override
    public void sendWhatsApp(String otp) {
        System.out.println("   [IndianWhatsApp] Sent WhatsApp OTP: " + otp);
    }
}

class IndianEmail implements EmailSender {

    public IndianEmail() {
        System.out.println("   [IndianEmail] Using Indian SMTP server");
    }

    @Override
    public void sendEmail(String otp) {
        System.out.println("   [IndianEmail] Sent Email OTP: " + otp);
    }
}

// ============ USA PRODUCTS (Family 2) ============
class USASMS implements SMSSender {

    public USASMS() {
        System.out.println("   [USASMS] Initialized with US SMS gateway");
    }

    @Override
    public void sendSMS(String otp) {
        System.out.println("   [USASMS] Sent SMS OTP: " + otp);
    }
}

class USAWhatsApp implements WhatsAppSender {

    public USAWhatsApp() {
        System.out.println("   [USAWhatsApp] Connected to US WhatsApp Business");
    }

    @Override
    public void sendWhatsApp(String otp) {
        System.out.println("   [USAWhatsApp] Sent WhatsApp OTP: " + otp);
    }
}

class USAEmail implements EmailSender {

    public USAEmail() {
        System.out.println("   [USAEmail] Using US SMTP server");
    }

    @Override
    public void sendEmail(String otp) {
        System.out.println("   [USAEmail] Sent Email OTP: " + otp);
    }
}

// ============ ABSTRACT FACTORY ============
interface OTPFactory {

    SMSSender createSMSSender();

    WhatsAppSender createWhatsAppSender();

    EmailSender createEmailSender();
}

// ============ CONCRETE FACTORIES ============
class IndiaOTPFactory implements OTPFactory {

    @Override
    public SMSSender createSMSSender() {
        return new IndianSMS();
    }

    @Override
    public WhatsAppSender createWhatsAppSender() {
        return new IndianWhatsApp();
    }

    @Override
    public EmailSender createEmailSender() {
        return new IndianEmail();
    }
}

class USAOTPFactory implements OTPFactory {

    @Override
    public SMSSender createSMSSender() {
        return new USASMS();
    }

    @Override
    public WhatsAppSender createWhatsAppSender() {
        return new USAWhatsApp();
    }

    @Override
    public EmailSender createEmailSender() {
        return new USAEmail();
    }
}

// KEY INSIGHT:
// - Each factory creates a FAMILY of compatible products
// - IndiaOTPFactory → all Indian providers (IndianSMS, IndianWhatsApp, IndianEmail)
// - USAOTPFactory → all US providers (USASMS, USAWhatsApp, USAEmail)
//
// You can NEVER accidentally mix USASMS with IndianWhatsApp!
// The factory ensures all products in a family work together.
