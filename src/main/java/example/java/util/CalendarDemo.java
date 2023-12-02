package example.java.util;

import java.util.Calendar;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class CalendarDemo {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Now: " + calendar.getTime());
        calendar.add(Calendar.DATE, 10);
        System.out.println("10 days after: " + calendar.getTime());
        System.out.println("Year: " + calendar.get(Calendar.YEAR));
        System.out.println("Month: " + calendar.get(Calendar.MONTH));
        System.out.println("Days of week: " + calendar.get(Calendar.DAY_OF_WEEK));

        int maximum = calendar.getMaximum(Calendar.WEEK_OF_YEAR);
        System.out.println("Maximum number of days in  year: " + maximum);

        int minimum = calendar.getMinimum(Calendar.DAY_OF_WEEK);
        System.out.println("Minimum number of days in week: " + minimum);

        int firstDay = calendar.getFirstDayOfWeek();
        System.out.println("First day of week: " + firstDay);

        calendar.add(Calendar.DATE, 900);
        System.out.println("Week year:" + calendar.getWeekYear());

        Calendar future = Calendar.getInstance();
        future.set(Calendar.YEAR, 2024);
        future.set(Calendar.MONTH, Calendar.DECEMBER);
        future.set(Calendar.HOUR_OF_DAY, 10);
        future.set(Calendar.MINUTE, 2);
        future.set(Calendar.SECOND, 2);
        System.out.println(calendar.after(future));

    }

}
