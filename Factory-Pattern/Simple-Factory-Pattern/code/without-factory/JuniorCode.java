// JUNIOR'S CODE - Without Factory Pattern

class EmailOTP {

    public EmailOTP(String host, int port) {
    }

    public void sendOTP(String otp) {
        System.out.println("   [EmailOTP] Sent OTP: " + otp);
    }
}

class SMSOTP {

    public SMSOTP(String key, String sid) {
    }

    public void sendOTP(String otp) {
        System.out.println("   [SMSOTP] Sent OTP: " + otp);
    }
}

class WhatsAppOTP {

    public WhatsAppOTP(String id, String token) {
    }

    public void sendOTP(String otp) {
        System.out.println("   [WhatsAppOTP] Sent OTP: " + otp);
    }
}

class TelegramOTP {

    public TelegramOTP(String id, String token) {
    }

    public void sendOTP(String otp) {
        System.out.println("   [TelegramOTP] Sent OTP: " + otp);
    }
}
