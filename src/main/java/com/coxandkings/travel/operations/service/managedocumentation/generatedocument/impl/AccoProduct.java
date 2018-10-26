package com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.Document;

import java.io.File;

public class AccoProduct implements Document {
    HotelDocument hotelDocument;

    @Override
    public File generate(OpsBooking opsBooking, OpsProduct opsProduct, String documentName) throws OperationException {
        switch (documentName) {
            case "Hotel Voucher":
                return hotelDocument.generateHotelVoucher(opsBooking,opsProduct);

            case "Cancel Hotel Booking":
                //return hotel.cancelHotelBooking(opsBooking);
                break;

        }
        return null;
    }
}
