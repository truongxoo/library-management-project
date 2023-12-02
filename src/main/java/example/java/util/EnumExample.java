package example.java.util;

enum Season {
    WINTER(1), SPRING(2), SUMMER(3), FALL(4);  
    
    private int value;
    
    Season(int value) {
        this.value = value;
    }
    public static Season getSeasonByValue(int value) {
        for (Season d : Season.values()) {
            if (d.value == value) {
                return d;
            }
        }
        return null;
    }
}

public class EnumExample {

    enum WeekDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    }

    public static void main(String[] args) {
        WeekDay d = WeekDay.MONDAY;
        System.out.println(d);
        
        for(Season s: Season.values()) {
            System.out.println(s);
        }
        
        Season season = Season.getSeasonByValue(2);
        System.out.println(season);
        
        switch (d) {
        case MONDAY:
            System.out.println("Today is working day");
            break;
        case SUNDAY:
            System.out.println("Today is a day off");
            break;
        default:
            System.out.println(d);
        }
    }
}
