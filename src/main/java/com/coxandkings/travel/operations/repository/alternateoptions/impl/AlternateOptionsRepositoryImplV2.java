package com.coxandkings.travel.operations.repository.alternateoptions.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsCriteria;
import com.coxandkings.travel.operations.criteria.alternateoptions.SearchCriteria;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsStatus;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsCompanyDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsProductDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsRepository;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsRepositoryV2;

@Repository
public class AlternateOptionsRepositoryImplV2 extends
        SimpleJpaRepository<AlternateOptionsV2, String> implements AlternateOptionsRepositoryV2 {

    private EntityManager entityManager;

    public AlternateOptionsRepositoryImplV2(@Qualifier("opsEntityManager") EntityManager em) {
        super(AlternateOptionsV2.class, em);
        entityManager = em;
    }

    @Override
    public List<AlternateOptionsV2> saveAlternateOptions(List<AlternateOptionsV2> alternateOptions) {
        final List<AlternateOptionsV2> savedEntities = new ArrayList<AlternateOptionsV2>(alternateOptions.size());

        for (AlternateOptionsV2 alternateOption : alternateOptions) {
            savedEntities.add(persistOrMerge(alternateOption));
        }
        entityManager.flush();
        entityManager.clear();
        return savedEntities;
    }


    private AlternateOptionsV2 persistOrMerge(AlternateOptionsV2 alternateOptions) {
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
    public AlternateOptionsV2 fetchAlternateOptionsForConfigurationId(String configurationId) {
      AlternateOptionsV2 alternateOptions = findOne(configurationId);
        return alternateOptions;
    }

    @Override
    @Transactional
    public AlternateOptionsV2 update(AlternateOptionsV2 alternateOptions) {
        return this.saveAndFlush(alternateOptions);
    }

    @Override
    public Map<String, Object> getAlternateOptionsByCriteria(SearchCriteria searchCriteria) {

      AlternateOptionsCriteria alternateOptionsCriteria = searchCriteria.getFilter();
      
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<AlternateOptionsV2> query = builder.createQuery(AlternateOptionsV2.class);
      Root<AlternateOptionsV2> root = query.from(AlternateOptionsV2.class);
      query.select(root);

      Join<AlternateOptionsV2, AlternateOptionsCompanyDetails> companyDetailsJoin = root.joinSet("companyDetails");
      Join<AlternateOptionsV2, AlternateOptionsProductDetails> productJoin = root.joinSet("productDetails");

      Integer size = searchCriteria.getCount() !=null ? searchCriteria.getCount() : 5;
      Integer page = searchCriteria.getPage()!=null ? searchCriteria.getPage() : 1;

      List<Predicate> p1 = new ArrayList<>();

      /*p1.add(builder.equal(root.get("status"), AlternateOptionsStatus.APPROVED.getValue()));*/
      
      if (!StringUtils.isEmpty(alternateOptionsCriteria.getSortCriteria())) {
        query.orderBy(alternateOptionsCriteria.getDescending() ? builder.desc(root.get(alternateOptionsCriteria.getSortCriteria())) :
          builder.asc(root.get(alternateOptionsCriteria.getSortCriteria())));
      }

      if (alternateOptionsCriteria.getCompanyMarket() != null && !alternateOptionsCriteria.getCompanyMarket().isEmpty())
          p1.add(builder.equal(companyDetailsJoin.get("companyMarket"), alternateOptionsCriteria.getCompanyMarket()));

      if (alternateOptionsCriteria.getClientType() != null && !alternateOptionsCriteria.getClientType().isEmpty())
          p1.add(builder.equal(companyDetailsJoin.get("clientType"), alternateOptionsCriteria.getClientType()));

      if (alternateOptionsCriteria.getProductCategory() != null && !alternateOptionsCriteria.getProductCategory().isEmpty())
          p1.add(builder.equal(productJoin.get("productCategory"), alternateOptionsCriteria.getProductCategory()));

      if (alternateOptionsCriteria.getProductCategorySubType() != null && !alternateOptionsCriteria.getProductCategorySubType().isEmpty())
          p1.add(builder.equal(productJoin.get("productCategorySubType"), alternateOptionsCriteria.getProductCategorySubType()));
      
      if (alternateOptionsCriteria.getCompanyId()!= null && !alternateOptionsCriteria.getCompanyId().isEmpty())
          p1.add(builder.equal(root.get("companyId"), alternateOptionsCriteria.getCompanyId()));


      if (!p1.isEmpty()) {
        query.where(p1.toArray(new Predicate[0]));
      }

      System.out.println("test working till here");
      
    query.distinct(true);
    TypedQuery<AlternateOptionsV2> typedquery = entityManager.createQuery(query);
    
    List<AlternateOptionsV2> alternateOptionstotalRecords = typedquery.getResultList();
    Integer totalCount = alternateOptionstotalRecords.size();
    
    Integer actualSize = typedquery.getResultList().size();
    Integer startIndex = 0;

    System.out.println("test working till here");
    
    if (size != null && page != null) {
        startIndex = (page - 1) * size;
        typedquery.setFirstResult(startIndex);
        typedquery.setMaxResults(size);
    }
    
    System.out.println("test working till here");
    
    List<AlternateOptionsV2> alternateOptionsRecord = typedquery.getResultList();
    
    System.out.println("test working till here");
    
    for(AlternateOptionsV2 alternateOptionRecord : alternateOptionsRecord)
    {
      if(alternateOptionRecord.getLock()!=null)
      {
        alternateOptionRecord.getLock().setUser(alternateOptionRecord.getLock().getUserId());
      }
    }

    return applyPagination(alternateOptionsRecord, size, page, actualSize, totalCount);
   }
    
    private Map<String, Object> applyPagination(List<AlternateOptionsV2> alternateOptionsRecords, Integer size, Integer page, Integer actualSize, Integer totalCount) {
      Map<String, Object> entity = new HashMap<>();
      entity.put("data", alternateOptionsRecords);
      entity.put("totalCount", totalCount);
      
      if (alternateOptionsRecords.isEmpty())
          return entity;

      entity.put("size", size);
      entity.put("page", page);

      Integer noOfPages = 0;
      actualSize = (null == actualSize) ? 0 : actualSize;
      size = (null == size) ? actualSize : size;
      if (actualSize % size == 0)
          noOfPages = actualSize / size;
      else noOfPages = actualSize / size + 1;

      entity.put("size", size);
      entity.put("page", page);
      entity.put("noOfPages", noOfPages);
      
      return entity;
  }

    @Override
    public int deleteMasterRecord(String configurationId) {
      String sqlQuery ="";

      sqlQuery = String.format("DELETE FROM operations_schema.alternateoptionslock\r\n" + 
          "      WHERE id='%s'",configurationId);

      System.out.println(sqlQuery);
      System.out.println();

      int deleted = entityManager.createNativeQuery(sqlQuery).executeUpdate();

      return deleted;
    }

}
