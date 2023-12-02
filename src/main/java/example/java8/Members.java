package example.java8;

import java.util.Objects;

public class Members {
    private String name;
    private int age;
    private String sex;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Members(String name, int age, String sex) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    

    public Members() {
        super();
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(age, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Members) {
            Members another = (Members) obj;
            if (this.name.equals(another.name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Members [name=" + name + ", age=" + age + ", sex=" + sex + "]";
    }
    
    public static Members newmethod(Members old) {
        old.setName("naaaa");
        return old;
    }
}
