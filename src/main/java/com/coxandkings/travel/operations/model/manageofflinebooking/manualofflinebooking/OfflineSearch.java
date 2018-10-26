package com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking;

import com.coxandkings.travel.operations.model.BaseModel;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class)})
public class OfflineSearch extends BaseModel implements Serializable{
    private static final long serialVersionUID = -8785127195982088821L;

    @Column(name = "bookRefNumber")
    private String bookRefNumber;

    @Column(name = "productCategory")
    private String productCategory;

    @Column(name = "productSubCategory")
    private String productSubCategory;

    @Column(name = "productName")
    private String productName;

    @Column(name = "supplierName")
    private String supplierName;

    @Column(name = "clientType")
    private String clienttype;

    @Column(name = "bookingStatus")
    private String bookingStatus;

    @Column(name = "clientName")
    private String clientName;

    @Column(name = "leadPaxName")
    private String leadPaxName;

    @Column(name = "productId")
    private String productId;

    @Column(name = "companyMarket")
    private String companyMarket;



    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    //    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "searchid")
//    private OfflineProducts offlineProducts;
//
//    public OfflineProducts getOfflineProducts() {
//        return offlineProducts;
//    }
//
//    public void setOfflineProducts(OfflineProducts offlineProducts) {
//        this.offlineProducts = offlineProducts;
//    }


    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookRefNumber() {
        return bookRefNumber;
    }

    public void setBookRefNumber(String bookRefNumber) {
        this.bookRefNumber = bookRefNumber;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }



    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLeadPaxName() {
        return leadPaxName;
    }

    public void setLeadPaxName(String leadPaxName) {
        this.leadPaxName = leadPaxName;
    }
}
