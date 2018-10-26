package com.coxandkings.travel.operations.utils.scheduler;


import com.coxandkings.travel.operations.resource.scheduler.QuarterlyPeriodicity;
import com.coxandkings.travel.operations.resource.scheduler.QuarterlyPeriodicityInput;
import com.coxandkings.travel.operations.resource.scheduler.QuarterlySettlementScheduleResponse;
import com.coxandkings.travel.operations.resource.scheduler.SettlementScheduleResponse;

import java.time.LocalDate;
import java.util.Calendar;

public class QuarterlySettlementSchedulerUtil {

    public static QuarterlySettlementScheduleResponse computeQuarterlySettlementSchedule( QuarterlyPeriodicityInput input )  {

        SettlementScheduleResponse q1Response = computeSettlementScheduleForQuarter( input.getQ1Input() );
        SettlementScheduleResponse q2Response = computeSettlementScheduleForQuarter( input.getQ2Input() );
        SettlementScheduleResponse q3Response = computeSettlementScheduleForQuarter( input.getQ3Input() );
        SettlementScheduleResponse q4Response = computeSettlementScheduleForQuarter( input.getQ4Input() );

        QuarterlySettlementScheduleResponse response = new QuarterlySettlementScheduleResponse();
        response.setQ1Response( q1Response );
        response.setQ2Response( q2Response );
        response.setQ3Response( q3Response );
        response.setQ4Response( q4Response );

        return response;
    }

    private static SettlementScheduleResponse computeSettlementScheduleForQuarter(QuarterlyPeriodicity aQuarterlyPeriodicity )    {
        SettlementScheduleResponse quarterlyResponse = null;
        if( aQuarterlyPeriodicity != null ) {
            LocalDate startDate = aQuarterlyPeriodicity.getStartDate();
            LocalDate endDate = aQuarterlyPeriodicity.getEndDate();
            LocalDate settlementDueDate = aQuarterlyPeriodicity.getSettlementDueDate();

            // use 1 as start date and last day of month as last date
            startDate = startDate.withDayOfMonth( 1 );
            Calendar rightNow = Calendar.getInstance();
            rightNow.set( Calendar.MONTH, endDate.getMonthValue() + 1 );
            int lastDate = rightNow.getActualMaximum( Calendar.DAY_OF_MONTH );
            endDate = endDate.withDayOfMonth( lastDate );

            if ((startDate != null) && (endDate != null) && (settlementDueDate != null)) {
                quarterlyResponse = new SettlementScheduleResponse();
                quarterlyResponse.setStartDate(startDate);
                quarterlyResponse.setEndDate(endDate);
                quarterlyResponse.setSettlementDueDay(settlementDueDate);
                if (settlementDueDate.equals(LocalDate.now())) {
                    quarterlyResponse.setShouldRunToday(true);
                }
            }
        }

        return quarterlyResponse;
    }
}
