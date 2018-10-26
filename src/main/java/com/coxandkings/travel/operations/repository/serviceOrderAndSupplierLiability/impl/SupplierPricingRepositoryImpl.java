package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.SupplierPricingCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.SupplierPricing;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.SupplierPricingRepository;
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
public class SupplierPricingRepositoryImpl extends SimpleJpaRepository<SupplierPricing, String> implements SupplierPricingRepository {

    private EntityManager entityManager;

    SupplierPricingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(SupplierPricing.class,em);
        this.entityManager=em;
    }

    @Override
    @Transactional(rollbackFor = OperationException.class)
    public SupplierPricing getSupplierPricing(SupplierPricingCriteria criteria) {
        SupplierPricing supplierPricing=null;
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierPricing> criteriaQuery=criteriaBuilder.createQuery(SupplierPricing.class);
        Root<SupplierPricing> root=criteriaQuery.from(SupplierPricing.class);
        criteriaQuery.select(root);
        List<Predicate> predicates=new ArrayList<>();

        if(criteria.getId()!=null)
            predicates.add(criteriaBuilder.equal(root.get("id"),criteria.getId()));

        if(criteria.getAmendmentCharges()!=null)
            predicates.add(criteriaBuilder.equal(root.get("amendmentCharges"),criteria.getAmendmentCharges()));

        if(criteria.getAmountPaidToSupplier()!=null)
            predicates.add(criteriaBuilder.equal(root.get("amountPaidToSupplier"),criteria.getAmountPaidToSupplier()));

        if(criteria.getCancellationCharges()!=null)
            predicates.add(criteriaBuilder.equal(root.get("cancellationCharges"),criteria.getCancellationCharges()));

        if(criteria.getSupplierCommercials()!=null)
            predicates.add(criteriaBuilder.equal(root.get("supplierCommercials"),criteria.getSupplierCommercials()));

        if(criteria.getSurcharges()!=null)
            predicates.add(criteriaBuilder.equal(root.get("surcharges"),criteria.getSurcharges()));

        if(criteria.getSupplements()!=null)
            predicates.add(criteriaBuilder.equal(root.get("supplements"),criteria.getSupplements()));

        if(criteria.getUpgrades()!=null)
            predicates.add(criteriaBuilder.equal(root.get("upgrades"),criteria.getUpgrades()));

        if(criteria.getAncillaries()!=null)
            predicates.add(criteriaBuilder.equal(root.get("ancillaries"),criteria.getAncillaries()));

        if(criteria.getSupplierGst()!=null)
            predicates.add(criteriaBuilder.equal(root.get("supplierGst"),criteria.getSupplierGst()));

        if(criteria.getSupplierCost()!=null)
            predicates.add(criteriaBuilder.equal(root.get("supplierCost"),criteria.getSupplierCost()));

        if(criteria.getNetPayableToSupplier()!=null)
            predicates.add(criteriaBuilder.equal(root.get("netPayableToSupplier"),criteria.getNetPayableToSupplier()));

        if(criteria.getTotalBalanceAmountPayable()!=null)
            predicates.add(criteriaBuilder.equal(root.get("totalBalanceAmountPayable"),criteria.getTotalBalanceAmountPayable()));

        if(criteria.getPaymentStatus()!=null)
            predicates.add(criteriaBuilder.equal(root.get("paymentStatus"),criteria.getPaymentStatus()));

        if (criteria.getSupplierTotalCost() != null)
            predicates.add(criteriaBuilder.equal(root.get("supplierTotalCost"), criteria.getSupplierTotalCost()));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        try{
            supplierPricing=entityManager.createQuery(criteriaQuery).getSingleResult();
            return supplierPricing;
        }
        catch(Exception e){
            return supplierPricing;
        }
    }

    @Override
    @Transactional
    public void remove(String id) {
        this.delete(id);
        this.flush();
    }
}
