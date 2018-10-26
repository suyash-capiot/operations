package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.PaymentInformationStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.PaymentInformationResource;
import com.coxandkings.travel.operations.service.whitelabel.FileSetupService;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelPaymentInformationService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("WhiteLabelPaymentInformation")
public class WhiteLabelPaymentInformationServiceImpl implements WhiteLabelPaymentInformationService{

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;

    @Autowired
    private FileSetupService fileSetUpService;

    public static final int FILE_CONTENT_INDEX = 0;

    public static final int FILE_PATH_INDEX = 1;


    @Override
    public PaymentInformationStandardDetail createOrUpdatePaymentInformation(PaymentInformationResource paymentInformationResource,
                                                                             MultipartFile file) throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = paymentInformationResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;


            if(existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if(!(existingWhiteLabel.getConfigurationTypeEnumHandler().getCode().
                    equals(WhiteLabelConfigurationType.STANDARD_DETAIL))){
                throw new OperationException("WhiteLabelConfigurationType should be "
                        + WhiteLabelConfigurationType.STANDARD_DETAIL);
            }

            if( !StringUtils.isEmpty(paymentInformationResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("PaymentInformationStandardDetail ResourceId:" + paymentInformationResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                PaymentInformationStandardDetail updatePaymentInformation =  null;
                Set<PaymentInformationStandardDetail> paymentInformationStandardDetailSet = existingWhiteLabel.getPaymentInformationStandardDetail();
                if(paymentInformationStandardDetailSet != null) {
                    Optional<PaymentInformationStandardDetail> paymentInformation = paymentInformationStandardDetailSet.stream()
                            .filter(paymentInformationStandardDetailMatch -> paymentInformationStandardDetailMatch.getId()
                                    .equalsIgnoreCase(paymentInformationResource.getId())).findFirst();

                    if(paymentInformation.isPresent()) {
                        updatePaymentInformation = paymentInformation.get();
                        if(file != null) {
                            List<String> filePath = fileSetUpService.uploadMultipleFiles(file,true);
                            updatePaymentInformation.setContent(filePath.get(FILE_CONTENT_INDEX ));
                            updatePaymentInformation.setKeyFile(filePath.get(FILE_PATH_INDEX));
                        }else {
                            CopyUtils.copy(paymentInformationResource, updatePaymentInformation);
                        }
                    }else {
                        throw new OperationException("PaymentInformationId not found for id " + paymentInformationResource.getId());
                    }
                }
                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updatePaymentInformation;
            } else {
                PaymentInformationStandardDetail paymentInformationStandardDetail = new PaymentInformationStandardDetail();
                if(file != null) {
                    List<String> filePath = fileSetUpService.uploadMultipleFiles(file,true);
                    paymentInformationStandardDetail.setContent(filePath.get(FILE_CONTENT_INDEX ));
                    paymentInformationStandardDetail.setKeyFile(filePath.get(FILE_PATH_INDEX));
                }
                else {
                    CopyUtils.copy(paymentInformationResource, paymentInformationStandardDetail);
                }
                paymentInformationStandardDetail.setWhiteLabel(existingWhiteLabel);
                Set<PaymentInformationStandardDetail> oldPaymentInformationSet = new HashSet<>();

                if (existingWhiteLabel.getPaymentInformationStandardDetail() == null) {
                    oldPaymentInformationSet.add(paymentInformationStandardDetail);
                    existingWhiteLabel.setPaymentInformationStandardDetail(oldPaymentInformationSet);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getPaymentInformationStandardDetail().iterator().next();
                } else {
                    oldPaymentInformationSet.addAll(existingWhiteLabel.getPaymentInformationStandardDetail());
                    existingWhiteLabel.getPaymentInformationStandardDetail().add(paymentInformationStandardDetail);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);

                    Set<PaymentInformationStandardDetail> newPaymentInformationSet = new HashSet<>();
                    newPaymentInformationSet.addAll(existingWhiteLabel.getPaymentInformationStandardDetail());
                    newPaymentInformationSet.removeAll(oldPaymentInformationSet);
                    return newPaymentInformationSet.iterator().next();
                }
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
