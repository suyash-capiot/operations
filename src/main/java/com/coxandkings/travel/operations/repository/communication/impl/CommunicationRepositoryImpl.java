package com.coxandkings.travel.operations.repository.communication.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationUnreadCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import com.coxandkings.travel.operations.repository.communication.CommunicationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Transactional
@Repository
public class CommunicationRepositoryImpl extends SimpleJpaRepository<BaseCommunication, String> implements CommunicationRepository {

    private EntityManager entityManager;

    public CommunicationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(BaseCommunication.class, em);
        entityManager = em;
    }

    @Override
    public BaseCommunication getCommunicationById(String id) {
        return this.findOne(id);
    }

    @Override
    public Map<String, Object> getCommunicationByBookingId(String bookId, Integer size, Integer page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseCommunication> criteriaQuery = criteriaBuilder.createQuery(BaseCommunication.class);
        Root<BaseCommunication> root = criteriaQuery.from(BaseCommunication.class);
        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateTime")));
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookId)) {
            predicates.add(criteriaBuilder.equal(root.get("bookId"), bookId));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<BaseCommunication> query = entityManager.createQuery(criteriaQuery);
        List<BaseCommunication> resultList = query.getResultList();

        Integer actualSize = query.getResultList().size();
        if (size != null && page != null) {
            Integer startIndex = (page - 1) * size;
            query.setFirstResult(startIndex);
            query.setMaxResults(size);
        }

        List<BaseCommunication> baseCommunications = query.getResultList();
        return applyPagination(baseCommunications, size, page, actualSize);

        //return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Map<String, Object> getCommunicationByBookingId(String bookId, Integer size, Integer page, String sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseCommunication> criteriaQuery = criteriaBuilder.createQuery(BaseCommunication.class);
        Root<BaseCommunication> root = criteriaQuery.from(BaseCommunication.class);
        criteriaQuery.select(root);
        if (sort.equalsIgnoreCase("ASC"))
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("dateTime")));
        else
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateTime")));
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookId)) {
            predicates.add(criteriaBuilder.equal(root.get("bookId"), bookId));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<BaseCommunication> query = entityManager.createQuery(criteriaQuery);
        Integer actualSize = query.getResultList().size();
        if (size != null && page != null) {
            Integer startIndex = (page - 1) * size;
            query.setFirstResult(startIndex);
            query.setMaxResults(size);
        }

        List<BaseCommunication> baseCommunications = query.getResultList();
        return applyPagination(baseCommunications, size, page, actualSize);

        //return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private Map<String, Object> applyPagination(List<BaseCommunication> baseCommunications, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entiy = new HashMap<>();
        entiy.put("result", baseCommunications);

        if (baseCommunications.isEmpty()) return entiy;

        entiy.put("size", size);
        entiy.put("page", page);

        Integer noOfPages = 0;
        actualSize = (null == actualSize) ? 0 : actualSize;
        size = (null == size) ? 0 : size;
        if (actualSize % size == 0)
            noOfPages = actualSize / size;
        else noOfPages = actualSize / size + 1;

        entiy.put("size", size);
        entiy.put("page", page);
        entiy.put("noOfPages", noOfPages);
        return entiy;
    }

    @Override
    public Map<String, Object> getCommunicationByUserId(String userId, Integer size, Integer page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseCommunication> criteriaQuery = criteriaBuilder.createQuery(BaseCommunication.class);
        Root<BaseCommunication> root = criteriaQuery.from(BaseCommunication.class);
        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateTime")));
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(userId)) {
            predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        TypedQuery<BaseCommunication> query = entityManager.createQuery(criteriaQuery);
        Integer actualSize = query.getResultList().size();
        if (size != null && page != null) {
            Integer startIndex = (page - 1) * size;
            query.setFirstResult(startIndex);
            query.setMaxResults(size);
        }

        List<BaseCommunication> baseCommunications = query.getResultList();
        return applyPagination(baseCommunications, size, page, actualSize);
    }

    @Override
    public Map<String, Object> getCommunicationByUserId(String userId, Integer size, Integer page, String sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseCommunication> criteriaQuery = criteriaBuilder.createQuery(BaseCommunication.class);
        Root<BaseCommunication> root = criteriaQuery.from(BaseCommunication.class);
        criteriaQuery.select(root);
        if (sort.equalsIgnoreCase("ASC"))
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("dateTime")));
        else
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateTime")));
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(userId)) {
            predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        TypedQuery<BaseCommunication> query = entityManager.createQuery(criteriaQuery);
        Integer actualSize = query.getResultList().size();
        if (size != null && page != null) {
            Integer startIndex = (page - 1) * size;
            query.setFirstResult(startIndex);
            query.setMaxResults(size);
        }

        List<BaseCommunication> baseCommunications = query.getResultList();
        return applyPagination(baseCommunications, size, page, actualSize);
    }

    @Override
    public Map<String, Long> getUnreadCounts(CommunicationUnreadCriteria communicationUnreadCriteria) {
        Long unreadEmail = 0L, unreadSMS = 0L, unreadCallRecord = 0L, unreadChat = 0L, unreadActivityLog = 0L, unreadLetter = 0L;

        if (communicationUnreadCriteria.getBookId() != null && communicationUnreadCriteria.getUserId() != null) {
            unreadEmail = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Email e WHERE e.bookId=:bookId AND e.userId=:userId AND e.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadSMS = (Long) entityManager.createQuery("SELECT COUNT(*) FROM OutboundSMS sms WHERE sms.bookId=:bookId AND sms.userId=:userId AND sms.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadCallRecord = (Long) entityManager.createQuery("SELECT COUNT(*) FROM CallRecord cr WHERE cr.bookId=:bookId AND cr.userId=:userId AND cr.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadChat = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Chat chat WHERE chat.bookId=:bookId  AND chat.userId=:userId AND chat.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadActivityLog = (Long) entityManager.createQuery("SELECT COUNT(*) FROM ActivityLog al WHERE al.bookId=:bookId AND al.userId=:userId AND al.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadLetter = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Letter l WHERE l.bookId=:bookId AND l.userId=:userId AND l.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
        } else if (communicationUnreadCriteria.getUserId() != null && communicationUnreadCriteria.getBookId() == null) {
            unreadEmail = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Email e WHERE  e.userId=:userId AND e.isRead=:isRead").setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadSMS = (Long) entityManager.createQuery("SELECT COUNT(*) FROM OutboundSMS sms WHERE sms.userId=:userId AND sms.isRead=:isRead").setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadCallRecord = (Long) entityManager.createQuery("SELECT COUNT(*) FROM CallRecord cr WHERE  cr.userId=:userId AND cr.isRead=:isRead").setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadChat = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Chat chat WHERE  chat.userId=:userId AND chat.isRead=:isRead").setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadActivityLog = (Long) entityManager.createQuery("SELECT COUNT(*) FROM ActivityLog al WHERE  al.userId=:userId AND al.isRead=:isRead").setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);
            unreadLetter = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Letter l WHERE  l.userId=:userId AND l.isRead=:isRead").setParameter("userId", communicationUnreadCriteria.getUserId()).setParameter("isRead", false).getResultList().get(0);

        } else if (communicationUnreadCriteria.getBookId() != null && communicationUnreadCriteria.getUserId() == null) {
            unreadEmail = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Email e WHERE e.bookId=:bookId AND e.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("isRead", false).getResultList().get(0);
            unreadSMS = (Long) entityManager.createQuery("SELECT COUNT(*) FROM OutboundSMS sms WHERE sms.bookId=:bookId AND sms.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("isRead", false).getResultList().get(0);
            unreadCallRecord = (Long) entityManager.createQuery("SELECT COUNT(*) FROM CallRecord cr WHERE cr.bookId=:bookId AND cr.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("isRead", false).getResultList().get(0);
            unreadChat = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Chat chat WHERE chat.bookId=:bookId AND chat.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("isRead", false).getResultList().get(0);
            unreadActivityLog = (Long) entityManager.createQuery("SELECT COUNT(*) FROM ActivityLog al WHERE al.bookId=:bookId AND al.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("isRead", false).getResultList().get(0);
            unreadLetter = (Long) entityManager.createQuery("SELECT COUNT(*) FROM Letter l WHERE l.bookId=:bookId AND l.isRead=:isRead").setParameter("bookId", communicationUnreadCriteria.getBookId()).setParameter("isRead", false).getResultList().get(0);

        }

        Map<String, Long> map = new HashMap<>();
        map.put("email", unreadEmail);
        map.put("sms", unreadSMS);
        map.put("callrecord", unreadCallRecord);
        map.put("chat", unreadChat);
        map.put("activitylog", unreadActivityLog);
        map.put("letter", unreadLetter);
        return map;
    }

    @Override
    public Map<String, Object> getCommunicationByCriteria(CommunicationCriteria criteria) throws OperationException {
        List<BaseCommunication> communications = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseCommunication> criteriaQuery = criteriaBuilder.createQuery(BaseCommunication.class);
        Root<BaseCommunication> root = criteriaQuery.from(BaseCommunication.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getCommunicationType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("communicationType"), criteria.getCommunicationType()));
        }
        if (criteria.getSubject() != null) {
            predicates.add(criteriaBuilder.equal(root.get("subject"), criteria.getSubject()));
        }
        if (criteria.getSender() != null) {
            predicates.add(criteriaBuilder.equal(root.get("sender"), criteria.getSender()));
        }
        if (criteria.getModule() != null) {
            predicates.add(criteriaBuilder.equal(root.get("module"), criteria.getModule()));
        }
        if (criteria.getProcess() != null) {
            predicates.add(criteriaBuilder.equal(root.get("process"), criteria.getProcess()));
        }
        if (criteria.getScenario() != null) {
            predicates.add(criteriaBuilder.equal(root.get("scenario"), criteria.getScenario()));
        }
        if (criteria.getFunction() != null) {
            predicates.add(criteriaBuilder.equal(root.get("function"), criteria.getFunction()));
        }
        if (criteria.getRead() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isRead"), criteria.getRead()));
        }
        if (criteria.getSupplier() != null) {
            predicates.add(criteriaBuilder.equal(root.get("supplier"), criteria.getSupplier()));
        }
        if (criteria.getProductSubCategory() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productSubCategory"), criteria.getProductSubCategory()));
        }
        if (criteria.getBookId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookId"), criteria.getBookId()));
        }
        if (criteria.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("userId"), criteria.getUserId()));
        }
        if (criteria.getIs_outbound() != null) {
            predicates.add(criteriaBuilder.equal(root.get("is_outbound"), criteria.getIs_outbound()));
        }
        if (criteria.getDateTime() != null) {
            predicates.add(criteriaBuilder.equal(root.get("dateTime"), criteria.getDateTime()));
        }
        if (criteria.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
        }
//        if (criteria.getRecipientList() != null) {
//            Join<BaseCommunication,Recipient> recipientJoin = root.join("recipientList");
//            predicates.add(recipientJoin.get("recipientList").in(criteria.getRecipientList()));
//            //predicates.add(criteriaBuilder.equal(root.get("recipientList"), criteria.getRecipientList()));
//        }
//        if (criteria.getCcRecipientList() != null) {
//            Join<BaseCommunication,CCRecipient> ccrecipientJoin = root.join("ccrecipientList");
//            predicates.add(ccrecipientJoin.get("ccrecipientList").in(criteria.getCcRecipientList()));
//            //predicates.add(criteriaBuilder.equal(root.get("ccRecipientList"), criteria.getCcRecipientList()));
//        }
//        if (criteria.getBccRecipientList() != null) {
//            Join<BaseCommunication,BCCRecipient> bccrecipientJoin = root.join("bccrecipientList");
//            predicates.add(bccrecipientJoin.get("bccrecipientList").in(criteria.getBccRecipientList()));
//            //predicates.add(criteriaBuilder.equal(root.get("bccRecipientList"), criteria.getBccRecipientList()));
//        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        List<BaseCommunication> baseCommunications = null;
        Integer size = criteria.getSize();
        Integer page = criteria.getPage();
        Integer actualSize = 0;


        TypedQuery<BaseCommunication> query = entityManager.createQuery(criteriaQuery);
        if (query.getResultList() != null && query.getResultList().size() > 0) {
            actualSize = query.getResultList().size();
            if (size != null && page != null) {
                Integer startIndex = (page - 1) * size;
                query.setFirstResult(startIndex);
                query.setMaxResults(size);
            }

            baseCommunications = query.getResultList();


            return applyPagination(baseCommunications, size, page, actualSize);
        }
        return Collections.emptyMap();
    }


}
