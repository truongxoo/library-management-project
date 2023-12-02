package example.java.collection;

import java.util.Comparator;

public class StudentComparator implements Comparator<Student>{

    @Override
    public int compare(Student o1, Student o2) {
//        return o1.getAge().compareTo(o2.getAge());
        if(o1.getAge()<o2.getAge()) {
            return 1;   
        }else if(o1.getAge()>o2.getAge()) {
            return -1;
        }
        return 0;
    }

}
