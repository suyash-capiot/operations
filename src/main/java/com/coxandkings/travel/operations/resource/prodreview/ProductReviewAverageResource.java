package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.model.prodreview.QuestionAvgCount;

import java.math.BigInteger;
import java.util.Set;

public class ProductReviewAverageResource {
    private String id;
    private String companyID;
    private String productCategory;
    private String productCategorySubType;
    private Set<QuestionAvgCount> questionAvgCounts;
    private Double aggregate;
    private BigInteger count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public Double getAggregate() {
        return aggregate;
    }

    public void setAggregate(Double aggregate) {
        this.aggregate = aggregate;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Set<QuestionAvgCount> getQuestionAvgCounts() {
        return questionAvgCounts;
    }

    public void setQuestionAvgCounts(Set<QuestionAvgCount> questionAvgCounts) {
        this.questionAvgCounts = questionAvgCounts;
    }
}
