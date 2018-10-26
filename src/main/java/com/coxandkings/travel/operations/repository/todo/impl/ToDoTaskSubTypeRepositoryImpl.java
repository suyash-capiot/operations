//package com.coxandkings.travel.operations.repository.todo.impl;
//
//import com.coxandkings.travel.operations.model.todo.ToDoSubType;
//import com.coxandkings.travel.operations.repository.todo.ToDoTaskSubTypeRepository;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//@Repository
//public class ToDoTaskSubTypeRepositoryImpl  extends SimpleJpaRepository<ToDoSubType, String> implements ToDoTaskSubTypeRepository {
//    private EntityManager entityManager;
//    public ToDoTaskSubTypeRepositoryImpl(@Qualifier("opsEntityManager")EntityManager em) {
//        super(ToDoSubType.class, em);
//        this.entityManager = em;
//    }
//
//    @Override
//    public ToDoSubType getById(String id) {
//        return findOne(id);
//    }
//
//    @Override
//    public List<ToDoSubType> getAll() {
//        return findAll();
//    }
//
//    @Override
//    public ToDoSubType saveOrUpdate(ToDoSubType toDoSubType) {
//        return this.saveAndFlush(toDoSubType);
//    }
//}
