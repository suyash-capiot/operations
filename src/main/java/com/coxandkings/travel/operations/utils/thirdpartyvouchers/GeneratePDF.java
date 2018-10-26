package com.coxandkings.travel.operations.utils.thirdpartyvouchers;

import com.coxandkings.travel.operations.response.thirdpartyvouchers.ThirdPartyVouchersReportResponse;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class GeneratePDF {

    @Value(value = "${third-party-vouchers.report}")
    private String reportPath;

    //private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public MultipartFile generateFile(List<ThirdPartyVouchersReportResponse> list) throws IOException, DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MultipartFile file = null;
        Document document = new Document();
        //FileOutputStream fileOutputStream = new FileOutputStream(reportPath);
        PdfWriter.getInstance(document, byteArrayOutputStream);
        if (!list.isEmpty()) {
            document.open();
            Font fontbold = FontFactory.getFont("Times-Roman", 12, Font.BOLD);
            Font headingFont = FontFactory.getFont("Cambria (Headings)", 10, Font.BOLD);
            Font normalFont = FontFactory.getFont("Calibri (Body)", 9);
            Paragraph heading = new Paragraph(" Third Party Vouchers Report Response", fontbold);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);
            table.setSpacingAfter(10f);

            Paragraph supplier = new Paragraph("Supplier", headingFont);
            Paragraph productName = new Paragraph("Product Name", headingFont);
            Paragraph productCategory = new Paragraph("Product Category", headingFont);
            Paragraph productCategorySubType = new Paragraph("Product Category Sub Type", headingFont);
            Paragraph usage = new Paragraph("Usage", headingFont);
            Paragraph status = new Paragraph("Status", headingFont);
            Paragraph paymentStatus = new Paragraph("Payment Status", headingFont);
            Paragraph voucherBasedOn = new Paragraph("Voucher Based On", headingFont);

            PdfPCell cell1 = new PdfPCell(supplier);
            PdfPCell cell2 = new PdfPCell(productName);
            PdfPCell cell3 = new PdfPCell(productCategory);
            PdfPCell cell4 = new PdfPCell(productCategorySubType);
            PdfPCell cell5 = new PdfPCell(usage);
            PdfPCell cell6 = new PdfPCell(status);
            PdfPCell cell7 = new PdfPCell(paymentStatus);
            PdfPCell cell8 = new PdfPCell(voucherBasedOn);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell7);
            table.addCell(cell8);

            for (ThirdPartyVouchersReportResponse reportResponse : list) {
                Paragraph cell11 = new Paragraph(reportResponse.getSupplier(), normalFont);
                Paragraph cell12 = new Paragraph(reportResponse.getProductName(), normalFont);
                Paragraph cell13 = new Paragraph(reportResponse.getProductCategory(), normalFont);
                Paragraph cell14 = new Paragraph(reportResponse.getProductCategorySubtype(), normalFont);
                Paragraph cell15 = new Paragraph(String.valueOf(reportResponse.getVoucherCodeUsageType()), normalFont);
                Paragraph cell16 = new Paragraph(String.valueOf(reportResponse.getStatus()), normalFont);
                Paragraph cell17 = new Paragraph(reportResponse.getPaymentStatusOfBooking(), normalFont);
                Paragraph cell18 = new Paragraph(String.valueOf(reportResponse.getVoucherToBeAppliedOn()), normalFont);

                table.addCell(new PdfPCell(cell11));
                table.addCell(new PdfPCell(cell12));
                table.addCell(new PdfPCell(cell13));
                table.addCell(new PdfPCell(cell14));
                table.addCell(new PdfPCell(cell15));
                table.addCell(new PdfPCell(cell16));
                table.addCell(new PdfPCell(cell17));
                table.addCell(new PdfPCell(cell18));

            }
            document.add(table);
            document.close();
            // fileOutputStream.write(byteArrayOutputStream.toByteArray());
            // fileOutputStream.close();

           /* byte[] content=null;
            Path filePath = Paths.get(reportPath);
            try {
                content= Files.readAllBytes(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            file = new MockMultipartFile("Report.pdf", byteArrayOutputStream.toByteArray());
        }
        return file;
    }


    public MultipartFile generateExcel(List<ThirdPartyVouchersReportResponse> list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MultipartFile file = null;
        String[] columns = {"Supplier", "Product Name", "Product Category", "Product Category Sub Type", "Usage", "Status", "Payment Status", "Voucher Based On"};


        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Report");





        // Create a Font for styling header cells
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setFontName("Cambria (Headings)");


        // Create a Font for styling Normal cells
        org.apache.poi.ss.usermodel.Font normalFont = workbook.createFont();
        normalFont.setBold(false);
        normalFont.setFontHeightInPoints((short) 9);
        normalFont.setFontName("Calibri (Body)");

        // Create a Font for styling Title cells
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setFontName("Times-Roman");


        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
         headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerCellStyle.setBorderRight(BorderStyle.MEDIUM);
        headerCellStyle.setBorderTop(BorderStyle.MEDIUM);



        CellStyle normalCellStyle = workbook.createCellStyle();
        normalCellStyle.setFont(normalFont);
        normalCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        normalCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        normalCellStyle.setBorderRight(BorderStyle.MEDIUM);
        normalCellStyle.setBorderTop(BorderStyle.MEDIUM);

        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleCellStyle.setBorderRight(BorderStyle.MEDIUM);
        titleCellStyle.setBorderTop(BorderStyle.MEDIUM);



        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
        Row titleRow = sheet.createRow((short) 0);
        Cell titleCell = titleRow.createCell((short) 2);
        titleCell.setCellValue("Third Party Vouchers Report Response");
        titleCell.setCellStyle(titleCellStyle);


        // Create a Row
        Row headerRow = sheet.createRow(1);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }


        // Create Other rows and cells with data
        int rowNum = 2;
        Cell data = null;
        for (ThirdPartyVouchersReportResponse resp : list) {
            Row row = sheet.createRow(rowNum++);

            data = row.createCell(0);
            data.setCellValue(resp.getSupplier());
            data.setCellStyle(normalCellStyle);

            data = row.createCell(1);
            data.setCellValue(resp.getProductName());
            data.setCellStyle(normalCellStyle);

            data = row.createCell(2);
            data.setCellValue(resp.getProductCategory());
            data.setCellStyle(normalCellStyle);

            data = row.createCell(3);
            data.setCellValue(resp.getProductCategorySubtype());
            data.setCellStyle(normalCellStyle);

            data = row.createCell(4);
            data.setCellValue(String.valueOf(resp.getVoucherCodeUsageType()));
            data.setCellStyle(normalCellStyle);

            data = row.createCell(5);
            data.setCellValue(String.valueOf(resp.getStatus()));
            data.setCellStyle(normalCellStyle);

            data = row.createCell(6);
            data.setCellValue(resp.getPaymentStatusOfBooking());
            data.setCellStyle(normalCellStyle);

            data = row.createCell(7);
            data.setCellValue(String.valueOf(resp.getVoucherToBeAppliedOn()));
            data.setCellStyle(normalCellStyle);
            //row.setRowStyle(normalCellStyle);

        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

           /* // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream("D:/thirdpartyreports/Report.xlsx");
            workbook.write(fileOut);
            fileOut.close();*/

        workbook.write(byteArrayOutputStream);
        // Closing the workbook
        workbook.close();
        file = new MockMultipartFile("Report.xlsx", byteArrayOutputStream.toByteArray());


        return file;


    }
}
