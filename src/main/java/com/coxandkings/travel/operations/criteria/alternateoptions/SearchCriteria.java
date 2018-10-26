package com.coxandkings.travel.operations.criteria.alternateoptions;

import java.io.Serializable;
import com.coxandkings.travel.operations.criteria.workflow.GenericCriteria;

public class SearchCriteria extends GenericCriteria{

  private AlternateOptionsCriteria filter;

  public AlternateOptionsCriteria getFilter() {
    return filter;
  }

  public void setFilter(AlternateOptionsCriteria filter) {
    this.filter = filter;
  }
  
  
}
