package com.coxandkings.travel.operations.repository.amendclientcommercial.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.ApprovalStatus;
import com.coxandkings.travel.operations.model.clientcommercial.ClientCommercial;
import com.coxandkings.travel.operations.repository.amendclientcommercial.AmendClientCommercialRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository("amendClientCommercialRepositoryImpl")
public class AmendClientCommercialRepositoryImpl extends
        SimpleJpaRepository<ClientCommercial, String> implements AmendClientCommercialRepository {

    private EntityManager entityManager;

    public AmendClientCommercialRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ClientCommercial.class, em);
        entityManager = em;
    }

    @Override
    public ClientCommercial saveClientCommercialAmendment(ClientCommercial clientCommercial) {
        return this.saveAndFlush(clientCommercial);
    }

    @Override
    public ClientCommercial getClientCommercialAmendment(String id) {
        return this.findOne(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAmendedCommercialHeads(String bookingId) {
        return entityManager.createQuery(" SELECT acc.commercialHead FROM ClientCommercial acc WHERE acc.bookingId=:bookingId")
                .setParameter("bookingId", bookingId).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getApprovedCommercialHeads(String bookingId) {
        return entityManager.createQuery(" SELECT acc.commercialHead FROM ClientCommercial acc WHERE acc.bookingId=:bookingId AND acc.approvalStatus=:status ")
                .setParameter("bookingId", bookingId).setParameter("status", ApprovalStatus.APPROVED).getResultList();

    }

   
}
