package com.coxandkings.travel.operations.repository.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.ProductAverageCriteria;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewAverage;

import java.util.List;

public interface ProductReviewAverageRepository {
    ProductReviewAverage saveAndUpdate(ProductReviewAverage productReviewAverage);

    ProductReviewAverage get(String Id);

    List<ProductReviewAverage> getByCriteria(ProductAverageCriteria productAverageCriteria);

}
