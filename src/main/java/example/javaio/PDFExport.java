package example.javaio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFExport {
    public final static String FILE_PATH = "D:\\PDFByJava.pdf";

    private static void ExportPDF(String filePath) throws MalformedURLException, IOException {
        Document document = new Document(PageSize.A4);
        FileOutputStream fout = new FileOutputStream(filePath);
        try {
            PdfWriter.getInstance(document, fout);
            document.open();
            Paragraph pra = new Paragraph("Hello! This is my first PDF file");

            List orderedList = new List(List.ORDERED);
            orderedList.add(new ListItem("Item 1"));
            orderedList.add(new ListItem("Item 2"));
            orderedList.add(new ListItem("Item 3"));
            pra.add(orderedList);

            List unorderedList = new List(List.UNORDERED);
            unorderedList.add(new ListItem("Item 1"));
            unorderedList.add(new ListItem("Item 2"));
            unorderedList.add(new ListItem("Item 3"));
            pra.add(unorderedList);
            document.add(pra);

            Anchor ac = new Anchor();
            ac.setName("Know more about us");
            ac.setReference("https://itextpdf.com/resources/api-documentation");
            pra.add(ac);

            Paragraph pra3 = new Paragraph("MY IDOL");
            Image image1 = Image.getInstance("D:\\1.png");
            pra3.add(image1);
            document.add(pra3);

            PdfPTable table = new PdfPTable(3);
            PdfPCell header1 = new PdfPCell(new Paragraph("Header 1"));
            PdfPCell header2 = new PdfPCell(new Paragraph("Header 2"));
            PdfPCell header3 = new PdfPCell(new Paragraph("Header 3"));
            PdfPCell data1 = new PdfPCell(new Paragraph("Hello"));
            PdfPCell data2 = new PdfPCell(new Paragraph("Hi"));
            table.addCell(header1);
            table.addCell(header2);
            table.addCell(header3);

            PdfPTable nestedTable = new PdfPTable(2);
            nestedTable.addCell(new Paragraph("Nested Cell 1"));
            nestedTable.addCell(new Paragraph("Nested Cell 2"));
            PdfPCell data3 = new PdfPCell(nestedTable);
            table.addCell(data1);
            table.addCell(data2);
            table.addCell(data3);
            document.add(table);

            Paragraph pra2 = new Paragraph("Second paragraph");
            document.add(pra2);
            System.out.println("Successful");
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        PDFExport.ExportPDF(FILE_PATH);
    }
}
