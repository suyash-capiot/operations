package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.PaymentInformationStandardDetail;
import com.coxandkings.travel.operations.resource.whitelabel.PaymentInformationResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface WhiteLabelPaymentInformationService {

    PaymentInformationStandardDetail createOrUpdatePaymentInformation(
            PaymentInformationResource paymentInformationResource, MultipartFile file) throws OperationException;

}
