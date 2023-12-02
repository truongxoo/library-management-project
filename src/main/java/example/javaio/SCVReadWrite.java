package example.javaio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class SCVReadWrite {
    public static final String FILE_PATH = "D:\\TESTJAVA\\Study\\";

    public static void writeCSV(String filePath, String fileName) throws IOException {
        FileWriter fw = null;
        List<Users> users = new ArrayList<Users>();
        users.add(new Users("Truong", 24.0, "Nam"));
        users.add(new Users("Truong1", 25.0, "Nam"));
        users.add(new Users("Truong2", 26.0, "Nam"));
        try {
            fw = new FileWriter(filePath + fileName);
            fw.append("Name, Age, Sex");
            fw.append("\n");
            for (Users user : users) {
                fw.append(user.getName());
                fw.append(",");
                fw.append(String.valueOf(user.getAge()));
                fw.append(",");
                fw.append(user.getSex());
                fw.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fw.close();
        }
    }

    public static void readCSV(String filePath, String fileName) throws FileNotFoundException, IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
            for (List<String> str : records) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCSVWithOpenCSV(String filePath, String fileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(filePath + fileName));
        String[] line1 = { "Name", "Age", "Sex" };
        String[] line2 = { "A", "24", "Nam" };
        String[] line3 = { "B", "25", "Nam" };
        String[] line4 = { "Truong3", "26", "Nam" };

        List<String[]> list = new ArrayList<String[]>();
        list.add(line1);
        list.add(line2);
        list.add(line3);
        list.add(line4);

        writer.writeAll(list);
        writer.flush();
        writer.close();
    }

    public static void readCSVWithOpenCSV(String filePath, String fileName) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath + fileName));
        String line[];
        while ((line = reader.readNext()) != null) {
            for (int i = 0; i < line.length; i++) {
                System.out.print(line[i] + " ");
            }
            System.out.println(" ");
        }
    }

    public static void main(String[] args) throws IOException {
//		SCVReadWrite.writeCSV(filePath);
//		SCVReadWrite.readCSV(filePath);
        SCVReadWrite.writeCSVWithOpenCSV(FILE_PATH, "output.csv");
        SCVReadWrite.readCSVWithOpenCSV(FILE_PATH, "output.csv");
    }
}
