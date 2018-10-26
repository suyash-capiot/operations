package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.ManageDocListener;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentCommunicationType;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentEntityReference;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.repository.managedocumentation.CutOffDateConfigurationRepository;
import com.coxandkings.travel.operations.repository.managedocumentation.HandoverAndCustomerDocInfoRepository;
import com.coxandkings.travel.operations.repository.managedocumentation.MDMUnConfiguredDataRepository;
import com.coxandkings.travel.operations.repository.managedocumentation.ReceivedDocsBookingInfoRepository;
import com.coxandkings.travel.operations.resource.managedocumentation.*;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentHandlingGrid;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.managedocumentation.*;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.managedocumentation.DocumentUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ManageDocumentationListener implements ManageDocListener {

    @Value(value = "${manage_documentation.be.get_document_details}")
    private String getBookingDocsUrl;

    @Autowired
    private DocumentMasterRequirements documentMasterRequirements;

    @Autowired
    private MDMUnConfiguredDataRepository mdmUnConfiguredDataRepository;

    @Autowired
    private CutOffDateConfigurationRepository cutOffDateConfigurationRepository;

    @Autowired
    private DocumentUtils documentUtils;

    @Autowired
    private GenerateDocumentService generateDocumentService;

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ReceivedDocsBookingInfoRepository receivedDocsBookingInfoRepository;

    @Autowired
    private HandoverAndCustomerDocInfoRepository handoverAndCustomerDocInfoRepository;

    @Autowired
    private UpdateReceivedDocsBookingInfoDetails updateReceivedDocsBookingInfoDetails;

    @Autowired
    private UpdateDocumentDetails updateDocumentDetails;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LogManager.getLogger(ManageDocumentationListener.class);

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.MANAGE_DOCUMENTATION;
    }

    @Async
    @Override
    public void processBooking(OpsBooking aBooking, KafkaBookingMessage message) throws OperationException, IOException {

        if (message.getActionType().equals(BookingActionConstants.JSON_PROP_NEW_BOOKING)) {
            if (aBooking.getStatus().equals(OpsBookingStatus.CNF)) {
                Boolean receiveDocs = null;
                for (OpsProduct product : aBooking.getProducts()) {
                    Boolean result = processDocuments(aBooking, product, false);
                    if (receiveDocs == null && result != null)
                        receiveDocs = result;
                }
                if (receiveDocs != null && receiveDocs) {
                    documentDetailsService.sendAlertAndCreateTodoTaskForOpsUserToCollectDocuments(aBooking.getBookID(), null, null);
                }
            }
        } else {
            if (message.getOrderNo() != null) {
                if (!message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_FULLCANCEL) &&
                        !message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_FULLCANCEL) &&
                        !message.getActionType().equals(BookingActionConstants.JSON_PROP_ERROR_BOOKING) &&
                        !message.getActionType().equals(BookingActionConstants.JSON_PROP_ON_REQUEST_BOOKING)) {
                    try {
                        OpsProduct product = aBooking.getProducts().stream().filter(product1 -> product1.getOrderID().equals(message.getOrderNo())).findFirst().get();
                        if (product.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.OK)) {
                            processDocuments(aBooking, product, true);
                        }

                    } catch (Exception ex) {
                        logger.info("Failed to process documents for amended order " + message.getOrderNo());
                    }
                }
            }

        }

    }

    private Boolean processDocuments(OpsBooking aBooking, OpsProduct product, Boolean disablePreviousVersions) throws OperationException, IOException {
        Boolean receivedDocs = null;
        List<DocumentHandlingGrid> mdmDocumentResponse = documentMasterRequirements.getDocumentHandlingGridDetails(documentDetailsService.getDocHandlingMasterSearchCriteria(product));
        if (mdmDocumentResponse == null || mdmDocumentResponse.size() == 0) {
            documentDetailsService.saveMDMUnConfiguredDataDetails(product, aBooking);
        } else {

            /*if (product.getProductSubCategory().equals("Visa")) {
                //todo check in db whether passport n visa details exist for that pax and verify
            }*/

            List<DocumentSetting> handoverDocs = new ArrayList<>();
            mdmDocumentResponse.stream().forEach(documentHandlingGrid ->
                    handoverDocs.addAll(documentHandlingGrid.getDocumentSetting().
                            stream().filter(documentSetting -> documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())
                            && documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType())).collect(Collectors.toList())));

            List<DocumentSetting> customerDocs = new ArrayList<>();
            mdmDocumentResponse.stream().forEach(documentHandlingGrid ->
                    customerDocs.addAll(documentHandlingGrid.getDocumentSetting().stream().filter(documentSetting ->
                            !documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())
                                    && documentSetting.getSalesStage().equals("postbooking")).collect(Collectors.toList())));

            if (disablePreviousVersions) {
                String result = RestUtils.getForObject(getBookingDocsUrl + aBooking.getBookID(), String.class);
                DocumentDetailsResponse documentDetailsResponse = null;
                try {
                    documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);
                } catch (Exception ex) {
                    logger.info("No document details found for booking");
                }
                if (documentDetailsResponse != null) {
                    for (DocumentSetting documentSetting : handoverDocs) {
                        disablePreviousVersionDocs(documentSetting, documentDetailsResponse, product, aBooking.getBookID());
                    }
                }
            }

            if (handoverDocs != null && handoverDocs.size() >= 1) {
                for (DocumentSetting documentSetting : handoverDocs) {
                    //Generate handover document if reconfirmation is done by supplier, client or both as applicable.
                    if (documentDetailsService.checkTriggerEvent(documentSetting, product, aBooking.getBookID())
                            && documentDetailsService.isReconfirmed(aBooking, product)) {
                        try {
                            generateDocumentService.generateHandOverDocument(aBooking, documentSetting.getDocumentName(), product, documentSetting);
                        } catch (Exception ex) {
                            logger.info("Failed to generate Hanover Documents for Document Name " + documentSetting.getDocumentName());
                        }
                    } else
                        documentDetailsService.saveHandoverAndCustomerDocInfo(aBooking.getBookID(), product, documentSetting);
                }
            }

            if (customerDocs != null && customerDocs.size() >= 1) {
                Set<String> documentSettings = customerDocs.stream().filter(documentSetting ->
                        documentSetting.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType())
                                || documentSetting.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType()))
                        .map(documentSetting -> documentSetting.getId()).collect(Collectors.toSet());
                if (documentSettings != null && documentSettings.size() >= 1) {
                    receivedDocs = true;
                    documentDetailsService.saveReceivedDocsBookingInfo(aBooking.getBookID(), product.getOrderID(), documentSettings);
                }
                for (DocumentSetting documentSetting : customerDocs) {
                    documentDetailsService.saveCutOffDateConfigurationDetails(documentSetting, aBooking, product);
                }

                //uncomment this code when document templates for documents to be sent to customer are available
                /*List<DocumentSetting> sentDocuments = new ArrayList<>();
                sentDocuments.addAll(customerDocs.stream().filter(documentSetting -> documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType())).collect(Collectors.toList()));
                if (sentDocuments.size() >= 1) {
                    for (DocumentSetting documentSetting : sentDocuments) {
                        documentDetailsService.saveHandoverAndCustomerDocInfo(aBooking.getBookID(), product, documentSetting);
                    }
                }*/

            }
        }
        return receivedDocs;
    }

    private void disablePreviousVersionDocs(DocumentSetting documentSetting, DocumentDetailsResponse documentDetailsResponse, OpsProduct product, String bookId) throws OperationException, JsonProcessingException {

        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE.getEntityReference())) {
            OrderDocuments orderDocuments = null;
            try {
                orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDocument -> orderDocument.getOrderId().equals(product.getOrderID())).findFirst().get();
            } catch (Exception ex) {
                logger.info("No Document Details found for Order " + product.getOrderID());
            }

            if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive() != null && bookingDocumentDetailsResource.getActive()).map(bookingDocumentDetailsResource -> updateDocumentDetails.changeActiveStatusOfDocs(bookingDocumentDetailsResource)).collect(Collectors.toList());
                DocumentsResource documentsResource = new DocumentsResource();
                documentsResource.setOrderId(product.getOrderID());
                documentsResource.setBookID(bookId);
                documentDetailsService.updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
            }

        } else if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE.getEntityReference())) {
            List<String> roomIds = documentDetailsService.getListOfRoomDetails(product);
            if (roomIds != null && roomIds.size() >= 1) {
                for (String roomId : roomIds) {
                    RoomDocuments roomDoc = null;
                    try {
                        roomDoc = documentDetailsResponse.getRoomDocument().stream().filter(roomDocuments1 -> roomDocuments1.getRoomID().equals(roomId)).findFirst().get();
                    } catch (Exception ex) {
                        logger.info("No Document Details found for  Room " + roomId);
                    }
                    if (roomDoc != null && roomDoc.getDocumentInfo() != null && roomDoc.getDocumentInfo().size() >= 1) {
                        roomDoc.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getOrderId().equals(product.getOrderID()) && bookingDocumentDetailsResource.getActive() != null && bookingDocumentDetailsResource.getActive()).map(bookingDocumentDetailsResource -> updateDocumentDetails.changeActiveStatusOfDocs(bookingDocumentDetailsResource)).collect(Collectors.toList());
                        DocumentsResource documentsResource = new DocumentsResource();
                        documentsResource.setRoomId(roomId);
                        documentDetailsService.updateRoomLevelDocuments(documentsResource, roomDoc.getDocumentInfo());
                    }
                }
            }
        } else if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE.getEntityReference())) {
            List<String> paxIds = null;
            if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference())) {
                paxIds = documentDetailsService.getListOfPaxDetails(product, false);
            } else {
                paxIds = documentDetailsService.getListOfPaxDetails(product, true);
            }
            if (paxIds != null && paxIds.size() >= 1) {
                for (String paxId : paxIds) {
                    PaxDocuments paxDocuments = null;
                    try {
                        paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDocuments1 -> paxDocuments1.getPaxID().equals(paxId)).findFirst().get();
                    } catch (Exception ex) {
                        logger.info("No Document Details found for Pax " + paxId);
                    }

                    if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                        paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(product.getOrderID()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive() != null && bookingDocumentDetailsResource.getActive()).map(bookingDocumentDetailsResource -> updateDocumentDetails.changeActiveStatusOfDocs(bookingDocumentDetailsResource)).collect(Collectors.toList());
                        DocumentsResource documentsResource = new DocumentsResource();
                        documentsResource.setPaxId(paxId);
                        documentDetailsService.updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                    }
                }
            }
        }

    }


}
