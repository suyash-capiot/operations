package com.coxandkings.travel.operations.repository.supplierbillpassing.impl;

import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.repository.supplierbillpassing.SupplierBillPassingRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@Repository
public class SupplierBillPassingRepositoryImpl extends SimpleJpaRepository<SupplierBillPassing, String> implements SupplierBillPassingRepository {

    private EntityManager entityManager;
    private Logger logger=Logger.getLogger(SupplierBillPassingRepositoryImpl.class);
    public SupplierBillPassingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(SupplierBillPassing.class, em);
        this.entityManager=em;
    }

    @Override
    public SupplierBillPassing add(SupplierBillPassing supplierBillPassing) {
        return this.saveAndFlush(supplierBillPassing);
    }

    @Override
    public SupplierBillPassing update(SupplierBillPassing supplierBillPassing) {
        return this.saveAndFlush(supplierBillPassing);
    }

    @Override
    public SupplierBillPassing get(String id) {
        return this.findOne(id);
    }

    @Override
    public void remove(String id) {
         this.delete(id);
    }

    @Override
    public List<SupplierBillPassing> getAll() {
        return this.findAll();
    }

}
