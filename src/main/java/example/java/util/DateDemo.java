package example.java.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDemo {
    public static void main(String[] args) {

        Date date1 = new Date();
        System.out.println(date1);
        
        long millis = System.currentTimeMillis();
        Date date2 = new Date(millis);
        System.out.println(date2);

        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a");
        System.out.println("Date hien tai: " + ft.format(date1.getTime()));

        boolean before = date1.before(date2);
        System.out.println("date1 before date2? " + before);

        boolean after = date1.after(date2);
        System.out.println("date1 after date2? " + after);

        int compare = date1.compareTo(date2);
        if (compare < 0) {
            System.out.println("date1 before date2 ");
        } else if (compare == 0) {
            System.out.println("date1 equal date2 ");
        } else {
            System.out.println("date1 after date2 ");
        }
    }
}
