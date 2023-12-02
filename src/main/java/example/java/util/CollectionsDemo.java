package example.java.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CollectionsDemo {
    public static void main(String[] args) {
        List<String> items = new ArrayList<>();
        items.add("A");
        items.add("F");

        List<String> items1 = new ArrayList<>();
        items1.add("P");
        items1.add("O");

        Collections.addAll(items, "G", "D", "L","A");
        System.out.println(items);
        
        Collections.sort(items);
        System.out.println(items);
        
        System.out.println("The index of F is " + Collections.binarySearch(items, "F"));
        
        Collections.sort(items, Collections.reverseOrder());
        System.out.println(items);

        Collections.copy(items, items1);
        Iterator itr = items.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
        
        boolean check =Collections.disjoint(items, items1); 
        System.out.println(check);
        
        System.out.println("The frequency of the word code is: "+Collections.frequency(items, "A"));
    }

}
