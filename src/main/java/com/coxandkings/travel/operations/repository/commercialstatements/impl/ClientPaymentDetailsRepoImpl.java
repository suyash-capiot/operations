package com.coxandkings.travel.operations.repository.commercialstatements.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatementPaymentDetail;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentDetail;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientPaymentAdviceRepo;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientPaymentDetailsRepo;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class ClientPaymentDetailsRepoImpl extends SimpleJpaRepository<ClientPaymentDetail,String>  implements ClientPaymentDetailsRepo {

    private EntityManager entityManager;

    private Logger logger=Logger.getLogger(ClientPaymentDetailsRepoImpl.class);

    public ClientPaymentDetailsRepoImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ClientPaymentDetail.class, entityManager);
        this.entityManager=entityManager;
    }

    @Autowired
    private ClientPaymentAdviceRepo clientPaymentadviceRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ClientPaymentDetail createPaymentDetail(ClientPaymentDetail clientPaymentDetail) throws OperationException {
        return this.save(clientPaymentDetail);
    }

    @Override
    public ClientPaymentDetail find(String id) throws OperationException {
        return this.findOne(id);
    }

    @Override
    @Transactional
    public String updateClientPaymentDetail(ClientPaymentDetail newClientPaymentDetail) throws OperationException {
        ClientPaymentAdvice clientPaymentAdvice = clientPaymentadviceRepository.getById(newClientPaymentDetail.getPaymentAdviceDetails().getId());
        Set<ClientCommercialStatementPaymentDetail> clientDetailsSet = newClientPaymentDetail.getClientCommercialStatementPaymentDetails();
        BigDecimal amount = new BigDecimal(0);
        for(ClientCommercialStatementPaymentDetail clientCommercialStatementPaymentDetail:clientDetailsSet)
        {
           amount = amount.add(clientCommercialStatementPaymentDetail.getAmountToBePaid());
        }
        amount =  clientPaymentAdvice.getBalanceAmtPayableToClient().subtract(amount);
        clientPaymentAdvice.setBalanceAmtPayableToClient(amount);

        clientPaymentadviceRepository.update(clientPaymentAdvice);
        this.saveAndFlush(newClientPaymentDetail);
        return "update successfull";
    }

    @Override
    public ClientPaymentDetail serachPaymentDetails(String paymentAdviceNumber) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ClientPaymentDetail> criteriaQuery = cb.createQuery(ClientPaymentDetail.class);
            Root<ClientPaymentDetail> root = criteriaQuery.from(ClientPaymentDetail.class);
            Join<ClientPaymentDetail, ClientPaymentAdvice> join = root.join("paymentAdviceDetails");
            criteriaQuery.where(cb.equal(join.get("paymentAdviceNumber"), paymentAdviceNumber));
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        }catch (Exception e){
            logger.error("error occured in get payment details method");
            return null;
        }
    }

//    @Override
//    @Transactional
//    public String updateClientPaymentDetail(ClientPaymentDetail clientPaymentDetail, ClientPaymentDetail newClientPaymentDetail) throws OperationException {
//
//        CopyUtils.copy(newClientPaymentDetail,clientPaymentDetail);
//        ClientPaymentAdvice clientPaymentAdvice = clientPaymentadviceRepository.getById(newClientPaymentDetail.getPaymentAdviceDetails().getId());
//        Set<ClientCommercialStatementPaymentDetail> clientDetailsSet = newClientPaymentDetail.getClientCommercialStatementPaymentDetails();
//        BigDecimal amount = new BigDecimal(0);
//        for(ClientCommercialStatementPaymentDetail clientCommercialStatementPaymentDetail:clientDetailsSet)
//        {
//           amount = amount.add(clientCommercialStatementPaymentDetail.getAmountToBePaid());
//        }
//        amount =  clientPaymentAdvice.getBalanceAmtPayableToClient().subtract(amount);
//        clientPaymentAdvice.setBalanceAmtPayableToClient(amount);
//
//        clientPaymentadviceRepository.update(clientPaymentAdvice);
//        this.saveAndFlush(clientPaymentDetail);
//        return "update successfull";
//    }
}
