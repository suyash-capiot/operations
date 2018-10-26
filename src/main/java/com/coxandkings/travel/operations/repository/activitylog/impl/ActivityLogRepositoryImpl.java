package com.coxandkings.travel.operations.repository.activitylog.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.activitylog.ActivityLog;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import com.coxandkings.travel.operations.repository.activitylog.ActivityLogRepository;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("activitylog")
@org.springframework.transaction.annotation.Transactional(readOnly = false)
public class ActivityLogRepositoryImpl extends SimpleJpaRepository<ActivityLog, String > implements ActivityLogRepository {

    private EntityManager entityManager;
    public ActivityLogRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(ActivityLog.class, em);
        entityManager=em;
    }

    @Override
    public ActivityLog saveActivityLog(ActivityLog activityLog) {
        return this.saveAndFlush(activityLog);
    }

    @Override
    public ActivityLog getActivityLogById(String id) {
        ActivityLog activityLog =this.findOne(id);
        return activityLog;
    }

    @Override
    public List<? extends BaseCommunication> getByCriteria(CommunicationCriteria criteria) throws OperationException {

        List<ActivityLog> communications = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityLog> criteriaQuery = criteriaBuilder.createQuery(ActivityLog.class);
        Root<ActivityLog> root = criteriaQuery.from(ActivityLog.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getCommunicationType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("communicationType"), criteria.getCommunicationType()));
        }


        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        List<ActivityLog> activityLogs = null;
        Integer size = criteria.getSize();
        Integer page = criteria.getPage();
        Integer actualSize = 0;
        try {

            TypedQuery<ActivityLog> query = entityManager.createQuery(criteriaQuery);
            actualSize = query.getResultList().size();
            if (size != null && page != null) {
                Integer startIndex = (page - 1) * size;
                query.setFirstResult(startIndex);
                query.setMaxResults(size);
            }

            activityLogs = query.getResultList();

        } catch (NoResultException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return activityLogs;
    }

}
