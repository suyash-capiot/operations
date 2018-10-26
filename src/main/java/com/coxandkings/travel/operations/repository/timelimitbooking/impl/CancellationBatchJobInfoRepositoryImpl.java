package com.coxandkings.travel.operations.repository.timelimitbooking.impl;

import com.coxandkings.travel.operations.model.timelimitbooking.TLCancellationBatchJobInfo;
import com.coxandkings.travel.operations.repository.timelimitbooking.CancellationBatchJobInfoRepository;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CancellationBatchJobInfoRepositoryImpl extends SimpleJpaRepository<TLCancellationBatchJobInfo, String> implements CancellationBatchJobInfoRepository {
    private static final Logger logger = LogManager.getLogger(CancellationBatchJobInfoRepositoryImpl.class);
    private EntityManager entityManager;


    public CancellationBatchJobInfoRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(TLCancellationBatchJobInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public TLCancellationBatchJobInfo saveCancellationBacthJobInfo(TLCancellationBatchJobInfo tlCancellationBatchJobInfo) {
        return this.save(tlCancellationBatchJobInfo);
    }

    @Override
    public List<TLCancellationBatchJobInfo> getTLCancellationBatchJobInfoByCriteria() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TLCancellationBatchJobInfo> criteriaQuery = criteriaBuilder.createQuery(TLCancellationBatchJobInfo.class);
        Root<TLCancellationBatchJobInfo> root = criteriaQuery.from(TLCancellationBatchJobInfo.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Timestamp tsNow = Timestamp.from(currentTime.toInstant());
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Timestamp>get("calculatedBeforeDueDate"), tsNow));

        ZonedDateTime hourLater = ZonedDateTime.now(ZoneId.of("UTC")).plusHours(1L);
        Timestamp tsHourLater = Timestamp.from(hourLater.toInstant());
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Timestamp>get("calculatedBeforeDueDate"), tsHourLater));

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
