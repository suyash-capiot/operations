package com.coxandkings.travel.operations.repository.alternateoptions.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsResponseCriteria;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsResponseRepository;

@Repository
public class AlternateOptionsResponseRepositoryImpl extends SimpleJpaRepository<AlternateOptionsResponseDetails, String> implements AlternateOptionsResponseRepository {
  
  private EntityManager entityManager;

  public AlternateOptionsResponseRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
      super(AlternateOptionsResponseDetails.class, em);
      entityManager = em;
  }

  @Override
  @Transactional
  public AlternateOptionsResponseDetails saveOrUpdate(AlternateOptionsResponseDetails alternateOptionsResponseDetails) 
  {
    return this.saveAndFlush(alternateOptionsResponseDetails);
  }
  
  @Override
  public AlternateOptionsResponseDetails getAlternateOptionsSentById(String id) {
    AlternateOptionsResponseDetails alternateOptionsResponseDetails = findOne(id);
    return alternateOptionsResponseDetails;
  }
  
  @Override
  public Map<String, Object> searchAlternateOptionResponse(AlternateOptionsResponseCriteria alternateOptionsResponseCriteria) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AlternateOptionsResponseDetails> query = builder.createQuery(AlternateOptionsResponseDetails.class);
    Root<AlternateOptionsResponseDetails> root = query.from(AlternateOptionsResponseDetails.class);
    query.select(root);

    Integer size = alternateOptionsResponseCriteria.getPageSize()!=null ? alternateOptionsResponseCriteria.getPageSize()  : 5;
    Integer page = alternateOptionsResponseCriteria.getPageNumber()!=null ? alternateOptionsResponseCriteria.getPageNumber(): 1;

    List<Predicate> p1 = new ArrayList<>();

    if (!StringUtils.isEmpty(alternateOptionsResponseCriteria.getSortCriteria())) {
      query.orderBy(alternateOptionsResponseCriteria.getDescending() ? builder.desc(root.get(alternateOptionsResponseCriteria.getSortCriteria())) :
        builder.asc(root.get(alternateOptionsResponseCriteria.getSortCriteria())));
    }

    if (alternateOptionsResponseCriteria.getBookID() != null && !alternateOptionsResponseCriteria.getBookID().isEmpty())
        p1.add(builder.equal(root.get("bookID"), alternateOptionsResponseCriteria.getBookID()));

    if (alternateOptionsResponseCriteria.getOrderID() != null && !alternateOptionsResponseCriteria.getOrderID().isEmpty())
        p1.add(builder.equal(root.get("orderID"), alternateOptionsResponseCriteria.getOrderID()));

    if (alternateOptionsResponseCriteria.getConfigurationId() != null && !alternateOptionsResponseCriteria.getConfigurationId().isEmpty())
        p1.add(builder.equal(root.get("configurationId"), alternateOptionsResponseCriteria.getConfigurationId()));

    if (alternateOptionsResponseCriteria.getResponseId() != null && !alternateOptionsResponseCriteria.getResponseId().isEmpty())
        p1.add(builder.equal(root.get("id"), alternateOptionsResponseCriteria.getResponseId()));
      
    if (alternateOptionsResponseCriteria.getLeadPaxName() != null && !alternateOptionsResponseCriteria.getLeadPaxName().isEmpty())
      p1.add(builder.equal(root.get("leadPaxName"), alternateOptionsResponseCriteria.getLeadPaxName()));

    
    if (!p1.isEmpty()) {
      query.where(p1.toArray(new Predicate[0]));
    }

    System.out.println("test working till here");
    
    query.distinct(true);
    TypedQuery<AlternateOptionsResponseDetails> typedquery = entityManager.createQuery(query);
    Integer actualSize = typedquery.getResultList().size();
    Integer startIndex = 0;
  
    System.out.println("test working till here");
    
    if (size != null && page != null) {
        startIndex = (page - 1) * size;
        typedquery.setFirstResult(startIndex);
        typedquery.setMaxResults(size);
    }
    
    System.out.println("test working till here");
    
    List<AlternateOptionsResponseDetails> alternateOptionsResponse = typedquery.getResultList();
    
    List<JSONObject> alternateOptionsResponseDetailsSent = new ArrayList<JSONObject>();
    
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    
    for(AlternateOptionsResponseDetails alternateOptionsResponseDetails : alternateOptionsResponse)
    {
      JSONObject responseDetails = new JSONObject();
      
      responseDetails.put("id", alternateOptionsResponseDetails.getId());
      responseDetails.put("leadPaxName", alternateOptionsResponseDetails.getLeadPaxName());
      responseDetails.put("productCategory", alternateOptionsResponseDetails.getProductCategory());
      responseDetails.put("tourStartDate", alternateOptionsResponseDetails.getTourStartDate());
      responseDetails.put("tourEndDate", alternateOptionsResponseDetails.getTourEndDate());
      responseDetails.put("alternateOptionSentDate", alternateOptionsResponseDetails.getAlternateOptionSentDate());
      responseDetails.put("failedBookingPrice", alternateOptionsResponseDetails.getFailedBookingPrice());
      responseDetails.put("alternateOptionPrice", alternateOptionsResponseDetails.getAlternateOptionPrice());
      responseDetails.put("bookID", alternateOptionsResponseDetails.getBookID());
      responseDetails.put("orderID", alternateOptionsResponseDetails.getOrderID());
      responseDetails.put("alternateOptionDetails", new JSONObject(alternateOptionsResponseDetails.getAlternateOptionDetails()));
      responseDetails.put("status", alternateOptionsResponseDetails.getStatus());
      responseDetails.put("payable", alternateOptionsResponseDetails.isPayable());
      
      alternateOptionsResponseDetailsSent.add(responseDetails);
    }
  
    return applyPagination(alternateOptionsResponseDetailsSent, size, page, actualSize);
   }
  
  private Map<String, Object> applyPagination(List<JSONObject> alternateOptionsResponseDetails, Integer size, Integer page, Integer actualSize) {
      Map<String, Object> entity = new HashMap<>();
      entity.put("data", alternateOptionsResponseDetails);
      entity.put("totalCount", alternateOptionsResponseDetails.size());
      
      if (alternateOptionsResponseDetails.isEmpty())
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

  
}
