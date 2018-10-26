package com.coxandkings.travel.operations.repository.commercialstatements.impl;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementBillStatus;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialType;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingOrder;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementsBillPassing;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.repository.commercialstatements.SupplierCommercialStatementRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

@Transactional
@Repository

public class SupplierCommercialStatementRepoImpl extends SimpleJpaRepository<SupplierCommercialStatement,String> implements SupplierCommercialStatementRepo {

    private EntityManager entityManager;

    private Logger logger=Logger.getLogger(SupplierCommercialStatementRepoImpl.class);

    public SupplierCommercialStatementRepoImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(SupplierCommercialStatement.class, entityManager);
        this.entityManager=entityManager;
    }

    @Override
    public SupplierCommercialStatement add(SupplierCommercialStatement supplierCommercialStatement) {
        return this.save(supplierCommercialStatement);
    }

    @Override
    public SupplierCommercialStatement get(String id) {
        return this.findOne(id);
    }

    @Override //Method Added by Ashley to get all supplier statements
    public List<SupplierCommercialStatement> getAll(List<String> ids){
        try {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SupplierCommercialStatement> criteriaQuery = criteriaBuilder.createQuery(SupplierCommercialStatement.class);
            Root<SupplierCommercialStatement> root = criteriaQuery.from(SupplierCommercialStatement.class);
            criteriaQuery.select(root);
            List<Predicate> predicates = new ArrayList<>();

            if (ids.size()>0)
                predicates.add(root.get("statementId").in(ids));
//                predicates.add(criteriaBuilder.equal(root.get("statementId"), ids));


            if (!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            }

            TypedQuery<SupplierCommercialStatement> query = entityManager.createQuery(criteriaQuery);
            List<SupplierCommercialStatement> supplierCommercialStatements = query.getResultList();
            return supplierCommercialStatements;
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SupplierCommercialStatement> criteriaQuery = criteriaBuilder.createQuery(SupplierCommercialStatement.class);
            Root<SupplierCommercialStatement> root = criteriaQuery.from(SupplierCommercialStatement.class);
            criteriaQuery.select(root);
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSupplierName()))
                predicates.add(criteriaBuilder.equal(root.get("supplierOrClientName"), commercialStatementSearchCriteria.getSupplierName()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialHead()))
                predicates.add(criteriaBuilder.equal(root.get("commercialHead"), commercialStatementSearchCriteria.getCommercialHead()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialType()) && !commercialStatementSearchCriteria.getCommercialType().equalsIgnoreCase(CommercialType.ALL.getValue()))
                predicates.add(criteriaBuilder.equal(root.get("commercialType"), commercialStatementSearchCriteria.getCommercialType()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductCategory()))
                predicates.add(criteriaBuilder.equal(root.get("productCategory"), commercialStatementSearchCriteria.getProductCategory()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductName()))
                predicates.add(criteriaBuilder.equal(root.get("productName"), commercialStatementSearchCriteria.getProductName()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCurrency()))
                predicates.add(criteriaBuilder.equal(root.get("currency"), commercialStatementSearchCriteria.getCurrency()));

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
                predicates.add(criteriaBuilder.equal(root.get("commercialStatementFor"),commercialStatementSearchCriteria.getCommercialStatementFor()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductCategorySubType()))
                predicates.add(criteriaBuilder.equal(root.get("productCategorySubType"),commercialStatementSearchCriteria.getProductCategorySubType()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductFlavourName()))
                predicates.add(criteriaBuilder.equal(root.get("productFlavourName"),commercialStatementSearchCriteria.getProductFlavourName()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getProductNameSubType()))
                predicates.add(criteriaBuilder.equal(root.get("productNameSubType"),commercialStatementSearchCriteria.getProductNameSubType()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialStatementName()))
                predicates.add(criteriaBuilder.equal(root.get("statementName"),commercialStatementSearchCriteria.getCommercialStatementName()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementStatus()))
                predicates.add(criteriaBuilder.equal(root.get("settlementStatus"),commercialStatementSearchCriteria.getSettlementStatus()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getCompanyMarketId()))
                predicates.add(criteriaBuilder.equal(root.get("companyMarket"),commercialStatementSearchCriteria.getCompanyMarketId()));

            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getStatementId()))
                predicates.add(criteriaBuilder.equal(root.get("statementId"),commercialStatementSearchCriteria.getStatementId()));

            if (commercialStatementSearchCriteria.getBillPassingResource() != null && commercialStatementSearchCriteria.getBillPassingResource())
                predicates.add(criteriaBuilder.isNull(root.get("commercialStatementsBillPassing")));

            if (commercialStatementSearchCriteria.getPaymentAdviceResource()!=null && commercialStatementSearchCriteria.getPaymentAdviceResource()) {
                predicates.add(criteriaBuilder.isNotNull(root.get("commercialStatementsBillPassing")));
                Join<SupplierCommercialStatement,CommercialStatementsBillPassing> join=root.join("commercialStatementsBillPassing");
                predicates.add(criteriaBuilder.notEqual(join.get("billPassingStatus"), CommercialStatementBillStatus.PENDING_APPROVAL.getValue()));
            }

            if (commercialStatementSearchCriteria.getAttachedStatementIds() != null && !commercialStatementSearchCriteria.getAttachedStatementIds().isEmpty())
                commercialStatementSearchCriteria.getAttachedStatementIds().stream().forEach(s -> {
                    predicates.add(criteriaBuilder.notEqual(root.get("statementId"), s));
                });

            if (!predicates.isEmpty()) {
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            }

            if (commercialStatementSearchCriteria.getCommercialStatementSortingCriteria()!=null ) {
                if (commercialStatementSearchCriteria.getCommercialStatementSortingOrder()!=null && commercialStatementSearchCriteria.getCommercialStatementSortingOrder()== CommercialStatementSortingOrder.DESC)
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(commercialStatementSearchCriteria.getCommercialStatementSortingCriteria().getSortByValue())));
                else
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(commercialStatementSearchCriteria.getCommercialStatementSortingCriteria().getSortByValue())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("travelDateFrom")));
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("bookingPeriodFrom")));
            }
            TypedQuery<SupplierCommercialStatement> query = entityManager.createQuery(criteriaQuery);
            Integer actualSize=query.getResultList().size();
            if (commercialStatementSearchCriteria.getPage() == null)
                commercialStatementSearchCriteria.setPage(1);
            if (commercialStatementSearchCriteria.getSize() == null || commercialStatementSearchCriteria.getSize() == 0)
                commercialStatementSearchCriteria.setSize(10);
            Integer startIndex = ((commercialStatementSearchCriteria.getPage() - 1) * commercialStatementSearchCriteria.getSize());
            query.setFirstResult(startIndex);
            query.setMaxResults(commercialStatementSearchCriteria.getSize());

            List<SupplierCommercialStatement> supplierCommercialStatements= query.getResultList();
            return applyPagination(supplierCommercialStatements,commercialStatementSearchCriteria,actualSize);
        }
        catch (Exception e){
            logger.error("exception occured while searching for records");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public SupplierCommercialStatement update(SupplierCommercialStatement supplierCommercialStatement) {
        return this.saveAndFlush(supplierCommercialStatement);
    }

    @Override
    public SupplierCommercialStatement getByName(String statementName) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SupplierCommercialStatement> criteriaQuery = criteriaBuilder.createQuery(SupplierCommercialStatement.class);
            Root<SupplierCommercialStatement> root = criteriaQuery.from(SupplierCommercialStatement.class);
            criteriaQuery.select(root);

            criteriaQuery.where(criteriaBuilder.equal(root.get("statementName"), statementName));
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            logger.debug("Error in getting supplier commercial statement by name");
            return null;
        }
    }

    @Override
    public Set<SupplierCommercialStatement> getStatementsForPaymentAdvice(String paymentAdviceNumber) {

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<SupplierCommercialStatement> criteriaQuery = criteriaBuilder.createQuery(SupplierCommercialStatement.class);
            Root<SupplierCommercialStatement> root = criteriaQuery.from(SupplierCommercialStatement.class);
            criteriaQuery.select(root);

            Expression<Set<String>> expression=root.get("paymentAdviceIds");
            criteriaQuery.where(criteriaBuilder.isMember(paymentAdviceNumber, expression));
            return new HashSet<>(entityManager.createQuery(criteriaQuery).getResultList());
        } catch (Exception e) {
            logger.debug("Error in getting supplier commercial statement by name");
            return null;
        }

    }

    @Override
    public Set<String> getSupplierNames() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("supplierOrClientName"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> supplierNames = typedQuery.getResultList();
        return new HashSet<>(supplierNames);
    }

    @Override
    public Set<String> getCompanyMarkets() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("companyMarket"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> companyMarkets = typedQuery.getResultList();
        return new HashSet<>(companyMarkets);
    }

    @Override
    public Set<String> getCommercialHeads() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("commercialHead"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> commercialHeads = typedQuery.getResultList();
        return new HashSet<>(commercialHeads);
    }

    @Override
    public Set<String> getCurrency() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("currency"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> currencies = typedQuery.getResultList();
        return new HashSet<>(currencies);
    }

    @Override
    public Set<String> getProductCategories() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("productCategory"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> productCategories = typedQuery.getResultList();
        return new HashSet<>(productCategories);
    }

    @Override
    public Set<String> getProductCategorySubTypes() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("productCategorySubType"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> productCategorySubTypes = typedQuery.getResultList();
        return new HashSet<>(productCategorySubTypes);
    }

    @Override
    public Set<String> getProductNames() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<SupplierCommercialStatement> root = query.from(SupplierCommercialStatement.class);
        query.select(root.get("productName"));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        List<String> productNames = typedQuery.getResultList();
        return new HashSet<>(productNames);
    }


    private Map applyPagination(List<SupplierCommercialStatement> commercialStatementList, CommercialStatementSearchCriteria commercialStatementSearchCriteria, Integer actualSize){
        Map<String,Object> entiy=new HashMap<>();
        entiy.put("result",commercialStatementList);
        entiy.put("size",commercialStatementSearchCriteria.getSize());
        entiy.put("page",commercialStatementSearchCriteria.getPage());

        if (commercialStatementList==null || commercialStatementList.isEmpty()) {
            entiy.put("noOfPages",1);
            return entiy;
        }

        Integer noOfPages=0;
        if (actualSize%commercialStatementSearchCriteria.getSize()==0)
            noOfPages=actualSize/commercialStatementSearchCriteria.getSize();
        else noOfPages=actualSize/commercialStatementSearchCriteria.getSize()+1;

        entiy.put("noOfPages",noOfPages);
        return entiy;
    }
}
