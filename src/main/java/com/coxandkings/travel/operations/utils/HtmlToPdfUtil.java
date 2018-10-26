package com.coxandkings.travel.operations.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

@Component
public class HtmlToPdfUtil {
    private static final Logger logger = LogManager.getLogger(HtmlToPdfUtil.class);
    @Value("${manage-document.directory}")
    private String workingDirectory;

   /* public File convertHtmlToPdf(String toReturn, String fileName) throws OperationException {
        if (StringUtils.isEmpty(fileName)) {
            throw new OperationException(Constants.ER08, "FileName");
        }

        if (StringUtils.isEmpty(toReturn)) {
            throw new OperationException(Constants.ER08, "toReturn");
        }

        *//*try {
            //Document document = new Document(new Rectangle(0, 0, 1000, 900), 25f, 25f, 25f, 25f);
            Document document = new Document(PageSize.A4);
            document.setMargins(10, 10, 10, 10);


            File file = new File("D:/apps/" + fileName + ".pdf");

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            worker.parseXHtml(pdfWriter, document, new StringReader(toReturn));

            document.close();

            return file;
        } catch (Exception e) {
            logger.error(e);
        }*//*
        try {
            String k = "<html><body> This is my Project </body></html>";
            File file = new File("D:/apps/" + fileName + ".pdf");

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            InputStream is = new ByteArrayInputStream(toReturn.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/


    public static File convertHtmlToPdf(String htmlString, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            System.out.println("fileName is empty");
        }

        if (StringUtils.isEmpty(htmlString)) {
            System.out.println("String is empty");
        }

        File file =null;
        try {

            Document document = new Document(PageSize.A4);
            document.setMargins(40, 0, 0, 0);
            String workingDirectory = System.getProperty("user.dir");
             file = new File(workingDirectory + fileName + ".pdf");

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            String xhtmlString = toXHTML(htmlString);
            worker.parseXHtml(pdfWriter, document, new StringReader(xhtmlString));
            document.close();
            System.out.println("Done.");


        } catch (Exception e) {
            System.out.println("Context : Converting Html to pdf");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    public static String toXHTML(String html) {
        final org.jsoup.nodes.Document document = Jsoup.parse(html);
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        return document.html();
    }


}
