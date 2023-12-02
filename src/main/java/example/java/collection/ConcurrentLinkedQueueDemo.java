package example.java.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

class MyRunnable3 implements Runnable {

    protected ConcurrentLinkedQueue<Integer> queue = null;

    public MyRunnable3(ConcurrentLinkedQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(queue.poll());
            System.out.println(queue.poll());
            System.out.println(queue.poll());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyRunnable4 implements Runnable {

    protected ConcurrentLinkedQueue<Integer> queue = null;

    public MyRunnable4(ConcurrentLinkedQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            queue.add(6);
            queue.add(7);
            Thread.sleep(2000);
            queue.add(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ConcurrentLinkedQueueDemo {

    public static void main(String[] args) throws Exception {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

        MyRunnable3 mr1 = new MyRunnable3(queue);
        MyRunnable4 mr2 = new MyRunnable4(queue);

        new Thread(mr1).start();
        new Thread(mr2).start();

    }
}
