package com.coxandkings.travel.operations.resource.thirdpartyVoucher;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.PaymentStatusToReleaseVoucher;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.CommunicationTemplateOfCustomer;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.CommunicationTemplateOfSupplier;

import java.util.Set;

public class UpdateSupplierConfigResource {

    private String id;
    private Integer noOfDaysToReleaseVoucher;
    private Integer noOfDaysToSendAlarm;
    private PaymentStatusToReleaseVoucher paymentStatusToReleaseVoucher;
    private String customerTemplateId;
    private String supplierTemplateId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNoOfDaysToReleaseVoucher() {
        return noOfDaysToReleaseVoucher;
    }

    public void setNoOfDaysToReleaseVoucher(Integer noOfDaysToReleaseVoucher) {
        this.noOfDaysToReleaseVoucher = noOfDaysToReleaseVoucher;
    }

    public Integer getNoOfDaysToSendAlarm() {
        return noOfDaysToSendAlarm;
    }

    public void setNoOfDaysToSendAlarm(Integer noOfDaysToSendAlarm) {
        this.noOfDaysToSendAlarm = noOfDaysToSendAlarm;
    }

    public PaymentStatusToReleaseVoucher getPaymentStatusToReleaseVoucher() {
        return paymentStatusToReleaseVoucher;
    }

    public void setPaymentStatusToReleaseVoucher(PaymentStatusToReleaseVoucher paymentStatusToReleaseVoucher) {
        this.paymentStatusToReleaseVoucher = paymentStatusToReleaseVoucher;
    }

    public String getCustomerTemplateId() {
        return customerTemplateId;
    }

    public void setCustomerTemplateId(String customerTemplateId) {
        this.customerTemplateId = customerTemplateId;
    }

    public String getSupplierTemplateId() {
        return supplierTemplateId;
    }

    public void setSupplierTemplateId(String supplierTemplateId) {
        this.supplierTemplateId = supplierTemplateId;
    }
}
