package com.coxandkings.travel.operations.utils.scheduler;

import com.coxandkings.travel.operations.resource.scheduler.SettlementScheduleResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeeklySettlementDaywiseSchedulerUtil {

    public static SettlementScheduleResponse computeWeeklyDaywiseSettlementSchedule(DayOfWeek fromDay, DayOfWeek toDay,
                                                                                    DayOfWeek settlementDay ) {

        SettlementScheduleResponse response = null;

        LocalDate today = LocalDate.now();
        LocalDate startDate = getStartDate( fromDay );
        LocalDate toDate = getEndDate( startDate, toDay );
        LocalDate settlementDate = getSettlementDate( toDate, settlementDay );

        response = new SettlementScheduleResponse();
        response.setStartDate( startDate );
        response.setEndDate( toDate );
        response.setSettlementDueDay( settlementDate );

        if( toDate.equals( today )) {
            response.setShouldRunToday( true );
        }
        return response;
    }

    private static LocalDate getStartDate( DayOfWeek fromDay ) {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();

        if( fromDay.equals( todayDayOfWeek ) )  {   // Ex: Start day is Sunday and Settlement day is Sunday
            today = today.minusDays( 7 );
        }
        else {
            for( int index = 1; index < 8; index ++ ) {
                today = today.minusDays( 1 );
                DayOfWeek aDay = today.getDayOfWeek();
                if( fromDay.equals( aDay )) {
                    break;
                }
            }
        }

        return today;
    }

    private static LocalDate getEndDate( LocalDate startDate, DayOfWeek toDay ) {
        LocalDate endDate = LocalDate.of( startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth() );
        DayOfWeek aDay = endDate.getDayOfWeek();
        if( ! aDay.equals( toDay ))   {
            for (int index = 1; index < 8; index++) {
                endDate = endDate.plusDays( 1 );
                aDay = endDate.getDayOfWeek();
                if( aDay.equals( toDay ))   {
                    break;
                }
            }
        }

        return endDate;
    }

    private static LocalDate getSettlementDate( LocalDate endDate, DayOfWeek settlementDay )   {
        LocalDate settlementDate = LocalDate.of( endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth() );
        DayOfWeek aDay = settlementDate.getDayOfWeek();
        if( ! aDay.equals( settlementDay ))   {
            for (int index = 1; index < 8; index++) {
                settlementDate = settlementDate.plusDays( 1 );
                aDay = settlementDate.getDayOfWeek();
                if( aDay.equals( settlementDay ))   {
                    break;
                }
            }
        }

        return settlementDate;
    }

    public static void main( String args[] )    {

        DayOfWeek sunday = DayOfWeek.MONDAY;
        DayOfWeek saturday = DayOfWeek.SATURDAY;
        DayOfWeek settlementDay = DayOfWeek.MONDAY;

        SettlementScheduleResponse settlementInfo = computeWeeklyDaywiseSettlementSchedule( sunday, saturday, settlementDay );
        if( settlementInfo != null )    {
            System.out.println( "Settlement info: " + settlementInfo );
        }
    }
}
