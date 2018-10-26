package com.coxandkings.travel.operations.controller.managedocumentation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentInfoForMails;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentRowItem;
import com.coxandkings.travel.operations.model.managedocumentation.DocumentVersion;
import com.coxandkings.travel.operations.resource.managedocumentation.DocStatusResource;
import com.coxandkings.travel.operations.resource.managedocumentation.DocumentsResource;
import com.coxandkings.travel.operations.resource.managedocumentation.TotalRevenueAndGrossProfitResource;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/documents")
public class ManageDocumentationController {

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @GetMapping(value = "/v1/searchByBookingRefNo/{bookID}")
    public ResponseEntity<List<DocumentRowItem>> getDocumentDetailsByBookingRefNo(@PathVariable("bookID") String bookID) throws IOException, ParseException, OperationException {
        return new ResponseEntity<>(documentDetailsService.getDocumentsForBooking(bookID), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/upload")
    public ResponseEntity<List<DocumentRowItem>> uploadDocuments(@RequestBody DocumentsResource data) throws RepositoryException, IOException, OperationException, ParseException {
        return new ResponseEntity<>(documentDetailsService.uploadDocument(data), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/save")
    public ResponseEntity<List<DocumentRowItem>> saveDocumentDetails(@RequestBody List<DocumentRowItem> documentDetailsList, @RequestParam("bookID") String bookID) throws ParseException, OperationException, IOException {
        return new ResponseEntity<>(documentDetailsService.saveDocumentDetails(documentDetailsList, bookID), HttpStatus.OK);
    }

    @PutMapping(value = "/v1/edit")
    public ResponseEntity<List<DocumentRowItem>> editDocument(@RequestBody DocumentsResource data) throws IOException, RepositoryException, OperationException, ParseException, SQLException {
        return new ResponseEntity<>(documentDetailsService.editDocument(data), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/re-upload")
    public ResponseEntity<List<DocumentRowItem>> reuploadDocument(@RequestBody DocumentsResource data) throws IOException, RepositoryException, OperationException, ParseException {
        return new ResponseEntity<>(documentDetailsService.reuploadDocument(data), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/revoke")
    public ResponseEntity<List<DocumentRowItem>> revokeDocument(@RequestBody DocumentsResource data) throws IOException, OperationException, ParseException {
        return new ResponseEntity<>(documentDetailsService.revokeDocument(data), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/copy")
    public ResponseEntity<List<DocumentRowItem>> copyDocument(@RequestBody DocumentsResource documentsResource) throws ParseException, OperationException, IOException, RepositoryException {
        return new ResponseEntity<>(documentDetailsService.copyDocument(documentsResource), HttpStatus.OK);
    }

    @PutMapping(value = "/v1/copiedDocs/edit")
    public ResponseEntity<List<DocumentRowItem>> editCopiedHandoverDocs(@RequestBody DocumentsResource documentsResource) throws OperationException, ParseException, IOException {
        return new ResponseEntity<>(documentDetailsService.editCopiedHandoverDocuments(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/history")
    public ResponseEntity<List<DocumentVersion>> getHistoryOfDocuments(@RequestBody DocumentsResource documentsResource) throws IOException, OperationException {
        return new ResponseEntity<>(documentDetailsService.getHistoryOfDocuments(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/getOrderDocs")
    public ResponseEntity<List<DocumentVersion>> getDocumentsForOrder(@RequestBody DocumentsResource documentsResource) throws IOException, OperationException {
        return new ResponseEntity<>(documentDetailsService.getDocumentsForOrder(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/updateCommDetails")
    public ResponseEntity<List<DocumentRowItem>> updateCommunicationDetails(@RequestBody DocumentsResource documentsResource) throws OperationException, ParseException, IOException {
        return new ResponseEntity<>(documentDetailsService.setCommDetailsForDocuments(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/addRemarks")
    public ResponseEntity<List<DocumentRowItem>> addRemarksToDocs(@RequestBody DocumentsResource documentsResource) throws ParseException, OperationException, IOException {
        return new ResponseEntity<>(documentDetailsService.addRemarksToDocs(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/getDocsAndEmailInfo")
    public ResponseEntity<DocumentInfoForMails> getDocsAndTemplateInfo(@RequestBody DocumentsResource documentsResource) throws IOException, OperationException {
        return new ResponseEntity<>(documentDetailsService.getDocumentInfoForMails(documentsResource),HttpStatus.OK);
    }

    @GetMapping(value = "/v1/receiveDoc/status")
    public ResponseEntity<List<DocStatusResource>> getDocStatusForReceivedDocuments() {
        return new ResponseEntity<>(documentDetailsService.getReceivedDocumentsStatus(),HttpStatus.OK);
    }

    @GetMapping(value = "/v1/sendDoc/status")
    public ResponseEntity<List<DocStatusResource>> getDocStatusForSentDocuments() {
        return new ResponseEntity<>(documentDetailsService.getSentDocumentsStatus(),HttpStatus.OK);
    }

    @GetMapping(value = "/v1/handoverDoc/status")
    public ResponseEntity<List<DocStatusResource>> getDocStatusForHandoverDocuments() {
        return new ResponseEntity<>(documentDetailsService.getHandoverDocumentsStatus(),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/getDocAndEmailInfo")
    public ResponseEntity<DocumentInfoForMails> getDocumentsAndEmailInfoForOrder(@RequestBody DocumentsResource documentsResource) throws IOException, OperationException {
        return new ResponseEntity<>(documentDetailsService.getDocumentInfoForMails(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/generate")
    public ResponseEntity<Map<String, Object>> generateHandoverDocuments(@RequestBody DocumentsResource documentsResource) throws ParseException, OperationException, IOException {
        return new ResponseEntity<>(documentDetailsService.generateHandoverDocs(documentsResource),HttpStatus.OK);
    }

    @PostMapping(value = "/v1/handoverDocGen/sendApprovalRequest")
    public ResponseEntity<Map<String, Object>> sendApprovalRequestForHandoverDocGeneration(@RequestBody DocumentsResource documentsResource) throws OperationException, IOException, ParseException {
        return new ResponseEntity<>(documentDetailsService.sendApprovalRequestToGenerateHandoverDocuments(documentsResource), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/handoverDocGenInfo/{id}")
    public ResponseEntity<DocumentsResource> getHandoverDocGenDetails(@PathVariable("id") String id) throws OperationException {
        return new ResponseEntity<>(documentDetailsService.getHandoverDocApprovalDetails(id), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/getB2BClientInfo")
    public ResponseEntity<TotalRevenueAndGrossProfitResource> getTotalRevenueAndGrossProfitInfo(@RequestBody TotalRevenueAndGrossProfitResource resource) throws OperationException {
        return new ResponseEntity<>(documentDetailsService.getTotalRevenueAndTotalGrossProfitValues(resource), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/handoverDocGen/approve")
    public ResponseEntity<Map<String, String>> approveHandoverDocGeneration(@RequestBody DocumentsResource resource) throws ParseException, OperationException, IOException {
        return new ResponseEntity<>(documentDetailsService.approveHandoverDocGeneration(resource), HttpStatus.OK);
    }

    @PostMapping(value = "/v1/handoverDocGen/reject")
    public ResponseEntity<Map<String, String>> rejectHandoverDocGeneration(@RequestBody DocumentsResource resource) {
        return new ResponseEntity<>(documentDetailsService.rejectHandoverDocGeneration(resource), HttpStatus.OK);
    }

}
