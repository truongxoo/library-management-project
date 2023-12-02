package example.javaio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExportPDF {
    private static Map<String, Object> createTestData() {
        Map<String, Object> data = new HashMap<>();

        List<Users> users = new ArrayList<>();
        Users user = new Users();
        user.setName("Truong");
        user.setAge(24.0);
        user.setSex("Male");
        users.add(user);

        Users user1 = new Users();
        user1.setName("Phanh");
        user1.setAge(24.0);
        user1.setSex("Female");
        users.add(user1);

        Users user2 = new Users();
        user2.setName("Xo");
        user2.setAge(25.0);
        user2.setSex("Male");
        users.add(user2);

        data.put("users", users);
        return data;
    }

    public static void main(String[] args) throws IOException {
        ExportPDF pdfFileExporter = new ExportPDF();
        Map<String, Object> data = createTestData();
        String pdfFileName = "D:\\table.pdf";
        pdfFileExporter.exportPdfFile("table", data, pdfFileName);
    }

}
