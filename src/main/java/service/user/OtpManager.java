package service.user;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
public class OtpManager {
    private static final Map<String, String> otpTokens = new HashMap<>();

    public static String generateOTP(String email) {
        SecureRandom secureRandom = new SecureRandom();
        int otpValue = secureRandom.nextInt(900000) + 100000;
        String otp = String.valueOf(otpValue);
        otpTokens.put(email, otp);
        System.out.println("the generated otp"+otpTokens.get(otp));
        System.out.println("if i do get email instead"+otpTokens.get(email));// Store OTP token temporarily
        return otp;
    }

    public static boolean verifyOTP(String email, String otp) {
        String storedOTP = otpTokens.get(email);
        if (storedOTP != null && storedOTP.equals(otp)) {
            otpTokens.remove(email); // Remove OTP token after successful verification
            return true;
        }
        return false;
    }
}
