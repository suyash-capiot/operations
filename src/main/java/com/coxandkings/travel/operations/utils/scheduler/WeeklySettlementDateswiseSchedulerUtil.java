package com.coxandkings.travel.operations.utils.scheduler;

import com.coxandkings.travel.operations.resource.scheduler.SettlementScheduleResponse;
import com.coxandkings.travel.operations.resource.scheduler.WeeklyDateswisePeriodicityInput;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklySettlementDateswiseSchedulerUtil {

    public static SettlementScheduleResponse canExecuteDateswiseSettlement(List<WeeklyDateswisePeriodicityInput> inputList )    {

        SettlementScheduleResponse response = null;
        if( inputList != null && inputList.size() > 0 ) {

            for( WeeklyDateswisePeriodicityInput aConfiguration : inputList )    {
                LocalDate aSettlementDate = aConfiguration.getSettlementDate();
                LocalDate today = LocalDate.now();
                if( aSettlementDate.equals( today ))    {
                    response = new SettlementScheduleResponse();
                    response.setShouldRunToday( true );

                    response.setStartDate( aConfiguration.getFromDate() );
                    response.setEndDate( aConfiguration.getToDate() );
                    response.setSettlementDueDay( aSettlementDate );

                    break;
                }
            }
        }

        return response;
    }

    public static void main( String args[] )    {

        // Positive scenario - test (use appropriate settlementDate1)
        // Business done on first week of this year, settle on 10th June
        LocalDate startDate1 = LocalDate.of( 2018, 1, 1 );
        LocalDate endDate1 = LocalDate.of( 2018, 1, 7 );
        LocalDate settlementDate1 = LocalDate.of( 2018, 6, 10 );

        // business done on Christmas month, settle on 1st Jan
        LocalDate startDate2 = LocalDate.of( 2018, 12, 25 );
        LocalDate endDate2 = LocalDate.of( 2018, 1, 31 );
        LocalDate settlementDate2 = LocalDate.of( 2019, 1, 1 );

        WeeklyDateswisePeriodicityInput configuration1 = new WeeklyDateswisePeriodicityInput();
        configuration1.setFromDate( startDate1 );
        configuration1.setToDate( endDate1 );
        configuration1.setSettlementDate( settlementDate1 );

        WeeklyDateswisePeriodicityInput configuration2 = new WeeklyDateswisePeriodicityInput();
        configuration2.setFromDate( startDate2 );
        configuration2.setToDate( endDate2 );
        configuration2.setSettlementDate( settlementDate2 );

        ArrayList<WeeklyDateswisePeriodicityInput> testData = new ArrayList<>();
        testData.add( configuration1 );
        testData.add( configuration2 );

        SettlementScheduleResponse response = WeeklySettlementDateswiseSchedulerUtil.canExecuteDateswiseSettlement( testData );
        if( response != null )  {
            System.out.println( response );
        }
    }
}
