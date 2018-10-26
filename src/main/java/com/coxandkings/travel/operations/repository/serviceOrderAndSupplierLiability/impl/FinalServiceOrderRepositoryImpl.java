package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingCriteria;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingOrder;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingStatus;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.Status;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.SupplierPricing;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.FinalServiceOrderRepository;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Transactional
public class FinalServiceOrderRepositoryImpl extends SimpleJpaRepository<FinalServiceOrder, VersionId> implements FinalServiceOrderRepository {

    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    FinalServiceOrderRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(FinalServiceOrder.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public FinalServiceOrder generateFSO(FinalServiceOrder finalServiceOrder) {
        return this.saveAndFlush(finalServiceOrder);
    }

    @Override
    public FinalServiceOrder updateFSO(FinalServiceOrder finalServiceOrder) {
        return this.saveAndFlush(finalServiceOrder);
    }

    @Override
    public FinalServiceOrder getFSOByVersionId(VersionId versionId) {
        return this.findOne(versionId);
    }

    @Override
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria) {
        return getFinalServiceOrders(searchCriteria, true);
    }
    @Override
    public Map<String, Object> getFinalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany) {
        /*Integer defaultPageSize = 10;
        Integer defaultPageNumber = 1;*/
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FinalServiceOrder> criteriaQuery = criteriaBuilder.createQuery(FinalServiceOrder.class);
        Root<FinalServiceOrder> root = criteriaQuery.from(FinalServiceOrder.class);
        criteriaQuery.select(root);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<FinalServiceOrder> subRoot = subquery.from(FinalServiceOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicateList = new ArrayList<>();

        //Added predicates For Organization level access.
        if(checkForUserCompany) {
            List<Predicate> companyPredicates = getPredicateForCompany(root, criteriaBuilder);
            predicateList.addAll(companyPredicates);
        }

        if (!StringUtils.isEmpty(searchCriteria.getBookingRefNo()))
            predicateList.add(criteriaBuilder.equal(root.get("bookingRefNo"), searchCriteria.getBookingRefNo()));

        if(searchCriteria.getOrderIds()!=null && searchCriteria.getOrderIds().size()!=0) {
            predicateList.add(root.get("orderIds").in(searchCriteria.getOrderIds().toArray(new String[]{})));
        }

        if(!StringUtils.isEmpty(searchCriteria.getOrderId()))
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
            predicates.add(criteriaBuilder.equal(subRoot.get("versionNumber"), searchCriteria.getVersionNumber()));*/

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

        if(searchCriteria.getDifferenceInSupplierInvoice()!=null)
            predicateList.add(criteriaBuilder.equal(root.get("diffAmount"),searchCriteria.getDifferenceInSupplierInvoice()));

        if(searchCriteria.getCreditOrDebitNoteNumber()!=null)
            predicateList.add(criteriaBuilder.equal(root.get("creditOrDebitNoteNumber"), searchCriteria.getCreditOrDebitNoteNumber()));

        if(searchCriteria.getDiffInGst()!=null)
            predicateList.add(criteriaBuilder.equal(root.get("diffInGst"), searchCriteria.getDiffInGst()));

        if(searchCriteria.getTotalDiffAmount()!=null)
            predicateList.add(criteriaBuilder.equal(root.get("totalDiffAmount"),searchCriteria.getTotalDiffAmount()));

        if(searchCriteria.getPaymentDueDate()!=null)
            predicateList.add(criteriaBuilder.equal(root.get("paymentDueDate"),searchCriteria.getPaymentDueDate()));

        if (searchCriteria.getPaymentDueDateFrom() != null && searchCriteria.getPaymentDueDateFrom() == null)
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("paymentDueDate"), searchCriteria.getPaymentDueDateFrom()));

        if (searchCriteria.getPaymentDueDateTo() == null && searchCriteria.getPaymentDueDateTo() != null)
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("paymentDueDate"), searchCriteria.getPaymentDueDateTo()));

        if(searchCriteria.getGeneralInvoice() != null)
            predicateList.add(criteriaBuilder.equal(root.get("isGeneralInvoice"),searchCriteria.getGeneralInvoice()));

        if(!StringUtils.isEmpty(searchCriteria.getInvoiceId()))
            predicateList.add(criteriaBuilder.equal(root.get("invoiceId"),searchCriteria.getInvoiceId()));

        if(!StringUtils.isEmpty(searchCriteria.getSupplierName()))
            predicateList.add(criteriaBuilder.equal(root.get("supplierName"),searchCriteria.getSupplierName()));

       /* if (searchCriteria.getSupplierBillPassing() != null && searchCriteria.getSupplierBillPassing()) {
            predicateList.add(criteriaBuilder.notEqual(root.get("productCategorySubTypeId"), OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory()));
        }*/

        predicateList.add(criteriaBuilder.notEqual(root.get("status"), Status.FINAL_SERVICE_ORDER_CANCELLED));


        if (searchCriteria.getAttachedServiceOrderIds() != null) {
            for (String id : searchCriteria.getAttachedServiceOrderIds()) {
                predicateList.add(criteriaBuilder.notEqual(root.get("uniqueId"), id));
            }
        }

        if (searchCriteria.getPaymentAdviceResource() != null && searchCriteria.getPaymentAdviceResource()) {
            Join<FinalServiceOrder, SupplierPricing> join = root.join("supplierPricing");
            predicateList.add(criteriaBuilder.notEqual(join.get("totalBalanceAmountPayable"), BigDecimal.ZERO));
        }

        if (!StringUtils.isEmpty(searchCriteria.getSupplierBillPassingStatus())) {
            //Bill Passing status will always be pending in case of Product-AIR as discussed with Ashish and confirmed by Alex
            //Refer Redmine Issue - 16993
            //So if pending records are searched, search for AIR FSOs since bill passing table has no records for such FSOs(as they do not go through bill passing)
            //This is just a way to show the records in UI without creating a bill passing record for such FSOs
            //(which anyways should not happen as they do not go through bill passing)
            if(searchCriteria.getSupplierBillPassingStatus().equalsIgnoreCase(SupplierBillPassingStatus.PENDING.getValue())) {
                predicateList.add(criteriaBuilder.equal(root.get("productCategoryId"), OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory()));
                predicateList.add(criteriaBuilder.equal(root.get("productCategorySubTypeId"), OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory()));
            }else {
                Join<FinalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing");
                predicateList.add(criteriaBuilder.equal(join.get("supplierBillPassingStatus"), searchCriteria.getSupplierBillPassingStatus()));
            }
        }

        if (searchCriteria.getPaymentAdviceId() != null) {
            Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.equal(paymentAdviceJoin.get("paymentAdviceNumber"),searchCriteria.getPaymentAdviceId()));
        }
        if (!StringUtils.isEmpty(searchCriteria.getPaymentAdviceStatusValues())) {
            Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.equal(paymentAdviceJoin.get("paymentAdviceStatus"), PaymentAdviceStatusValues.fromString(searchCriteria.getPaymentAdviceStatusValues())));
        }

        if (searchCriteria.getPaymentAdviceFromDate() != null && searchCriteria.getPaymentAdviceToDate() != null) {
            Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.between(paymentAdviceJoin.get("paymentAdviceGenerationDate"),searchCriteria.getPaymentAdviceFromDate(),searchCriteria.getPaymentAdviceToDate()));
        }

        if (searchCriteria.getPaymentAdviceFromDate() != null && searchCriteria.getPaymentAdviceToDate() == null) {
            Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(paymentAdviceJoin.get("paymentAdviceGenerationDate"),searchCriteria.getPaymentAdviceFromDate()));
        }

        if (searchCriteria.getPaymentAdviceFromDate() == null && searchCriteria.getPaymentAdviceToDate() != null) {
            Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(paymentAdviceJoin.get("paymentAdviceGenerationDate"),searchCriteria.getPaymentAdviceToDate()));
        }

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        if (searchCriteria.getSupplierBillPassingSortingCriteria()!=null ) {
            if (searchCriteria.getSupplierBillPassingSortingOrder()!=null && searchCriteria.getSupplierBillPassingSortingOrder()== SupplierBillPassingSortingOrder.DESC){
                if (searchCriteria.getSupplierBillPassingSortingCriteria()== SupplierBillPassingSortingCriteria.PaymentAdviceStatus){
                    Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.desc(paymentAdviceJoin.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else if (searchCriteria.getSupplierBillPassingSortingCriteria()==SupplierBillPassingSortingCriteria.SupplierBillPassingStatus){
                    Join<FinalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing");
                    criteriaQuery.orderBy(criteriaBuilder.desc(join.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else criteriaQuery.orderBy(criteriaBuilder.desc(root.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
            }
            else{
                if (searchCriteria.getSupplierBillPassingSortingCriteria()== SupplierBillPassingSortingCriteria.PaymentAdviceStatus){
                    Join<FinalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.asc(paymentAdviceJoin.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else if (searchCriteria.getSupplierBillPassingSortingCriteria()==SupplierBillPassingSortingCriteria.SupplierBillPassingStatus){
                    Join<FinalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing");
                    criteriaQuery.orderBy(criteriaBuilder.asc(join.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else criteriaQuery.orderBy(criteriaBuilder.asc(root.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
            }
        }

        TypedQuery<FinalServiceOrder> typedQuery = entityManager.createQuery(criteriaQuery);
        int count = typedQuery.getResultList().size();
        /*if (searchCriteria.getPageSize() == null || searchCriteria.getPageSize() == 0)
            searchCriteria.setPageSize(defaultPageSize);
        if (searchCriteria.getPageNumber() == null)
            searchCriteria.setPageNumber(defaultPageNumber);*/
        if (searchCriteria.getPageSize() != null && searchCriteria.getPageNumber() != null) {
            typedQuery.setFirstResult((searchCriteria.getPageNumber() - 1) * searchCriteria.getPageSize());
            typedQuery.setMaxResults(searchCriteria.getPageSize());
        }

        List<FinalServiceOrder> serviceOrderAndSupplierLiabilities = typedQuery.getResultList();
        Map<String, Object> result = new HashMap<>();
        result.put("result", serviceOrderAndSupplierLiabilities);
        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(searchCriteria.getPageSize(), count));
        return result;
    }

    private List<Predicate> getPredicateForCompany(Root<FinalServiceOrder> root, CriteriaBuilder criteriaBuilder){

        List<Predicate> predicateList = new ArrayList<>();
        OpsUser loggedInUser = userService.getLoggedInUser();
        if(loggedInUser!=null) {

            //TODO: uncomment Later
          /*  if (!StringUtils.isEmpty(loggedInUser.getBU()))
                predicateList.add(criteriaBuilder.equal(root.get("BU"), loggedInUser.getBU()));

            if (!StringUtils.isEmpty(loggedInUser.getSBU()))
                predicateList.add(criteriaBuilder.equal(root.get("SBU"), loggedInUser.getSBU()));*/

            if (!StringUtils.isEmpty(loggedInUser.getCompanyId()))
                predicateList.add(criteriaBuilder.equal(root.get("companyId"), loggedInUser.getCompanyId()));

            /*if (!StringUtils.isEmpty(loggedInUser.getCompanyName()))
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

    @Override
    public void paymentAdviceAutoGeneration() {

    }

    @Override
    public Map<String, Object> getFSOByIds(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FinalServiceOrder> criteriaQuery = criteriaBuilder.createQuery(FinalServiceOrder.class);
        Root<FinalServiceOrder> root = criteriaQuery.from(FinalServiceOrder.class);
        criteriaQuery.select(root);

        if (!StringUtils.isEmpty(sortCriteria)) {
            Join<FinalServiceOrder, SupplierBillPassing> supplierPricingJoin = root.join("supplierBillPassing", JoinType.LEFT);
            if(sortCriteria.equalsIgnoreCase("supplierName")) {
                criteriaQuery.orderBy(descending ? criteriaBuilder.desc(supplierPricingJoin.get("supplierName")) : criteriaBuilder.asc(supplierPricingJoin.get("supplierName")));
            }
            else if(sortCriteria.equalsIgnoreCase("inalServiceOrderID"))
                criteriaQuery.orderBy(descending ? criteriaBuilder.desc(root.get("finalServiceOrderID")) : criteriaBuilder.asc(root.get("finalServiceOrderID")));
            else
                criteriaQuery.orderBy(descending ? criteriaBuilder.desc(root.get("type")) : criteriaBuilder.asc(root.get("type")));
        }
        size = size !=null ? size : 5;
        page = page !=null ? page : 1;

        Expression<String> parentExpression = root.get("uniqueId");
        Predicate predicate = parentExpression.in(attachedServiceOrderIds);
        //TODO: Decide whether to include company predicates
        /*List<Predicate> companyPredicates = getPredicateForCompany(root, criteriaBuilder);
        companyPredicates.add(predicate);*/
        criteriaQuery.where(predicate);
        TypedQuery<FinalServiceOrder> query = entityManager.createQuery(criteriaQuery);
        Integer actualSize = query.getResultList().size();
        Integer startIndex = 0;

        startIndex = (page - 1) * size;
        query.setFirstResult(startIndex);
        query.setMaxResults(size);

        List<FinalServiceOrder> finalServiceOrderList = query.getResultList();
        return applyPagination(finalServiceOrderList, size, page, actualSize);
    }

    private Map<String, Object> applyPagination(List<FinalServiceOrder> forexBookings, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entity = new HashMap<>();
        entity.put("result", forexBookings);

        if (forexBookings.isEmpty())
            return entity;

        entity.put("size", size);
        entity.put("page", page);

        Integer noOfPages = 0;
        actualSize = (null == actualSize) ? 0 : actualSize;
        size = (null == size) ? actualSize : size;
        if (actualSize % size == 0)
            noOfPages = actualSize / size;
        else noOfPages = actualSize / size + 1;

        entity.put("size", size);
        entity.put("page", page);
        entity.put("noOfPages", noOfPages);
        return entity;
    }

    @Override
    public List<String> getAutoSuggestBookId(JSONObject req) {

        String suppId = req.optString("supplierId");
        String suppName = req.optString("supplierName");
        String bookId = req.optString("bookId");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<FinalServiceOrder> root = criteriaQuery.from(FinalServiceOrder.class);
        criteriaQuery.select(root.get("bookingRefNo"));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingRefNo")), "%" + bookId.trim().toLowerCase() + "%"));
        if(!StringUtils.isEmpty(suppId))
            predicates.add(criteriaBuilder.equal(root.get("supplierId"), suppId));
        if(!StringUtils.isEmpty(suppName))
            predicates.add(criteriaBuilder.equal(root.get("supplierName"), suppName));

        List<Predicate> companyPredicates = getPredicateForCompany(root, criteriaBuilder);
        predicates.addAll(companyPredicates);

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.distinct(true);

        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);
        List<String> bookingList = query.getResultList();

        return  bookingList;
    }
}
