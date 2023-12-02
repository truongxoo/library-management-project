package example.javaio;

import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ExportPDF {

    public void exportPdfFile(String templateFileName, Map<String, Object> data, String pdfFileName)
            throws IOException {
        String htmlContent = generateHtml(templateFileName, data);
        System.out.println(htmlContent);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pdfFileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            fileOutputStream.close();
        }
    }
    private String generateHtml(String templateFileName, Map<String, Object> data) {
        TemplateEngine templateEngine = createTemplateEngine();
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateFileName, context);
        return htmlContent;
    }
    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
        pdfTemplateResolver.setPrefix("pdf-templates/");
        pdfTemplateResolver.setSuffix(".html");
        pdfTemplateResolver.setTemplateMode("HTML");
        pdfTemplateResolver.setCharacterEncoding("UTF-8");
        pdfTemplateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(pdfTemplateResolver);
        return templateEngine;
    }

}
