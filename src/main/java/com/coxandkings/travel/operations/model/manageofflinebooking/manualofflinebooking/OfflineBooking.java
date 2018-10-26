package com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking;

import com.coxandkings.travel.operations.model.BaseModel;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "offline_booking")
public class OfflineBooking extends BaseModel implements Serializable {
    private static final long serialVersionUID = -8785127195982088821L;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_sub_category")
    private String productSubCategory;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "company_market")
    private String companyMarket;

    @Column(name = "client_type")
    private String clienttype;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "request_header")
    private String requestHeader;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "create_booking_resp")
    private String createBookingResp;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "clientDetails")
    private String clientDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "paymentDetails")
    private String paymentDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "productDetails")
    private String productDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "travelAndPassengerDetails")
    private String travelAndPassengerDetails;

    @Column(name = "bookId")
    private String bookId;

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

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(String clientDetails) {
        this.clientDetails = clientDetails;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getTravelAndPassengerDetails() {
        return travelAndPassengerDetails;
    }

    public void setTravelAndPassengerDetails(String travelAndPassengerDetails) {
        this.travelAndPassengerDetails = travelAndPassengerDetails;
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

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getCreateBookingResp() {
        return createBookingResp;
    }

    public void setCreateBookingResp(String createBookingResp) {
        this.createBookingResp = createBookingResp;
    }

}
