package com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentMDMService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.utils.HtmlToPdfUtil;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

@Component
public class HotelDocument {
    private static final Logger logger = LogManager.getLogger(HotelDocument.class);
    @Value("${manage_document.handover_document.function}")
    private String hotelFunction;
    @Value("${manage_document.handover_document.hotel_voucher.scenario}")
    private String hotelScenario;
    @Autowired
    private TemplateBuilder templateBuilder;
    @Autowired
    TemplateLoaderService templateLoaderService;
    @Autowired
    private ClientMasterDataService clientMasterDataService;
    @Autowired
    private HtmlToPdfUtil htmlToPdfUtil;
    @Autowired
    private DocumentMDMService documentMDMService;


    public HotelDocument() {
    }

    public File generateHotelVoucher(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
        logger.info("*** Document: started generating hotel voucher ***");
        logger.info("*** Document:" + opsBooking);

        TemplateInfo templateInfo = templateBuilder.build(hotelFunction, hotelScenario);

        List<DynamicVariables> dynamicVariablesList = new ArrayList<>();
        String hotelName = opsProduct.getOrderDetails().getHotelDetails().getHotelName();
        Map<String, String> hotelDetails = documentMDMService.getHotelDetails(hotelName);
        dynamicVariablesList.add(new DynamicVariables("HOTEL_NAME", hotelName));
        dynamicVariablesList.add(new DynamicVariables("HOTEL_ADDRESS", hotelDetails.get("address")));
        dynamicVariablesList.add(new DynamicVariables("HOTEL_NUMBER", hotelDetails.get("contact")));
        dynamicVariablesList.add(new DynamicVariables("HOTEL_CITY", hotelDetails.get("city")));
        //dynamicVariablesList.add(new DynamicVariables("HOTEL_NUMBER", hotelDetails.get("state")));
        dynamicVariablesList.add(new DynamicVariables("HOTEL_COUNTRY", hotelDetails.get("country")));
        //Todo: need to change it but to update template in MDM not possible with html
        dynamicVariablesList.add(new DynamicVariables("HOTEL_PIN_CODE", hotelDetails.get("state")));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        String strDate = sdf.format(Date.from(java.time.ZonedDateTime.now().toInstant()));

        DynamicVariables dynamicVariables = new DynamicVariables("DATE", "" + strDate);
        dynamicVariablesList.add(dynamicVariables);

        dynamicVariablesList.add(new DynamicVariables("BOOKING_REF", opsBooking.getBookID()));
        dynamicVariablesList.add(new DynamicVariables("SUPPLIER_REF", opsProduct.getSupplierReferenceId()));

        String clientName = null;
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            clientName = clientMasterDataService.getB2BClientNames(Arrays.asList(opsBooking.getClientID())).get(0);
        } else if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            clientName = clientMasterDataService.getB2CClientNames(Arrays.asList(opsBooking.getClientID())).get(0);
        }

        dynamicVariablesList.add(new DynamicVariables("CLIENT_NAME", clientName));
        dynamicVariablesList.add(new DynamicVariables("HOTEL", opsProduct.getOrderDetails().getHotelDetails().getHotelName()));
        dynamicVariablesList.add(new DynamicVariables("CHECK_IN", opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn()));
        dynamicVariablesList.add(new DynamicVariables("CHECK_OUT", opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckOut()));
        dynamicVariablesList.add(new DynamicVariables("ROOM_PARTICULARS", "NA"));

        dynamicVariablesList.add(new DynamicVariables("COMPANY_NAME", documentMDMService.getCompanyName(opsBooking.getCompanyId())));
        String supplierName = documentMDMService.getSupplierName(opsProduct.getSupplierID());
        dynamicVariablesList.add(new DynamicVariables("SUPPLIER_NAME", supplierName));
        dynamicVariablesList.add(new DynamicVariables("CLIENT_TYPE", opsBooking.getClientType()));
        List<OpsRoom> rooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();


        String paxArrivalDetails = "<table border=\"0\" cellpadding=\"6\" style=\"width:100%\" bgcolor=\"#999999\">\n" +
                "\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t<tr style=\"background-color:#c1d830;\">\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; font-size:13px; text-align:center; font-weight:bold;\">Pax.No</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; font-size:13px; text-align:center; font-weight:bold;\">Name</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; font-size:13px; text-align:center; font-weight:bold;\">Date</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; font-size:13px; text-align:center; font-weight:bold;\">Arrival Time</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; font-size:13px; text-align:center; font-weight:bold;\">Flight / Rail</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; font-size:13px; text-align:center; font-weight:bold;\">Flight / Rail No</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n";

        int paxCount = 1;
        int count = 0;
        for (OpsRoom room : rooms) {
            List<OpsAccommodationPaxInfo> paxInfos = room.getPaxInfo();
            for (OpsAccommodationPaxInfo paxInfo : paxInfos) {
                //to get adult pax count
                if ("ADT".equalsIgnoreCase(paxInfo.getPaxType())) {
                    count++;
                }
                paxArrivalDetails = paxArrivalDetails.concat(
                        "\t\t\t\t\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; text-align:center; font-size:12px;\">" + paxCount++ + "</td>\n" +
                                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; text-align:center; font-size:12px;\">" + paxInfo.getFirstName() + " " + paxInfo.getLastName() + "</td>\n" +
                                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; text-align:center; font-size:12px;\">NA</td>\n" +
                                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; text-align:center; font-size:12px;\">NA</td>\n" +
                                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; text-align:center; font-size:12px;\">NA</td>\n" +
                                "\t\t\t\t\t\t\t\t\t<td style=\"font-family:Arial, Helvetica, sans-serif; color:#000; text-align:center; font-size:12px;\">NA</td>\n" +
                                "\t\t\t\t\t\t\t\t</tr>\n");
            }
        }
        dynamicVariablesList.add(new DynamicVariables("ADULT", "" + count));
        dynamicVariablesList.add(new DynamicVariables("TOTAL_ROOMS", "" + rooms.size()));
        paxArrivalDetails = paxArrivalDetails.concat(
                "\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t</table>");

        dynamicVariablesList.add(new DynamicVariables("PAX_ARRIVAL_DETAILS", paxArrivalDetails));
        dynamicVariablesList.add(new DynamicVariables("SUPPLIER_NAME_EMERGENCY", supplierName));
        dynamicVariablesList.add(new DynamicVariables("EMERGENCY_CLIENT_TYPE", opsBooking.getClientType()));

        dynamicVariablesList.add(new DynamicVariables("SUPPLIER_CONTACT", documentMDMService.supplierContact(opsProduct.getSupplierID())));


        String emailContent = null;
        try {
            emailContent = templateLoaderService.getEmailContent(templateInfo, dynamicVariablesList);
        } catch (IOException e) {
            logger.error("Unable to get data from dtms for hotel voucher", e);
            throw new OperationException("Unable to get data from dtms for hotel voucher");
        } catch (Exception e) {
            logger.error("Error in getting template from dtms", e);
            throw new OperationException("Error in getting template from dtms");
        }

        return htmlToPdfUtil.convertHtmlToPdf(emailContent, opsBooking.getBookID() + UUID.randomUUID());

    }

}
