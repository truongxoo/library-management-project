package example.java8;

import static java.lang.Math.random;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
interface Sayable {
    public String say(String name);
}

@FunctionalInterface
interface CustomFuncIterface {
    // Can user methods of java.lang.Object.
    int hashCode();

    String toString();

    boolean equals(Object obj);

    default void defaultMethod() {
        System.out.println("default method");
    }

    int add(int a, int b, int c);

    static void staticMethod() {
    }

    // Can extends other interface if it has no abstract method itself.
}


@FunctionalInterface
interface TriFunction<A, B, C, R> {

    R apply(A a, B b, C c);

    default <V> TriFunction<A, B, C, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c) -> after.apply(apply(a, b, c));
    }
}

public class Java8Example implements CustomFuncIterface {

    @Override
    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // lambda expression
        Sayable s1 = (name) -> {
            return "Hello, " + name;
        };
        System.out.println(s1.say("Truong"));

        Sayable s3 = name -> "Hello, " + name;
        System.out.println(s3.say("Truong"));

        CustomFuncIterface add = (a, b, c) -> {
            int sum = (a + b + c);
            return sum;
        };
        System.out.println(add.add(10, 20, 7));

        // lambda expression with foreach
        List<String> languages = Arrays.asList("Java", "C#", "Python", "PHP", "Javascript");
        languages.forEach(n -> System.out.println(n));
        /*
         * map() để biến đổi phần tử 1-1, nên nó dùng Function<T, R>,nhận vào một tham
         * số và trả về một giá trị. filter() để lọc ra các phần tử phù hợp, nên nó dùng
         * Predicate<T>,nhận vào một tham số và trả về giá trị boolean. forEach() chỉ
         * dùng in ra thôi, không return gì nữa, nên nó dùng Consumer<T>
         */
        Function<String, Integer> numberConverter = (str) -> Integer.parseInt(str);
        System.out.println(numberConverter.apply("1"));

        Consumer<Integer> print = num -> System.out.println(num);
        print.accept(3);

        Predicate<Integer> checkAge = age -> age > 18;
        System.out.println(checkAge.test(10));

        Supplier<Double> generate = () -> Math.random();
        System.out.println(generate.get());

        TriFunction<String, Integer, Long, String> tri = (x, y, z) -> "" + x + "," + y + "," + z;

        System.out.println(tri.apply("String", 1, 2L)); 

        tri = tri.andThen(s -> "[" + s + "]"); 
        System.out.println(tri.apply("String", 2, 3L)); 
        
        
        ForkJoinPool cus =new ForkJoinPool(2);
        
        List<Integer> ages = Arrays.asList(5, 5, 5, 5, 5,5);
        Integer computedAges = cus.submit(() -> ages.parallelStream().reduce(1, (a, b) -> a + b)).get();
        int computedAges1 = ages.stream().reduce(1,(a,b) -> a + b);
        System.out.println(computedAges);
        System.out.println(computedAges1);

    }

}