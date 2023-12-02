package example.javaio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.crypto.engines.AESLightEngine;

public class ExcelReadWrite {
    private static final String FILE_PATH = "D:\\Test.xlsx";
    public static final int COLUMN_INDEX_NAME = 0;
    public static final int COLUMN_INDEX_AGE = 1;
    public static final int COLUMN_INDEX_SEX = 2;

    private static List<Users> readFileExcel(String filePath) throws IOException {
        List<Users> users = new ArrayList<>();
        FileInputStream fin = null;
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            fin = new FileInputStream(filePath);
            workbook = getWorkbook(fin, filePath);
            sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            while (it.hasNext()) {
                Row nextRow = it.next();
                if (nextRow.getRowNum() == 0) {
                    continue;
                }
                Iterator<Cell> celIt = nextRow.cellIterator();
                Users user = new Users();
                while (celIt.hasNext()) {
                    Cell cell = celIt.next();
                    Object cellValue = getCellValue(cell);
                    if (cellValue == null || cellValue.toString().isEmpty()) {
                        continue;
                    }
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                    case COLUMN_INDEX_NAME:
                        user.setName((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_AGE:
                        user.setAge(((Double) getCellValue(cell)));
                        break;
                    case COLUMN_INDEX_SEX:
                        user.setSex((String) getCellValue(cell));
                        break;
                    default:
                        break;
                    }
                }
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found:" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
            fin.close();
        }
        return users;
    }

    private static Workbook getWorkbook(FileInputStream fin, String filePath) throws IOException {
        Workbook wb = null;
        if (filePath.endsWith("xlsx")) {
            wb = new XSSFWorkbook(fin);
        } else if (filePath.endsWith("xls")) {
            wb = new HSSFWorkbook(fin);
        } else {
            throw new IllegalArgumentException("This specified file was not Excel file");
        }
        return wb;
    }

    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
        case NUMERIC:
            cellValue = cell.getNumericCellValue();
            break;
        case STRING:
            cellValue = cell.getStringCellValue();
            break;
        default:
            break;
        }
        return cellValue;
    }

    private static Workbook getWorkBookforWrite(String filePath) {
        try (FileInputStream fin = new FileInputStream(filePath)) {
            Workbook workBook = new XSSFWorkbook(fin);
            return workBook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeExcel(List<Users> users, String filePath, String nameSheet) throws IOException {
        Sheet sheet = null;
        File file = null;
        file = new File(filePath);
        Workbook workbook = null;
        FileOutputStream fout = null;

        try {
            file = new File(filePath);
            if (file.exists()) {
                workbook = getWorkBookforWrite(filePath);
                fout = new FileOutputStream(filePath);
                int sheetcount = workbook.getNumberOfSheets();
                for (int i = 0; i < sheetcount; i++) {
                    if (workbook.getSheetName(i).equalsIgnoreCase(nameSheet)) {
                        System.out.println("Sheet name already in use");
                        return;
                    }
                }
            } else {
                fout = new FileOutputStream(filePath);
                workbook = new XSSFWorkbook();
            }
            sheet = workbook.createSheet(nameSheet);
            int rowIndex = 0;
            for (Users user : users) {
                Row row = sheet.createRow(rowIndex++);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getName());
                cell = row.createCell(1);
                cell.setCellValue(user.getAge());
                cell = row.createCell(2);
                cell.setCellValue(user.getSex());
            }
            workbook.write(fout);
            fout.flush();
            System.out.println("Successful!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fout.close();
            workbook.close();
        }
    }

    public static void main(String[] args) throws IOException, EncryptedDocumentException, InvalidFormatException {
//        List<Users> users = readFileExcel(FILE_PATH);
//        for (Users user : users) {
//            System.out.println(user);
//        }
//
        List<Users> users2 = new ArrayList<>();
        users2.add(new Users("Kay", 17.0, "Male"));
        users2.add(new Users("Hani", 17.0, "Female"));
        users2.add(new Users("Wook", 19.0, "Male"));

        List<Users> users3 = new ArrayList<>();
        users3.add(new Users("A", 17.0, "Male"));
        users3.add(new Users("B", 17.0, "Female"));
        users3.add(new Users("C", 19.0, "Male"));

        ExcelReadWrite.writeExcel(users3, FILE_PATH, "newSheet9");

    }
}
