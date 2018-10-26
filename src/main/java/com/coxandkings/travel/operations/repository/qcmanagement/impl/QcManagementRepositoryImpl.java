package com.coxandkings.travel.operations.repository.qcmanagement.impl;

import com.coxandkings.travel.operations.model.qcmanagement.QcStatusInfo;
import com.coxandkings.travel.operations.repository.qcmanagement.QcManagementRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class QcManagementRepositoryImpl extends SimpleJpaRepository<QcStatusInfo, String> implements QcManagementRepository{

    private EntityManager entityManager;

    public QcManagementRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(QcStatusInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    public QcStatusInfo saveOrUpdate(QcStatusInfo qcStatusInfo) {
        return this.saveAndFlush(qcStatusInfo);
    }

    @Override
    public List<QcStatusInfo> getAllQcStatusInfo() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<QcStatusInfo> qcStatusInfoCriteriaQuery=criteriaBuilder.createQuery(QcStatusInfo.class);
        Root<QcStatusInfo> root = qcStatusInfoCriteriaQuery.from(QcStatusInfo.class);
        return entityManager.createQuery(qcStatusInfoCriteriaQuery).getResultList();
    }
}
