package com.coxandkings.travel.operations.repository.booking.impl;

import com.coxandkings.travel.operations.criteria.booking.UserFavCriteria;
import com.coxandkings.travel.operations.model.booking.UserFavourite;
import com.coxandkings.travel.operations.repository.booking.UserFavRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("userFavouriteRepository")
public class UserFavRepositoryImpl extends SimpleJpaRepository<UserFavourite,String> implements UserFavRepository {
    private EntityManager entityManager;

    public UserFavRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(UserFavourite.class, entityManager);
        this.entityManager = entityManager;
    }


    @Override
    public UserFavourite saveUserFavourite(UserFavourite userFavourite) {
        return this.saveAndFlush(userFavourite);
    }

    @Override
    public List<UserFavourite> getUserFavouritesByCriteria(UserFavCriteria criteria) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<UserFavourite> criteriaQuery=criteriaBuilder.createQuery(UserFavourite.class);
        Root<UserFavourite> root=criteriaQuery.from(UserFavourite.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        String[] ids = criteria.getIds();
        if(ids !=null && ids.length > 0) {
            Predicate id = root.get("id").in(ids);
            predicates.add(id);
        }

        String[] excludeIds = criteria.getExcludeIds();
        if(excludeIds !=null && excludeIds.length > 0) {
            Predicate id = criteriaBuilder.not(root.get("id").in(ids));
            predicates.add(id);
        }

        String favName = criteria.getFavName();
        if (!StringUtils.isEmpty(favName)) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("favName")), favName.toUpperCase()));
        }

        String userId = criteria.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            predicates.add(criteriaBuilder.equal(root.get("userId"),userId));
        }

        if(!CollectionUtils.isEmpty(predicates)) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public UserFavourite getUserFavouriteById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<UserFavourite> getUserFavouriteByFavName(UserFavCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserFavourite> criteriaQuery = criteriaBuilder.createQuery(UserFavourite.class);
        Root<UserFavourite> root = criteriaQuery.from(UserFavourite.class);
        criteriaQuery.select(root);
        Predicate predicate = criteriaBuilder.like(root.get("favName"), criteria.getFavName());
        criteriaQuery.where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public String deleteById(String id) {
        this.delete(id);
        flush();
        return "Deleted Successfully";
    }
}
