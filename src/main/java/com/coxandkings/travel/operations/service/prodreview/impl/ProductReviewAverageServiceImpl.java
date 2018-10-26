package com.coxandkings.travel.operations.service.prodreview.impl;

import com.coxandkings.travel.operations.criteria.prodreview.ProductAverageCriteria;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewAverage;
import com.coxandkings.travel.operations.model.prodreview.QuestionAvgCount;
import com.coxandkings.travel.operations.repository.prodreview.ProductReviewAverageRepository;
import com.coxandkings.travel.operations.resource.prodreview.ProductReviewAverageResource;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewAverageService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productAverageReviewService")
public class ProductReviewAverageServiceImpl implements ProductReviewAverageService {


    @Autowired
    private ProductReviewAverageRepository productReviewAverageRepository;

    @Override
    public ProductReviewAverage saveAndUpdate(ProductReviewAverageResource productReviewAverageResource) {
        ProductReviewAverage productReviewAverage = null;
        if (productReviewAverageResource != null && productReviewAverageResource.getId() != null) {
            productReviewAverage = productReviewAverageRepository.get(productReviewAverageResource.getId());
            CopyUtils.copy(productReviewAverageResource, productReviewAverage);
        } else {
            productReviewAverage = new ProductReviewAverage();
            CopyUtils.copy(productReviewAverageResource, productReviewAverage);
            List<QuestionAvgCount> questionAvgCounts = productReviewAverage.getQuestionAvgCounts();
            for (QuestionAvgCount questionAvgCount : questionAvgCounts) {
                questionAvgCount.setProductReviewAverage(productReviewAverage);
            }
            productReviewAverage.setQuestionAvgCounts(questionAvgCounts);
        }
        for (QuestionAvgCount questionAvgCount : productReviewAverage.getQuestionAvgCounts()) {
            questionAvgCount.setProductReviewAverage(productReviewAverage);
        }
        return productReviewAverageRepository.saveAndUpdate(productReviewAverage);
    }

    @Override
    public ProductReviewAverage getProductReviewAverage(String id) {
        return productReviewAverageRepository.get(id);
    }

    @Override
    public List<ProductReviewAverage> getByCriteria(ProductAverageCriteria productAverageCriteria) {
        return productReviewAverageRepository.getByCriteria(productAverageCriteria);
    }


}
