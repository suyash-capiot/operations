package com.coxandkings.travel.operations.controller.whitelable;

import com.coxandkings.travel.operations.criteria.whitelabel.WhiteLabelCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.*;
import com.coxandkings.travel.operations.resource.whitelabel.*;
import com.coxandkings.travel.operations.service.whitelabel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/whiteLabels")
@CrossOrigin(origins = "*")
public class WhiteLabelController {

    @Autowired
    private WhiteLabelService whiteLabelService;

    @Autowired
    private WhiteLabelClientLoginParamsService whiteLabelClientLoginParamsService;

    @Autowired
    private WhiteLabelColorSchemeService whiteLabelColorSchemeService;

    @Autowired
    private WhiteLabelCustomService whiteLabelCustomService;

    @Autowired
    private WhiteLabelEmailConfigurationService whiteLabelEmailConfigurationService;

    @Autowired
    private WhiteLabelGeneralInformationService whiteLabelGeneralInformationService;

    @Autowired
    private WhiteLabelHeaderFooterService whiteLabelHeaderFooterService;

    @Autowired
    private WhiteLabelMarketingSetupService whiteLabelMarketingSetupService;

    @Autowired
    private WhiteLabelPaymentInformationService whiteLabelPaymentInformationService;

    @Autowired
    private WhiteLabelSubTemplateLayoutService whiteLabelSubTemplateLayoutService;

    @Autowired
    private WhiteLabelTemplateService whiteLabelTemplateService;



    @RequestMapping(value="/",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> createWhiteLabel(@RequestBody WhiteLabelResource whiteLabelResource)
            throws OperationException {
        WhiteLabel whiteLabel = whiteLabelService.createOrUpdateWhiteLabel(whiteLabelResource);
        return new ResponseEntity<WhiteLabel>(whiteLabel, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET )
    public ResponseEntity<WhiteLabel> getWhiteLabel(@PathVariable("id") String id) throws OperationException  {
        WhiteLabel whiteLabel = whiteLabelService.getWhiteLabelId(id);
        return new ResponseEntity<WhiteLabel>(whiteLabel, HttpStatus.OK);
    }

    @RequestMapping(value="/landingPage",method= RequestMethod.GET)
    public ResponseEntity<List<LandingPageResponse>> getLandingPage() throws OperationException
    {
        //TODO : Finalize the attributes required by UI
        List<LandingPageResponse> landingPageResponses = whiteLabelService.getAllWhiteLabels();
        return new ResponseEntity<List<LandingPageResponse>>(landingPageResponses, HttpStatus.CREATED);
    }

    @PostMapping(path = "/landingPage/sort")
    public ResponseEntity<List<WhiteLabel>> sortWhiteLabelByCriteria(
            @RequestBody WhiteLabelCriteria whiteLabelCriteria) {
        List<WhiteLabel> whiteLabelList = whiteLabelService.sortByCriteria(whiteLabelCriteria);
        return new ResponseEntity<List<WhiteLabel>>(whiteLabelList , HttpStatus.OK);
    }

    @RequestMapping(value="/addOrUpdateColorScheme",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<ColorScheme> createColorScheme(
            @RequestBody ColorSchemeResource colorSchemeResource) throws OperationException  {

        ColorScheme colorScheme = whiteLabelColorSchemeService.createOrUpdateColorScheme(colorSchemeResource);
        return new ResponseEntity<ColorScheme>(colorScheme, HttpStatus.CREATED);
    }


    @RequestMapping(value="/addOrUpdateWhiteLabelTemplate",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> createWhiteLabel(
            @RequestBody WhiteLabelTemplateResource whiteLabelTemplateResource) throws OperationException  {
        WhiteLabel whiteLabel = whiteLabelTemplateService.saveWhiteLabelTemplate(whiteLabelTemplateResource);
        return new ResponseEntity<WhiteLabel>(whiteLabel, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateHeaderDetail",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<HeaderDetail> createHeaderDetail(
            @RequestBody HeaderFooterDetailResource headerFooterDetailResource) throws OperationException  {

        HeaderDetail headerDetail = whiteLabelHeaderFooterService.createOrUpdateHeaderDetail(headerFooterDetailResource,null);
        return new ResponseEntity<HeaderDetail>(headerDetail, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateFooterDetail",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<FooterDetail> createFooterDetail(
            @RequestBody HeaderFooterDetailResource headerFooterDetailResource) throws OperationException  {

        FooterDetail footerDetail = whiteLabelHeaderFooterService.createOrUpdateFooterDetail(headerFooterDetailResource,null);
        return new ResponseEntity<FooterDetail>(footerDetail, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateEmailConfiguration",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<EmailConfiguration> createEmailConfiguration(
            @RequestBody EmailConfigurationResource emailConfigurationResource) throws OperationException  {

        EmailConfiguration emailConfiguration = whiteLabelEmailConfigurationService.createOrUpdateEmailConfiguration(emailConfigurationResource);
        return new ResponseEntity<EmailConfiguration>(emailConfiguration, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdatePaymentInformation",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<PaymentInformationStandardDetail> createPaymentInformation(
            @RequestBody PaymentInformationResource paymentInformationResource) throws OperationException  {

        PaymentInformationStandardDetail paymentInformation =
                whiteLabelPaymentInformationService.createOrUpdatePaymentInformation(paymentInformationResource,null);
        return new ResponseEntity<PaymentInformationStandardDetail>(paymentInformation, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateGeneralInformation",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<GeneralInformationStandardDetail> createGeneralInformation(
            @RequestBody GeneralInformationResource generalInformationResource) throws OperationException  {

        GeneralInformationStandardDetail generalInformation =
                whiteLabelGeneralInformationService.createOrUpdateGeneralInformation(generalInformationResource);
        return new ResponseEntity<GeneralInformationStandardDetail>(generalInformation, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateMarketingSetupParameter", method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<MarketingSetupStandardDetail> createMarketingSetupParameter(
            @RequestBody MarketingSetupParameterResource marketingSetupParameterResource) throws OperationException {

        MarketingSetupStandardDetail marketingSetupStandardDetail =
                whiteLabelMarketingSetupService.createOrUpdateMarketingSetupParameter(marketingSetupParameterResource,null);

        return new ResponseEntity<MarketingSetupStandardDetail>(marketingSetupStandardDetail , HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateClientLoginParameter", method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<ClientLoginParameter> createClientLoginParameter(
            @RequestBody ClientLoginParameterResource clientLoginParameterResource) throws OperationException  {

        ClientLoginParameter clientLoginParameter = whiteLabelClientLoginParamsService.createOrUpdateClientLoginParameter(clientLoginParameterResource);
        return new ResponseEntity<ClientLoginParameter>(clientLoginParameter, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateTemplateLayout", method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<TemplateLayoutStandardDetail> createTemplateLayout(
            @RequestBody TemplateLayoutResource templateLayoutResource) throws OperationException {
        TemplateLayoutStandardDetail templateLayoutStandardDetail =
                whiteLabelSubTemplateLayoutService.createOrUpdateTemplateLayout(templateLayoutResource);
        return new ResponseEntity<TemplateLayoutStandardDetail>(templateLayoutStandardDetail, HttpStatus.CREATED);
    }

    @RequestMapping(value="/addOrUpdateCustomWhiteLabel", method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<CustomWhiteLabel> createCustomWhiteLabel(
            @RequestBody CustomWhiteLabelResource customWhiteLabelResource) throws OperationException  {
       CustomWhiteLabel customWhiteLabel =
                whiteLabelCustomService.createOrUpdateCustomWhiteLabel(customWhiteLabelResource);
        return new ResponseEntity<CustomWhiteLabel>(customWhiteLabel, HttpStatus.CREATED);
    }

    //======All deletes ===== //

    @DeleteMapping("/deleteWhiteLabel/{id}")
    public void delete(@PathVariable(name = "id") String id) {
        whiteLabelService.deleteWhiteLabel(id);
    }


    @RequestMapping(value="/deleteColorScheme",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> deleteColorScheme(
            @RequestBody ColorSchemeResource colorSchemeResource) throws OperationException  {

        WhiteLabel updatedWhiteLabel = whiteLabelColorSchemeService.deleteColorScheme(colorSchemeResource);
        return new ResponseEntity<WhiteLabel>(updatedWhiteLabel, HttpStatus.OK);
    }


    @RequestMapping(value="/deleteHeaderDetail",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> deleteHeaderDetail(
            @RequestBody HeaderFooterDetailResource headerFooterDetailResource) throws OperationException  {
        WhiteLabel updatedWhiteLabel = whiteLabelHeaderFooterService.deleteHeaderDetails(headerFooterDetailResource);
        return new ResponseEntity<WhiteLabel>(updatedWhiteLabel, HttpStatus.OK);
    }

    @RequestMapping(value="/deleteFooterDetail",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> deleteFooterDetail(
            @RequestBody HeaderFooterDetailResource headerFooterDetailResource) throws OperationException {
        WhiteLabel updatedWhiteLabel = whiteLabelHeaderFooterService.deleteFooterDetails(headerFooterDetailResource);
        return new ResponseEntity<WhiteLabel>(updatedWhiteLabel, HttpStatus.OK);
    }

    @RequestMapping(value="/deleteMarketingSetup",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> deleteMarketingSetup(
            @RequestBody MarketingSetupParameterResource marketingSetupParameterResource) throws OperationException  {
        WhiteLabel updatedWhiteLabel = whiteLabelMarketingSetupService.deleteMarketingSetup(marketingSetupParameterResource);
        return new ResponseEntity<WhiteLabel>(updatedWhiteLabel, HttpStatus.OK);
    }

    @RequestMapping(value="/deleteTemplateLayout",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> deleteTemplateLayout(
            @RequestBody TemplateLayoutResource templateLayoutResource) throws OperationException  {
        WhiteLabel updatedWhiteLabel = whiteLabelSubTemplateLayoutService.deleteTemplateLayout(templateLayoutResource);
        return new ResponseEntity<WhiteLabel>(updatedWhiteLabel, HttpStatus.OK);
    }


    @RequestMapping(value="/deleteEmailConfiguration",method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<WhiteLabel> deleteEmailConfiguration(
            @RequestBody EmailConfigurationResource emailConfigurationResource) throws OperationException  {

        WhiteLabel updatedWhiteLabel = whiteLabelEmailConfigurationService.deleteEmailConfiguration(emailConfigurationResource);
        return new ResponseEntity<WhiteLabel>(updatedWhiteLabel, HttpStatus.CREATED);
    }

}
