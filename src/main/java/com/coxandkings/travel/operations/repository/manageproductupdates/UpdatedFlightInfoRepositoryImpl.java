package com.coxandkings.travel.operations.repository.manageproductupdates;

import com.coxandkings.travel.operations.enums.manageproductupdates.UpdatedFlightStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.UpdatedFlightInfo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UpdatedFlightInfoRepositoryImpl extends SimpleJpaRepository<UpdatedFlightInfo, String> implements UpdatedFlightInfoRepository {
    private EntityManager entityManager;

    public UpdatedFlightInfoRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(UpdatedFlightInfo.class, em);
        entityManager = em;
    }

    @Transactional
    @Override
    public UpdatedFlightInfo saveUpdatedFlightInfo(UpdatedFlightInfo updatedFlightInfo) {
        return this.saveAndFlush(updatedFlightInfo);
    }

    @Override
    public UpdatedFlightInfo getUpdatedFlightInfoByCriteria(String bookId, String orderId) {
        UpdatedFlightInfo updatedFlightInfo = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UpdatedFlightInfo> criteriaQuery = criteriaBuilder.createQuery(UpdatedFlightInfo.class);
        Root<UpdatedFlightInfo> root = criteriaQuery.from(UpdatedFlightInfo.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (bookId != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingID"), bookId));
        }

        if (orderId != null) {
            predicates.add(criteriaBuilder.equal(root.get("orderID"), orderId));
        }

        predicates.add(criteriaBuilder.equal(root.get("updatedFlightStatus"), UpdatedFlightStatus.NEW.getStatus()));

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        try
        {
            updatedFlightInfo  = entityManager.createQuery(criteriaQuery).getSingleResult();
        }
        catch (Exception e)
        {
            return updatedFlightInfo;
        }
        return updatedFlightInfo;
    }

    @Override
    public UpdatedFlightInfo getById(String id) {
        UpdatedFlightInfo updatedFlightInfo = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UpdatedFlightInfo> criteriaQuery = criteriaBuilder.createQuery(UpdatedFlightInfo.class);
        Root<UpdatedFlightInfo> root = criteriaQuery.from(UpdatedFlightInfo.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(id)) {
            predicates.add(criteriaBuilder.equal(root.get("id"), id));
        }
        predicates.add(criteriaBuilder.equal(root.get("updatedFlightStatus"), UpdatedFlightStatus.NEW.getStatus()));
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        try
        {
            updatedFlightInfo  = entityManager.createQuery(criteriaQuery).getSingleResult();
        }
        catch (Exception e)
        {
            return updatedFlightInfo;
        }
        return updatedFlightInfo;
    }

    @Override
    public Map<String, Object> getUpdatedFlightInfo(Integer size, Integer page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UpdatedFlightInfo> updatedFlightInfoCriteriaQuery = criteriaBuilder.createQuery(UpdatedFlightInfo.class);
        Root<UpdatedFlightInfo> root = updatedFlightInfoCriteriaQuery.from(UpdatedFlightInfo.class);
        updatedFlightInfoCriteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("updatedFlightStatus"), UpdatedFlightStatus.NEW.getStatus()));

        if (!predicates.isEmpty()) {
            updatedFlightInfoCriteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<UpdatedFlightInfo> query = entityManager.createQuery(updatedFlightInfoCriteriaQuery);
        Integer actualSize = query.getResultList().size();
        if (size != null && page != null) {
            Integer startIndex = (page - 1) * size;
            query.setFirstResult(startIndex);
            query.setMaxResults(size);
        }

        List<UpdatedFlightInfo> resultList = query.getResultList();
        return applyPagination(resultList, size, page, actualSize);
    }

    private Map<String, Object> applyPagination(List<UpdatedFlightInfo> updatedFlightInfos, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entiy = new HashMap<>();
        entiy.put("result", updatedFlightInfos);

        if (updatedFlightInfos.isEmpty()) return entiy;

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
}
