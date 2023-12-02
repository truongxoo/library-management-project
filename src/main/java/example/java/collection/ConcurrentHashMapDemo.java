package example.java.collection;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDemo extends Thread {

    static ConcurrentHashMap<Integer, String> m = new ConcurrentHashMap<Integer, String>();

    public void run() {
        System.out.println("Child thread is updating.....");
        m.put(4, "D");
    }

    public static void main(String arg[]) throws InterruptedException {
        m.put(1, "A");
        m.put(2, "B");
        m.put(3, "C"); 

        Set<Integer> s1 = m.keySet();
        Iterator<Integer> itr = s1.iterator();

        ConcurrentHashMapDemo t = new ConcurrentHashMapDemo();
        while (itr.hasNext()) {
            Integer i = itr.next();
            System.out.println(i + ": " + m.get(i));
            if (i ==2) {
                t.start();
            }
            Thread.sleep(1000);
        }
        System.out.println(m);
        
    }
}
