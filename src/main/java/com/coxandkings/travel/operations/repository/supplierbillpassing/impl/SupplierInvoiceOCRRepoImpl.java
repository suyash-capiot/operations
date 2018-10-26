package com.coxandkings.travel.operations.repository.supplierbillpassing.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.repository.supplierbillpassing.SupplierInvoiceOCRRepo;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class SupplierInvoiceOCRRepoImpl extends SimpleJpaRepository<SupplierInvoiceOCR,String> implements SupplierInvoiceOCRRepo {

    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(SupplierInvoiceOCRRepoImpl.class);

    public SupplierInvoiceOCRRepoImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(SupplierInvoiceOCR.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public SupplierInvoiceOCR add(SupplierInvoiceOCR supplierInvoiceOCR) {
        return this.save(supplierInvoiceOCR);
    }

    @Override
    public SupplierInvoiceOCR getById(String id) {
        return this.findOne(id);
    }


    @Override
    public List<SupplierInvoiceOCR> getAvailableInvoice(SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria) throws OperationException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SupplierInvoiceOCR> criteriaQuery = criteriaBuilder.createQuery(SupplierInvoiceOCR.class);
            Root<SupplierInvoiceOCR> root = criteriaQuery.from(SupplierInvoiceOCR.class);
            criteriaQuery.select(root);

            Subquery<ZonedDateTime> subquery=criteriaQuery.subquery(ZonedDateTime.class);
            Root<SupplierInvoiceOCR> subRoot=subquery.from(SupplierInvoiceOCR.class);

            Expression expression=criteriaBuilder.max(subRoot.get("creationDate"));
            subquery.select(expression);
            subquery.groupBy(subRoot.get("supplierId"),subRoot.get("invoiceNumber"));
            Set<Predicate> predicateSet=new HashSet<>();
            predicateSet.add(criteriaBuilder.equal(root.get("supplierId"),subRoot.get("supplierId")));
            predicateSet.add(criteriaBuilder.equal(root.get("invoiceNumber"),subRoot.get("invoiceNumber")));
            subquery.where(predicateSet.toArray(new Predicate[0]));

            List<Predicate> predicates=new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("creationDate"),subquery));
            predicates.add(criteriaBuilder.isFalse(root.get("used")));
            if (supplierInvoiceSearchCriteria!=null){
                if(!StringUtils.isEmpty(supplierInvoiceSearchCriteria.getSupplierId()))
                    predicates.add(criteriaBuilder.equal(root.get("supplierId"), supplierInvoiceSearchCriteria.getSupplierId()));
                if (!StringUtils.isEmpty(supplierInvoiceSearchCriteria.getInvoiceNumber()))
                    predicates.add(criteriaBuilder.equal(root.get("invoiceNumber"),supplierInvoiceSearchCriteria.getInvoiceNumber()));
            }

            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            List<SupplierInvoiceOCR> supplierInvoiceOCRList = null;
            supplierInvoiceOCRList = entityManager.createQuery(criteriaQuery).getResultList();
            return supplierInvoiceOCRList;
        } catch (Exception e) {
            logger.error("Error occured in getting invoice list");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public SupplierInvoiceOCR update(SupplierInvoiceOCR supplierInvoiceOCR) {
        return this.saveAndFlush(supplierInvoiceOCR);
    }

    @Override
    public SupplierInvoiceOCR getByinvoiceNumber(String invoiceNumber, String supplierId) throws OperationException {

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SupplierInvoiceOCR> criteriaQuery = criteriaBuilder.createQuery(SupplierInvoiceOCR.class);
            Root<SupplierInvoiceOCR> root = criteriaQuery.from(SupplierInvoiceOCR.class);
            criteriaQuery.select(root);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("invoiceNumber"), invoiceNumber));
            predicates.add(criteriaBuilder.equal(root.get("supplierId"), supplierId));
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            SupplierInvoiceOCR supplierInvoiceOCR = entityManager.createQuery(criteriaQuery).getSingleResult();
            return supplierInvoiceOCR;
        } catch (Exception e) {
            logger.error("Error occured in getting supplier OCR invoice");
            throw new OperationException(Constants.ER1030);
        }
    }
}
