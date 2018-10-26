package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentCopy;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentUpdateReason;
import com.coxandkings.travel.operations.model.managedocumentation.Document;
import com.coxandkings.travel.operations.resource.managedocumentation.BookingDocumentDetailsResource;
import com.coxandkings.travel.operations.resource.managedocumentation.DocumentsResource;
import com.coxandkings.travel.operations.resource.managedocumentation.PassportResource;
import com.coxandkings.travel.operations.resource.managedocumentation.VisaDocumentResource;
import com.coxandkings.travel.operations.service.managedocumentation.UpdateDocumentDetails;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateDocumentDetailsImpl implements UpdateDocumentDetails {

    @Override
    public BookingDocumentDetailsResource changeDocumentStatus(BookingDocumentDetailsResource documentDetailsResource) {
        documentDetailsResource.setActive(false);
        documentDetailsResource.setReason(DocumentUpdateReason.DOCUMENT_EDITED.getValue());
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource setCopyToCustomer(BookingDocumentDetailsResource documentDetailsResource) {
        documentDetailsResource.setCopyTo(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy());
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource addCommentsToDocument(BookingDocumentDetailsResource documentDetailsResource, String remarks) {
        documentDetailsResource.setRemarks(remarks);
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource updateDocumentStatus(BookingDocumentDetailsResource documentDetailsResource, DocumentStatus documentStatus) {
        documentDetailsResource.setDocumentStatus(documentStatus.getValue());
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource setCommIdForDocuments(BookingDocumentDetailsResource documentDetailsResource, DocumentsResource documentsResource) {
        if (documentDetailsResource.getCommunicationIds() == null) {
            List<String> commIds = new ArrayList<>();
            commIds.add(documentsResource.getCommunicationId());
            documentDetailsResource.setCommunicationIds(commIds);
        } else
            documentDetailsResource.getCommunicationIds().add(documentsResource.getCommunicationId());

        if (documentsResource.getCommunicationTemplate() != null) {
            switch (documentsResource.getCommunicationTemplate()) {

                case COMM_SEND_DOCS_TO_CUST:

                    if(documentDetailsResource.getSendStatus() == null)
                        documentDetailsResource.setSendStatus(DocumentStatus.SENT.getValue());
                    break;

                case COMM_SEND_HNDOVR_DOCS_TO_CUST:

                    if(documentDetailsResource.getCopyTo() == null)
                        documentDetailsResource.setCopyTo(DocumentCopy.DOCUMENT_COPY_CUSTOMER.getDocumentCopy());
                    if(documentDetailsResource.getSendStatus() == null)
                        documentDetailsResource.setSendStatus(DocumentStatus.SENT_TO_CUSTOMER.getValue());
                    break;

                case COMM_COPY_SEND:

                    if(documentDetailsResource.getSendStatus() == null)
                        documentDetailsResource.setSendStatus(DocumentStatus.SENT.getValue());
                    break;

                default:
                    break;
            }
        }
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource revokeDocument(BookingDocumentDetailsResource documentDetailsResource) {
        documentDetailsResource.setActive(false);
        documentDetailsResource.setReason(DocumentUpdateReason.DOCUMENT_REVOKED.getValue());
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource copyPassportAndVisaDetails(BookingDocumentDetailsResource documentDetailsResource, Document document) {
        PassportResource passportResource = new PassportResource();
        VisaDocumentResource visaDocumentResource = new VisaDocumentResource();
        CopyUtils.copy(document, passportResource);
        CopyUtils.copy(document, visaDocumentResource);
        documentDetailsResource.setPassport(passportResource);
        documentDetailsResource.setVisa(visaDocumentResource);
        return documentDetailsResource;
    }

    @Override
    public BookingDocumentDetailsResource changeActiveStatusOfDocs(BookingDocumentDetailsResource documentDetailsResource) {
        documentDetailsResource.setActive(false);
        return documentDetailsResource;
    }
}
