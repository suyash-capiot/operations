package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.commercialStatements.SettlementStatus;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingCriteria;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingOrder;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.Status;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.VersionId;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalServiceOrderRepository;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Transactional
@Repository
public class ProvisionalServiceOrderRepositoryImpl extends SimpleJpaRepository<ProvisionalServiceOrder, VersionId> implements ProvisionalServiceOrderRepository {

    private EntityManager entityManager;

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserService userService;

    private Logger logger = LogManager.getLogger(ProvisionalSupplierLiabilityRepositoryImpl.class);

    ProvisionalServiceOrderRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ProvisionalServiceOrder.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public ProvisionalServiceOrder generatePSO(ProvisionalServiceOrder provisionalServiceOrder) {
        return this.saveAndFlush(provisionalServiceOrder);
    }

    @Override
    public ProvisionalServiceOrder updatePSO(ProvisionalServiceOrder provisionalServiceOrder) {
        return this.saveAndFlush(provisionalServiceOrder);
    }

    @Override
    public ProvisionalServiceOrder getPSOByVersionId(VersionId versionId) {
        return this.findOne(versionId);
    }

    @Override
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria, Boolean checkForUserCompany) {

        Integer pageSize = searchCriteria.getPageSize() != null ? searchCriteria.getPageSize() : 10;
        Integer pageNumber = searchCriteria.getPageNumber() != null ? searchCriteria.getPageNumber() : 1;

        List<Predicate> companyPredicates = new ArrayList<>();
        List<Predicate> predicateList = new ArrayList<>();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProvisionalServiceOrder> criteriaQuery = criteriaBuilder.createQuery(ProvisionalServiceOrder.class);
        Root<ProvisionalServiceOrder> root = criteriaQuery.from(ProvisionalServiceOrder.class);
        criteriaQuery.select(root);

        if (checkForUserCompany){
            //Added predicates For Organization level access.
            companyPredicates = getPredicateForCompany(root, criteriaBuilder);
            predicateList.addAll(companyPredicates);
        }

       /* Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ProvisionalServiceOrder> subRoot = subquery.from(ProvisionalServiceOrder.class);
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

        if (searchCriteria.getLinkedVersion() != null)
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

        if (searchCriteria.getSupplierBillPassing() != null && searchCriteria.getSupplierBillPassing()) {
//            predicateList.add(criteriaBuilder.notEqual(root.get("productCategorySubTypeId"), OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory()));
//            Join<ProvisionalServiceOrder, SupplierPricing> supplierPricingJoin = root.join("supplierPricing", JoinType.LEFT);
            predicateList.add(criteriaBuilder.notEqual(root.get("supplierPricing").get("totalBalanceAmountPayable"), BigDecimal.ZERO));
        }

        if(searchCriteria.getOrderIds()!=null) {
            predicateList.add(root.get("orderId").in(searchCriteria.getOrderIds().toArray(new String[]{})));
        }

        predicateList.add(criteriaBuilder.notEqual(root.get("status"), Status.PROVISIONAL_SERVICE_ORDER_CANCELLED));

     /*   Expression expression = criteriaBuilder.max(subRoot.get("versionNumber"));
        subquery.select(expression);
        subquery.groupBy(subRoot.get("uniqueId"));
        predicates.add(criteriaBuilder.equal(root.get("uniqueId"), subRoot.get("uniqueId")));
        subquery.where(predicates.toArray(new Predicate[0]));

        predicateList.add(criteriaBuilder.equal(root.get("versionNumber"), subquery));*/

        if ((searchCriteria.getBillPassingResource() != null && searchCriteria.getBillPassingResource()) || (!StringUtils.isEmpty(searchCriteria.getSupplierBillPassingStatus()) && searchCriteria.getSupplierBillPassingStatus().equalsIgnoreCase(SupplierBillPassingStatus.PENDING.getValue())))
            predicateList.add(criteriaBuilder.isNull(root.get("finalServiceOrderID")));

        if (!StringUtils.isEmpty(searchCriteria.getAttachedServiceOrderIds())) {
            for (String id : searchCriteria.getAttachedServiceOrderIds()) {
                predicateList.add(criteriaBuilder.notEqual(root.get("uniqueId"), id));
            }
        }

        if (!StringUtils.isEmpty(searchCriteria.getSupplierBillPassingStatus()) && !searchCriteria.getSupplierBillPassingStatus().equalsIgnoreCase(SupplierBillPassingStatus.PENDING.getValue())) {
            Join<ProvisionalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing", JoinType.LEFT);
            predicateList.add(criteriaBuilder.equal(join.get("supplierBillPassingStatus"), searchCriteria.getSupplierBillPassingStatus()));
        }

        if (!StringUtils.isEmpty(searchCriteria.getPaymentAdviceId())) {
            Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.equal(paymentAdviceJoin.get("paymentAdviceNumber"), searchCriteria.getPaymentAdviceId()));
        }
        if (!StringUtils.isEmpty(searchCriteria.getPaymentAdviceStatusValues())) {
            Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.equal(paymentAdviceJoin.get("paymentAdviceStatus"), PaymentAdviceStatusValues.fromString(searchCriteria.getPaymentAdviceStatusValues())));
        }

        if (searchCriteria.getPaymentAdviceFromDate() != null && searchCriteria.getPaymentAdviceToDate() != null) {
            Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.between(paymentAdviceJoin.get("paymentAdviceGenerationDate"),searchCriteria.getPaymentAdviceFromDate(),searchCriteria.getPaymentAdviceToDate()));
        }

        if (searchCriteria.getPaymentAdviceFromDate() != null && searchCriteria.getPaymentAdviceToDate() == null) {
            Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(paymentAdviceJoin.get("paymentAdviceGenerationDate"),searchCriteria.getPaymentAdviceFromDate()));
        }

        if (searchCriteria.getPaymentAdviceFromDate() == null && searchCriteria.getPaymentAdviceToDate() != null) {
            Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet");
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(paymentAdviceJoin.get("paymentAdviceGenerationDate"),searchCriteria.getPaymentAdviceToDate()));
        }

        long startTime = System.currentTimeMillis();

     /*   CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(ProvisionalServiceOrder.class).get("uniqueId")));
        entityManager.createQuery(cq);
        cq.where(predicateList.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(cq).getSingleResult();*/

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        if (searchCriteria.getSupplierBillPassingSortingCriteria()!=null ) {
            if (searchCriteria.getSupplierBillPassingSortingOrder()!=null && searchCriteria.getSupplierBillPassingSortingOrder()== SupplierBillPassingSortingOrder.DESC){
                if (searchCriteria.getSupplierBillPassingSortingCriteria()== SupplierBillPassingSortingCriteria.PaymentAdviceStatus){
                    Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.desc(paymentAdviceJoin.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else if (searchCriteria.getSupplierBillPassingSortingCriteria()==SupplierBillPassingSortingCriteria.SupplierBillPassingStatus){
                    Join<ProvisionalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.desc(join.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else criteriaQuery.orderBy(criteriaBuilder.desc(root.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
            }
            else{
                if (searchCriteria.getSupplierBillPassingSortingCriteria()== SupplierBillPassingSortingCriteria.PaymentAdviceStatus){
                    Join<ProvisionalServiceOrder, PaymentAdvice> paymentAdviceJoin = root.join("paymentAdviceSet", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.asc(paymentAdviceJoin.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else if (searchCriteria.getSupplierBillPassingSortingCriteria()==SupplierBillPassingSortingCriteria.SupplierBillPassingStatus){
                    Join<ProvisionalServiceOrder, SupplierBillPassing> join = root.join("supplierBillPassing", JoinType.LEFT);
                    criteriaQuery.orderBy(criteriaBuilder.asc(join.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
                }
                else criteriaQuery.orderBy(criteriaBuilder.asc(root.get(searchCriteria.getSupplierBillPassingSortingCriteria().getSortByValue())));
            }
        }

        TypedQuery<ProvisionalServiceOrder> typedQuery = entityManager.createQuery(criteriaQuery);
        int count = typedQuery.getResultList().size();

        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<ProvisionalServiceOrder> serviceOrderAndSupplierLiabilities = typedQuery.getResultList();
        long endTime = System.currentTimeMillis();
        System.out.println((endTime-startTime)/1000);
//        Set<ProvisionalServiceOrder> provisionalServiceOrders = new HashSet<>(serviceOrderAndSupplierLiabilities);
//        serviceOrderAndSupplierLiabilities = new ArrayList<>(provisionalServiceOrders);
        Map<String, Object> result = new HashMap<>();
        result.put("result", serviceOrderAndSupplierLiabilities);
        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(pageSize, count));
        return result;
    }

    @Override
    public Map<String, Object> getProvisionalServiceOrders(ServiceOrderSearchCriteria searchCriteria) {

        return getProvisionalServiceOrders(searchCriteria , true);
    }

    private List<Predicate> getPredicateForCompany(Root<ProvisionalServiceOrder> root, CriteriaBuilder criteriaBuilder){

        List<Predicate> predicateList = new ArrayList<>();
        OpsUser loggedInUser =  userService.getLoggedInUser();
        if(loggedInUser!=null){
        //TODO: uncomment Later
      /*  if (!StringUtils.isEmpty(loggedInUser.getBU()))
            predicateList.add(criteriaBuilder.equal(root.get("BU"), loggedInUser.getBU()));

        if (!StringUtils.isEmpty(loggedInUser.getSBU()))
            predicateList.add(criteriaBuilder.equal(root.get("SBU"), loggedInUser.getSBU()));*/

        if (!StringUtils.isEmpty(loggedInUser.getCompanyId()))
            predicateList.add(criteriaBuilder.equal(root.get("companyId"), loggedInUser.getCompanyId()));

      /*  if (!StringUtils.isEmpty(loggedInUser.getCompanyName()))
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
            predicateList.add(criteriaBuilder.equal(root.get("branchName"), loggedInUser.getBranchName()));
*/      }
        return predicateList;

    }

    @Override
    public Long getCount() {
        return this.count();
    }

    @Override
    public List<ProvisionalServiceOrder> supplierBillPassingScheduler() {

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProvisionalServiceOrder> criteriaQuery = criteriaBuilder.createQuery(ProvisionalServiceOrder.class);
            Root<ProvisionalServiceOrder> root = criteriaQuery.from(ProvisionalServiceOrder.class);
            criteriaQuery.select(root);
            //Join<ProvisionalServiceOrder, SupplierBillPassing> finalJoin = root.join("supplierBillPassing");

            ZonedDateTime startDate = ZonedDateTime.now().minusDays(1).withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp startTime = Timestamp.from(startDate.toInstant());
            ZonedDateTime endDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp endTime = Timestamp.from(endDate.toInstant());
            Predicate p1 = criteriaBuilder.between(root.get("paymentDueDate"), startTime, endTime);
            //p1 = criteriaBuilder.and(p1, criteriaBuilder.isNull(root.get("supplierBillPassing")));
            //Predicate p2 = criteriaBuilder.between(root.get("paymentDueDate"), startTime, endTime);
            //p2 = criteriaBuilder.and(p2, criteriaBuilder.isNotNull(finalJoin.get("supplierBillPassingStatus")));
            p1 = criteriaBuilder.and(p1, criteriaBuilder.notEqual(root.get("supplierSettlementStatus"), SettlementStatus.SETTLED.getValue()));
            //Predicate result = criteriaBuilder.or(p1, p2);

            criteriaQuery.where(p1);
            List<ProvisionalServiceOrder> provisionalServiceOrders = null;
            provisionalServiceOrders = entityManager.createQuery(criteriaQuery).getResultList();
            return provisionalServiceOrders;
        } catch (Exception e) {
            logger.error("No records found/exception occured in supplierBillPassingBatchJob");
            return new ArrayList<>();
        }
    }

    @Override
    public void releasePaymentsScheduler() {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProvisionalServiceOrder> criteriaQuery = criteriaBuilder.createQuery(ProvisionalServiceOrder.class);
            Root<ProvisionalServiceOrder> root = criteriaQuery.from(ProvisionalServiceOrder.class);
            List<Predicate> predicates = new ArrayList<>();

            ZonedDateTime startDate = ZonedDateTime.now().minusHours(1).withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp startTime = Timestamp.from(startDate.toInstant());
            ZonedDateTime endDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp endTime = Timestamp.from(endDate.toInstant());
            predicates.add(criteriaBuilder.between(root.get("stopPaymentTillDate"), startTime, endTime));
            predicates.add(criteriaBuilder.equal(root.get("stopPaymentStatus"), StopPaymentStatus.ACTIVE));
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
            List<ProvisionalServiceOrder> provisionalServiceOrders = null;
            provisionalServiceOrders = entityManager.createQuery(criteriaQuery).getResultList();
            if (provisionalServiceOrders != null) {
                provisionalServiceOrders.stream().forEach(provisionalServiceOrder -> {
                    provisionalServiceOrder.setStopPaymentStatus(StopPaymentStatus.INACTIVE);
                    this.saveAndFlush(provisionalServiceOrder);
                    try {
                        releasePaymentAlert(provisionalServiceOrder);
                    } catch (Exception e) {
                        logger.error("Error occured while sending release payment alert");
                    }
                });
            }
        } catch (Exception e) {
            logger.error("exception occured in stopPaymentBatchJob");
        }
    }

    @Override
    public JSONArray getAutoSuggestBookId(String bookId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ProvisionalServiceOrder> root = criteriaQuery.from(ProvisionalServiceOrder.class);
        criteriaQuery.select(root.get("bookingRefNo"));

        Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingRefNo")), "%" + bookId.trim().toLowerCase() + "%");
        List<Predicate> companyPredicates = getPredicateForCompany(root, criteriaBuilder);
        companyPredicates.add(predicate);

        criteriaQuery.where(companyPredicates.toArray(new Predicate[0]));
        criteriaQuery.distinct(true);

        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);
        List<String> bookingList = query.getResultList();

        JSONArray res = new JSONArray();
        for(String bookRef :  bookingList){
            res.put(new JSONObject().put("bookingRefNumber", bookRef));
        }
        return res;
    }

    @Override
    public Map<String, Object> getPSOByIds(List<String> attachedServiceOrderIds, Integer page, Integer size, String sortCriteria, boolean descending) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProvisionalServiceOrder> criteriaQuery = criteriaBuilder.createQuery(ProvisionalServiceOrder.class);
        Root<ProvisionalServiceOrder> root = criteriaQuery.from(ProvisionalServiceOrder.class);
        criteriaQuery.select(root);

        if (!StringUtils.isEmpty(sortCriteria)) {
            Join<ProvisionalServiceOrder, SupplierBillPassing> supplierPricingJoin = root.join("supplierBillPassing", JoinType.LEFT);
            if(sortCriteria.equalsIgnoreCase("supplierName")) {
                criteriaQuery.orderBy(descending ? criteriaBuilder.desc(supplierPricingJoin.get("supplierName")) : criteriaBuilder.asc(supplierPricingJoin.get("supplierName")));
            }
            else if(sortCriteria.equalsIgnoreCase("provisionalServiceOrderID"))
                criteriaQuery.orderBy(descending ? criteriaBuilder.desc(root.get("provisionalServiceOrderID")) : criteriaBuilder.asc(root.get("provisionalServiceOrderID")));
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
        TypedQuery<ProvisionalServiceOrder> query = entityManager.createQuery(criteriaQuery);
        Integer actualSize = query.getResultList().size();
        Integer startIndex = 0;

        startIndex = (page - 1) * size;
        query.setFirstResult(startIndex);
        query.setMaxResults(size);

        List<ProvisionalServiceOrder> provisionalServiceOrderList = query.getResultList();
        return applyPagination(provisionalServiceOrderList, size, page, actualSize);

    }

    @Override
    public List<String> getAutoSuggestBookId(JSONObject req) {
        String suppId = req.optString("supplierId");
        String suppName = req.optString("supplierName");
        String bookId = req.optString("bookId");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ProvisionalServiceOrder> root = criteriaQuery.from(ProvisionalServiceOrder.class);
        criteriaQuery.select(root.get("bookingRefNo"));

        List<Predicate> predicates = new ArrayList<>();
        if(!StringUtils.isEmpty(bookId))
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
        return bookingList;
    }

    private Map<String, Object> applyPagination(List<ProvisionalServiceOrder> forexBookings, Integer size, Integer page, Integer actualSize) {
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

    public void releasePaymentAlert(ProvisionalServiceOrder provisionalServiceOrder) throws OperationException, ParseException {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        inlineMessageResource.setAlertName("RELEASE_PAYMENT");
        inlineMessageResource.setNotificationType("System");
        ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
        entity.put("provisionalServiceOrderId", provisionalServiceOrder.getUniqueId());
        inlineMessageResource.setDynamicVariables(entity);
        alertService.sendInlineMessageAlert(inlineMessageResource);
    }


}

