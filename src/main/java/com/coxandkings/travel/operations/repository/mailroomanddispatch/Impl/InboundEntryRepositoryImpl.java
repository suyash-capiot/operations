package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.InboundEntryCriteria;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.InboundEntryRepository;
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
public class InboundEntryRepositoryImpl extends SimpleJpaRepository<InboundEntry, String> implements InboundEntryRepository {

    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(InboundEntryRepositoryImpl.class);

    public InboundEntryRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(InboundEntry.class, em);
        entityManager = em;
    }


    public InboundEntry saveOrUpdate(InboundEntry inboundEntry) {

        return saveAndFlush(inboundEntry);
    }

    public Map<String, Object> getByInboundCriteria(InboundEntryCriteria inboundEntryCriteria) {

        Integer size = inboundEntryCriteria.getPageSize() !=null ? inboundEntryCriteria.getPageSize() : 5;
        Integer page = inboundEntryCriteria.getPageNumber()!=null ? inboundEntryCriteria.getPageNumber() : 1;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<InboundEntry> inboundEntryCriteriaQuery = criteriaBuilder.createQuery(InboundEntry.class);
        Root<InboundEntry> root = inboundEntryCriteriaQuery.from(InboundEntry.class);
        List<Predicate> predicates = new ArrayList<>();
        if (inboundEntryCriteria.getId() != null && !inboundEntryCriteria.getId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("id"), inboundEntryCriteria.getId()));
        }
        if (inboundEntryCriteria.getInboundNo() != null && !inboundEntryCriteria.getInboundNo().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("inboundNo"), inboundEntryCriteria.getInboundNo()));
        }
        if (inboundEntryCriteria.getAwbNo() != null && !inboundEntryCriteria.getAwbNo().isEmpty()){
            predicates.add(criteriaBuilder.equal(root.get("awbNo"), inboundEntryCriteria.getAwbNo()));
        }
        if (inboundEntryCriteria.getMailRoomName() != null && !inboundEntryCriteria.getMailRoomName().isEmpty()) {
            Join<InboundEntry, MailRoomMaster> roomName = root.join("mailRoomMaster");
            predicates.add(criteriaBuilder.equal(roomName.get("mailRoomName"), inboundEntryCriteria.getMailRoomName()));
        }
        if (inboundEntryCriteria.getSenderName() != null && !inboundEntryCriteria.getSenderName().isEmpty()) {
            Join<InboundEntry, InboundSenderDetails> senderName = root.join("inboundSenderDetails");
            predicates.add(criteriaBuilder.equal(senderName.get("senderName"), inboundEntryCriteria.getSenderName()));
        }
        if (inboundEntryCriteria.getRecipientName() != null && !inboundEntryCriteria.getRecipientName().isEmpty()) {
            Join<InboundEntry, InboundRecipientDetails> recipientName = root.join("inboundRecipientDetails");
            predicates.add(criteriaBuilder.equal(recipientName.get("empName"), inboundEntryCriteria.getRecipientName()));
        }
        if (inboundEntryCriteria.getFloor() != null && !inboundEntryCriteria.getFloor().isEmpty()) {
            Join<InboundEntry, MailRoomMaster> floor = root.join("mailRoomMaster");
            //predicates.add(criteriaBuilder.equal(floor.get("mailRoomOnFloor"), inboundEntryCriteria.getFloor()));
            predicates.add(criteriaBuilder.isMember(inboundEntryCriteria.getFloor(),floor.get("mailRoomOnFloor")));
        }

        if (inboundEntryCriteria.getReceiptFromDate() != null && inboundEntryCriteria.getReceiptToDate() != null) {
            predicates.add(criteriaBuilder.between(root.get("receivedDate"), inboundEntryCriteria.getReceiptFromDate(),inboundEntryCriteria.getReceiptToDate()));
        }

        if (inboundEntryCriteria.getStatusId() != null) {
            Join<InboundEntry, InboundLogEntryStatus> status = root.join("inboundLogEntryStatus");
            predicates.add(criteriaBuilder.equal(status.get("status"), inboundEntryCriteria.getStatusId()));
        }
        if (!predicates.isEmpty()) {
            inboundEntryCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<InboundEntry> typedQuery = entityManager.createQuery(inboundEntryCriteriaQuery);
        int count;
         List<InboundEntry> inboundEntryList = typedQuery.getResultList();
        if(inboundEntryList != null && inboundEntryList.size() > 0){
            count = typedQuery.getResultList().size();    }else {count= 0;}
        if (inboundEntryCriteria.getPageSize() == null || inboundEntryCriteria.getPageSize() == 0)
            inboundEntryCriteria.setPageSize(size);
        /*if (searchCriteria.getPageNumber() == null)
            searchCriteria.setPageNumber(defaultPageNumber);*/
        if (inboundEntryCriteria.getPageSize() != null && inboundEntryCriteria.getPageNumber() != null) {
            typedQuery.setFirstResult((inboundEntryCriteria.getPageNumber() - 1) * inboundEntryCriteria.getPageSize());
            typedQuery.setMaxResults(inboundEntryCriteria.getPageSize());
        }

        List<InboundEntry> inboundEntries = typedQuery.getResultList();
//        Map<String, Object> result = new HashMap<>();
//        result.put("result", inboundEntries);
//        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(inboundEntryCriteria.getPageSize(), count));
        return applyPagination(inboundEntries,size,page,count );
    }

    private Map<String, Object> applyPagination(List<InboundEntry> InboundEntry, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entity = new HashMap<>();
        entity.put("data", InboundEntry);
        entity.put("totalCount", actualSize);

        if (InboundEntry.isEmpty())
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

    public InboundEntry getInboundId(String inboundId) {

        return this.findOne(inboundId);
    }

    public List<InboundRecipientDetails> suggest(String attribute, String value) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<InboundRecipientDetails> InboundRecipientCriteriaQuery = criteriaBuilder.createQuery(InboundRecipientDetails.class);
        Root<InboundRecipientDetails> root = InboundRecipientCriteriaQuery.from(InboundRecipientDetails.class);
        List<Predicate> predicates = new ArrayList<>();
        if (attribute != null && value != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(attribute).as(String.class)), "%" + value.toUpperCase() + "%"));
        }
        InboundRecipientCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(InboundRecipientCriteriaQuery).getResultList();

    }


    @Override
    public List<String> inboundNoList(String param) {
        List<String> inboundNamesList;

        List<InboundEntry> inboundEntries = this.findAll();
        String regex = new StringBuilder().append(".*").append("(?i)").append(param).append(".*").toString().trim();
        java.util.function.Predicate<String> predicateRegex = Pattern.compile(regex).asPredicate();
        inboundNamesList = inboundEntries.stream().map(x -> x.getInboundNo()).collect(Collectors.toList());
        return inboundNamesList.parallelStream().filter(predicateRegex).collect(Collectors.toList());
    }

    @Override
    public List<String> awbNo() {
        List<String> inboundEntryList = new ArrayList<>();
        List<InboundEntry> inboundEntries = this.findAll();
        List<String>inboundEntryL = inboundEntries.parallelStream().map(x -> x.getAwbNo()).collect(Collectors.toList());
        Set<String> inboundEntrySet = new HashSet<>(inboundEntryL);
        inboundEntryList = inboundEntrySet.stream().collect(Collectors.toList());
        return inboundEntryList;
    }

    @Override
    public List<String> senderNames() {
        List<String> inboundEntryList = new ArrayList<>();
        List<InboundEntry> inboundEntries = this.findAll();
        List<String> inboundEntryL = inboundEntries.parallelStream().map(x -> x.getInboundSenderDetails().getSenderName()).collect(Collectors.toList());
        Set<String> inboundEntrySet = new HashSet<>(inboundEntryL);
         inboundEntryList = inboundEntrySet.stream().collect(Collectors.toList());
         return inboundEntryList;
    }

    @Override
    public List<String> recipientNames() {
        List<String> inboundEntryList = new ArrayList<>();
        List<InboundEntry> inboundEntries = this.findAll();
        List<String> inboundEntryL = inboundEntries.parallelStream().map(x -> x.getInboundRecipientDetails().getEmpName()).collect(Collectors.toList());
        Set<String> inboundEntrySet = new HashSet<>(inboundEntryL);
        inboundEntryList = inboundEntrySet.stream().collect(Collectors.toList());
        return inboundEntryList;
    }

    @Override
    public List<String> departments() {
        List<String> inboundEntryList = new ArrayList<>();
        List<InboundEntry> inboundEntries = this.findAll();
        List<String> inboundEntryL = inboundEntries.parallelStream().map(x -> x.getInboundRecipientDetails().getEmpName()).collect(Collectors.toList());
        Set<String> inboundEntrySet = new HashSet<>(inboundEntryL);
        inboundEntryList = inboundEntrySet.stream().collect(Collectors.toList());
        return inboundEntryList;
    }

}
