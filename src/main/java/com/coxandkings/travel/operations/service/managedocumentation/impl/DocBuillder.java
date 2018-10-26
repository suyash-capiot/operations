package com.coxandkings.travel.operations.service.managedocumentation.impl;


import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.managedocumentation.CommunicationTemplate;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentEntityReference;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsFlightPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentVersion;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.managedocumentation.*;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentCommunication;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentDetailsService;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentMDMService;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.AbstractProductFactory;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DocBuillder {

    private static final Logger logger = LogManager.getLogger(DocBuillder.class);

    @Value("${document_library.upload}")
    private String documentApi;

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @Autowired
    private DocumentLibraryService documentLibraryService;

    @Autowired
    private DocumentCommunication documentCommunication;

    @Autowired
    private DocumentMDMService documentMDMService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value("${manage_document.email.subject}")
    private String handOverDocumentSubject;

    @Value(value = "${manage_documentation.be.acco_rooms_pax_info}")
    private String accoRoomsPaxInfo;

    @Value(value = "${manage_documentation.be.air_pax_info}")
    private String airPaxInfo;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Value(value = "${manage_documentation.be.get_document_details}")
    private String getBookingDocsUrl;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public void build(AbstractProductFactory product, OpsProduct opsProduct, OpsBooking opsBooking, String documentName, DocumentSetting documentSetting) throws OperationException {
        File file = product.GenerateDocument(opsProduct, opsBooking, documentName);
        String uniqueFileName = opsBooking.getBookID() + UUID.randomUUID();

        String uploadedDocumentId = uploadDocument(opsProduct, opsBooking, file, uniqueFileName);
        DocumentsResource documentsResource = new DocumentsResource();

        documentsResource.setDocumentId(uploadedDocumentId);
        documentsResource.setBookID(opsBooking.getBookID());
        documentsResource.setOrderId(opsProduct.getOrderID());
        documentsResource.setDocumentWise(DocumentEntityReference.fromString(documentSetting.getDocumentsAsPer()));
        documentsResource.setDocumentSettingId(documentSetting.getId());
        documentsResource.setDocumentName(documentSetting.getDocumentName());
        documentsResource.setFileName(file.getName());

        documentsResource.setKafkaGenerated(true);
        documentsResource.setFileType("pdf");

        try {
            BookingDocumentDetailsResource bookingDocumentDetailsResource = documentDetailsService.createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT());

            //Todo:need update document
            switch (DocumentEntityReference.fromString(documentSetting.getDocumentsAsPer())) {
                case DOCUMENT_LEAD_PAX_WISE:
                    List<String> paxIds = getListOfPaxDetails(opsProduct, true);
                    if (paxIds != null && paxIds.size() >= 1) {
                        documentsResource.setPaxId(paxIds.iterator().next());
                        updatePaxDocuments(opsBooking.getBookID(), documentsResource, bookingDocumentDetailsResource);
                    }

                case DOCUMENT_BOOKING_WISE:
                    updateOrderDocuments(opsBooking.getBookID(), opsProduct.getOrderID(), documentsResource, bookingDocumentDetailsResource);
                    break;

                case DOCUMENT_ROOM_WISE:
                    updateRoomDocuments(opsBooking.getBookID(), documentsResource, bookingDocumentDetailsResource, opsProduct);
                    break;

                case DOCUMENT_CABIN_WISE:
                    updateRoomDocuments(opsBooking.getBookID(), documentsResource, bookingDocumentDetailsResource, opsProduct);
                    break;

                case DOCUMENT_PAX_WISE:
                    List<String> paxIDs = getListOfPaxDetails(opsProduct, true);
                    if (paxIDs != null && paxIDs.size() >= 1) {
                        for (String paxId : paxIDs) {
                            documentsResource.setPaxId(paxId);
                            updatePaxDocuments(opsBooking.getBookID(), documentsResource, bookingDocumentDetailsResource);
                        }
                    }


                case DOCUMENT_ORDER_WISE:
                    updateOrderDocuments(opsBooking.getBookID(), opsProduct.getOrderID(), documentsResource, bookingDocumentDetailsResource);
                    break;

                default:
                    logger.error("Invalid document type configured");
                    break;
            }
        } catch (Exception e) {
            logger.error("Unable to create Booking document details " + documentsResource, e);
            throw new OperationException(Constants.OPS_ERR_20601);
        }

        try {
            List<DocumentVersion> documentsForOrder = documentDetailsService.getDocumentsForOrder(documentsResource);

            List<String> referenceId = new ArrayList<>();
            for (DocumentVersion documentVersion : documentsForOrder) {
                referenceId.add(documentVersion.getDocumentId());
            }
            if (referenceId.size() == 0) {
                referenceId.add(uploadedDocumentId);
            }
            String clientId = opsBooking.getClientID();
            String clientType = opsBooking.getClientType();

            String clientName = documentMDMService.getClientName(clientId, MDMClientType.fromString(clientType));

            String clientEmail = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType));

            //send mail

            Map<String, String> dynamicVariables = new ConcurrentHashMap<>();
            dynamicVariables.put("First Name", clientName);
            dynamicVariables.put("documentName", documentSetting.getDocumentName());
            dynamicVariables.put("Order_Id", opsProduct.getOrderID());
            dynamicVariables.put("Booking_Ref_Id", opsBooking.getBookID());


            EmailResponse emailResponse = documentCommunication.sendEmailToClient(referenceId, clientEmail, handOverDocumentSubject, dynamicVariables);

            //set communication details
            if (null != emailResponse) {
                documentsResource.setCommunicationId(emailResponse.getMessageID());
                documentsResource.setCommunicationTemplate(CommunicationTemplate.COMM_EMAIL_HNDOVR_DOCS);
                documentDetailsService.setCommDetailsForDocuments(documentsResource);
            }
        } catch (Exception e) {
            logger.error("Unable to get document for order", e);
        }
    }

    private DocumentDetailsResponse getDocumentDetailsResponse(String bookingRefNo) {
        String result = RestUtils.getForObject(getBookingDocsUrl + bookingRefNo, String.class);
        DocumentDetailsResponse documentDetailsResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);
        } catch (Exception ex) {
            logger.info("No document details found for booking");
        }
        return documentDetailsResponse;
    }

    private void updateOrderDocuments(String bookingRefNo, String orderId, DocumentsResource documentsResource, BookingDocumentDetailsResource bookingDocumentDetailsResource) throws OperationException {
        OrderDocuments orderLevelDocuments = null;

        DocumentDetailsResponse documentDetailsResponse = getDocumentDetailsResponse(bookingRefNo);
        try {
            orderLevelDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDocuments -> orderDocuments.getOrderId().equals(orderId)).findFirst().get();
        } catch (Exception ex) {
            logger.info("No details found for Order Documents");
        }

        if (orderLevelDocuments != null && orderLevelDocuments.getDocumentInfo() != null && orderLevelDocuments.getDocumentInfo() != null) {
            orderLevelDocuments.getDocumentInfo().add(bookingDocumentDetailsResource);
            documentDetailsService.updateOrderLevelDocuments(documentsResource, orderLevelDocuments.getDocumentInfo());
        } else {
            List<BookingDocumentDetailsResource> documentList = new ArrayList<>();
            documentList.add(bookingDocumentDetailsResource);
            documentDetailsService.updateOrderLevelDocuments(documentsResource, documentList);
        }

    }

    private void updateRoomDocuments(String bookingRefNo, DocumentsResource documentsResource, BookingDocumentDetailsResource bookingDocumentDetailsResource, OpsProduct product) throws OperationException {
        List<String> roomIds = null;
        try {
            roomIds = documentDetailsService.getListOfRoomDetails(product);
        } catch (Exception e) {
            throw new OperationException("Unable to get room details" + product);
        }

        RoomDocuments roomDocuments = null;
        String roomId = roomIds.iterator().next();
        documentsResource.setRoomId(roomId);
        DocumentDetailsResponse documentDetailsResponse = getDocumentDetailsResponse(bookingRefNo);
        try {
            roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
        } catch (Exception ex) {
            logger.info("No details found for Room Documents");
        }

        if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
            roomDocuments.getDocumentInfo().add(bookingDocumentDetailsResource);
            documentDetailsService.updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
        } else {
            List<BookingDocumentDetailsResource> documentList = new ArrayList<>();
            documentList.add(bookingDocumentDetailsResource);
            documentDetailsService.updateRoomLevelDocuments(documentsResource, documentList);
        }
        //List<RoomDocuments> roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDocuments1 -> roomIds.contains(roomDocuments1.getRoomID()) && roomDocuments1.getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(orderId) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive())).collect(Collectors.toList());

    }

    private void updatePaxDocuments(String bookingRefNo, DocumentsResource documentsResource, BookingDocumentDetailsResource bookingDocumentDetailsResource) {
        PaxDocuments paxDocuments = null;

        DocumentDetailsResponse documentDetailsResponse = getDocumentDetailsResponse(bookingRefNo);
        try {
            paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
        } catch (Exception ex) {
            logger.error("No details found for Pax Documents");
        }

        if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
            paxDocuments.getDocumentInfo().getDocumentInfo().add(bookingDocumentDetailsResource);
            documentDetailsService.updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
        } else {
            List<BookingDocumentDetailsResource> documentList = new ArrayList<>();
            documentList.add(bookingDocumentDetailsResource);
            documentDetailsService.updatePaxLevelDocuments(documentsResource, documentList);
        }

    }

    private List<String> getListOfPaxDetails(OpsProduct product, boolean leadPax) throws JsonProcessingException {
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
                List<OpsAccommodationPaxInfo> accommodationPaxInfos = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product), accoRoomsPaxInfo, OpsAccommodationPaxInfo.class);
                if (leadPax) {
                    return accommodationPaxInfos.stream().filter(opsAccommodationPaxInfo -> opsAccommodationPaxInfo.getLeadPax()).map(opsAccommodationPaxInfo -> opsAccommodationPaxInfo.getPaxID()).collect(Collectors.toList());
                } else {
                    return accommodationPaxInfos.stream().map(opsAccommodationPaxInfo -> opsAccommodationPaxInfo.getPaxID()).collect(Collectors.toList());
                }

            case PRODUCT_SUB_CATEGORY_FLIGHT:
                List<OpsFlightPaxInfo> flightPaxInfos = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product), airPaxInfo, OpsFlightPaxInfo.class);
                if (leadPax) {
                    return flightPaxInfos.stream().filter(opsFlightPaxInfo -> opsFlightPaxInfo.getLeadPax()).map(opsFlightPaxInfo -> opsFlightPaxInfo.getPassengerID()).collect(Collectors.toList());
                } else {
                    return flightPaxInfos.stream().map(opsFlightPaxInfo -> opsFlightPaxInfo.getPassengerID()).collect(Collectors.toList());
                }

            default:
                return null;
        }
    }

    /**
     * Upload document in document library
     *
     * @param opsProduct
     * @param opsBooking
     * @param file
     * @return document id
     */
    private String uploadDocument(OpsProduct opsProduct, OpsBooking opsBooking, File file, String uniqueFileName) {
        //update this file into document library


        File fileWithNewName = new File(file.getParent(), uniqueFileName + ".pdf");
        file.renameTo(fileWithNewName);
        ResponseEntity<DocumentReferenceResource> entity = null;
        UriComponentsBuilder builder = null;

        builder = UriComponentsBuilder.fromHttpUrl(documentApi);

        if (null != builder) {
            builder.queryParam("type", "pdf");
            builder.queryParam("name", uniqueFileName);
            builder.queryParam("category", opsProduct.getProductCategory());
            builder.queryParam("subCategory", opsProduct.getProductCategory());
            builder.queryParam("clientId", opsBooking.getClientID());
            builder.queryParam("bookId", opsBooking.getBookID());
        }
        LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file", new FileSystemResource(fileWithNewName));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        try {
            URI documentUri = builder.build().encode().toUri();
            entity = mdmRestUtils.exchange(documentUri, HttpMethod.POST, params, DocumentReferenceResource.class, MediaType.MULTIPART_FORM_DATA);
        } catch (Exception e1) {
            logger.error(Constants.OPS_ERR_20601, e1);
        } finally {
            boolean fileDelete = fileWithNewName.delete();
            if (!fileDelete) {
                logger.error("File delete failed; take appropriate action");
            }
        }

        String documentId = null;
        if (null != entity) {
            documentId = entity.getBody().getId();
        }

        return documentId;
    }

}
