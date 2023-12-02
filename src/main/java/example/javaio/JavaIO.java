package example.javaio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class JavaIO {

    public static void main(String[] args) throws IOException {
        // write file to destination
        DataInputStream dis = new DataInputStream(System.in);
        FileInputStream fin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        BufferedInputStream bfr = null;
        
        try {
            fout = new FileOutputStream("data.txt");
            bout = new BufferedOutputStream(fout, 1024);
            System.out.println("Enter text (@ at the end):");
            char ch;
            while ((ch = (char) dis.read()) != '@') {
                bout.write(ch);
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } finally {
            bout.close();
        }
        
        // read file from source
        try {
            Path path = FileSystems.getDefault().getPath(new String()).toAbsolutePath();
            fin = new FileInputStream("data.txt");
            bfr = new BufferedInputStream(fin);
            while (bfr.available() > 0) {
                System.out.print((char) bfr.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bfr.close();
    }
}
