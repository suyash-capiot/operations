package com.coxandkings.travel.operations.model.prodreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

public class ProductTemplateReference {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    private String templateId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uniqueReference")
    @JsonIgnore
    private ProductReviewTemplateReference productReviewTemplateReference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ProductReviewTemplateReference getProductReviewTemplateReference() {
        return productReviewTemplateReference;
    }

    public void setProductReviewTemplateReference(ProductReviewTemplateReference productReviewTemplateReference) {
        this.productReviewTemplateReference = productReviewTemplateReference;
    }
}
