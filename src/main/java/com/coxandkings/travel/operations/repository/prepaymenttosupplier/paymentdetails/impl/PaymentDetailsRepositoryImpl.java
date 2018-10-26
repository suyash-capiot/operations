package com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails.impl;


import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails.PaymentDetailsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


@Repository(value = "PaymentDetailsRepositoryImpl")
public class PaymentDetailsRepositoryImpl extends SimpleJpaRepository<PaymentDetails, String> implements PaymentDetailsRepository {

    private EntityManager entityManager;
    @Autowired
    private PaymentAdviceRepository paymentAdviceRepository;

    private static Logger logger = LogManager.getLogger(PaymentDetailsRepositoryImpl.class);

    public PaymentDetailsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(PaymentDetails.class, em);
        entityManager = em;
    }


    @Override
    public PaymentDetails savePaymentDetails(PaymentDetails paymentDetails) {
        return this.saveAndFlush(paymentDetails);

    }

    @Override
    public PaymentDetails serachPaymentDetails(String paymentAdviceNumber) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentDetails> criteriaQuery = cb.createQuery(PaymentDetails.class);
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);
        //PaymentAdvice paymentAdvice = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria).get(0);
//        String paymentAdviceId = paymentAdvice.getId();
        PaymentDetails paymentDetails = null;
        try {
            paymentDetails = (PaymentDetails) entityManager.createQuery("SELECT p1 FROM PaymentDetails p1 join p1.paymentAdviceNumber p2  where p2.paymentAdviceNumber =:pid ").setParameter("pid", paymentAdviceNumber).getSingleResult();
        } catch (Exception ex) {
            logger.error("Error in serachPaymentDetails() method");
            logger.error("Payment detail not found");
        }
//        paymentDetails.setPaymentAdviceNumber(paymentAdvice);
       /* Root<PaymentDetails> root = criteriaQuery.from(PaymentDetails.class);
        criteriaQuery.select(root);

        Predicate p = cb.equal(root.get("paymentAdviceNumber"), paymentAdviceNumber);

        criteriaQuery.where(p);
        PaymentDetails paymentDetails = null;
        try {
            paymentDetails = entityManager.createQuery(criteriaQuery).getSingleResult();
            return paymentDetails;
        } catch (NoResultException e) {
            paymentDetails = null;
        }*/
        //   final List resultList = entityManager.createQuery(sql).getResultList();

        return paymentDetails;
    }

    @Override
    public PaymentDetails getPaymentDetailsById(String id) {
        return findOne(id);
    }


}
