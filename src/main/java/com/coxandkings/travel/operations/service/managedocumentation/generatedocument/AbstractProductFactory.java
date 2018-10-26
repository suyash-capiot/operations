package com.coxandkings.travel.operations.service.managedocumentation.generatedocument;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;

import java.io.File;

public interface AbstractProductFactory {


    File GenerateDocument(OpsProduct opsProduct, OpsBooking opsBooking, String documentName) throws OperationException;
}