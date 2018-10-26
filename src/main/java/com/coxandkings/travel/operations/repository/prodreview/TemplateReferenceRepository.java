package com.coxandkings.travel.operations.repository.prodreview;

import com.coxandkings.travel.operations.model.prodreview.ProductReviewTemplateReference;

public interface TemplateReferenceRepository {
    ProductReviewTemplateReference saveOrUpdate(ProductReviewTemplateReference productReviewTemplateReference);

    ProductReviewTemplateReference getTemplateReferenceById(String bookingReferenceNo);
}
