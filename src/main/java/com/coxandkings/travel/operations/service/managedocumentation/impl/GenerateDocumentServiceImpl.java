package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.service.managedocumentation.GenerateDocumentService;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.AbstractProductFactory;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl.Accommodation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateDocumentServiceImpl implements GenerateDocumentService {
    @Autowired
    private Accommodation accommodation;

    @Autowired
    private DocBuillder docBuillder;

    @Override
    public void generateHandOverDocument(OpsBooking opsBooking, String documentName, OpsProduct opsProduct, DocumentSetting documentSetting) throws OperationException {

        AbstractProductFactory abstractProductFactory = null;

        String category = opsProduct.getProductCategory();

        switch (category) {
            case "Accommodation":
                abstractProductFactory = accommodation;
                break;
            case "Transport":
                //abstractProductFactory = TransportProduct;
                break;

        }

        docBuillder.build(abstractProductFactory, opsProduct, opsBooking, documentName, documentSetting);
    }
}
