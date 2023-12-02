package example.java.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListDemo {

    public static void demoArrayList() {
        List<Integer> arrList = new ArrayList<Integer>();
        arrList.add(5);
        arrList.add(2);
        arrList.add(0, 2);
        arrList.add(1, 1);
        System.out.println(arrList);

        List<Integer> arrList1 = Collections.synchronizedList(new ArrayList());
        arrList1.add(1);
        arrList1.add(2);
        arrList1.add(3);
        
        synchronized (arrList1) {
            Iterator i = arrList1.iterator();
            while (i.hasNext()) {
                System.out.println(i.next());
            }

        arrList.addAll(1, arrList1);
        System.out.println(arrList);

        System.out.println(arrList.get(3));

        arrList.set(4, 777);
        System.out.println(arrList);

        int index = arrList.indexOf(2);
        System.out.println("The first occurrence of 2 is at index " + index);
        
        
        }
        List<String> list = new CopyOnWriteArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        
        Iterator<String> iterator1 = list.iterator();
        
        list.add("AA");
        list.add("BB");
        
        Iterator<String> iterator2 = list.iterator();
        
        System.out.println("--- Iterator 1: -----");
        while(iterator1.hasNext()) {
            System.out.println(iterator1.next());
        }
        
        System.out.println("--- Iterator 2: -----");
        while(iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }

    }

    public static void demoLinkedList() {
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("A");
        linkedList.add("V");
        linkedList.add("S");
        linkedList.add("R");

        Iterator<String> itr = linkedList.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        linkedList.addFirst("P");
        System.out.println("After invoking addFirst method: " + linkedList);

        linkedList.addLast("H");
        System.out.println("After invoking addLast method: " + linkedList);

    }

    public static void demoStack() {
        Stack<Integer> stack = new Stack<>();
        boolean result = stack.empty();
        System.out.println("Is the stack empty? " + result);
        stack.push(1);
        stack.push(5);
        stack.push(6);
        System.out.println(stack);

        System.out.println("Location of 5: " + stack.search(5));

        stack.peek();
        System.out.println("After invoking peek() method:" + stack);

        stack.pop();
        System.out.println("After invoking pop() method:" + stack);

        stack.removeIf(element -> element < 5);
        System.out.println("After invoking removeIf method:" + stack);
    }

    public static void main(String[] args) {
        System.out.println("--------Demo Arraylist-------");
        ListDemo.demoArrayList();
        System.out.println("--------Demo LinkedList-------");
        ListDemo.demoLinkedList();
        System.out.println("--------Demo Stack-------");
        ListDemo.demoStack();
    }

}
