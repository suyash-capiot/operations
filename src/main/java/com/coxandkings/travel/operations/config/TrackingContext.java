package com.coxandkings.travel.operations.config;

import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.*;

public class TrackingContext {

    private static Map<Long, TrackingContext> mTrackCtx = new HashMap<Long, TrackingContext>();
    private static TrackingContext DEFAULT_TRACKING_CONTEXT = new TrackingContext();
    private List<AbstractMap.SimpleEntry<String, String>> mTrackVals = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
    private String mTrackParamsStr;

    private TrackingContext() {
        mTrackParamsStr = "";
    }


    private TrackingContext(KafkaBookingMessage kafkaBookingMessage) throws Exception {
        String key = "1", val = "1";
        StringBuilder strBldr = new StringBuilder();

        ObjectMapper m = new ObjectMapper();
        Map<String, Object> props = m.convertValue(kafkaBookingMessage, Map.class);
        String value = "";
        Set<String> keySet = props.keySet();
        for (String kafkaObjectKey : keySet) {
            mTrackVals.add(new AbstractMap.SimpleEntry<String, String>(kafkaObjectKey, (String) props.get(kafkaObjectKey)));
            value = (String) props.get(kafkaObjectKey);
            if (StringUtils.isEmpty(value)) {
                value = "null";
            }
            strBldr.append(String.format("[%s: %s] ", kafkaObjectKey, value));


            strBldr.setLength(strBldr.length() - 1);

        }

        //Todo work on this key value

        mTrackParamsStr = strBldr.toString();
    }

    public static TrackingContext getTrackingContext() {
        TrackingContext trackCtx = mTrackCtx.get(Thread.currentThread().getId());
        return (trackCtx != null) ? trackCtx : DEFAULT_TRACKING_CONTEXT;
    }

    public static void setTrackingContext(KafkaBookingMessage kafkaBookingMessage) throws Exception {


        TrackingContext trckngCtx = new TrackingContext(kafkaBookingMessage);
        // trckngCtx.addRestServiceTrackingParam();
        mTrackCtx.put(Thread.currentThread().getId(), trckngCtx);
    }

    public String getTrackingParameter(String paramName) {
        for (AbstractMap.SimpleEntry<String, String> trackVal : mTrackVals) {
            if (trackVal.getKey().equals(paramName)) {
                return trackVal.getValue();
            }
        }

        return "";
    }

    public String toString() {
        return mTrackParamsStr;
    }

  /*  private void addRestServiceTrackingParam() {
        List<AbstractMap.SimpleEntry<String, String>> trackParams = ServletContext.getRestTrackingParamList();
        mTrackVals.addAll(trackParams);
        trackParams.forEach(trackElem -> mTrackParamsStr=mTrackParamsStr.concat(String.format(" [%s: %s]", trackElem.getKey(),trackElem.getValue())));
    }*/
}
