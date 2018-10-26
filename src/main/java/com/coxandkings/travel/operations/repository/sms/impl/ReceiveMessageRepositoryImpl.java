package com.coxandkings.travel.operations.repository.sms.impl;

import com.coxandkings.travel.operations.model.sms.InboundSMS;
import com.coxandkings.travel.operations.repository.sms.ReceiveMessageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Transactional
@Repository("receiveMessage")
public class ReceiveMessageRepositoryImpl extends SimpleJpaRepository<InboundSMS, String> implements ReceiveMessageRepository{


    private EntityManager entityManager;

    public ReceiveMessageRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(InboundSMS.class, em);
        entityManager=em;
    }

    @Override
    public InboundSMS saveMessage(InboundSMS inboundSMS) {
        return saveAndFlush(inboundSMS);
    }
}
