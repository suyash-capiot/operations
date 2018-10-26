package com.coxandkings.travel.operations.model.template.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isActive",
        "groupOfCompanies",
        "groupCompany",
        "companyName",
        "businessUnit",
        "subBusinessUnit",
        "market",
        "office",
        "source",
        "productCategory",
        "productCategorySubType",
        "supplier",
        "clientType",
        "clientGroup",
        "clientName",
        "process",
        "function",
        "scenario",
        "rule1",
        "rule2",
        "rule3",
        "communicationType",
        "communicateTo",
        "incomingCommunicationType",
        "destination",
        "brochure",
        "tour",
})
public class TemplateInfo implements Serializable {

    private static final long serialVersionUID = 8799061430179743580L;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("groupOfCompanies")
    private String groupOfCompanies;
    @JsonProperty("groupCompany")
    private String groupCompany;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("businessUnit")
    private String businessUnit;
    @JsonProperty("subBusinessUnit")
    private String subBusinessUnit;
    @JsonProperty("market")
    private String market;
    @JsonProperty("office")
    private String office;
    @JsonProperty("source")
    private String source;
    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productCategorySubType")
    private String productCategorySubType;
    @JsonProperty("supplier")
    private String supplier;
    @JsonProperty("clientType")
    private String clientType;
    @JsonProperty("clientGroup")
    private String clientGroup;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("process")
    private String process;
    @JsonProperty("function")
    private String function;
    @JsonProperty("scenario")
    private String scenario;
    @JsonProperty("rule1")
    private String rule1;
    @JsonProperty("rule2")
    private String rule2;
    @JsonProperty("rule3")
    private String rule3;
    @JsonProperty("communicationType")
    private String communicationType;
    @JsonProperty("communicateTo")
    private String communicateTo;
    @JsonProperty("incomingCommunicationType")
    private String incomingCommunicationType;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("brochure")
    private String brochure;
    @JsonProperty("tour")
    private String tour;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getGroupOfCompanies() {
        return groupOfCompanies;
    }

    public void setGroupOfCompanies(String groupOfCompanies) {
        this.groupOfCompanies = groupOfCompanies;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getSubBusinessUnit() {
        return subBusinessUnit;
    }

    public void setSubBusinessUnit(String subBusinessUnit) {
        this.subBusinessUnit = subBusinessUnit;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }


    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getRule1() {
        return rule1;
    }

    public void setRule1(String rule1) {
        this.rule1 = rule1;
    }

    public String getRule2() {
        return rule2;
    }

    public void setRule2(String rule2) {
        this.rule2 = rule2;
    }

    public String getRule3() {
        return rule3;
    }

    public void setRule3(String rule3) {
        this.rule3 = rule3;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public String getCommunicateTo() {
        return communicateTo;
    }

    public void setCommunicateTo(String communicateTo) {
        this.communicateTo = communicateTo;
    }

    public String getIncomingCommunicationType() {
        return incomingCommunicationType;
    }

    public void setIncomingCommunicationType(String incomingCommunicationType) {
        this.incomingCommunicationType = incomingCommunicationType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBrochure() {
        return brochure;
    }

    public void setBrochure(String brochure) {
        this.brochure = brochure;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "TemplateInfo{" +
                "isActive=" + isActive +
                ", groupOfCompanies='" + groupOfCompanies + '\'' +
                ", groupCompany='" + groupCompany + '\'' +
                ", companyName='" + companyName + '\'' +
                ", businessUnit='" + businessUnit + '\'' +
                ", subBusinessUnit='" + subBusinessUnit + '\'' +
                ", market='" + market + '\'' +
                ", office='" + office + '\'' +
                ", source='" + source + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productCategorySubType='" + productCategorySubType + '\'' +
                ", supplier='" + supplier + '\'' +
                ", clientType='" + clientType + '\'' +
                ", clientGroup='" + clientGroup + '\'' +
                ", clientName='" + clientName + '\'' +
                ", process='" + process + '\'' +
                ", function='" + function + '\'' +
                ", scenario='" + scenario + '\'' +
                ", rule1='" + rule1 + '\'' +
                ", rule2='" + rule2 + '\'' +
                ", rule3='" + rule3 + '\'' +
                ", communicationType='" + communicationType + '\'' +
                ", communicateTo='" + communicateTo + '\'' +
                ", incomingCommunicationType='" + incomingCommunicationType + '\'' +
                ", destination='" + destination + '\'' +
                ", brochure='" + brochure + '\'' +
                ", tour='" + tour + '\'' +
                '}';
    }
}