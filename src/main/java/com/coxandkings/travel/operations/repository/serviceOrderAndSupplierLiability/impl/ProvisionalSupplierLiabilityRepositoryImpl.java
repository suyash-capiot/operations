package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingCriteria;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingOrder;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.Status;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalSupplierLiabilityRepository;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class ProvisionalSupplierLiabilityRepositoryImpl extends SimpleJpaRepository<ProvisionalSupplierLiability, VersionId> implements ProvisionalSupplierLiabilityRepository {

    private EntityManager entityManager;

    @Autowired
    private OpsBookingAdapter opsBookingAdapter;

    @Autowired
    private UserService userService;

    ProvisionalSupplierLiabilityRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ProvisionalSupplierLiability.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public ProvisionalSupplierLiability generatePSL(ProvisionalSupplierLiability provisionalSupplierLiability) {
        return this.saveAndFlush(provisionalSupplierLiability);
    }

    @Override
    public ProvisionalSupplierLiability updatePSL(ProvisionalSupplierLiability provisionalSupplierLiability) {
        return this.saveAndFlush(provisionalSupplierLiability);
    }

    @Override
    public ProvisionalSupplierLiability getPSLByVersionId(VersionId versionId) {
        return this.findOne(versionId);
    }

    @Override
    public Map<String, Object> getProvisionalSupplierLiabilities(ServiceOrderSearchCriteria searchCriteria) {
        Integer pageSize = searchCriteria.getPageSize()!=null ? searchCriteria.getPageSize() : 10;
        Integer pageNumber = searchCriteria.getPageNumber()!=null ? searchCriteria.getPageSize() : 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProvisionalSupplierLiability> criteriaQuery = criteriaBuilder.createQuery(ProvisionalSupplierLiability.class);
        Root<ProvisionalSupplierLiability> root = criteriaQuery.from(ProvisionalSupplierLiability.class);
        criteriaQuery.select(root);
        List<Predicate> predicateList = new ArrayList<>();

        //Added predicates For Organization level access.
        List<Predicate> companyPredicates = getPredicateForCompany(root, criteriaBuilder);
        predicateList.addAll(companyPredicates);
      /*  Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ProvisionalSupplierLiability> subRoot = subquery.from(ProvisionalSupplierLiability.class);
        List<Predicate> predicates = new ArrayList<>();*/

        if (!StringUtils.isEmpty(searchCriteria.getBookingRefNo()))
            predicateList.add(criteriaBuilder.equal(root.get("bookingRefNo"), searchCriteria.getBookingRefNo()));

        if (!StringUtils.isEmpty(searchCriteria.getOrderId()))
            predicateList.add(criteriaBuilder.equal(root.get("orderId"), searchCriteria.getOrderId()));

        if (!StringUtils.isEmpty(searchCriteria.getCompanyMarketId()))
            predicateList.add(criteriaBuilder.equal(root.get("companyMarketId"), searchCriteria.getCompanyMarketId()));

        if (!StringUtils.isEmpty(searchCriteria.getProductCategoryId()))
            predicateList.add(criteriaBuilder.equal(root.get("productCategoryId"), searchCriteria.getProductCategoryId()));

        if (!StringUtils.isEmpty(searchCriteria.getProductCategorySubTypeId()))
            predicateList.add(criteriaBuilder.equal(root.get("productCategorySubTypeId"), searchCriteria.getProductCategorySubTypeId()));

        if (!StringUtils.isEmpty(searchCriteria.getProductNameId()))
            predicateList.add(criteriaBuilder.equal(root.get("productNameId"), searchCriteria.getProductNameId()));

        if (!StringUtils.isEmpty(searchCriteria.getProductNameSubTypeOrProductFlavorName()))
            predicateList.add(criteriaBuilder.equal(root.get("productNameSubTypeOrProductFlavorName"), searchCriteria.getProductNameSubTypeOrProductFlavorName()));

        /*if (searchCriteria.getVersionNumber() != null)
            predicateList.add(criteriaBuilder.equal(root.get("versionNumber"), searchCriteria.getVersionNumber()));*/

        if (!StringUtils.isEmpty(searchCriteria.getServiceOrderType())) {
            predicateList.add(criteriaBuilder.equal(root.get("type"), searchCriteria.getServiceOrderType()));
        }

        if (!StringUtils.isEmpty(searchCriteria.getServiceOrderTypeValue())) {
            predicateList.add(criteriaBuilder.equal(root.get("type"), ServiceOrderAndSupplierLiabilityType.getEnumValueFromString(searchCriteria.getServiceOrderTypeValue())));
        }

        if (!StringUtils.isEmpty(searchCriteria.getUniqueId())) {
            predicateList.add(criteriaBuilder.equal(root.get("uniqueId"), searchCriteria.getUniqueId()));
        }

        if (!StringUtils.isEmpty(searchCriteria.getProvisionalServiceOrderID()))
            predicateList.add(criteriaBuilder.equal(root.get("provisionalServiceOrderID"), searchCriteria.getProvisionalServiceOrderID()));

        if (!StringUtils.isEmpty(searchCriteria.getFinalServiceOrderID()))
            predicateList.add(criteriaBuilder.equal(root.get("finalServiceOrderID"), searchCriteria.getFinalServiceOrderID()));

        if (!StringUtils.isEmpty(searchCriteria.getProvisionalSupplierLiabilityID()))
            predicateList.add(criteriaBuilder.equal(root.get("provisionalSupplierLiabilityID"), searchCriteria.getProvisionalSupplierLiabilityID()));

        if (!StringUtils.isEmpty(searchCriteria.getFinalSupplierLiabilityID()))
            predicateList.add(criteriaBuilder.equal(root.get("finalSupplierLiabilityID"), searchCriteria.getFinalSupplierLiabilityID()));

        if (!StringUtils.isEmpty(searchCriteria.getStatus()))
            predicateList.add(criteriaBuilder.equal(root.get("status"), Status.fromString(searchCriteria.getStatus())));

        if (searchCriteria.getPaymentAdviceId() != null)
            predicateList.add(criteriaBuilder.isMember(searchCriteria.getPaymentAdviceId(), root.get("paymentAdviceIds")));

        if (!StringUtils.isEmpty(searchCriteria.getSupplierId()))
            predicateList.add(criteriaBuilder.equal(root.get("supplierId"), searchCriteria.getSupplierId()));

        if (!StringUtils.isEmpty(searchCriteria.getSupplierCurrency()))
            predicateList.add(criteriaBuilder.equal(root.get("supplierCurrency"), searchCriteria.getSupplierCurrency()));

        if (!StringUtils.isEmpty(searchCriteria.getSupplierSettlementStatus()))
            predicateList.add(criteriaBuilder.equal(root.get("supplierSettlementStatus"), searchCriteria.getSupplierSettlementStatus()));

        if (searchCriteria.getFromGenerationDate() != null && searchCriteria.getToGenerationDate() != null)
            predicateList.add(criteriaBuilder.between(root.get("dateOfGeneration"), searchCriteria.getFromGenerationDate(), searchCriteria.getToGenerationDate()));

        if (searchCriteria.getFromGenerationDate() != null && searchCriteria.getToGenerationDate() == null)
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfGeneration"), searchCriteria.getFromGenerationDate()));

        if (searchCriteria.getFromGenerationDate() == null && searchCriteria.getToGenerationDate() != null)
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateOfGeneration"), searchCriteria.getToGenerationDate()));

        if (!StringUtils.isEmpty(searchCriteria.getLinkedVersion()))
            predicateList.add(criteriaBuilder.equal(root.get("linkedVersion"), searchCriteria.getLinkedVersion()));

        if (searchCriteria.getTravelCompletionDate() != null)
            predicateList.add(criteriaBuilder.equal(root.get("travelCompletionDate"), searchCriteria.getTravelCompletionDate()));

        if (!StringUtils.isEmpty(searchCriteria.getCreatedByUserId()))
            predicateList.add(criteriaBuilder.equal(root.get("createdByUserId"), searchCriteria.getCreatedByUserId()));

        if (searchCriteria.getCreatedTime() != null)
            predicateList.add(criteriaBuilder.equal(root.get("createdTime"), searchCriteria.getCreatedTime()));

        if (!StringUtils.isEmpty(searchCriteria.getLastModifiedByUserId()))
            predicateList.add(criteriaBuilder.equal(root.get("lastModifiedByUserId"), searchCriteria.getLastModifiedByUserId()));

        if (searchCriteria.getLastModifiedTime() != null)
            predicateList.add(criteriaBuilder.equal(root.get("lastModifiedTime"), searchCriteria.getLastModifiedTime()));

        if (searchCriteria.getDifferenceInSupplierInvoice() != null)
            predicateList.add(criteriaBuilder.equal(root.get("diffAmount"), searchCriteria.getDifferenceInSupplierInvoice()));

        if (searchCriteria.getCreditOrDebitNoteNumber() != null)
            predicateList.add(criteriaBuilder.equal(root.get("creditOrDebitNoteNumber"), searchCriteria.getCreditOrDebitNoteNumber()));

        if (searchCriteria.getDiffInGst() != null)
            predicateList.add(criteriaBuilder.equal(root.get("diffInGst"), searchCriteria.getDiffInGst()));

        if (searchCriteria.getTotalDiffAmount() != null)
            predicateList.add(criteriaBuilder.equal(root.get("totalDiffAmount"), searchCriteria.getTotalDiffAmount()));

        if (searchCriteria.getPaymentDueDate() != null)
            predicateList.add(criteriaBuilder.equal(root.get("paymentDueDate"), searchCriteria.getPaymentDueDate()));

        if (searchCriteria.getPaymentDueDateFrom() != null && searchCriteria.getPaymentDueDateFrom() == null)
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("paymentDueDate"), searchCriteria.getPaymentDueDateFrom()));

        if (searchCriteria.getPaymentDueDateTo() == null && searchCriteria.getPaymentDueDateTo() != null)
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("paymentDueDate"), searchCriteria.getPaymentDueDateTo()));

        if (searchCriteria.getGeneralInvoice() != null)
            predicateList.add(criteriaBuilder.equal(root.get("isGeneralInvoice"), searchCriteria.getGeneralInvoice()));

        if (!StringUtils.isEmpty(searchCriteria.getInvoiceId()))
            predicateList.add(criteriaBuilder.equal(root.get("invoiceId"), searchCriteria.getInvoiceId()));

        if (!StringUtils.isEmpty(searchCriteria.getSupplierName()))
            predicateList.add(criteriaBuilder.equal(root.get("supplierName"), searchCriteria.getSupplierName()));

        predicateList.add(criteriaBuilder.notEqual(root.get("status"), Status.PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED));

        if (!StringUtils.isEmpty(searchCriteria.getSupplierBillPassingStatus())) {
            Join<ProvisionalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing");
            predicateList.add(criteriaBuilder.equal(join.get("supplierBillPassingStatus"), searchCriteria.getSupplierBillPassingStatus()));
        }

       /* Expression expression = criteriaBuilder.max(subRoot.get("versionNumber"));
        subquery.select(expression);
        subquery.groupBy(subRoot.get("uniqueId"));
        predicates.add(criteriaBuilder.equal(root.get("uniqueId"), subRoot.get("uniqueId")));
        subquery.where(predicates.toArray(new Predicate[0]));

        predicateList.add(criteriaBuilder.equal(root.get("versionNumber"), subquery));*/
  /*      CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(ProvisionalSupplierLiability.class).get("uniqueId")));
        entityManager.createQuery(cq);
        cq.where(predicateList.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(cq).getSingleResult();
*/
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        if (searchCriteria.getSupplierBillPassingSortingCriteria() != null) {
            if (searchCriteria.getSupplierBillPassingSortingOrder() != null && searchCriteria.getSupplierBillPassingSortingOrder() == SupplierBillPassingSortingOrder.DESC) {
                if (searchCriteria.getSupplierBillPassingSortingCriteria() == SupplierBillPassingSortingCriteria.PaymentAdviceStatus) {
                    Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.desc(paymentAdviceJoin.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                } else if (searchCriteria.getSupplierBillPassingSortingCriteria() == SupplierBillPassingSortingCriteria.SupplierBillPassingStatus) {
                    Join<ProvisionalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing");
                    criteriaQuery.orderBy(criteriaBuilder.desc(join.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                } else
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
            } else {
                if (searchCriteria.getSupplierBillPassingSortingCriteria() == SupplierBillPassingSortingCriteria.PaymentAdviceStatus) {
                    Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.asc(paymentAdviceJoin.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                } else if (searchCriteria.getSupplierBillPassingSortingCriteria() == SupplierBillPassingSortingCriteria.SupplierBillPassingStatus) {
                    Join<ProvisionalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing");
                    criteriaQuery.orderBy(criteriaBuilder.asc(join.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                } else
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
            }
        }

        TypedQuery<ProvisionalSupplierLiability> typedQuery = entityManager.createQuery(criteriaQuery);
        int count = typedQuery.getResultList().size();
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<ProvisionalSupplierLiability> serviceOrderAndSupplierLiabilities = typedQuery.getResultList();
        for (ProvisionalSupplierLiability provisionalSupplierLiability : serviceOrderAndSupplierLiabilities) {
            String supplierId = provisionalSupplierLiability.getSupplierId();

            provisionalSupplierLiability.setSupplierName(opsBookingAdapter.checkSupplierNameType(supplierId)[1]);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("result", serviceOrderAndSupplierLiabilities);
        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(searchCriteria.getPageSize(), count));
        return result;
    }

    private List<Predicate> getPredicateForCompany(Root<ProvisionalSupplierLiability> root, CriteriaBuilder criteriaBuilder){

        List<Predicate> predicateList = new ArrayList<>();
        OpsUser loggedInUser = userService.getLoggedInUser();
        if(loggedInUser!=null) {
            //TODO: uncomment Later.
    /*    if (!StringUtils.isEmpty(loggedInUser.getBU()))
            predicateList.add(criteriaBuilder.equal(root.get("BU"), loggedInUser.getBU()));

        if (!StringUtils.isEmpty(loggedInUser.getSBU()))
            predicateList.add(criteriaBuilder.equal(root.get("SBU"), loggedInUser.getSBU()));*/

            if (!StringUtils.isEmpty(loggedInUser.getCompanyId()))
                predicateList.add(criteriaBuilder.equal(root.get("companyId"), loggedInUser.getCompanyId()));

   /*     if (!StringUtils.isEmpty(loggedInUser.getCompanyName()))
            predicateList.add(criteriaBuilder.equal(root.get("companyName"), loggedInUser.getCompanyName()));

        if (!StringUtils.isEmpty(loggedInUser.getCompanyGroupId()))
            predicateList.add(criteriaBuilder.equal(root.get("companyGroupId"), loggedInUser.getCompanyGroupId()));

        if (!StringUtils.isEmpty(loggedInUser.getCompanyGroupName()))
            predicateList.add(criteriaBuilder.equal(root.get("companyGroupName"), loggedInUser.getCompanyGroupName()));

        if (!StringUtils.isEmpty(loggedInUser.getGroupOfCompanyId()))
            predicateList.add(criteriaBuilder.equal(root.get("groupOfCompanyId"), loggedInUser.getGroupOfCompanyId()));

        if (!StringUtils.isEmpty(loggedInUser.getGroupOfCompanyName()))
            predicateList.add(criteriaBuilder.equal(root.get("groupOfCompanyName"), loggedInUser.getGroupOfCompanyName()));

        if (!StringUtils.isEmpty(loggedInUser.getBranchName()))
            predicateList.add(criteriaBuilder.equal(root.get("branchName"), loggedInUser.getBranchName()));*/

        }
        return predicateList;
    }


    @Override
    public Long getCount() {
        return this.count();
    }
}
