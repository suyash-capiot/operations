package com.coxandkings.travel.operations.utils.thirdpartyvouchers;


import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.FileAttachmentResource;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Component
public class SendVoucherToClient {

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Value(value = "${third-party-vouchers.barcode-file}")
    private String defaultLocation;

    @Value(value = "${third-party-vouchers.barcode-file-archived}")
    private String archivedFiles;

    public FileAttachmentResource generateBarCodes(String code) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        String name = "barCode_" + new Date().getTime() + ".pdf";
        String fileName = defaultLocation.concat(name);
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        document.addCreationDate();
        document.addTitle("Barcode doc");
        PdfContentByte cb = writer.getDirectContent();
        Barcode128 barcode = new Barcode128();
        barcode.setCode(code);
        barcode.setCodeType(Barcode.CODE128);
        Image code128Image = barcode.createImageWithBarcode(cb, null, null);
        document.add(code128Image);
        document.newPage();
        document.close();
        byte[] content = null;
        Path filePath = Paths.get(fileName);
        try {
            content = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewDocumentResource newDocumentResource = new NewDocumentResource();
        newDocumentResource.setExtension("pdf");
        newDocumentResource.setName("barcode");

        FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
        fileAttachmentResource.setContent(content);
        fileAttachmentResource.setFilename("BarCode.pdf");

        String targetPathName = archivedFiles.concat(name);
        Path targetPath = Paths.get(targetPathName);
        try {
            Files.copy(filePath, targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileAttachmentResource;
    }


}




