package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.io.IOException;

public interface ManageDocumentationBatchJobService {

    public void sendCutOffAlert() throws OperationException;
    public void checkMDMUpdates() throws OperationException;
    public void generateHandoverDocsAndCustomerDocs() throws OperationException, IOException;
    public void checkReceivedDocsStatus();

}
