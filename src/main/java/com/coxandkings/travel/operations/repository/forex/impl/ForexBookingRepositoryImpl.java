package com.coxandkings.travel.operations.repository.forex.impl;

import com.coxandkings.travel.operations.criteria.forex.ForexCriteria;
import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.forex.ForexIndent;
import com.coxandkings.travel.operations.model.forex.ForexPassenger;
import com.coxandkings.travel.operations.model.forex.TourCostDetails;
import com.coxandkings.travel.operations.repository.forex.ForexBookingRepository;
import com.coxandkings.travel.operations.resource.forex.PassengerNameResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class ForexBookingRepositoryImpl extends SimpleJpaRepository<ForexBooking, String> implements ForexBookingRepository {

    private EntityManager entityManager;

    public ForexBookingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ForexBooking.class, em);
        entityManager = em;
    }

    public Map<String, Object> getForexBookByCriteria(ForexCriteria criteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexBooking> criteriaQuery = criteriaBuilder.createQuery(ForexBooking.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        Integer size = criteria.getPageSize() !=null ? criteria.getPageSize() : 5;
        Integer page = criteria.getPageNumber()!=null ? criteria.getPageNumber() : 1;
        String sortCriteria = criteria.getSortCriteria();
        SetJoin<ForexBooking, ForexIndent> indentSet = root.joinSet("indentSet", JoinType.LEFT);

        if (!StringUtils.isEmpty(sortCriteria)) {
            criteriaQuery.orderBy(criteria.getDescending() ? criteriaBuilder.desc(root.get(sortCriteria)) :
                        criteriaBuilder.asc(root.get(sortCriteria)));
        }

        if (!StringUtils.isEmpty(criteria.getBookRefNo())) {
            predicates.add(criteriaBuilder.equal(root.get("bookRefNo"), criteria.getBookRefNo()));
        }
        if (!StringUtils.isEmpty(criteria.getClientName())) {
            predicates.add(criteriaBuilder.equal(root.get("clientName"), criteria.getClientName()));
        }

        Path<ZonedDateTime> date = root.get("createdAtTime");
        if (criteria.getFromDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(date, criteria.getFromDate()));
        }
        if (criteria.getToDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(date, criteria.getToDate().plusDays(1)));
        }

        if (!StringUtils.isEmpty(criteria.getTravelCountry())) {
            predicates.add(criteriaBuilder.equal(root.get("travelCountry"), criteria.getTravelCountry()));
        }

        if (!StringUtils.isEmpty(criteria.getSupplierName())) {
            predicates.add(criteriaBuilder.equal(indentSet.get("supplierName"), criteria.getSupplierName()));
        }
        if (!StringUtils.isEmpty(criteria.getReqOrIndentId())) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("requestId"), criteria.getReqOrIndentId()),
                    criteriaBuilder.equal(indentSet.get("id"), criteria.getReqOrIndentId())
            ));
        }

        if (RequestStatus.fromString(criteria.getRequestStatus()) != null) {
            predicates.add(criteriaBuilder.equal(root.get("requestStatus"), RequestStatus.fromString(criteria.getRequestStatus())));
        }

        if (IndentStatus.fromString(criteria.getIndentStatus()) != null) {
            predicates.add(criteriaBuilder.equal(indentSet.get("indentStatus"), IndentStatus.fromString(criteria.getIndentStatus())));
        }

        if (!StringUtils.isEmpty(criteria.getDisbursementStatus())) {
            predicates.add(criteriaBuilder.equal(indentSet.get("disbursementDetails").get("status"), criteria.getDisbursementStatus()));
        }

        //TODO: currency
        SetJoin<ForexBooking, ForexPassenger> paxSet = root.joinSet("forexPassengerSet");
        if (!StringUtils.isEmpty(criteria.getCurrency())) {
            Join<ForexPassenger, TourCostDetails> tourSet = paxSet.joinSet("tourCostDetails");
            predicates.add(criteriaBuilder.or(criteriaBuilder.equal(paxSet.get("personalExpenseDetails").get("currency"), criteria.getCurrency()),
                    criteriaBuilder.equal(tourSet.get("currency"), criteria.getCurrency())));
        }

        PassengerNameResource passengerName = criteria.getPassengerName();
        if (passengerName != null) {
            if (!StringUtils.isEmpty(passengerName.getFirstName())) {
                predicates.add(criteriaBuilder.equal(paxSet.get("firstName"), passengerName.getFirstName()));
            }
            if (!StringUtils.isEmpty(passengerName.getMiddleName())) {
                predicates.add(criteriaBuilder.equal(paxSet.get("middleName"), passengerName.getMiddleName()));
            }
            if (!StringUtils.isEmpty(passengerName.getLastName())) {
                predicates.add(criteriaBuilder.equal(paxSet.get("lastName"), passengerName.getLastName()));
            }
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        criteriaQuery.distinct(true);
        TypedQuery<ForexBooking> query = entityManager.createQuery(criteriaQuery);
        Integer actualSize = query.getResultList().size();
        Integer startIndex = 0;

        if (size != null && page != null) {
            startIndex = (page - 1) * size;
            query.setFirstResult(startIndex);
            query.setMaxResults(size);
        }

        List<ForexBooking> forexBookings = query.getResultList();
        return applyPagination(forexBookings, size, page, actualSize);

    }

    public ForexBooking getById(String id) {
        return this.findOne(id);
    }

    @Override
    public ForexBooking getByRequestId(String id) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexBooking> criteriaQuery = criteriaBuilder.createQuery(ForexBooking.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root);

        Predicate predicate = criteriaBuilder.equal(root.get("requestId"), id);
        criteriaQuery.where(predicate);
        TypedQuery<ForexBooking> query = entityManager.createQuery(criteriaQuery);

        ForexBooking forexBooking;
        try {
           forexBooking = query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
        return forexBooking;
    }

    @Override
    public List<String> getClientListForGivenName(String name) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root.get("clientName"));

        Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("clientName")), "%" + name.trim().toLowerCase() + "%");
        criteriaQuery.where(predicate);
        criteriaQuery.distinct(true);
        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);

        List<String> clientList = query.getResultList();
        return clientList;

    }

    @Override
    public List<String> getClientList() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root.get("clientName"));
        criteriaQuery.distinct(true);
        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);
        List<String> clientList = query.getResultList();

        return clientList;

    }

    @Override
    public List<String> getBookRefList() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root.get("bookRefNo"));
        criteriaQuery.distinct(true);
        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);
        List<String> bookingList = query.getResultList();

        return bookingList;

    }

    @Override
    public List<String> getBookRefListForGivenString(String bookRef) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root.get("bookRefNo"));

        Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("bookRefNo")), "%" + bookRef.trim().toLowerCase() + "%");
        criteriaQuery.where(predicate);
        criteriaQuery.distinct(true);
        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);

        List<String> bookingList = query.getResultList();
        return bookingList;
    }

    @Override
    public List<PassengerNameResource> getPaxListForGivenName(String paxName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PassengerNameResource> criteriaQuery = criteriaBuilder.createQuery(PassengerNameResource.class);
        Root<ForexPassenger> root = criteriaQuery.from(ForexPassenger.class);
        criteriaQuery.multiselect(root.get("firstName"), root.get("middleName"), root.get("lastName"));

        String names[] = paxName.trim().split(" ", 3);
        List<Predicate> predicates = new ArrayList<>();
        int j = 0;
        while (j < names.length) {
            Predicate p1 = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + names[j].toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName")), "%" + names[j].toLowerCase() + "%"));

            predicates.add(criteriaBuilder.or(p1,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + names[j].toLowerCase() + "%")));
            j++;
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.distinct(true);
        List<PassengerNameResource> namesList = entityManager.createQuery(criteriaQuery).getResultList();
        return namesList;
    }

    public List<String> getReqOrIndentIdsForGivenValue(String reqOrIndentId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);

        Root<ForexBooking> forexRoot = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(forexRoot.get("requestId"));
        Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(forexRoot.get("requestId")), "%" + reqOrIndentId.trim().toLowerCase() + "%");
        criteriaQuery.where(predicate);
        TypedQuery<String> query1 = entityManager.createQuery(criteriaQuery);

        SetJoin<ForexBooking, ForexIndent> indentSet = forexRoot.joinSet("indentSet");
        criteriaQuery.select(indentSet.get("id"));
        predicate = criteriaBuilder.like(criteriaBuilder.lower(indentSet.get("id")), "%" + reqOrIndentId.trim().toLowerCase() + "%");
        criteriaQuery.where(predicate);
        TypedQuery<String> query2 = entityManager.createQuery(criteriaQuery);

        List<String> resultList = query1.getResultList();
        resultList.addAll(query2.getResultList());
        return resultList;
    }

    @Override
    public Integer getRequestStatusCount(RequestStatus status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexBooking> criteriaQuery = criteriaBuilder.createQuery(ForexBooking.class);

        Root<ForexBooking> forexRoot = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(forexRoot);
        criteriaQuery.where(criteriaBuilder.equal(forexRoot.get("requestStatus"), status));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    @Override
    public ForexBooking getRecordByBookId(String bookID) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexBooking> criteriaQuery = criteriaBuilder.createQuery(ForexBooking.class);
        Root<ForexBooking> root = criteriaQuery.from(ForexBooking.class);
        criteriaQuery.select(root);

        Predicate predicate = criteriaBuilder.equal(root.get("bookRefNo"), bookID);
        criteriaQuery.where(predicate);
        TypedQuery<ForexBooking> query = entityManager.createQuery(criteriaQuery);

        ForexBooking forexBooking = null;
        try {
            forexBooking = query.getSingleResult();
        }catch(NoResultException|IllegalArgumentException e){
            return null;
        }
        return forexBooking;
    }

    private Map<String, Object> applyPagination(List<ForexBooking> forexBookings, Integer size, Integer page, Integer actualSize) {
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
    @Transactional
    public ForexBooking saveOrUpdate(ForexBooking forexBooking) {
        return this.saveAndFlush(forexBooking);
    }

}
