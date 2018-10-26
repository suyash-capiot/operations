package com.coxandkings.travel.operations.repository.communication;

import com.coxandkings.travel.operations.criteria.communication.CommunicationTagCriteria;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;

import java.util.List;

public interface CommunicationTagRepository {

    List<CommunicationTags> getCommunicationByTAGCriteria(CommunicationTagCriteria criteria);

}
