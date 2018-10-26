package com.coxandkings.travel.operations.service.chat.impl;

import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.chat.Chat;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.repository.chat.ChatRepository;
import com.coxandkings.travel.operations.resource.chat.ChatResource;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.service.chat.ChatService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat save(ChatResource chatResource) {
        Chat chat = new Chat();
        if(chatResource != null){

            chat.setTransactionId(chatResource.getTransactionId());
            chat.setTransactionType(chatResource.getTransactionType());
            chat.setSender(chatResource.getSender());
            chat.setRecipientList(Collections.singletonList(chatResource.getReceiver()));
            chat.setChatScript(chatResource.getChatScript());
            chat.setCommunicationType(String.valueOf(CommunicationType.CHAT));
            chat.setProductSubCategory(chatResource.getProductSubCategory());
            chat.setSupplier(chatResource.getSupplier());
            chat.setProcess(chatResource.getProcess());
            chat.setScenario(chatResource.getScenario());
            chat.setFunction(chatResource.getFunction());
            chat.setBookId(chatResource.getBookId());
            chat.setUserId(chatResource.getUserId());

            if(chatResource.getCommunicationTagResource() != null) {
                CommunicationTags communicationTags = new CommunicationTags();
                communicationTags.setBaseCommunication(chat);
                CopyUtils.copy(chatResource.getCommunicationTagResource(), communicationTags);
                chat.setCommunicationTags(communicationTags);
            }
        }
        return chatRepository.saveChat(chat);
    }

    @Override
    public Chat getChatById(String id) throws OperationException {
        Chat chat = null;
        if(id != null) {
             chat = chatRepository.getChatById(id);
            if(chat == null ){
                throw new OperationException("Chat record does not exist");
            }
        }else{
            throw new OperationException("Invalid input");
        }
        return chat;
    }

    @Override
    public Chat markAsRead(String id) throws OperationException {
        Chat chat = chatRepository.getChatById(id);
        if(chat == null)
            throw new OperationException(Constants.ER01);
        chat.setRead(true);
        return this.chatRepository.saveChat(chat);
    }

    @Override
    public CommunicationTags getAssociatedTags(String id) {
        Chat chat = chatRepository.getChatById(id);
        return chat.getCommunicationTags();
    }

    @Override
    public Chat updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException {
        Chat chat = getChatById(id);
        CommunicationTags communicationTags = new CommunicationTags();
        CopyUtils.copy(communicationTagResource, communicationTags);
        chat.setCommunicationTags(communicationTags);
        return chatRepository.saveChat(chat);
    }
}
