package com.coxandkings.travel.operations.repository.chat;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.model.chat.Chat;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatRepository {
    Chat saveChat(Chat chat);
    Chat getChatById(String id);


    List<? extends BaseCommunication> getByCriteria(CommunicationCriteria criteria);
}
