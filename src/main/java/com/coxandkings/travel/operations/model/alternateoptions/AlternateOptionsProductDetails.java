package com.coxandkings.travel.operations.model.alternateoptions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
@Component
@Entity
@Table(name="ALTERNATEOPTIONSPRODUCTDETAILS")
public class AlternateOptionsProductDetails {

	public AlternateOptionsProductDetails(String id,String ConfigurationID) {
      this.id = id;
	  this.setAlternateOptions(new AlternateOptionsV2());
	  this.alternateOptions.setConfigurationId(ConfigurationID);
	}
	
	public AlternateOptionsProductDetails() {
	}
	@Id
	@Column(name="id")
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy = "uuid")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="configurationId")
	@JsonIgnore
	private AlternateOptionsV2 alternateOptions;
	
	@Column
	private String productCategory;
	
	@Column
	private String productCategorySubType;

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AlternateOptionsV2 getAlternateOptions() {
		return alternateOptions;
	}

	public void setAlternateOptions(AlternateOptionsV2 alternateOptions) {
		this.alternateOptions = alternateOptions;
	}
	
	

  public String getProductCategorySubType() {
    return productCategorySubType;
  }

  public void setProductCategorySubType(String productCategorySubType) {
    this.productCategorySubType = productCategorySubType;
  }

  @Override
	public String toString() {
		return this.productCategory+" "+this.productCategorySubType;
	}
	
}
