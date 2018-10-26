package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.enums.whitelabel.WhiteLabelConfigurationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.*;
import com.coxandkings.travel.operations.resource.whitelabel.*;
import com.coxandkings.travel.operations.service.whitelabel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(value = SpringRunner.class)
@SpringBootTest
public class WhiteLabelServiceImplTest {

    private static WhiteLabelResource whiteLabelResource = null;
    private static ClientLoginParameterResource clientLoginParameterResource = null;
    private static ColorSchemeResource colorSchemeResource =  null;
    private static CurrencyResource currencyResource = null;
    private static CustomWhiteLabelResource customWhiteLabelResource = null;
    private static EmailConfigurationResource emailConfigurationResource= null;
    private static HeaderFooterDetailResource footerDetailResource = null;
    private static HeaderFooterDetailResource headerDetailResource = null;
    private static GeneralInformationResource generalInformationResource = null;
    private static MarketingSetupParameterResource marketingSetupParameterResource = null;
    private static PaymentInformationResource paymentInformationResource = null;
    private static TemplateLayoutResource templateLayoutResource = null;
    private static WhiteLabelTemplateResource whiteLabelTemplateResource = null;

    private String whiteLabelId = null;

    @Autowired
    WhiteLabelClientLoginParamsServiceImpl whiteLabelClientLoginParamsService;

    @Autowired
    WhiteLabelCustomService whiteLabelCustomService;

    @Autowired
    WhiteLabelEmailConfigurationService whiteLabelEmailConfigurationService;

    @Autowired
    WhiteLabelGeneralInformationService whiteLabelGeneralInformationService;

    @Autowired
    WhiteLabelHeaderFooterService whiteLabelHeaderFooterService;

    @Autowired
    WhiteLabelMarketingSetupService whiteLabelMarketingSetupService;

    @Autowired
    WhiteLabelPaymentInformationService whiteLabelPaymentInformationService;

    @Autowired
    WhiteLabelColorSchemeService whiteLabelColorSchemeService;

    @Autowired
    WhiteLabelService whiteLabelService;

    @Autowired
    WhiteLabelSubTemplateLayoutService whiteLabelSubTemplateLayoutService;

    @Autowired
    WhiteLabelTemplateService whiteLabelTemplateService;


    static {
        whiteLabelResource = new WhiteLabelResource();
        whiteLabelResource.setClientMarketId("1");
        whiteLabelResource.setPointOfSales("India");
        whiteLabelResource.setDomainUrl("www.eezego1.com");
        whiteLabelResource.setWhiteLabelEnumHandlerId("3");

        clientLoginParameterResource = new ClientLoginParameterResource();
        clientLoginParameterResource.setLoginParameter("test");

        colorSchemeResource = new ColorSchemeResource();
        colorSchemeResource.setColorId("1");
        colorSchemeResource.setCssElementId("2");

        customWhiteLabelResource = new CustomWhiteLabelResource();
        customWhiteLabelResource.setCustomText("Test Custom WhiteLabel");


        emailConfigurationResource = new EmailConfigurationResource();
        emailConfigurationResource.setEmailFrom("shivam@coxandKings.com");
        emailConfigurationResource.setEmailToBCC("shivam@coxandKings.com");
        emailConfigurationResource.setEmailToCC("shivam@coxandKings.com");
        emailConfigurationResource.setFunctionId("had");
        emailConfigurationResource.setServerSettingId("email.smpt.2000");

        headerDetailResource =  new HeaderFooterDetailResource();
        headerDetailResource.setContent("header");
        headerDetailResource.setElementId("12");
        headerDetailResource.setCreatedByUserId("21");
        headerDetailResource.setFilePath("aa/bb/");

        footerDetailResource =  new HeaderFooterDetailResource();
        footerDetailResource.setContent("hello");
        footerDetailResource.setElementId("1");
        footerDetailResource.setCreatedByUserId("shivam");
        footerDetailResource.setFilePath("Test");

        generalInformationResource =  new GeneralInformationResource();
        generalInformationResource.setEnableComboProducts(true);
        generalInformationResource.setEnableCrossSell(false);
        generalInformationResource.setEnableUpSell(true);
        generalInformationResource.setEnableProductDisplaySorting(false);
        generalInformationResource.setShoppingCartRequired(true);

        marketingSetupParameterResource = new MarketingSetupParameterResource();
        marketingSetupParameterResource.setContent("Content Marketing SetUp");
        marketingSetupParameterResource.setFilePath("");
        marketingSetupParameterResource.setMarketingSetUpName("MarketSetUp");

        paymentInformationResource =  new PaymentInformationResource();
        paymentInformationResource.setContent("ABC");
        paymentInformationResource.setBankId("BANK34IND");
        paymentInformationResource.setKeyFile("KeyFile");
        paymentInformationResource.setMidName("MidName");
        paymentInformationResource.setModeOfPayment("NetBanking");

        templateLayoutResource =  new TemplateLayoutResource();
        templateLayoutResource.setTemplateLayoutId("12");
        templateLayoutResource.setTemplateTypeId("2");

        whiteLabelTemplateResource = new WhiteLabelTemplateResource();
        whiteLabelTemplateResource.setTemplateId("1");
    }


    @Test
    @Transactional
    public void createOrUpdateWhiteLabel() throws IOException, OperationException {
        WhiteLabel whiteLabel = whiteLabelService.createOrUpdateWhiteLabel(whiteLabelResource);
        whiteLabelId = whiteLabel.getId();
        assertNotNull(whiteLabel.getId());
        assertEquals(whiteLabel.getClientMarketId(), whiteLabelResource.getClientMarketId());
        assertEquals(whiteLabel.getPointOfSales(), whiteLabelResource.getPointOfSales());
        assertEquals(whiteLabel.getDomainUrl(), whiteLabelResource.getDomainUrl());
        assertEquals(whiteLabel.getConfigurationTypeEnumHandler().getCode(), WhiteLabelConfigurationType.STANDARD_DETAIL);
    }

    @Test
    @Transactional
    public void saveWhiteLabelTemplate() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        whiteLabelTemplateResource.setWhiteLabelId(whiteLabelId);
        WhiteLabel whiteLabelRes = whiteLabelTemplateService.saveWhiteLabelTemplate(whiteLabelTemplateResource);
        assertEquals(whiteLabelRes.getTemplateId(), whiteLabelTemplateResource.getTemplateId());
        assertEquals(whiteLabelRes.getId(), whiteLabelTemplateResource.getWhiteLabelId());
    }

    @Test
    @Transactional
    public void createOrUpdateHeaderFooterDetail() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        headerDetailResource.setWhiteLabelId(whiteLabelId);

        HeaderDetail headerDetailRes =
                whiteLabelHeaderFooterService.createOrUpdateHeaderDetail(headerDetailResource,null);

        assertNotNull(headerDetailRes.getId());
        assertEquals(headerDetailRes.getContent(), headerDetailResource.getContent());
        assertEquals(headerDetailRes.getElementId(), headerDetailResource.getElementId());
        assertEquals(headerDetailRes.getCreatedByUserId(), headerDetailResource.getCreatedByUserId());

        footerDetailResource.setWhiteLabelId(whiteLabelId);
        FooterDetail footerDetailRes = whiteLabelHeaderFooterService.createOrUpdateFooterDetail(footerDetailResource,null);
        assertNotNull(footerDetailRes.getId());
        assertEquals(footerDetailRes.getContent(), footerDetailResource.getContent());
        assertEquals(footerDetailRes.getElementId(), footerDetailResource.getElementId());
        assertEquals(footerDetailRes.getCreatedByUserId(), footerDetailResource.getCreatedByUserId());

    }

    @Test
    @Transactional
    public void createOrUpdateEmailConfiguration() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        emailConfigurationResource.setWhiteLabelId(whiteLabelId);

        EmailConfiguration emailConfigurationRes = whiteLabelEmailConfigurationService.
                createOrUpdateEmailConfiguration(emailConfigurationResource);

        assertNotNull(emailConfigurationRes.getId());
        assertEquals(emailConfigurationRes.getEmailFrom(),emailConfigurationResource.getEmailFrom());
        assertEquals(emailConfigurationRes.getEmailToBCC(),emailConfigurationResource.getEmailToBCC());
        assertEquals(emailConfigurationRes.getEmailToCC(),emailConfigurationResource.getEmailToCC());
        assertEquals(emailConfigurationRes.getFunctionId(),emailConfigurationResource.getFunctionId());
        assertEquals(emailConfigurationRes.getServerSettingId(),emailConfigurationResource.getServerSettingId());
    }

    @Test
    @Transactional
    public void createOrUpdatePaymentInformation() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        paymentInformationResource.setWhiteLabelId(whiteLabelId);
        PaymentInformationStandardDetail paymentInformationRes = whiteLabelPaymentInformationService.
                createOrUpdatePaymentInformation(paymentInformationResource,null);

        assertNotNull(paymentInformationRes.getId());
        assertEquals(paymentInformationRes.getBankId() ,  paymentInformationResource.getBankId());
        assertEquals(paymentInformationRes.getContent() ,  paymentInformationResource.getContent());
        assertEquals(paymentInformationRes.getKeyFile() ,  paymentInformationResource.getKeyFile());
        assertEquals(paymentInformationRes.getMidName() ,  paymentInformationResource.getMidName());
    }


    @Test
    @Transactional
    public void createOrUpdateGeneralInformation() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        generalInformationResource.setWhiteLabelId(whiteLabelId);
        GeneralInformationStandardDetail generalInformationRes = whiteLabelGeneralInformationService.
                createOrUpdateGeneralInformation(generalInformationResource);

        assertNotNull(generalInformationRes.getId());
        assertEquals(generalInformationRes.getEnableComboProducts(),
                generalInformationResource.getEnableComboProducts());

        assertEquals(generalInformationRes.getEnableCrossSell(),
                generalInformationResource.getEnableCrossSell());

        assertEquals(generalInformationRes.getEnableUpSell(),
                generalInformationResource.getEnableUpSell());

        assertEquals(generalInformationRes.getShoppingCartRequired(),
                generalInformationResource.getShoppingCartRequired());

        assertEquals(generalInformationRes.getEnableProductDisplaySorting(),
                generalInformationResource.getEnableProductDisplaySorting());
    }

    @Test
    @Transactional
    public void createOrUpdateMarketingSetupParameter() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        marketingSetupParameterResource.setWhiteLabelId(whiteLabelId);

        MarketingSetupStandardDetail marketingSetupRes = whiteLabelMarketingSetupService.
                createOrUpdateMarketingSetupParameter(marketingSetupParameterResource,null);

        assertNotNull(marketingSetupRes.getId());
        assertEquals(marketingSetupRes.getContent(), marketingSetupParameterResource.getContent());
        assertEquals(marketingSetupRes.getMarketingSetUpName(), marketingSetupParameterResource.getMarketingSetUpName());
    }

    @Test
    @Transactional
    public void createOrUpdateClientLoginParameter() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        clientLoginParameterResource.setWhiteLabelId(whiteLabelId);
        ClientLoginParameter clientLoginParameterRes =
                whiteLabelClientLoginParamsService.createOrUpdateClientLoginParameter(clientLoginParameterResource);

        assertNotNull(clientLoginParameterRes.getId());
        assertEquals(clientLoginParameterRes.getLoginParameter(), clientLoginParameterResource.getLoginParameter());
    }

    @Test
    @Transactional
    public void createOrUpdateColorScheme() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        colorSchemeResource.setWhiteLabelId(whiteLabelId);
        ColorScheme colorSchemeRes = whiteLabelColorSchemeService.createOrUpdateColorScheme(colorSchemeResource);

        assertNotNull(colorSchemeRes.getId());
        assertEquals(colorSchemeRes.getColorId(), colorSchemeResource.getColorId());
        assertEquals(colorSchemeRes.getCssElementId(), colorSchemeResource.getCssElementId());

    }

    @Test
    @Transactional
    public void createOrUpdateTemplateLayout() throws IOException, OperationException {
        createOrUpdateWhiteLabel();
        templateLayoutResource.setWhiteLabelId(whiteLabelId);
        TemplateLayoutStandardDetail templateLayoutStandardDetailRes =
                whiteLabelSubTemplateLayoutService.createOrUpdateTemplateLayout(templateLayoutResource);

        assertNotNull(templateLayoutStandardDetailRes.getId());
        assertEquals(templateLayoutStandardDetailRes.getTemplateLayoutId(),
                templateLayoutStandardDetailRes.getTemplateLayoutId());
        assertEquals(templateLayoutStandardDetailRes.getTemplateTypeId(),
                templateLayoutStandardDetailRes.getTemplateTypeId());
    }


    @Test
    @Transactional
    public void getWhiteLabelId() throws IOException, OperationException {
        saveWhiteLabelTemplate();
        createOrUpdateHeaderFooterDetail();
        createOrUpdateEmailConfiguration();
        createOrUpdatePaymentInformation();
        createOrUpdateGeneralInformation();
        createOrUpdateMarketingSetupParameter();
        createOrUpdateClientLoginParameter();
        createOrUpdateColorScheme();
        createOrUpdateTemplateLayout();

        WhiteLabel whiteLabelRes = whiteLabelService.getWhiteLabelId(whiteLabelId);
        assertNotNull(whiteLabelRes.getId());

    }

}