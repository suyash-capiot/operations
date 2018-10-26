package com.coxandkings.travel.operations.repository.manageofflinebooking.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineBooking;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineBookingRepository;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ManualOfflineBookingRepositoryImpl extends SimpleJpaRepository<OfflineBooking, String> implements ManualOfflineBookingRepository {

    private static Logger logger = LogManager.getLogger(ManualOfflineBookingRepositoryImpl.class);

    private EntityManager entityManager;

    public ManualOfflineBookingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(OfflineBooking.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void saveCreateRequestDetails(OfflineBooking offlineBooking) throws OperationException {
        try {
            entityManager.persist(offlineBooking);
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_REQUEST_SAVE);
        }
    }

    @Override
    public List<OfflineBooking> getBooking(String bookingRefId) throws OperationException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OfflineBooking> criteria = builder.createQuery(OfflineBooking.class);
        Root<OfflineBooking> root = criteria.from(OfflineBooking.class);
        Predicate p1 = builder.and(builder.equal(root.get("bookId"), bookingRefId));
        criteria.where(p1);
        return entityManager.createQuery( criteria ).getResultList();
    }
}
