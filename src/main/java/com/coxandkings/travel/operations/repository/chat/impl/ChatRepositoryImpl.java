package com.coxandkings.travel.operations.repository.chat.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.model.chat.Chat;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import com.coxandkings.travel.operations.repository.chat.ChatRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository("chatRepository")
@org.springframework.transaction.annotation.Transactional(readOnly = false)
public class ChatRepositoryImpl extends SimpleJpaRepository<Chat, String > implements ChatRepository {
    private EntityManager entityManager;
    public ChatRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(Chat.class, em);
        entityManager=em;
    }


    @Override
    public Chat saveChat(Chat chat) {
        System.out.println("Chat "+chat.toString());
        return this.saveAndFlush(chat);
    }

    @Override
    public Chat getChatById(String id) {
        Chat chat =this.findOne(id);
        return chat;
    }

    @Override
    public List<? extends BaseCommunication> getByCriteria(CommunicationCriteria criteria) {
        return findAll();
    }


}
