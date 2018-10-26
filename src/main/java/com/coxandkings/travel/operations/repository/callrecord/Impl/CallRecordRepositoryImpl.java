package com.coxandkings.travel.operations.repository.callrecord.Impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.model.callrecord.CallRecord;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import com.coxandkings.travel.operations.repository.callrecord.CallRecordRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository("callRecords")
@org.springframework.transaction.annotation.Transactional(readOnly = false)
public class CallRecordRepositoryImpl extends SimpleJpaRepository<CallRecord, String > implements CallRecordRepository {

    private EntityManager entityManager;
    public CallRecordRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(CallRecord.class, em);
        entityManager=em;
    }
    @Override
    public CallRecord saveOrUpdateCallRecords(CallRecord callRecord) {
        return this.saveAndFlush(callRecord);
    }

    @Override
    public CallRecord getCallRecordsById(String id) {
        CallRecord callRecord =this.findOne(id);
        return callRecord;
    }

    @Override
    public List<? extends BaseCommunication> getByCriteria(CommunicationCriteria criteria) {
        return findAll();
    }

}
