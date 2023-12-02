package example.java.designpattern;

interface Image {
    void display();
}

class RealImage implements Image {

    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk(fileName);
    }

    @Override
    public void display() {
        System.out.println("Displaying: " + fileName);
    }

    private void loadFromDisk(String fileName) {
        System.out.println("Loading image from disk: " + fileName);
    }
}

class ProxyImage implements Image {

    private RealImage realImage;
    private String fileName;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}

public class ProxyPatternDemo2 {
    public static void main(String[] args) {
        Image image = new ProxyImage("image.png");
        System.out.println("Display image for the first time...........");
        image.display();
        System.out.println("Display image for the next time............");
        image.display();
    }
}
