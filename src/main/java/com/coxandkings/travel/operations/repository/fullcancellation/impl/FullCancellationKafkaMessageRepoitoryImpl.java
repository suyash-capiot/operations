package com.coxandkings.travel.operations.repository.fullcancellation.impl;

import com.coxandkings.travel.operations.model.fullCancellation.FullCancellationIdentity;
import com.coxandkings.travel.operations.model.fullCancellation.FullCancellationKafkaMessage;
import com.coxandkings.travel.operations.repository.fullcancellation.FullCancellationKafkaMessageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository

public class FullCancellationKafkaMessageRepoitoryImpl extends SimpleJpaRepository<FullCancellationKafkaMessage, FullCancellationIdentity> implements FullCancellationKafkaMessageRepository {

    private EntityManager entityManager;

    public FullCancellationKafkaMessageRepoitoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(FullCancellationKafkaMessage.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public FullCancellationKafkaMessage saveAndUpdate(FullCancellationKafkaMessage fullCancellationKafkaMessage) {
        return saveAndFlush(fullCancellationKafkaMessage);
    }

    @Override
    public FullCancellationKafkaMessage getFullCancellationKafkaMessage(FullCancellationIdentity fullCancellationIdentity) {
        return this.findOne(fullCancellationIdentity);
    }
}
