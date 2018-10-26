package com.coxandkings.travel.operations.repository.changeSupplierPrice.impl;

import com.coxandkings.travel.operations.model.booking.NewSellingPriceRecord;
import com.coxandkings.travel.operations.repository.changeSupplierPrice.ChangeSupplierPriceRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ChangeSupplierPriceRepoImpl extends SimpleJpaRepository<NewSellingPriceRecord,String> implements ChangeSupplierPriceRepo {

    private EntityManager em;

    public ChangeSupplierPriceRepoImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(NewSellingPriceRecord.class, entityManager);
        this.em=entityManager;
    }

    @Override
    public NewSellingPriceRecord add(NewSellingPriceRecord newSellingPriceRecord) {
        return this.saveAndFlush(newSellingPriceRecord);
    }

    @Override
    public NewSellingPriceRecord update(NewSellingPriceRecord newSellingPriceRecord) {
        List<NewSellingPriceRecord> newSellingPriceRecords=this.findAll();
        NewSellingPriceRecord newSellingPriceRecord2=null;
        for(NewSellingPriceRecord newSellingPriceRecord1:newSellingPriceRecords){
            if(newSellingPriceRecord1.getBookingRefNo().equals(newSellingPriceRecord.getBookingRefNo())){
                newSellingPriceRecord1.setNewSellingPriceRecordDetails(newSellingPriceRecord.getNewSellingPriceRecordDetails());
                newSellingPriceRecord2=this.saveAndFlush(newSellingPriceRecord1);
            }
        }
        return newSellingPriceRecord2;
    }

    @Override
    public void delete(String bookingRefno) {
        List<NewSellingPriceRecord> newSellingPriceRecords=this.findAll();
        for(NewSellingPriceRecord newSellingPriceRecord:newSellingPriceRecords){
            if(newSellingPriceRecord.getBookingRefNo().equals(bookingRefno)){
                this.delete(newSellingPriceRecord);
                this.flush();
            }
        }
    }
}
