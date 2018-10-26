package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.MailRoomSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.MailroomSearchCriteriaSorted;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.coxandkings.travel.operations.model.mailroomanddispatch.MailRoomMaster;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.MailRoomRepository;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;


        /*import com.coxandkings.travel.operations.model.mailroomanddispatch.RoomStatus;*/

@Transactional(readOnly=false)
@Repository
public class MailRoomRepositoryImpl extends SimpleJpaRepository<MailRoomMaster, String> implements MailRoomRepository {


    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(MailRoomRepositoryImpl.class);

    public MailRoomRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(MailRoomMaster.class, em);
        entityManager = em;
    }

    public MailRoomMaster saveOrUpdate(MailRoomMaster mailRoom) {

        return saveAndFlush(mailRoom);

    }

    public Map<String, Object> getByCriteria(MailRoomSearchCriteria maiRoomCriteria) {
        Integer defaultPageSize = 10;
        Integer defaultPageNumber = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MailRoomMaster> mailRoomCriteriaQuery = criteriaBuilder.createQuery(MailRoomMaster.class);
        Root<MailRoomMaster> root = mailRoomCriteriaQuery.from(MailRoomMaster.class);
        List<Predicate> predicates = new ArrayList<>();
        if (maiRoomCriteria.getId() != null && !(maiRoomCriteria.getId().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("id"), maiRoomCriteria.getId()));
        }
        if (maiRoomCriteria.getMailRoomName() != null && !(maiRoomCriteria.getMailRoomName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("mailRoomName"), maiRoomCriteria.getMailRoomName()));
        }
        if (maiRoomCriteria.getCountry() != null && !(maiRoomCriteria.getCountry().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("country"), maiRoomCriteria.getCountry()));
        }
        if (maiRoomCriteria.getState() != null && !(maiRoomCriteria.getState().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("state"), maiRoomCriteria.getState()));
        }
        if (maiRoomCriteria.getCity() != null && !(maiRoomCriteria.getCity().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("city"), maiRoomCriteria.getCity()));
        }
        if (maiRoomCriteria.getRoomStatus() != null && !(maiRoomCriteria.getRoomStatus().name().isEmpty())) {
            /*Join<MailRoomMaster, RoomStatus> status = root.join("roomStatus");*/
            predicates.add(criteriaBuilder.equal(root.get("roomStatus"), maiRoomCriteria.getRoomStatus()));
        }
        if (!predicates.isEmpty()) {
            mailRoomCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        int count;
        TypedQuery<MailRoomMaster> typedQuery = entityManager.createQuery(mailRoomCriteriaQuery);
        List<MailRoomMaster> mailRoomMasterList = typedQuery.getResultList();
                if(mailRoomMasterList != null && mailRoomMasterList.size() > 0){
                       count = typedQuery.getResultList().size();    }else {count= 0;}
        if (maiRoomCriteria.getPageSize() == null || maiRoomCriteria.getPageSize() == 0)
            maiRoomCriteria.setPageSize(defaultPageSize);
        /*if (searchCriteria.getPageNumber() == null)
            searchCriteria.setPageNumber(defaultPageNumber);*/
        if (maiRoomCriteria.getPageSize() != null && maiRoomCriteria.getPageNumber() != null) {
            typedQuery.setFirstResult((maiRoomCriteria.getPageNumber() - 1) * maiRoomCriteria.getPageSize());
            typedQuery.setMaxResults(maiRoomCriteria.getPageSize());
        }

        List<MailRoomMaster> mailRoomMasterList1 = typedQuery.getResultList();
        Map<String, Object> result = new HashMap<>();
        result.put("result", mailRoomMasterList1);
        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(maiRoomCriteria.getPageSize(), count));
        return applyPagination(mailRoomMasterList1,maiRoomCriteria.getPageSize(),maiRoomCriteria.getPageNumber(),count);
    }



    @Override
    public Map<String, Object> getByCriteriaSorted(MailroomSearchCriteriaSorted mailroomSearchCriteriaSorted) {
        Integer defaultPageSize = 10;
        Integer defaultPageNumber = 1;

        Integer size = mailroomSearchCriteriaSorted.getPageSize() !=null ? mailroomSearchCriteriaSorted.getPageSize() : 5;
        Integer page = mailroomSearchCriteriaSorted.getPageNumber()!=null ? mailroomSearchCriteriaSorted.getPageNumber() : 1;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MailRoomMaster> mailRoomCriteriaQuery = criteriaBuilder.createQuery(MailRoomMaster.class);
        Root<MailRoomMaster> root = mailRoomCriteriaQuery.from(MailRoomMaster.class);
        mailRoomCriteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if(mailroomSearchCriteriaSorted.getSortCriteria().contains("-")) {
            mailroomSearchCriteriaSorted.setSortCriteria(mailroomSearchCriteriaSorted.getSortCriteria().substring(1));
            mailroomSearchCriteriaSorted.setDescending(true);
        }
        else
            mailroomSearchCriteriaSorted.setDescending(false);


        if (!StringUtils.isEmpty(mailroomSearchCriteriaSorted.getSortCriteria())) {
            mailRoomCriteriaQuery.orderBy(mailroomSearchCriteriaSorted.getDescending() ? criteriaBuilder.desc(root.get(mailroomSearchCriteriaSorted.getSortCriteria())) :
                    criteriaBuilder.asc(root.get(mailroomSearchCriteriaSorted.getSortCriteria())));
        }

        if (mailroomSearchCriteriaSorted.getId() != null && !(mailroomSearchCriteriaSorted.getId().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("id"), mailroomSearchCriteriaSorted.getId()));
        }
        if (mailroomSearchCriteriaSorted.getMailRoomName() != null && !(mailroomSearchCriteriaSorted.getMailRoomName().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("mailRoomName"), mailroomSearchCriteriaSorted.getMailRoomName()));
        }
        if (mailroomSearchCriteriaSorted.getCountry() != null && !(mailroomSearchCriteriaSorted.getCountry().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("country"), mailroomSearchCriteriaSorted.getCountry()));
        }
        if (mailroomSearchCriteriaSorted.getState() != null && !(mailroomSearchCriteriaSorted.getState().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("state"), mailroomSearchCriteriaSorted.getState()));
        }
        if (mailroomSearchCriteriaSorted.getCity() != null && !(mailroomSearchCriteriaSorted.getCity().isEmpty())) {
            predicates.add(criteriaBuilder.equal(root.get("city"), mailroomSearchCriteriaSorted.getCity()));
        }
        if (mailroomSearchCriteriaSorted.getRoomStatus() != null && !mailroomSearchCriteriaSorted.getRoomStatus().getValue().isEmpty()) {
            /*Join<MailRoomMaster, RoomStatus> status = root.join("roomStatus");*/
            if(mailroomSearchCriteriaSorted.getRoomStatus().getValue().equalsIgnoreCase("active"))
                predicates.add(criteriaBuilder.equal(root.get("roomStatus"), MailRoomStatus.ACTIVE));
            else
                predicates.add(criteriaBuilder.equal(root.get("roomStatus"), MailRoomStatus.INACTIVE));
        }
        if (!predicates.isEmpty()) {
            mailRoomCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        if(mailroomSearchCriteriaSorted.getOrder()!=null && !mailroomSearchCriteriaSorted.getOrder().isEmpty()) {
            Order orderBy = null;
            if ("ASC".equalsIgnoreCase(mailroomSearchCriteriaSorted.getOrder())) {
                orderBy = criteriaBuilder.asc(root.get(mailroomSearchCriteriaSorted.getParam()));
            } else if ("DESC".equalsIgnoreCase(mailroomSearchCriteriaSorted.getOrder())) {
                orderBy = criteriaBuilder.desc(root.get(mailroomSearchCriteriaSorted.getParam()));
            }
            mailRoomCriteriaQuery.orderBy(orderBy);
        }
        int count;
        TypedQuery<MailRoomMaster> typedQuery = entityManager.createQuery(mailRoomCriteriaQuery);
        List<MailRoomMaster> mailRoomMasterList = typedQuery.getResultList();
        if(mailRoomMasterList != null && mailRoomMasterList.size() > 0){
            count = typedQuery.getResultList().size();    }else {count= 0;}
        if (mailroomSearchCriteriaSorted.getPageSize() == null || mailroomSearchCriteriaSorted.getPageSize() == 0)
            mailroomSearchCriteriaSorted.setPageSize(defaultPageSize);
        if (mailroomSearchCriteriaSorted.getPageSize() != null && mailroomSearchCriteriaSorted.getPageNumber() != null) {
            typedQuery.setFirstResult((mailroomSearchCriteriaSorted.getPageNumber() - 1) * mailroomSearchCriteriaSorted.getPageSize());
            typedQuery.setMaxResults(mailroomSearchCriteriaSorted.getPageSize());
        }


        /*Integer actualSize = typedQuery.getResultList().size();
        Integer startIndex = 0;

        System.out.println("test working till here");

        if (size != null && page != null) {
            startIndex = (page - 1) * size;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(size);
        }*/

        List<MailRoomMaster> serviceOrderAndSupplierLiabilities = typedQuery.getResultList();
        Map<String, Object> result = new HashMap<>();
        result.put("result", serviceOrderAndSupplierLiabilities);
        result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(mailroomSearchCriteriaSorted.getPageSize(), count));
        result.put("order",mailroomSearchCriteriaSorted.getOrder());
        return applyPagination(serviceOrderAndSupplierLiabilities,size,page,count);
    }

    private Map<String, Object> applyPagination(List<MailRoomMaster> mailRoomMasters, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entity = new HashMap<>();
        entity.put("data", mailRoomMasters);
        entity.put("totalCount", actualSize);

        if (mailRoomMasters.isEmpty())
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

    public MailRoomMaster getId(String mailRoomId) {
        return this.findOne(mailRoomId);

    }

    public List<MailRoomMaster> getAllMailRoomDetails() {
        return this.findAll();
    }

    public List<MailRoomMaster> getAllMailRoomsSorted(String columnName,String order){
        List<MailRoomMaster> mailRoomMasterList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MailRoomMaster> mailRoomCriteriaQuery = criteriaBuilder.createQuery(MailRoomMaster.class);
        Root<MailRoomMaster> root = mailRoomCriteriaQuery.from(MailRoomMaster.class);
        mailRoomCriteriaQuery.select(root);

        Order orderBy = null;
        if ("ASC".equalsIgnoreCase(order)) {
            orderBy = criteriaBuilder.asc(root.get(columnName));
        } else if("DESC".equalsIgnoreCase(order)){
            orderBy = criteriaBuilder.desc(root.get(columnName));
        }
        mailRoomCriteriaQuery.orderBy(orderBy);

        mailRoomMasterList = entityManager.createQuery(mailRoomCriteriaQuery).getResultList();
        return mailRoomMasterList;
    }

    public List<String> getMailRoomNames(String param) {
        List<String> mailRoomMasterDetailsList= new ArrayList<>();
        List<String> mailRoomMasterList = new ArrayList<>();
        List<MailRoomMaster> mailRoomMasterDetails = this.findAll();
        String regex = new StringBuilder().append(".*").append("(?i)").append(param).append(".*").toString().trim();
        java.util.function.Predicate<String> predicateRegex = Pattern.compile(regex).asPredicate();

        mailRoomMasterDetailsList = mailRoomMasterDetails.stream().map(x -> x.getMailRoomName()).collect(Collectors.toList());
        mailRoomMasterList = mailRoomMasterDetailsList.parallelStream().filter(predicateRegex).collect(Collectors.toList());

        return mailRoomMasterList;
    }

    public List<String> getMailRooms(){
        List<String> mailRoomMasterDetailsList= new ArrayList<>();
        List<String> mailRoomMasterList = new ArrayList<>();
        List<MailRoomMaster> mailRoomMasterDetails = this.findAll();
        mailRoomMasterDetailsList = mailRoomMasterDetails.stream().map(x -> x.getMailRoomName()).collect(Collectors.toList());
        return mailRoomMasterDetailsList;
    }

    @Override
    public MailRoomMaster fetchMailroomMasterForConfigId(String configurationId) {
        MailRoomMaster mailRoomMaster = findOne(configurationId);
        return mailRoomMaster;
    }

    @Override
    public MailRoomMaster update(MailRoomMaster mailRoomMaster) {
        return this.saveAndFlush(mailRoomMaster);
    }


    @Override
    public int deleteMasterRecord(String configurationId) {
        String sqlQuery ="";

        sqlQuery = String.format("DELETE FROM operations_schema.mailroommasterlock\r\n" +
                "      WHERE id='%s'",configurationId);

        System.out.println(sqlQuery);
        System.out.println();

        int deleted = entityManager.createNativeQuery(sqlQuery).executeUpdate();

        return deleted;
    }

}
