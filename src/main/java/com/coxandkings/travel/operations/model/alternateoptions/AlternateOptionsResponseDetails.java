package com.coxandkings.travel.operations.model.alternateoptions;

import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Entity
@Table(name = "ALTERNATEOPTIONSRESPONSEDETAILS")
@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
public class AlternateOptionsResponseDetails {

    @Id
    @Column(name="id")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String id;

    @Column
    private String leadPaxName;

    @Column
    private String productCategory;

    @Column
    private String tourStartDate;

    @Column
    private String tourEndDate;
    
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "createdOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime alternateOptionSentDate;
    
    //Price Details
    @Column
    private String failedBookingPrice;
    
    @Column
    private String alternateOptionPrice;
    
    @Column
    private boolean isPayable;
    
    @Column
    private String bookID;
    
    @Column
    private String orderID;
    
    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String alternateOptionDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "configurationId")
    @JsonIgnore
    private AlternateOptionsV2 alternateOptions;
    
    @Column
    private String status; 

    public String getLeadPaxName() {
        return leadPaxName;
    }

    public void setLeadPaxName(String leadPaxName) {
        this.leadPaxName = leadPaxName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getTourStartDate() {
        return tourStartDate;
    }

    public void setTourStartDate(String tourStartDate) {
        this.tourStartDate = tourStartDate;
    }

    public String getTourEndDate() {
        return tourEndDate;
    }

    public void setTourEndDate(String tourEndDate) {
        this.tourEndDate = tourEndDate;
    }

    public AlternateOptionsV2 getAlternateOptions() {
        return alternateOptions;
    }

    public void setAlternateOptions(AlternateOptionsV2 alternateOptions) {
        this.alternateOptions = alternateOptions;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailedBookingPrice() {
      return failedBookingPrice;
    }

    public void setFailedBookingPrice(String failedBookingPrice) {
      this.failedBookingPrice = failedBookingPrice;
    }

    public String getAlternateOptionPrice() {
      return alternateOptionPrice;
    }

    public void setAlternateOptionPrice(String alternateOptionPrice) {
      this.alternateOptionPrice = alternateOptionPrice;
    }

    public boolean isPayable() {
      return isPayable;
    }

    public void setPayable(boolean isPayable) {
      this.isPayable = isPayable;
    }

    public String getAlternateOptionDetails() {
      return alternateOptionDetails;
    }

    public void setAlternateOptionDetails(String alternateOptionDetails) {
      this.alternateOptionDetails = alternateOptionDetails;
    }

    public ZonedDateTime getAlternateOptionSentDate() {
      return alternateOptionSentDate;
    }

    public void setAlternateOptionSentDate(ZonedDateTime alternateOptionSentDate) {
      this.alternateOptionSentDate = alternateOptionSentDate;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getBookID() {
      return bookID;
    }

    public void setBookID(String bookID) {
      this.bookID = bookID;
    }

    public String getOrderID() {
      return orderID;
    }

    public void setOrderID(String orderID) {
      this.orderID = orderID;
    }
    
    

}
