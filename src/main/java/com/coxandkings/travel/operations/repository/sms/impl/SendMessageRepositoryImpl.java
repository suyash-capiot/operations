package com.coxandkings.travel.operations.repository.sms.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.sms.OutboundSMS;
import com.coxandkings.travel.operations.repository.sms.SendMessageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
@Transactional
@Repository("sendMessage")
public class SendMessageRepositoryImpl extends SimpleJpaRepository<OutboundSMS, String> implements SendMessageRepository {

    private EntityManager entityManager;

    public SendMessageRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(OutboundSMS.class, em);
        entityManager=em;
    }

    @Override
    public OutboundSMS saveMessage(OutboundSMS message) {
        return saveAndFlush(message);
    }

    @Override
    public OutboundSMS getSMSById(String id) throws OperationException {
        return this.findOne(id);
    }
}
