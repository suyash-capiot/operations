package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;

public interface GenerateDocumentService {
    public void generateHandOverDocument(OpsBooking opsBooking, String documentName, OpsProduct opsProduct, DocumentSetting documentSetting) throws OperationException;
}
