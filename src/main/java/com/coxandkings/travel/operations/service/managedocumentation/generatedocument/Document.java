package com.coxandkings.travel.operations.service.managedocumentation.generatedocument;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;

import java.io.File;

public interface Document {
    File generate(OpsBooking opsBooking, OpsProduct opsProduct, String documentName) throws OperationException;
}
