package com.coxandkings.travel.operations.service.chat;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.chat.Chat;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.resource.chat.ChatResource;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    Chat save(ChatResource chatResource);
    Chat getChatById(String id) throws OperationException;
    Chat markAsRead(String id) throws OperationException;
    CommunicationTags getAssociatedTags(String id);
    Chat updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException;
}
