package com.coxandkings.travel.operations.service.prodreview;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewTemplateReference;

import java.util.List;


public interface TemplateReferenceService {
    List<String> saveAndUpdate(List<ProductReviewTemplateReference> productReviewTemplateReferences) throws OperationException;

    ProductReviewTemplateReference getTemplateReferenceById(String referenceNo);
}
