package com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.AbstractProductFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Accommodation implements AbstractProductFactory {
    @Autowired
    private HotelDocument hotelDocument;

    @Override
    public File GenerateDocument(OpsProduct opsProduct, OpsBooking opsBooking, String documentName) throws OperationException {
        switch (documentName) {
            case "Hotel Voucher":
                return hotelDocument.generateHotelVoucher(opsBooking, opsProduct);


            default:
                throw new OperationException(documentName + " Document name not match");

        }

    }
}
