package com.coxandkings.travel.operations.repository.alert.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alert.Notification;
import com.coxandkings.travel.operations.repository.alert.NotificationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class NotificationRepositoryImpl extends SimpleJpaRepository<Notification, String> implements NotificationRepository {
    private EntityManager entityManager;

    public NotificationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(Notification.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Notification saveOrUpdate(Notification notification) {
        return saveAndFlush(notification);
    }

    @Override
    public List<Notification> getNotifications(String userId, Integer page, Integer count) throws OperationException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> criteriaQuery = criteriaBuilder.createQuery(Notification.class);
        Root<Notification> root = criteriaQuery.from(Notification.class);
        criteriaQuery.select(root);
        Predicate predicate = null;

        if (!StringUtils.isEmpty(userId)) {
            predicate = criteriaBuilder.equal(root.get("userId"), userId);
        }
        criteriaQuery
                .orderBy(criteriaBuilder.desc(root.get("createdDate")));
        if (null == predicate) {
            throw new OperationException("UserId is invalid");
        } else {
            criteriaQuery.where(predicate);
        }
        final TypedQuery<Notification> query = entityManager.createQuery(criteriaQuery);

        setPagination(page, count, query);

        return query.getResultList();
    }

    private void setPagination(Integer page, Integer count, TypedQuery<Notification> query) {
        if (page == null || page == 0 ) {
            page = 1;
        }
        if (count == null) {
            count = 10;
        }

        Integer recordNo = ((page-1) * count) + 1;
        query.setFirstResult(recordNo);
        query.setMaxResults(count);
    }
}
