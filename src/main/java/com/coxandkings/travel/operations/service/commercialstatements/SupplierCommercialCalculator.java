package com.coxandkings.travel.operations.service.commercialstatements;

import com.coxandkings.travel.operations.utils.BRMSConstant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

public class SupplierCommercialCalculator implements BRMSConstant {
    @Value("${commercial-calculation.acco}")
    private String accoSupplierCommercialCalUrl;

    JSONObject calculateSupplierCommercial() {
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonResponse = new JSONObject();
        jsonRequest.put("lookup", "suppliertransactionalsession");
        JSONArray commandsJsonArr = new JSONArray();
        JSONObject insertJson = getInsertJson();
        JSONObject insertObjectJson = new JSONObject();
        insertObjectJson.put("insert", insertJson);
        JSONObject fire_all_rules = new JSONObject();
        fire_all_rules.put("fire-all-rules", new JSONObject());
        commandsJsonArr.put(insertObjectJson);
        commandsJsonArr.put(fire_all_rules);
        jsonRequest.put("commands", commandsJsonArr);

        return jsonRequest;
    }

    private JSONObject getInsertJson() {
        JSONObject insertJson = new JSONObject();
        insertJson.put("out-identifier", "root");
        insertJson.put("return-object", true);
        insertJson.put("entry-point", "DEFAULT");
        insertJson.put("object", getObjectJson());
        return insertJson;
    }

    private JSONObject getObjectJson() {
        JSONObject jsonObject = new JSONObject();

        //jsonObject.put("transactional", false);
        jsonObject.put("businessRuleIntake", getBusinessRuleInTake());
        jsonObject.put("header", new JSONObject().put("operationName", "Search"));
        return new JSONObject().put("cnk.acco_commercialscalculationengine.suppliertransactionalrules.Root", jsonObject);
    }

    public JSONArray getBusinessRuleInTake() {

        JSONObject businessRuleIntake = new JSONObject();
        businessRuleIntake.put(PENALTY_FEE, getPenaltyFeeJson(new JSONObject()));
        businessRuleIntake.put(MAINTENANCE_FEE, getMsfFeeJson(new JSONObject()));
        businessRuleIntake.put(ADVANCED_DEFINITION, getAdvancedDefinitionJson(new JSONObject()));
        businessRuleIntake.put(MAINTENANCE_FEE, getMaintenanceFeeJson(new JSONObject()));
        businessRuleIntake.put(INTEGRATION_FEE, getOtherFees(new JSONObject()));// todo unimplemented
        businessRuleIntake.put(PREFERENCE_BENEFIT, getPreferenceBenefitJson(new JSONObject()));
        businessRuleIntake.put(LOYALTY_BONUS, getOtherFees(new JSONObject())); // todo unimplemented
        businessRuleIntake.put(WEBSERVICE_FEE, getOtherFees(new JSONObject()));// todo unimplemented
        businessRuleIntake.put(LICENCE_FEE, getOtherFees(new JSONObject()));
        businessRuleIntake.put(RETAINER_FEE, getOtherFees(new JSONObject()));
        businessRuleIntake.put(LISTING_FEE, getOtherFees(new JSONObject()));
        businessRuleIntake.put(TERMINATION_FEE, getTerminationFeeJson(new JSONObject()));
        businessRuleIntake.put(SIGN_UP_FEE, getOtherFees(new JSONObject()));
        businessRuleIntake.put(CONTENT_ACCESS_FEE, getOtherFees(new JSONObject()));
        businessRuleIntake.put(TRAINING_FEE, getOtherFees(new JSONObject()));
        businessRuleIntake.put(SIGN_UP_BONUS, getSignUpBonusJson(new JSONObject()));
        businessRuleIntake.put(LOOK_TO_BOOK, getLookToBookJson(new JSONObject()));
        businessRuleIntake.put(REMITTANCE_FEE, getRemittanceFeeJson(new JSONObject()));
        businessRuleIntake.put(COMMON_ELEMENTS, getCommonElements(new JSONObject()));
        businessRuleIntake.put(MSF_FEE, getMsfFeeJson(new JSONObject()));
        businessRuleIntake.put(INCENTIVE_ON_TOP_UP, getIncentiveOnTopUp(new JSONObject()));
        return new JSONArray().put(businessRuleIntake);
    }

    private JSONObject getIncentiveOnTopUp(JSONObject jsonObject) {
        JSONObject incentiveOnTopUp = new JSONObject();
        incentiveOnTopUp.put("topUpDateTime", "2017-09-25T16:10:00");
        incentiveOnTopUp.put("modeOfPayment", "CreditCard");
        incentiveOnTopUp.put("bankName", "Saraswat Bank");
        incentiveOnTopUp.put("incentiveCurrency", "INR");
        incentiveOnTopUp.put("incentiveAmount", "100000");
        incentiveOnTopUp.put("incentiveRateType", "RT01");
        incentiveOnTopUp.put("incentiveRateType", "RT01");
        incentiveOnTopUp.put("incentiveRateCode", "RC01");
        return incentiveOnTopUp;
    }

    private JSONObject getCommonElements(JSONObject jsonObject) {

        JSONObject commonElement = new JSONObject();
        commonElement.put("clientType", "B2C");
        commonElement.put("contractValidity", "2018-05-31T00:00:00");
        commonElement.put("productCategorySubType", "Hotel");
        commonElement.put("supplier", "SUPP100127");
        commonElement.put("supplierMarket", "India");

        return commonElement;
    }

    private JSONObject getLicenceFeeJson(JSONObject jsonObject) {
        return jsonObject;
    }

    private JSONObject getWebServiceFeeJson(JSONObject jsonObject) {
        return jsonObject;
    }

    private JSONObject getLoyaltyBonusJson(JSONObject jsonObject) {
        return jsonObject;
    }

    private JSONObject getIntegrationFeeJson(JSONObject jsonObject) {
        return jsonObject;
    }

    public JSONArray getbusinessRuleIntake() {
        JSONObject businessRuleIntake = new JSONObject();
        JSONObject commonElementsJson = new JSONObject();

        commonElementsJson.put("contractValidity", "2018-05-31T00:00:00");
        commonElementsJson.put("clientType", "B2C");
        commonElementsJson.put("supplier", "SUPP100127");
        commonElementsJson.put("productCategorySubType", "Hotel");
        commonElementsJson.put("supplierMarket", "India");
        businessRuleIntake.put("commonElements", commonElementsJson);

        JSONArray slabDetailsArry = new JSONArray();
        JSONObject slabObjectJson = new JSONObject();
        slabObjectJson.put("slabType", "No of Rooms");
        slabObjectJson.put("slabTypeValue", "8");
        slabDetailsArry.put(slabObjectJson);
        businessRuleIntake.put("slabDetails", slabDetailsArry);

        businessRuleIntake.put("hotelDetails", getHotelDetailsJsonArr());
        businessRuleIntake.put("advancedDefinition", getAdvancedDefinitionJson(new JSONObject()));


        return new JSONArray().put(businessRuleIntake);
    }

    private JSONObject getAdvancedDefinitionJson(JSONObject jsonObject) {
        JSONObject advancedDefinitionJson = new JSONObject();
        advancedDefinitionJson.put("continent", "");
        advancedDefinitionJson.put("country", "");
        advancedDefinitionJson.put("travelCheckOutDate", "2018-06-29T00:00:00");
        advancedDefinitionJson.put("nationality", "Indian");
        advancedDefinitionJson.put("salesDate", "2018-05-31T00:00:00");
        advancedDefinitionJson.put("salesDate", "2018-05-31T00:00:00");
        advancedDefinitionJson.put("bookingType", "Online");
        advancedDefinitionJson.put("credentialsName", "B2C");
        advancedDefinitionJson.put("state", "");
        advancedDefinitionJson.put("travelCheckInDate", "2018-06-27T00:00:00");
        return advancedDefinitionJson;

    }

    private JSONArray getHotelDetailsJsonArr() {
        JSONObject hotelDetail = new JSONObject();
        hotelDetail.put("productBrand", "");
        hotelDetail.put("productChain", "");
        hotelDetail.put("roomDetails", getRoomDetailsArr());
        hotelDetail.put("productName", "");
        JSONArray hotelDetailsArr = new JSONArray();
        hotelDetailsArr.put(hotelDetail);
        return hotelDetailsArr;
    }

    private JSONArray getRoomDetailsArr() {
        JSONArray roomDetailsJsonArr = new JSONArray();
        JSONObject roomDetail = new JSONObject();
        roomDetail.put("rateType", "Room Only");
        roomDetail.put("totalFare", "1032.12");
        roomDetail.put("roomCategory", "Deluxe Front Street Double");
        roomDetail.put("roomType", "Double");
        roomDetail.put("rateCode", "1");
        JSONObject fareBreakUpJson = new JSONObject();
        fareBreakUpJson.put("baseFare", 1032.12);
        fareBreakUpJson.put("taxDetails", getTaxDetailsArr());
        roomDetail.put("fareBreakUp", fareBreakUpJson);
        roomDetailsJsonArr.put(roomDetail);
        return roomDetailsJsonArr;
    }

    private JSONArray getTaxDetailsArr() {
        JSONArray taxDetailsArry = new JSONArray();
        JSONObject taxDetail1 = new JSONObject();
        taxDetail1.put("taxValue", 0);
        taxDetail1.put("taxName", "roomTax");
        taxDetailsArry.put(taxDetail1);
        JSONObject taxDetail2 = new JSONObject();
        taxDetail2.put("taxValue", 0);
        taxDetail2.put("taxName", "salesTax");
        taxDetailsArry.put(taxDetail2);
        return taxDetailsArry;

    }


    public JSONObject getPenaltyFeeJson(JSONObject jsonObject) {
        JSONObject penaltyFreeJson = new JSONObject();
        JSONArray slabDetails = new JSONArray();
        JSONObject slabObject = new JSONObject();
        slabObject.put("slabType", "No of Bookings");
        slabObject.put("slabTypeValue", "2");
        slabDetails.put(slabObject);
        penaltyFreeJson.put("slabDetails", slabDetails);
        return penaltyFreeJson;
    }

    public JSONObject getMsfFeeJson(JSONObject jsonObject) {
        JSONObject msfFeeJson = new JSONObject();
        msfFeeJson.put("transactionType", "Online");
        msfFeeJson.put("msfchargeType", "OnUs");
        msfFeeJson.put("paymentType", "CreditCard");
        msfFeeJson.put("cardType", "Visa");
        msfFeeJson.put("totalFare", "3800");


        return msfFeeJson;
    }

    public JSONObject getMaintenanceFeeJson(JSONObject jsonObject) {
        JSONObject maintenanceFeeJson = new JSONObject();
        JSONObject otherFeeJSon = new JSONObject();
        JSONArray feeDetailsArr = new JSONArray();
        // todo fee details will be array
        JSONObject feeDetail1 = new JSONObject();
        JSONObject feeDetail2 = new JSONObject();
        feeDetail2.put("commercialName", "IntegrationFee");
        feeDetail2.put("commercialAmount", "500");
        feeDetailsArr.put(feeDetail2);
        otherFeeJSon.put("feeDetails", feeDetailsArr);
        feeDetail1.put("commercialName", "LicenceFee");
        feeDetail1.put("commercialAmount", "1200");
        feeDetailsArr.put(feeDetail1);
        maintenanceFeeJson.put("otherFees", otherFeeJSon);
        return maintenanceFeeJson;
    }

    public JSONObject getPreferenceBenefitJson(JSONObject jsonObject) {
        JSONObject preferenceBenefitJson = new JSONObject();
        JSONObject otherFeeJSon = new JSONObject();
        JSONArray feeDetailsArr = new JSONArray();
        // todo fee details will be array
        JSONObject feeDetail1 = new JSONObject();
        JSONObject feeDetail2 = new JSONObject();
        feeDetail2.put("commercialName", "IntegrationFee");
        feeDetail2.put("commercialAmount", "500");
        feeDetailsArr.put(feeDetail2);
        otherFeeJSon.put("feeDetails", feeDetailsArr);
        feeDetail1.put("commercialName", "LicenceFee");
        feeDetail1.put("commercialAmount", "1200");
        feeDetailsArr.put(feeDetail1);
        preferenceBenefitJson.put("otherFees", otherFeeJSon);
        return preferenceBenefitJson;
    }

    public JSONObject getOtherFees(JSONObject jsonObject) {
        JSONObject otherFeeJSon = new JSONObject();
        JSONArray feeDetailsArr = new JSONArray();
        // todo fee details will be array
        JSONObject feeDetail1 = new JSONObject();
        JSONObject feeDetail2 = new JSONObject();
        feeDetail2.put("commercialName", "IntegrationFee");
        feeDetail2.put("commercialAmount", "500");
        feeDetailsArr.put(feeDetail2);
        otherFeeJSon.put("feeDetails", feeDetailsArr);
        feeDetail1.put("commercialName", "LicenceFee");
        feeDetail1.put("commercialAmount", "1200");
        feeDetailsArr.put(feeDetail1);
        return new JSONObject().put("otherFees", otherFeeJSon);
    }

    public JSONObject getTerminationFeeJson(JSONObject jsonObject) {
        JSONArray returnableCommercialHeadArr = new JSONArray();
        // todo fee details will be array
        JSONObject returnableCommercialHead1 = new JSONObject();
        JSONObject returnableCommercialHead2 = new JSONObject();
        returnableCommercialHead1.put("commercialName", "IntegrationFee");
        returnableCommercialHead1.put("commercialAmount", "500");
        returnableCommercialHeadArr.put(returnableCommercialHead1);

        returnableCommercialHead2.put("commercialName", "IntegrationFee");
        returnableCommercialHead2.put("commercialAmount", "500");
        returnableCommercialHeadArr.put(returnableCommercialHead2);

        return new JSONObject().put("returnableCommercialHead", returnableCommercialHeadArr);
    }

    public JSONObject getSignUpBonusJson(JSONObject jsonObject) {
        JSONArray slabDetails = new JSONArray();
        JSONObject slab = new JSONObject();
        slab.put("slabType", "NumberOfSegments");
        slab.put("slabTypeValue", "3000000");
        slabDetails.put(slab);
        return new JSONObject().put("slabDetails", slabDetails);
    }

    public JSONObject getLookToBookJson(JSONObject jsonObject) {
        JSONObject lookToBook = new JSONObject();
        lookToBook.put("numberOfLooks", "2600");
        lookToBook.put("numberOfBooks", "1");
        lookToBook.put("isCumulative", true);
        lookToBook.put("cumulativeSequence", "0");
        return lookToBook;
    }

    public JSONObject getRemittanceFeeJson(JSONObject jsonObject) {
        jsonObject.put("totalSettlementAmount", "21000");
        jsonObject.put("totalSettlementCurrency", "INR");
        return jsonObject;
    }


    public static void main(String[] args) {
        SupplierCommercialCalculator commercialCalculator = new SupplierCommercialCalculator();
        System.out.println(commercialCalculator.calculateSupplierCommercial().toString());
        //System.out.println(commercialCalculator.getPenaltyFeeJson(new JSONObject()));
        // System.out.println(commercialCalculator.getMsfFeeJson(new JSONObject()));
        //System.out.println(commercialCalculator.getMaintenanceFeeJson(new JSONObject()));
        //System.out.println(commercialCalculator.getOtherFees(new JSONObject()));
        //  System.out.println(commercialCalculator.getSignUpBonusJson(new JSONObject()));
    }
}
