package com.coxandkings.travel.operations.repository.commercialstatements.impl;/*
package com.coxandkings.travel.operations.repository.commercialstatements.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatement;
import com.coxandkings.travel.operations.repository.commercialstatements.CommercialStatementRepository;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository("commercialRepository")

public class CommercialStatementRepositoryImpl extends SimpleJpaRepository<CommercialStatement,String> implements CommercialStatementRepository {

    private EntityManager entityManager;

    private Logger logger=Logger.getLogger(CommercialStatementRepositoryImpl.class);

    public CommercialStatementRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(CommercialStatement.class, entityManager);
        this.entityManager=entityManager;
    }

    @Override
    public CommercialStatement add(CommercialStatement commercialStatement) {
        return this.save(commercialStatement);
    }

    @Override
    public CommercialStatement get(String id) {
        return this.findOne(id);
    }


    @Override
    public Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CommercialStatement> criteriaQuery = criteriaBuilder.createQuery(CommercialStatement.class);
            Root<CommercialStatement> root = criteriaQuery.from(CommercialStatement.class);
            criteriaQuery.select(root);
            List<Predicate> predicates = new ArrayList<>();


            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getClientOrSupplierId()))
                predicates.add(criteriaBuilder.equal(root.get("supplierOrClientId"), commercialStatementSearchCriteria.getClientOrSupplierId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialHead()))
                predicates.add(criteriaBuilder.equal(root.get("commercialHead"), commercialStatementSearchCriteria.getCommercialHead()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialType()))
                predicates.add(criteriaBuilder.equal(root.get("commercialType"), commercialStatementSearchCriteria.getCommercialType()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductCategoryId()))
                predicates.add(criteriaBuilder.equal(root.get("productCategoryId"), commercialStatementSearchCriteria.getProductCategoryId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductId()))
                predicates.add(criteriaBuilder.equal(root.get("productId"), commercialStatementSearchCriteria.getProductId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementDueDateFrom()))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("settlementDueDate"), DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateFrom())));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementDueDateTo()))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("settlementDueDate"), DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateTo())));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getBookingDateFrom()))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookingPeriodFrom"), DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateFrom())));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getBookingDateTo()))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("bookingPeriodTo"), DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateTo())));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getTravelDateFrom()))
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("travelDateFrom"), DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateFrom())));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getTravelDateTo()))
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("travelDateTo"), DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateTo())));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialStatementFor()))
                predicates.add(criteriaBuilder.equal(root.get("issuedTo"),commercialStatementSearchCriteria.getCommercialStatementFor()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductCategorySubTypeId()))
                predicates.add(criteriaBuilder.equal(root.get("productCategorySubTypeId"),commercialStatementSearchCriteria.getProductCategorySubTypeId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductFlavourNameId()))
                predicates.add(criteriaBuilder.equal(root.get("productFlavourNameId"),commercialStatementSearchCriteria.getProductFlavourNameId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductNameSubTypeId()))
                predicates.add(criteriaBuilder.equal(root.get("productNameSubTypeId"),commercialStatementSearchCriteria.getProductNameSubTypeId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialStatementName()))
                predicates.add(criteriaBuilder.equal(root.get("statementName"),commercialStatementSearchCriteria.getCommercialStatementName()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementStatus()))
                predicates.add(criteriaBuilder.equal(root.get("settlementStatus"),commercialStatementSearchCriteria.getSettlementStatus()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCompanyMarketId()))
                predicates.add(criteriaBuilder.equal(root.get("companyMarketId"),commercialStatementSearchCriteria.getCompanyMarketId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getStatementId()))
                predicates.add(criteriaBuilder.equal(root.get("statementId"),commercialStatementSearchCriteria.getStatementId()));

            if (!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            }
        */
/*if (clientCommercialStatementCriteria.getSortCriteria()!=null ) {
            if (clientCommercialStatementCriteria.getDescending()!=null && clientCommercialStatementCriteria.getDescending()) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(clientCommercialStatementCriteria.getSortCriteria())));
            }else{
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(clientCommercialStatementCriteria.getSortCriteria())));
            }
        }*//*

            TypedQuery<CommercialStatement> query = entityManager.createQuery(criteriaQuery);
            Integer actualSize=query.getResultList().size();
            if (commercialStatementSearchCriteria.getSize() != null && commercialStatementSearchCriteria.getPage() != null) {
                Integer startIndex = ((commercialStatementSearchCriteria.getPage() - 1) * commercialStatementSearchCriteria.getSize());
                query.setFirstResult(startIndex);
                query.setMaxResults(commercialStatementSearchCriteria.getSize());
            }

            List<CommercialStatement> commercialStatementList= query.getResultList();
            return applyPagination(commercialStatementList,commercialStatementSearchCriteria,actualSize);
        }
        catch (Exception e){
            logger.error("exception occured while searching for records");
            return new HashMap();
        }
    }

    @Override
    public CommercialStatement update(CommercialStatement commercialStatement) {
        return this.saveAndFlush(commercialStatement);
    }

    private Map applyPagination(List<CommercialStatement> commercialStatementList,CommercialStatementSearchCriteria commercialStatementSearchCriteria,Integer actualSize){
        Map<String,Object> entiy=new HashMap<>();
        entiy.put("result",commercialStatementList);

        if (commercialStatementList.isEmpty()) return entiy;

        entiy.put("size",commercialStatementSearchCriteria.getSize());
        entiy.put("page",commercialStatementSearchCriteria.getPage());

        Integer noOfPages=0;
        if (actualSize%commercialStatementSearchCriteria.getSize()==0)
            noOfPages=actualSize/commercialStatementSearchCriteria.getSize();
        else noOfPages=actualSize/commercialStatementSearchCriteria.getSize()+1;

        entiy.put("size",commercialStatementSearchCriteria.getSize());
        entiy.put("page",commercialStatementSearchCriteria.getPage());
        entiy.put("noOfPages",noOfPages);
        return entiy;
    }
}
*/
