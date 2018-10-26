package com.coxandkings.travel.operations.repository.prodreview;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductRating;

public interface ProductRatingRepository {
    public ProductRating saveOrUpdate(ProductRating productReview) throws OperationException;

    public ProductRating getProductId(String productId) throws OperationException;
}
