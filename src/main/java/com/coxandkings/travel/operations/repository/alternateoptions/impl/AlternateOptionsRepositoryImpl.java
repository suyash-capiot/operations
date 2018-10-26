package com.coxandkings.travel.operations.repository.alternateoptions.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsStatus;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsProductDetails;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsRepository;

@Repository
public class AlternateOptionsRepositoryImpl extends
        SimpleJpaRepository<AlternateOptions, String> implements AlternateOptionsRepository {

    private EntityManager entityManager;

    public AlternateOptionsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(AlternateOptions.class, em);
        entityManager = em;
    }

    @Override
    public List<AlternateOptions> saveAlternateOptions(List<AlternateOptions> alternateOptions) {
        final List<AlternateOptions> savedEntities = new ArrayList<AlternateOptions>(alternateOptions.size());

        for (AlternateOptions alternateOption : alternateOptions) {
            savedEntities.add(persistOrMerge(alternateOption));
        }
        entityManager.flush();
        entityManager.clear();
        return savedEntities;
    }


    private AlternateOptions persistOrMerge(AlternateOptions alternateOptions) {
        if (alternateOptions.getConfigurationId() == null) {
            entityManager.persist(alternateOptions);
            return alternateOptions;
        } else {
            return entityManager.merge(alternateOptions);
        }
    }

    @Override
    public List<AlternateOptions> searchAlternateOptions(AlternateOptions alternateOptions) {


        String comapnyMarket = alternateOptions.getCompanyMarket();
        String clientType = alternateOptions.getClientType();
        String productcategory = alternateOptions.getAlternateOptionsProductDetails().get(0).getProductCategory();
        String productSubCategory = alternateOptions.getAlternateOptionsProductDetails().get(0).getProductCategorySubType();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlternateOptions> query = builder.createQuery(AlternateOptions.class);
        Root<AlternateOptions> root = query.from(AlternateOptions.class);

        Join<AlternateOptions, AlternateOptionsProductDetails> join = root.join("alternateOptionsProductDetails");


        List<Predicate> p1 = new ArrayList<Predicate>();

        /*p1.add(builder.equal(root.get("status"), AlternateOptionsStatus.APPROVED.getValue()));*/

        if (comapnyMarket != null && !comapnyMarket.isEmpty())
            p1.add(builder.and(builder.equal(root.get("companyMarket"), comapnyMarket)));

        if (clientType != null && !clientType.isEmpty())
            p1.add(builder.and(builder.equal(root.get("clientType"), clientType)));

        if (productcategory != null && !productcategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productCategory"), productcategory)));

        if (productSubCategory != null && !productSubCategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productcategorySubtype"), productSubCategory)));


        query.where(p1.toArray(new Predicate[p1.size()]));

        List<AlternateOptions> alternateOptions2 = entityManager.createQuery(query).getResultList();

        Hibernate.initialize(alternateOptions2);

        alternateOptions2 = alternateOptions2.stream().filter(x -> x.getAlternateOptionsProductDetails().equals(x.getAlternateOptionsProductDetails())).collect(Collectors.toList());

        return alternateOptions2;
    }

    @Override
    public AlternateOptionResponse searchAlternateOptions(AlternateOptions alternateOptions, int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        AlternateOptionResponse alternateOptionResponse = new AlternateOptionResponse();

        String comapnyMarket = alternateOptions.getCompanyMarket();
        String clientType = alternateOptions.getClientType();
        String productcategory = alternateOptions.getAlternateOptionsProductDetails().get(0).getProductCategory();
        String productSubCategory = alternateOptions.getAlternateOptionsProductDetails().get(0).getProductCategorySubType();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlternateOptions> query = builder.createQuery(AlternateOptions.class);
        Root<AlternateOptions> root = query.from(AlternateOptions.class);

        Join<AlternateOptions, AlternateOptionsProductDetails> join = root.join("alternateOptionsProductDetails");


        List<Predicate> p1 = new ArrayList<Predicate>();

        /*p1.add(builder.equal(root.get("status"), AlternateOptionsStatus.APPROVED.getValue()));*/

        if (comapnyMarket != null && !comapnyMarket.isEmpty())
            p1.add(builder.and(builder.equal(root.get("companyMarket"), comapnyMarket)));

        if (clientType != null && !clientType.isEmpty())
            p1.add(builder.and(builder.equal(root.get("clientType"), clientType)));

        if (productcategory != null && !productcategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productCategory"), productcategory)));

        if (productSubCategory != null && !productSubCategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productcategorySubtype"), productSubCategory)));


        query.where(p1.toArray(new Predicate[p1.size()])).distinct(true);
        Long count = getCountByCriteria(alternateOptions);

        TypedQuery<AlternateOptions> typedQuery = entityManager.createQuery(query);


        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<AlternateOptions> alternateOptions2 = typedQuery.getResultList();
        Hibernate.initialize(alternateOptions2);

        alternateOptions2 = alternateOptions2.stream().filter(x -> x.getAlternateOptionsProductDetails().equals(x.getAlternateOptionsProductDetails())).collect(Collectors.toList());
        alternateOptionResponse.setCount((count/pageSize)+1);
        alternateOptionResponse.setAlternateOptions(alternateOptions2);
        return alternateOptionResponse;
    }

    private Long getCountByCriteria(AlternateOptions alternateOptions) {

        AlternateOptionResponse alternateOptionResponse = new AlternateOptionResponse();

        String comapnyMarket = alternateOptions.getCompanyMarket();
        String clientType = alternateOptions.getClientType();
        String productcategory = alternateOptions.getAlternateOptionsProductDetails().get(0).getProductCategory();
        String productSubCategory = alternateOptions.getAlternateOptionsProductDetails().get(0).getProductCategorySubType();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<AlternateOptions> root = query.from(AlternateOptions.class);
        query.select(builder.countDistinct(root));
        Join<AlternateOptions, AlternateOptionsProductDetails> join = root.join("alternateOptionsProductDetails");


        List<Predicate> p1 = new ArrayList<Predicate>();

        /*p1.add(builder.equal(root.get("status"), AlternateOptionsStatus.APPROVED.getValue()));*/

        if (comapnyMarket != null && !comapnyMarket.isEmpty())
            p1.add(builder.and(builder.equal(root.get("companyMarket"), comapnyMarket)));

        if (clientType != null && !clientType.isEmpty())
            p1.add(builder.and(builder.equal(root.get("clientType"), clientType)));

        if (productcategory != null && !productcategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productCategory"), productcategory)));

        if (productSubCategory != null && !productSubCategory.isEmpty())
            p1.add(builder.and(builder.equal(join.get("productcategorySubtype"), productSubCategory)));


        query.where(p1.toArray(new Predicate[p1.size()]));



        return entityManager.createQuery(query).getSingleResult();

    }


    @Override
    public List<AlternateOptions> fetchAlternateoptions() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlternateOptions> query = builder.createQuery(AlternateOptions.class);
        Root<AlternateOptions> root = query.from(AlternateOptions.class);

        query.multiselect(root.get("configurationId"), root.get("companyMarket"), root.get("clientType")).distinct(true);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<AlternateOptionsProductDetails> fetchAlternateoptionProductDetails() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlternateOptionsProductDetails> query = builder.createQuery(AlternateOptionsProductDetails.class);
        Root<AlternateOptionsProductDetails> root = query.from(AlternateOptionsProductDetails.class);

        Join<AlternateOptionsProductDetails, AlternateOptions> join = root.join("alternateOptions");

        query.multiselect(root.get("id"), join.get("configurationId"), root.get("productCategory"), root.get("productcategorySubtype")).distinct(true);

        return entityManager.createQuery(query).getResultList();

    }

    @Override
    public AlternateOptions fetchAlternateOptionsForConfigurationId(String configurationId) {
        AlternateOptions alternateOptions = this.findOne(configurationId);
        return alternateOptions;
    }

    @Override
    public AlternateOptions update(AlternateOptions alternateOptions) {
        return this.saveAndFlush(alternateOptions);
    }

}
