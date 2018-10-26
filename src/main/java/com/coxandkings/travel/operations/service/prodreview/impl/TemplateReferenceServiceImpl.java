package com.coxandkings.travel.operations.service.prodreview.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewTemplateReference;
import com.coxandkings.travel.operations.repository.prodreview.TemplateReferenceRepository;
import com.coxandkings.travel.operations.service.prodreview.TemplateReferenceService;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("templateReferenceService")
public class TemplateReferenceServiceImpl implements TemplateReferenceService {

    @Value("${review.operations_base_url}")
    private String operationsBaseUrl;

    private static Logger log = LogManager.getLogger(TemplateReferenceServiceImpl.class);
    @Autowired
    private TemplateReferenceRepository templateReferenceRepository;


    @Override
    public List<String> saveAndUpdate(List<ProductReviewTemplateReference> productReviewTemplateReferences) throws OperationException {
        ProductReviewTemplateReference templateReference = null;
        List<String> urls = new ArrayList<>();
        //get the product review URL
        String reviewUrl = operationsBaseUrl;

        for (ProductReviewTemplateReference productReviewTemplateReference : productReviewTemplateReferences) {


            if (!StringUtils.isEmpty(productReviewTemplateReference.getId())) {

                if (log.isDebugEnabled()) {
                    log.debug("Template Reference No: " + productReviewTemplateReference.getId());
                }
                templateReference = templateReferenceRepository.getTemplateReferenceById(productReviewTemplateReference.getId());
                if (log.isDebugEnabled()) {
                    log.debug("Template Reference Details" + templateReference);
                }

                if (templateReference == null) {
                    throw new OperationException("Template Reference not found with Reference no " + productReviewTemplateReference.getId());
                }


            } else {
                templateReference = new ProductReviewTemplateReference();
                templateReferenceRepository.saveOrUpdate(templateReference);

            }
            urls.add(reviewUrl + templateReference.getId());

        }

        return urls;
    }

    @Override
    public ProductReviewTemplateReference getTemplateReferenceById(String referenceNo) {
        return templateReferenceRepository.getTemplateReferenceById(referenceNo);
    }
}
