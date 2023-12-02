package example.java.collection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.PriorityQueue;

public class QueueDemo {
    public static void demoPriorityQueue() {
        PriorityQueue<String> queue = new PriorityQueue<String>();
        queue.add("A");
        queue.add("D");
        queue.add("V");
        queue.add("R");    // return true or throw exception if can't add
        queue.add("W");
        queue.offer("R"); 
        
            // return true or return false if can't add
        Iterator itr = queue.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
        System.out.println("The first elemnt :" + queue.element());    // retrieving the first element, throws
                                                                       // NoSuchElementException if empty
        System.out.println("The first elemnt :" + queue.peek());    // retrieving the first element,return false
                                                                    // if empty
        System.out.println("Iterating the queue elements:");
        
        System.out.println("retrieving the first element and remove: " + queue.remove());    // retrieving the first
                                                                                             // element and remove, throws
                                                                                             // e if queue null
        System.out.println("retrieving the first element and remove: " + queue.poll());    // retrieving the first element
                                                                                           // and remove, return null if
                                                                                           // queue is empty
        System.out.println("After removing two elements:");
        Iterator<String> itr2 = queue.iterator();
        while (itr2.hasNext()) {
            System.out.println(itr2.next());
        }
        PriorityQueue<Student> queue1 = new PriorityQueue<Student>(new StudentComparator());
        queue1.add(new Student("C",26));
        queue1.add(new Student("B",19));
        queue1.add(new Student("A",25));
        System.out.println(queue1);
        
        while (!queue1.isEmpty()) {
        System.out.println("-------"+queue1.poll().getName());
        }
    }

    public static void demoArrayDeque() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.add(1);
        deque.offer(2);
        System.out.println("First: " + deque.removeFirst() + ", Last: " + deque.removeLast());
        
        deque.addFirst(5);
        deque.addFirst(6);
        deque.addLast(7);
        deque.addLast(9);
        System.out.println(deque);

    }

    public static void main(String[] args) {
        System.out.println("--------------PriorityQueue Demo--------------");
        QueueDemo.demoPriorityQueue();
        System.out.println("--------------ArrayDeque Demo--------------");
        QueueDemo.demoArrayDeque();

    }
}
