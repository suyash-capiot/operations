package com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateBuilder {

    @Value("${communication.email.process}")
    private String process;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    public TemplateInfo build(String function, String scenario) {
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setIsActive(true);
        templateInfo.setGroupOfCompanies("");
        templateInfo.setGroupCompany("");
        templateInfo.setCompanyName("");
        templateInfo.setSubBusinessUnit("");
        templateInfo.setMarket("");
        templateInfo.setSource("");
        templateInfo.setProductCategory("");
        templateInfo.setProductCategorySubType("");
        templateInfo.setProcess(process);
        templateInfo.setFunction(function);
        templateInfo.setScenario(scenario);
        templateInfo.setRule1("");
        templateInfo.setRule2("");
        templateInfo.setRule3("");
        templateInfo.setCommunicationType("");
        templateInfo.setCommunicateTo("");
        templateInfo.setIncomingCommunicationType("");
        templateInfo.setDestination("");
        templateInfo.setBrochure("");
        templateInfo.setTour("");
        return templateInfo;
    }

    public TemplateInfo build(String function, String scenario, String bookId, String orderId) throws OperationException {

        OpsBooking opsBooking = opsBookingService.getBooking(bookId);
        List<OpsProduct> opsProducts = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderID().equals(orderId))
                .collect(Collectors.toList());
        if (opsProducts.size() == 0) {
            throw new OperationException("No Order found for Order Id" + orderId);
        }
        String clientName = "";
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            clientName = clientMasterDataService.getB2BClientNames(Arrays.asList(opsBooking.getClientID())).get(0);
        } else if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            clientName = clientMasterDataService.getB2CClientNames(Arrays.asList(opsBooking.getClientID())).get(0);
        }

        OpsProduct opsProduct = opsProducts.get(0);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setIsActive(true);

        //TODO: Decide whether to set these or not, Not in MDM Screen.
        templateInfo.setGroupOfCompanies(opsBooking.getGroupOfCompaniesID());
        templateInfo.setGroupCompany(opsBooking.getGroupCompanyID());

        templateInfo.setCompanyName(opsBooking.getCompanyId());
        templateInfo.setBusinessUnit(opsBooking.getBu());
        templateInfo.setSubBusinessUnit(opsBooking.getSbu());
        templateInfo.setMarket(opsBooking.getClientMarket());

        templateInfo.setSupplier(opsProduct.getSupplierID());
        templateInfo.setOffice("");
        templateInfo.setProductCategory(opsProduct.getProductCategory());
        templateInfo.setProductCategorySubType(opsProduct.getProductSubCategory());

        //TODO: find out how to set these Values
        templateInfo.setSource("");
        templateInfo.setClientType(opsBooking.getClientType());
        templateInfo.setClientGroup("");
        templateInfo.setClientName(clientName);

        templateInfo.setProcess(process);
        templateInfo.setFunction(function);
        templateInfo.setScenario(scenario);
        templateInfo.setRule1("");
        templateInfo.setRule2("");
        templateInfo.setRule3("");

        templateInfo.setCommunicationType("");
        templateInfo.setCommunicateTo("");
        templateInfo.setIncomingCommunicationType("");

        //TODO: Decide whether to set these or not, Not in MDM Screen.
        templateInfo.setDestination("");
        templateInfo.setBrochure("");
        templateInfo.setTour("");

        return templateInfo;
    }

}
