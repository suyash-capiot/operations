package com.coxandkings.travel.operations.enums.reconfirmation;

public enum ClientReconfirmationStatus {

    RECONFIRMED_BY_CLIENT("Reconfirmed"),
    REJECTED_BY_CLIENT("Rejected By Client"),
    REJECTED_DUE_TO_NO_RESPONSE_FROM_CLIENT("Rejected due to No Response"),
    RECONFIRMATION_REQUEST_ON_HOLD_BY_CLIENT("Reconfirmation On Hold");

    private String reconfirmationStatus;

    private ClientReconfirmationStatus(String newReconfirmationStatus ) {
        this.reconfirmationStatus = newReconfirmationStatus;
    }

    public ClientReconfirmationStatus from( String newReconfirmationStatus ) {
        if( newReconfirmationStatus == null || newReconfirmationStatus.isEmpty() )  {
            return null;
        }

        ClientReconfirmationStatus aReconStatus = null;
        for( ClientReconfirmationStatus aTmpStatus : ClientReconfirmationStatus.values() )  {
            if( aTmpStatus.getReconfirmationStatus().equalsIgnoreCase(  newReconfirmationStatus  )) {
                aReconStatus = aTmpStatus;
                break;
            }
        }

        return aReconStatus;
    }

    public String getReconfirmationStatus() {
        return this.reconfirmationStatus;
    }
}
