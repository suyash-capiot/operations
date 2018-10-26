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
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity
@Table(name="ALTERNATEOPTIONSCOMPANYDETAILS", uniqueConstraints= @UniqueConstraint(columnNames = {"configurationId","companyMarket","clientType"}))
public class AlternateOptionsCompanyDetails {

  public AlternateOptionsCompanyDetails(String id,String ConfigurationID, String productCategory, String productSubCategory) {
    this.id = id;
    this.setAlternateOptions(new AlternateOptionsV2());
    this.alternateOptions.setConfigurationId(ConfigurationID);
    this.companyMarket = companyMarket;
    this.clientType = clientType;
  }
  
  public AlternateOptionsCompanyDetails() {
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
  private String companyMarket;
  
  @Column
  private String clientType;

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

  public String getCompanyMarket() {
    return companyMarket;
  }

  public void setCompanyMarket(String companyMarket) {
    this.companyMarket = companyMarket;
  }

  public String getClientType() {
    return clientType;
  }

  public void setClientType(String clientType) {
    this.clientType = clientType;
  }
  
}
