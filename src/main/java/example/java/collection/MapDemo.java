package example.java.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MapDemo {

    public static void demoHashMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();

        hashMap.put("A", 10);
        hashMap.put("B", 30);
        hashMap.put("C", 20);

        System.out.println("Size of map is: " + hashMap.size());
        System.out.println(hashMap);

        if (hashMap.containsKey("B")) {
            System.out.println("Value of key 'B':" + hashMap.get("B"));
        } else {
            System.out.println("B is not present");
        }

        hashMap.remove("A");
        for (Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

    }

    public static void demoLinkedHashMap() {

        LinkedHashMap<String, Integer> lhm = new LinkedHashMap<String, Integer>();

        lhm.put("A", 10);
        lhm.put("B", 30);
        lhm.put("C", 20);
        System.out.println(lhm);

        System.out.println("Getting value for key 'A': " + lhm.get("A"));

        System.out.println("Is linkedHashMap empty? " + lhm.isEmpty());

        System.out.println("Contains key 'C'? " + lhm.containsKey("C"));

        System.out.println("Contains value '20'? " + lhm.containsValue(20));

    }

    public static void demoTreeMap() {
        Map<String, Integer> treeMap = new TreeMap<>();

        treeMap.put("A", 10);
        treeMap.put("C", 30);
        treeMap.put("B", 20);

        System.out.println("Value of A: " + treeMap.get("A"));

        treeMap.remove("B");
        for (String key : treeMap.keySet()) {
            System.out.println("Key: " + key + ", Value: " + treeMap.get(key));
        }
        
        for(Map.Entry m: treeMap.entrySet()){    
            System.out.println(m.getKey()+" : "+m.getValue());    
           }    
        
    }

    public static void main(String[] args) {
        MapDemo.demoHashMap();
        System.out.println("---------------");
        MapDemo.demoLinkedHashMap();
        System.out.println("---------------");
        MapDemo.demoTreeMap();
    }

}
