package com.coxandkings.travel.operations.utils.scheduler;



import com.coxandkings.travel.operations.resource.scheduler.FortnightSettlementScheduleResponse;
import com.coxandkings.travel.operations.resource.scheduler.FortnightlyPeriodicityInput;
import com.coxandkings.travel.operations.resource.scheduler.SettlementScheduleResponse;

import java.time.LocalDate;
import java.util.Calendar;

public class FortnightlySettlementSchedulerUtil {

    public static FortnightSettlementScheduleResponse computeFortnightSettlementSchedule( FortnightlyPeriodicityInput input ) {

        FortnightSettlementScheduleResponse response = new FortnightSettlementScheduleResponse();

        int firstFortnightSettlementDueDate = input.getFirstFortnightSettlementDueDay();
        boolean settleFirstFornightByCurrentMonth = input.isSettleFirstFornightByCurrentMonth();

        int secondFortnightSettlementDueDate = input.getSecondFortnightSettlementDueDay();
        boolean settleSecondFornightByCurrentMonth = input.isSettleSecondFornightByCurrentMonth();

        SettlementScheduleResponse firstFortnightSchedule = computeForFornightSettlementSchedule( firstFortnightSettlementDueDate, true, settleFirstFornightByCurrentMonth );
        SettlementScheduleResponse secondFortnightSchedule = computeForFornightSettlementSchedule( secondFortnightSettlementDueDate, false, settleSecondFornightByCurrentMonth );

        if( firstFortnightSchedule != null )    {
            response.setFirstFortnightScheduleResponse( firstFortnightSchedule );
        }
        if( secondFortnightSchedule != null )   {
            response.setSecondFortnightScheduleResponse( secondFortnightSchedule );
        }

        return response;
    }

    private static SettlementScheduleResponse computeForFornightSettlementSchedule(int aSettlementDueDate, boolean isFirstFortnight, boolean settleCurrentMonth )    {

        SettlementScheduleResponse aSettlementSchedulerResponse = null;

        if( aSettlementDueDate > 0 )    {
            // Handle for 28 days, 29 days in a month scenario!!
            Calendar calendar = Calendar.getInstance();
            int lastDate = calendar.getActualMaximum(Calendar.DATE);
            if( aSettlementDueDate > lastDate ) {
                aSettlementDueDate = lastDate;
            }

            aSettlementSchedulerResponse = new SettlementScheduleResponse();

            if( isFirstFortnight )  {
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now();
                startDate = startDate.withDayOfMonth( 1 );
                endDate = endDate.withDayOfMonth( 15 );
                aSettlementSchedulerResponse.setStartDate( startDate );
                aSettlementSchedulerResponse.setEndDate( endDate );
            }
            else {
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now();
                startDate = startDate.withDayOfMonth( 16 );
                endDate = endDate.withDayOfMonth( lastDate ); //Compute last day of month
                aSettlementSchedulerResponse.setStartDate( startDate );
                aSettlementSchedulerResponse.setEndDate( endDate );
            }

            if( settleCurrentMonth )    {
                LocalDate settlementDate = LocalDate.now();
                settlementDate = settlementDate.withDayOfMonth( aSettlementDueDate );
                aSettlementSchedulerResponse.setSettlementDueDay( settlementDate );
            }
            else {
                LocalDate settlementDate = LocalDate.now();
                settlementDate = settlementDate.withDayOfMonth( aSettlementDueDate );
                settlementDate = settlementDate.plusMonths( 1 ); // if its next month!!
                aSettlementSchedulerResponse.setSettlementDueDay( settlementDate );
            }

            if( aSettlementSchedulerResponse.getEndDate().equals( LocalDate.now() ))   {
                aSettlementSchedulerResponse.setShouldRunToday( true );
            }
        }

        return aSettlementSchedulerResponse;
    }
}
