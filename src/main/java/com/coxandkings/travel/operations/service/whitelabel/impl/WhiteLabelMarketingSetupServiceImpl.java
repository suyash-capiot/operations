package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.MarketingSetupStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.MarketingSetupParameterResource;
import com.coxandkings.travel.operations.service.whitelabel.FileSetupService;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelMarketingSetupService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("WhiteLabelMarketingSetup")
public class WhiteLabelMarketingSetupServiceImpl implements WhiteLabelMarketingSetupService {

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
    public MarketingSetupStandardDetail createOrUpdateMarketingSetupParameter(
            MarketingSetupParameterResource marketingSetupParameterResource, MultipartFile file) throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = marketingSetupParameterResource.getWhiteLabelId();
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



            if( !StringUtils.isEmpty(marketingSetupParameterResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("MarketingSetupStandardDetail ResourceId:" + marketingSetupParameterResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                MarketingSetupStandardDetail updateMarketingSetup = null;
                Set<MarketingSetupStandardDetail> marketingSetupStandardDetails = existingWhiteLabel.getMarketingSetupStandardDetails();
                if(marketingSetupStandardDetails != null) {
                    Optional<MarketingSetupStandardDetail> marketingSetupParameter = marketingSetupStandardDetails.stream()
                            .filter(marketingSetupStandardDetailMatch -> marketingSetupStandardDetailMatch.getId()
                                    .equalsIgnoreCase(marketingSetupParameterResource.getId())).findFirst();
                    if(marketingSetupParameter.isPresent()) {
                        updateMarketingSetup = marketingSetupParameter.get();
                        if(file != null) {
                            List<String> filePath = fileSetUpService.uploadMultipleFiles(file,true);
                            updateMarketingSetup.setContent(filePath.get(FILE_CONTENT_INDEX));
                            updateMarketingSetup.setFilePath(filePath.get(FILE_PATH_INDEX));
                        }else {
                            CopyUtils.copy(marketingSetupParameterResource, updateMarketingSetup);
                        }
                    }else {
                        throw new OperationException("MarketingSetupParameter not found for id" + marketingSetupParameterResource.getId());
                    }
                }

                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateMarketingSetup;

            } else {
                MarketingSetupStandardDetail marketingSetupStandardDetail = new MarketingSetupStandardDetail();
                if(file != null) {
                    List<String> filePath = fileSetUpService.uploadMultipleFiles(file,true);
                    marketingSetupStandardDetail.setContent(filePath.get(FILE_CONTENT_INDEX));
                    marketingSetupStandardDetail.setFilePath(filePath.get(FILE_PATH_INDEX));
                }else {
                    CopyUtils.copy(marketingSetupParameterResource, marketingSetupStandardDetail);
                }
                marketingSetupStandardDetail.setWhiteLabel(existingWhiteLabel);
                Set<MarketingSetupStandardDetail> oldMarketingSetupSet = new HashSet<>();

                if(existingWhiteLabel.getMarketingSetupStandardDetails() == null) {
                    oldMarketingSetupSet.add(marketingSetupStandardDetail);
                    existingWhiteLabel.setMarketingSetupStandardDetails(oldMarketingSetupSet);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getMarketingSetupStandardDetails().iterator().next();

                } else {

                    oldMarketingSetupSet.addAll(existingWhiteLabel.getMarketingSetupStandardDetails());
                    existingWhiteLabel.getMarketingSetupStandardDetails().add(marketingSetupStandardDetail);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    Set<MarketingSetupStandardDetail> newMarketingSetupSet = new HashSet<>();
                    newMarketingSetupSet.addAll(existingWhiteLabel.getMarketingSetupStandardDetails());
                    newMarketingSetupSet.removeAll(oldMarketingSetupSet);
                    return newMarketingSetupSet.iterator().next();
                }
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public WhiteLabel deleteMarketingSetup(MarketingSetupParameterResource marketingSetupParameterResource)
            throws OperationException {
        WhiteLabel existingWhiteLabel =  null;
        String whiteLabelTemplateId = marketingSetupParameterResource.getWhiteLabelId();
        existingWhiteLabel = whiteLabelTemplateId != null ?
                whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

        try {
            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(marketingSetupParameterResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("MarketingSetupParameter ResourceId:" + marketingSetupParameterResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                MarketingSetupStandardDetail deleteMarketingSetupStandardDetail =  null;
                Set<MarketingSetupStandardDetail> marketingSetupStandardDetailSet = existingWhiteLabel.getMarketingSetupStandardDetails();

                if(marketingSetupStandardDetailSet != null) {
                    Optional<MarketingSetupStandardDetail> marketingSetupStandardDetail = marketingSetupStandardDetailSet.stream()
                            .filter(marketingSetupStandardDetailMatch -> marketingSetupStandardDetailMatch.getId()
                                    .equalsIgnoreCase(marketingSetupParameterResource.getId())).findFirst();

                    if(marketingSetupStandardDetail.isPresent()) {
                        deleteMarketingSetupStandardDetail = marketingSetupStandardDetail.get();
                        marketingSetupStandardDetailSet.remove(deleteMarketingSetupStandardDetail);
                    }else {
                        throw new Exception("Marketing Setup Parameter not found for id "
                                + marketingSetupParameterResource.getId());
                    }
                }

                WhiteLabel whiteLabel = whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return whiteLabel;
            }
            else{
                throw new OperationException("Marketing Setup Parameter is empty for "
                        + marketingSetupParameterResource.getId());
            }

        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
