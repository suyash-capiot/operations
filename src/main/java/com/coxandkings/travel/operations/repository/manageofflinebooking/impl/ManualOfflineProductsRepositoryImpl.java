package com.coxandkings.travel.operations.repository.manageofflinebooking.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineProducts;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineProductsRepository;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ManualOfflineProductsRepositoryImpl extends SimpleJpaRepository<OfflineProducts, String> implements ManualOfflineProductsRepository{

    private static Logger logger = LogManager.getLogger(ManualOfflineProductsRepositoryImpl.class);

    private EntityManager entityManager;

    public ManualOfflineProductsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(OfflineProducts.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public OfflineProducts saveCreateRequestDetails(OfflineProducts offlineBooking) throws OperationException {
        try {
             return this.saveAndFlush(offlineBooking);
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_REQUEST_SAVE);
        }

    }

    @Override
    public List<OfflineProducts> getBooking(String bookingRefId) throws OperationException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OfflineProducts> criteria = builder.createQuery(OfflineProducts.class);
        Root<OfflineProducts> root = criteria.from(OfflineProducts.class);
        Predicate p1 = builder.and(builder.equal(root.get("bookRefNumber"), bookingRefId));
        criteria.where(p1);
        return entityManager.createQuery( criteria ).getResultList();
    }

    @Override
    public OfflineProducts updateBooking(JSONObject updateReq) throws OperationException {
        try{
            CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
            CriteriaQuery<OfflineProducts> criteriaQuery=criteriaBuilder.createQuery(OfflineProducts.class);
            Root<OfflineProducts> root=criteriaQuery.from(OfflineProducts.class);
            criteriaQuery.select(root);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),false));
            predicates.add(criteriaBuilder.equal(root.get("bookRefNumber"),updateReq.getString("bookReferenceId")));
            predicates.add(criteriaBuilder.equal(root.get("id"),updateReq.getString("offlineBookingId")));
            predicates.add(criteriaBuilder.equal(root.get("bookingStatus"),"Pending"));
            if(!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
            }
            OfflineProducts updatedBooking = null;
            if(entityManager.createQuery( criteriaQuery ).getResultList().size()>0)
                logger.info("multiple bookings are selected.. need to handle this ");//TODO
            else {
                OfflineProducts getBookingFroUpdate = entityManager.createQuery( criteriaQuery ).getResultList().get(0);
                if(updateReq.optJSONObject("clientDetails")!=null)
                    getBookingFroUpdate.setClientDetails(updateReq.getJSONObject("clientDetails").toString());

                if(updateReq.optJSONObject("productDetails")!=null)
                    getBookingFroUpdate.setClientDetails(updateReq.getJSONObject("productDetails").toString());

                if(updateReq.optJSONObject("travelAndPassengerDetails")!=null)
                    getBookingFroUpdate.setClientDetails(updateReq.getJSONObject("travelAndPassengerDetails").toString());

                if(updateReq.optJSONObject("paymentDetails")!=null)
                    getBookingFroUpdate.setClientDetails(updateReq.getJSONObject("paymentDetails").toString());

                updatedBooking = this.saveAndFlush(getBookingFroUpdate);
            }
            return updatedBooking;
        }catch (Exception e){
            logger.info("exception occured in update private db");
            return null;
        }

    }

    @Override
    @Transactional
    public String deleteBooking(String offlineBookId) throws OperationException {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<OfflineProducts> criteriaQuery=criteriaBuilder.createQuery(OfflineProducts.class);
        Root<OfflineProducts> root=criteriaQuery.from(OfflineProducts.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("isDeleted"),false));
        predicates.add(criteriaBuilder.equal(root.get("id"),offlineBookId));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        if(entityManager.createQuery( criteriaQuery ).getResultList().size()>1)
            logger.info("multiple bookings are selected.. need to handle this ");//TODO
        else {
            OfflineProducts getBookingForDelete = entityManager.createQuery( criteriaQuery ).getResultList().get(0);
            getBookingForDelete.setDeleted(true);
            OfflineProducts res = this.saveAndFlush(getBookingForDelete);
            if(res!=null)
                return "booking deleted successfully";
        }
        return null;
    }

    @Override
    @Transactional
    public OfflineProducts findById(String id) throws OperationException {
        return this.findOne(id);
    }

}
