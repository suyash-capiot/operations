package com.coxandkings.travel.operations.service.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.ProductAverageCriteria;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewAverage;
import com.coxandkings.travel.operations.resource.prodreview.ProductReviewAverageResource;

import java.util.List;

public interface ProductReviewAverageService {
    ProductReviewAverage saveAndUpdate(ProductReviewAverageResource productReviewAverageResource);

    ProductReviewAverage getProductReviewAverage(String id);

    List<ProductReviewAverage> getByCriteria(ProductAverageCriteria productAverageCriteria);

}
