package com.coxandkings.travel.operations.repository.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.ProductReviewCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductReview;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewByUser;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewVersion;
import com.coxandkings.travel.operations.resource.prodreview.SearchProductReviewResponse;
import com.coxandkings.travel.operations.response.prodreview.ProductReviewResposneClient;
import org.json.JSONObject;

import java.util.List;

public interface ProductReviewRepository {
    ProductReview saveOrUpdate(ProductReview productReview);

    SearchProductReviewResponse getManageProductCriteria(ProductReviewCriteria productReviewCriteria);

    ProductReview getReviewId(ProductReviewVersion productReviewVersion);

    ProductReview getReviewByLatestVersion(String id);

    ProductReviewResposneClient getProductReviewsByUserId(ProductReviewByUser productReviewByUser);

    //Boolean getExists(String id);

    //void deleteProductReview(String reviewId) throws OperationException;

}
