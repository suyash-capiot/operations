package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.InboundEntryCriteria;
import com.coxandkings.travel.operations.model.mailroomanddispatch.InboundEntry;
import com.coxandkings.travel.operations.model.mailroomanddispatch.InboundRecipientDetails;

import java.util.List;
import java.util.Map;

public interface InboundEntryRepository {

    public InboundEntry saveOrUpdate(InboundEntry inboundEntry);
    public Map<String, Object> getByInboundCriteria(InboundEntryCriteria maiRoomCriteria);
    public InboundEntry getInboundId(String inboundId);
    public List<InboundRecipientDetails> suggest(String attribute, String value);

    public List<String> inboundNoList(String param);

    public List<String> awbNo();

    public List<String> senderNames();

    public List<String> recipientNames();

    public List<String> departments();

}
