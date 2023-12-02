package example.java8;

public class MyClass {

    public static void staticMethod() {
        // Tạo một thể hiện của AnotherClass bên trong một phương thức static
        AnotherClass obj = new AnotherClass();
        obj.nonStaticMethod();
    }

    public static void main(String[] args) {
        int a = 1;
        if( 1<0) {
            System.out.println(true);
        }else if(1<3){
            System.out.println("hee");
            if(1<2) {
                System.out.println("hh");
            }
        }else {
            
            
        }
    }
}

class AnotherClass {
    public void nonStaticMethod() {
        System.out.println("Phương thức không static của AnotherClass được gọi.");
    }
}