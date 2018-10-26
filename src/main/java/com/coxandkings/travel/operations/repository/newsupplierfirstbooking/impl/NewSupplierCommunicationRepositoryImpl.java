package com.coxandkings.travel.operations.repository.newsupplierfirstbooking.impl;

import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierCommunication;
import com.coxandkings.travel.operations.repository.newsupplierfirstbooking.NewSupplierCommunicationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class NewSupplierCommunicationRepositoryImpl extends SimpleJpaRepository<NewSupplierCommunication, String> implements NewSupplierCommunicationRepository {

    private EntityManager entityManager;

    public NewSupplierCommunicationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(NewSupplierCommunication.class, entityManager);
        this.entityManager = entityManager;
    }


    @Override
    public NewSupplierCommunication saveOrUpdate(NewSupplierCommunication newSupplierCommunication){
        if(isNewSupplierCommunicationExists(newSupplierCommunication.getBookId()))
        {
            return null;
        }
        else
            return this.saveAndFlush(newSupplierCommunication);
    }

    @Override
    public boolean isNewSupplierCommunicationExists(String id) {
        return this.exists(id);
    }
}
