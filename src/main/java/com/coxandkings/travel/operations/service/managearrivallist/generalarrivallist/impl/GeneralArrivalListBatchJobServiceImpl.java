package com.coxandkings.travel.operations.service.managearrivallist.generalarrivallist.impl;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.managearrivallist.PdfHeaders;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.model.managearrivallist.GeneralArrivalListItem;
import com.coxandkings.travel.operations.repository.managearrivallist.ArrivalListRepository;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingBodyAndDocumentsResource;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.coxandkings.travel.operations.resource.managearrivallist.GeneralArrivalListItemResource;
import com.coxandkings.travel.operations.resource.managearrivallist.besearch.GeneralArrivalListSearchCriteria;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.managearrivallist.HeaderFooterPageEvent;
import com.coxandkings.travel.operations.service.managearrivallist.generalarrivallist.GeneralArrivalListBatchJobService;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomSupplierDetails;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Service(value = "GeneralArrivalListBatchJobServiceImpl")
public class GeneralArrivalListBatchJobServiceImpl  implements GeneralArrivalListBatchJobService
{

    @Autowired
    JsonObjectProvider jsonObjectProvider;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private ArrivalListRepository arrivalListRepository;

    @Value("${document_library.upload}")
    private String docsUrl;

    @Value("${manage-arrival-list.general-list-name}")
    private String generalName;

    @Value("${communication.email.sendEmailUsingBodyAndDocuments}")
    private String sendEmailUsingBodyAndDocuments;

    @Value("$communication.email.from_address}")
    private String fromEmailAddress;

    @Value("${manage-arrival-list.search.general-arrival-list}")
    private String generalArrivalListUrl;

    @Value("${manage-arrival-list.mdm.supplier-id}")
    private String supplierUrl;

    @Value("${mdm.common.client.b2b_client}")
    private String b2bClientId;

    @Value("${mdm.common.client.b2c_client}")
    private String b2cClientId;


    @Value("${mdm.common.client.client-group}")
    private String clientGroupNameUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private DocumentLibraryService documentLibraryService;


    public static String getTravelDate(Duration duration) {
        if (duration!= null || !StringUtils.isEmpty(duration)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            int day = (int) TimeUnit.DAYS.convert(duration.toHours(), TimeUnit.HOURS);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, day);
            date = calendar.getTime();
            return sdf.format(date).toString();
        }
        else
        {
            return String.valueOf(duration);
        }
    }

    @Override
    public ArrivalListInfo generateArrivalListBasedOnCutOffDate(ConfigurationResource configurationResource) throws OperationException, IOException,
            DocumentException, JSONException
    {
        ArrivalListInfo arrivalListInfo = new ArrivalListInfo();
        List<List<GeneralArrivalListItem>> generalArrivalLists = searchAndGetArrivalList(configurationResource);
        for (List<GeneralArrivalListItem> listItems : generalArrivalLists)
        {
            arrivalListInfo.setGeneralArrivalListItem(listItems);
            arrivalListInfo.setAlertDefinitionId("Alert-1234");  // TODO: Fetch it from MDM
            arrivalListInfo.setGeneratedDateTime(ZonedDateTime.now());
            arrivalListInfo = arrivalListRepository.saveArrivalListConfiguration(arrivalListInfo);

            //send an alert to Supplier
            if (configurationResource.getSendToSupplier())
            {
                DocumentReferenceResource document = generatePdf(configurationResource,arrivalListInfo);
//                LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//                File file = new File(document.getOriginalFilename());
//                FileSystemResource value = null;
//                value = new FileSystemResource(file);
//                params.add("file",value );
//                params.add("type", "pdf");
//                params.add("name","generalArrivalList");
//                params.add("category","category");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//                HttpEntity<NewDocumentResource> requestEntity = new HttpEntity<>(document, headers);
//                ResponseEntity<DocumentReferenceResource> responseEntity = RestUtils.exchange(docsUrl , HttpMethod.POST, requestEntity, DocumentReferenceResource.class);
//                DocumentReferenceResource body = responseEntity.getBody();
                System.out.println(document.getId());
                EmailResponse emailResponse = sendAnEmail(document.getId(),getSupplierId(listItems.get(0).getSupplierName()));
                System.out.println(emailResponse.getMesssage());
            }
        }
        return arrivalListInfo;
    }


    private List<List<GeneralArrivalListItem>> searchAndGetArrivalList(ConfigurationResource configurationResource) throws JSONException, OperationException
    {
        GeneralArrivalListSearchCriteria generalArrivalListSearchCriteria = new GeneralArrivalListSearchCriteria();
        GeneralArrivalListItem generalArrivalListItem = null;
        List<GeneralArrivalListItem> generalArrivalList = null;
        List<List<GeneralArrivalListItem>> toReturn = new ArrayList<>();
        HashSet<String> supplierId = new HashSet<>();

        if (configurationResource != null) {

            if (configurationResource.getCutOffGeneration() != null) {
                generalArrivalListSearchCriteria.setTravelDateTime(getTravelDate(configurationResource.getCutOffGeneration()));
            }

            if (!StringUtils.isEmpty(configurationResource.getBookingDate())) {
                generalArrivalListSearchCriteria.setBookingDateTime(configurationResource.getBookingDate());
            }

            if (!StringUtils.isEmpty(configurationResource.getProductCategorySubType())
                    && !configurationResource.getProductCategorySubType().equalsIgnoreCase("ALL")) {
                generalArrivalListSearchCriteria.setProductSubCategory(configurationResource.getProductCategorySubType());
            }

            if (!StringUtils.isEmpty(configurationResource.getSupplierName())
                    && !configurationResource.getSupplierName().equalsIgnoreCase("ALL")) {
                generalArrivalListSearchCriteria.setSupplierId(getSupplierId(configurationResource.getSupplierName()));
            }

            if (!StringUtils.isEmpty(configurationResource.getClientType())
                    && !configurationResource.getClientType().equalsIgnoreCase("ALL")) {
                generalArrivalListSearchCriteria.setClientType(configurationResource.getClientType());
                if (!StringUtils.isEmpty(configurationResource.getClientName()) && !configurationResource.getClientName().equalsIgnoreCase("ALL")) {
                    generalArrivalListSearchCriteria.setClientId(getClientId(configurationResource.getClientName(), configurationResource.getClientType()));
                }
            }

            if (!StringUtils.isEmpty(configurationResource.getClientGroup())
                    && !configurationResource.getClientGroup().equalsIgnoreCase("ALL")) {
                generalArrivalListSearchCriteria.setClientGroupId(getClientGroupId(configurationResource.getClientGroup()));
            }
        }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeneralArrivalListSearchCriteria> entity = new HttpEntity<>(generalArrivalListSearchCriteria, httpHeaders);
        ParameterizedTypeReference<List<GeneralArrivalListItemResource>> responseType = new ParameterizedTypeReference<List<GeneralArrivalListItemResource>>() {};
        ResponseEntity<List<GeneralArrivalListItemResource>> listResponseEntity = RestUtils.exchange(generalArrivalListUrl, HttpMethod.POST, entity, responseType);
        List<GeneralArrivalListItemResource> listItemResources = listResponseEntity.getBody();

        //To get All Supplier ids
        for (GeneralArrivalListItemResource itemResource : listItemResources)
        {
            supplierId.add(itemResource.getSupplierId());
        }

        for(String s: supplierId)
        {
            generalArrivalList= new ArrayList<>();
            for (GeneralArrivalListItemResource resource : listItemResources)
            {
                if (resource.getSupplierId().equalsIgnoreCase(s))
                {
                    generalArrivalListItem = new GeneralArrivalListItem();
                    generalArrivalListItem.setProductCategory(resource.getProductCategory());
                    generalArrivalListItem.setProductCategorySubType(resource.getProductSubCategory());
                    generalArrivalListItem.setClientType(resource.getClientType());
                    generalArrivalListItem.setClientGroup(getClientGroupName(resource.getGroupNameId()));
                    generalArrivalListItem.setSupplierName(getSupName(resource.getSupplierId()));
                    generalArrivalListItem.setClientName(getClientNameUsingClientIDAndType(resource.getClientId(), resource.getClientType())/*getClientName(resource.getClientId(),resource.getClientType())*/);
                    generalArrivalList.add(generalArrivalListItem);
                }
            }
            toReturn.add(generalArrivalList);
        }

        return toReturn;
    }




    private DocumentReferenceResource generatePdf(ConfigurationResource configurationResource, ArrivalListInfo arrivalListInfo) throws IOException, OperationException {

        FileOutputStream fileOutputStream = new FileOutputStream(generalName);
        NewDocumentResource documentResource = null;
        DocumentReferenceResource documentReferenceResource = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;

        if (configurationResource!=null && arrivalListInfo!=null) {
            if (configurationResource.getCutOffGeneration() != null || !configurationResource.getCutOffGeneration().isZero()) {
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

                    PdfPTable table = new PdfPTable(7);
                    table.setSpacingBefore(15f);
                    table.setSpacingAfter(10f);
                    table.setTotalWidth(770F);
                    table.writeSelectedRows(0, -1, 34, 750,writer.getDirectContent());

                    Paragraph productCategory = new Paragraph(PdfHeaders.PRODUCT_CATEGORY.getvalue(), headingFont);
                    Paragraph productCategorySubType = new Paragraph(PdfHeaders.PRODUCT_CATEGORY_SUB_TYPE.getvalue(), headingFont);
                    Paragraph clientType = new Paragraph(PdfHeaders.CLIENT_TYPE.getvalue(), headingFont);
                    Paragraph clientGroup = new Paragraph(PdfHeaders.CLIENT_GROUP.getvalue(), headingFont);
                    Paragraph clientName = new Paragraph(PdfHeaders.CLIENT_NAME.getvalue(), headingFont);
                    Paragraph supplierName = new Paragraph(PdfHeaders.SUPPLIER_NAME.getvalue(), headingFont);
                    Paragraph generatedDateTime = new Paragraph(PdfHeaders.GENERATED_DATE_AND_TIME.getvalue(), headingFont);


                    PdfPCell cell1 = new PdfPCell(productCategory);
                    PdfPCell cell2 = new PdfPCell(productCategorySubType);
                    PdfPCell cell3 = new PdfPCell(clientType);
                    PdfPCell cell4 = new PdfPCell(clientGroup);
                    PdfPCell cell5 = new PdfPCell(clientName);
                    PdfPCell cell6 = new PdfPCell(supplierName);
                    PdfPCell cell7 = new PdfPCell(generatedDateTime);

                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                    table.addCell(cell4);
                    table.addCell(cell5);
                    table.addCell(cell6);
                    table.addCell(cell7);


                    List<GeneralArrivalListItem> generalArrivalListItem = arrivalListInfo.getGeneralArrivalListItem();

                    for (GeneralArrivalListItem item : generalArrivalListItem)
                    {
                        Paragraph cell11 = new Paragraph(item.getProductCategory(), normalFont);
                        Paragraph cell12 = new Paragraph(item.getProductCategorySubType(), normalFont);
                        Paragraph cell13 = new Paragraph(item.getClientType(), normalFont);
                        Paragraph cell14 = new Paragraph(item.getClientGroup(), normalFont);
                        Paragraph cell15 = new Paragraph(item.getClientName(), normalFont);
                        Paragraph cell16 = new Paragraph(item.getSupplierName(), normalFont);
                        Paragraph cell17 = new Paragraph(arrivalListInfo.getGeneratedDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm")), normalFont);

                        table.addCell(new PdfPCell(cell11));
                        table.addCell(new PdfPCell(cell12));
                        table.addCell(new PdfPCell(cell13));
                        table.addCell(new PdfPCell(cell14));
                        table.addCell(new PdfPCell(cell15));
                        table.addCell(new PdfPCell(cell16));
                        table.addCell(new PdfPCell(cell17));
                    }


                    document.add(table);
                    document.close();
                    fileOutputStream = new FileOutputStream(generalName);

                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte bytes[]= byteArrayOutputStream.toByteArray();

                fileOutputStream.write(bytes);
                fileOutputStream.close();


                documentResource=new NewDocumentResource();
                documentResource.setType("ArrivalList ");
                i++;
                documentResource.setName("General Arrival List".concat(String.valueOf(i)));
                documentResource.setCategory("Category");
                documentResource.setExtension("pdf");
                documentResource.setSubCategory("SubCategory");
                try {
                    documentReferenceResource = documentLibraryService.create(null, documentResource, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                    if(!StringUtils.isEmpty(documentReferenceResource.getId())){
                        // code to delete
                    }

                } catch (RepositoryException e) {
                    e.printStackTrace();
                }


            }
        }
        File file = new File(generalName);
        System.out.println("File Name" + file.getName());
        file.deleteOnExit();
        return documentReferenceResource;
    }

    private EmailResponse sendAnEmail(String documentId,String supplierId) throws DocumentException, OperationException {
        URI url = UriComponentsBuilder.fromUriString(sendEmailUsingBodyAndDocuments).build().encode().toUri();
        HttpEntity<EmailUsingBodyAndDocumentsResource> httpEntity = new HttpEntity<>(getBodyAndDocs(documentId,supplierId));
        RestTemplate restTemplate = RestUtils.getTemplate();
        ResponseEntity<EmailResponse> emailResponse = restTemplate.exchange(url, HttpMethod.POST,httpEntity,EmailResponse.class);
        return emailResponse.getBody();
    }

    private EmailUsingBodyAndDocumentsResource getBodyAndDocs(String documentId,String supplierId) throws DocumentException, OperationException {
        EmailUsingBodyAndDocumentsResource emailUsingBodyAndDocumentsResource = new EmailUsingBodyAndDocumentsResource();

        CustomSupplierDetails details = reconfirmationMDMService.getSupplierContactDetails(supplierId);
        emailUsingBodyAndDocumentsResource.setFromMail(fromEmailAddress);
        //TODO: getSupplier name and fetch MDM to get email address of that supplier
        emailUsingBodyAndDocumentsResource.setToMail(Collections.singletonList(details.getEmailId()/*"pooja.gawde@coxandkings.com"*/));
        emailUsingBodyAndDocumentsResource.setSubject("Arrival List ");
        emailUsingBodyAndDocumentsResource.setPriority(EmailPriority.HIGH);
        emailUsingBodyAndDocumentsResource.setBody("This is the arrival list ");
        emailUsingBodyAndDocumentsResource.setDocumentReferenceIDs(Collections.singletonList(documentId));
        emailUsingBodyAndDocumentsResource.setFileAttachments(null);
        emailUsingBodyAndDocumentsResource.setCommunicationTagResource(null);
        return emailUsingBodyAndDocumentsResource;
    }



    public String getSupplierId(String supplierName) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        String supplierId = new String();
        if (!StringUtils.isEmpty(supplierName) || supplierName!=null)
        {
            jsonObject.put("supplier.name",supplierName);
            String filter = supplierUrl + jsonObject.toString().concat("&select=_id");


            URI uri = UriComponentsBuilder.fromUriString(filter).build().encode().toUri();
//            URI uri = UriComponentsBuilder.fromUriString(supplierUrl+filter).build().encode().toUri();

            supplierId = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            supplierId = jsonObjectProvider.getAttributeValue(supplierId, "$.data[0]._id", String.class);
            return supplierId;
        }
        else {
            return String.valueOf(supplierId.isEmpty());
        }
    }

    public String getSupName(String supplierId) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        String supplierName = new String();
        if (!StringUtils.isEmpty(supplierId) || supplierId != null) {
            jsonObject.put("_id", supplierId);
            String filter = supplierUrl + jsonObject.toString().concat("&select=supplier.name");


            URI uri = UriComponentsBuilder.fromUriString(filter).build().encode().toUri();
//            URI uri = UriComponentsBuilder.fromUriString(supplierUrl+filter).build().encode().toUri();

            supplierName = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            supplierName = jsonObjectProvider.getAttributeValue(supplierName, "$.data[0].supplier.name", String.class);
            return supplierName;
        } else {
            return String.valueOf(supplierId.isEmpty());
        }
    }


    public String getClientId(String clientName, String clientType) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        String clientId = new String();
        if (!StringUtils.isEmpty(clientType) || clientType!=null)
        {
            if (!StringUtils.isEmpty(clientName) || clientName!=null)
            {
                if (clientType.equalsIgnoreCase("B2B")) {
                    jsonObject.put("clientProfile.clientDetails.clientType",clientType);
                    jsonObject.put("clientProfile.clientDetails.clientName",clientName);
                    String b2bClientUrl = b2bClientId + "?filter=" + jsonObject.toString();

                    URI uri = UriComponentsBuilder.fromUriString(b2bClientUrl).build().encode().toUri();
                    String json = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                    clientId = jsonObjectProvider.getAttributeValue(json,"$.data[0]._id", String.class);
                    return clientId;
                }
                else
                {
                    jsonObject.put("clientDetails.clientType", clientType);
                    jsonObject.put("clientDetails.clientName", clientName);
                    String b2cClientUrl = b2cClientId + "?filter=" + jsonObject.toString();
                    URI uri = UriComponentsBuilder.fromUriString(b2bClientId).build().encode().toUri();
                    String json = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                    clientId = jsonObjectProvider.getAttributeValue(json,"$.data[0]._id", String.class);
                    return clientId;
                }
            }
        }
        else {
            return String.valueOf(clientId.isEmpty());
        }
        return null;
    }

    public String getClientGroupId(String clientGroupName) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        String clientGroupId = new String();

        if (!StringUtils.isEmpty(clientGroupName) || clientGroupName != null) {
            jsonObject.put("clientGroupName", clientGroupName);
            String filter = clientGroupNameUrl + jsonObject.toString().concat("?select=_id");
            URI uri = UriComponentsBuilder.fromUriString(filter).build().encode().toUri();
            clientGroupId = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            clientGroupName = jsonObjectProvider.getAttributeValue(clientGroupId, "$.clientGroupName", String.class);
            return clientGroupName;
        } else {
            return String.valueOf(clientGroupName.isEmpty());
        }
    }

    public String getClientNameUsingClientIDAndType(String clientId, String clientType) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        String clientName = new String();
        if (!StringUtils.isEmpty(clientType) || clientType!=null)
        {
            if (!StringUtils.isEmpty(clientId) || clientId != null)
            {
                if (clientType.equalsIgnoreCase("B2B"))
                {
                    jsonObject.put("clientProfile.clientDetails.clientType", clientType);
                    jsonObject.put("_id", clientId);
                    String b2bClientUrl = b2bClientId + jsonObject.toString();
                    URI uri = UriComponentsBuilder.fromUriString(b2bClientUrl).build().encode().toUri();
                    String json = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                    clientName = jsonObjectProvider.getAttributeValue(json, "$.data[0].clientProfile.clientDetails.clientName", String.class);
                    return clientName;
                }
                else
                    {
                        jsonObject.put("clientDetails.clientType", clientType);
                    jsonObject.put("_id", clientId);
                        String b2cClientUrl = b2cClientId + jsonObject.toString();
                        URI uri = UriComponentsBuilder.fromUriString(b2cClientUrl).build().encode().toUri();
                        String json = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                        clientName = jsonObjectProvider.getAttributeValue(json, "$.data[0]].clientDetails.clientName", String.class);
                    return clientName;
                }
            }
        }
        return null;
    }

//    private List<GeneralArrivalListItem> getArrival(List<GeneralArrivalListItemResource> generalArrivalListItemResources)
//    {
////        List<String> clientIds = generalArrivalListItemResources.stream().map(GeneralArrivalListItemResource::getClientId).collect(Collectors.toList());
//        Integer size = 0;
//        List<String> clientId = new ArrayList<>();
//        HashSet<String> b2bHashSet = new HashSet<>();
//        HashSet<String> b2cHashSet = new HashSet<>();
//
//        HashMap<String, String> b2bMap = new HashMap<String, String>();
//        HashMap<String, String> b2cMap = new HashMap<String, String>();
//
//        for (GeneralArrivalListItemResource resource : generalArrivalListItemResources)
//        {
//            if (resource.getClientType().equalsIgnoreCase("B2B"))
//            {
//                b2bHashSet.add(resource.getClientId());
//            }
//            if (resource.getClientType().equalsIgnoreCase("B2C"))
//            {
//                b2cHashSet.add(resource.getClientId());
//            }
//
//        }
//
//       for (String s : b2bHashSet)
//           clientId.add(s);
//
//        //TODO:
//
//        return null;
//
//
//    }

    public String getClientGroupName(String clientGroupId) throws JSONException, OperationException {

        String clientGroupName = new String();
        if (!StringUtils.isEmpty(clientGroupId) || clientGroupId != null)
        {

            String filter = clientGroupNameUrl + clientGroupId.concat("?select=clientGroupName");
            URI uri = UriComponentsBuilder.fromUriString(filter).build().encode().toUri();
            clientGroupName = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            clientGroupName = jsonObjectProvider.getAttributeValue(clientGroupName, "$.clientGroupName", String.class);
            return clientGroupName;
        } else {
            return String.valueOf(clientGroupName.isEmpty());
        }
    }


}


