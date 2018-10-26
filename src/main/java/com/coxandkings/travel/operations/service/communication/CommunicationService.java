package com.coxandkings.travel.operations.service.communication;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationTagCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationUnreadCriteria;
import com.coxandkings.travel.operations.enums.communication.UserProfileType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public interface CommunicationService {
    Map<String,Object> getCommunicationByBookingId (String bookId , Integer size , Integer page) ;
    Map<String,Object> getCommunicationByUserId(String userId, Integer size , Integer page ) ;

    Map<String, Object> getCommunicationByBookingId(String bookId, Integer size, Integer page, String sort);

    Map<String, Object> getCommunicationByUserId(String userId, Integer size, Integer page, String sort);
    List<BaseCommunication> getCommunicationByCommunicationTagsCriteria(CommunicationTagCriteria criteria) ;
    Map<String,Object> getCommunicationByCriteria(CommunicationCriteria criteria) throws OperationException;
    String getTemplateDetails(String businessProcess , String function) throws OperationException;
    String getCommunicationTemplate(String templateId) throws OperationException, JSONException;
    List<String> getListEmailIds(String criteria, UserProfileType userProfileType) throws OperationException;
    Long countTotalUnread(List<BaseCommunication> communications);
    Map<String, Long> getUnreadCounts(CommunicationUnreadCriteria communicationUnreadCriteria);
}
