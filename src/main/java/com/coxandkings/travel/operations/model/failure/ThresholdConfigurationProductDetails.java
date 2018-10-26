package com.coxandkings.travel.operations.model.failure;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
public class ThresholdConfigurationProductDetails {

    public ThresholdConfigurationProductDetails(String id, String configurationId, String productCategory, String productCategorySubtype) {
        this.id = id;
        this.setThresholdConfiguration(new ThresholdConfiguration());
        this.thresholdConfiguration.setConfigurationId(configurationId);
        this.productCategory = productCategory;
        this.productCategorySubtype = productCategorySubtype;
    }

    public ThresholdConfigurationProductDetails() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "configurationId")
    private ThresholdConfiguration thresholdConfiguration;

    @Column
    private String productCategory;

    @Column
    private String productCategorySubtype;

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubtype() {
        return productCategorySubtype;
    }

    public void setProductCategorySubtype(String productCategorySubtype) {
        this.productCategorySubtype = productCategorySubtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ThresholdConfiguration getThresholdConfiguration() {
        return thresholdConfiguration;
    }

    public void setThresholdConfiguration(ThresholdConfiguration thresholdConfiguration) {
        this.thresholdConfiguration = thresholdConfiguration;
    }
}
