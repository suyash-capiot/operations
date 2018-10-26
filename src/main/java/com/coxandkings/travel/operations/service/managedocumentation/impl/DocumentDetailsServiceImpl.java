package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingDetailsFilter;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.criteria.booking.becriteria.ClientAndPassengerBasedFilter;
import com.coxandkings.travel.operations.criteria.managedocumentation.*;
import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.managedocumentation.*;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.reconfirmation.ClientReconfirmationStatus;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor;
import com.coxandkings.travel.operations.enums.reconfirmation.SupplierReconfirmationStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.managedocumentation.*;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.repository.managedocumentation.*;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentReferenceResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DocumentSearchResource;
import com.coxandkings.travel.operations.resource.documentLibrary.DownloadableDocumentResource;
import com.coxandkings.travel.operations.resource.documentLibrary.NewDocumentResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateAndDocumentsResource;
import com.coxandkings.travel.operations.resource.managedocumentation.*;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentHandlingGrid;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.documentLibrary.DocumentLibraryService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.managedocumentation.*;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.managedocumentation.DocumentCutOffDateCalculation;
import com.coxandkings.travel.operations.utils.managedocumentation.DocumentPageComparator;
import com.coxandkings.travel.operations.utils.managedocumentation.DocumentReceivedDateComparator;
import com.coxandkings.travel.operations.utils.managedocumentation.DocumentUtils;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DocumentDetailsServiceImpl implements DocumentDetailsService {

    @Autowired
    private DocumentLibraryService documentLibraryService;

    private static Logger logger = LogManager.getLogger(DocumentDetailsServiceImpl.class);

    @Value(value = "${manage_documentation.be.get_document_details}")
    private String getBookingDocsUrl;

    @Value(value = "${manage-documentation.get-document-handling-master}")
    private String getDocumentHandlingMasterDataUrl;

    @Value(value = "${manage_documentation.be.update_order_level_documents}")
    private String updateOrderLevelDocumentsUrl;

    @Value(value = "${manage_documentation.be.update_pax_level_documents}")
    private String updatePaxLevelDocumentsUrl;

    @Value(value = "${manage_documentation.be.update_room_level_documents}")
    private String updateRoomLevelDocumentsUrl;

    @Value(value = "${manage_documentation.be.acco_rooms_info}")
    private String accoRoomsInfo;

    @Value(value = "${manage_documentation.be.acco_rooms_pax_info}")
    private String accoRoomsPaxInfo;

    @Value(value = "${manage_documentation.be.air_pax_info}")
    private String airPaxInfo;

    @Value(value = "${documentation.fromMail}")
    private String fromMail;

    @Value(value = "${documentation.template_config.function}")
    private String templateFunction;

    @Value(value = "${documentation.template_config.scenario}")
    private String templateScenario;

    @Value(value = "${documentation.template_config.process}")
    private String templateProcess;

    @Value(value = "${documentation.template_config.subject}")
    private String emailSubject;

    @Value(value = "${documentation.dynamic_variables.first_name}")
    private String firstName;

    @Value(value = "${documentation.dynamic_variables.document_name}")
    private String documentName;

    @Value(value = "${documentation.dynamic_variables.order_id}")
    private String order_id;

    @Value(value = "${documentation.dynamic_variables.book_id}")
    private String book_ref_id;

    @Value(value = "${documentation.finance.url}")
    private String financeUrl;

    @Value(value = "${documentation.finance.total_revenue}")
    private String totalRevenue;

    @Value(value = "${documentation.finance.total_gross_profit}")
    private String totalGrossProfit;

    @Value(value = "${documentation.path_expression}")
    private String revenueOrGrossProfitValuePath;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private DocumentUtils documentUtils;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private DocumentMasterRequirements documentMasterRequirements;

    @Autowired
    private UserService userService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    @Autowired
    private SupplierReconfirmationService supplierReconfirmationService;

    @Autowired
    private UpdateDocumentDetails updateDocumentDetails;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GenerateDocumentService generateDocumentService;

    @Autowired
    private CutOffDateConfigurationRepository cutOffDateConfigurationRepository;

    @Autowired
    private MDMUnConfiguredDataRepository mdmUnConfiguredDataRepository;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private DocumentMDMService documentMDMService;

    @Autowired
    private HandoverDocGenApprovalRepository handoverDocGenApprovalRepository;

    @Autowired
    private ReceivedDocsBookingInfoRepository receivedDocsBookingInfoRepository;

    @Autowired
    private UpdateReceivedDocsBookingInfoDetails updateReceivedDocsBookingInfoDetails;

    @Autowired
    private HandoverAndCustomerDocInfoRepository handoverAndCustomerDocInfoRepository;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<DocumentRowItem> getDocumentsForBooking(String bookingRefNo) throws IOException, ParseException, OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefNo);
        String result = RestUtils.getForObject(getBookingDocsUrl + bookingRefNo, String.class);
        DocumentDetailsResponse documentDetailsResponse = null;
        try {
            documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);
        } catch (Exception ex) {
            logger.info("No document details found for booking");
        }
        if (documentDetailsResponse != null) {
            List<DocumentRowItem> bookingDocuments = new ArrayList<>();
            for (OpsProduct product : opsBooking.getProducts()) {
                DocumentRowItem documentRowItem = null;
                List<DocumentHandlingGrid> mdmDocumentResponse = documentMasterRequirements.getDocumentHandlingGridDetails(getDocHandlingMasterSearchCriteria(product));
                List<DocumentSetting> documentSettingList = new ArrayList<>();
                mdmDocumentResponse.stream().forEach(documentHandlingGrid -> documentSettingList.addAll(documentHandlingGrid.getDocumentSetting().stream().filter(documentSetting -> documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()) || (!documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()) && documentSetting.getSalesStage().equals("postbooking"))).collect(Collectors.toList())));
                if (documentSettingList != null && documentSettingList.size() >= 1) {
                    for (DocumentSetting documentSetting : documentSettingList) {
                        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE.getEntityReference())) {
                            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                                OrderDocuments orderLevelDocuments = null;
                                try {
                                    orderLevelDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDocuments -> orderDocuments.getOrderId().equals(product.getOrderID()) && orderDocuments.getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive())).findFirst().get();
                                } catch (Exception ex) {
                                    logger.info("No details found for Order Documents");
                                }
                                if (orderLevelDocuments != null && orderLevelDocuments.getDocumentInfo() != null && orderLevelDocuments.getDocumentInfo().size() >= 1) {
                                    documentRowItem = renderOrderLevelDocumentsForBEDocs(orderLevelDocuments, documentSetting, product, opsBooking.getBookingDateZDT());
                                } else {
                                    documentRowItem = renderOrderLevelDocuments(documentSetting, product, opsBooking.getBookingDateZDT());
                                    documentRowItem.setBookId(bookingRefNo);
                                }
                            } else {
                                documentRowItem = renderOrderLevelDocuments(documentSetting, product, opsBooking.getBookingDateZDT());
                                documentRowItem.setBookId(bookingRefNo);
                            }

                        } else if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference())) {
                            List<String> paxIds;
                            if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE.getEntityReference()))
                                paxIds = getListOfPaxDetails(product, true);
                            else
                                paxIds = getListOfPaxDetails(product, false);
                            if (paxIds != null && paxIds.size() >= 1) {
                                if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                                    List<PaxDocuments> paxLevelDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDocuments -> paxIds.contains(paxDocuments.getPaxID()) && paxDocuments.getDocumentInfo().getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(product.getOrderID()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive())).collect(Collectors.toList());
                                    if (paxLevelDocuments != null && paxLevelDocuments.size() >= 1) {
                                        documentRowItem = renderPaxLevelDocumentsForBeDocs(paxLevelDocuments, documentSetting, product, paxIds);
                                    } else {
                                        documentRowItem = renderPaxLevelDocumentsForOrder(documentSetting, product, paxIds, opsBooking.getBookingDateZDT());
                                        documentRowItem.setBookId(bookingRefNo);
                                    }
                                } else {
                                    documentRowItem = renderPaxLevelDocumentsForOrder(documentSetting, product, paxIds, opsBooking.getBookingDateZDT());
                                    documentRowItem.setBookId(bookingRefNo);
                                }
                            }

                        } else if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE.getEntityReference())) {
                            List<String> roomIds = getListOfRoomDetails(product);
                            if (roomIds != null && roomIds.size() >= 1) {
                                if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                                    List<RoomDocuments> roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomIds.contains(roomDoc.getRoomID()) && roomDoc.getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(product.getOrderID()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive())).collect(Collectors.toList());
                                    if (roomDocuments != null && roomDocuments.size() >= 1) {
                                        documentRowItem = renderRoomLevelDocumentsForBEDocs(roomDocuments, documentSetting, product, roomIds);
                                    } else {
                                        documentRowItem = renderRoomLevelDocumentsForOrder(documentSetting, product, roomIds, opsBooking.getBookingDateZDT());
                                        documentRowItem.setBookId(bookingRefNo);
                                    }
                                } else {
                                    documentRowItem = renderRoomLevelDocumentsForOrder(documentSetting, product, roomIds, opsBooking.getBookingDateZDT());
                                    documentRowItem.setBookId(bookingRefNo);
                                }
                            }
                        }
                        if (documentRowItem != null) {
                            if (product.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.RXL) || product.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.XL) || product.getOrderDetails().getOpsBookingAttribute().contains(OpsBookingAttribute.RAMD))
                                documentRowItem.setDisableHandoverDocActions(true);
                            bookingDocuments.add(documentRowItem);
                        }
                    }

                } else {
                    //create to-do task and send an alert to admin to configure MDM
                    saveMDMUnConfiguredDataDetails(product, opsBooking);
                }


            }
            if (bookingDocuments != null && bookingDocuments.size() >= 1)
                return bookingDocuments;
            else throw new OperationException(Constants.ER01);
        } else throw new OperationException(Constants.ER01);
    }

    public DocumentRowItem renderOrderLevelDocumentsForBEDocs(OrderDocuments orderLevelDocuments, DocumentSetting documentSetting, OpsProduct product, ZonedDateTime bookingDate) throws JsonProcessingException {
        List<BookingDocumentDetailsResource> bookingDetailsResource = orderLevelDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null).collect(Collectors.toList());
        if (bookingDetailsResource != null && bookingDetailsResource.size() >= 1) {
            DocumentRowItem documentRowItem = getDocumentRowItemForBEDocs(bookingDetailsResource, documentSetting, product);
            List<Document> documents = new ArrayList<>();
            Document document = documentUtils.getPaxInfo(product);
            document.getCommunicationIds().addAll(bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCommunicationIds() != null && bookingDocumentDetailsResource.getCommunicationIds().size() >= 1).flatMap(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCommunicationIds().stream()).collect(Collectors.toSet()));
            documents.add(getDocumentForBEDocs(document, bookingDetailsResource, documentSetting));
            documentRowItem.setDocuments(documents);
            return documentRowItem;
        } else {
            return renderOrderLevelDocuments(documentSetting, product, bookingDate);
        }
    }

    public DocumentRowItem renderOrderLevelDocuments(DocumentSetting documentSetting, OpsProduct product, ZonedDateTime bookingDate) throws JsonProcessingException {
        DocumentRowItem documentRowItem = getDocumentRowItem(documentSetting, product, bookingDate);
        List<Document> documents = new ArrayList<>();
        Document document = documentUtils.getPaxInfo(product);
        documents.add(getDocument(document, documentSetting, product));
        documentRowItem.setDocuments(documents);
        return documentRowItem;
    }

    public DocumentRowItem renderPaxLevelDocumentsForBeDocs(List<PaxDocuments> paxLevelDocuments, DocumentSetting documentSetting, OpsProduct product, List<String> paxIds) {

        DocumentRowItem documentRowItem = null;
        List<Document> documents = new ArrayList<>();
        for (String paxId : paxIds) {
            PaxDocuments paxDocumentDetails = null;
            try {
                paxDocumentDetails = paxLevelDocuments.stream().filter(paxDocuments -> paxDocuments.getPaxID().equals(paxId)).findFirst().get();
            } catch (Exception ex) {
                logger.info("No details found for Pax Documents");
            }
            if (paxDocumentDetails != null && paxDocumentDetails.getDocumentInfo() != null && paxDocumentDetails.getDocumentInfo().getDocumentInfo().size() >= 1) {
                List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocumentDetails.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getOrderId().equals(product.getOrderID()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null).collect(Collectors.toList());
                if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                    if (documentRowItem == null) {
                        documentRowItem = getDocumentRowItemForBEDocs(bookingDocumentDetailsResources, documentSetting, product);
                    }

                    if (documentRowItem != null && documentRowItem.getDocumentID() == null) {
                        if (documentRowItem.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS)) {
                            List<String> docIds = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())).map(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentID()).collect(Collectors.toList());
                            if (docIds != null && docIds.size() >= 1)
                                documentRowItem.setDocumentID(docIds.iterator().next());
                        } else
                            documentRowItem.setDocumentID("NA");
                    }

                    Document document = new Document();
                    document.setPassengerName(getPaxName(product, paxId));
                    document.setPaxId(paxId);
                    document.getCommunicationIds().addAll(bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCommunicationIds() != null && bookingDocumentDetailsResource.getCommunicationIds().size() >= 1).flatMap(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCommunicationIds().stream()).collect(Collectors.toSet()));
                    documents.add(getDocumentForBEDocs(document, bookingDocumentDetailsResources, documentSetting));
                } else {
                    documents.add(renderPaxLevelDocuments(documentSetting, product, paxId));
                }
            } else {
                documents.add(renderPaxLevelDocuments(documentSetting, product, paxId));
            }
        }
        documentRowItem.setDocuments(documents);
        return documentRowItem;

    }

    public DocumentRowItem renderPaxLevelDocumentsForOrder(DocumentSetting documentSetting, OpsProduct product, List<String> paxIds, ZonedDateTime bookingDate) {
        DocumentRowItem documentRowItem = getDocumentRowItem(documentSetting, product, bookingDate);

        List<Document> documents = new ArrayList<>();
        for (String paxId : paxIds) {
            Document document = new Document();
            document.setPaxId(paxId);
            document.setPassengerName(getPaxName(product, paxId));
            documents.add(getDocument(document, documentSetting, product));
        }
        documentRowItem.setDocuments(documents);
        return documentRowItem;
    }

    public Document renderPaxLevelDocuments(DocumentSetting documentSetting, OpsProduct product, String paxId) {
        Document document = new Document();
        document.setPaxId(paxId);
        document.setPassengerName(getPaxName(product, paxId));
        return getDocument(document, documentSetting, product);
    }

    public DocumentRowItem renderRoomLevelDocumentsForBEDocs(List<RoomDocuments> roomLevelDocuments, DocumentSetting documentSetting, OpsProduct product, List<String> roomIds) throws JsonProcessingException {

        DocumentRowItem documentRowItem = null;
        List<Document> documents = new ArrayList<>();
        Document document = null;
        List<RoomWiseDocuments> roomWiseDocuments = new ArrayList<>();
        List<BookingDocumentDetailsResource> bookingDocumentDetailsResourceList = new ArrayList<>();
        for (String roomId : roomIds) {
            RoomDocuments roomDocuments = null;
            try {
                roomDocuments = roomLevelDocuments.stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
            } catch (Exception ex) {
                logger.info("No details found for Room Documents");
            }

            if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(product.getOrderID()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null).collect(Collectors.toList());
                if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                    bookingDocumentDetailsResourceList.addAll(bookingDocumentDetailsResources);
                    BookingDocumentDetailsResource documentDetailsResource = bookingDocumentDetailsResources.iterator().next();
                    if (documentRowItem == null) {
                        documentRowItem = getDocumentRowItemForBEDocs(bookingDocumentDetailsResources, documentSetting, product);
                    }

                    if (documentRowItem != null && documentRowItem.getDocumentID() == null) {
                        if (documentRowItem.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS)) {
                            List<String> docIds = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())).map(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentID()).collect(Collectors.toList());
                            if (docIds != null && docIds.size() >= 1)
                                documentRowItem.setDocumentID(docIds.iterator().next());
                        } else
                            documentRowItem.setDocumentID("NA");
                    }

                    if (document == null) {
                        document = new Document();
                        document.setPassengerName(documentDetailsResource.getPaxName());
                        document.setPaxId(documentDetailsResource.getPaxId());
                        document.setVisaAndPassportDetailsRequired(documentDetailsResource.getVisaAndPassportDetailsRequired());
                        document.setRemarks(documentDetailsResource.getRemarks());

                        if (bookingDocumentDetailsResources.stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
                            document.setCanUpload(false);
                        else
                            document.setCanUpload(true);

                        if (documentDetailsResource.getPassport() != null) {
                            CopyUtils.copy(documentDetailsResource.getPassport(), document);
                        }
                        if (documentDetailsResource.getVisa() != null) {
                            CopyUtils.copy(documentDetailsResource.getVisa(), document);
                        }

                        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                            document.setCanCopy(true);
                            document.setCanEdit(true);
                        } else {
                            document.setCanEdit(false);
                            document.setCanCopy(false);
                        }

                        if (documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType()) || documentSetting.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_CUSTOMER.getCommunicationType())) {
                            document.setDocumentReceivedDate("NA");
                        } else {
                            List<BookingDocumentDetailsResource> receivedDocs = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentReceivedDate() != null && !bookingDocumentDetailsResource.getDocumentReceivedDate().equals("NA") && bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())).collect(Collectors.toList());
                            document.setDocumentReceivedDate(getLatestDocumentReceivedDate(receivedDocs));
                        }
                    }

                    RoomWiseDocuments roomWiseDocument = new RoomWiseDocuments();
                    roomWiseDocument.setRoomId(roomId);

                    List<BookingDocumentDetailsResource> uploadedDocuments = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue()) && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                    List<DocumentVersion> documentVersions = getDocumentVersions(uploadedDocuments);
                    roomWiseDocument.setDocumentVersions(documentVersions);
                    roomWiseDocuments.add(roomWiseDocument);
                }
            }
        }
        document.getRoomIds().addAll(getRoomDetails(product, roomIds));
        document.setRoomWiseDocuments(roomWiseDocuments);

        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            document = getDocumentVersionDetailsForHandoverDocuments(bookingDocumentDetailsResourceList, document);
            document = getGeneratedDocuments(bookingDocumentDetailsResourceList, document);
        } else {
            document = getDocumentVersionDetailsForCustomerDocumentation(bookingDocumentDetailsResourceList, document, documentSetting.getDocumentBy());
        }

        document.setApprovalRequestSent(checkApprovalRequest(document, documentSetting, product));

        document.getCommunicationIds().addAll(bookingDocumentDetailsResourceList.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCommunicationIds() != null && bookingDocumentDetailsResource.getCommunicationIds().size() >= 1).flatMap(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCommunicationIds().stream()).collect(Collectors.toSet()));

        List<BookingDocumentDetailsResource> detailsResourceList = bookingDocumentDetailsResourceList.stream().filter(detailsResource -> detailsResource.getCopyTo() == null || (detailsResource.getCopyTo() != null && detailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy()))).collect(Collectors.toList());
        document.setDocumentStatus(getDocumentStatus(documentSetting, detailsResourceList));

        documents.add(document);
        documentRowItem.setDocuments(documents);
        return documentRowItem;

    }

    public DocumentRowItem renderRoomLevelDocumentsForOrder(DocumentSetting documentSetting, OpsProduct product, List<String> roomIds, ZonedDateTime bookingDate) throws JsonProcessingException {
        List<Document> documents = new ArrayList<>();
        DocumentRowItem documentRowItem = getDocumentRowItem(documentSetting, product, bookingDate);
        Document document = documentUtils.getPaxInfo(product);
        document.getRoomIds().addAll(getRoomDetails(product, roomIds));
        documents.add(getDocument(document, documentSetting, product));
        documentRowItem.setDocuments(documents);
        return documentRowItem;
    }

    private DocumentRowItem getDocumentRowItemForBEDocs(List<BookingDocumentDetailsResource> bookingDetailsResource, DocumentSetting documentSetting, OpsProduct product) {
        DocumentRowItem documentRowItem = new DocumentRowItem();
        BookingDocumentDetailsResource detailsResource = bookingDetailsResource.iterator().next();
        documentRowItem.setBookId(detailsResource.getBookingReferenceNo());
        documentRowItem.setOrderId(detailsResource.getOrderId());
        documentRowItem.setProductStatus(product.getOrderDetails().getOpsOrderStatus().getProductStatus());
        documentRowItem.setCutOffDueDate(detailsResource.getCutOffDate());
        documentRowItem.setDocumentFormat(detailsResource.getDocumentFormat());

        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            List<String> docIds = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())).map(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentID()).collect(Collectors.toList());
            if (docIds != null && docIds.size() >= 1)
                documentRowItem.setDocumentID(docIds.iterator().next());
        } else documentRowItem.setDocumentID("NA");

        documentRowItem.setDocumentName(detailsResource.getDocumentName());
        documentRowItem.setDocumentWise(DocumentEntityReference.fromString(detailsResource.getDocumentAsPer()));
        documentRowItem.setDocWise(detailsResource.getDocumentAsPer());
        documentRowItem.setOrderLevel(detailsResource.getOrderLevel());
        documentRowItem.setProductName(detailsResource.getProductName());
        documentRowItem.setDocumentType(DocumentType.fromString(detailsResource.getDocumentType()));
        documentRowItem.setDocumentSettingId(detailsResource.getDocumentSettingId());
        return getCommTypeAndMode(documentRowItem, documentSetting);
    }

    private Document getDocumentForBEDocs(Document document, List<BookingDocumentDetailsResource> bookingDetailsResource, DocumentSetting documentSetting) {
        BookingDocumentDetailsResource detailsResource = bookingDetailsResource.iterator().next();

        if (documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType()) || documentSetting.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_CUSTOMER.getCommunicationType())) {
            document.setDocumentReceivedDate("NA");
        } else {
            List<BookingDocumentDetailsResource> receivedDocs = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentReceivedDate() != null && !bookingDocumentDetailsResource.getDocumentReceivedDate().equals("NA") && bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())).collect(Collectors.toList());
            document.setDocumentReceivedDate(getLatestDocumentReceivedDate(receivedDocs));
        }

        document.setRemarks(detailsResource.getRemarks());
        document.setVisaAndPassportDetailsRequired(detailsResource.getVisaAndPassportDetailsRequired());
        List<BookingDocumentDetailsResource> detailsResourceList = bookingDetailsResource.stream().filter(documentDetailsResource -> documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy()))).collect(Collectors.toList());
        document.setDocumentStatus(getDocumentStatus(documentSetting, detailsResourceList));

        if (detailsResource.getPassport() != null) {
            CopyUtils.copy(detailsResource.getPassport(), document);
        }
        if (detailsResource.getVisa() != null) {
            CopyUtils.copy(detailsResource.getVisa(), document);
        }

        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            document = getUploadedDocsForHandoverDocuments(bookingDetailsResource, document);
            document = getDocumentVersionDetailsForHandoverDocuments(bookingDetailsResource, document);
            document.setCanCopy(true);
            document.setCanEdit(true);
        } else {
            document = getDocumentVersionDetailsForCustomerDocumentation(bookingDetailsResource, document, documentSetting.getDocumentBy());
            document = getUploadedDocsForCustomerDocumentation(bookingDetailsResource, document);
            document.setCanEdit(false);
            document.setCanCopy(false);
        }

        if (document.getGeneratedDocuments() == null || document.getGeneratedDocuments().size() == 0) {
            HandoverDocGenSearchCriteria searchCriteria = new HandoverDocGenSearchCriteria();
            searchCriteria.setDocumentSettingId(documentSetting.getId());
            searchCriteria.setBookID(detailsResource.getBookingReferenceNo());
            searchCriteria.setOrderId(detailsResource.getOrderId());
            searchCriteria.setPaxId(detailsResource.getPaxId());
            searchCriteria.setRoomId(detailsResource.getRoomId());
            List<HandoverDocGenApprovalDetails> handoverDocGenApprovalDetails = handoverDocGenApprovalRepository.searchDetails(searchCriteria);
            if (handoverDocGenApprovalDetails != null && handoverDocGenApprovalDetails.size() >= 1) {
                document.setApprovalRequestSent(true);
            }
        }

        if (bookingDetailsResource.stream().anyMatch(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
            document.setCanUpload(false);
        else
            document.setCanUpload(true);

        if (detailsResource.getVisaAndPassportDetailsRequired() != null && detailsResource.getVisaAndPassportDetailsRequired()) {
            if (bookingDetailsResource.stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.isVisaDocsUploaded() && bookingDocumentDetailsResource.getVisa() != null))
                document.setVisaDetailsEntered(true);
            if (bookingDetailsResource.stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.isPassportDocsUploaded() && bookingDocumentDetailsResource.getPassport() != null))
                document.setPassportDetailsEntered(true);
        }

        return document;
    }


    private DocumentRowItem getDocumentRowItem(DocumentSetting documentSetting, OpsProduct product, ZonedDateTime bookingDate) {
        DocumentRowItem documentRowItem = new DocumentRowItem();
        documentRowItem.setOrderId(product.getOrderID());
        documentRowItem.setProductStatus(product.getOrderDetails().getOpsOrderStatus().getProductStatus());
        documentRowItem.setDocumentWise(DocumentEntityReference.fromString(documentSetting.getDocumentsAsPer()));
        documentRowItem.setDocWise(documentSetting.getDocumentsAsPer());
        documentRowItem.setDocumentName(documentSetting.getDocumentName());
        documentRowItem.setDocumentFormat(DocumentFormat.fromString(documentSetting.getDocumentFormat()).getDocFormat());
        documentRowItem.setDocumentSettingId(documentSetting.getId());
        documentRowItem.setDocumentType(DocumentType.fromString(documentSetting.getDocumentType()));

        if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS))
            documentRowItem.setProductName(product.getOrderDetails().getHotelDetails().getHotelName());
        else if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT))
            documentRowItem.setProductName(product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOperatingAirline().getAirlineCode());

        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference()))
            documentRowItem.setOrderLevel(false);
        else documentRowItem.setOrderLevel(true);

        if (!documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            documentRowItem.setDocumentID("NA");
            documentRowItem.setCutOffDueDate(DocumentCutOffDateCalculation.calculateCutOffDate(product, documentSetting, bookingDate));
        } else documentRowItem.setCutOffDueDate("NA");

        return getCommTypeAndMode(documentRowItem, documentSetting);
    }

    private Document getDocument(Document document, DocumentSetting documentSetting, OpsProduct product) {

        if (documentSetting.getDocumentBy().equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType()) || documentSetting.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_CUSTOMER.getCommunicationType())) {
            document.setDocumentReceivedDate("NA");
        }

        if (product.getProductSubCategory().equals("Visa"))
            document.setVisaAndPassportDetailsRequired(true);
        else document.setVisaAndPassportDetailsRequired(false);

        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            document.setCanCopy(true);
            document.setCanEdit(true);
        } else {
            document.setCanEdit(false);
            document.setCanCopy(false);
        }

        document.setCanUpload(true);
        document.setDocumentStatus(getDocumentStatus(documentSetting, null));

        document.setApprovalRequestSent(checkApprovalRequest(document, documentSetting, product));

        return document;
    }

    private DocumentRowItem getCommTypeAndMode(DocumentRowItem documentRowItem, DocumentSetting documentSetting) {

        ModeOfCommunication modeOfCommunication = new ModeOfCommunication();
        for (String commMode : documentSetting.getModeOfComm()) {
            if (DocumentCommunicationMode.fromString(commMode).equals(DocumentCommunicationMode.COMMUNICATION_MODE_COURIER)) {
                modeOfCommunication.setCommModeCourier(true);
                modeOfCommunication.setCourier(DocumentCommunicationMode.fromString(commMode).getDisplayValue());
            } else if (DocumentCommunicationMode.fromString(commMode).equals(DocumentCommunicationMode.COMMUNICATION_MODE_EMAIL)) {
                modeOfCommunication.setCommModeEmail(true);
                modeOfCommunication.setEmail(DocumentCommunicationMode.fromString(commMode).getDisplayValue());
            } else if (DocumentCommunicationMode.fromString(commMode).equals(DocumentCommunicationMode.COMMUNICATION_MODE_SMS)) {
                modeOfCommunication.setCommModeSMS(true);
                modeOfCommunication.setSms(DocumentCommunicationMode.fromString(commMode).getDisplayValue());
            }
        }
        documentRowItem.setModeOfCommunication(modeOfCommunication);

        CommunicationType communicationType = new CommunicationType();
        if (DocumentCommunicationType.fromString(documentSetting.getDocumentBy()).equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY) || DocumentCommunicationType.fromString(documentSetting.getDocumentBy()).equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY)) {
            communicationType.setReceiveDocCommType(DocumentCommunicationType.fromString(documentSetting.getDocumentBy()).getDisplayValue());
            if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()))
                communicationType.setReceiveDocument(false);
            else
                communicationType.setReceiveDocument(true);
        } else {
            communicationType.setSendDocCommType(DocumentCommunicationType.fromString(documentSetting.getDocumentBy()).getDisplayValue());
            if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()))
                communicationType.setSendDocument(false);
            else
                communicationType.setSendDocument(true);
        }
        documentRowItem.setCommunicationType(communicationType);
        return documentRowItem;
    }

    @Override
    public List<DocumentRowItem> uploadDocument(DocumentsResource documentsResource) throws IOException, OperationException, RepositoryException, ParseException {

        OpsBooking opsBooking = opsBookingService.getBooking(documentsResource.getBookID());
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        BookingDocumentDetailsResource documentDetails = new BookingDocumentDetailsResource();

        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {

            PaxDocuments paxDocuments = null;
            try {
                paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
            } catch (Exception ex) {
                logger.info("No details found for Pax Documents");
            }
            if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                List<BookingDocumentDetailsResource> bookingDocumentDetails = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                if (bookingDocumentDetails != null && bookingDocumentDetails.size() >= 1) {
                    Collections.sort(bookingDocumentDetails, new DocumentPageComparator());
                    CopyUtils.copy(bookingDocumentDetails.iterator().next(), documentDetails);
                    paxDocuments.getDocumentInfo().getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());

                } else {
                    paxDocuments.getDocumentInfo().getDocumentInfo().add(createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT()));
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                }
            } else {
                List<BookingDocumentDetailsResource> documentDetailsResourceList = new ArrayList<>();
                documentDetailsResourceList.add(createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT()));
                updatePaxLevelDocuments(documentsResource, documentDetailsResourceList);
            }
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {

            RoomDocuments roomDocuments = null;
            try {
                roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
            } catch (Exception ex) {
                logger.info("No details found for Room Documents");
            }

            if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                List<BookingDocumentDetailsResource> bookingDocumentDetails = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                if (bookingDocumentDetails != null && bookingDocumentDetails.size() >= 1) {
                    Collections.sort(bookingDocumentDetails, new DocumentPageComparator());
                    CopyUtils.copy(bookingDocumentDetails.iterator().next(), documentDetails);
                    roomDocuments.getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
                    updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());

                } else {
                    roomDocuments.getDocumentInfo().add(createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT()));
                    updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                }
            } else {
                List<BookingDocumentDetailsResource> bookingDocumentDetailsResourceList = new ArrayList<>();
                bookingDocumentDetailsResourceList.add(createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT()));
                updateRoomLevelDocuments(documentsResource, bookingDocumentDetailsResourceList);
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {

            OrderDocuments orderDocuments = null;
            try {
                orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
            } catch (Exception ex) {
                logger.info("No details found for Order Documents");
            }

            if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                List<BookingDocumentDetailsResource> bookingDocumentDetails = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                if (bookingDocumentDetails != null && bookingDocumentDetails.size() >= 1) {
                    Collections.sort(bookingDocumentDetails, new DocumentPageComparator());
                    CopyUtils.copy(bookingDocumentDetails.iterator().next(), documentDetails);
                    orderDocuments.getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
                    updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                } else {
                    orderDocuments.getDocumentInfo().add(createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT()));
                    updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                }
            } else {
                List<BookingDocumentDetailsResource> bookingDocumentDetailsResourceList = new ArrayList<>();
                bookingDocumentDetailsResourceList.add(createBookingDocumentDetailsResource(documentsResource, opsBooking.getBookingDateZDT()));
                updateOrderLevelDocuments(documentsResource, bookingDocumentDetailsResourceList);
            }
        }

        return getDocumentsForBooking(documentsResource.getBookID());
    }

    public BookingDocumentDetailsResource createBookingDocumentDetailsResource(DocumentsResource documentsResource, ZonedDateTime bookingDate) throws OperationException {

        BookingDocumentDetailsResource bookingDocumentDetailsResource = new BookingDocumentDetailsResource();

        if (documentsResource.getPassport() != null) {
            Boolean isPassportValid = validatePassportDetails(documentsResource.getPassport(), getTravelEndDate(documentsResource.getBookID()));
            if (!isPassportValid) {
                throw new OperationException("Passport details are not valid for the travel date");
            } else {
                PassportResource passportResource = new PassportResource();
                CopyUtils.copy(documentsResource.getPassport(), passportResource);
                bookingDocumentDetailsResource.setPassport(passportResource);
            }
        }

        if (documentsResource.getVisa() != null) {
            Boolean isVisaValid = validateVisaDetails(documentsResource.getVisa(), getTravelEndDate(documentsResource.getBookID()));
            if (!isVisaValid) {
                throw new OperationException("Visa details are not valid for the travel date");
            } else {
                VisaDocumentResource visaResource = new VisaDocumentResource();
                CopyUtils.copy(documentsResource.getVisa(), visaResource);
                bookingDocumentDetailsResource.setVisa(visaResource);
            }
        }

        if (documentsResource.getVisaDocument() != null && documentsResource.getVisaDocument())
            bookingDocumentDetailsResource.setVisaDocsUploaded(true);
        if (documentsResource.getPassportDocument() != null && documentsResource.getPassportDocument())
            bookingDocumentDetailsResource.setPassportDocsUploaded(true);

        OpsProduct opsProduct = opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("documentHandlingGrid.documentSetting._id", documentsResource.getDocumentSettingId());
        DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
        bookingDocumentDetailsResource.setBookingReferenceNo(documentsResource.getBookID());

        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()))
            bookingDocumentDetailsResource.setCutOffDate("NA");
        else
            bookingDocumentDetailsResource.setCutOffDate(DocumentCutOffDateCalculation.calculateCutOffDate(opsProduct, documentSetting, bookingDate));

        bookingDocumentDetailsResource.setDocumentAsPer(documentSetting.getDocumentsAsPer());
        bookingDocumentDetailsResource.setDocumentBy(documentSetting.getDocumentBy());
        bookingDocumentDetailsResource.setDocumentFormat(documentSetting.getDocumentFormat());
        bookingDocumentDetailsResource.setDocumentName(documentSetting.getDocumentName());
        bookingDocumentDetailsResource.setDocumentSettingId(documentSetting.getId());
        bookingDocumentDetailsResource.setDocumentType(documentSetting.getDocumentType());
        bookingDocumentDetailsResource.setOrderId(documentsResource.getOrderId());
        bookingDocumentDetailsResource.setModeOfCommunication(documentSetting.getModeOfComm());
        if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS))
            bookingDocumentDetailsResource.setProductName(opsProduct.getOrderDetails().getHotelDetails().getHotelName());
        else if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT))
            bookingDocumentDetailsResource.setProductName(opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOperatingAirline().getAirlineCode());

        if (opsProduct.getProductSubCategory().equals("Visa"))
            bookingDocumentDetailsResource.setVisaAndPassportDetailsRequired(true);
        else bookingDocumentDetailsResource.setVisaAndPassportDetailsRequired(false);


        if (documentsResource.getPaxId() == null) {
            Document document = documentUtils.getPaxInfo(opsProduct);
            bookingDocumentDetailsResource.setPaxId(document.getPaxId());
            bookingDocumentDetailsResource.setPaxName(document.getPassengerName());
        } else {
            bookingDocumentDetailsResource.setPaxId(documentsResource.getPaxId());
            bookingDocumentDetailsResource.setPaxName(getPaxName(opsProduct, documentsResource.getPaxId()));
        }

        bookingDocumentDetailsResource.setRoomId(documentsResource.getRoomId());
        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference()))
            bookingDocumentDetailsResource.setOrderLevel(false);
        else
            bookingDocumentDetailsResource.setOrderLevel(true);

        return uploadDoc(documentsResource, bookingDocumentDetailsResource);

    }

    @Override
    public BookingDocumentDetailsResource uploadDoc(DocumentsResource documentsResource, BookingDocumentDetailsResource documentDetails) {

        documentDetails.setActive(true);
        documentDetails.setDocumentID(documentsResource.getDocumentId());
        documentDetails.setDocumentFileName(documentsResource.getFileName());
        documentDetails.setDocumentExtension(documentsResource.getExtension());

        if (documentDetails.getDocumentPageNumber() == null) {
            documentDetails.setDocumentPageNumber(String.valueOf(1));
        } else {
            documentDetails.setDocumentPageNumber(String.valueOf(Integer.parseInt(documentDetails.getDocumentPageNumber()) + 1));
        }

        if (documentDetails.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            if (documentDetails.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()) || documentDetails.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType()))
                documentDetails.setDocumentReceivedDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            else documentDetails.setDocumentReceivedDate("NA");

            if (documentsResource.getKafkaGenerated() != null && documentsResource.getKafkaGenerated()) {
                documentDetails.setDocumentStatus(DocumentStatus.GENERATED.getValue());
                documentDetails.setReceivedStatus(DocumentStatus.GENERATED.getValue());
            } else {
                documentDetails.setDocumentStatus(DocumentStatus.RECEIVED.getValue());
                documentDetails.setReceivedStatus(DocumentStatus.RECEIVED.getValue());
            }
        } else {
            if (documentsResource.getKafkaGenerated() != null && documentsResource.getKafkaGenerated()) {
                documentDetails.setDocumentStatus(DocumentStatus.GENERATED.getValue());
                documentDetails.setReceivedStatus(DocumentStatus.GENERATED.getValue());
            } else {
                documentDetails.setReceivedStatus(DocumentStatus.RECEIVED.getValue());
                if (documentsResource.getVerified() != null && documentsResource.getVerified())
                    documentDetails.setDocumentStatus(DocumentStatus.VERIFIED.getValue());
                else
                    documentDetails.setDocumentStatus(DocumentStatus.RECEIVED.getValue());
            }

            if (documentDetails.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()) || documentDetails.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())) {
                documentDetails.setDocumentReceivedDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            } else {
                documentDetails.setDocumentReceivedDate("NA");
            }
        }

        return documentDetails;
    }

    @Override
    public List<DocumentRowItem> editDocument(DocumentsResource documentsResource) throws OperationException, IOException, SQLException, ParseException {
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        BookingDocumentDetailsResource documentDetails = new BookingDocumentDetailsResource();
        OpsBooking booking = opsBookingService.getBooking(documentsResource.getBookID());
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {

            PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive()).collect(Collectors.toList());
            paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
            BookingDocumentDetailsResource bookingDocumentDetails = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).findFirst().get();
            CopyUtils.copy(bookingDocumentDetails, documentDetails);
            Collections.sort(bookingDocumentDetailsResources, new DocumentPageComparator());
            documentDetails.setDocumentPageNumber(bookingDocumentDetailsResources.get(0).getDocumentPageNumber());
            paxDocuments.getDocumentInfo().getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
            //updating details
            updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());

            if (bookingDocumentDetails.getSendStatus() != null && bookingDocumentDetails.getSendStatus().equals(DocumentStatus.SENT.getValue())) {
                documentsResource.setDocumentName(bookingDocumentDetails.getDocumentName());
                documentsResource.setCommunicationId(sendEditedDocsToCustomer(documentsResource, booking));
                documentsResource.setDocumentIds(Arrays.asList(documentsResource.getDocumentId()));
                documentsResource.setCommunicationTemplate(CommunicationTemplate.COMM_SEND_HNDOVR_DOCS_TO_CUST);
                return setCommDetailsForDocuments(documentsResource);
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {

            RoomDocuments roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive()).collect(Collectors.toList());
            roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
            BookingDocumentDetailsResource bookingDocumentDetails = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).findFirst().get();
            CopyUtils.copy(bookingDocumentDetails, documentDetails);
            Collections.sort(bookingDocumentDetailsResources, new DocumentPageComparator());
            documentDetails.setDocumentPageNumber(bookingDocumentDetailsResources.iterator().next().getDocumentPageNumber());
            roomDocuments.getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
            //updating details
            updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());

            if (bookingDocumentDetails.getSendStatus() != null && bookingDocumentDetails.getSendStatus().equals(DocumentStatus.SENT.getValue())) {
                documentsResource.setDocumentName(bookingDocumentDetails.getDocumentName());
                documentsResource.setPaxId(bookingDocumentDetails.getPaxId());
                documentsResource.setCommunicationId(sendEditedDocsToCustomer(documentsResource, booking));
                documentsResource.setDocumentIds(Arrays.asList(documentsResource.getDocumentId()));
                documentsResource.setCommunicationTemplate(CommunicationTemplate.COMM_SEND_HNDOVR_DOCS_TO_CUST);
                return setCommDetailsForDocuments(documentsResource);
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {

            OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive()).collect(Collectors.toList());
            orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
            BookingDocumentDetailsResource bookingDocumentDetails = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).findFirst().get();
            CopyUtils.copy(bookingDocumentDetails, documentDetails);
            Collections.sort(bookingDocumentDetailsResources, new DocumentPageComparator());
            documentDetails.setDocumentPageNumber(bookingDocumentDetailsResources.get(0).getDocumentPageNumber());
            orderDocuments.getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
            //updating details
            updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());

            if (bookingDocumentDetails.getSendStatus() != null && bookingDocumentDetails.getSendStatus().equals(DocumentStatus.SENT.getValue())) {
                documentsResource.setDocumentName(bookingDocumentDetails.getDocumentName());
                documentsResource.setPaxId(bookingDocumentDetails.getPaxId());
                documentsResource.setCommunicationId(sendEditedDocsToCustomer(documentsResource, booking));
                documentsResource.setDocumentIds(Arrays.asList(documentsResource.getDocumentId()));
                documentsResource.setCommunicationTemplate(CommunicationTemplate.COMM_SEND_HNDOVR_DOCS_TO_CUST);
                return setCommDetailsForDocuments(documentsResource);
            }

        }
        return getDocumentsForBooking(documentsResource.getBookID());
    }

    public String sendEditedDocsToCustomer(DocumentsResource documentsResource, OpsBooking opsBooking) throws SQLException, OperationException, IOException {
        EmailUsingTemplateAndDocumentsResource resource = new EmailUsingTemplateAndDocumentsResource();
        List<String> toMail = new ArrayList<>();
        toMail.add(clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType())));
        resource.setToMail(toMail);
        resource.setFromMail(fromMail);
        resource.setPriority(EmailPriority.HIGH);
        resource.setSubject(emailSubject);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setIsActive(true);
        templateInfo.setProcess(templateProcess);
        templateInfo.setScenario(templateScenario);
        templateInfo.setFunction(templateFunction);
        resource.setTemplateInfo(templateInfo);
        Map<String, String> dynamicVariables = new HashMap<>();
        dynamicVariables.put(firstName, documentMDMService.getClientName(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType())));
        dynamicVariables.put(documentName, documentsResource.getDocumentName());
        dynamicVariables.put(order_id, documentsResource.getOrderId());
        dynamicVariables.put(book_ref_id, documentsResource.getBookID());
        resource.setDynamicVariables(dynamicVariables);
        resource.setDocumentReferenceIDs(Arrays.asList(documentsResource.getDocumentId()));
        EmailResponse emailResponse = emailService.sendEmail(resource);
        return emailResponse.getEmailID();
    }

    @Override
    public List<DocumentRowItem> reuploadDocument(DocumentsResource documentsResource) throws IOException, OperationException, ParseException {

        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        BookingDocumentDetailsResource documentDetails = new BookingDocumentDetailsResource();
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {

            PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
            BookingDocumentDetailsResource bookingDocumentDetails = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).findFirst().get();
            CopyUtils.copy(bookingDocumentDetails, documentDetails);
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive()).collect(Collectors.toList());

            if (bookingDocumentDetails.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
            } else {
                if (documentsResource.getDisqualifyPreviousVersion()) {
                    paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
                } else {
                    documentDetails.setDisqualifyDocumentId(bookingDocumentDetails.getDocumentID());
                    documentDetails.setDisqualifyDocumentPageNo(bookingDocumentDetails.getDocumentPageNumber());
                }
            }

            Collections.sort(bookingDocumentDetailsResources, new DocumentPageComparator());
            documentDetails.setDocumentPageNumber(bookingDocumentDetailsResources.get(0).getDocumentPageNumber());
            paxDocuments.getDocumentInfo().getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
            updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {

            RoomDocuments roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
            BookingDocumentDetailsResource bookingDocumentDetails = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).findFirst().get();
            CopyUtils.copy(bookingDocumentDetails, documentDetails);
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive()).collect(Collectors.toList());

            if (bookingDocumentDetails.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
            } else {
                if (documentsResource.getDisqualifyPreviousVersion()) {
                    roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
                } else {
                    documentDetails.setDisqualifyDocumentId(bookingDocumentDetails.getDocumentID());
                    documentDetails.setDisqualifyDocumentPageNo(bookingDocumentDetails.getDocumentPageNumber());
                }
            }

            Collections.sort(bookingDocumentDetailsResources, new DocumentPageComparator());
            documentDetails.setDocumentPageNumber(bookingDocumentDetailsResources.get(0).getDocumentPageNumber());
            roomDocuments.getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
            updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {

            OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
            BookingDocumentDetailsResource bookingDocumentDetails = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).findFirst().get();
            CopyUtils.copy(bookingDocumentDetails, documentDetails);
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive()).collect(Collectors.toList());

            if (bookingDocumentDetails.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
            } else {
                if (documentsResource.getDisqualifyPreviousVersion()) {
                    orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getEditOrReUploadDocumentId()) && bookingDocumentDetailsResource.getDocumentPageNumber().equals(documentsResource.getEditOrReUploadDocumentPageNo())).map(documentDetailsResource -> updateDocumentDetails.changeDocumentStatus(documentDetailsResource)).collect(Collectors.toList());
                } else {
                    documentDetails.setDisqualifyDocumentId(bookingDocumentDetails.getDocumentID());
                    documentDetails.setDisqualifyDocumentPageNo(bookingDocumentDetails.getDocumentPageNumber());
                }
            }

            Collections.sort(bookingDocumentDetailsResources, new DocumentPageComparator());
            documentDetails.setDocumentPageNumber(bookingDocumentDetailsResources.get(0).getDocumentPageNumber());
            orderDocuments.getDocumentInfo().add(uploadDoc(documentsResource, documentDetails));
            updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());

        }
        return getDocumentsForBooking(documentsResource.getBookID());
    }

    @Override
    public List<DocumentRowItem> revokeDocument(DocumentsResource documentsResource) throws IOException, OperationException, ParseException {
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);

        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {

            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> revokedDoc = orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getDocumentID().equals(documentsResource.getDocumentId()) && documentDetailsResource.getDocumentPageNumber().equals(documentsResource.getDocumentPageNumber()) && documentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).map(documentDetailsResource -> updateDocumentDetails.revokeDocument(documentDetailsResource)).collect(Collectors.toList());
                    if (revokedDoc != null && revokedDoc.size() >= 1)
                        updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                    else
                        throw new OperationException("Cannot revoke the document");
                } else
                    throw new OperationException("Cannot revoke the document");
            } else
                throw new OperationException("Cannot revoke the document");

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {

            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> revokedDoc = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getDocumentID().equals(documentsResource.getDocumentId()) && documentDetailsResource.getDocumentPageNumber().equals(documentsResource.getDocumentPageNumber()) && documentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).map(documentDetailsResource -> updateDocumentDetails.revokeDocument(documentDetailsResource)).collect(Collectors.toList());
                    if (revokedDoc != null && revokedDoc.size() >= 1)
                        updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                    else
                        throw new OperationException("Cannot revoke the document");
                } else
                    throw new OperationException("Cannot revoke the document");
            } else
                throw new OperationException("Cannot revoke the document");

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                RoomDocuments roomDocument = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
                if (roomDocument != null && roomDocument.getDocumentInfo() != null && roomDocument.getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> revokedDoc = roomDocument.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getDocumentID().equals(documentsResource.getDocumentId()) && documentDetailsResource.getDocumentPageNumber().equals(documentsResource.getDocumentPageNumber()) && documentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).map(documentDetailsResource -> updateDocumentDetails.revokeDocument(documentDetailsResource)).collect(Collectors.toList());
                    if (revokedDoc != null && revokedDoc.size() >= 1)
                        updateRoomLevelDocuments(documentsResource, roomDocument.getDocumentInfo());
                    else
                        throw new OperationException("Cannot revoke the document");
                } else
                    throw new OperationException("Cannot revoke the document");
            } else
                throw new OperationException("Cannot revoke the document");
        }

        return getDocumentsForBooking(documentsResource.getBookID());
    }

    @Override
    public void updateOrderLevelDocuments(DocumentsResource documentsResource, List<BookingDocumentDetailsResource> documentInfo) throws OperationException {
        OrderDocumentsRequest orderDocumentsRequest = new OrderDocumentsRequest();
        orderDocumentsRequest.setOrderID(documentsResource.getOrderId());
        orderDocumentsRequest.setOrderDocumentIDs(documentInfo);
        orderDocumentsRequest.setUserID(userService.getLoggedInUserId());
        if (orderDocumentsRequest.getUserID() == null) {
            orderDocumentsRequest.setUserID(userService.getSystemUserIdFromMDMToken());
        }
        OpsProduct product = opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId());
        orderDocumentsRequest.setProductCategory(product.getProductCategory());
        orderDocumentsRequest.setProductSubCategory(product.getProductSubCategory());
        RestUtils.put(updateOrderLevelDocumentsUrl, orderDocumentsRequest, String.class);
    }

    @Override
    public void updatePaxLevelDocuments(DocumentsResource documentsResource, List<BookingDocumentDetailsResource> documentInfo) {
        PaxDocumentsRequest paxDocumentsRequest = new PaxDocumentsRequest();
        PaxDocInfo paxDocInfo = new PaxDocInfo();
        paxDocInfo.setDocumentInfo(documentInfo);
        paxDocumentsRequest.setPassengerID(documentsResource.getPaxId());
        paxDocumentsRequest.setPaxDocuments(paxDocInfo);
        paxDocumentsRequest.setUserID(userService.getLoggedInUserId());
        if (paxDocumentsRequest.getUserID() == null) {
            paxDocumentsRequest.setUserID(userService.getSystemUserIdFromMDMToken());
        }
        RestUtils.put(updatePaxLevelDocumentsUrl, paxDocumentsRequest, String.class);
    }

    @Override
    public void updateRoomLevelDocuments(DocumentsResource documentsResource, List<BookingDocumentDetailsResource> documentInfo) {
        RoomDocumentsRequest roomDocumentsRequest = new RoomDocumentsRequest();
        roomDocumentsRequest.setRoomID(documentsResource.getRoomId());
        roomDocumentsRequest.setRoomDocuments(documentInfo);
        roomDocumentsRequest.setUserID(userService.getLoggedInUserId());
        if (roomDocumentsRequest.getUserID() == null) {
            roomDocumentsRequest.setUserID(userService.getSystemUserIdFromMDMToken());
        }
        RestUtils.put(updateRoomLevelDocumentsUrl, roomDocumentsRequest, String.class);
    }

    @Override
    public List<DocumentVersion> getHistoryOfDocuments(DocumentsResource documentsResource) throws IOException, OperationException {

        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId())).collect(Collectors.toList());
                    List<DocumentVersion> documentVersions = getDocumentVersions(bookingDocumentDetailsResources);
                    return documentVersions;
                } else throw new OperationException(Constants.ER01);
            } else throw new OperationException(Constants.ER01);
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {
            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId())).collect(Collectors.toList());
                    List<DocumentVersion> documentVersions = getDocumentVersions(bookingDocumentDetailsResources);
                    return documentVersions;
                } else throw new OperationException(Constants.ER01);
            } else throw new OperationException(Constants.ER01);

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                OpsProduct product = opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId());
                List<String> roomIds = getListOfRoomDetails(product);
                if (roomIds != null && roomIds.size() >= 1) {
                    List<DocumentVersion> documentVersions = new ArrayList<>();
                    for (String roomId : roomIds) {
                        RoomDocuments roomDocuments = null;
                        try {
                            roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                        } catch (Exception ex) {
                            logger.info("No document details found for Room " + roomId);
                        }

                        if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId())).collect(Collectors.toList());
                            documentVersions.addAll(getDocumentVersions(bookingDocumentDetailsResources));
                        }
                    }
                    return documentVersions;
                } else throw new OperationException(Constants.ER01);
            } else throw new OperationException(Constants.ER01);
        } else throw new OperationException(Constants.ER01);
    }

    @Override
    public List<DocumentRowItem> copyDocument(DocumentsResource documentsResource) throws IOException, ParseException, OperationException, RepositoryException {
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        for (String copy : documentsResource.getCopyTo()) {
                            for (BookingDocumentDetailsResource documentDetailsResource : bookingDocumentDetailsResources) {
                                if (!orderDocuments.getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopiedDocumentId() != null && bookingDocumentDetailsResource.getCopiedDocumentId().equals(documentDetailsResource.getDocumentID()) && bookingDocumentDetailsResource.getCopyTo().equals(copy))) {
                                    orderDocuments.getDocumentInfo().add(copyDocumentDetails(documentDetailsResource, copy));
                                }
                            }
                        }
                        updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                    }
                }
            }
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {
            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive() && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        for (String copy : documentsResource.getCopyTo()) {
                            for (BookingDocumentDetailsResource documentDetailsResource : bookingDocumentDetailsResources) {
                                if (!paxDocuments.getDocumentInfo().getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopiedDocumentId() != null && bookingDocumentDetailsResource.getCopiedDocumentId().equals(documentDetailsResource.getDocumentID()) && bookingDocumentDetailsResource.getCopyTo().equals(copy))) {
                                    paxDocuments.getDocumentInfo().getDocumentInfo().add(copyDocumentDetails(documentDetailsResource, copy));
                                }
                            }
                        }
                        updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                    }
                }
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                List<String> roomIds = getListOfRoomDetails(opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId()));
                for (String roomId : roomIds) {
                    RoomDocuments roomDocuments = null;
                    try {
                        roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                    } catch (Exception ex) {
                        logger.info("No document details found for room " + roomId);
                    }
                    if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                        List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                        if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                            for (String copy : documentsResource.getCopyTo()) {
                                for (BookingDocumentDetailsResource documentDetailsResource : bookingDocumentDetailsResources) {
                                    if (!roomDocuments.getDocumentInfo().stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopiedDocumentId() != null && bookingDocumentDetailsResource.getCopiedDocumentId().equals(documentDetailsResource.getDocumentID()) && bookingDocumentDetailsResource.getCopyTo().equals(copy))) {
                                        roomDocuments.getDocumentInfo().add(copyDocumentDetails(documentDetailsResource, copy));
                                    }
                                }
                            }
                            documentsResource.setRoomId(roomId);
                            updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                        }
                    }
                }

            }
        }

        return getDocumentsForBooking(documentsResource.getBookID());
    }

    private BookingDocumentDetailsResource copyDocumentDetails(BookingDocumentDetailsResource documentDetailsResource, String copy) throws OperationException, RepositoryException, IOException {
        DownloadableDocumentResource downloadableDocumentResource = documentLibraryService.get(documentDetailsResource.getDocumentID(), null);
        List<DocumentSearchResource> documentSearchResources = new ArrayList<>();
        DocumentSearchResource documentSearchResource = new DocumentSearchResource();
        documentSearchResource.setId(documentDetailsResource.getDocumentID());
        documentSearchResources.add(documentSearchResource);
        List<JSONObject> metaData = documentLibraryService.getMetaData(documentSearchResources);
        NewDocumentResource newDocumentResource = objectMapper.readValue(metaData.iterator().next().toString(), NewDocumentResource.class);
        DocumentReferenceResource documentReferenceResource = documentLibraryService.create(null, newDocumentResource, downloadableDocumentResource.getInputStream());
        BookingDocumentDetailsResource bookingDocumentDetailsResource = new BookingDocumentDetailsResource();
        CopyUtils.copy(documentDetailsResource, bookingDocumentDetailsResource);
        bookingDocumentDetailsResource.setDocumentID(documentReferenceResource.getId());
        bookingDocumentDetailsResource.setCopyTo(copy);
        bookingDocumentDetailsResource.setDocumentFileName(newDocumentResource.getName());
        bookingDocumentDetailsResource.setDocumentExtension(newDocumentResource.getExtension());
        bookingDocumentDetailsResource.setCopiedDocumentId(documentDetailsResource.getDocumentID());
        return setCopyDetails(bookingDocumentDetailsResource);
    }

    private BookingDocumentDetailsResource setCopyDetails(BookingDocumentDetailsResource bookingDocumentDetailsResource) {
        switch (DocumentCopy.fromString(bookingDocumentDetailsResource.getCopyTo())) {
            case DOCUMENT_COPY_SUPPLIER:
                bookingDocumentDetailsResource.setReason(DocumentUpdateReason.DOCUMENT_COPIED_TO_SUPPLIER.getValue());
                break;
            case DOCUMENT_COPY_TOUR_MANAGER:
                bookingDocumentDetailsResource.setReason(DocumentUpdateReason.DOCUMENT_COPIED_TO_TOUR_MANAGER.getValue());
                break;
            case DOCUMENT_COPY_MAN_ON_SPOT:
                bookingDocumentDetailsResource.setReason(DocumentUpdateReason.DOCUMENT_COPIED_TO_MAN_ON_SPOT.getValue());
                break;
        }

        bookingDocumentDetailsResource.setDisqualifyDocumentPageNo(null);
        bookingDocumentDetailsResource.setDisqualifyDocumentId(null);
        bookingDocumentDetailsResource.setActive(true);
        bookingDocumentDetailsResource.setDocumentStatus(null);
        bookingDocumentDetailsResource.setReceivedStatus(null);
        bookingDocumentDetailsResource.setDocumentReceivedDate(null);
        bookingDocumentDetailsResource.setSendStatus(null);
        bookingDocumentDetailsResource.setRemarks(null);
        bookingDocumentDetailsResource.setCommunicationIds(null);
        return bookingDocumentDetailsResource;
    }

    @Override
    public List<DocumentRowItem> saveDocumentDetails(List<DocumentRowItem> documentDetailsList, String bookID) throws ParseException, OperationException, IOException {
        String response = RestUtils.getForObject(getBookingDocsUrl + bookID, String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        for (DocumentRowItem rowItem : documentDetailsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("documentHandlingGrid.documentSetting._id", rowItem.getDocumentSettingId());
            DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
            if (rowItem.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
                saveOrderDocumentDetails(rowItem, documentDetailsResponse, documentSetting);
            } else if (rowItem.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE) || rowItem.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE)) {
                savePaxLevelDocumentDetails(rowItem, documentDetailsResponse, documentSetting);
            } else if (rowItem.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || rowItem.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
                saveRoomLevelDocumentDetails(rowItem, documentDetailsResponse, documentSetting);
            }
        }

        return getDocumentsForBooking(bookID);
    }

    private void saveOrderDocumentDetails(DocumentRowItem rowItem, DocumentDetailsResponse documentDetailsResponse, DocumentSetting documentSetting) throws OperationException {
        OrderDocuments orderDocuments = null;
        try {
            orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(rowItem.getOrderId())).findFirst().get();
        } catch (Exception ex) {
            logger.debug("No document details found for Order " + rowItem.getOrderId());
        }
        if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getActive() && documentDetailsResource.getDocumentSettingId().equals(rowItem.getDocumentSettingId()) && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
            DocumentStatus documentStatus = getDocumentStatus(documentSetting, bookingDocumentDetailsResources);
            if (!documentStatus.equals(rowItem.getDocuments().iterator().next().getDocumentStatus())) {
                orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getActive() && documentDetailsResource.getDocumentSettingId().equals(rowItem.getDocumentSettingId()) && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.updateDocumentStatus(documentDetailsResource, rowItem.getDocuments().iterator().next().getDocumentStatus())).collect(Collectors.toList());
                DocumentsResource documentsResource = new DocumentsResource();
                documentsResource.setOrderId(rowItem.getOrderId());
                documentsResource.setBookID(rowItem.getBookId());
                updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
            }

        }
    }

    private void savePaxLevelDocumentDetails(DocumentRowItem documentRowItem, DocumentDetailsResponse documentDetailsResponse, DocumentSetting documentSetting) {
        for (Document document : documentRowItem.getDocuments()) {
            PaxDocuments paxDocuments = null;
            try {
                paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(document.getPaxId())).findFirst().get();
            } catch (Exception ex) {
                logger.debug("No document details found for passenger " + document.getPaxId());
            }
            if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getActive() && documentDetailsResource.getOrderId().equals(documentRowItem.getOrderId()) && documentDetailsResource.getDisqualifyDocumentId() == null && documentDetailsResource.getDocumentSettingId().equals(documentRowItem.getDocumentSettingId()) && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                DocumentStatus documentStatus = getDocumentStatus(documentSetting, bookingDocumentDetailsResources);
                DocumentsResource documentsResource = new DocumentsResource();
                documentsResource.setPaxId(document.getPaxId());
                if (!documentStatus.equals(document.getDocumentStatus())) {
                    paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getActive() && documentDetailsResource.getDocumentSettingId().equals(documentRowItem.getDocumentSettingId()) && documentDetailsResource.getOrderId().equals(documentRowItem.getOrderId()) && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.updateDocumentStatus(documentDetailsResource, document.getDocumentStatus())).collect(Collectors.toList());
                    paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getActive() && documentDetailsResource.getDocumentSettingId().equals(documentRowItem.getDocumentSettingId()) && documentDetailsResource.getOrderId().equals(documentRowItem.getOrderId()) && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.copyPassportAndVisaDetails(documentDetailsResource, document)).collect(Collectors.toList());
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                } else {
                    paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getActive() && documentDetailsResource.getDocumentSettingId().equals(documentRowItem.getDocumentSettingId()) && documentDetailsResource.getOrderId().equals(documentRowItem.getOrderId()) && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.copyPassportAndVisaDetails(documentDetailsResource, document)).collect(Collectors.toList());
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                }
            }
        }
    }

    private void saveRoomLevelDocumentDetails(DocumentRowItem documentRowItem, DocumentDetailsResponse documentDetailsResponse, DocumentSetting documentSetting) throws OperationException, JsonProcessingException {
        OpsProduct product = opsBookingService.getProduct(documentRowItem.getBookId(), documentRowItem.getOrderId());
        List<String> roomIds = getListOfRoomDetails(product);
        List<BookingDocumentDetailsResource> bookingDocumentDetailsResourceList = new ArrayList<>();
        if (roomIds != null && roomIds.size() >= 1) {
            for (String roomId : roomIds) {
                RoomDocuments roomDocuments = null;
                try {
                    roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                } catch (Exception ex) {
                    logger.info("No document details found for room " + roomId);
                }
                if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                    bookingDocumentDetailsResourceList.addAll(roomDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentRowItem.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getOrderId().equals(documentRowItem.getOrderId()) && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList()));
                }
            }

            DocumentStatus documentStatus = getDocumentStatus(documentSetting, bookingDocumentDetailsResourceList);

            if (!documentStatus.equals(documentRowItem.getDocuments().iterator().next().getDocumentStatus())) {
                for (String roomId : roomIds) {
                    RoomDocuments roomDocuments = null;
                    try {
                        roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                    } catch (Exception ex) {
                        logger.info("No document details found for room " + roomId);
                    }
                    if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                        roomDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentRowItem.getDocumentSettingId()) && documentDetailsResource.getOrderId().equals(documentRowItem.getOrderId()) && documentDetailsResource.getActive() && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.updateDocumentStatus(documentDetailsResource, documentRowItem.getDocuments().iterator().next().getDocumentStatus())).collect(Collectors.toList());
                        DocumentsResource documentsResource = new DocumentsResource();
                        documentsResource.setRoomId(roomId);
                        updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                    }

                }
            }
        }
    }

    public List<DocumentRowItem> revokeAllReceivedDocuments(DocumentsResource documentsResource) throws ParseException, OperationException, IOException {

        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);

        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())).map(bookingDocumentDetailsResource -> updateDocumentDetails.revokeDocument(bookingDocumentDetailsResource)).collect(Collectors.toList());
                    updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                }
            }
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE)) {
            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())).map(bookingDocumentDetailsResource -> updateDocumentDetails.revokeDocument(bookingDocumentDetailsResource)).collect(Collectors.toList());
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                }
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                List<String> roomIds = getListOfRoomDetails(opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId()));
                if (roomIds != null && roomIds.size() >= 1) {
                    for (String roomId : roomIds) {
                        RoomDocuments roomDocument = null;
                        try {
                            roomDocument = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                        } catch (Exception ex) {
                            logger.info("No document details found for Room " + roomId);
                        }

                        if (roomDocument != null && roomDocument.getDocumentInfo() != null && roomDocument.getDocumentInfo().size() >= 1) {
                            roomDocument.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())).map(bookingDocumentDetailsResource -> updateDocumentDetails.revokeDocument(bookingDocumentDetailsResource)).collect(Collectors.toList());
                            updateRoomLevelDocuments(documentsResource, roomDocument.getDocumentInfo());
                        }
                    }
                }
            }
        }

        return getDocumentsForBooking(documentsResource.getBookID());
    }

    @Override
    public List<String> getListOfPaxDetails(OpsProduct product, boolean leadPax) throws JsonProcessingException {
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

    @Override
    public List<String> getListOfRoomDetails(OpsProduct product) throws OperationException {
        try {
            if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                List<OpsRoom> opsRooms = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product), accoRoomsInfo, OpsRoom.class);
                Set<String> opsRoomTypeSet = opsRooms.stream().map(opsRoom -> opsRoom.getRoomTypeInfo().getRoomTypeName()).collect(Collectors.toSet());
                List<String> roomIds = new ArrayList<>();
                for (String roomType : opsRoomTypeSet) {
                    roomIds.add(opsRooms.stream().filter(opsRoom -> opsRoom.getRoomTypeInfo().getRoomTypeName().equals(roomType)).findFirst().get().getRoomID());
                }
                return roomIds;
            } else return null;
        } catch (Exception ex) {
            throw new OperationException("Failed to serialize OpsProduct object");
        }

    }

    private String getPaxName(OpsProduct product, String paxId) {
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                for (OpsFlightPaxInfo paxInfo : product.getOrderDetails().getFlightDetails().getPaxInfo()) {
                    if (paxInfo.getPassengerID().equals(paxId)) {
                        StringBuilder paxName = new StringBuilder();
                        paxName.append(paxInfo.getFirstName());
                        /*if (!StringUtils.isEmpty(paxInfo.getMiddleName()))
                            paxName.append(" ").append(paxInfo.getMiddleName());*/
                        if (!StringUtils.isEmpty(paxInfo.getLastName()))
                            paxName.append(" ").append(paxInfo.getLastName());
                        return paxName.toString();
                    }
                }
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                for (OpsRoom room : product.getOrderDetails().getHotelDetails().getRooms()) {
                    for (OpsAccommodationPaxInfo paxInfo : room.getPaxInfo()) {
                        if (paxInfo.getPaxID().equals(paxId)) {
                            StringBuilder paxName = new StringBuilder();
                            paxName.append(paxInfo.getFirstName());
                            /*if (!StringUtils.isEmpty(paxInfo.getMiddleName()))
                                paxName.append(" ").append(paxInfo.getMiddleName());*/
                            if (!StringUtils.isEmpty(paxInfo.getLastName()))
                                paxName.append(" ").append(paxInfo.getLastName());
                            return paxName.toString();
                        }

                    }
                }
                break;
        }
        return null;
    }

    private Boolean validateVisaDetails(VisaDocumentResource visaDocumentResource, ZonedDateTime travelEndDate) {
        ZonedDateTime visaValidToDate = DateTimeUtil.formatBEDateTimeZone(visaDocumentResource.getVisaValidTillDate());
        if (visaValidToDate.isAfter(travelEndDate))
            return true;
        else
            return false;
    }

    private Boolean validatePassportDetails(PassportResource passportResource, ZonedDateTime travelEndDate) {
        ZonedDateTime passportExpiryDate = DateTimeUtil.formatBEDateTimeZone(passportResource.getPassportExpiryDate());
        if (passportExpiryDate.isAfter(travelEndDate.plusMonths(6))) {
            return true;
        } else
            return false;
    }

    private List<RoomDetails> getRoomDetails(OpsProduct product, List<String> roomIds) {
        List<RoomDetails> roomDetails = new ArrayList<>();
        for (String roomId : roomIds) {
            RoomDetails roomDetail = new RoomDetails();
            OpsRoom opsRoomDetails = product.getOrderDetails().getHotelDetails().getRooms().stream().filter(opsRoom -> opsRoom.getRoomID().equals(roomId)).findFirst().get();
            roomDetail.setRoomId(opsRoomDetails.getRoomID());
            roomDetail.setRoomCategory(opsRoomDetails.getRoomTypeInfo().getRoomCategoryName());
            roomDetail.setRoomType(opsRoomDetails.getRoomTypeInfo().getRoomTypeName());
            roomDetails.add(roomDetail);
        }
        return roomDetails;
    }

    private Document getDocumentVersionDetailsForCustomerDocumentation(List<BookingDocumentDetailsResource> bookingDetailsResource, Document document, String documentBy) {

        if (documentBy.equals(DocumentCommunicationType.SUPPLIER_TO_CUSTOMER.getCommunicationType()) || documentBy.equals(DocumentCommunicationType.SYSTEM_TO_CUSTOMER.getCommunicationType())) {
            List<BookingDocumentDetailsResource> sentDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getSendStatus() != null && bookingDocumentDetailsResource.getSendStatus().equals(DocumentStatus.SENT.getValue()) && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
            if (sentDocuments != null && sentDocuments.size() >= 1) {
                document.getSentDocuments().addAll(getDocumentVersions(sentDocuments));
            }
        } else {
            List<BookingDocumentDetailsResource> receivedDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue()) && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
            if (receivedDocuments != null && receivedDocuments.size() >= 1) {
                document.getReceivedDocuments().addAll(getDocumentVersions(receivedDocuments));
            }
        }
        return document;
    }

    private Document getUploadedDocsForCustomerDocumentation(List<BookingDocumentDetailsResource> bookingDocumentDetailsResources, Document document) {
        List<BookingDocumentDetailsResource> uploadedDocuments = bookingDocumentDetailsResources.stream().filter(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue()) && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
        if (uploadedDocuments != null && uploadedDocuments.size() >= 1) {
            document.getDocumentVersions().addAll(getDocumentVersions(uploadedDocuments));
        }
        return document;
    }

    private Document getDocumentVersionDetailsForHandoverDocuments(List<BookingDocumentDetailsResource> bookingDetailsResource, Document document) {

        Set<String> copiedTo = new HashSet<>();

        List<BookingDocumentDetailsResource> customerDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())).collect(Collectors.toList());
        if (customerDocuments != null && customerDocuments.size() >= 1) {
            copiedTo.add(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy());
            document.getCustomerCopy().addAll(getDocumentVersions(customerDocuments));
        }

        List<BookingDocumentDetailsResource> supplierDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_SUPPLIER.getDocumentCopy())).collect(Collectors.toList());
        if (supplierDocuments != null && supplierDocuments.size() >= 1) {
            copiedTo.add(DocumentCopy.DOCUMENT_COPY_SUPPLIER.getDocumentCopy());
            document.getSupplierCopy().addAll(getDocumentVersions(supplierDocuments));
        }

        List<BookingDocumentDetailsResource> tourManagerDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_TOUR_MANAGER.getDocumentCopy())).collect(Collectors.toList());
        if (tourManagerDocuments != null && tourManagerDocuments.size() >= 1) {
            copiedTo.add(DocumentCopy.DOCUMENT_COPY_TOUR_MANAGER.getDocumentCopy());
            document.getTourManagerCopy().addAll(getDocumentVersions(tourManagerDocuments));
        }

        List<BookingDocumentDetailsResource> mosDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_MAN_ON_SPOT.getDocumentCopy())).collect(Collectors.toList());
        if (mosDocuments != null && mosDocuments.size() >= 1) {
            copiedTo.add(DocumentCopy.DOCUMENT_COPY_MAN_ON_SPOT.getDocumentCopy());
            document.getMosCopy().addAll(getDocumentVersions(mosDocuments));
        }

        document.setCopiedTo(copiedTo);
        return document;
    }

    private Document getUploadedDocsForHandoverDocuments(List<BookingDocumentDetailsResource> bookingDetailsResource, Document document) {
        List<BookingDocumentDetailsResource> uploadedDocuments = bookingDetailsResource.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue()) && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
        if (uploadedDocuments != null && uploadedDocuments.size() >= 1) {
            document.getDocumentVersions().addAll(getDocumentVersions(uploadedDocuments));
        }
        return getGeneratedDocuments(bookingDetailsResource, document);
    }

    private Document getGeneratedDocuments(List<BookingDocumentDetailsResource> bookingDetailsResource, Document document) {
        List<BookingDocumentDetailsResource> generatedDocuments = bookingDetailsResource.stream().filter(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue()) && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
        if (generatedDocuments != null && generatedDocuments.size() >= 1) {
            document.getGeneratedDocuments().addAll(getDocumentVersions(generatedDocuments));
        }
        return document;
    }

    private String getLatestDocumentReceivedDate(List<BookingDocumentDetailsResource> bookingDocumentDetailsResources) {
        Collections.sort(bookingDocumentDetailsResources, new DocumentReceivedDateComparator());
        return bookingDocumentDetailsResources.iterator().next().getDocumentReceivedDate();
    }

    @Override
    public DocumentStatus getDocumentStatus(DocumentSetting documentSetting, List<BookingDocumentDetailsResource> bookingDocumentDetailsResources) {
        DocumentStatus documentStatus = null;
        if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
            switch (DocumentCommunicationType.fromString(documentSetting.getDocumentBy())) {
                case SYSTEM_TO_CUSTOMER:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getSendStatus() != null && bookingDocumentDetailsResource.getSendStatus().equals(DocumentStatus.SENT_TO_CUSTOMER.getValue())))
                            documentStatus = DocumentStatus.SENT_TO_CUSTOMER;
                        else
                            documentStatus = DocumentStatus.PENDING_TO_BE_SENT_TO_CUSTOMER;
                    } else
                        documentStatus = DocumentStatus.PENDING_TO_BE_SENT_TO_CUSTOMER;
                    break;

                case SUPPLIER_TO_CUSTOMER:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getSendStatus() != null && bookingDocumentDetailsResource.getSendStatus().equals(DocumentStatus.SENT_TO_CUSTOMER.getValue())))
                            documentStatus = DocumentStatus.SENT_TO_CUSTOMER;
                        else if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
                            documentStatus = DocumentStatus.RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER;
                        else if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())))
                            documentStatus = DocumentStatus.RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER;
                        else
                            documentStatus = DocumentStatus.PENDING_FROM_SUPPLIER;
                    } else documentStatus = DocumentStatus.PENDING_FROM_SUPPLIER;
                    break;

                case SUPPLIER_TO_COMPANY:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getSendStatus() != null && bookingDocumentDetailsResource.getSendStatus().equals(DocumentStatus.SENT_TO_CUSTOMER.getValue())))
                            documentStatus = DocumentStatus.SENT_TO_CUSTOMER;
                        else if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
                            documentStatus = DocumentStatus.RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER;
                        else if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())))
                            documentStatus = DocumentStatus.RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER;
                        else
                            documentStatus = DocumentStatus.PENDING_FROM_SUPPLIER;
                    } else documentStatus = DocumentStatus.PENDING_FROM_SUPPLIER;
                    break;
            }

        } else {
            switch (DocumentCommunicationType.fromString(documentSetting.getDocumentBy())) {
                case SYSTEM_TO_CUSTOMER:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getSendStatus() != null && bookingDocumentDetailsResource.getSendStatus().equals(DocumentStatus.SENT.getValue())))
                            documentStatus = DocumentStatus.SENT;
                        else
                            documentStatus = DocumentStatus.PENDING;
                    } else documentStatus = DocumentStatus.PENDING;
                    break;

                case SUPPLIER_TO_CUSTOMER:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getSendStatus() != null && bookingDocumentDetailsResource.getSendStatus().equals(DocumentStatus.SENT.getValue())))
                            documentStatus = DocumentStatus.SENT;
                        else if (bookingDocumentDetailsResources.stream().allMatch(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())) || bookingDocumentDetailsResources.stream().allMatch(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
                            documentStatus = DocumentStatus.PENDING;
                        else if (bookingDocumentDetailsResources.stream().anyMatch(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.GENERATED.getValue())) && bookingDocumentDetailsResources.stream().anyMatch(documentDetailsResource -> documentDetailsResource.getReceivedStatus() != null && documentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
                            documentStatus = DocumentStatus.PENDING;
                        else
                            documentStatus = DocumentStatus.PENDING_FROM_SUPPLIER;
                    } else documentStatus = DocumentStatus.PENDING_FROM_SUPPLIER;
                    break;

                case CUSTOMER_TO_COMPANY:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())) && !bookingDocumentDetailsResources.stream().anyMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.REJECTED.getValue()))) {
                            if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())))
                                documentStatus = DocumentStatus.VERIFIED;
                            else if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.RECEIVED.getValue())))
                                documentStatus = DocumentStatus.RECEIVED;
                            else
                                documentStatus = DocumentStatus.VERIFICATION_PENDING;
                        } else documentStatus = DocumentStatus.REJECTED;
                    } else documentStatus = DocumentStatus.PENDING;

                    break;

                case SUPPLIER_TO_COMPANY:
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        if (bookingDocumentDetailsResources.stream().allMatch(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getReceivedStatus() != null && bookingDocumentDetailsResource.getReceivedStatus().equals(DocumentStatus.RECEIVED.getValue())))
                            documentStatus = DocumentStatus.RECEIVED;
                        else
                            documentStatus = DocumentStatus.PENDING;
                    } else documentStatus = DocumentStatus.PENDING;
                    break;
            }
        }
        return documentStatus;
    }

    @Override
    public List<DocumentVersion> getDocumentsForOrder(DocumentsResource documentsResource) throws IOException, OperationException {
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        List<DocumentVersion> documentVersions = new ArrayList<>();
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                    if (documentsResource.isSendDocsToSupplier()) {
                        bookingDocumentDetailsResources = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus() != null && bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).collect(Collectors.toList());
                    }
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        for (BookingDocumentDetailsResource bookingDocumentDetailsResource : bookingDocumentDetailsResources) {
                            DocumentVersion documentVersion = new DocumentVersion();
                            documentVersion.setStatus(DocumentStatus.fromString(bookingDocumentDetailsResource.getDocumentStatus()));
                            documentVersion.setFileName(bookingDocumentDetailsResource.getDocumentFileName());
                            documentVersion.setDocumentId(bookingDocumentDetailsResource.getDocumentID());
                            documentVersion.setDocumentPageNumber(bookingDocumentDetailsResource.getDocumentPageNumber());
                            documentVersion.setCopyTo(DocumentCopy.fromString(bookingDocumentDetailsResource.getCopyTo()));
                            documentVersion.setExtension(bookingDocumentDetailsResource.getDocumentExtension());
                            documentVersions.add(documentVersion);
                        }
                    }
                }
            }
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {
            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getActive() && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                    if (documentsResource.isSendDocsToSupplier()) {
                        bookingDocumentDetailsResources = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus() != null && bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).collect(Collectors.toList());
                    }
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        for (BookingDocumentDetailsResource bookingDocumentDetailsResource : bookingDocumentDetailsResources) {
                            DocumentVersion documentVersion = new DocumentVersion();
                            documentVersion.setStatus(DocumentStatus.fromString(bookingDocumentDetailsResource.getDocumentStatus()));
                            documentVersion.setFileName(bookingDocumentDetailsResource.getDocumentFileName());
                            documentVersion.setDocumentId(bookingDocumentDetailsResource.getDocumentID());
                            documentVersion.setDocumentPageNumber(bookingDocumentDetailsResource.getDocumentPageNumber());
                            documentVersion.setCopyTo(DocumentCopy.fromString(bookingDocumentDetailsResource.getCopyTo()));
                            documentVersion.setExtension(bookingDocumentDetailsResource.getDocumentExtension());
                            documentVersions.add(documentVersion);
                        }
                    }

                }
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                if (documentsResource.getRoomId() != null) {
                    RoomDocuments roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
                    if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                        List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                        if (documentsResource.isSendDocsToSupplier()) {
                            bookingDocumentDetailsResources = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus() != null && bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).collect(Collectors.toList());
                        }
                        if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                            for (BookingDocumentDetailsResource bookingDocumentDetailsResource : bookingDocumentDetailsResources) {
                                DocumentVersion documentVersion = new DocumentVersion();
                                documentVersion.setStatus(DocumentStatus.fromString(bookingDocumentDetailsResource.getDocumentStatus()));
                                documentVersion.setFileName(bookingDocumentDetailsResource.getDocumentFileName());
                                documentVersion.setDocumentId(bookingDocumentDetailsResource.getDocumentID());
                                documentVersion.setDocumentPageNumber(bookingDocumentDetailsResource.getDocumentPageNumber());
                                documentVersion.setCopyTo(DocumentCopy.fromString(bookingDocumentDetailsResource.getCopyTo()));
                                documentVersion.setExtension(bookingDocumentDetailsResource.getDocumentExtension());
                                documentVersions.add(documentVersion);
                            }
                        }
                    }
                } else {
                    List<String> roomIds = getListOfRoomDetails(opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId()));
                    for (String roomId : roomIds) {
                        RoomDocuments roomDocuments = null;
                        try {
                            roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                        } catch (Exception ex) {
                            logger.info("No document details found for room " + roomId);
                        }
                        if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).collect(Collectors.toList());
                            if (documentsResource.isSendDocsToSupplier()) {
                                bookingDocumentDetailsResources = bookingDocumentDetailsResources.stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentStatus() != null && bookingDocumentDetailsResource.getDocumentStatus().equals(DocumentStatus.VERIFIED.getValue())).collect(Collectors.toList());
                            }
                            if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                                for (BookingDocumentDetailsResource bookingDocumentDetailsResource : bookingDocumentDetailsResources) {
                                    DocumentVersion documentVersion = new DocumentVersion();
                                    documentVersion.setStatus(DocumentStatus.fromString(bookingDocumentDetailsResource.getDocumentStatus()));
                                    documentVersion.setFileName(bookingDocumentDetailsResource.getDocumentFileName());
                                    documentVersion.setDocumentId(bookingDocumentDetailsResource.getDocumentID());
                                    documentVersion.setDocumentPageNumber(bookingDocumentDetailsResource.getDocumentPageNumber());
                                    documentVersion.setCopyTo(DocumentCopy.fromString(bookingDocumentDetailsResource.getCopyTo()));
                                    documentVersion.setExtension(bookingDocumentDetailsResource.getDocumentExtension());
                                    documentVersions.add(documentVersion);
                                }
                            }
                        }
                    }
                }
            }
        }
        return documentVersions;
    }

    @Override
    public void setCopyToCustomerForDocuments(DocumentsResource documentsResource) throws IOException, OperationException {
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getCopyTo() == null).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCopyToCustomer(bookingDocumentDetailsResource)).collect(Collectors.toList());
                    updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                }
            }
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {
            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getCopyTo() == null).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCopyToCustomer(bookingDocumentDetailsResource)).collect(Collectors.toList());
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                }
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                if (documentsResource.getRoomId() != null) {
                    RoomDocuments roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
                    if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                        roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getCopyTo() == null).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCopyToCustomer(bookingDocumentDetailsResource)).collect(Collectors.toList());
                        updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                    }
                } else {
                    List<String> roomIds = getListOfRoomDetails(opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId()));
                    for (String roomId : roomIds) {
                        RoomDocuments roomDocuments = null;
                        try {
                            roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                        } catch (Exception ex) {
                            logger.info("No document details found for room " + roomId);
                        }
                        if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                            roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getCopyTo() == null).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCopyToCustomer(bookingDocumentDetailsResource)).collect(Collectors.toList());
                            documentsResource.setRoomId(roomId);
                            updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<DocumentRowItem> setCommDetailsForDocuments(DocumentsResource documentsResource) throws IOException, OperationException, ParseException {
        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {
            if (documentDetailsResponse.getOrderDocuments() != null && documentDetailsResponse.getOrderDocuments().size() >= 1) {
                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    if (documentsResource.getDocumentIds() == null) {
                        orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                    } else {
                        orderDocuments.getDocumentInfo().stream().filter(documentDetailsResource -> documentsResource.getDocumentIds().contains(documentDetailsResource.getDocumentID()) && documentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && documentDetailsResource.getActive() && documentDetailsResource.getDisqualifyDocumentId() == null && (documentDetailsResource.getCopyTo() == null || (documentDetailsResource.getCopyTo() != null && documentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                    }
                    updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
                }
            }
        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {
            if (documentDetailsResponse.getPaxDocument() != null && documentDetailsResponse.getPaxDocument().size() >= 1) {
                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
                    if (documentsResource.getDocumentIds() == null) {
                        paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getActive() && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                    } else {
                        paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> documentsResource.getDocumentIds().contains(bookingDocumentDetailsResource.getDocumentID()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getActive() && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                    }
                    updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());
                }
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            if (documentDetailsResponse.getRoomDocument() != null && documentDetailsResponse.getRoomDocument().size() >= 1) {
                if (documentsResource.getRoomId() != null) {
                    RoomDocuments roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
                    if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                        if (documentsResource.getDocumentIds() == null) {
                            roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                        } else {
                            roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> documentsResource.getDocumentIds().contains(bookingDocumentDetailsResource.getDocumentID()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                        }
                        updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                    }
                } else {
                    List<String> roomIds = getListOfRoomDetails(opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId()));
                    for (String roomId : roomIds) {
                        RoomDocuments roomDocuments = null;
                        try {
                            roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                        } catch (Exception ex) {
                            logger.info("No document details found for room " + roomId);
                        }
                        if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                            if (documentsResource.getDocumentIds() == null) {
                                roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                            } else {
                                roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> documentsResource.getDocumentIds().contains(bookingDocumentDetailsResource.getDocumentID()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(bookingDocumentDetailsResource -> updateDocumentDetails.setCommIdForDocuments(bookingDocumentDetailsResource, documentsResource)).collect(Collectors.toList());
                            }
                            documentsResource.setRoomId(roomId);
                            updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                        }
                    }
                }
            }
        }
        return getDocumentsForBooking(documentsResource.getBookID());
    }

    @Override
    public List<DocumentRowItem> editCopiedHandoverDocuments(DocumentsResource documentsResource) throws IOException, OperationException, ParseException {
        try {
            String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
            DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
            if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {

                PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
                paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getDocumentId())).map(documentDetailsResource -> updateDocumentDetails.addCommentsToDocument(documentDetailsResource, documentsResource.getRemarks())).collect(Collectors.toList());
                updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());

            } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {

                RoomDocuments roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(documentsResource.getRoomId())).findFirst().get();
                roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getDocumentId())).map(documentDetailsResource -> updateDocumentDetails.addCommentsToDocument(documentDetailsResource, documentsResource.getRemarks())).collect(Collectors.toList());
                updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());

            } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {

                OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
                orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getDocumentID().equals(documentsResource.getDocumentId())).map(documentDetailsResource -> updateDocumentDetails.addCommentsToDocument(documentDetailsResource, documentsResource.getRemarks())).collect(Collectors.toList());
                updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());

            }
        } catch (Exception ex) {
            logger.debug("Handover documents edit is not successful");
        }
        return getDocumentsForBooking(documentsResource.getBookID());
    }

    @Override
    public DocumentInfoForMails getDocumentInfoForMails(DocumentsResource documentsResource) throws IOException, OperationException {
        DocumentInfoForMails documentInfoForMails = new DocumentInfoForMails();
        List<DocumentVersion> documentVersions = getDocumentsForOrder(documentsResource);
        documentInfoForMails.setDocumentVersions(documentVersions);
        if (documentsResource.isClientEmail()) {
            OpsBooking opsBooking = opsBookingService.getBooking(documentsResource.getBookID());
            documentInfoForMails.setEmailId(clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType())));
        }
        return documentInfoForMails;
    }


    @Override
    public List<DocumentRowItem> addRemarksToDocs(DocumentsResource documentsResource) throws ParseException, OperationException, IOException {

        String response = RestUtils.getForObject(getBookingDocsUrl + documentsResource.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(response, DocumentDetailsResponse.class);
        if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_PAX_WISE)) {
            PaxDocuments paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(documentsResource.getPaxId())).findFirst().get();
            paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.addCommentsToDocument(documentDetailsResource, documentsResource.getRemarks())).collect(Collectors.toList());
            updatePaxLevelDocuments(documentsResource, paxDocuments.getDocumentInfo().getDocumentInfo());

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE) || documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE)) {
            OpsProduct product = opsBookingService.getProduct(documentsResource.getBookID(), documentsResource.getOrderId());
            List<String> roomIds = getListOfRoomDetails(product);
            if (roomIds != null && roomIds.size() >= 1) {
                for (String roomId : roomIds) {
                    RoomDocuments roomDocuments = null;
                    try {
                        roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                    } catch (Exception ex) {
                        logger.info("No document details found for room " + roomId);
                    }
                    if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                        roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(documentsResource.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.addCommentsToDocument(documentDetailsResource, documentsResource.getRemarks())).collect(Collectors.toList());
                        updateRoomLevelDocuments(documentsResource, roomDocuments.getDocumentInfo());
                    }
                }
            }

        } else if (documentsResource.getDocumentWise().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE)) {

            OrderDocuments orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(documentsResource.getOrderId())).findFirst().get();
            orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentsResource.getDocumentSettingId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && (bookingDocumentDetailsResource.getCopyTo() == null || (bookingDocumentDetailsResource.getCopyTo() != null && bookingDocumentDetailsResource.getCopyTo().equals(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy())))).map(documentDetailsResource -> updateDocumentDetails.addCommentsToDocument(documentDetailsResource, documentsResource.getRemarks())).collect(Collectors.toList());
            updateOrderLevelDocuments(documentsResource, orderDocuments.getDocumentInfo());
        }
        return getDocumentsForBooking(documentsResource.getBookID());
    }

    @Override
    public List<DocStatusResource> getReceivedDocumentsStatus() {
        Map<String, DocumentStatus> docStatus = new HashMap<>();
        docStatus.put(DocumentStatus.PENDING.getValue(), DocumentStatus.PENDING);
        docStatus.put(DocumentStatus.REJECTED.getValue(), DocumentStatus.REJECTED);
        docStatus.put(DocumentStatus.VERIFIED.getValue(), DocumentStatus.VERIFIED);
        docStatus.put(DocumentStatus.RECEIVED.getValue(), DocumentStatus.RECEIVED);
        docStatus.put(DocumentStatus.VERIFICATION_PENDING.getValue(), DocumentStatus.VERIFICATION_PENDING);
        List<DocStatusResource> docStatusResources = new ArrayList<>();
        DocStatusResource docStatusResource = null;
        for (Map.Entry<String, DocumentStatus> entry : docStatus.entrySet()) {
            docStatusResource = new DocStatusResource();
            docStatusResource.setName(entry.getKey());
            docStatusResource.setValue(entry.getValue());
            docStatusResources.add(docStatusResource);
        }
        return docStatusResources;
    }

    @Override
    public List<DocStatusResource> getSentDocumentsStatus() {
        Map<String, DocumentStatus> docStatus = new HashMap<>();
        docStatus.put(DocumentStatus.SENT.getValue(), DocumentStatus.SENT);
        docStatus.put(DocumentStatus.PENDING.getValue(), DocumentStatus.PENDING);
        docStatus.put(DocumentStatus.PENDING_FROM_SUPPLIER.getValue(), DocumentStatus.PENDING_FROM_SUPPLIER);
        List<DocStatusResource> docStatusResources = new ArrayList<>();
        DocStatusResource docStatusResource = null;
        for (Map.Entry<String, DocumentStatus> entry : docStatus.entrySet()) {
            docStatusResource = new DocStatusResource();
            docStatusResource.setName(entry.getKey());
            docStatusResource.setValue(entry.getValue());
            docStatusResources.add(docStatusResource);
        }
        return docStatusResources;
    }

    @Override
    public List<DocStatusResource> getHandoverDocumentsStatus() {
        Map<String, DocumentStatus> docStatus = new HashMap<>();
        docStatus.put(DocumentStatus.SENT_TO_CUSTOMER.getValue(), DocumentStatus.SENT_TO_CUSTOMER);
        docStatus.put(DocumentStatus.PENDING_TO_BE_SENT_TO_CUSTOMER.getValue(), DocumentStatus.PENDING_TO_BE_SENT_TO_CUSTOMER);
        docStatus.put(DocumentStatus.PENDING_FROM_SUPPLIER.getValue(), DocumentStatus.PENDING_FROM_SUPPLIER);
        docStatus.put(DocumentStatus.RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER.getValue(), DocumentStatus.RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER);
        List<DocStatusResource> docStatusResources = new ArrayList<>();
        DocStatusResource docStatusResource = null;
        for (Map.Entry<String, DocumentStatus> entry : docStatus.entrySet()) {
            docStatusResource = new DocStatusResource();
            docStatusResource.setName(entry.getKey());
            docStatusResource.setValue(entry.getValue());
            docStatusResources.add(docStatusResource);
        }
        return docStatusResources;
    }

    private List<DocumentVersion> getDocumentVersions(List<BookingDocumentDetailsResource> documentsList) {
        List<DocumentVersion> documentVersions = new ArrayList<>();
        for (BookingDocumentDetailsResource bookingDocumentDetailsResource : documentsList) {
            DocumentVersion documentVersion = new DocumentVersion();
            documentVersion.setStatus(DocumentStatus.fromString(bookingDocumentDetailsResource.getDocumentStatus()));
            documentVersion.setFileName(bookingDocumentDetailsResource.getDocumentFileName());
            documentVersion.setDocumentId(bookingDocumentDetailsResource.getDocumentID());
            documentVersion.setDocumentPageNumber(bookingDocumentDetailsResource.getDocumentPageNumber());
            documentVersion.setCopyTo(DocumentCopy.fromString(bookingDocumentDetailsResource.getCopyTo()));
            documentVersion.setRoomId(bookingDocumentDetailsResource.getRoomId());
            if (bookingDocumentDetailsResource.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType()) || bookingDocumentDetailsResource.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()))
                documentVersion.setDocType(DocumentType.RECEIVE_DOCUMENT);
            else
                documentVersion.setDocType(DocumentType.SEND_DOCUMENT);
            documentVersion.setExtension(bookingDocumentDetailsResource.getDocumentExtension());
            documentVersion.setRemarks(bookingDocumentDetailsResource.getRemarks());
            documentVersion.setReason(bookingDocumentDetailsResource.getReason());
            documentVersion.setCommIds(bookingDocumentDetailsResource.getCommunicationIds());
            documentVersions.add(documentVersion);
        }
        return documentVersions;
    }

    @Override
    public void createToDoTaskAndSendAlertToAdminForDocumentConfiguration(OpsProduct product, OpsBooking opsBooking, String referenceId) {

        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("CONFIGURE_DOCUMENTS");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("supplierID", product.getSupplierID());
            entity.put("productCategory", product.getProductCategory());
            entity.put("productSubCategory", product.getProductSubCategory());
            entity.put("supplierName", product.getSourceSupplierName());
            entity.put("orderID", product.getOrderID());
            entity.put("bookID", opsBooking.getBookID());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to Admin to configure documents in MDM");
        }

        try {
            ToDoTaskResource todo = new ToDoTaskResource();
            todo.setTaskSubTypeId(ToDoTaskSubTypeValues.CONFIGURE_DOCUMENTS_IN_MDM.name());
            todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            todo.setTaskNameId(ToDoTaskNameValues.CONFIGURE.getValue());
            todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
            todo.setBookingRefId(opsBooking.getBookID());

            todo.setCompanyId(opsBooking.getCompanyId());
            todo.setTaskOrientedTypeId(ToDoTaskOrientedValues.ACTION_ORIENTED.getValue());
            todo.setClientCategoryId(opsBooking.getClientCategory());
            todo.setClientId(opsBooking.getClientID());
            todo.setClientSubCategoryId(opsBooking.getClientSubCategory());
            todo.setClientTypeId(opsBooking.getClientType());
            todo.setCompanyId(opsBooking.getCompanyId());
            todo.setProductId(product.getProductName());
            todo.setProductCategory(product.getProductCategory());
            todo.setProductSubCategory(product.getProductSubCategory());
            todo.setCompanyMarketId(opsBooking.getCompanyMarket());

            todo.setReferenceId(referenceId);
            if (!checkTodoTaskCreation(todo)) {
                //supplier id, product name, product category, product sub category, product location details
                todo.setCreatedByUserId(userService.getLoggedInUserId());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Configure Document details in MDM Document Handling Master for Supplier - Supplier Id: ")
                        .append(product.getSupplierID()).append(", ").append("Supplier Name: ").append(product.getSourceSupplierName())
                        .append(" - ").append("for Product Category: ").append(product.getProductCategory()).append(" - ")
                        .append("Product Sub Category: ").append(product.getProductSubCategory()).append(" - ").append("Order: ").append(product.getOrderID())
                        .append(" - ").append("Booking: ").append(opsBooking.getBookID());
                todo.setRemark(stringBuilder.toString());
                toDoTaskService.save(todo);
            }
        } catch (Exception ex) {
            logger.info("Failed to create todo task for Admin to configure documents in MDM");
        }

    }

    @Override
    public void sendAlertAndCreateTodoTaskForOpsUserToCollectDocuments(String bookId, String orderId, String productName) {

        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            if (orderId == null)
                inlineMessageResource.setAlertName("RECEIVE_DOCUMENTS_FOR_BOOKING");
            else
                inlineMessageResource.setAlertName("COLLECT_DOCUMENTS_FOR_ORDER");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("bookID", bookId);
            if (orderId != null) {
                entity.put("orderID", orderId);
                entity.put("productName", productName);
            }
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to ops user");
        }

        try {
            //add path in todojsonconfig
            ToDoTaskResource todo = new ToDoTaskResource();
            if (orderId == null)
                todo.setTaskSubTypeId(ToDoTaskSubTypeValues.COLLECT_DOCUMENTS_FOR_BOOKING.name());
            else
                todo.setTaskSubTypeId(ToDoTaskSubTypeValues.COLLECT_DOCUMENTS_FOR_ORDER.name());
            todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            todo.setTaskNameId(ToDoTaskNameValues.CUSTOMER_DOCUMENTATION.getValue());
            todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
            todo.setBookingRefId(bookId);
            if (orderId == null)
                todo.setReferenceId(bookId);
            else todo.setReferenceId(orderId);
            if (!checkTodoTaskCreation(todo)) {
                todo.setCreatedByUserId(userService.getLoggedInUserId());
                StringBuilder stringBuilder = new StringBuilder();
                if (orderId != null) {
                    stringBuilder.append("Collect Documents to be received from Customer for Product Name: ").append(productName).append(", ")
                            .append("Order: ").append(orderId).append(", ").append("Booking: ").append(bookId);
                } else {
                    stringBuilder.append("Collect Documents to be received from Customer for Booking: ").append(bookId);
                }
                todo.setRemark(stringBuilder.toString());
                toDoTaskService.save(todo);
            }
        } catch (Exception ex) {
            logger.info("Failed to create todo task for ops user to receive documents");
        }
    }

    @Override
    public String getProductName(String bookId, String orderId) throws OperationException {
        OpsProduct product = opsBookingService.getProduct(bookId, orderId);
        String productName = null;
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                productName = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOperatingAirline().getAirlineCode();
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                productName = product.getOrderDetails().getHotelDetails().getHotelName();
                break;
            default:
                break;

        }
        return productName;
    }

    @Override
    public Map<String, Object> generateHandoverDocs(DocumentsResource documentsResource) throws OperationException {
        logger.info("Entered DocumentDetailsServiceImpl::generateHandoverDocs() method to generate handover documents");
        Map<String, Object> result = new HashMap<>();
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(documentsResource.getBookID());
            OpsProduct product = opsBooking.getProducts().stream().filter(product1 -> product1.getOrderID().equals(documentsResource.getOrderId())).findFirst().get();
            //check whether configured criteria is met
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("documentHandlingGrid.documentSetting._id", documentsResource.getDocumentSettingId());
            List<DocumentHandlingGrid> documentHandlingGridList = documentMasterRequirements.getDocumentHandlingGridDetails(jsonObject.toString());
            DocumentHandlingGrid documentGrid = documentHandlingGridList.iterator().next();
            DocumentSetting documentSetting = documentGrid.getDocumentSetting().stream().filter(docSetting -> docSetting.getId().equals(documentsResource.getDocumentSettingId())).findFirst().get();
            logger.info("Checking trigger event");
            if (checkTriggerEvent(documentSetting, product, documentsResource.getBookID()) && isReconfirmed(opsBooking, product)) {
                logger.info("Generating Handover Documents");
                generateDocumentService.generateHandOverDocument(opsBooking, documentSetting.getDocumentName(), product, documentSetting);
                HandoverAndCustomerDocInfoSearchCriteria criteria = new HandoverAndCustomerDocInfoSearchCriteria();
                criteria.setBookId(opsBooking.getBookID());
                criteria.setOrderId(product.getOrderID());
                criteria.setDocumentSettingId(documentSetting.getId());
                List<HandoverAndCustomerDocInfo> docInfoList = handoverAndCustomerDocInfoRepository.searchDetails(criteria);
                if (docInfoList != null && docInfoList.size() >= 1) {
                    handoverAndCustomerDocInfoRepository.deleteDetails(docInfoList.iterator().next().getId());
                }
                result.put("generated", "true");
            } else {
                if (isApprovalRequired(documentGrid))
                    result.put("generated", "false");
                else result.put("generated", "NA");
            }

            result.put("data", getDocumentsForBooking(documentsResource.getBookID()));
        } catch (Exception ex) {
            logger.info("Failed to generate Handover Documents");
            throw new OperationException("Handover Document Generation failed");
        }
        logger.info("Exit DocumentDetailsServiceImpl::generateHandoverDocs() method");
        return result;
    }

    @Override
    public Map<String, Object> sendApprovalRequestToGenerateHandoverDocuments(DocumentsResource documentsResource) throws OperationException, IOException, ParseException {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("documentHandlingGrid.documentSetting._id", documentsResource.getDocumentSettingId());
        DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
        createTodoTaskAndSendAlertToApprover(documentsResource, documentSetting);
        result.put("message", "Document submitted for Approval");
        result.put("data", getDocumentsForBooking(documentsResource.getBookID()));
        return result;
    }

    private void createTodoTaskAndSendAlertToApprover(DocumentsResource documentsResource, DocumentSetting documentSetting) {

        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("APPROVE_HANDOVER_DOC_GEN");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("documentName", documentSetting.getDocumentName());
            entity.put("orderID", documentsResource.getOrderId());
            entity.put("bookID", documentsResource.getBookID());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to Approver to approve the document generation");
        }

        try {
            //add path in todoconfig json
            ToDoTaskResource todo = new ToDoTaskResource();
            todo.setTaskSubTypeId(ToDoTaskSubTypeValues.HANDOVER_DOCUMENT_GENERATION.name());
            todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            todo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
            todo.setBookingRefId(documentsResource.getBookID());

            HandoverDocGenApprovalDetails handoverDocGenApprovalDetails = new HandoverDocGenApprovalDetails();
            handoverDocGenApprovalDetails.setApprovalRequestJustification(documentsResource.getRemarks());
            handoverDocGenApprovalDetails.setBookID(documentsResource.getBookID());
            handoverDocGenApprovalDetails.setOrderId(documentsResource.getOrderId());
            handoverDocGenApprovalDetails.setDocumentSettingId(documentsResource.getDocumentSettingId());
            handoverDocGenApprovalDetails.setRoomId(documentsResource.getRoomId());
            handoverDocGenApprovalDetails.setPaxId(documentsResource.getPaxId());
            HandoverDocGenSearchCriteria searchCriteria = new HandoverDocGenSearchCriteria();
            CopyUtils.copy(handoverDocGenApprovalDetails, searchCriteria);
            List<HandoverDocGenApprovalDetails> handoverDocGenApprovalDetailsList = handoverDocGenApprovalRepository.searchDetails(searchCriteria);
            if (handoverDocGenApprovalDetailsList != null && handoverDocGenApprovalDetailsList.size() >= 1)
                todo.setReferenceId(handoverDocGenApprovalDetailsList.iterator().next().getId());
            else
                todo.setReferenceId(handoverDocGenApprovalRepository.saveDetails(handoverDocGenApprovalDetails).getId());

            if (!checkTodoTaskCreation(todo)) {
                todo.setCreatedByUserId(userService.getLoggedInUserId());
                todo.setRemark(documentsResource.getRemarks());
                StringBuilder suggestedActions = new StringBuilder();
                suggestedActions.append("Approve Handover Document Generation for Document Name: ").append(documentSetting.getDocumentName())
                        .append(", ").append("for Order ").append(documentsResource.getOrderId()).append(" and Booking: ").append(documentsResource.getBookID());
                todo.setSuggestedActions(suggestedActions.toString());

                toDoTaskService.save(todo);
            }
        } catch (Exception ex) {
            logger.info("Failed to create todo task for Approver to approve the document generation");
        }

    }

    @Override
    public DocumentsResource getHandoverDocApprovalDetails(String id) throws OperationException {
        HandoverDocGenApprovalDetails details = handoverDocGenApprovalRepository.getDetailsById(id);
        DocumentsResource resource = new DocumentsResource();
        CopyUtils.copy(details, resource);
        resource.setHandoverDocGenId(details.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("documentHandlingGrid.documentSetting._id", details.getDocumentSettingId());
        DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
        resource.setDocumentName(documentSetting.getDocumentName());
        OpsBooking opsBooking = opsBookingService.getBooking(details.getBookID());
        resource.setCustomerName(documentMDMService.getClientName(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType())));
        resource.setProductName(getProductName(details.getBookID(), details.getOrderId()));
        TotalRevenueAndGrossProfitResource revenueResource = new TotalRevenueAndGrossProfitResource();
        revenueResource.setBookId(details.getBookID());
        resource.setRevenueAndGrossProfitResource(getTotalRevenueAndTotalGrossProfitValues(revenueResource));
        return resource;
    }

    @Override
    public TotalRevenueAndGrossProfitResource getTotalRevenueAndTotalGrossProfitValues(TotalRevenueAndGrossProfitResource revenueAndGrossProfitResource) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(revenueAndGrossProfitResource.getBookId());

        if (!StringUtils.isEmpty(opsBooking.getClientType()) && opsBooking.getClientType().equals("B2B")) {
            if (StringUtils.isEmpty(revenueAndGrossProfitResource.getFromDateRange()) && StringUtils.isEmpty(revenueAndGrossProfitResource.getToDateRange())) {
                ZonedDateTime toDate = ZonedDateTime.now();
                LocalDateTime localDateTime = LocalDateTime.of(toDate.getYear() - 1, 04, 01, 00, 00, 00);
                ZonedDateTime fromDate = ZonedDateTime.of(localDateTime, toDate.getZone());
                revenueAndGrossProfitResource.setToDateRange(DateConverter.zonedDateTimeToString(toDate));
                revenueAndGrossProfitResource.setFromDateRange(DateConverter.zonedDateTimeToString(fromDate));
            }

            StringBuilder revenueUrl = new StringBuilder();
            revenueUrl.append(financeUrl).append(opsBooking.getCompanyId()).append(totalRevenue).append(opsBooking.getClientID()).append("&from=")
                    .append(revenueAndGrossProfitResource.getFromDateRange()).append("&to=").append(revenueAndGrossProfitResource.getToDateRange());

            String totalRevenueResponse = null;
            totalRevenueResponse = mdmRestUtils.getForObject(revenueUrl.toString(), String.class);

            String totalRevenue = jsonObjectProvider.getAttributeValue(totalRevenueResponse, revenueOrGrossProfitValuePath, String.class);
            revenueAndGrossProfitResource.setTotalRevenueAmount(new BigDecimal(totalRevenue));
            StringBuilder profitUrl = new StringBuilder();
            profitUrl.append(financeUrl).append(opsBooking.getCompanyId()).append(totalGrossProfit).append(opsBooking.getClientID()).append("&from=")
                    .append(revenueAndGrossProfitResource.getFromDateRange()).append("&to=").append(revenueAndGrossProfitResource.getToDateRange());
            String totalGrossProfitResponse = mdmRestUtils.getForObject(profitUrl.toString(), String.class);
            String totalGrossProfit = jsonObjectProvider.getAttributeValue(totalGrossProfitResponse, revenueOrGrossProfitValuePath, String.class);
            revenueAndGrossProfitResource.setTotalGrossProfitAmount(new BigDecimal(totalGrossProfit));
            revenueAndGrossProfitResource.setCompanyId(opsBooking.getCompanyId());
            revenueAndGrossProfitResource.setBookId(opsBooking.getBookID());
            revenueAndGrossProfitResource.setEntityId(opsBooking.getClientID());
            revenueAndGrossProfitResource.setEntityType(opsBooking.getClientType());

            BookingSearchCriteria bookingSearchCriteria = new BookingSearchCriteria();
            BookingDetailsFilter bookingDetailsFilter = new BookingDetailsFilter();
            bookingDetailsFilter.setBookingFromDate(revenueAndGrossProfitResource.getFromDateRange());
            bookingDetailsFilter.setBookingToDate(revenueAndGrossProfitResource.getToDateRange());
            bookingSearchCriteria.setBookingBasedFilter(bookingDetailsFilter);

            ClientAndPassengerBasedFilter clientAndPassengerBasedFilter = new ClientAndPassengerBasedFilter();
            clientAndPassengerBasedFilter.setClientId(opsBooking.getClientID());
            clientAndPassengerBasedFilter.setClientType(opsBooking.getClientType());
            bookingSearchCriteria.setClientPxBasedFilter(clientAndPassengerBasedFilter);

            List<BookingSearchResponseItem> bookings = opsBookingService.searchBookings(bookingSearchCriteria);
            if (bookings != null && bookings.size() >= 1)
                revenueAndGrossProfitResource.setNoOfBookings(bookings.size());
            else revenueAndGrossProfitResource.setNoOfBookings(0);

            return revenueAndGrossProfitResource;
        } else
            return null;
    }

    @Override
    public Map<String, String> approveHandoverDocGeneration(DocumentsResource resource) throws OperationException {
        Map<String, String> result = new HashMap<>();
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("REG_HANDOVER_DOC_GEN_APPROVAL");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("documentName", resource.getDocumentName());
            entity.put("orderID", resource.getOrderId());
            entity.put("bookID", resource.getBookID());
            entity.put("remarks", StringUtils.isEmpty(resource.getRemarks()) ? "--" : resource.getRemarks());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to ops user regarding approval of handover document generation");
        }

        generateHandoverDocsAfterApproval(resource);
        toDoTaskService.updateToDoTaskStatus(resource.getHandoverDocGenId(), ToDoTaskSubTypeValues.HANDOVER_DOCUMENT_GENERATION, ToDoTaskStatusValues.COMPLETED);
        handoverDocGenApprovalRepository.deleteRecordById(resource.getHandoverDocGenId());
        result.put("message", "Handover Documents generation is successfully done");
        return result;
    }

    private void generateHandoverDocsAfterApproval(DocumentsResource documentsResource) {
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(documentsResource.getBookID());
            OpsProduct product = opsBooking.getProducts().stream().filter(product1 -> product1.getOrderID().equals(documentsResource.getOrderId())).findFirst().get();
            //check whether configured criteria is met
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("documentHandlingGrid.documentSetting._id", documentsResource.getDocumentSettingId());
            DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
            generateDocumentService.generateHandOverDocument(opsBooking, documentSetting.getDocumentName(), product, documentSetting);
            HandoverAndCustomerDocInfoSearchCriteria criteria = new HandoverAndCustomerDocInfoSearchCriteria();
            criteria.setBookId(opsBooking.getBookID());
            criteria.setOrderId(product.getOrderID());
            criteria.setDocumentSettingId(documentSetting.getId());
            List<HandoverAndCustomerDocInfo> docInfoList = handoverAndCustomerDocInfoRepository.searchDetails(criteria);
            if (docInfoList != null && docInfoList.size() >= 1) {
                handoverAndCustomerDocInfoRepository.deleteDetails(docInfoList.iterator().next().getId());
            }
        } catch (Exception ex) {
            logger.info("Failed to generate Handover Documents");
        }
    }

    @Override
    public Map<String, String> rejectHandoverDocGeneration(DocumentsResource resource) {
        Map<String, String> result = new HashMap<>();
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("REG_HANDOVER_DOC_GEN_REJECTION");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("documentName", resource.getDocumentName());
            entity.put("orderID", resource.getOrderId());
            entity.put("bookID", resource.getBookID());
            entity.put("rejectionReason", resource.getRemarks());
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to ops user regarding rejection of handover document generation");
        }
        toDoTaskService.updateToDoTaskStatus(resource.getHandoverDocGenId(), ToDoTaskSubTypeValues.HANDOVER_DOCUMENT_GENERATION, ToDoTaskStatusValues.COMPLETED);
        handoverDocGenApprovalRepository.deleteRecordById(resource.getHandoverDocGenId());
        result.put("message", "Handover Documents generation is rejected");
        return result;
    }

    @Override
    public void saveMDMUnConfiguredDataDetails(OpsProduct product, OpsBooking opsBooking) {
        //send an alert to admin to configure
        String referenceId = null;
        MDMUnConfiguredData mdmUnConfiguredData = new MDMUnConfiguredData();
        mdmUnConfiguredData.setProductCategory(product.getProductCategory());
        mdmUnConfiguredData.setProductCategorySubType(product.getProductSubCategory());
        mdmUnConfiguredData.setSupplierId(product.getSupplierID());
        mdmUnConfiguredData.setOrderId(product.getOrderID());
        mdmUnConfiguredData.setBookId(opsBooking.getBookID());

        MDMUnConfiguredDataSearchCriteria searchCriteria = new MDMUnConfiguredDataSearchCriteria();
        CopyUtils.copy(mdmUnConfiguredData, searchCriteria);
        List<MDMUnConfiguredData> result = mdmUnConfiguredDataRepository.searchDetails(searchCriteria);

        if (result == null || result.size() == 0) {
            referenceId = mdmUnConfiguredDataRepository.saveDetails(mdmUnConfiguredData).getId();
        }
        createToDoTaskAndSendAlertToAdminForDocumentConfiguration(product, opsBooking, referenceId);
    }

    @Override
    public void saveCutOffDateConfigurationDetails(DocumentSetting documentSetting, OpsBooking aBooking, OpsProduct product) throws OperationException {
        CutOffDateConfigurationSearchCriteria searchCriteria = null;

        CutOffDateConfiguration cutOffDateConfiguration = new CutOffDateConfiguration();
        cutOffDateConfiguration.setBookId(aBooking.getBookID());
        cutOffDateConfiguration.setOrderId(product.getOrderID());
        cutOffDateConfiguration.setDocumentSettingId(documentSetting.getId());
        cutOffDateConfiguration.setDocumentWise(documentSetting.getDocumentsAsPer());
        cutOffDateConfiguration.setDocumentBy(documentSetting.getDocumentBy());
        cutOffDateConfiguration.setDocumentName(documentSetting.getDocumentName());
        cutOffDateConfiguration.setProductName(getProductName(aBooking.getBookID(), product.getOrderID()));
        cutOffDateConfiguration.setCutOffDate(ZonedDateTime.parse(DocumentCutOffDateCalculation.calculateCutOffDate(product, documentSetting, aBooking.getBookingDateZDT())));

        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE.getEntityReference())) {
            List<String> roomIds = getListOfRoomDetails(product);
            if (roomIds != null && roomIds.size() >= 1) {
                cutOffDateConfiguration.setRoomId(roomIds.iterator().next());
            }
        }

        if (!documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference())) {
            Document document = documentUtils.getPaxInfo(product);
            cutOffDateConfiguration.setPaxId(document.getPaxId());
            cutOffDateConfiguration.setPaxName(document.getPassengerName());

            searchCriteria = new CutOffDateConfigurationSearchCriteria();
            CopyUtils.copy(cutOffDateConfiguration, searchCriteria);
            List<CutOffDateConfiguration> result = cutOffDateConfigurationRepository.searchDetails(searchCriteria);
            if (result == null || result.size() == 0)
                cutOffDateConfigurationRepository.saveDetails(cutOffDateConfiguration);
        } else {
            if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                for (OpsFlightPaxInfo paxInfo : product.getOrderDetails().getFlightDetails().getPaxInfo()) {
                    cutOffDateConfiguration.setPaxId(paxInfo.getPassengerID());
                    cutOffDateConfiguration.setPaxName(getPaxName(product, paxInfo.getPassengerID()));
                    searchCriteria = new CutOffDateConfigurationSearchCriteria();
                    CopyUtils.copy(cutOffDateConfiguration, searchCriteria);
                    List<CutOffDateConfiguration> resultList = cutOffDateConfigurationRepository.searchDetails(searchCriteria);
                    if (resultList == null || resultList.size() == 0)
                        cutOffDateConfigurationRepository.saveDetails(cutOffDateConfiguration);
                }
            } else if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                for (OpsRoom opsRoom : product.getOrderDetails().getHotelDetails().getRooms()) {
                    for (OpsAccommodationPaxInfo paxInfo : opsRoom.getPaxInfo()) {
                        cutOffDateConfiguration.setPaxId(paxInfo.getPaxID());
                        cutOffDateConfiguration.setPaxName(getPaxName(product, paxInfo.getPaxID()));
                        searchCriteria = new CutOffDateConfigurationSearchCriteria();
                        CopyUtils.copy(cutOffDateConfiguration, searchCriteria);
                        List<CutOffDateConfiguration> resultList = cutOffDateConfigurationRepository.searchDetails(searchCriteria);
                        if (resultList == null || resultList.size() == 0)
                            cutOffDateConfigurationRepository.saveDetails(cutOffDateConfiguration);
                    }
                }
            }
        }


    }

    @Override
    public String getDocHandlingMasterSearchCriteria(OpsProduct product) {
        String criteria = null;
        JSONObject jsonObject = null;
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                jsonObject = new JSONObject();
                jsonObject.put("productTransportation.productCategory", product.getProductCategory());
                jsonObject.put("productTransportation.productSubCategory", product.getProductSubCategory());
                jsonObject.put("supplierDetails.supplierId", product.getSupplierID());
                //todo filter using location details and supplier name
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                jsonObject = new JSONObject();
                jsonObject.put("productAccommodation.productCategory", product.getProductCategory());
                jsonObject.put("productAccommodation.productSubCategory", product.getProductSubCategory());
                jsonObject.put("supplierDetails.supplierId", product.getSupplierID());
                //todo filter using location details and supplier name
                break;
            default:
                break;
        }
        if (jsonObject != null) {
            criteria = jsonObject.toString();
        }
        return criteria;
    }

    private boolean checkTodoTaskCreation(ToDoTaskResource toDoTaskResource) throws OperationException {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        CopyUtils.copy(toDoTaskResource, toDoCriteria);
        ToDoResponse toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        if (toDoResponse != null && toDoResponse.getData() != null && toDoResponse.getData().size() >= 1)
            return true;
        else
            return false;
    }

    private boolean checkApprovalRequest(Document document, DocumentSetting documentSetting, OpsProduct product) {
        if (document.getGeneratedDocuments() == null || document.getGeneratedDocuments().size() == 0) {
            HandoverDocGenSearchCriteria searchCriteria = new HandoverDocGenSearchCriteria();
            searchCriteria.setDocumentSettingId(documentSetting.getId());
            searchCriteria.setOrderId(product.getOrderID());
            searchCriteria.setPaxId(document.getPaxId());
            if (document.getRoomIds() != null && document.getRoomIds().size() >= 1)
                searchCriteria.setRoomId(document.getRoomIds().iterator().next().getRoomId());
            List<HandoverDocGenApprovalDetails> handoverDocGenApprovalDetails = handoverDocGenApprovalRepository.searchDetails(searchCriteria);
            if (handoverDocGenApprovalDetails != null && handoverDocGenApprovalDetails.size() >= 1) {
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public void saveReceivedDocsBookingInfo(String bookId, String orderId, Set<String> documentSettingIds) {
        List<ReceivedDocsBookingInfo> bookingInfo = receivedDocsBookingInfoRepository.searchDetails(new ReceivedDocsBookingInfoSearchCriteria(bookId, orderId));
        if (bookingInfo == null || bookingInfo.size() == 0) {
            ReceivedDocsBookingInfo receivedDocsBookingInfo = new ReceivedDocsBookingInfo();
            receivedDocsBookingInfo.setBookId(bookId);
            receivedDocsBookingInfo.setOrderId(orderId);
            receivedDocsBookingInfo.setDocumentSettingIds(documentSettingIds);
            receivedDocsBookingInfoRepository.saveDetails(receivedDocsBookingInfo);
        } else {
            if (!bookingInfo.iterator().next().getDocumentSettingIds().equals(documentSettingIds)) {
                ReceivedDocsBookingInfo receivedDocsBookingInfo = bookingInfo.iterator().next();
                receivedDocsBookingInfo.getDocumentSettingIds().addAll(documentSettingIds);
                receivedDocsBookingInfoRepository.updateDetails(receivedDocsBookingInfo);
            }
        }
    }

    private ZonedDateTime getTravelEndDate(String bookId) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(bookId);
        List<ZonedDateTime> travelCompletionDate = new ArrayList<>();
        for (OpsProduct product : opsBooking.getProducts()) {
            ZonedDateTime dateTime = getTravelCompletionDate(product);
            if (dateTime != null)
                travelCompletionDate.add(dateTime);
        }
        if (travelCompletionDate != null && travelCompletionDate.size() >= 1)
            Collections.sort(travelCompletionDate);
        return travelCompletionDate.get(travelCompletionDate.size() - 1);
    }

    private ZonedDateTime getTravelCompletionDate(OpsProduct product) {
        ZonedDateTime travelCompletionDate = null;
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                travelCompletionDate = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getArrivalDateZDT();
                break;

            case PRODUCT_SUB_CATEGORY_HOTELS:
                travelCompletionDate = DateTimeUtil.formatBEDateTimeZone(product.getOrderDetails().getHotelDetails().getRooms().iterator().next().getCheckOut());
                break;

            default:
                break;
        }
        return travelCompletionDate;
    }

    private void createTodoTaskAndSendAlertToOpsUserToVerifyDocs(DocumentsResource documentsResource) {
        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName("VERIFY_OR_REJECT_DOCS_RECVD");
            inlineMessageResource.setNotificationType("System");
            ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
            entity.put("documentName", documentsResource.getDocumentName()); //set this before calling
            entity.put("orderID", documentsResource.getOrderId());
            entity.put("bookID", documentsResource.getBookID());
            entity.put("productName", documentsResource.getProductName()); //get this from UI
            entity.put("paxName", documentsResource.getPaxName()); //set this before calling
            inlineMessageResource.setDynamicVariables(entity);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception ex) {
            logger.info("Failed to send alert to Approver to approve the document generation");
        }

        try {
            //add path in todoconfig json
            ToDoTaskResource todo = new ToDoTaskResource();
            todo.setTaskSubTypeId(ToDoTaskSubTypeValues.VERIFY_OR_REJECT_DOCUMENTS.name());
            todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            todo.setTaskNameId(ToDoTaskNameValues.VERIFY.getValue());
            todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
            todo.setBookingRefId(documentsResource.getBookID());
            todo.setReferenceId(documentsResource.getDocumentSettingId());

            if (!checkTodoTaskCreation(todo)) {
                todo.setCreatedByUserId(userService.getLoggedInUserId());
                StringBuilder remarks = new StringBuilder();
                remarks.append("Verify or Reject the documents received for Document Name: ").append(documentsResource.getDocumentName())
                        .append(", ").append("for Product Name: ").append(documentsResource.getProductName()).append(", ").append("for Pax: ").append(documentsResource.getPaxName())
                        .append(", ").append("for Order ").append(documentsResource.getOrderId()).append(" and Booking: ").append(documentsResource.getBookID());
                todo.setRemark(remarks.toString());

                toDoTaskService.save(todo);
            }
        } catch (Exception ex) {
            logger.info("Failed to create todo task for Approver to approve the document generation");
        }
    }

    public boolean checkTriggerEvent(DocumentSetting documentSetting, OpsProduct product, String bookId) throws OperationException, IOException {
        DocumentHandlingGrid documentHandlingGrid = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("documentHandlingGrid.documentSetting._id", documentSetting.getId());
        List<DocumentHandlingGrid> documentHandlingGridList = documentMasterRequirements.getDocumentHandlingGridDetails(jsonObject.toString());
        if (documentHandlingGridList != null && documentHandlingGridList.size() >= 1)
            documentHandlingGrid = documentHandlingGridList.iterator().next();
        if (documentHandlingGrid != null) {
            if (documentHandlingGrid.getTriggerEvent() != null && documentHandlingGrid.getTriggerEvent().getCriteriaType() != null) {
                switch (DocumentCriteria.fromString(documentHandlingGrid.getTriggerEvent().getCriteriaType())) {
                    case CRITERIA_BOOKING_STATUS:
                        if (product.getOrderDetails().getOpsOrderStatus() != null && documentHandlingGrid.getTriggerEvent().getOthers() != null && documentHandlingGrid.getTriggerEvent().getOthers().size() >= 1 && documentHandlingGrid.getTriggerEvent().getOthers().stream().anyMatch(other -> other.getCriteriaName().equals(product.getOrderDetails().getOpsOrderStatus().getProductStatus()))) {
                            if (documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue())) {
                                if (checkMandatoryReceivedDocsStatus(product, bookId)) {
                                    return true;
                                } else return false;
                            } else {
                                //logic for documents to be sent to customer
                                //this implementation is pending as there are no templates for documents to be sent
                                //implementation - generate documents to be sent to customer and mail them
                            }
                        } else return false;
                        break;

                    case CRITERIA_NO_OF_NIGHTS:

                        //logic for documents to be sent to customer
                        //this implementation is pending as there are no templates for documents to be sent
                        //implementation - generate documents to be sent to customer and mail them
                        break;

                    case CRITERIA_PAYMENT_TYPE:
                        break;

                    case CRITERIA_ENQUIRY: //raised query regarding this
                        break;

                    case CRITERIA_QUOTATION: //raised query regarding this
                        break;
                }

            }
        }
        return false;
    }

    private boolean checkMandatoryReceivedDocsStatus(OpsProduct product, String bookId) throws OperationException, IOException {

        List<DocumentHandlingGrid> mdmDocumentResponse = documentMasterRequirements.getDocumentHandlingGridDetails(getDocHandlingMasterSearchCriteria(product));
        List<DocumentSetting> documentSettingList = new ArrayList<>();
        mdmDocumentResponse.stream().forEach(documentHandlingGrid -> documentSettingList.addAll(documentHandlingGrid.getDocumentSetting().stream().filter(documentSetting -> !documentSetting.getDocumentType().equals(DocumentType.HANDOVER_DOCUMENTS.getValue()) && documentSetting.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType()) && documentSetting.getHandoverDocumentMandatory() != null && documentSetting.getHandoverDocumentMandatory()).collect(Collectors.toList())));
        if (documentSettingList != null && documentSettingList.size() >= 1) {
            String result = RestUtils.getForObject(getBookingDocsUrl + bookId, String.class);
            DocumentDetailsResponse documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);
            ReceivedDocsBookingInfo docsBookingInfo = new ReceivedDocsBookingInfo();
            docsBookingInfo.setOrderId(product.getOrderID());
            docsBookingInfo.setBookId(bookId);
            if (documentSettingList.stream().allMatch(documentSetting -> updateReceivedDocsBookingInfoDetails.checkReceivedDocsStatus(docsBookingInfo, documentSetting.getId(), documentDetailsResponse)))
                return true;
            else return false;
        } else return true;
    }

    private boolean isApprovalRequired(DocumentHandlingGrid documentHandlingGrid) {
        if (documentHandlingGrid.getTriggerEvent() != null && documentHandlingGrid.getTriggerEvent().getCriteriaType() != null) {
            if (documentHandlingGrid.getTriggerEvent().getCriteriaType().equals(DocumentCriteria.CRITERIA_PAYMENT_TYPE.getCriteria())) {
                if (documentHandlingGrid.getTriggerEvent().getPayment() != null && documentHandlingGrid.getTriggerEvent().getPayment().size() >= 1 && documentHandlingGrid.getTriggerEvent().getPayment().stream().anyMatch(payment -> payment.getApprovalRequired() != null && payment.getApprovalRequired()))
                    return true;
                else
                    return false;
            } else if (documentHandlingGrid.getTriggerEvent().getCriteriaType().equals(DocumentCriteria.CRITERIA_NO_OF_NIGHTS.getCriteria())) {
                return false;
            } else {
                if (documentHandlingGrid.getTriggerEvent().getOthers() != null && documentHandlingGrid.getTriggerEvent().getOthers().size() >= 1 && documentHandlingGrid.getTriggerEvent().getOthers().stream().anyMatch(other -> other.getApprovalRequired() != null && other.getApprovalRequired()))
                    return true;
                else
                    return false;
            }
        }
        return false;

    }

    public void saveHandoverAndCustomerDocInfo(String bookId, OpsProduct product, DocumentSetting documentSetting) throws JsonProcessingException, OperationException {
        HandoverAndCustomerDocInfo docInfo = new HandoverAndCustomerDocInfo();
        docInfo.setBookId(bookId);
        docInfo.setOrderId(product.getOrderID());
        docInfo.setDocumentSettingId(documentSetting.getId());
        docInfo.setDocumentWise(documentSetting.getDocumentsAsPer());
        docInfo.setDocumentType(documentSetting.getDocumentType());
        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE.getEntityReference())) {
            List<String> roomIds = getListOfRoomDetails(product);
            if (roomIds != null && roomIds.size() >= 1) {
                docInfo.setRoomId(getListOfRoomDetails(product).iterator().next());
            }
        }

        if (!documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference())) {
            docInfo.setPaxId(getListOfPaxDetails(product, true).iterator().next());
            HandoverAndCustomerDocInfoSearchCriteria searchCriteria = new HandoverAndCustomerDocInfoSearchCriteria();
            CopyUtils.copy(docInfo, searchCriteria);
            List<HandoverAndCustomerDocInfo> docInfoList = handoverAndCustomerDocInfoRepository.searchDetails(searchCriteria);
            if (docInfoList == null || docInfoList.size() == 0)
                handoverAndCustomerDocInfoRepository.saveDetails(docInfo);
        } else {
            for (String paxId : getListOfPaxDetails(product, false)) {
                docInfo.setPaxId(paxId);
                HandoverAndCustomerDocInfoSearchCriteria searchCriteria = new HandoverAndCustomerDocInfoSearchCriteria();
                CopyUtils.copy(docInfo, searchCriteria);
                List<HandoverAndCustomerDocInfo> docInfoList = handoverAndCustomerDocInfoRepository.searchDetails(searchCriteria);
                if (docInfoList == null || docInfoList.size() == 0)
                    handoverAndCustomerDocInfoRepository.saveDetails(docInfo);
            }
        }

    }

    public boolean isReconfirmed(OpsBooking aBooking, OpsProduct product){

        ReconfirmationConfiguration reconfirmationConfiguration;
        SupplierReconfirmationFilter supplierReconfirmationFilter = new SupplierReconfirmationFilter();
        supplierReconfirmationFilter.setProductCategory(product.getProductCategory());
        supplierReconfirmationFilter.setProductCatSubtype(product.getProductSubCategory());
        supplierReconfirmationFilter.setSupplierId(product.getSupplierID());
        supplierReconfirmationFilter.setSupplierName(product.getSourceSupplierName());
        try {
            reconfirmationConfiguration = reconfirmationMDMService.getConfiguration(supplierReconfirmationFilter);
        }catch(Exception e){
            logger.warn("Unable to get the MDM reconfirmation Configuration for Supplier" + product.getSourceSupplierName());
            return false;
        }

        ClientReconfirmationDetails clientReconfDetails = clientReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), product.getOrderID());
        if(reconfirmationConfiguration.getConfigFor().equals(ReconfirmationConfigFor.CLIENT)
                || reconfirmationConfiguration.getConfigFor().equals(ReconfirmationConfigFor.CUSTOMER)){
            if(clientReconfDetails.getClientReconfirmationStatus().equals(ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT))
                return true;
        }
        SupplierReconfirmationDetails supplierReconfDetails = supplierReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), product.getOrderID());
        if(reconfirmationConfiguration.getConfigFor().equals(ReconfirmationConfigFor.SUPPLIER)
                || reconfirmationConfiguration.getConfigFor().equals(ReconfirmationConfigFor.SERVICE_PROVIDER)){
            if(supplierReconfDetails.getSupplierReconfirmationStatus().equals(SupplierReconfirmationStatus.RECONFIRMED_BY_SUPPLIER))
                return true;
        }
        if(reconfirmationConfiguration.getConfigFor().equals(ReconfirmationConfigFor.CLIENT_AND_SUPPLIER)){
            if(clientReconfDetails.getClientReconfirmationStatus().equals(ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT) &&
                    supplierReconfDetails.getSupplierReconfirmationStatus().equals(SupplierReconfirmationStatus.RECONFIRMED_BY_SUPPLIER))
                return true;
        }
        return false;
    }


    /*@Override
    public Map<String, String> uploadDocumentsByCustomer(MultipartFile file, DocumentsResource documentsResource) throws RepositoryException {
        documentsResource.setDocumentDetailsId(getDocumentsForBooking(documentsResource.getBookingRefNo()).stream().filter(documentDetails -> documentDetails.getOrderId().equals(documentsResource.getOrderId())).findFirst().get().getId());
        return uploadDocument(file, documentsResource);
        return null;
    }*/

}
