package com.coxandkings.travel.operations.model.prodreview;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product_review_average")
public class ProductReviewAverage {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    private String companyID;

    @Column
    private String productCategory;

    @Column
    private String productCategorySubType;

    @Column
    private String productName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_average_count_id")
    private List<QuestionAvgCount> questionAvgCounts;

    @Column
    private double aggregate = 0.0;

    @Column
    private Long count = 0L;

    public ProductReviewAverage() {
        this.id = id;
        this.companyID = companyID;
        this.productCategory = productCategory;
        this.productCategorySubType = productCategorySubType;
        this.productName = productName;
        this.questionAvgCounts = questionAvgCounts;
        this.aggregate = aggregate;
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public double getAggregate() {
        return aggregate;
    }

    public void setAggregate(double aggregate) {
        this.aggregate = aggregate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public List<QuestionAvgCount> getQuestionAvgCounts() {
        return questionAvgCounts;
    }

    public void setQuestionAvgCounts(List<QuestionAvgCount> questionAvgCounts) {
        this.questionAvgCounts = questionAvgCounts;
    }
}
