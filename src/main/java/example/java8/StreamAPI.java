package example.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StreamAPI {
    public static void main(String[] args) {

        List<Integer> listNumbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<String> listStrings = Arrays.asList("Java", "C#", "C++", "PHP", "Javascript");
        List<Members> users = new ArrayList<>();
        users.add(new Members("Truong", 24, "Nam"));
        users.add(new Members("Phanh1", 25, "Nam"));
        users.add(new Members("Truong1", 25, "Nam"));
        users.add(new Members("Truong1", 25, "Nam"));

        Map<String, Members> members = users.stream().distinct().map(s -> {
            s.setAge(s.getAge() + 1);
            return s;
        }).collect(Collectors.toMap(Members::getName, Function.identity()));
        for (Entry<String, Members> entry : members.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        String string = List.of(new User("John", "Kena", 19), new User("Lisa", "Mody", 19), new User("Bera", "Tin", 20))
                .stream().filter(s -> s.getAge() > 18)
                .filter(s -> s.getFirstName().equalsIgnoreCase("John"))
                .findFirst()
                .map(s -> s.getFirstName() + "" + s.getLastName())
                .orElseGet(() -> "Unknow");
        System.out.println(string);

        // using reduce, paralelstream
        String result = listStrings.stream().reduce("I'm learning ", (substring, element) -> substring + element);
        String result2 = listStrings.parallelStream().reduce("I'm learning ", String::concat);
        System.out.println(result);     // I'm learning JavaC#C++PHPJavascript
        System.out.println(result2);     // I'm learning JavaI'm learning C#I'm learning C++I'm learning PHPI'm learning
                                         // Javascript

        // using map,filter, limit, forEach, skip
        listNumbers.stream().skip(1).map(num -> num * 2).filter(num -> num < 10).limit(2)
                .forEach(num -> System.out.println(num));

        // using anyMatch
        boolean rst = listStrings.stream().anyMatch((s) -> s.equalsIgnoreCase("Java"));
        System.out.println(rst);     // true

        // using max,min
        int max = listNumbers.stream().mapToInt(a -> a).max().getAsInt();
        int min = listNumbers.stream().max(Integer::compare).get();
        System.out.println(max);     // 6
        System.out.println(max);     // 1

        // using count
        long count = listNumbers.stream().filter(num -> num % 3 == 0).count();
        System.out.println(count);
        
        // searching user with filter
        users.stream().filter(s -> s.getAge() > 24).forEach(u -> System.out.println(u.toString()));
        
        // sorting user
        users.stream().sorted(Comparator.comparingInt(u -> u.getAge())).forEach(System.out::println);
        
        Members user1 = users.stream().findAny().orElse(null);
        System.out.println(user1);
        
        // converting list to map
        Map<String, String> map = listStrings.stream().collect(Collectors.toMap(Function.identity().andThen(s -> s+"truong"),Function.identity()));
        for(Entry<String, String> s : map.entrySet()) {
            System.out.println(s.getKey()+":"+s.getValue());
        }
        Map<Integer, String> map1 = users.stream().collect(Collectors.toMap(s->s.getAge() , s -> s.getName()));
        for(Entry<String, String> s : map.entrySet()) {
            System.out.println(s.getKey()+":"+s.getValue());
        }
        
        // Generate Streams using Stream.generate()
        String[] testStrArr =listStrings.stream().toArray(String[]::new);
            
        // combine with pattern
        String str = "Hello,world";
        Pattern.compile(",").splitAsStream(str).forEach(System.out::print);     // Helloworld
        
    }
}
