package com.coxandkings.travel.operations.repository.merge.impl;

import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.model.merge.MergeType;
import com.coxandkings.travel.operations.repository.merge.MergeTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class MergeTypeRepositoryImpl extends SimpleJpaRepository<MergeType, String> implements MergeTypeRepository {
    EntityManager entityManager;

    public MergeTypeRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(MergeType.class, em);
        entityManager = em;
    }

    @Override
    public MergeType getById(String id) {
        return findOne(id);
    }

    @Override
    public MergeType getByCode(MergeTypeValues code) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MergeType> mergeTypeCriteriaQuery = criteriaBuilder.createQuery(MergeType.class);
        Root<MergeType> mergeTypeRoot = mergeTypeCriteriaQuery.from(MergeType.class);

        Predicate predicate = criteriaBuilder.equal(mergeTypeRoot.get("code"), code);

        mergeTypeCriteriaQuery.where(predicate);
        return entityManager.createQuery(mergeTypeCriteriaQuery).getSingleResult();
    }
}
