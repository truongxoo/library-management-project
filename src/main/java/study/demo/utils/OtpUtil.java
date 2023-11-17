package study.demo.utils;

import java.util.Random;

public class OtpUtil {

    public static String generateOtp() {
        Random random = new Random();
        return String.valueOf(random.nextInt(999999));
    }
}
