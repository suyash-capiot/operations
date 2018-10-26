package com.coxandkings.travel.operations.repository.failure.Impl;

import com.coxandkings.travel.operations.model.failure.ThresholdConfiguration;
import com.coxandkings.travel.operations.model.failure.ThresholdConfigurationProductDetails;
import com.coxandkings.travel.operations.repository.failure.FailureThresholdConfigurationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FailureThresholdConfigurationRepositoryImpl extends SimpleJpaRepository<ThresholdConfiguration, String> implements FailureThresholdConfigurationRepository {

    private EntityManager entityManager;

    public FailureThresholdConfigurationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ThresholdConfiguration.class, em);
        entityManager = em;
    }

    @Override
    public List<ThresholdConfiguration> saveThresholdConfiguration(List<ThresholdConfiguration> thresholdConfigurations) {
        final List<ThresholdConfiguration> savedEntities = new ArrayList<ThresholdConfiguration>(thresholdConfigurations.size());

        for (ThresholdConfiguration thresholdConfiguration : thresholdConfigurations) {
            savedEntities.add(persistOrMerge(thresholdConfiguration));
        }
        entityManager.flush();
        entityManager.clear();
        return savedEntities;
    }


    private ThresholdConfiguration persistOrMerge(ThresholdConfiguration thresholdConfiguration) {
        if (thresholdConfiguration.getConfigurationId() == null) {
            entityManager.persist(thresholdConfiguration);
            return thresholdConfiguration;
        } else {
            return entityManager.merge(thresholdConfiguration);
        }
    }

    @Override
    public List<ThresholdConfiguration> searchThresholdConfiguration(ThresholdConfiguration thresholdConfiguration) {
        String companyMarket = thresholdConfiguration.getCompanyMarket();
        String clientType = thresholdConfiguration.getClientType();
        String productCategory = thresholdConfiguration.getThresholdConfigurationProductDetails().get(0).getProductCategory();
        String productSubCategory = thresholdConfiguration.getThresholdConfigurationProductDetails().get(0).getProductCategorySubtype();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ThresholdConfiguration> query = builder.createQuery(ThresholdConfiguration.class);
        Root<ThresholdConfiguration> root = query.from(ThresholdConfiguration.class);

        Join<ThresholdConfiguration, ThresholdConfigurationProductDetails> join = root.join("thresholdConfigurationProductDetails");


        List<Predicate> p1 = new ArrayList<Predicate>();


        if (companyMarket != null && !companyMarket.isEmpty())
            p1.add(builder.and(builder.equal(root.get("companyMarket"), companyMarket)));

        if (clientType != null && !clientType.isEmpty())
            p1.add(builder.and(builder.equal(root.get("clientType"), clientType)));

        if (productCategory != null && !productCategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productCategory"), productCategory)));

        if (productSubCategory != null && !productSubCategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productCategorySubtype"), productSubCategory)));


        query.where(p1.toArray(new Predicate[p1.size()]));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<ThresholdConfiguration> fetchThresholdConfiguration() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ThresholdConfiguration> query = builder.createQuery(ThresholdConfiguration.class);
        Root<ThresholdConfiguration> root = query.from(ThresholdConfiguration.class);
        query.multiselect(root.get("configurationId"), root.get("companyMarket"), root.get("clientType")).distinct(true);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<ThresholdConfigurationProductDetails> fetchThresholdConfigurationProductDetails() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ThresholdConfigurationProductDetails> query = builder.createQuery(ThresholdConfigurationProductDetails.class);
        Root<ThresholdConfigurationProductDetails> root = query.from(ThresholdConfigurationProductDetails.class);
        Join<ThresholdConfigurationProductDetails, ThresholdConfiguration> join = root.join("thresholdConfiguration");
        query.multiselect(root.get("id"), join.get("configurationId"), root.get("productCategory"), root.get("productCategorySubtype")).distinct(true);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public ThresholdConfiguration fetchThresholdForConfigurationId(String configurationId) {
        return this.findOne(configurationId);
    }

}
