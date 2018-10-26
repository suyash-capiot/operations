package com.coxandkings.travel.operations.helper.beconsumption;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class SupplierDetailAccessor {
    public static final String JSON_PROP_SUPPREF = "supplierRef";
    public static final String JSON_PROP_AIRITINERARY = "airItinerary";
    public static final String JSON_PROP_ORIGDESTOPTS = "originDestinationOptions";
    public static final String JSON_PROP_FLIGHTSEG = "flightSegment";
    public static final String JSON_PROP_OPERAIRLINE = "operatingAirline";
    public static final String JSON_PROP_AIRLINECODE = "airlineCode";
    public static final String JSON_PROP_FLIGHTNBR = "flightNumber";
    public static final char KEYSEPARATOR = '|';
    public static final String JSON_PROP_COUNTRYCODE = "countryCode";
    public static final String JSON_PROP_CITYCODE = "cityCode";
    public static final String JSON_PROP_CHKIN = "checkIn";
    public static final String JSON_PROP_CHKOUT = "checkOut";
    public static final String JSON_PROP_HOTELINFO = "hotelInfo";
    public static final String JSON_PROP_HOTELCODE = "hotelCode";
    public static final String JSON_PROP_ROOMTYPEINFO = "roomTypeInfo";
    public static final String JSON_PROP_ROOMTYPECODE = "roomTypeCode";
    public static final String JSON_PROP_ROOMCATEGCODE = "roomCategoryCode";
    public static final String JSON_PROP_RATEPLANINFO = "ratePlanInfo";
    public static final String JSON_PROP_RATEPLANCODE = "ratePlanCode";
    public static final String JSON_PROP_ROOMINDEX = "requestedRoomIndex";
    public static final String JSON_PROP_ROOMREF = "roomRef";
    public static final String JSON_PROP_SUPPROOMPRICE = "supplierTotalPriceInfo";
    public static final String JSON_PROP_SUPPNIGHTLYPRICEARR = "supplierNightlyPriceInfo";
    public static final String JSON_PROP_CLIENTCOMMENTITYDTLS = "clientCommercialsEntityDetails";
    public static final String JSON_PROP_SUPPCOMM = "supplierCommercials";
    public static final String JSON_PROP_ROOMPRICE = "totalPriceInfo";
    public static final String JSON_PROP_OCCUPANCYARR = "occupancyInfo";
    public static final String JSON_PROP_SUPPROOMyPRICE = "supplierTotalPriceInfo";









    public static String getRedisKeyForPricedItinerary(JSONObject pricedItinJson) throws JSONException {
        StringBuilder strBldr = new StringBuilder(pricedItinJson.optString(JSON_PROP_SUPPREF));

        JSONObject airItinJson = pricedItinJson.optJSONObject(JSON_PROP_AIRITINERARY);
        if (airItinJson != null) {
            JSONArray origDestOptsJsonArr = airItinJson.getJSONArray(JSON_PROP_ORIGDESTOPTS);
            for (int j = 0; j < origDestOptsJsonArr.length(); j++) {
                JSONObject origDestOptJson = origDestOptsJsonArr.getJSONObject(j);
                strBldr.append('[');
                JSONArray flSegsJsonArr = origDestOptJson.optJSONArray(JSON_PROP_FLIGHTSEG);
                if (flSegsJsonArr == null) {
                    break;
                }

                for (int k = 0; k < flSegsJsonArr.length(); k++) {
                    JSONObject flSegJson = flSegsJsonArr.getJSONObject(k);
                    JSONObject opAirlineJson = flSegJson.getJSONObject(JSON_PROP_OPERAIRLINE);
                    strBldr.append(opAirlineJson.getString(JSON_PROP_AIRLINECODE).concat(opAirlineJson.getString(JSON_PROP_FLIGHTNBR)).concat("|"));
                }
                strBldr.setLength(strBldr.length() - 1);
                strBldr.append(']');
            }
        }
        return strBldr.toString();
    }

    public static Map<String, String> getSupplierData(String key) {
        Map<String, String> reprcSuppFaresMap = null;
        try (Jedis redisConn = RedisConfig.getRedisConnectionFromPool();) {
            String redisKey = key;
            reprcSuppFaresMap = redisConn.hgetAll(redisKey);
            if (reprcSuppFaresMap == null || reprcSuppFaresMap.isEmpty()) {
                throw new OperationException(String.format("Booking context not found for %s", redisKey));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reprcSuppFaresMap;
    }

    public static Map<String, String> getSupplierPricingDetail(String key) {
        Map<String, String> reprcSuppFaresMap = null;
        try (Jedis redisConn = RedisConfig.getRedisConnectionFromPool();) {

            reprcSuppFaresMap = redisConn.hgetAll(key);
            if (reprcSuppFaresMap == null || reprcSuppFaresMap.isEmpty()) {
                throw new OperationException(String.format("Booking context not found for %s", key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reprcSuppFaresMap;
    }
    static public String getRedisKeyForReq( JSONObject subReqJson) throws JSONException {

        return String.format("%s%c%s%c%s%c%s%c%s",subReqJson.getString(JSON_PROP_SUPPREF),KEYSEPARATOR,
                subReqJson.getString(JSON_PROP_COUNTRYCODE),KEYSEPARATOR,subReqJson.getString(JSON_PROP_CITYCODE),KEYSEPARATOR,
                subReqJson.getString(JSON_PROP_CHKIN),KEYSEPARATOR,subReqJson.getString(JSON_PROP_CHKOUT));
    }

    static String getRedisKeyForRoomStay(JSONObject roomInfoJson) throws JSONException {
        //TODO:should supplier ref be present or indexes/uuid should be used?
        //TODO:add req params here
        return String.format("%c%s%c%s%c%s%c%s%c%d%c%s",KEYSEPARATOR,roomInfoJson.getJSONObject(JSON_PROP_HOTELINFO).getString(JSON_PROP_HOTELCODE),KEYSEPARATOR,
                roomInfoJson.getJSONObject(JSON_PROP_ROOMTYPEINFO).getString(JSON_PROP_ROOMTYPECODE),KEYSEPARATOR,roomInfoJson.getJSONObject(JSON_PROP_ROOMTYPEINFO).getString(JSON_PROP_ROOMCATEGCODE),
                KEYSEPARATOR,roomInfoJson.getJSONObject(JSON_PROP_RATEPLANINFO).getString(JSON_PROP_RATEPLANCODE),KEYSEPARATOR,roomInfoJson.getInt(JSON_PROP_ROOMINDEX),KEYSEPARATOR,roomInfoJson.getJSONObject(JSON_PROP_ROOMTYPEINFO).getString(JSON_PROP_ROOMREF));
    }

    static String getRedisKeyForRoomPrice(JSONObject subReqJson,JSONObject roomInfoJson) throws JSONException {
        return String.format("%s%s", getRedisKeyForReq(subReqJson),getRedisKeyForRoomStay(roomInfoJson));
    }
}
