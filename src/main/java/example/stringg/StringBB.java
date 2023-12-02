package example.stringg;

public class StringBB {
    static void testStringBuilder() {
        StringBuilder sbD = new StringBuilder();
        Runnable run2 = ()->{
            for(int i=0;i<1000;i++) {
                sbD.append("A");
            }
        };
        Thread thread3 = new Thread(run2);
        Thread thread4 = new Thread(run2);
        thread3.start();
        thread4.start();
        try {
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("String Builder: " + sbD.length());
    }
    public static void testStringBuffer() {
        StringBuffer sbB = new StringBuffer();

        Runnable run1 = () -> {
            for (int i = 0; i < 1000; i++) {
                sbB.append("A");
            }
        };
        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run1);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("String Buffer: " + sbB.length());
    }
    public static void main(String[] args) {

        String s1 = "Hello";
        String s2 = s1;
        s1 = s1 + "world";

        StringBuffer sb = new StringBuffer("Hello");
        System.out.println(sb.capacity()); // dung lượng bộ nhớ đệm 16
        sb.append("Word I come from Viet Nam");
        System.out.println(sb.capacity()); // dung lượng bộ nhớ đệm (16*2)+2
        sb.replace(1, 3, "Hi");
        sb.delete(5, 8);
        sb.insert(1, "Truong");
        System.out.println(sb);

        StringBB.testStringBuffer();
//        StringBB.testStringBuilder();
    }
}
