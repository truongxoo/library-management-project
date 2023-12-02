package example.java.designpattern;

class Computer implements Cloneable {
    private String name;
    private String os;
    private String office;
    private String others;
    
    public void setOs(String os) {
        this.os = os;
    }

    public String getOffice() {
        return office;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Computer(String name, String os, String office) {
        super();
        this.name = name;
        this.os = os;
        this.office = office;
    }

    @Override
    protected Computer clone() {
        try {
            return (Computer) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Computer [name=" + name + ", os=" + os + ", office=" + office + ", others=" + others + "]";
    }

}

public class PrototypePatternDemo {
    
    public static void main(String[] args) {
        Computer computer1 = new Computer("Lenovo", "Win 11", "2019");
        Computer computer2 = computer1.clone();
        computer2.setOthers("Photoshop");
        System.out.println(computer1);  
        System.out.println(computer2);
    }
}
