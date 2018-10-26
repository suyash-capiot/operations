package com.coxandkings.travel.operations.repository.alternateoptions;

import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsResponseCriteria;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;

public interface AlternateOptionsResponseRepository {
  
  AlternateOptionsResponseDetails saveOrUpdate(AlternateOptionsResponseDetails alternateOptionsResponseDetails);
  
  Map<String, Object> searchAlternateOptionResponse(AlternateOptionsResponseCriteria alternateOptionsResponseCriteria);
  
  AlternateOptionsResponseDetails getAlternateOptionsSentById(String configurationId);
  
}
