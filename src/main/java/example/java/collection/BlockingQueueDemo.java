package example.java.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class MyRunnable1 implements Runnable {

    protected BlockingQueue<String> queue = null;

    public MyRunnable1(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyRunnable2 implements Runnable {

    protected BlockingQueue<String> queue = null;

    public MyRunnable2(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            queue.put("100");
            Thread.sleep(1000);
            queue.put("200");
            Thread.sleep(1000);
            queue.put("300");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class BlockingQueueDemo {


    public static void main(String[] args) throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);

        MyRunnable1 mr1 = new MyRunnable1(queue);
        MyRunnable2 mr2 = new MyRunnable2(queue);

        new Thread(mr1).start();
        new Thread(mr2).start();

    }
}
