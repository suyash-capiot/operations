package com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "PrePaymentToSupplierListener")
public class PaymentAdviceRepositoryImpl extends SimpleJpaRepository<PaymentAdvice,String> implements PaymentAdviceRepository {


    private  EntityManager entityManager;

    public PaymentAdviceRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(PaymentAdvice.class, em);
        entityManager=em;
    }


    @Override
    @Transactional
    public PaymentAdvice savePaymentAdvice(PaymentAdvice paymentAdvice) {
        return this.save(paymentAdvice);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentAdvice updatePaymentAdvice(PaymentAdvice advice) {
        return  this.saveAndFlush(advice);
    }


    @Override
    public List<PaymentAdvice> searchPaymentAdvise(PaymentCriteria paymentCriteria) {
        List<PaymentAdvice> paymentAdvices =null;
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentAdvice> criteriaQuery=criteriaBuilder.createQuery(PaymentAdvice.class);
        Root<PaymentAdvice> root=criteriaQuery.from(PaymentAdvice.class);

        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (paymentCriteria.getId()!=null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), paymentCriteria.getId()));
        }

        if (paymentCriteria.getSupplierReferenceId()!=null) {
            predicates.add(criteriaBuilder.equal(root.get("supplierRefId"), paymentCriteria.getSupplierReferenceId()));
        }

        if (paymentCriteria.getSupplierName()!=null) {
            predicates.add(criteriaBuilder.equal(root.get("supplierName"), paymentCriteria.getSupplierName()));
        }

        if (paymentCriteria.getPaymentAdviceNumber()!=null) {
            predicates.add(criteriaBuilder.equal(root.get("paymentAdviceNumber"), paymentCriteria.getPaymentAdviceNumber()));
        }

        if (paymentCriteria.getPaymentDueDateFrom() != null || paymentCriteria.getPaymentDueDateTo() != null) {
            predicates.add(criteriaBuilder.between(root.get("paymentDueDate"), paymentCriteria.getPaymentDueDateFrom(), paymentCriteria.getPaymentDueDateTo()));
        }

        if (!StringUtils.isEmpty(paymentCriteria.isPrePayment()))
        {
            predicates.add(criteriaBuilder.equal(root.get("prePaymentApplicable"), paymentCriteria.isPrePayment()));
        }


        if (paymentCriteria.getSearchByPaymentAdviceNumber()==null || !paymentCriteria.getSearchByPaymentAdviceNumber()){

            SetJoin<PaymentAdvice, PaymentAdviceOrderInfo> join = root.joinSet("paymentAdviceOrderInfoSet");
            if (paymentCriteria.getBookingRefId()!=null) {
                predicates.add(criteriaBuilder.equal(join.get("bookingRefId"), paymentCriteria.getBookingRefId()));
            }

            if (paymentCriteria.getOrderId()!=null) {
                predicates.add(criteriaBuilder.equal(join.get("orderId"), paymentCriteria.getOrderId()));
            }

            if (paymentCriteria.getOrderLevelNetPayableAmt() != null) {
                predicates.add(criteriaBuilder.equal(join.get("orderLevelNetPayableToSupplier"), paymentCriteria.getOrderLevelNetPayableAmt()));
            }

            if (paymentCriteria.getOrderLevelAmountPayableToSupplier() != null) {
                predicates.add(criteriaBuilder.equal(join.get("orderLevelAmountPayableForSupplier"), paymentCriteria.getOrderLevelAmountPayableToSupplier()));
            }

            if (paymentCriteria.getBalanceAmtToBePaidToSupplier() != null) {
                predicates.add(criteriaBuilder.equal(join.get("orderLevelBalanceAmtPayableToSupplier"), paymentCriteria.getBalanceAmtToBePaidToSupplier()));
            }

            if (paymentCriteria.getServiceOrderId() != null) {
                predicates.add(criteriaBuilder.equal(join.get("serviceOrderId"), paymentCriteria.getServiceOrderId()));
            }
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try{
            paymentAdvices = entityManager.createQuery(criteriaQuery).getResultList();

            TypedQuery<PaymentAdvice> query = entityManager.createQuery(criteriaQuery);

            pagination(paymentCriteria.getPageNumber(), paymentCriteria.getPageSize(), query);

            return paymentAdvices;
        }
        catch (NoResultException e){
            return paymentAdvices;
        }
    }


    @Override
    @Transactional
    public PaymentAdvice getById(String id) {
        return this.findOne(id);
    }

    @Override
    public String balancePayment(String bookingRefId, String orderId, String supplierId) {
//        String q = String.valueOf(entityManager.createQuery("select  sum(t2.orderLevelAmountPayableForSupplier)  " +
//                "from PaymentAdvice t1 join t1.paymentAdviceOrderInfoSet t2 " +
//                " where t2.bookingRefId = :bookingRefId and t2.orderId = :orderId and t1.supplierRefId = :supplierId " +
//                "group by t2.bookingRefId , t2.orderId ,t1.supplierRefId ")
//                .setParameter("bookingRefId",bookingRefId)
//        .setParameter("orderId",orderId)
//        .setParameter("supplierId",supplierId).getFirstResult());
//        if (!StringUtils.isEmpty(q))
//        {
//            System.out.println("Balance Amt"+q);
//            return q;
//        }
//        else
//            return String.valueOf(0);


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);

        Root<PaymentAdvice> root = query.from(PaymentAdvice.class);
        SetJoin<PaymentAdvice, PaymentAdviceOrderInfo> posList = root.joinSet("paymentAdviceOrderInfoSet");

        query
                .select(cb.sum(posList.get("orderLevelAmountPayableForSupplier")))
                .where(
                        cb.equal(root.get("supplierRefId"), supplierId),
                        cb.equal(posList.get("orderId"), orderId),
                        cb.equal(posList.get("bookingRefId"), bookingRefId)

                );
        BigDecimal balance = entityManager.createQuery(query).getSingleResult();
        if (balance != null)
            return String.valueOf(balance);
        else
            balance = new BigDecimal(0);
        return String.valueOf(balance);

    }

    @Override
    public String netPayableAmount(String bookingRefId, String orderId, String supplierId) {
        String netAmt = String.valueOf(entityManager.createQuery("select t2.orderLevelNetPayableToSupplier from PaymentAdvice t1 join t1.paymentAdviceOrderInfoSet t2 " +
                "where  t2.bookingRefId = :bookingRefId and t2.orderId = :orderId and t1.supplierRefId = :supplierId")
                .setParameter("bookingRefId", bookingRefId)
                .setParameter("orderId", orderId)
                .setParameter("supplierId", supplierId).getFirstResult());
        if (!StringUtils.isEmpty(netAmt)) {
            System.out.println("netAmt" + netAmt);
            return netAmt;
        } else
            return String.valueOf(0);


    }

    @Override
    public String calculateAmountPayableToSupplier(String serviceOrderId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);

        Root<PaymentAdvice> root = query.from(PaymentAdvice.class);
        SetJoin<PaymentAdvice, PaymentAdviceOrderInfo> posList = root.joinSet("paymentAdviceOrderInfoSet");

        query
                .select(cb.sum(posList.get("orderLevelAmountPayableForSupplier")))
                .where(cb.equal(posList.get("serviceOrderId"), serviceOrderId));
        BigDecimal amtPayable = entityManager.createQuery(query).getSingleResult();
        return String.valueOf(amtPayable);
    }


    private void pagination(Integer pageNumber, Integer pageSize, TypedQuery<PaymentAdvice> query) {
        if (pageNumber == null || pageNumber == 0) {
            pageNumber = new Integer(1);
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = new Integer(10);
        }

        Integer firstResult = ((pageNumber - 1) * pageSize) + 1;
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
    }



}
