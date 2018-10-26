package com.coxandkings.travel.operations.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTimeUtil {

    private static Logger log = LogManager.getLogger(DateTimeUtil.class);

    public static String formatBEDateTime( String aBEDateTime ) throws ParseException {
        if( aBEDateTime == null || aBEDateTime.trim().length() == 0 )   {
            return null;
        }
        String opsDateTime = null;
        String beDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z['z']'";
        String opsDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

        SimpleDateFormat formatter = new SimpleDateFormat( beDateFormat );
        SimpleDateFormat opsFormatter = new SimpleDateFormat( opsDateFormat );
        java.util.Date aDate = formatter.parse( aBEDateTime );
        opsDateTime = opsFormatter.format( aDate );

        return opsDateTime;
    }

    public static ZonedDateTime formatBEDateTimeZone(String aBEDateTime )  {
         if( aBEDateTime == null || aBEDateTime.trim().length() == 0 )   {
            return null;
        }
        ZonedDateTime zonedDateTime = null;
        String opsDateTime = null;
        String beDateFormats[] = {"yyyy-MM-dd'T'HH:mm:ss.SSSz", "yyyy-MM-dd'T'HH:mm:ss'Z['z']'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z['z']'", "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSSX'['z']'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSX'['zzzz']'", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ssXXX"};
        java.util.Date aDate = null;
        try {
            aDate = DateUtils.parseDate(aBEDateTime, beDateFormats);
        } catch (Exception e) {
            try {
                zonedDateTime = ZonedDateTime.parse(
                        aBEDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.systemDefault()));
                return zonedDateTime;
            }catch(Exception x){
                log.error("Unable to parse date from Booking Engine");
                Calendar rightNow = Calendar.getInstance();
                rightNow.add(Calendar.DAY_OF_MONTH, 1 );
                aDate = rightNow.getTime();
            }
        }

//        DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
//        DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
//        Date date = originalFormat.parse("August 21, 2012");
//        String formattedDate = targetFormat.format(date);
//
//        SimpleDateFormat formatter = new SimpleDateFormat( beDateFormat );
//        SimpleDateFormat opsFormatter = new SimpleDateFormat( opsDateFormat );
//        java.util.Date aDate = formatter.parse( aBEDateTime );

        zonedDateTime = ZonedDateTime.ofInstant(aDate.toInstant(),
                ZoneId.systemDefault());

        return zonedDateTime;
    }

    public static  void main( String[] args ) throws ParseException {
        System.out.println( DateTimeUtil.formatBEDateTime( "2018-03-07T11:50:09.017Z[UTC]" ) );
    }
}
