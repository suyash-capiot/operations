package com.coxandkings.travel.operations.model.whitelabel;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name= "white_label")
public class WhiteLabel extends BaseModel{

//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;

    @Column(name = "client_Market_id")
    private String clientMarketId;

    @Column(name = "point_of_sales")
    private String pointOfSales;


    @Column(name = "domain_url")
    private String domainUrl;

    @ManyToOne
    @JoinColumn(name = "white_label_enum_handler_id")
    private ConfigurationTypeEnumHandler configurationTypeEnumHandler;

    @Column(name = "template_id")
    private String templateId;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ColorScheme> colorScheme;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HeaderDetail> headerDetail;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FooterDetail> footerDetail;

    @Column(name = "logo_id")
    private String logoId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "white_label_language", joinColumns = @JoinColumn(name = "white_label_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "language_id", referencedColumnName = "id"))
    private Set<Language> language;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "white_label_currency", joinColumns = @JoinColumn(name = "white_label_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "currency_id", referencedColumnName = "id"))
    private Set<Currency> currency;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL )
    private Set<EmailConfiguration> emailConfigurations;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL)
    private Set<TemplateLayoutStandardDetail> templateLayoutStandardDetail;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL)
    private Set<PaymentInformationStandardDetail> paymentInformationStandardDetail;

    @OneToOne(mappedBy = "whiteLabel", cascade = CascadeType.ALL)
    private GeneralInformationStandardDetail generalInformationStandardDetail;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL)
    private Set<ClientLoginParameter> clientLoginParameters;

    @OneToMany(mappedBy = "whiteLabel", cascade = CascadeType.ALL)
    private Set<MarketingSetupStandardDetail> marketingSetupStandardDetails;

    @OneToOne(mappedBy = "whiteLabel", cascade = CascadeType.ALL)
    private CustomWhiteLabel customWhiteLabel;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getClientMarketId() {
        return clientMarketId;
    }

    public void setClientMarketId(String clientMarketId) {
        this.clientMarketId = clientMarketId;
    }

    public String getPointOfSales() {
        return pointOfSales;
    }

    public void setPointOfSales(String pointOfSales) {
        this.pointOfSales = pointOfSales;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public ConfigurationTypeEnumHandler getConfigurationTypeEnumHandler() {
        return configurationTypeEnumHandler;
    }

    public void setConfigurationTypeEnumHandler(ConfigurationTypeEnumHandler configurationTypeEnumHandler) {
        this.configurationTypeEnumHandler = configurationTypeEnumHandler;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Set<ColorScheme> getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(Set<ColorScheme> colorScheme) {
        this.colorScheme = colorScheme;
    }

    public Set<HeaderDetail> getHeaderDetail() {
        return headerDetail;
    }

    public void setHeaderDetail(Set<HeaderDetail> headerDetail) {
        this.headerDetail = headerDetail;
    }

    public Set<FooterDetail> getFooterDetail() {
        return footerDetail;
    }

    public void setFooterDetail(Set<FooterDetail> footerDetail) {
        this.footerDetail = footerDetail;
    }

    public String getLogoId() {
        return logoId;
    }

    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }

    public Set<Language> getLanguage() {
        return language;
    }

    public void setLanguage(Set<Language> language) {
        this.language = language;
    }

    public Set<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(Set<Currency> currency) {
        this.currency = currency;
    }

    public Set<EmailConfiguration> getEmailConfigurations() {
        return emailConfigurations;
    }

    public void setEmailConfigurations(Set<EmailConfiguration> emailConfigurations) {
        this.emailConfigurations = emailConfigurations;
    }

    public Set<TemplateLayoutStandardDetail> getTemplateLayoutStandardDetail() {
        return templateLayoutStandardDetail;
    }

    public void setTemplateLayoutStandardDetail(Set<TemplateLayoutStandardDetail> templateLayoutStandardDetail) {
        this.templateLayoutStandardDetail = templateLayoutStandardDetail;
    }

    public Set<PaymentInformationStandardDetail> getPaymentInformationStandardDetail() {
        return paymentInformationStandardDetail;
    }

    public void setPaymentInformationStandardDetail(Set<PaymentInformationStandardDetail> paymentInformationStandardDetail) {
        this.paymentInformationStandardDetail = paymentInformationStandardDetail;
    }

    public GeneralInformationStandardDetail getGeneralInformationStandardDetail() {
        return generalInformationStandardDetail;
    }

    public void setGeneralInformationStandardDetail(GeneralInformationStandardDetail generalInformationStandardDetail) {
        this.generalInformationStandardDetail = generalInformationStandardDetail;
    }

    public Set<ClientLoginParameter> getClientLoginParameters() {
        return clientLoginParameters;
    }

    public void setClientLoginParameters(Set<ClientLoginParameter> clientLoginParameters) {
        this.clientLoginParameters = clientLoginParameters;
    }

    public Set<MarketingSetupStandardDetail> getMarketingSetupStandardDetails() {
        return marketingSetupStandardDetails;
    }

    public void setMarketingSetupStandardDetails(Set<MarketingSetupStandardDetail> marketingSetupStandardDetails) {
        this.marketingSetupStandardDetails = marketingSetupStandardDetails;
    }


    public CustomWhiteLabel getCustomWhiteLabel() {
        return customWhiteLabel;
    }

    public void setCustomWhiteLabel(CustomWhiteLabel customWhiteLabel) {
        this.customWhiteLabel = customWhiteLabel;
    }
}