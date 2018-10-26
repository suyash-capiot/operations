package com.coxandkings.travel.operations.service.fullcancellation;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderCancellation;

import java.io.UnsupportedEncodingException;

public interface BEService {
    OrderCancellation getCancellations(KafkaBookingMessage kafkaBookingMessage) throws OperationException;

    public void updateOrderStatus(OpsProductCategory productCategory, OpsProductSubCategory opsProductSubCategory, OpsOrderStatus orderStatus, String orderId) throws OperationException, UnsupportedEncodingException;

    void updateInventoryStatus(KafkaBookingMessage kafkaBookingMessage);
}
