package com.coxandkings.travel.operations.resource.outbound.be;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingAttribute;
import com.coxandkings.travel.operations.model.core.OpsPaymentInfo;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BookingOverview {
    private String bookingRefId;
    private String invoice;
    private String sapReferenceNumber;
    private String bookingDateTime;
    private String clientReferral;
    private String travelRequestId;
    private String qcStatus;
    private String companyDetails;
    private String clientID;
    private String clientDetails;
    private String clientMobileNumber;
    private String clientEmail;
    private List<ProductOverview> productOverviews;

    private CompanyDetails companyInfo = null;

    private ClientDetails clientContactDetails;

    private String paymentFailureStatus;

    public BookingOverview() {
    }

    public BookingOverview(OpsBooking booking) {
        bookingRefId = booking.getBookID();
        invoice = "";
        sapReferenceNumber = "";
        clientReferral = "";
        travelRequestId = "";
        if (booking.getBookingDateZDT() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ssz");
            ZonedDateTime bookingZDT = booking.getBookingDateZDT();
            bookingDateTime = bookingZDT.format(formatter);
        }

        if (!StringUtils.isEmpty(booking.getQcStatus())) {
            qcStatus = booking.getQcStatus();
        }

        companyInfo = new CompanyDetails();
        companyInfo.setBu(booking.getBu());
        companyInfo.setSbu(booking.getSbu());

        List<OpsPaymentInfo> paymentInfoList = booking.getPaymentInfo();
        if (paymentInfoList != null) {
            for (OpsPaymentInfo opsPaymentInfo : paymentInfoList) {
                if (!StringUtils.isEmpty(opsPaymentInfo.getPayStatus())) {
                    if (opsPaymentInfo.getPayStatus().equalsIgnoreCase("FAILED")) {
                        paymentFailureStatus = OpsBookingAttribute.TF.getBookingAttribute();
                    }
                } else {
                    paymentFailureStatus = "None";
                }
            }
        }


        productOverviews = booking.getProducts().stream().map(ProductOverview::new).collect(Collectors.toList());
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPaymentFailureStatus() {
        return paymentFailureStatus;
    }

    public void setPaymentFailureStatus(String paymentFailureStatus) {
        this.paymentFailureStatus = paymentFailureStatus;
    }

    public String getClientMobileNumber() {
        return clientMobileNumber;
    }

    public void setClientMobileNumber(String clientMobileNumber) {
        this.clientMobileNumber = clientMobileNumber;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public String getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(String clientDetails) {
        this.clientDetails = clientDetails;
    }

    public String getClientReferral() {
        return clientReferral;
    }

    public void setClientReferral(String clientReferral) {
        this.clientReferral = clientReferral;
    }

    public String getTravelRequestId() {
        return travelRequestId;
    }

    public void setTravelRequestId(String travelRequestId) {
        this.travelRequestId = travelRequestId;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getSapReferenceNumber() {
        return sapReferenceNumber;
    }

    public void setSapReferenceNumber(String sapReferenceNumber) {
        this.sapReferenceNumber = sapReferenceNumber;
    }

    public String getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public List<ProductOverview> getProductOverviews() {
        return productOverviews;
    }

    public void setProductOverviews(List<ProductOverview> productOverviews) {
        this.productOverviews = productOverviews;
    }

    public CompanyDetails getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyDetails companyInfo) {
        this.companyInfo = companyInfo;
    }

    public ClientDetails getClientContactDetails() {
        return clientContactDetails;
    }

    public void setClientContactDetails(ClientDetails clientContactDetails) {
        this.clientContactDetails = clientContactDetails;
    }
}
