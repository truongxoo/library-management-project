package example.java8;

@FunctionalInterface
interface FunctionallInterface {
    void doSomething();
    int hashCode();
    String toString();
    boolean equals(Object obj);
    //có thể có các phương thức của lớp java.lang.Object.
    
    default void defaultMethod2() {
    }
    static void staticMethod() {
    }
    //có thể có phương thức static và default
    //có thể mở rộng một Interface khác chỉ khi nó không có bất kỳ phương thức trừu tượng nào.
}
/*
 * map() để biến đổi phần tử 1-1, nên nó dùng Function<T, R>,nhận vào một tham
 * số và trả về một giá trị. filter() để lọc ra các phần tử phù hợp, nên nó dùng
 * Predicate<T>,nhận vào một tham số và trả về giá trị boolean. forEach() chỉ
 * dùng in ra thôi, không return gì nữa, nên nó dùng Consumer<T>
 */
