package com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.enums.prepaymenttosupplier.ServiceOrderValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PaymentAdviceOrderInfo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentAdviceOrderInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "bookingReferenceId")
    private String bookingRefId;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "serviceOrderId")
    private String serviceOrderId;

    @Column(name = "serviceOrderValue")
    private ServiceOrderValue serviceOrderValue;

    @Column(name = "orderLevelNetPayableToSupplier")
    private BigDecimal orderLevelNetPayableToSupplier;

    @Column(name = "orderLevelamountPayableForSupplier")
    private BigDecimal orderLevelAmountPayableForSupplier;

    @Column(name = "balanceAmtPayableToSupplier")
    private BigDecimal orderLevelBalanceAmtPayableToSupplier;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public BigDecimal getOrderLevelNetPayableToSupplier() {
        return orderLevelNetPayableToSupplier;
    }

    public void setOrderLevelNetPayableToSupplier(BigDecimal orderLevelNetPayableToSupplier) {
        this.orderLevelNetPayableToSupplier = orderLevelNetPayableToSupplier;
    }

    public BigDecimal getOrderLevelAmountPayableForSupplier() {
        return orderLevelAmountPayableForSupplier;
    }

    public void setOrderLevelAmountPayableForSupplier(BigDecimal orderLevelAmountPayableForSupplier) {
        this.orderLevelAmountPayableForSupplier = orderLevelAmountPayableForSupplier;
    }

    public BigDecimal getOrderLevelBalanceAmtPayableToSupplier() {
        return orderLevelBalanceAmtPayableToSupplier;
    }

    public void setOrderLevelBalanceAmtPayableToSupplier(BigDecimal orderLevelBalanceAmtPayableToSupplier) {
        this.orderLevelBalanceAmtPayableToSupplier = orderLevelBalanceAmtPayableToSupplier;
    }

    public ServiceOrderValue getServiceOrderValue() {
        return serviceOrderValue;
    }

    public void setServiceOrderValue(ServiceOrderValue serviceOrderValue) {
        this.serviceOrderValue = serviceOrderValue;
    }

    @Override
    public boolean equals( Object anotherPaymentAdviceOrderInfo )   {
        boolean isEqual = false;

        if( anotherPaymentAdviceOrderInfo instanceof PaymentAdviceOrderInfo )   {
            PaymentAdviceOrderInfo anotherInstance = (PaymentAdviceOrderInfo) anotherPaymentAdviceOrderInfo;
            if( anotherInstance.getOrderId().equalsIgnoreCase( orderId ) )  { //comparing order ID itself is unique across bookings!
                isEqual = true;
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode()   {
        return orderId.hashCode();
    }
}

