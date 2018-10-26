package com.coxandkings.travel.operations.model.partpaymentmonitor;

import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.core.OpsBookingAttribute;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "part_payment_info")
public class PartPaymentMonitorEntity extends BaseModel {

    private String bookingID;

    private OpsBookingAttribute paymentStatus;

    private String jobStatus;
}
