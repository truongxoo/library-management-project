package example.java.util;

import java.util.Iterator;
import java.util.Vector;

public class VectorDemo {
    public static void main(String[] args) {
        Vector<String> vec1 = new Vector<>(4);
        vec1.add("E");
        vec1.add("F");
        vec1.add("G");

        Vector<String> vec2 = new Vector<>();
        vec2.add("A");
        vec2.add("B");
        vec2.add("C");
        vec1.addAll(0, vec2);
        
        System.out.println("Final vector list: " + vec1);
        System.out.println("The first animal of the vector is = " + vec1.firstElement());
        System.out.println("The last animal of the vector is = " + vec1.lastElement());
        System.out.println("Remove first occourence of element 200: "+vec1.remove("B"));  
        System.out.println("Remove element at index 4: " +vec1.remove(4));  
        vec1.removeElementAt(3);        
        System.out.println("Hash code of this vector = "+vec1.hashCode());  
        System.out.println("Element at index 1 is = "+vec1.get(2));  
      
        if (vec1.contains("A")) {
            System.out.println("A is present at the index " + vec1.indexOf("A"));
        } else {
            System.out.println("A is not present in the list.");
        }
        System.out.println("Is the Vector empty? = " +vec2.isEmpty());  
        
        Iterator<String> itr = vec1.iterator();  
        while(itr.hasNext()){  
             System.out.println(itr.next());  
        }   
        vec2.clear();   
    }
}
