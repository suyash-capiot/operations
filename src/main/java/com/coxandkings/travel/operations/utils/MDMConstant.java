package com.coxandkings.travel.operations.utils;

public interface MDMConstant {

    String MSFFees = "MSfFees";
    String commission = "commissionFlights";
    String destinationIncentives = "destinationIncentives";
    String FOC = "FOC";
    String incentivesOnTopUp = "incentiveOnTopup";
    String issuanceFees = "issuanceFees";
    String lookToBook = "lookToBook";
    String otherFees = "otherFees";
    String overridingCommission = "overRidingCommission";
    String penaltyFee_KickBack = "penaltyFeeOrKickBack";
    String productivityLinkedBonus = "plb";
    String remittanceFees = "remittanceFees";
    String sectorIncentives = "sectorWiseIncentives";
    String segmentsFees = "segmentFees";
    String serviceCharges = "serviceCharge";
    String signUpBonus = "signupBonus";
    String terminationFees = "terminationFees";

    interface FOCMapping {
        String Accommodation = "freeOfCostAccommodation";
        String Activities = "freeOfCostActivities";
        String Transportation = "freeOfCostFlights";
        String Holidays = "freeOfCostHolidays";
    }
}
