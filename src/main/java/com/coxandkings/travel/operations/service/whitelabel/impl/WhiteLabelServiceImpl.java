package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.criteria.whitelabel.WhiteLabelCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.ConfigurationTypeEnumHandler;
import com.coxandkings.travel.operations.model.whitelabel.Currency;
import com.coxandkings.travel.operations.model.whitelabel.Language;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.LandingPageResponse;
import com.coxandkings.travel.operations.resource.whitelabel.WhiteLabelResource;
import com.coxandkings.travel.operations.service.whitelabel.FileSetupService;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


@Service("WhiteLabelService")
public class WhiteLabelServiceImpl implements WhiteLabelService {

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
    public WhiteLabel createOrUpdateWhiteLabel(WhiteLabelResource whiteLabelResource) throws OperationException{
        WhiteLabel whiteLabel = null;
        try
        {
            if(!StringUtils.isEmpty(whiteLabelResource.getId())) {
                if(log.isDebugEnabled()) {
                    log.debug("White label Id:" + whiteLabelResource.getId());
                }

                WhiteLabel existingWhiteLabel = whiteLabelRepository.getWhiteLabelById(whiteLabelResource.getId());

                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabel Details:" + existingWhiteLabel);
                }
                if(existingWhiteLabel == null) {
                    throw new OperationException("Driver not found with id" + whiteLabelResource.getId());
                }
                CopyUtils.copy(whiteLabelResource, existingWhiteLabel);
                whiteLabel = existingWhiteLabel;
            } else {
                whiteLabel = new WhiteLabel();
                //TODO : Checking Duplicate but nothing mentioned in doc
                CopyUtils.copy(whiteLabelResource, whiteLabel);
            }
            validateWhiteLabelResource(whiteLabelResource, whiteLabel);
            return whiteLabelRepository.saveOrUpdate(whiteLabel);
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<LandingPageResponse> getAllWhiteLabels() throws OperationException{
        List<WhiteLabel> whiteLabelRes =  whiteLabelRepository.getAllWhiteLabels();
        List<LandingPageResponse> landingPageResponses = new ArrayList<>();

        for(WhiteLabel whiteLabel:whiteLabelRes){
            LandingPageResponse res = new LandingPageResponse();
            res.setId(whiteLabel.getId());
            res.setClientMarketId(whiteLabel.getClientMarketId());
            res.setConfigurationTypeEnumHandler(whiteLabel.getConfigurationTypeEnumHandler());
            res.setLanguage(whiteLabel.getLanguage());
            res.setPointOfSale(whiteLabel.getPointOfSales());
            landingPageResponses.add(res);
        }

        return landingPageResponses;
    }

    @Override
    public void deleteWhiteLabel(String id) {
        whiteLabelRepository.deleteById(id);
    }

    @Override
    public List<WhiteLabel> sortByCriteria(WhiteLabelCriteria criteria) {
        return whiteLabelRepository.getWhiteLabelsByCriteria(criteria);
    }

    @Override
    public WhiteLabel getWhiteLabelId(String id) {
        return  whiteLabelRepository.getWhiteLabelById(id);
    }

    public void validateWhiteLabelResource(WhiteLabelResource whiteLabelResource, WhiteLabel whiteLabel){

        Set<Language> languageSet = whiteLabel.getLanguage() != null ? whiteLabel.getLanguage()  : new HashSet<>();;
        Set<String> languageIds  = whiteLabelResource.getLanguageIds();

        if (languageIds != null) {
            for (String languageId : languageIds) {
                Optional<Language> language = languageSet.stream().filter(languageMatch -> languageMatch.getLanguageId()
                        .equalsIgnoreCase(languageId)).findFirst();
                if(!(language.isPresent())){
                    Language lang = new Language();
                    lang.setLanguageId(languageId);
                    languageSet.add(lang);
                }
            }
            whiteLabel.setLanguage(languageSet);
        }

        Set<Currency> currencySet = whiteLabel.getCurrency() !=  null ? whiteLabel.getCurrency() : new HashSet<>();
        Set<String> currencyIds  = whiteLabelResource.getLanguageIds();
        if (currencyIds != null) {
            for (String currencyId : currencyIds) {

                Optional<Currency> currency = currencySet.stream().filter(currencyMatch -> currencyMatch.getCurrencyId()
                        .equalsIgnoreCase(currencyId)).findFirst();

                //TODO : Verify Implementation Logic
                if(!(currency.isPresent())){
                    Currency curr = new Currency();
                    curr.setCurrencyId(currencyId);
                    currencySet.add(curr);
                }
            }
            whiteLabel.setCurrency(currencySet);
        }

        String enumId = whiteLabelResource.getWhiteLabelEnumHandlerId();
        ConfigurationTypeEnumHandler configType = configurationTypeRepository.getWhiteLabelById(enumId);
        whiteLabel.setConfigurationTypeEnumHandler(configType);
    }
}
