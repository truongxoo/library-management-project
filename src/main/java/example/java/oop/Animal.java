package example.java.oop;

public abstract class Animal {
    protected final String TYPE = "ANIMAL" ; 
     
    protected abstract void doSomething();
    
    protected void doAnything() {
        System.out.println("doing.....");
    }

    public String getTYPE() {
        return TYPE;
    }
}
