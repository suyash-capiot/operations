package com.coxandkings.travel.operations.service.managearrivallist.flightarrivallist.impl;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.managearrivallist.PdfHeaders;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.model.managearrivallist.FlightArrivalListItem;
import com.coxandkings.travel.operations.repository.managearrivallist.ArrivalListRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingBodyAndDocumentsResource;
import com.coxandkings.travel.operations.resource.managearrivallist.ArrivalListFlightItemResource;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.coxandkings.travel.operations.resource.managearrivallist.besearch.FlightArrivalListSearchCriteria;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.managearrivallist.HeaderFooterPageEvent;
import com.coxandkings.travel.operations.service.managearrivallist.flightarrivallist.FlightArrivalListBatchJobService;
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

@Service(value = "FlightArrivalListBatchJobServiceImpl")
public class FlightArrivalListBatchJobServiceImpl  implements FlightArrivalListBatchJobService
{
    @Autowired
    private GeneralArrivalListBatchJobService generalArrivalListBatchJobService;

    @Autowired
    private ArrivalListRepository arrivalListRepository;

    @Value("${document_library.upload}")
    private String docsUrl;

    @Value("${manage-arrival-list.search.flight-arrival-list}")
    private String flightArrivalListUrl;

    @Value("${manage-arrival-list.flight-list-name}")
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
    public ArrivalListInfo flightArrivalList(ConfigurationResource configurationResource) throws IOException, DocumentException, JSONException, OperationException {

        ArrivalListInfo arrivalListInfo = new ArrivalListInfo();

        List<List<FlightArrivalListItem>> generalArrivalLists = getFlightArrivalList(configurationResource);
        for (List<FlightArrivalListItem> list : generalArrivalLists) {
            arrivalListInfo.setFlightArrivalListItems(list);
            arrivalListInfo.setAlertDefinitionId("Alert-1234");  // TODO: Fetch it from MDM
            arrivalListInfo.setGeneratedDateTime(ZonedDateTime.now());
            arrivalListInfo = arrivalListRepository.saveArrivalListConfiguration(arrivalListInfo);


            // send an alert to Supplier
            if (configurationResource.getSendToSupplier()) {
                DocumentReferenceResource document = generatePdf(configurationResource, arrivalListInfo);

//            LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//            FileSystemResource value = null;
//            value = new FileSystemResource(new File(document.getOriginalFilename()));
//            params.add("file",value );
//            params.add("type", "pdf");
//            params.add("name","flightArrivalList");
//            params.add("category","category");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

//            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =new HttpEntity<>(params, headers);

//            ResponseEntity<DocumentReferenceResource> responseEntity = RestUtils.exchange(docsUrl , HttpMethod.POST, requestEntity, DocumentReferenceResource.class);
//            DocumentReferenceResource body = responseEntity.getBody();
//            System.out.println(body.getId());
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

                PdfPTable table = new PdfPTable(11);
                table.setSpacingBefore(15f);
                table.setSpacingAfter(10f);
                table.setTotalWidth(770F);
                table.writeSelectedRows(0, -1, 34, 750, writer.getDirectContent());

                Paragraph airlineName = new Paragraph(PdfHeaders.AIRLINE_NAME.getvalue(), headingFont);
                Paragraph fromSector = new Paragraph(PdfHeaders.FROM_SECTOR.getvalue(), headingFont);
                Paragraph toSector = new Paragraph(PdfHeaders.TO_SECTOR.getvalue(), headingFont);
                Paragraph passengerName = new Paragraph(PdfHeaders.PASSANGER_NAME.getvalue(), headingFont);
                Paragraph passengerType = new Paragraph(PdfHeaders.PASSANGER_TYPE.getvalue(), headingFont);
                Paragraph supplierName = new Paragraph(PdfHeaders.SUPPLIER_NAME.getvalue(), headingFont);
                Paragraph pccHapCredentials = new Paragraph(PdfHeaders.PCC_HAP_CREDENTIALS.getvalue(), headingFont);
                Paragraph pnr = new Paragraph(PdfHeaders.PNR.getvalue(), headingFont);
                Paragraph flightClass = new Paragraph(PdfHeaders.CLASS.getvalue(), headingFont);
                Paragraph rbd = new Paragraph(PdfHeaders.RDB.getvalue(), headingFont);
                Paragraph totalNumberOfPassengers = new Paragraph(PdfHeaders.TOTAL_NUMBER_OF_PASSENGER.getvalue(), headingFont);

                PdfPCell cell1 = new PdfPCell(airlineName);
                PdfPCell cell2 = new PdfPCell(fromSector);
                PdfPCell cell3 = new PdfPCell(toSector);
                PdfPCell cell4 = new PdfPCell(passengerName);
                PdfPCell cell5 = new PdfPCell(passengerType);
                PdfPCell cell6 = new PdfPCell(supplierName);
                PdfPCell cell7 = new PdfPCell(pccHapCredentials);
                PdfPCell cell8 = new PdfPCell(pnr);
                PdfPCell cell9 = new PdfPCell(flightClass);
                PdfPCell cell10 = new PdfPCell(rbd);
                PdfPCell cell11 = new PdfPCell(totalNumberOfPassengers);

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

                List<FlightArrivalListItem> flightArrivalListItems = arrivalListInfo.getFlightArrivalListItems();

                for (FlightArrivalListItem item : flightArrivalListItems)
                    {
                        Paragraph cell12 = new Paragraph(item.getAirlineName(), normalFont);
                        Paragraph cell13 = new Paragraph(item.getFromSector(), normalFont);
                        Paragraph cell14 = new Paragraph(item.getToSector(), normalFont);
                        Paragraph cell15 = new Paragraph(item.getPassengerName(), normalFont);
                        Paragraph cell16 = new Paragraph(item.getPassengerType(), normalFont);
                        Paragraph cell17 = new Paragraph(item.getSupplierName(), normalFont);
                        Paragraph cell18 = new Paragraph(item.getPccHapCredentials(), normalFont);
                        Paragraph cell19 = new Paragraph(item.getPnr(), normalFont);
                        Paragraph cell20 = new Paragraph(item.getFlightClass(), normalFont);
                        Paragraph cell21 = new Paragraph(item.getRbd(), normalFont);
                        Paragraph cell22 = new Paragraph(String.valueOf(item.getTotalNumberOfPassengers()), normalFont);


                        table.addCell(new PdfPCell(cell12));
                        table.addCell(new PdfPCell(cell13));
                        table.addCell(new PdfPCell(cell14));
                        table.addCell(new PdfPCell(cell15));
                        table.addCell(new PdfPCell(cell16));
                        table.addCell(new PdfPCell(cell17));
                        table.addCell(new PdfPCell(cell18));
                        table.addCell(new PdfPCell(cell19));
                        table.addCell(new PdfPCell(cell20));
                        table.addCell(new PdfPCell(cell21));
                        table.addCell(new PdfPCell(cell22));


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
            documentResource.setName("Flight Arrival List".concat(String.valueOf(i)));
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
        HttpEntity<EmailUsingBodyAndDocumentsResource> httpEntity = new HttpEntity<>(getBodyAndDocs(documentId, supplierId), httpHeaders);
        RestTemplate restTemplate = RestUtils.getTemplate();
        ResponseEntity<EmailResponse> emailResponse = restTemplate.exchange(url, HttpMethod.POST,httpEntity,EmailResponse.class);
        return emailResponse.getBody();
    }

    private EmailUsingBodyAndDocumentsResource getBodyAndDocs(String documentId, String supplierId) throws DocumentException, OperationException {

        EmailUsingBodyAndDocumentsResource emailUsingBodyAndDocumentsResource = new EmailUsingBodyAndDocumentsResource();

        CustomSupplierDetails details = reconfirmationMDMService.getSupplierContactDetails(supplierId);
        emailUsingBodyAndDocumentsResource.setFromMail(fromEmailAddress);
        //TODO: getSupplier name and fetch MDM to get email address of that supplier
        emailUsingBodyAndDocumentsResource.setToMail(Collections.singletonList(details.getEmailId()/*"pooja.gawde@coxandkings.com"*/));
        emailUsingBodyAndDocumentsResource.setCcMail(null);
        emailUsingBodyAndDocumentsResource.setBccMail(null);
        emailUsingBodyAndDocumentsResource.setSubject("Flight Arrival List ");
        emailUsingBodyAndDocumentsResource.setPriority(EmailPriority.HIGH);
        emailUsingBodyAndDocumentsResource.setBody("This is the Flight arrival list ");
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


    private List<List<FlightArrivalListItem>> getFlightArrivalList(ConfigurationResource configurationResource) throws JSONException, OperationException
    {
        FlightArrivalListSearchCriteria flightCriteria = new FlightArrivalListSearchCriteria();
        FlightArrivalListItem flightArrivalListItem = null;
        List<FlightArrivalListItem> flightArrivalListItemList = null;
        List<List<FlightArrivalListItem>> toReturn = new ArrayList<>();
        HashSet<String> supplierId = new HashSet<>();


        if (configurationResource != null) {
            if (configurationResource.getCutOffGeneration() != null) {
                flightCriteria.setTravelDateTime(getTravelDate(configurationResource.getCutOffGeneration()));
            }
            if (!StringUtils.isEmpty(configurationResource.getSupplierName()) && !configurationResource.getSupplierName().equalsIgnoreCase("ALL")) {
                flightCriteria.setSupplierId(generalArrivalListBatchJobService.getSupplierId(configurationResource.getSupplierName()));
            }

            if (!StringUtils.isEmpty(configurationResource.getClientGroup()) && !configurationResource.getClientGroup().equalsIgnoreCase("ALL")) {
                flightCriteria.setClientGroupId(generalArrivalListBatchJobService.getClientGroupId(configurationResource.getClientGroup()));
            }

            if (!StringUtils.isEmpty(configurationResource.getClientType()) && !configurationResource.getClientType().equalsIgnoreCase("ALL")) {
                flightCriteria.setClientType(configurationResource.getClientType());
            }
            if (!StringUtils.isEmpty(configurationResource.getClientName()) && !configurationResource.getClientName().equalsIgnoreCase("ALL")) {
                flightCriteria.setClientId(generalArrivalListBatchJobService.getClientId(configurationResource.getClientName(), configurationResource.getClientType()));
            }
            if (!StringUtils.isEmpty(configurationResource.getBookingDate()) && !configurationResource.getBookingDate().equalsIgnoreCase("ALL")) {
                flightCriteria.setBookingDateTime(configurationResource.getBookingDate());
            }
            if (!StringUtils.isEmpty(configurationResource.getFromCity()) && !configurationResource.getFromCity().equalsIgnoreCase("ALL")) {
                flightCriteria.setFromCity(configurationResource.getFromCity());
            }
            if (!StringUtils.isEmpty(configurationResource.getToCity()) && !configurationResource.getToCity().equalsIgnoreCase("ALL")) {
                flightCriteria.setToCity(configurationResource.getToCity());
            }
            if (!StringUtils.isEmpty(configurationResource.getJourneyType()) && !configurationResource.getJourneyType().equalsIgnoreCase("ALL")) {
                flightCriteria.setJourneyType(configurationResource.getJourneyType());
            }
            //TODO: Pending from BE , I need to set "configuration.getAirlineName"
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FlightArrivalListSearchCriteria> entity = new HttpEntity<>(flightCriteria, httpHeaders);
        ParameterizedTypeReference<List<ArrivalListFlightItemResource>> responseType = new ParameterizedTypeReference<List<ArrivalListFlightItemResource>>() {
        };
        ResponseEntity<List<ArrivalListFlightItemResource>> listResponseEntity = RestUtils.exchange(flightArrivalListUrl, HttpMethod.POST, entity, responseType);
        List<ArrivalListFlightItemResource> listItemResources = listResponseEntity.getBody();

        //To get All Supplier ids
        for (ArrivalListFlightItemResource itemResource : listItemResources) {
            supplierId.add(itemResource.getSupplierId());
        }

        for (String s : supplierId) {
            flightArrivalListItemList = new ArrayList<>();
            for (ArrivalListFlightItemResource resource : listItemResources)
            {
                if (resource.getSupplierId().equalsIgnoreCase(s)) {
                    flightArrivalListItem = new FlightArrivalListItem();
                    flightArrivalListItem.setAirlineName("");  //TODO: Need to get from BE
                    flightArrivalListItem.setFromSector(resource.getOriginLocation());
                    flightArrivalListItem.setToSector(resource.getDestinationLocation());
                    flightArrivalListItem.setPassengerName(resource.getFirstName().concat(" " + resource.getLastName()));
                    flightArrivalListItem.setPassengerType(resource.getPaxType());
                    flightArrivalListItem.setSupplierName(generalArrivalListBatchJobService.getSupName(resource.getSupplierId()));
                    flightArrivalListItem.setPccHapCredentials(resource.getTicketingPcc());
                    flightArrivalListItem.setPnr(resource.getSupplierReferenceNumber());
                    flightArrivalListItem.setFlightClass(resource.getCabinType());
                    flightArrivalListItem.setRbd(resource.getRph());
                    flightArrivalListItem.setTotalNumberOfPassengers(Integer.valueOf(resource.getPaxCount()));

                    flightArrivalListItemList.add(flightArrivalListItem);
                }
            }
            toReturn.add(flightArrivalListItemList);
        }

        return toReturn;
    }


}
