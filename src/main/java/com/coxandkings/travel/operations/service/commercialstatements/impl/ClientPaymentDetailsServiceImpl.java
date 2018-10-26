package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatementPaymentDetail;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentDetail;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientPaymentAdviceRepo;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientPaymentDetailsRepo;
import com.coxandkings.travel.operations.repository.commercialstatements.impl.ClientCommercialStatementRepoImpl;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentdetails.PaymentDetailsRepository;
import com.coxandkings.travel.operations.service.commercialstatements.ClientCommercialStatementService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientPaymentDetailsService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class ClientPaymentDetailsServiceImpl implements ClientPaymentDetailsService{

    private Logger logger=Logger.getLogger(ClientPaymentDetailsServiceImpl.class);
    @Autowired
    private ClientPaymentDetailsRepo clientPaymentDetailsRepository;

    @Autowired
    private ClientPaymentAdviceRepo clientPaymentadviceRepository;

    @Autowired
    private ClientCommercialStatementService clientCommercialStatementService;

//    @Autowired
//    ClientCommercialStatementRepoImpl clientCommercialStatementRepo;

    @Override
    public ClientPaymentDetail createPaymentDetail(ClientPaymentDetail clientPaymentDetail) throws OperationException {

        ClientPaymentDetail paymentDetail = null;
        try {
            if(clientPaymentDetail.getPaymentAdviceDetails().getPaymentAdviceNumber()!=null)
            {
                 ClientPaymentAdvice clientPaymentAdvice  = clientPaymentadviceRepository.getById(clientPaymentDetail.getPaymentAdviceDetails().getId());
                    Set<ClientCommercialStatementPaymentDetail> clientDetailsSet = clientPaymentDetail.getClientCommercialStatementPaymentDetails();
                    BigDecimal amount = new BigDecimal(0);
                    for(ClientCommercialStatementPaymentDetail clientCommercialStatementPaymentDetail:clientDetailsSet)
                    {
                        amount = amount.add(clientCommercialStatementPaymentDetail.getAmountToBePaid());
                        //added here
//                        ClientCommercialStatement oldClientCommStatement = clientCommercialStatementRepo.get(clientCommercialStatementPaymentDetail.getClientCommercialStatementId());
//                        oldClientCommStatement.setTotalPaid(clientCommercialStatementPaymentDetail.getAmountToBePaid());
//                        BigDecimal totalPayable = oldClientCommStatement.getTotalPayable().subtract(clientCommercialStatementPaymentDetail.getAmountToBePaid());
//                        oldClientCommStatement.setTotalPayable(totalPayable);
//                        oldClientCommStatement.setBalancePayable(oldClientCommStatement.getTotalPayable().subtract(oldClientCommStatement.getTotalPaid()));
//                        clientCommercialStatementRepo.update(oldClientCommStatement);
                    }
//                    amount =  clientPaymentAdvice.getBalanceAmtPayableToClient().subtract(amount);
                    if(clientPaymentAdvice.getAmountPayableForClient().compareTo(amount)<0)
                    {
                        throw new OperationException("Payment details cannot be created as amount in payment advice for client is less than amount present in payment detail request ");
                    }
                    else
                    {
                        clientPaymentAdvice.setAmountPaidByFinance(amount);
                        clientPaymentadviceRepository.update(clientPaymentAdvice);
                        paymentDetail = clientPaymentDetailsRepository.createPaymentDetail(clientPaymentDetail);
                    }

                if(clientPaymentAdvice.getAmountPayableForClient().compareTo(amount)==0)
                    clientCommercialStatementService.updatePaymentDetails(clientPaymentDetail.getPaymentAdviceDetails());
            }
            return paymentDetail;
        }catch (OperationException e){
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }

    }

    @Override
    public String updatePaymentDetail(ClientPaymentDetail clientPaymentDetail) throws OperationException {
        String  message= clientPaymentDetailsRepository.updateClientPaymentDetail(clientPaymentDetail);
        return message;
    }

//    @Override
//    public String updatePaymentDetail(ClientPaymentDetail newClientPaymentDetail, String id) throws OperationException {
//        ClientPaymentDetail previousClientPaymentDetail= getClientPaymentDetailById(id);
//        String  message= clientPaymentDetailsRepository.updateClientPaymentDetail(previousClientPaymentDetail,newClientPaymentDetail);
//        return message;
//    }

    @Override
    public ClientPaymentDetail getClientPaymentDetailById(String id) throws OperationException {
        ClientPaymentDetail clientPaymentDetail = clientPaymentDetailsRepository.find(id);
        return clientPaymentDetail;
    }

    @Override
    public ClientPaymentDetail getPaymentDetails(String paymentAdviceNumber) throws OperationException {

        ClientPaymentDetail paymentDetails = clientPaymentDetailsRepository.serachPaymentDetails(paymentAdviceNumber);
        if (paymentDetails == null) {
            paymentDetails = new ClientPaymentDetail();
            ClientPaymentAdvice advice = clientPaymentadviceRepository.getByPaymentAdviceNumber(paymentAdviceNumber);
            if (advice != null) {
                if (PaymentAdviceStatusValues.APPROVED.equals(advice.getPaymentAdviceStatus()))
                    paymentDetails.setPaymentAdviceDetails(advice);
                else
                    throw new OperationException("Payment Details can not generate until Payment Advice is Approved");
            }
        }
        return paymentDetails;
    }

}
