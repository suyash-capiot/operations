package com.coxandkings.travel.operations.repository.doTicketing;

import com.coxandkings.travel.operations.model.doTicketing.DoTicketing;

public interface DoTicketingRepository {
    DoTicketing getById(String id);

    DoTicketing saveOrUpdate(DoTicketing doTicketing);

    DoTicketing update(DoTicketing doTicketing);

    DoTicketing getByBookAndOrderId(String bookID, String orderID);
}
