package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentInfoForMails;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentRowItem;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentVersion;
import com.coxandkings.travel.operations.resource.managedocumentation.BookingDocumentDetailsResource;
import com.coxandkings.travel.operations.resource.managedocumentation.DocStatusResource;
import com.coxandkings.travel.operations.resource.managedocumentation.DocumentsResource;
import com.coxandkings.travel.operations.resource.managedocumentation.TotalRevenueAndGrossProfitResource;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DocumentDetailsService {

    public List<DocumentRowItem> uploadDocument(DocumentsResource data) throws RepositoryException, IOException, OperationException, ParseException;

    public List<DocumentRowItem> getDocumentsForBooking(String bookingRefNo) throws IOException, ParseException, OperationException;

    public List<DocumentRowItem> editDocument(DocumentsResource data) throws IOException, RepositoryException, OperationException, ParseException, SQLException;

    public List<DocumentRowItem> reuploadDocument(DocumentsResource data) throws IOException, RepositoryException, OperationException, ParseException;

    public List<DocumentRowItem> revokeDocument(DocumentsResource data) throws IOException, OperationException, ParseException;

    public List<DocumentRowItem> saveDocumentDetails(List<DocumentRowItem> documentDetailsList, String bookID) throws ParseException, OperationException, IOException;

    public BookingDocumentDetailsResource uploadDoc(DocumentsResource documentsResource, BookingDocumentDetailsResource documentDetails) throws OperationException, RepositoryException;

    public List<DocumentVersion> getHistoryOfDocuments(DocumentsResource data) throws IOException, OperationException;

    public List<DocumentRowItem> copyDocument(DocumentsResource documentsResource) throws IOException, ParseException, OperationException, RepositoryException;

    List<String> getListOfRoomDetails(OpsProduct product) throws OperationException;

    public List<DocumentVersion> getDocumentsForOrder(DocumentsResource documentsResource) throws IOException, OperationException;

    public BookingDocumentDetailsResource createBookingDocumentDetailsResource(DocumentsResource documentsResource, ZonedDateTime bookingDate) throws OperationException, RepositoryException, IOException;

    public List<DocumentRowItem> editCopiedHandoverDocuments(DocumentsResource documentsResource) throws IOException, OperationException, ParseException;

    public List<DocumentRowItem> setCommDetailsForDocuments(DocumentsResource documentsResource) throws IOException, OperationException, ParseException;

    public List<DocumentRowItem> addRemarksToDocs(DocumentsResource documentsResource) throws ParseException, OperationException, IOException;

    public void setCopyToCustomerForDocuments(DocumentsResource documentsResource) throws IOException, OperationException;

    public DocumentInfoForMails getDocumentInfoForMails(DocumentsResource documentsResource) throws IOException, OperationException;

    public List<DocStatusResource> getReceivedDocumentsStatus();

    public List<DocStatusResource> getSentDocumentsStatus();

    public List<DocStatusResource> getHandoverDocumentsStatus();

    public void updateOrderLevelDocuments(DocumentsResource documentsResource, List<BookingDocumentDetailsResource> documentInfo) throws OperationException;

    public void updatePaxLevelDocuments(DocumentsResource documentsResource, List<BookingDocumentDetailsResource> documentInfo);

    public void updateRoomLevelDocuments(DocumentsResource documentsResource, List<BookingDocumentDetailsResource> documentInfo);

    public List<String> getListOfPaxDetails(OpsProduct product, boolean leadPax) throws JsonProcessingException;

    public void createToDoTaskAndSendAlertToAdminForDocumentConfiguration(OpsProduct product, OpsBooking opsBooking, String referenceId);

    public void sendAlertAndCreateTodoTaskForOpsUserToCollectDocuments(String bookId, String orderId, String productName);

    public String getProductName(String bookId, String orderId) throws OperationException;

    public Map<String, Object> generateHandoverDocs(DocumentsResource documentsResource) throws ParseException, OperationException, IOException;

    public String sendEditedDocsToCustomer(DocumentsResource documentsResource, OpsBooking opsBooking) throws SQLException, OperationException, IOException;

    public void saveCutOffDateConfigurationDetails(DocumentSetting documentSetting, OpsBooking aBooking, OpsProduct product) throws OperationException;

    public void saveMDMUnConfiguredDataDetails(OpsProduct product, OpsBooking opsBooking);

    public Map<String, Object> sendApprovalRequestToGenerateHandoverDocuments(DocumentsResource documentsResource) throws OperationException, IOException, ParseException;

    public DocumentsResource getHandoverDocApprovalDetails(String id) throws OperationException;

    public TotalRevenueAndGrossProfitResource getTotalRevenueAndTotalGrossProfitValues(TotalRevenueAndGrossProfitResource revenueAndGrossProfitResource) throws OperationException;

    public Map<String, String> approveHandoverDocGeneration(DocumentsResource resource) throws ParseException, OperationException, IOException;

    public Map<String, String> rejectHandoverDocGeneration(DocumentsResource resource);

    public String getDocHandlingMasterSearchCriteria(OpsProduct product);

    public DocumentStatus getDocumentStatus(DocumentSetting documentSetting, List<BookingDocumentDetailsResource> bookingDocumentDetailsResources);

    public void saveReceivedDocsBookingInfo(String bookId, String orderId, Set<String> documentSettingIds);

    public boolean checkTriggerEvent(DocumentSetting documentSetting, OpsProduct product, String bookId) throws OperationException, IOException;

    public void saveHandoverAndCustomerDocInfo(String bookId, OpsProduct product, DocumentSetting documentSetting) throws JsonProcessingException, OperationException;

    public boolean isReconfirmed(OpsBooking aBooking, OpsProduct product);

    //public Map<String,String> uploadDocumentsByCustomer(MultipartFile file, DocumentsResource documentsResource) throws RepositoryException;

}
