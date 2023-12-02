package example.java.collection;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentDemo extends Thread {

    static CopyOnWriteArrayList<String> l = new CopyOnWriteArrayList<String>();

    public void run() {
        l.add("D");
    }

    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList<String> list=  new CopyOnWriteArrayList<String>();
        l.add("A");
        l.add("B");
        l.add("C");

      
        System.out.println(l);
    }

}
