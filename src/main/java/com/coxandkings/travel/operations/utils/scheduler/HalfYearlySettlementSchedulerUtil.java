package com.coxandkings.travel.operations.utils.scheduler;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

public class HalfYearlySettlementSchedulerUtil {

    public static boolean computeForHalfYearlySettelemetSchelule(LocalDate scheduledDate) {

        return LocalDate.now().isEqual(scheduledDate);
    }

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalDate lastOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(date==lastOfMonth);
        System.out.println(date.isEqual(lastOfMonth));
        System.out.println(ZonedDateTime.now().minusMonths(1).minusDays(1));
        System.out.println( ZonedDateTime.now().minusDays(1));
    }
}
