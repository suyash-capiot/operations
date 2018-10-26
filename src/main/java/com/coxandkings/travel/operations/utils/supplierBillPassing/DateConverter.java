package com.coxandkings.travel.operations.utils.supplierBillPassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    static Logger logger=LogManager.getLogger(DateConverter.class);

    public static ZonedDateTime stringToZonedDateTime(String date) throws OperationException {
        if (StringUtils.isEmpty(date)) throw new OperationException(Constants.ER693);
        ZonedDateTime zonedDateTime = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");
        try {
            zonedDateTime = ZonedDateTime.parse(date, dtf);
        } catch (Exception e) {
            logger.debug("error occured while parsing date");
            throw new OperationException(Constants.ER371);
        }
        return zonedDateTime;
    }

    public static String zonedDateTimeToString(ZonedDateTime date){
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }

}