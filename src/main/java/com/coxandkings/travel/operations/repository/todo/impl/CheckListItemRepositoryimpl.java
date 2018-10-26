package com.coxandkings.travel.operations.repository.todo.impl;

import com.coxandkings.travel.operations.model.todo.ToDoCheckListItem;
import com.coxandkings.travel.operations.repository.todo.CheckListItemRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CheckListItemRepositoryimpl extends SimpleJpaRepository<ToDoCheckListItem, String> implements CheckListItemRepository {
    public CheckListItemRepositoryimpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ToDoCheckListItem.class, em);
    }


    @Override
    public ToDoCheckListItem saveOrUpdate(ToDoCheckListItem checkListItem) {
        return this.saveAndFlush(checkListItem);
    }

    @Override
    public ToDoCheckListItem getById(String id) {
        return this.findOne(id);
    }
}
