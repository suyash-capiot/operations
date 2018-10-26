package com.coxandkings.travel.operations.repository.amendcompanycommercial.impl;


import com.coxandkings.travel.operations.enums.amendclientcommercials.ApprovalStatus;
import com.coxandkings.travel.operations.model.companycommercial.AmendCompanyCommercial;
import com.coxandkings.travel.operations.repository.amendcompanycommercial.AmendCompanyCommercialRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
public class AmendCompanyCommercialRepositoryImpl extends SimpleJpaRepository<AmendCompanyCommercial, String> implements AmendCompanyCommercialRepository {
    private EntityManager entityManager;

    public AmendCompanyCommercialRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(AmendCompanyCommercial.class, entityManager);
        this.entityManager = entityManager;

    }

	@Override
	@Transactional
	public AmendCompanyCommercial saveCommercial(AmendCompanyCommercial amendCompanyCommercial) {
		   return this.saveAndFlush(amendCompanyCommercial);
	}
    
	@Override
    public AmendCompanyCommercial getCommercial(String id) {
        return this.findOne(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAmendedCommercialHeads(String bookingId) {
        return entityManager.createQuery(" SELECT acc.commercialHead FROM AmendCompanyCommercial acc WHERE acc.bookingId=:bookingId")
                .setParameter("bookingId", bookingId).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getApprovedCommercialHeads(String bookingId) {
        return entityManager.createQuery(" SELECT acc.commercialHead FROM AmendCompanyCommercial acc WHERE acc.bookingId=:bookingId AND acc.approvalStatus=:status ")
                .setParameter("bookingId", bookingId).setParameter("status", ApprovalStatus.APPROVED).getResultList();

    }

}
