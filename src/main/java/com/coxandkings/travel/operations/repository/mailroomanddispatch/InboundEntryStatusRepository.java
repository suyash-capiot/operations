package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.model.mailroomanddispatch.InboundLogEntryStatus;

public interface InboundEntryStatusRepository {
    public InboundLogEntryStatus saveOrUpdate(InboundLogEntryStatus inboundLogEntryStatus);
}
