package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.managedocumentation.CutOffDateConfigurationSearchCriteria;
import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentCommunicationType;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentEntityReference;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingAttribute;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.managedocumentation.CutOffDateConfiguration;
import com.coxandkings.travel.operations.model.managedocumentation.HandoverAndCustomerDocInfo;
import com.coxandkings.travel.operations.model.managedocumentation.MDMUnConfiguredData;
import com.coxandkings.travel.operations.model.managedocumentation.ReceivedDocsBookingInfo;
import com.coxandkings.travel.operations.repository.managedocumentation.CutOffDateConfigurationRepository;
import com.coxandkings.travel.operations.repository.managedocumentation.HandoverAndCustomerDocInfoRepository;
import com.coxandkings.travel.operations.repository.managedocumentation.MDMUnConfiguredDataRepository;
import com.coxandkings.travel.operations.repository.managedocumentation.ReceivedDocsBookingInfoRepository;
import com.coxandkings.travel.operations.resource.managedocumentation.*;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentHandlingGrid;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.managedocumentation.*;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class ManageDocumentationBatchJobServiceImpl implements ManageDocumentationBatchJobService {

    @Value(value = "${manage_documentation.be.get_document_details}")
    private String getBookingDocsUrl;

    @Autowired
    private CutOffDateConfigurationRepository cutOffDateConfigurationRepository;

    @Autowired
    private MDMUnConfiguredDataRepository mdmUnConfiguredDataRepository;

    @Autowired
    private DocumentMasterRequirements documentMasterRequirements;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private ReceivedDocsBookingInfoRepository receivedDocsBookingInfoRepository;

    @Autowired
    private UpdateReceivedDocsBookingInfoDetails updateReceivedDocsBookingInfoDetails;

    @Autowired
    private HandoverAndCustomerDocInfoRepository handoverAndCustomerDocInfoRepository;

    @Autowired
    private GenerateDocumentService generateDocumentService;

    private static final Logger logger = LogManager.getLogger(ManageDocumentationBatchJobServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendCutOffAlert() throws OperationException {

        CutOffDateConfigurationSearchCriteria searchCriteria = new CutOffDateConfigurationSearchCriteria();
        searchCriteria.setCutOffDate(ZonedDateTime.now());
        List<CutOffDateConfiguration> cutOffCalculationList = cutOffDateConfigurationRepository.searchDetails(searchCriteria);

        if (cutOffCalculationList != null && cutOffCalculationList.size() >= 1) {
            Set<String> bookIds = cutOffCalculationList.stream().map(cutOffDateConfiguration -> cutOffDateConfiguration.getBookId()).collect(Collectors.toSet());
            for (String bookId : bookIds) {
                String result = RestUtils.getForObject(getBookingDocsUrl + bookId, String.class);
                DocumentDetailsResponse documentDetailsResponse = null;
                try {
                    documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);
                } catch (Exception ex) {
                    logger.info("No document details found for booking");
                }
                if (documentDetailsResponse != null) {
                    for (CutOffDateConfiguration cutOffDateConfiguration : cutOffCalculationList.stream().filter(cutOffDateConfiguration -> cutOffDateConfiguration.getBookId().equals(bookId)).collect(Collectors.toList())) {
                        if (cutOffDateConfiguration.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE.getEntityReference())) {
                            OrderDocuments orderDocuments = null;
                            try {
                                orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(cutOffDateConfiguration.getOrderId())).findFirst().get();
                            } catch (Exception ex) {
                                logger.info("No document details found for Order " + cutOffDateConfiguration.getOrderId());
                            }
                            if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                                List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(cutOffDateConfiguration.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getCopyTo() == null).collect(Collectors.toList());
                                if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1)
                                    checkDocStatus(bookingDocumentDetailsResources, cutOffDateConfiguration);
                                else
                                    createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
                            } else {
                                createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
                            }
                        } else if (cutOffDateConfiguration.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE.getEntityReference()) || cutOffDateConfiguration.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference())) {
                            PaxDocuments paxDocuments = null;
                            try {
                                paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(cutOffDateConfiguration.getPaxId())).findFirst().get();
                            } catch (Exception ex) {
                                logger.info("No document details found for Pax " + cutOffDateConfiguration.getPaxId());
                            }
                            if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                                List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(cutOffDateConfiguration.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(cutOffDateConfiguration.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getCopyTo() == null).collect(Collectors.toList());
                                if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1)
                                    checkDocStatus(bookingDocumentDetailsResources, cutOffDateConfiguration);
                                else
                                    createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
                            } else {
                                createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
                            }
                        } else if (cutOffDateConfiguration.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE.getEntityReference()) || cutOffDateConfiguration.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE.getEntityReference())) {
                            List<String> roomIds = documentDetailsService.getListOfRoomDetails(opsBookingService.getProduct(cutOffDateConfiguration.getBookId(), cutOffDateConfiguration.getOrderId()));
                            List<BookingDocumentDetailsResource> documentDetails = new ArrayList<>();
                            for (String roomId : roomIds) {
                                RoomDocuments roomDocuments = null;
                                try {
                                    roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                                } catch (Exception ex) {
                                    logger.info("No document details found for Room " + roomId);
                                }
                                if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                                    documentDetails.addAll(roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(cutOffDateConfiguration.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(cutOffDateConfiguration.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getCopyTo() == null).collect(Collectors.toList()));
                                }
                            }
                            if (documentDetails == null || documentDetails.size() == 0) {
                                createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
                            } else {
                                checkDocStatus(documentDetails, cutOffDateConfiguration);
                            }
                        }
                    }
                }
            }
        }

    }

    private void checkDocStatus(List<BookingDocumentDetailsResource> documentDetailsResources, CutOffDateConfiguration cutOffDateConfiguration) throws OperationException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("documentHandlingGrid.documentSetting._id", cutOffDateConfiguration.getDocumentSettingId());
        DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
        DocumentStatus documentStatus = documentDetailsService.getDocumentStatus(documentSetting, documentDetailsResources);
        if (cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType())) {
            if (!documentStatus.equals(DocumentStatus.VERIFIED)) {
                createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
            } else {
                updateTodoTaskStatusForCutOffDueDate(cutOffDateConfiguration);
                cutOffDateConfigurationRepository.deleteDetails(cutOffDateConfiguration.getId());
            }
        } else if (cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())) {
            if (!documentStatus.equals(DocumentStatus.RECEIVED)) {
                createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
            } else {
                updateTodoTaskStatusForCutOffDueDate(cutOffDateConfiguration);
                cutOffDateConfigurationRepository.deleteDetails(cutOffDateConfiguration.getId());
            }
        } else {
            if (!documentStatus.equals(DocumentStatus.SENT)) {
                createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(cutOffDateConfiguration);
            } else {
                updateTodoTaskStatusForCutOffDueDate(cutOffDateConfiguration);
                cutOffDateConfigurationRepository.deleteDetails(cutOffDateConfiguration.getId());
            }
        }
    }

    private void createToDoTaskAndSendAlertToOpsUserToReceiveOrSendDocuments(CutOffDateConfiguration cutOffDateConfiguration) {
        if (cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType())) {
            createToDoTaskAndSendAlertToOpsUserToReceiveDocumentsFromCustomer(cutOffDateConfiguration, "Customer");
        } else if (cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())) {
            createToDoTaskAndSendAlertToOpsUserToReceiveDocumentsFromCustomer(cutOffDateConfiguration, "Supplier");
        } else {
            createToDoTaskAndSendAlertToOpsUserToSendDocumentsToCustomer(cutOffDateConfiguration);
        }
    }

    private void createToDoTaskAndSendAlertToOpsUserToReceiveDocumentsFromCustomer(CutOffDateConfiguration cutOffDateConfiguration, String receiveDocFrom) {
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("COLLECT_DOCUMENTS");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("bookID", cutOffDateConfiguration.getBookId());
            entity.put("orderID", cutOffDateConfiguration.getOrderId());
            entity.put("productName", cutOffDateConfiguration.getProductName());
            entity.put("documentName", cutOffDateConfiguration.getDocumentName());
            entity.put("paxName", cutOffDateConfiguration.getPaxName());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to ops user");
        }

        try {
            if (!checkTodoTaskCreationForCustomerDocs(cutOffDateConfiguration)) {
                ToDoTaskResource todo = new ToDoTaskResource();
                todo.setTaskSubTypeId(ToDoTaskSubTypeValues.COLLECT_DOCUMENTS.name());
                todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
                todo.setTaskNameId(ToDoTaskNameValues.CUSTOMER_DOCUMENTATION.getValue());
                todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
                todo.setBookingRefId(cutOffDateConfiguration.getBookId());
                todo.setReferenceId(cutOffDateConfiguration.getId());
                todo.setCreatedByUserId(userService.getLoggedInUserId());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Receive Documents from ").append(receiveDocFrom).append(" for Product Name: ").append(cutOffDateConfiguration.getProductName())
                        .append(" - ").append("Document Name: ").append(cutOffDateConfiguration.getDocumentName()).append(" - ")
                        .append("Pax Name: ").append(cutOffDateConfiguration.getPaxName()).append(" - ").append("Order: ")
                        .append(cutOffDateConfiguration.getOrderId()).append(" - ").append("Booking: ").append(cutOffDateConfiguration.getBookId());
                todo.setRemark(stringBuilder.toString());
                toDoTaskService.save(todo);
            }
        } catch (Exception ex) {
            logger.info("Failed to create todo task for ops user to receive documents");
        }

    }

    private void createToDoTaskAndSendAlertToOpsUserToSendDocumentsToCustomer(CutOffDateConfiguration cutOffDateConfiguration) {
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("SEND_DOCUMENTS");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("bookID", cutOffDateConfiguration.getBookId());
            entity.put("orderID", cutOffDateConfiguration.getOrderId());
            entity.put("productName", cutOffDateConfiguration.getProductName());
            entity.put("documentName", cutOffDateConfiguration.getDocumentName());
            entity.put("paxName", cutOffDateConfiguration.getPaxName());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to ops user");
        }

        try {
            if (!checkTodoTaskCreationForCustomerDocs(cutOffDateConfiguration)) {
                ToDoTaskResource todo = new ToDoTaskResource();
                todo.setTaskSubTypeId(ToDoTaskSubTypeValues.SEND_DOCUMENTS.name());
                todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
                todo.setTaskNameId(ToDoTaskNameValues.CUSTOMER_DOCUMENTATION.getValue());
                todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
                todo.setBookingRefId(cutOffDateConfiguration.getBookId());
                todo.setReferenceId(cutOffDateConfiguration.getId());
                todo.setCreatedByUserId(userService.getLoggedInUserId());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Send Documents to Customer for Product Name: ").append(cutOffDateConfiguration.getProductName()).append(" - ")
                        .append("Document Name: ").append(cutOffDateConfiguration.getDocumentName()).append(" - ")
                        .append("Pax Name: ").append(cutOffDateConfiguration.getPaxName()).append(" - ").append("Order: ")
                        .append(cutOffDateConfiguration.getOrderId()).append(" - ").append("Booking: ").append(cutOffDateConfiguration.getBookId());
                todo.setRemark(stringBuilder.toString());
                toDoTaskService.save(todo);
            }
        } catch (Exception ex) {
            logger.info("Failed to create todo task for ops user to send documents");
        }

    }

    public void updateTodoTaskStatusForCutOffDueDate(CutOffDateConfiguration cutOffDateConfiguration) {
        try {
            if (checkTodoTaskCreationForCustomerDocs(cutOffDateConfiguration)) {
                if (cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()) || cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())) {
                    toDoTaskService.updateToDoTaskStatus(cutOffDateConfiguration.getId(), ToDoTaskSubTypeValues.COLLECT_DOCUMENTS, ToDoTaskStatusValues.COMPLETED);
                } else {
                    toDoTaskService.updateToDoTaskStatus(cutOffDateConfiguration.getId(), ToDoTaskSubTypeValues.SEND_DOCUMENTS, ToDoTaskStatusValues.COMPLETED);
                }
            }
        } catch (Exception ex) {
            logger.info("Failed to update todo task status");
        }
    }

    private boolean checkTodoTaskCreationForCustomerDocs(CutOffDateConfiguration cutOffDateConfiguration) throws OperationException {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setBookingRefId(cutOffDateConfiguration.getBookId());
        toDoCriteria.setReferenceId(cutOffDateConfiguration.getId());
        if (cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()) || cutOffDateConfiguration.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())) {
            toDoCriteria.setTaskSubTypeId(ToDoTaskSubTypeValues.COLLECT_DOCUMENTS.name());
            toDoCriteria.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDoCriteria.setTaskNameId(ToDoTaskNameValues.CUSTOMER_DOCUMENTATION.getValue());
            toDoCriteria.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        } else {
            toDoCriteria.setTaskSubTypeId(ToDoTaskSubTypeValues.SEND_DOCUMENTS.name());
            toDoCriteria.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDoCriteria.setTaskNameId(ToDoTaskNameValues.CUSTOMER_DOCUMENTATION.getValue());
            toDoCriteria.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        }
        ToDoResponse toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        if (toDoResponse != null && toDoResponse.getData() != null && toDoResponse.getData().size() > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public void checkMDMUpdates() {
        List<MDMUnConfiguredData> mdmUnConfiguredDataList = mdmUnConfiguredDataRepository.findAllDetails();
        if (mdmUnConfiguredDataList != null && mdmUnConfiguredDataList.size() >= 1) {
            Set<String> bookIds = mdmUnConfiguredDataList.stream().map(mdmUnConfiguredData -> mdmUnConfiguredData.getBookId()).collect(Collectors.toSet());
            for (String bookId : bookIds) {
                try {
                    OpsBooking opsBooking = opsBookingService.getBooking(bookId);
                    for (MDMUnConfiguredData unConfiguredData : mdmUnConfiguredDataList.stream().filter(mdmUnConfiguredData -> mdmUnConfiguredData.getBookId().equals(bookId)).collect(Collectors.toList())) {
                        OpsProduct opsProduct = opsBooking.getProducts().stream().filter(product -> product.getOrderID().equals(unConfiguredData.getOrderId())).findFirst().get();
                        List<DocumentHandlingGrid> mdmDocumentResponse = documentMasterRequirements.getDocumentHandlingGridDetails(documentDetailsService.getDocHandlingMasterSearchCriteria(opsProduct));
                        if (mdmDocumentResponse != null && mdmDocumentResponse.size() >= 1) {
                            try {
                                List<DocumentSetting> documentSettingList = new ArrayList<>();
                                mdmDocumentResponse.stream().forEach(documentHandlingGrid -> documentSettingList.addAll(documentHandlingGrid.getDocumentSetting().stream().filter(documentSetting -> !documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()) && documentSetting.getSalesStage().equals("postbooking")).collect(Collectors.toList())));
                                if (documentSettingList != null && documentSettingList.size() >= 1) {
                                    for (DocumentSetting documentSetting : documentSettingList) {
                                        documentDetailsService.saveCutOffDateConfigurationDetails(documentSetting, opsBooking, opsProduct);
                                    }

                                    Set<String> documentSettings = new HashSet<>();
                                    documentSettings.addAll(documentSettingList.stream().filter(documentSetting -> documentSetting.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()) || documentSetting.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())).map(documentSetting -> documentSetting.getId()).collect(Collectors.toSet()));

                                    if (documentSettings != null && documentSettings.size() >= 1) {
                                        documentDetailsService.saveReceivedDocsBookingInfo(unConfiguredData.getBookId(), unConfiguredData.getOrderId(), documentSettings);
                                        if (opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.OK)) {
                                            documentDetailsService.sendAlertAndCreateTodoTaskForOpsUserToCollectDocuments(unConfiguredData.getBookId(), unConfiguredData.getOrderId(), documentDetailsService.getProductName(unConfiguredData.getBookId(), unConfiguredData.getOrderId()));
                                        }
                                    }
                                }
                                List<DocumentSetting> docSettingList = new ArrayList<>();
                                docSettingList.addAll(documentSettingList.stream().filter(documentSetting -> documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType())).collect(Collectors.toList()));
                                mdmDocumentResponse.stream().forEach(documentHandlingGrid -> docSettingList.addAll(documentHandlingGrid.getDocumentSetting().stream().filter(documentSetting -> documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()) && documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType())).collect(Collectors.toList())));
                                if (docSettingList.size() >= 1) {
                                    for (DocumentSetting documentSetting : docSettingList) {
                                        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                                            //Generate handover document if reconfirmation is done by supplier, client or both as applicable.
                                            if (documentDetailsService.checkTriggerEvent(documentSetting, opsProduct, opsBooking.getBookID())
                                                    && documentDetailsService.isReconfirmed(opsBooking, opsProduct)) {
                                                try {
                                                    generateDocumentService.generateHandOverDocument(opsBooking, documentSetting.getDocumentName(), opsProduct, documentSetting);
                                                } catch (Exception ex) {
                                                    logger.info("Failed to generate Hanover Documents for Document Name " + documentSetting.getDocumentName());
                                                }
                                            } else
                                                documentDetailsService.saveHandoverAndCustomerDocInfo(opsBooking.getBookID(), opsProduct, documentSetting);
                                        } else
                                            documentDetailsService.saveHandoverAndCustomerDocInfo(opsBooking.getBookID(), opsProduct, documentSetting);
                                    }
                                }

                                toDoTaskService.updateToDoTaskStatus(unConfiguredData.getId(), ToDoTaskSubTypeValues.CONFIGURE_DOCUMENTS_IN_MDM, ToDoTaskStatusValues.COMPLETED);
                                mdmUnConfiguredDataRepository.deleteDetails(unConfiguredData.getId());

                            } catch (Exception ex) {
                                logger.info("No document details found");
                            }
                        }
                    }
                } catch (Exception ex) {
                    logger.info("Failed to process updates from MDM");
                }

            }

        }
    }

    @Override
    public void generateHandoverDocsAndCustomerDocs() throws OperationException, IOException {
        List<HandoverAndCustomerDocInfo> docsInfo = handoverAndCustomerDocInfoRepository.getAllDetails();
        for (HandoverAndCustomerDocInfo info : docsInfo) {
            if (info.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("documentHandlingGrid.documentSetting._id", info.getDocumentSettingId());
                DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
                OpsBooking booking = opsBookingService.getBooking(info.getBookId());
                OpsProduct product = booking.getProducts().stream().filter(product1 -> product1.getOrderID().equals(info.getOrderId())).findFirst().get();
                if (!product.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.RXL) && !product.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.XL) && !product.getOrderDetails().getOpsBookingAttribute().contains(OpsBookingAttribute.RAMD)) {
                    if (documentDetailsService.checkTriggerEvent(documentSetting, product, info.getBookId())
                            && documentDetailsService.isReconfirmed(booking, product)) {
                        try {
                            generateDocumentService.generateHandOverDocument(booking, documentSetting.getDocumentName(), product, documentSetting);
                            handoverAndCustomerDocInfoRepository.deleteDetails(info.getId());
                        } catch (Exception ex) {
                            logger.info("Failed to generate Handover Documents for Document Name " + documentSetting.getDocumentName());
                        }
                    }
                }
            }
            //write logic for documents to be sent to customer docs here
        }
    }


    @Override
    public void checkReceivedDocsStatus() {
        List<ReceivedDocsBookingInfo> receivedDocsBookingInfos = receivedDocsBookingInfoRepository.getAllDetails();
        Set<String> bookIds = receivedDocsBookingInfos.stream().map(bookingInfo -> bookingInfo.getBookId()).collect(Collectors.toSet());
        for (String bookId : bookIds) {
            try {
                String result = RestUtils.getForObject(getBookingDocsUrl + bookId, String.class);
                DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);

                if (documentDetailsResponse != null) {
                    List<ReceivedDocsBookingInfo> receivedDocsBookingInfoList = new ArrayList<>();
                    List<ReceivedDocsBookingInfo> bookingInfoList = receivedDocsBookingInfos.stream().filter(bookingInfo -> bookingInfo.getBookId().equals(bookId)).collect(Collectors.toList());
                    for (ReceivedDocsBookingInfo bookingInfo : bookingInfoList) {
                        Set<String> docSettingIds = bookingInfo.getDocumentSettingIds().stream().filter(s -> updateReceivedDocsBookingInfoDetails.checkReceivedDocsStatus(bookingInfo, s, documentDetailsResponse)).collect(Collectors.toSet());
                        if (bookingInfo.getDocumentSettingIds().size() == docSettingIds.size()) {
                            receivedDocsBookingInfoList.add(bookingInfo);
                            toDoTaskService.updateToDoTaskStatus(bookingInfo.getOrderId(), ToDoTaskSubTypeValues.COLLECT_DOCUMENTS_FOR_ORDER, ToDoTaskStatusValues.COMPLETED);
                        } else {
                            if (docSettingIds != null && docSettingIds.size() >= 1)
                                updateReceivedDocsBookingInfo(bookingInfo, docSettingIds);
                        }
                    }
                    if (receivedDocsBookingInfoList.size() == bookingInfoList.size()) {
                        toDoTaskService.updateToDoTaskStatus(bookId, ToDoTaskSubTypeValues.COLLECT_DOCUMENTS_FOR_BOOKING, ToDoTaskStatusValues.COMPLETED);
                    }
                    if (receivedDocsBookingInfoList != null && receivedDocsBookingInfoList.size() >= 1)
                        deleteReceivedDocInfo(receivedDocsBookingInfoList);
                }
            } catch (Exception ex) {
                logger.info("No document details found for booking");
            }
        }

    }


    private void updateReceivedDocsBookingInfo(ReceivedDocsBookingInfo bookingInfo, Set<String> docSettingIds) {
        bookingInfo.getDocumentSettingIds().removeAll(docSettingIds);
        receivedDocsBookingInfoRepository.updateDetails(bookingInfo);
    }

    private void deleteReceivedDocInfo(List<ReceivedDocsBookingInfo> bookingInfoList) {
        for (ReceivedDocsBookingInfo bookingInfo : bookingInfoList)
            receivedDocsBookingInfoRepository.deleteDetails(bookingInfo.getId());
    }

}
