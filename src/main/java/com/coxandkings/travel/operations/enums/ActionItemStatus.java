package com.coxandkings.travel.operations.enums;

public enum ActionItemStatus {

    ACTIVE( "Active" ), INACTIVE ("Inactive");

    private String actionItemStatus = null;
    private ActionItemStatus( String newStatus )    {
        actionItemStatus = newStatus ;
    }

    public String getActionItemStatus() {
        return actionItemStatus;
    }

    public ActionItemStatus fromString( String newActionItem )  {
        ActionItemStatus aStatus = INACTIVE;
        if( newActionItem != null && (!newActionItem.isEmpty()) )  {
            for( ActionItemStatus tmpStatus : ActionItemStatus.values() ) {
                if( tmpStatus.getActionItemStatus().equalsIgnoreCase( newActionItem ))  {
                    aStatus = tmpStatus;
                    break;
                }
            }
        }

        return aStatus;
    }
}
