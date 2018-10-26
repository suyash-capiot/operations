package com.coxandkings.travel.operations.service.managearrivallist.accomodationarrivallist.impl;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.managearrivallist.PdfHeaders;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managearrivallist.AccommodationArrivalListItem;
import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.repository.managearrivallist.ArrivalListRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingBodyAndDocumentsResource;
import com.coxandkings.travel.operations.resource.managearrivallist.ArrivalListAccomodationItemResource;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.coxandkings.travel.operations.resource.managearrivallist.besearch.HotelArrivalListSearchCriteria;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.managearrivallist.HeaderFooterPageEvent;
import com.coxandkings.travel.operations.service.managearrivallist.accomodationarrivallist.AccoArrivalListBatchJobService;
import com.coxandkings.travel.operations.service.managearrivallist.generalarrivallist.GeneralArrivalListBatchJobService;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomSupplierDetails;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.jcr.RepositoryException;
import java.io.*;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.coxandkings.travel.operations.service.managearrivallist.generalarrivallist.impl.GeneralArrivalListBatchJobServiceImpl.getTravelDate;

//import org.springframework.mock.web.MockMultipartFile;


@Service(value = "AccoArrivalListBatchJobServiceImpl")
public class AccoArrivalListBatchJobServiceImpl implements AccoArrivalListBatchJobService {


    @Autowired
    private GeneralArrivalListBatchJobService generalArrivalListBatchJobService;

    @Autowired
    private ArrivalListRepository arrivalListRepository;

    @Value("${document_library.upload}")
    private String docsUrl;

    @Value("${manage-arrival-list.search.acco-arrival-list}")
    private String accorrivalListUrl;

    @Value("${manage-arrival-list.acco-list-name}")
    private String generalName;


    @Value("${communication.email.sendEmailUsingBodyAndDocuments}")
    private String sendEmailUsingBodyAndDocuments;

    @Value("${communication.email.from_address}")
    private String fromEmailAddress;

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;




    @Override
    public ArrivalListInfo accoArrivalList(ConfigurationResource configurationResource) throws IOException, DocumentException, JSONException, OperationException {

        ArrivalListInfo arrivalListInfo = new ArrivalListInfo();

        List<List<AccommodationArrivalListItem>> generalArrivalLists = getAccommodationArrivalList(configurationResource);

        for (List<AccommodationArrivalListItem> list : generalArrivalLists) {
            arrivalListInfo.setArrivalListAccomodationItems(list);
            arrivalListInfo.setAlertDefinitionId("Alert-1234");  // TODO: Fetch it from MDM
            arrivalListInfo.setGeneratedDateTime(ZonedDateTime.now());
            arrivalListInfo = arrivalListRepository.saveArrivalListConfiguration(arrivalListInfo);

            //TODO: send an alert to Supplier
            if (configurationResource.getSendToSupplier()) {
                DocumentReferenceResource document = generatePdf(configurationResource, arrivalListInfo);
                EmailResponse emailResponse = sendAnEmail(document.getId(), generalArrivalListBatchJobService.getSupplierId(list.get(0).getSupplierName()));
                System.out.println(emailResponse.getMesssage());
            }

        }
        return arrivalListInfo;
    }

    private DocumentReferenceResource generatePdf(ConfigurationResource configurationResource, ArrivalListInfo arrivalListInfo) throws IOException
    {
        FileOutputStream fileOutputStream = new FileOutputStream(generalName);
        NewDocumentResource documentResource = null;
        DocumentReferenceResource documentReferenceResource = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;

        if (configurationResource != null && arrivalListInfo != null) {
            try {
                Font fontbold = FontFactory.getFont("Times-Roman", 12, Font.BOLD);
                Font headingFont = FontFactory.getFont("Cambria (Headings)", 10, Font.BOLD);
                Font normalFont = FontFactory.getFont("Calibri (Body)", 9);

                Document document = new Document(PageSize.A4);
                document.setMargins(36, 36, 90, 36);
                PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
                HeaderFooterPageEvent event = new HeaderFooterPageEvent();
                writer.setPageEvent(event);
                document.open();
                document.setPageSize(PageSize.A4);
                document.newPage();

                PdfPTable table = new PdfPTable(13);
                table.setSpacingBefore(15f);
                table.setSpacingAfter(10f);
                table.setTotalWidth(770F);
                table.writeSelectedRows(0, -1, 34, 750, writer.getDirectContent());


                Paragraph productCategorySubType = new Paragraph(PdfHeaders.PRODUCT_CATEGORY_SUB_TYPE.getvalue(), headingFont);
                Paragraph productName = new Paragraph(PdfHeaders.PRODUCT_CATEGORY.getvalue(), headingFont);
                Paragraph bookingReferenceId = new Paragraph(PdfHeaders.BOOKING_REFERNCE_NUMBER.getvalue(), headingFont);
                Paragraph passengerName = new Paragraph(PdfHeaders.PASSANGER_NAME.getvalue(), headingFont);
                Paragraph passengerType = new Paragraph(PdfHeaders.PASSANGER_TYPE.getvalue(), headingFont);
                Paragraph checkInDate = new Paragraph(PdfHeaders.CHECK_IN_DATE.getvalue(), headingFont);
                Paragraph checkOutDate = new Paragraph(PdfHeaders.CHECK_OUT_Date.getvalue(), headingFont);
                Paragraph supplierName = new Paragraph(PdfHeaders.SUPPLIER_NAME.getvalue(), headingFont);
                Paragraph supplierReferenceNo = new Paragraph(PdfHeaders.SUPPLIER_REFERNCE_NUMBER.getvalue(), headingFont);
                Paragraph roomCategory = new Paragraph(PdfHeaders.ROOM_CATEGORY.getvalue(), headingFont);
                Paragraph roomType = new Paragraph(PdfHeaders.ROOM_TYPE.getvalue(), headingFont);
                Paragraph totalNumberOfRooms = new Paragraph(PdfHeaders.TOTAL_NUMBER_OF_ROOMS.getvalue(), headingFont);
                Paragraph totalNumberOfPassengers = new Paragraph(PdfHeaders.TOTAL_NUMBER_OF_PASSENGER.getvalue(), headingFont);

                PdfPCell cell1 = new PdfPCell(productCategorySubType);
                PdfPCell cell2 = new PdfPCell(productName);
                PdfPCell cell3 = new PdfPCell(bookingReferenceId);
                PdfPCell cell4 = new PdfPCell(passengerName);
                PdfPCell cell5 = new PdfPCell(passengerType);
                PdfPCell cell6 = new PdfPCell(checkInDate);
                PdfPCell cell7 = new PdfPCell(checkOutDate);
                PdfPCell cell8 = new PdfPCell(supplierName);
                PdfPCell cell9 = new PdfPCell(supplierReferenceNo);
                PdfPCell cell10 = new PdfPCell(roomCategory);
                PdfPCell cell11 = new PdfPCell(roomType);
                PdfPCell cell12 = new PdfPCell(totalNumberOfRooms);
                PdfPCell cell13 = new PdfPCell(totalNumberOfPassengers);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
                table.addCell(cell7);
                table.addCell(cell8);
                table.addCell(cell9);
                table.addCell(cell10);
                table.addCell(cell11);
                table.addCell(cell12);
                table.addCell(cell13);

                List<AccommodationArrivalListItem> arrivalListFlightItems = arrivalListInfo.getArrivalListAccomodationItems();

                for (AccommodationArrivalListItem item : arrivalListFlightItems) {
                    Paragraph cell21 = new Paragraph(item.getProductCategorySubType(), normalFont);
                    Paragraph cell22 = new Paragraph(item.getProductName(), normalFont);
                    Paragraph cell23 = new Paragraph(item.getBookingReferenceId(), normalFont);
                    Paragraph cell24 = new Paragraph(item.getPassengerName(), normalFont);
                    Paragraph cell25 = new Paragraph(item.getPassengerType(), normalFont);
                    Paragraph cell26 = new Paragraph(String.valueOf(item.getCheckInDate()), normalFont);
                    Paragraph cell27 = new Paragraph(String.valueOf(item.getCheckOutDate()), normalFont);
                    Paragraph cell28 = new Paragraph(item.getSupplierName(), normalFont);
                    Paragraph cell29 = new Paragraph(item.getSupplierReferenceNo(), normalFont);
                    Paragraph cell30 = new Paragraph(item.getRoomCategory(), normalFont);
                    Paragraph cell31 = new Paragraph(item.getRoomType(), normalFont);
                    Paragraph cell32 = new Paragraph(String.valueOf(item.getTotalNumberOfRooms()), normalFont);
                    Paragraph cell33 = new Paragraph(String.valueOf(item.getTotalNumberOfPassengers()), normalFont);

                    table.addCell(new PdfPCell(cell21));
                    table.addCell(new PdfPCell(cell22));
                    table.addCell(new PdfPCell(cell23));
                    table.addCell(new PdfPCell(cell24));
                    table.addCell(new PdfPCell(cell25));
                    table.addCell(new PdfPCell(cell26));
                    table.addCell(new PdfPCell(cell27));
                    table.addCell(new PdfPCell(cell28));
                    table.addCell(new PdfPCell(cell29));
                    table.addCell(new PdfPCell(cell30));
                    table.addCell(new PdfPCell(cell31));
                    table.addCell(new PdfPCell(cell32));
                    table.addCell(new PdfPCell(cell33));

                }
                document.add(table);
                document.close();
                fileOutputStream = new FileOutputStream(generalName);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte bytes[] = byteArrayOutputStream.toByteArray();

            fileOutputStream.write(bytes);
            fileOutputStream.close();

            documentResource = new NewDocumentResource();
            documentResource.setType("ArrivalList");
            i++;
            documentResource.setName("Accommmodation Arrival List".concat(String.valueOf(i)));
            documentResource.setCategory("Category");
            documentResource.setExtension("pdf");
            documentResource.setSubCategory("SubCategory");
            try {
                documentReferenceResource = documentLibraryService.create(null, documentResource, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                if (!StringUtils.isEmpty(documentReferenceResource.getId())) {
                    // code to delete
                }

            } catch (RepositoryException e) {
                e.printStackTrace();
            } catch (OperationException e) {
                e.printStackTrace();
            }
        }
        File file = new File(generalName);
        file.delete();
        return documentReferenceResource;
    }

    private EmailResponse sendAnEmail(String documentId, String supplierId) throws DocumentException, OperationException {
        URI url = UriComponentsBuilder.fromUriString(sendEmailUsingBodyAndDocuments).build().encode().toUri();
        MDMToken mdmToken = new MDMToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", mdmToken.getToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmailUsingBodyAndDocumentsResource> httpEntity = new HttpEntity<>(getBodyAndDocs(documentId, supplierId));
        RestTemplate restTemplate = RestUtils.getTemplate();
        ResponseEntity<EmailResponse> emailResponse = restTemplate.exchange(url, HttpMethod.POST, httpEntity, EmailResponse.class);
        return emailResponse.getBody();
    }


    private EmailUsingBodyAndDocumentsResource getBodyAndDocs(String documentId, String supplierId) throws DocumentException, OperationException {
        EmailUsingBodyAndDocumentsResource emailUsingBodyAndDocumentsResource = new EmailUsingBodyAndDocumentsResource();

        CustomSupplierDetails details = reconfirmationMDMService.getSupplierContactDetails(supplierId);

        emailUsingBodyAndDocumentsResource.setFromMail(fromEmailAddress);
        //TODO: getSupplier name and fetch MDM to get email address of that supplier
        emailUsingBodyAndDocumentsResource.setToMail(Collections.singletonList(details.getEmailId()));
        emailUsingBodyAndDocumentsResource.setCcMail(null);
        emailUsingBodyAndDocumentsResource.setBccMail(null);
        emailUsingBodyAndDocumentsResource.setSubject("Acco Arrival List ");
        emailUsingBodyAndDocumentsResource.setPriority(EmailPriority.HIGH);
        emailUsingBodyAndDocumentsResource.setBody("This is the Acco arrival list ");
        emailUsingBodyAndDocumentsResource.setDocumentReferenceIDs(Collections.singletonList(documentId));
        emailUsingBodyAndDocumentsResource.setFileAttachments(null);
        emailUsingBodyAndDocumentsResource.setCommunicationTagResource(null);
        emailUsingBodyAndDocumentsResource.setBookId(null);
        emailUsingBodyAndDocumentsResource.setUserId(null);
        emailUsingBodyAndDocumentsResource.setSupplier("");
        emailUsingBodyAndDocumentsResource.setProductSubCategory("");
        emailUsingBodyAndDocumentsResource.setProcess("");
        emailUsingBodyAndDocumentsResource.setScenario(null);
        emailUsingBodyAndDocumentsResource.setFunction(null);
        return emailUsingBodyAndDocumentsResource;
    }

    private List<List<AccommodationArrivalListItem>> getAccommodationArrivalList(ConfigurationResource configurationResource) throws JSONException, OperationException {
        HotelArrivalListSearchCriteria hotelArrivalListSearchCriteria = new HotelArrivalListSearchCriteria();
        AccommodationArrivalListItem accommodationArrivalListItem = null;
        List<AccommodationArrivalListItem> accommodationArrivalListItemList = null;
        List<List<AccommodationArrivalListItem>> toReturn = new ArrayList<>();
        HashSet<String> supplierId = new HashSet<>();


        if (configurationResource != null) {

            if (configurationResource.getCutOffGeneration() != null) {
                hotelArrivalListSearchCriteria.setCheckInDate(getTravelDate(configurationResource.getCutOffGeneration()));
            }

            if (!StringUtils.isEmpty(configurationResource.getBookingDate()) && !configurationResource.getBookingDate().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setBookingDateTime(configurationResource.getBookingDate());
            }

            if (!StringUtils.isEmpty(configurationResource.getSupplierName()) && !configurationResource.getSupplierName().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setSupplierId(generalArrivalListBatchJobService.getSupplierId(configurationResource.getSupplierName()));
            }

            if (!StringUtils.isEmpty(configurationResource.getClientType()) && !configurationResource.getClientType().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setClientType(configurationResource.getClientType());
            }

            if (!StringUtils.isEmpty(configurationResource.getClientGroup()) && !configurationResource.getClientGroup().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setClientGroupId(generalArrivalListBatchJobService.getClientGroupId(configurationResource.getClientGroup()));
            }

            if (!StringUtils.isEmpty(configurationResource.getClientName()) && !configurationResource.getClientName().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setClientId(generalArrivalListBatchJobService.getClientId(configurationResource.getClientName(), configurationResource.getClientType()));
            }

            if (!StringUtils.isEmpty(configurationResource.getContinent()) && !configurationResource.getContinent().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setContinent(configurationResource.getContinent());
            }
            if (!StringUtils.isEmpty(configurationResource.getCountry()) && !configurationResource.getCountry().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setCountry(configurationResource.getCountry());
            }
            if (!StringUtils.isEmpty(configurationResource.getCity()) && !configurationResource.getCity().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setCity(configurationResource.getCity());
            }
            if (!StringUtils.isEmpty(configurationResource.getProductName()) && !configurationResource.getProductName().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setProductName(configurationResource.getProductName());
            }
            if (!StringUtils.isEmpty(configurationResource.getChain()) && !configurationResource.getChain().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setChain(configurationResource.getChain());
            }
            if (!StringUtils.isEmpty(configurationResource.getCity()) && !configurationResource.getCity().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setCity(configurationResource.getCity());
            }
            if (!StringUtils.isEmpty(configurationResource.getIsMysteryProduct()) && !configurationResource.getIsMysteryProduct().equalsIgnoreCase("ALL")) {
                hotelArrivalListSearchCriteria.setCity(configurationResource.getCity());
            }
            //TODO: Pending from BE , I need to set "configuration.getAirlineName"
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HotelArrivalListSearchCriteria> entity = new HttpEntity<>(hotelArrivalListSearchCriteria, httpHeaders);
        ParameterizedTypeReference<List<ArrivalListAccomodationItemResource>> responseType = new ParameterizedTypeReference<List<ArrivalListAccomodationItemResource>>() {
        };

        ResponseEntity<List<ArrivalListAccomodationItemResource>> listResponseEntity = RestUtils.exchange(accorrivalListUrl, HttpMethod.POST, entity, responseType);
        List<ArrivalListAccomodationItemResource> listItemResources = listResponseEntity.getBody();

        //To get All Supplier ids
        for (ArrivalListAccomodationItemResource itemResource : listItemResources) {
            supplierId.add(itemResource.getSupplierId());
        }

        for (String s : supplierId) {
            accommodationArrivalListItemList = new ArrayList<>();
            for (ArrivalListAccomodationItemResource resource : listItemResources) {
                if (resource.getSupplierId().equalsIgnoreCase(s)) {
                    accommodationArrivalListItem = new AccommodationArrivalListItem();
                    accommodationArrivalListItem.setProductCategorySubType(resource.getProductSubCategory());
                    accommodationArrivalListItem.setProductName(resource.getProductSubCategory());   //TODO: AbstractProductFactory Name?
                    accommodationArrivalListItem.setBookingReferenceId(resource.getBookID());
                    accommodationArrivalListItem.setPassengerName(resource.getFirstName().concat(" " + resource.getLastName()));
                    accommodationArrivalListItem.setPassengerType(resource.getPaxType());
                    accommodationArrivalListItem.setCheckInDate(resource.getCheckInDate());
                    accommodationArrivalListItem.setCheckOutDate(resource.getCheckOutDate());
                    accommodationArrivalListItem.setSupplierName(generalArrivalListBatchJobService.getSupName(resource.getSupplierId()));
                    accommodationArrivalListItem.setSupplierReferenceNo(resource.getSupplierReferenceNumber());
                    accommodationArrivalListItem.setRoomCategory(resource.getRoomCategory());
                    accommodationArrivalListItem.setRoomType(resource.getRoomType());
                    accommodationArrivalListItem.setTotalNumberOfPassengers(Integer.valueOf(resource.getPaxCount()));
                    accommodationArrivalListItem.setTotalNumberOfRooms(Integer.valueOf(resource.getTotalRoomsInOrder()));


                    accommodationArrivalListItem.setTotalNumberOfPassengers(Integer.valueOf(resource.getPaxCount()));

                    accommodationArrivalListItemList.add(accommodationArrivalListItem);
                }
            }
            toReturn.add(accommodationArrivalListItemList);
        }

        return toReturn;
    }

}

