package com.coxandkings.travel.operations.service.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.ProductReviewCriteria;
import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductReview;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewByUser;
import com.coxandkings.travel.operations.resource.prodreview.*;
import com.coxandkings.travel.operations.response.prodreview.ProductReviewResposneClient;
import com.coxandkings.travel.operations.response.prodreview.ReviewFormSubmittedResponse;
import com.coxandkings.travel.operations.response.prodreview.ReviewResponse;
import org.json.JSONObject;

import javax.management.OperationsException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ProductReviewService {
    ProductReview saveAndUpdate(ProductReviewResource productReviewResource) throws OperationException;

    ProductReview updateProductReview(ReviewResponse reviewResponse) throws OperationException;

    SearchProductReviewResponse getManageProductCriteria(ProductReviewCriteria productReviewCriteria);

    ProductReview getProductReviewById(String productReviewTemplateId) throws OperationException;

    String changeProductReviewStatus(ProductReviewStatusResource productReviewStatusResource) throws OperationException, OperationsException;

    String submitReview(CommentResource commentResource) throws OperationException;

    //String deleteReview(String reviewId) throws OperationException;

    //Map<String, String> submit(ReviewResponse reviewResponse) throws ParseException, OperationException;

    ProductReview updateStatus(String id, ProdReviewStatus prodReviewStatus) throws OperationException;

    Map sendMailToCustomer(ClientMailResource clientMailResource) throws OperationException;

    ReviewResponse getUIResponse(String id);

    List<String> getCompanyByUser(String userID);

    ProductReviewResposneClient getProductReviewByUserId(ProductReviewByUser productReviewByUser);

}
