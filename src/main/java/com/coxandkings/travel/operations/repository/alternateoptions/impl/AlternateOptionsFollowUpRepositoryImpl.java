package com.coxandkings.travel.operations.repository.alternateoptions.impl;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsFollowUpDetails;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsFollowUpRepository;

@Repository
public class AlternateOptionsFollowUpRepositoryImpl extends SimpleJpaRepository<AlternateOptionsFollowUpDetails, String> implements AlternateOptionsFollowUpRepository {

  private EntityManager entityManager;

  public AlternateOptionsFollowUpRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
      super(AlternateOptionsFollowUpDetails.class, em);
      entityManager = em;
  }

  @Override
  @Transactional
  public AlternateOptionsFollowUpDetails saveOrUpdate(AlternateOptionsFollowUpDetails alternateOptionsResponseDetails) 
  {
    return this.saveAndFlush(alternateOptionsResponseDetails);
  }
  
}
