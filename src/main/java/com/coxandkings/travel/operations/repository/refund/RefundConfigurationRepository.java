package com.coxandkings.travel.operations.repository.refund;

import com.coxandkings.travel.operations.model.refund.RefundConfiguration;

import java.util.List;

public interface RefundConfigurationRepository {
    RefundConfiguration saveOrUpdate(RefundConfiguration refundConfiguration);

    List<RefundConfiguration> findByCriteria(RefundConfiguration refundConfiguration);
}
