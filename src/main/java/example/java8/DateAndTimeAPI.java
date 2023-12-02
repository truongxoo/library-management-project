package example.java8;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.apache.poi.util.SystemOutLogger;

public class DateAndTimeAPI {
    public static void main(String[] args) {

        LocalDate today = LocalDate.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd:MM:yyyy");
        System.out.println("Date after formatting: "+today.format(pattern));
        System.out.println("Current date plus 2 days = " + today.plus(2,ChronoUnit.WEEKS));
        String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));   
        System.out.println("FormatStyle.LONG: "+formattedDate);
        formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));  
        System.out.println("FormatStyle.MEDIUM: "+formattedDate);
        formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));  
        System.out.println("FormatStyle.SHORT: "+formattedDate);
        formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));  
        System.out.println("FormatStyle.FULL: "+formattedDate);
        LocalDate firstDay_2023 = LocalDate.of(2023, Month.JANUARY, 1);
        System.out.println("Specific Date = " + firstDay_2023);

        // Current date in "Asia/Ho_Chi_Minh", you can get it from ZoneId javadoc
        LocalDate todayHCM = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        System.out.println("Current Date in IST = " + todayHCM);

        // Obtains an instance of LocalDate from a year and day-of-year
        LocalDate hundredDay2023 = LocalDate.ofYearDay(2023, 100);
        System.out.println("100th day of 2023 = " + hundredDay2023);
        
        LocalTime time = LocalTime.now();
        System.out.println("Current Time=" + time);
 
        // LocalTime.of(int hour, int minute, int second, int nanoOfSecond)
        LocalTime specificTime = LocalTime.of(12, 20, 25, 40);
        System.out.println("Specific Time of Day = " + specificTime);
 
        // Current date in "Asia/Ho_Chi_Minh", you can get it from ZoneId javadoc
        LocalTime timeHCM = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        System.out.println("Current Time in IST = " + timeHCM);
 
        // Getting date from the base date 
        LocalTime specificSecondTime = LocalTime.ofSecondOfDay(10000);
        System.out.println("10000th second time = " + specificSecondTime);
        
        // Get the Year, check if it's leap year   
        LocalDate today1 = LocalDate.now();
        System.out.println("Year " + today.getYear() + " is Leap Year? " + today1.isLeapYear());
        // Compare two LocalDate for before and after
        System.out.println(today1.isBefore(LocalDate.of(2023, 10, 10)));
        System.out.println("10 days after today will be " + today.plusDays(10));
        
        // Current timestamp
        Instant now = Instant.now();
        System.out.println("Date time now: "+now);
        
        Duration thirtyDay = Duration.ofDays(30);
        System.out.println("Duration: "+thirtyDay);
        
        DayOfWeek monday = DayOfWeek.MONDAY;
        System.out.println(monday); // MONDAY
        
        Year currentYear = Year.now();
        System.out.println("currentYear: " + currentYear);
        
        String string = "January 1, 2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(string, formatter);
//        LocalDate date1= LocalDate.parse("January 1, 2023",DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        System.out.println(date);
        
    }

}
