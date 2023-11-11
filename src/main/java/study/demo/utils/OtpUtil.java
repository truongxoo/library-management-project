package study.demo.utils;

import java.util.Random;

public class OtpUtil {

    public static String generateOtp() {
        Random random = new Random();
        String otp = String.valueOf(random.nextInt(999999));
        return otp;
    }
}
