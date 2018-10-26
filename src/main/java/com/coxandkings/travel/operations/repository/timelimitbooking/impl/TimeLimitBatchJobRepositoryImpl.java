package com.coxandkings.travel.operations.repository.timelimitbooking.impl;

import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitBatchJobInfo;
import com.coxandkings.travel.operations.repository.timelimitbooking.TimeLimitBatchJobRepository;
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
public class TimeLimitBatchJobRepositoryImpl extends SimpleJpaRepository<TimeLimitBatchJobInfo, String> implements TimeLimitBatchJobRepository {

    private static final Logger logger = LogManager.getLogger(TimeLimitBatchJobRepositoryImpl.class);
    private EntityManager entityManager;


    public TimeLimitBatchJobRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(TimeLimitBatchJobInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public TimeLimitBatchJobInfo saveTimeLimitBacthJobInfo(TimeLimitBatchJobInfo timeLimitBatchJobInfo) {
        return this.saveAndFlush(timeLimitBatchJobInfo);
    }


    @Override
    public List<TimeLimitBatchJobInfo> getTLBatchJobInfoByCriteria() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TimeLimitBatchJobInfo> criteriaQuery = criteriaBuilder.createQuery(TimeLimitBatchJobInfo.class);
        Root<TimeLimitBatchJobInfo> root = criteriaQuery.from(TimeLimitBatchJobInfo.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();



        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("UTC"));
//        ZonedDateTime aDateTime = ZonedDateTime.now( ZoneId.of("UTC") );
        Timestamp tsNow = Timestamp.from(currentTime.toInstant());
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Timestamp>get("calculatedExpiryDueDate"), tsNow));

        ZonedDateTime hourLater = ZonedDateTime.now(ZoneId.of("UTC")).plusHours(1L);
        Timestamp tsHourLater = Timestamp.from(hourLater.toInstant());
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Timestamp>get("calculatedExpiryDueDate"), tsHourLater));

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(criteriaQuery).getResultList();

    }


}
