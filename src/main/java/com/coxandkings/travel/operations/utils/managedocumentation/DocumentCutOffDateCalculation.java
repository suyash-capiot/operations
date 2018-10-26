package com.coxandkings.travel.operations.utils.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.ApplyCutOff;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.utils.DateTimeUtil;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DocumentCutOffDateCalculation {

    public static String calculateCutOffDate(OpsProduct product, DocumentSetting documentSetting, ZonedDateTime bookingDate) {

        if (documentSetting.getCutOf() != null) {
            if (documentSetting.getCutOf().getApplyAs().equals(ApplyCutOff.FROM_BOOKING_DATE.getValue())) {
                if (documentSetting.getCutOf().getCutOffMode().equals(ApplyCutOff.DAYS.getValue())) {
                    return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(bookingDate.plusDays(documentSetting.getCutOf().getCutOffPeriod()));
                } else if (documentSetting.getCutOf().getCutOffMode().equals(ApplyCutOff.HOURS.getValue())) {
                    return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(bookingDate.plusHours(documentSetting.getCutOf().getCutOffPeriod()));
                }
            } else if (documentSetting.getCutOf().getApplyAs().equals(ApplyCutOff.PRIOR_TO_TRAVEL_DATE.getValue())) {
                ZonedDateTime zonedDateTime = null;
                if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                    zonedDateTime = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getDepartureDateZDT();
                } else if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                    zonedDateTime = DateTimeUtil.formatBEDateTimeZone(product.getOrderDetails().getHotelDetails().getRooms().iterator().next().getCheckIn());
                }

                if (zonedDateTime != null) {
                    if (documentSetting.getCutOf().getCutOffMode().equals(ApplyCutOff.DAYS.getValue())) {
                        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(zonedDateTime.minusDays(documentSetting.getCutOf().getCutOffPeriod()));
                    } else if (documentSetting.getCutOf().getCutOffMode().equals(ApplyCutOff.HOURS.getValue())) {
                        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(zonedDateTime.minusHours(documentSetting.getCutOf().getCutOffPeriod()));
                    }
                } else
                    return null;

            } else if (documentSetting.getCutOf().getApplyAs().equals(ApplyCutOff.IMMEDIATE_AFTER_BOOKING.getValue())) {
                return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(bookingDate);
            }
            return null;
        } else return null;
    }
}
