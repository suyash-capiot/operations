package com.coxandkings.travel.operations.repository.booking.impl;

import com.coxandkings.travel.operations.model.booking.AirlineUpdates;
import com.coxandkings.travel.operations.repository.booking.AirlineUpdatesRepository;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class AirlineUpdatesRepositoryImpl extends SimpleJpaRepository<AirlineUpdates, String> implements AirlineUpdatesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(AirlineUpdatesRepositoryImpl.class);

    AirlineUpdatesRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(AirlineUpdates.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public AirlineUpdates saveOrUpdateAirlineUpdates(AirlineUpdates airlineUpdates) {
        logger.debug("\n Entering AirlineUpdatesRepositoryImpl :: saveOrUpdateAirlineUpdates method");
        AirlineUpdates updates = getAirlineUpdate(airlineUpdates.getBookID(), airlineUpdates.getOrderID());
        if (updates == null) {
            logger.debug("\n saving the airline updates record");
            updates = this.saveAndFlush(airlineUpdates);
        } else {
            airlineUpdates.setAirlineUpdateId(updates.getAirlineUpdateId());
            logger.debug("\n updating the airline updates record");
            updates = this.saveAndFlush(airlineUpdates);

        }
        logger.debug("\n Exiting AirlineUpdatesRepositoryImpl :: saveOrUpdateAirlineUpdates method");
        return updates;
    }

    @Override
    public List<AirlineUpdates> getAirlineUpdates() {
        logger.debug("\n Entering AirlineUpdatesRepositoryImpl :: getAirlineUpdates method");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AirlineUpdates> criteriaQuery = criteriaBuilder.createQuery(AirlineUpdates.class);
        Root<AirlineUpdates> root = criteriaQuery.from(AirlineUpdates.class);
        logger.debug("\n retrieving list of airline updates which are not updated");
        criteriaQuery.where(criteriaBuilder.equal(root.get("isBookingUpdated"), false));
        logger.debug("\n Exiting AirlineUpdatesRepositoryImpl :: getAirlineUpdates method");
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public AirlineUpdates getAirlineUpdate(String bookingRef, String productId) {
        logger.debug("\n Entering AirlineUpdatesRepositoryImpl :: getAirlineUpdate method");
        AirlineUpdates airlineUpdates = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AirlineUpdates> criteriaQuery = criteriaBuilder.createQuery(AirlineUpdates.class);
        Root<AirlineUpdates> root = criteriaQuery.from(AirlineUpdates.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("bookID"), bookingRef), criteriaBuilder.equal(root.get("orderID"), productId));
        logger.debug("\n retrieving airline update of a particular flight booking");
        try {
            airlineUpdates = entityManager.createQuery(criteriaQuery).getSingleResult();
            logger.debug("\n Exiting AirlineUpdatesRepositoryImpl :: getAirlineUpdate method");
            return airlineUpdates;
        } catch (Exception e) {
            logger.debug("\n Exiting AirlineUpdatesRepositoryImpl :: getAirlineUpdate method");
            return null;
        }
    }
}
