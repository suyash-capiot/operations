package com.coxandkings.travel.operations.repository.communication;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationUnreadCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import java.util.Map;

public interface CommunicationRepository {
    BaseCommunication getCommunicationById(String id);
    Map<String,Object> getCommunicationByBookingId(String bookId, Integer size, Integer page);
    Map<String, Object> getCommunicationByUserId(String UserId , Integer size, Integer page) ;

    Map<String, Object> getCommunicationByBookingId(String bookId, Integer size, Integer page, String sort);

    Map<String, Object> getCommunicationByUserId(String UserId, Integer size, Integer page, String sort);
    Map<String, Long> getUnreadCounts(CommunicationUnreadCriteria communicationUnreadCriteria);
    Map<String,Object> getCommunicationByCriteria(CommunicationCriteria criteria) throws OperationException;
    //List<BaseCommunication> getCommunicationByCriteria(CommunicationTagCriteria criteria);
}
