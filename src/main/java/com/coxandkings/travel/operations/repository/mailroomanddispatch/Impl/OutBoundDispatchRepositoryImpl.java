package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.ExistingOutboundDispatchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.OutBoundDispatchCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.OutBoundDispatchRepository;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional(readOnly=false)
@Repository
public class OutBoundDispatchRepositoryImpl extends SimpleJpaRepository<OutboundDispatch, String> implements OutBoundDispatchRepository {


    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(OutBoundDispatchRepositoryImpl.class);

    public OutBoundDispatchRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(OutboundDispatch.class, em);
        entityManager = em;
    }
    public OutboundDispatch saveOrUpdate(OutboundDispatch outboundDispatch) {

        return saveAndFlush(outboundDispatch);
    }

    public Map<String, Object> getByOutBoundCriteria(OutBoundDispatchCriteria outBoundDispatchCriteria) {
        Integer defaultPageSize = 10;
        Integer defaultPageNumber = 1;

        Integer size = outBoundDispatchCriteria.getPageSize() !=null ? outBoundDispatchCriteria.getPageSize() : 5;
        Integer page = outBoundDispatchCriteria.getPageNumber()!=null ? outBoundDispatchCriteria.getPageNumber() : 1;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OutboundDispatch> outBoundEntryCriteriaQuery = criteriaBuilder.createQuery(OutboundDispatch.class);
        Root<OutboundDispatch> root = outBoundEntryCriteriaQuery.from(OutboundDispatch.class);
        List<Predicate> predicates = new ArrayList<>();
        if (outBoundDispatchCriteria.getId() != null && !outBoundDispatchCriteria.getId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("dispatchId"), outBoundDispatchCriteria.getId()));
        }
        if (outBoundDispatchCriteria.getParcelId() != null && !outBoundDispatchCriteria.getParcelId().isEmpty()) {
            Join<OutboundDispatch, Parcel> parcelId = root.join("parcel");
            predicates.add(criteriaBuilder.equal(parcelId.get("parcelId"), outBoundDispatchCriteria.getParcelId()));
        }
        if (outBoundDispatchCriteria.getPassportNumber() != null && !outBoundDispatchCriteria.getPassportNumber().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("passportNumber"), outBoundDispatchCriteria.getPassportNumber()));
        }
        if(outBoundDispatchCriteria.getReferenceNumber() != null && !outBoundDispatchCriteria.getReferenceNumber().isEmpty()){
            predicates.add(criteriaBuilder.equal(root.get("referenceNumber"), outBoundDispatchCriteria.getReferenceNumber()));
        }
        if (outBoundDispatchCriteria.getEmployeeName() != null && !outBoundDispatchCriteria.getEmployeeName().isEmpty()) {
            Join<OutboundDispatch, DispatchSenderDetails> emp = root.join("dispatchSenderDetails");
            predicates.add(criteriaBuilder.equal(emp.get("employeeName"), outBoundDispatchCriteria.getEmployeeName()));
        }
        if (outBoundDispatchCriteria.getEmployeeId() != null && !outBoundDispatchCriteria.getEmployeeId().isEmpty()) {
            Join<OutboundDispatch, DispatchSenderDetails> empId = root.join("dispatchSenderDetails");
            predicates.add(criteriaBuilder.equal(empId.get("employeeId"), outBoundDispatchCriteria.getEmployeeId()));
        }
        if (outBoundDispatchCriteria.getPassengerName() != null && !outBoundDispatchCriteria.getPassengerName().isEmpty()) {
            /*Join<OutboundDispatch, ExternalRecipient> passengerName = root.join("recipientDetailsList");*/
            predicates.add(criteriaBuilder.equal(root.get("leadPassengerName"), outBoundDispatchCriteria.getPassengerName()));
        }
        if (outBoundDispatchCriteria.getSupplierName() != null && !outBoundDispatchCriteria.getSupplierName().isEmpty()) {
            //Join<OutboundDispatch, DispatchSenderDetails> supplier = root.join("dispatchSenderDetails");
            Join<OutboundDispatch, DispatchCostDetails> supplierName = root.join("dispatchCostDetails");
            predicates.add(criteriaBuilder.equal(supplierName.get("supplierName"), outBoundDispatchCriteria.getSupplierName()));
        }
        if (outBoundDispatchCriteria.getDispatchStatus() != null) {
            Join<OutboundDispatch, OutboundDispatchStatus> dispatchStatus = root.join("status");
            predicates.add(criteriaBuilder.equal(dispatchStatus.get("status"), outBoundDispatchCriteria.getDispatchStatus()));
        }

        if (!predicates.isEmpty()) {
            outBoundEntryCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        int count;
        TypedQuery<OutboundDispatch> typedQuery = entityManager.createQuery(outBoundEntryCriteriaQuery);
        List<OutboundDispatch> outboundDispatchList = typedQuery.getResultList();
        if(outboundDispatchList != null && outboundDispatchList.size() > 0){
            count = typedQuery.getResultList().size();    }else {count= 0;}
        if (outBoundDispatchCriteria.getPageSize() == null || outBoundDispatchCriteria.getPageSize() == 0)
            outBoundDispatchCriteria.setPageSize(defaultPageSize);
        /*if (searchCriteria.getPageNumber() == null)
            searchCriteria.setPageNumber(defaultPageNumber);*/
        if (outBoundDispatchCriteria.getPageSize() != null && outBoundDispatchCriteria.getPageNumber() != null) {
            typedQuery.setFirstResult((outBoundDispatchCriteria.getPageNumber() - 1) * outBoundDispatchCriteria.getPageSize());
            typedQuery.setMaxResults(outBoundDispatchCriteria.getPageSize());
        }

        List<OutboundDispatch> serviceOrderAndSupplierLiabilities = typedQuery.getResultList();
        Map<String, Object> result = new HashMap<>();
        result.put("result", serviceOrderAndSupplierLiabilities);
        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(outBoundDispatchCriteria.getPageSize(), count));
        return applyPagination(serviceOrderAndSupplierLiabilities,size,page,count);
    }

    private Map<String, Object> applyPagination(List<OutboundDispatch> outboundDispatches, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entity = new HashMap<>();
        entity.put("data", outboundDispatches);
        entity.put("totalCount", outboundDispatches.size());

        if (outboundDispatches.isEmpty())
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
    public OutboundDispatch getOutBoundId(String dispatchId) {

        return this.findOne(dispatchId);
    }

    /* public void deleteDispatch(String id){
         CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
         CriteriaQuery<OutboundDispatch> criteria = criteriaBuilder.createQuery( OutboundDispatch.class );
         Root<OutboundDispatch> root = criteria.from( OutboundDispatch.class );
         Join<OutboundDispatch,OutboundStatus> status=root.join("dicpatchStatus");
         criteria.where(root.get("id").in());
         return  entityManager.createQuery( criteria ).getSingleResult();

     }*/
    public void deleteDispatch(String id) {
        delete(id);
        flush();
    }

    @Override
    public List<OutboundDispatch> getAllOutboundDetails() {
        return this.findAll();
    }

    @Override
    public List<OutboundDispatch> getByExistingOutboundCriteria(ExistingOutboundDispatchCriteria existingOutboundDispatchCriteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OutboundDispatch> outBoundEntryCriteriaQuery = criteriaBuilder.createQuery(OutboundDispatch.class);
        Root<OutboundDispatch> root = outBoundEntryCriteriaQuery.from(OutboundDispatch.class);
        List<Predicate> predicates = new ArrayList<>();
        if (existingOutboundDispatchCriteria.getDispatchId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("dispatchId"), existingOutboundDispatchCriteria.getDispatchId()));
        }
        if (existingOutboundDispatchCriteria.getLinkInBoudNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("linkInBoudNumber"), existingOutboundDispatchCriteria.getLinkInBoudNumber()));
        }
        if (existingOutboundDispatchCriteria.getReferenceNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("referenceNumber"), existingOutboundDispatchCriteria.getReferenceNumber()));
        }
        if (!predicates.isEmpty()) {
            outBoundEntryCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        List<OutboundDispatch> result = entityManager.createQuery(outBoundEntryCriteriaQuery).getResultList();

        return result;

    }

    @Override
    public List<String> getPassengarNames(String param) {

        List<String> passNamesList,passNames;

        List<OutboundDispatch> outboundDispatches = this.findAll();
        String regex = new StringBuilder().append(".*").append("(?i)").append(param).append(".*").toString().trim();
        java.util.function.Predicate<String> predicateRegex = Pattern.compile(regex).asPredicate();

        passNamesList = outboundDispatches.stream().map(x -> x.getLeadPassengerName()).collect(Collectors.toList());
        return passNamesList.stream().filter(predicateRegex).collect(Collectors.toList());
    }
}


