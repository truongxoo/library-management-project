package example.java.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


public class PropertiesDemo {
    public static final String FILE_PATH = "D:\\TESTJAVA\\Study\\src\\main\\resources\\";

    public static Properties readPropertiesFile(String filePath, String fileName) throws IOException {
        FileReader reader = null;
        Properties pro = new Properties();
        try {
            reader = new FileReader(filePath + fileName);
            pro.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return pro;
    }

    public static void createNewPropertiesFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath + fileName);
        FileWriter writer = null;
        Properties pro = new Properties();
        pro.setProperty("name", "Truong");
        pro.setProperty("age", "24");
        pro.setProperty("sex", "Male");
        try {
            if(file.exists()) {
                System.out.println("File already exist");
                return;
            }
            writer = new FileWriter(file);
            pro.store(writer, "This file is created by Truong");
            System.out.println("Successful!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    public static void readAllSystemProperties() {
        Properties pro = System.getProperties();
        Set set = pro.entrySet();
        Iterator itr = set.iterator();
        while(itr.hasNext()) {
            Map.Entry<String, String> entry = (Entry<String, String>) itr.next();
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        Properties pro = PropertiesDemo.readPropertiesFile(FILE_PATH, "db.properties");
        System.out.println("User: " + pro.getProperty("user"));
        System.out.println("Password: " + pro.getProperty("password"));
        System.out.println("----------------------");
        PropertiesDemo.createNewPropertiesFile(FILE_PATH, "newFile.properties");
        PropertiesDemo.readAllSystemProperties();
    }
}
