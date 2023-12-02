package example.java.collection;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

enum Days {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}

public class SetDemo {

    public static void demoEnumset() {
        Set<Days> set1 = EnumSet.of(Days.MONDAY, Days.TUESDAY);
        System.out.println(set1);

        Set<Days> set2 = EnumSet.allOf(Days.class);
        System.out.println(set2);

        Set<Days> set3 = EnumSet.noneOf(Days.class);
        System.out.println(set3);
        
        Set<Days> set4= EnumSet.range(Days.MONDAY, Days.THURSDAY);
        System.out.println(set4);

    }

    public static void demoHashSet() {
        Set<Integer> set = new HashSet<>();
        set.add(5);
        set.add(1);
        set.add(3);
        set.add(9);
        
        Set<Integer> set2 = new HashSet<>();
        set2.add(5);
        set2.add(1);
        set2.add(4);
        
        System.out.println(set);

        set.add(5);
        System.out.println(set);

        set.remove(9);
        System.out.println("After removing: "+ set);
        
        set.retainAll(set2);
        System.out.println("After retainAll: "+set);
        
    }

    public static void demoLinkedHashSet() {
        Set<Integer> linkedHashset = new LinkedHashSet<>();
        linkedHashset.add(5);
        linkedHashset.add(1);
        linkedHashset.add(7);
        linkedHashset.add(3);

        System.out.println(linkedHashset);

        linkedHashset.add(7);
        System.out.println(linkedHashset);
        
        linkedHashset.clear();
        System.out.println("Is set empty: "+linkedHashset.isEmpty());
       

    }

    public static void demoTreeSet() {
        TreeSet<Student> set = new TreeSet<>();
        set.add(new Student("truong",24));
        set.add(new Student("truong1",25));
        System.out.println(set);
        
    }

    public static void main(String[] args) {
//        SetDemo.demoEnumset();
//        SetDemo.demoHashSet();
//        SetDemo.demoLinkedHashSet();
        SetDemo.demoTreeSet();
    }

}
