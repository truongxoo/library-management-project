package example.java.oop.inheritance;

import example.java.oop.Animal;
import example.java.oop.InterfaceDemo;

public class Cat extends Animal implements InterfaceDemo {
    private String name;
    private int age;
    private String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Cat() {
        super();
    }

    public Cat(String name, int age, String color) {
        super();
        this.name = name;
        this.age = age;
        this.color = color;
    }
    @Override
    protected void doSomething() {
        System.out.println(this.name+ " Meowww");
    }

    @Override
    public void catchesMouse() {
        System.out.println(this.name+" is catching.....");
    }

    public static void main(String[] args) {
        Cat cat = new Cat("Kitty",2,"White");
        System.out.println(cat.TYPE);
        cat.doSomething();
        cat.catchesMouse();
        cat.doAnything();
    }
}
