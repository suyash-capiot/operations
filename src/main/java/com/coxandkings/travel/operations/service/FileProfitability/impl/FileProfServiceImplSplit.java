package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchReportCriteria;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.ProfSellingPrice;
import com.coxandkings.travel.operations.repository.fileProfitabilityModified.FileProfitabilityModifiedRepository;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class FileProfServiceImplSplit {
    private static final Logger logger = Logger.getLogger(FileProfServiceImplSplit.class);
    @Autowired
    FileProfitabilityModifiedRepository fileProfitabilityModifiedRepository;
    @Autowired
    FileProfitabilityServiceImpl fileProfitabilityServiceImpl;

    public List<FileProfitabilityBooking> getFileProfsWRTBookingId(FileProfSearchCriteria fileProfBookingCriteria) {
        return fileProfitabilityModifiedRepository.getFileProfsWRTBookingId(fileProfBookingCriteria);
    }

    public MultipartFile exportReport(FileProfSearchReportCriteria bookingCriteria, String exportType) {
        List<FileProfitabilityBooking> fileProfsByCriteria = getFileProfsByCriteria(bookingCriteria);
        MultipartFile file = null;
        if (exportType.equalsIgnoreCase("pdf")) {

        } else {
            file = createExcelReport(fileProfsByCriteria);
        }
        return file;
    }

        private MultipartFile createExcelReport(List<FileProfitabilityBooking> fileProfsByCriteria) {

        String fileName = "Javatpoint" + new Date().getTime() + ".xls";

        try (OutputStream fileOut = new FileOutputStream(fileName); Workbook wb = new HSSFWorkbook();) {
            Sheet sheet = wb.createSheet("File Profitability Report");
            String[] columns1 = {"", "Budgeted Profitability", "Operational Profitability", "Final Profitability", "Variance"};

            String[] columns2 = {"Booking Reference Number", "Client Type", "Client Name", "Product Name", "Lead Passenger &\n" +
                    "Pax Breakdown", "Price Component"};
            int headerRowCount = 5;

            // Create a Font for styling header cells
            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setFontName("Cambria (Headings)");


            // Create a Font for styling Normal cells
            org.apache.poi.ss.usermodel.Font normalFont = wb.createFont();
            normalFont.setBold(false);
            normalFont.setFontHeightInPoints((short) 9);
            normalFont.setFontName("Calibri (Body)");

            // Create a Font for styling Title cells
            org.apache.poi.ss.usermodel.Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 12);
            titleFont.setFontName("Times-Roman");


            // Create a CellStyle with the font
            CellStyle headerCellStyle = wb.createCellStyle();
            headerCellStyle.setFont(headerFont);


            CellStyle normalCellStyle = wb.createCellStyle();
            normalCellStyle.setFont(normalFont);

            CellStyle titleCellStyle = wb.createCellStyle();
            titleCellStyle.setFont(titleFont);
            titleCellStyle.setAlignment(HorizontalAlignment.CENTER);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
            Row titleRow = sheet.createRow((short) 0);
            Cell titleCell = titleRow.createCell((short) 2);
            titleCell.setCellValue("File Profitability - Booking Reference Number Wise");
            titleCell.setCellStyle(titleCellStyle);

            Row headerRow1 = sheet.createRow(1);
            Row headerRow2 = sheet.createRow(2);
            Row headerRow3 = sheet.createRow(3);
            Row headerRow4 = sheet.createRow(4);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            {
                Cell cell = headerRow1.createCell(1);
                cell.setCellValue(" ");
                cell.setCellStyle(headerCellStyle);
            }

            //sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));
            //second row
            for (int i = 0; i < columns2.length; i++) {
                sheet.addMergedRegion(new CellRangeAddress(2, 4, i, i));
                Cell cell = headerRow2.createCell(i);
                cell.setCellValue(columns2[i]);
                cell.setCellStyle(headerCellStyle);
            }

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 19));

            Cell cell1 = headerRow1.createCell(6);
            cell1.setCellValue("Budgeted Profitability");
            cell1.setCellStyle(headerCellStyle);

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 20, 29));
            {
                Cell cell = headerRow1.createCell(20);
                cell.setCellValue("Final Profitability ");
                cell.setCellStyle(headerCellStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 30, 32));
            {
                Cell cell = headerRow1.createCell(30);
                cell.setCellValue("Variance");
                ;
                cell.setCellStyle(headerCellStyle);

            }
            //second row
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 11));
            {
                Cell cell = headerRow2.createCell(6);
                cell.setCellValue("Selling Price");
                cell.setCellStyle(headerCellStyle);

            }
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 12, 15));
            {
                Cell cell = headerRow2.createCell(12);
                cell.setCellValue("Supplier Cost Price");
                cell.setCellStyle(headerCellStyle);

            }
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 16, 17));
            {
                Cell cell = headerRow2.createCell(16);
                cell.setCellValue("Margin");
                cell.setCellStyle(headerCellStyle);

            }

            //Supplier Cost Price
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 18, 23));
            {
                Cell cell = headerRow2.createCell(18);
                cell.setCellValue("Selling Price");
                cell.setCellStyle(headerCellStyle);

            }

            sheet.addMergedRegion(new CellRangeAddress(2, 2, 24, 27));
            {
                Cell cell = headerRow2.createCell(24);
                cell.setCellValue("Supplier Cost Price");
                cell.setCellStyle(headerCellStyle);

            }

            sheet.addMergedRegion(new CellRangeAddress(2, 2, 28, 29));
            {
                Cell cell = headerRow2.createCell(28);
                cell.setCellValue("Margin");
                cell.setCellStyle(headerCellStyle);

            }
            // sheet.addMergedRegion(new CellRangeAddress(2, 2, 32, 32));
            {
                Cell cell = headerRow2.createCell(30);
                cell.setCellValue("Budgeted vs Operational");
                cell.setCellStyle(headerCellStyle);

            }
            //sheet.addMergedRegion(new CellRangeAddress(2, 2, 33, 33));
            {
                Cell cell = headerRow2.createCell(31);
                cell.setCellValue("Budgeted vs Final");
                cell.setCellStyle(headerCellStyle);

            }


            {
                Cell cell = headerRow2.createCell(32);
                cell.setCellValue("Operational vs Final");
                cell.setCellStyle(headerCellStyle);

            }
            String[] columns3 = {"Selling Price", "Total in Company Market Currency", "Discount/Offers", "Client Commission Payable", "Client Commission Receivable", "Total Net Selling Price", "Gross Supplier Cost", "Supplier Commercials Payble", "Supplier Commercials Receivable", "Total Net Payable to Supplier", "Net Margin(Amount)", "Net Margin (%)", "Selling Price", "Total in Company Market Currency", "Disccount/Offers", "Client Commission Payable", "Client Commission Receivable", "Total net Selling Price", "Gross Supplier Cost", "Supplier Commercials Payable", "Supplier Commercails Receivable", "Total Net Payable to Supplier", "Net Margin(Amount)", "Net Margin(%)"};
            int beforeColumncount = 6;
            int i = 0;
            for (i = beforeColumncount; i < (columns3.length + beforeColumncount); i++) {
                Cell cell = headerRow3.createCell(i);
                cell.setCellValue(columns3[i - beforeColumncount]);
                cell.setCellStyle(headerCellStyle);
            }
            //String[] columns4 = {"Impact on Profitability", "Impact On Profitability", "Impact On Profitability"};
            for (int j = i; j < i + 3; j++) {
                sheet.addMergedRegion(new CellRangeAddress(3, 4, j, j));
                Cell cell = headerRow3.createCell(j);
                cell.setCellValue("Impact on Profitability");
                cell.setCellStyle(headerCellStyle);
            }

            for (i = beforeColumncount; i < (columns3.length + beforeColumncount); i++) {
                Cell cell = headerRow4.createCell(i);
                cell.setCellValue("INR");
                cell.setCellStyle(headerCellStyle);
            }

            for (FileProfitabilityBooking fileProfitabilityBooking : fileProfsByCriteria) {
                Row rowNext = sheet.createRow(headerRowCount);
                Row rowNext1 = sheet.createRow(headerRowCount + 1);
                Row rowNext2 = sheet.createRow(headerRowCount + 2);
                sheet.addMergedRegion(new CellRangeAddress(headerRowCount, headerRowCount + 2, 0, 0));
                {
                    Cell cell = rowNext.createCell(0);
                    cell.setCellValue(fileProfitabilityBooking.getBookingReferenceNumber());
                    cell.setCellStyle(headerCellStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(headerRowCount, headerRowCount + 2, 1, 1));
                {
                    Cell cell = rowNext.createCell(1);
                    cell.setCellValue(fileProfitabilityBooking.getClientType());
                    cell.setCellStyle(headerCellStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(headerRowCount, headerRowCount + 2, 2, 2));
                {
                    Cell cell = rowNext.createCell(2);
                    String clientName = fileProfitabilityBooking.getClientName();
                    if (StringUtils.isEmpty(clientName)) {
                        clientName = "Not assigned";
                    }
                    cell.setCellValue(clientName);
                    cell.setCellStyle(headerCellStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(headerRowCount, headerRowCount + 2, 3, 3));
                {
                    Cell cell = rowNext.createCell(3);
                    cell.setCellValue(fileProfitabilityBooking.getProductName());
                    cell.setCellStyle(headerCellStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(headerRowCount, headerRowCount + 2, 4, 4));
                {
                    Cell cell = rowNext.createCell(4);
                    Integer noOfAdtls = fileProfitabilityBooking.getPaxBreakDown().getNoOfAdtls();
                    if (noOfAdtls == null)
                        noOfAdtls = 0;

                    Integer noOfchildren = fileProfitabilityBooking.getPaxBreakDown().getNoOfchildren();
                    if (noOfchildren == null)
                        noOfchildren = 0;
                    cell.setCellValue(fileProfitabilityBooking.getPaxBreakDown().getLeadPassengerName() + " \n Adult " + noOfAdtls + "\n Child" + noOfchildren);
                    cell.setCellStyle(headerCellStyle);
                }

                {
                    Cell cell = rowNext.createCell(5);
                    cell.setCellValue("Base Fare");
                    cell.setCellStyle(headerCellStyle);
                }
                {
                    Cell cell = rowNext1.createCell(5);
                    cell.setCellValue("Fees");
                    cell.setCellStyle(headerCellStyle);
                }
                {
                    Cell cell = rowNext2.createCell(5);
                    cell.setCellValue("Taxes");
                    cell.setCellStyle(headerCellStyle);
                }
                //sellingPrice


                {
                    Cell cell = rowNext.createCell(6);
                    cell.setCellValue(fileProfitabilityBooking.getBudgetedFileProf().getProfSellingPrice().getSellingPrice().getBasFare());
                    cell.setCellStyle(headerCellStyle);
                }
                {
                    Cell cell = rowNext1.createCell(6);
                    Map<String, BigDecimal> fees = fileProfitabilityBooking.getBudgetedFileProf().getProfSellingPrice().getSellingPrice().getFees();
                    BigDecimal feesValue = new BigDecimal(0.00);
                    if (fees.size() != 0) {


                        Set<String> keySet = fees.keySet();
                        for (String key : keySet) {
                            feesValue = fees.get(key);
                        }
                    }
                    cell.setCellValue(feesValue.doubleValue());
                    cell.setCellStyle(headerCellStyle);
                }
                {
                    Map<String, BigDecimal> fees = fileProfitabilityBooking.getBudgetedFileProf().getProfSellingPrice().getSellingPrice().getTaxes();
                    BigDecimal feesValue = new BigDecimal(0.00);
                    if (fees.size() != 0) {


                        Set<String> keySet = fees.keySet();
                        for (String key : keySet) {
                            feesValue = fees.get(key);
                        }
                    }
                    Cell cell = rowNext2.createCell(6);
                    cell.setCellValue(feesValue.doubleValue());
                    cell.setCellStyle(headerCellStyle);
                }


                headerRowCount = headerRowCount + 3;
            }

            wb.write(fileOut);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return new MockMultipartFile(fileName, new ByteArrayOutputStream().toByteArray());

    }


    public List<FileProfitabilityBooking> getFileProfsByCriteria(FileProfSearchReportCriteria bookingCriteria) {
        logger.info("In findFileProfBySeachCriteria() method From FileProfitabilityController");
        List<FileProfitabilityBooking> generic = new ArrayList<>();
        List<FileProfitabilityBooking> fileProfTransPo = null;
        List<FileProfitabilityBooking> fileProfAccomo = null;
        List<FileProfitabilityBooking> randomEntry = null;


        if (bookingCriteria.isBookingRefNumbWise()) {
            bookingCriteria.setTransportation(true);
            fileProfTransPo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
            bookingCriteria.setTransportation(false);
            bookingCriteria.setAccomodation(true);
            fileProfAccomo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
        } else {
            bookingCriteria.setTransportation(false);
            bookingCriteria.setAccomodation(false);
            bookingCriteria.setPassengerwise(true);
            fileProfTransPo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
            bookingCriteria.setPassengerwise(false);
            bookingCriteria.setRoomwise(true);
            fileProfAccomo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
        }

        if (fileProfAccomo != null)
            generic.addAll(fileProfAccomo);
        if (fileProfTransPo != null)
            generic.addAll(fileProfTransPo);
        if (randomEntry != null)
            generic.addAll(randomEntry);

        return generic;
    }
}
