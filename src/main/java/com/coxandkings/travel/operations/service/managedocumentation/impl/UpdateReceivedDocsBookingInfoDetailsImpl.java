package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentCommunicationType;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentEntityReference;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.managedocumentation.ReceivedDocsBookingInfo;
import com.coxandkings.travel.operations.repository.managedocumentation.ReceivedDocsBookingInfoRepository;
import com.coxandkings.travel.operations.resource.managedocumentation.*;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentDetailsService;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentMasterRequirements;
import com.coxandkings.travel.operations.service.managedocumentation.UpdateReceivedDocsBookingInfoDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateReceivedDocsBookingInfoDetailsImpl implements UpdateReceivedDocsBookingInfoDetails {

    private static final Logger logger = LogManager.getLogger(ManageDocumentationBatchJobServiceImpl.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DocumentMasterRequirements documentMasterRequirements;
    @Autowired
    private OpsBookingService opsBookingService;
    @Autowired
    private DocumentDetailsService documentDetailsService;
    @Autowired
    private ReceivedDocsBookingInfoRepository receivedDocsBookingInfoRepository;

    public boolean checkReceivedDocsStatus(ReceivedDocsBookingInfo bookingInfo, String documentSettingId, DocumentDetailsResponse documentDetailsResponse) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("documentHandlingGrid.documentSetting._id", documentSettingId);
            DocumentSetting documentSetting = documentMasterRequirements.getDocumentSettingDetails(jsonObject.toString()).iterator().next();
            if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_BOOKING_WISE.getEntityReference())) {
                OrderDocuments orderDocuments = null;
                try {
                    orderDocuments = documentDetailsResponse.getOrderDocuments().stream().filter(orderDoc -> orderDoc.getOrderId().equals(bookingInfo.getOrderId())).findFirst().get();
                } catch (Exception ex) {
                    logger.info("No document details found for Order " + bookingInfo.getOrderId());
                }
                if (orderDocuments != null && orderDocuments.getDocumentInfo() != null && orderDocuments.getDocumentInfo().size() >= 1) {
                    List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = orderDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSettingId) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getCopyTo() == null).collect(Collectors.toList());
                    if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                        DocumentStatus documentStatus = documentDetailsService.getDocumentStatus(documentSetting, bookingDocumentDetailsResources);
                        return checkDocStatusOfDocuments(documentStatus, documentSetting);
                    } else return false;
                } else return false;
            } else if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_CABIN_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_ROOM_WISE.getEntityReference())) {
                return checkRoomDocs(documentDetailsResponse, documentSetting, bookingInfo);
            } else if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE.getEntityReference()) || documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_PAX_WISE.getEntityReference())) {
                return checkPaxDocs(documentSetting, bookingInfo, documentDetailsResponse);
            }
            return false;
        } catch (Exception ex) {
            logger.info("Failed to check the received docs status");
        }
        return false;
    }

    public boolean checkPaxDocs(DocumentSetting documentSetting, ReceivedDocsBookingInfo bookingInfo, DocumentDetailsResponse documentDetailsResponse) throws OperationException, JsonProcessingException {
        OpsProduct product = opsBookingService.getProduct(bookingInfo.getBookId(), bookingInfo.getOrderId());
        List<String> paxIds;
        if (documentSetting.getDocumentsAsPer().equals(DocumentEntityReference.DOCUMENT_LEAD_PAX_WISE.getEntityReference())) {
            paxIds = documentDetailsService.getListOfPaxDetails(product, true);
        } else
            paxIds = documentDetailsService.getListOfPaxDetails(product, false);

        if (paxIds != null && paxIds.size() >= 1) {
            if (paxIds.stream().allMatch(s -> checkReceivedDocsStatusForPax(s, documentDetailsResponse, documentSetting, bookingInfo)))
                return true;
            else
                return false;
        } else return false;
    }

    public boolean checkReceivedDocsStatusForPax(String paxId, DocumentDetailsResponse documentDetailsResponse, DocumentSetting documentSetting, ReceivedDocsBookingInfo bookingInfo) {
        PaxDocuments paxDocuments = null;
        try {
            paxDocuments = documentDetailsResponse.getPaxDocument().stream().filter(paxDoc -> paxDoc.getPaxID().equals(paxId)).findFirst().get();
        } catch (Exception ex) {
            logger.info("No document details found for Pax " + paxId);
        }
        if (paxDocuments != null && paxDocuments.getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo() != null && paxDocuments.getDocumentInfo().getDocumentInfo().size() >= 1) {
            List<BookingDocumentDetailsResource> bookingDocumentDetailsResources = paxDocuments.getDocumentInfo().getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(bookingInfo.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getCopyTo() == null).collect(Collectors.toList());
            if (bookingDocumentDetailsResources != null && bookingDocumentDetailsResources.size() >= 1) {
                DocumentStatus documentStatus = documentDetailsService.getDocumentStatus(documentSetting, bookingDocumentDetailsResources);
                return checkDocStatusOfDocuments(documentStatus, documentSetting);
            } else return false;
        } else return false;
    }

    private boolean checkRoomDocs(DocumentDetailsResponse documentDetailsResponse, DocumentSetting documentSetting, ReceivedDocsBookingInfo bookingInfo) throws OperationException {
        List<String> roomIds = documentDetailsService.getListOfRoomDetails(opsBookingService.getProduct(bookingInfo.getBookId(), bookingInfo.getOrderId()));
        List<BookingDocumentDetailsResource> documentDetails = new ArrayList<>();
        if (roomIds != null && roomIds.size() >= 1) {
            for (String roomId : roomIds) {
                RoomDocuments roomDocuments = null;
                try {
                    roomDocuments = documentDetailsResponse.getRoomDocument().stream().filter(roomDoc -> roomDoc.getRoomID().equals(roomId)).findFirst().get();
                } catch (Exception ex) {
                    logger.info("No document details found for Room " + roomId);
                }
                if (roomDocuments != null && roomDocuments.getDocumentInfo() != null && roomDocuments.getDocumentInfo().size() >= 1) {
                    documentDetails.addAll(roomDocuments.getDocumentInfo().stream().filter(bookingDocumentDetailsResource -> bookingDocumentDetailsResource.getOrderId().equals(bookingInfo.getOrderId()) && bookingDocumentDetailsResource.getDocumentSettingId().equals(documentSetting.getId()) && bookingDocumentDetailsResource.getActive() && bookingDocumentDetailsResource.getDisqualifyDocumentId() == null && bookingDocumentDetailsResource.getCopyTo() == null).collect(Collectors.toList()));
                }
            }
            if (documentDetails != null && documentDetails.size() >= 1) {
                DocumentStatus documentStatus = documentDetailsService.getDocumentStatus(documentSetting, documentDetails);
                return checkDocStatusOfDocuments(documentStatus, documentSetting);
            } else return false;

        } else return false;

    }

    private boolean checkDocStatusOfDocuments(DocumentStatus documentStatus, DocumentSetting documentSetting) {
        if (documentSetting.getDocumentBy().equals(DocumentCommunicationType.CUSTOMER_TO_COMPANY.getCommunicationType())) {
            if (!documentStatus.equals(DocumentStatus.VERIFIED)) {
                return false;
            } else {
                return true;
            }
        } else if (documentSetting.getDocumentBy().equals(DocumentCommunicationType.SUPPLIER_TO_COMPANY.getCommunicationType())) {
            if (!documentStatus.equals(DocumentStatus.RECEIVED)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (!documentStatus.equals(DocumentStatus.SENT)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
