package com.coxandkings.travel.operations.service.letter;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.letter.Letter;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.letter.LetterResource;
import org.springframework.stereotype.Service;

@Service
public interface LetterService {
    Letter save(LetterResource letterResource) throws OperationException;
    Letter getLetterById(String id) throws OperationException;
    Letter markAsRead(String id) throws OperationException;
    CommunicationTags getAssociatedTags(String id) throws OperationException;
    Letter updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException;
}
