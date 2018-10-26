package com.coxandkings.travel.operations.repository.booking.impl;

import com.coxandkings.travel.operations.model.booking.NewSellingPriceRecord;
import com.coxandkings.travel.operations.repository.booking.SellingPriceRecordRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class SellingPriceRecordRepositoryImpl extends SimpleJpaRepository<NewSellingPriceRecord,String> implements SellingPriceRecordRepository {

    private EntityManager entityManager;

    SellingPriceRecordRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager){
        super(NewSellingPriceRecord.class, entityManager);
        this.entityManager=entityManager;
    }

    @Override
    public NewSellingPriceRecord saveOrUpdateNewSellingPriceDetails(NewSellingPriceRecord sellingPriceRecord) {
        return this.saveAndFlush(sellingPriceRecord);
    }

    @Override
    public NewSellingPriceRecord getNewSellingPriceDetails(String bookingRefNo, String productId) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<NewSellingPriceRecord> criteriaQuery=criteriaBuilder.createQuery(NewSellingPriceRecord.class);
        Root<NewSellingPriceRecord> root=criteriaQuery.from(NewSellingPriceRecord.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("bookingRefNo"),bookingRefNo),criteriaBuilder.equal(root.get("productId"),productId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public String deleteNewSellingPriceDetails(String bookingRefNo, String productId) {
        NewSellingPriceRecord sellingPriceRecord=getNewSellingPriceDetails(bookingRefNo,productId);
        this.delete(sellingPriceRecord.getId());
        this.flush();
        return "delete is successful";
    }
}
