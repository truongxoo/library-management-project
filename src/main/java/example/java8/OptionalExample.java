package example.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javassist.compiler.ast.Member;

public class OptionalExample {

    public static Members getMember() {
        Members member = new Members("Truong", 24, "Nam");
        return null;
    }

    public static void main(String[] args) {
        List.of(new Members("Truong", 24, "Nam"),new Members("A", 25, "Nam")).stream().map(Members::getAge);
        

        Optional<Members> opt = Optional.ofNullable(getMember());
        Optional<Members> opt1 = Optional.empty();
        Optional<Members> opt2 = Optional.of(new Members("Phanh", 24, "Nư"));
        
        opt2.filter(s -> s.getAge() >18).map(s -> {
            s.setName("Lớn hơn 18");
            return s;
        });
        opt2.filter(s -> s.getAge() >18).orElseGet(()->new Members("A",18,"Nam"));
        
//        opt2.filter(s -> s.getAge() >18).map(Members::newmethod));
        
        opt.ifPresent(s -> System.out.println(opt1.get().getName()));
//        opt.orElseThrow(()-> new IllegalStateException("This should not empty"));

        Members member1 = opt1.orElse(new Members("Truong", 24, "Nam"));
        System.out.println(member1);
        
        // Being called only when Optional is empty
        Members member2 = opt2.orElseGet(() -> new Members("Truong", 24, "Nam"));
        System.out.println(member2);
        
        Optional<String> op3 = Optional.of("hello");
        Optional<String> op4 = Optional.empty();
 
        // Filter on Optional
        System.out.println(op3.filter(g -> g.equals("HELLO")));    // Optional.empty
        System.out.println(op3.filter(g -> g.equalsIgnoreCase("HELLO")));    // Optional[hello]
        System.out.println(op4.filter(g -> g.equalsIgnoreCase("hello")));    // Optional.empty
        
        Optional<Optional<String>> ss = Optional.of(Optional.of("test"));
        Optional<String>  string  = ss.flatMap(q -> q).filter(s -> s.equalsIgnoreCase("test"));
        System.out.println(string);
    }
}
