package com.coxandkings.travel.operations.repository.refund.impl;

import com.coxandkings.travel.operations.model.refund.RefundConfiguration;
import com.coxandkings.travel.operations.repository.refund.RefundConfigurationRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RefundConfigurationRepositroyImpl extends SimpleJpaRepository<RefundConfiguration, String> implements RefundConfigurationRepository {
    private static Logger logger = LogManager.getLogger(RefundConfigurationRepositroyImpl.class);
    private EntityManager entityManager;

    public RefundConfigurationRepositroyImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(RefundConfiguration.class, entityManager);
        this.entityManager = entityManager;

    }


    @Override
    public RefundConfiguration saveOrUpdate(RefundConfiguration refundConfiguration) {
        try {
            return super.saveAndFlush(refundConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RefundConfiguration> findByCriteria(RefundConfiguration refundConfiguration) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RefundConfiguration> refundConfigurationCriteriaQuery = criteriaBuilder.createQuery(RefundConfiguration.class);

        Root<RefundConfiguration> root = refundConfigurationCriteriaQuery.from(RefundConfiguration.class);
        refundConfigurationCriteriaQuery.select(root);

        try {
            if (null != refundConfiguration) {
                refundConfigurationCriteriaQuery = criteriaBuilder.createQuery(RefundConfiguration.class);
                root = refundConfigurationCriteriaQuery.from(RefundConfiguration.class);
                List<Predicate> predicates = new ArrayList<>();

                if (null != refundConfiguration.getCompanyMarket()) {
                    Predicate companyMarketPredicate = criteriaBuilder.equal(root.get("companyMarket"), refundConfiguration.getCompanyMarket());
                    predicates.add(companyMarketPredicate);
                }
                if (null != refundConfiguration.getClientType()) {
                    Predicate clientTypePredicate = criteriaBuilder.equal(root.get("clientType"), refundConfiguration.getClientType());
                    predicates.add(clientTypePredicate);
                }
                if (null != refundConfiguration.getClientCategory()) {
                    Predicate clientCategoryPredicate = criteriaBuilder.equal(root.get("clientCategory"), refundConfiguration.getClientCategory());
                    predicates.add(clientCategoryPredicate);
                }

                if (null != refundConfiguration.getClientSubCategory()) {
                    Predicate clientSubCategoryPredicate = criteriaBuilder.equal(root.get("clientSubCategory"), refundConfiguration.getClientSubCategory());
                    predicates.add(clientSubCategoryPredicate);
                }

                if (null != refundConfiguration.getClientName()) {
                    Predicate clientNamePredicate = criteriaBuilder.equal(root.get("clientName"), refundConfiguration.getClientName());
                    predicates.add(clientNamePredicate);
                }

                if (null != refundConfiguration.getClientName()) {
                    Predicate clientGroupPredicate = criteriaBuilder.equal(root.get("clientGroup"), refundConfiguration.getClientGroup());
                    predicates.add(clientGroupPredicate);
                }

                if (!predicates.isEmpty()) {
                    refundConfigurationCriteriaQuery.where(predicates.toArray(new Predicate[0]));
                }
                // refundConfigurationCriteriaQuery.select(root).where(criteriaBuilder.and(companyMarketPredicate), criteriaBuilder.and(clientTypePredicate), criteriaBuilder.and(clientCategoryPredicate), criteriaBuilder.and(clientSubCategoryPredicate), criteriaBuilder.and(clientNamePredicate), criteriaBuilder.and(clientGroupPredicate));

            }
            return this.entityManager.createQuery(refundConfigurationCriteriaQuery).getResultList();

        } catch (NonUniqueResultException e) {

            logger.info(" public List<RefundConfiguration> findByCriteria(" + refundConfiguration + ") , exception raised : " + e);

        } catch (Exception e) {

            logger.info(" public List<RefundConfiguration> findByCriteria(" + refundConfiguration + ") , exception raised : " + e);

        }

        return null;
    }
}
