package com.coxandkings.travel.operations.enums.commercialStatements;

public enum CommercialHeads {

	STANDARD("Standard Commercial","Standard"),
	OVERRIDING_COMMISION("Overriding Commission","Overriding"),
	PLB("Productivity Linked Bonus","PLB"),
	DESTINATION_INCENTIVES("Destination Incentives","DestinationIncentive"),
	SEGMENT_FEE("Segments Fees","SegmentFee"),
	SERVICE_CHARGE("Service Charges","ServiceCharge"),
	ISSUANCE_FEE("Issuance Fees","IssuanceFee"),
	MANAGEMENT_FEE("Management Fee","ManagementFee"),
	SECTOR_INCENTIVES("Sector Wise Incentives","SectorWiseIncentive"),
	COMMISSION("Commission","Commission"),
	MAINTENANCE_FEE("Maintenance Fees","MaintenanceFees"),
	INTEGRATION_FEE("Integration Fees","IntegrationFees"),
	LICENCE_FEE("Licence Fees","LicenceFees"),
	WEB_SERVICE_FEE("Web Service Fees","WebServiceFees"),
	LOYALTY_BONUS("Loyalty Bonus","LoyaltyBonus"),
	PREFERENCE_BENEFIT("Preference Benefit","PreferenceBenefit"),
	RETAINER_FEE("Retainer Fee","RetainerFee"),
	LISTING_FEE("Listing Fee","ListingFee"),
	CONTENT_ACCESS_FEE("Content Access Fee","ContentAccessFee"),
	SIGNUP_FEE("Sign Up Fees","SignUpFees"),
	SIGNUP_BONUS("Sign Up Bonus","SignUpBonus"),
	LOOK2BOOK("Look To Book","LookToBook"),
	MSF_FEE("MSF Fees","MSFFees"),
	INCENTIVES_ON_TOPUP("Incentives On Top Up","IncentivesOnTopUp"),
	TERMINATION_FEE("Termination Fees","TerminationFees"),
	PENALTY_FEE("Penalty Fee / Kick Back","PenaltyFee"),
    FOC("Free Of Cost (FOC)","FreeOfCost"),
    LOST_TICKET("Lost Ticket","LostTicket"),
    REMITTANCE_FEE("Remittance Fees","RemittanceFees"),
    TRAINING_FEE("Training Fees","TrainingFees");
    //OTHER_FEE("Training Fees");
    
    private String mdmValue;
	 private String brmsValue;

    CommercialHeads(String mdmValue,String brmsValue){
        this.mdmValue=mdmValue;
        this.brmsValue=brmsValue;
    }

    public String toMDMValue() {
        return mdmValue;
    }
    
    public String toBRMSValue() {
        return brmsValue;
    }
    
    public static CommercialHeads forMDMString(String mdmStr) {
    	for(CommercialHeads commHead:CommercialHeads.values()) {
    		if(commHead.mdmValue.equalsIgnoreCase(mdmStr))
    			return commHead;
    	}
    	return null;
    }
    
    public static CommercialHeads forBRMSString(String brmsStr) {
    	for(CommercialHeads commHead:CommercialHeads.values()) {
    		if(commHead.brmsValue.equalsIgnoreCase(brmsStr))
    			return commHead;
    	}
    	return null;
    }

    /* interface FOCMapping {
        String Accommodation = "freeOfCostAccommodation";
        String Activities = "freeOfCostActivities";
        String Transportation = "freeOfCostFlights";
        String Holidays = "freeOfCostHolidays";
    }*/
}
