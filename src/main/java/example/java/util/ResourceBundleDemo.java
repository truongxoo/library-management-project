package example.java.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleDemo {
    public static void main(String[] args) {

        ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle", Locale.US);
        System.out.println("Message in " + Locale.US + ": " + bundle.getString("greeting"));

        Locale.setDefault(Locale.CHINESE);
        bundle = ResourceBundle.getBundle("MessageBundle");
        System.out.println("Message in " + Locale.CHINESE + ": " + bundle.getString("greeting"));

        Locale locale = new Locale("vi");
        bundle = ResourceBundle.getBundle("MessageBundle", locale);
        System.out.println("Message in " + locale + ": " + bundle.getString("greeting"));
        
        Locale locale1 = new Locale("vi","VN","VN");
        bundle = ResourceBundle.getBundle("MessageBundle", locale1);
        System.out.println("Message in " + locale + ": " + bundle.getString("greeting"));

    }

}
