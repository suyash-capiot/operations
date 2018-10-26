package com.coxandkings.travel.operations.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JsonObjectProvider<AnEntity> {

    private static Logger logger = LogManager.getLogger( JsonObjectProvider.class );

    private Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);

    public String getAttributeValue(String jsonAsString, String jsonPathExpression, Class<String> stringClass) {
        logger.debug( "*** Entering JsonObjectProvider.getAttributeValue() method ***" );
        String jsonAttributeValue = null;
        try {
            if( jsonAsString == null || jsonAsString.trim().length() == 0 )  {
                logger.debug( "Input JSON string is empty" );
                return jsonAttributeValue;
            }
            else {
                Object attributeInfo = JsonPath.parse(jsonAsString, option).read(jsonPathExpression);
                if (attributeInfo != null) {
                    jsonAttributeValue = attributeInfo.toString();
                }
            }
        }
        catch( Exception e )    {
            logger.error( "Error occurred while parsing JSON string.  Please check JSON Path filter expression", e );
        }
        logger.debug( "*** Exiting JsonObjectProvider.getAttributeValue() method ***" );
        return jsonAttributeValue;
    }

    /**
     * The purpose of this method is to parse a JSON string using JSON Path, return a POJO based on input type.
     * This method returns null in case the filter/json path expression fails.
     * Note: If the child JSON structure that you expect is of type Map<String,String>, then then this method will
     * convert the Map to desired object instance
     *
     * @param jsonAsString - the input JSON data which contains data to be extracted
     * @param jsonPathExpression - the filter expression used by JSONPath APIs
     * @param desiredClassType - The output POJO class type
     * @return - Returns an object instance based on the class type (desiredType)
     */
    public AnEntity getChildObject( String jsonAsString, String jsonPathExpression, Class desiredClassType ) {
        logger.debug( "*** Entering JsonObjectProvider.getChildObject() method ***" );
        AnEntity childObject = null;
        try {
            Object resultantObject = JsonPath.parse(jsonAsString, option).read(jsonPathExpression);
            if( resultantObject != null ) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                childObject = (AnEntity) mapper.convertValue( resultantObject, desiredClassType );
            }
        }
        catch( Exception e )    {
            logger.error( "Error occurred while parsing JSON string.  Please check JSON Path filter expression", e );
        }
        logger.debug( "*** Exiting JsonObjectProvider.getChildObject() method ***" );
        return childObject;
    }

    /**
     * The purpose of this method is to parse the Json using JSON Path and return the children data as a List
     * of a specific type.  If the jsonPathExpression is wrong, this method returns null;
     *
     * @param jsonAsString - the JSON to be parsed
     * @param jsonPathExpression - the JSON Path expresson to fetch the children data
     * @param collectionEntityType - the Entity type to be converted to in the collection
     * @return - returns List of elements of type collectionEntityType
     */
    public List<AnEntity> getChildrenCollection(String jsonAsString, String jsonPathExpression, Class collectionEntityType )  {
        logger.debug( "*** Entering JsonObjectProvider.getChildrenCollection() method ***" );
        ArrayList childCollection = null;

        try {
            // Changed code to use JSONObject and additional checks to handle JSONArray objects (may be due to API upgrades)!!
            Object parseResponse = JsonPath.parse(jsonAsString, option).read(jsonPathExpression);
            childCollection = new ArrayList();
            if( parseResponse instanceof JSONArray )    {
                JSONArray responseArray = (JSONArray) parseResponse;
                int elementsSize = responseArray.size();
                childCollection = new ArrayList();
                if( elementsSize > 0 ) {
                    for (int index = 0; index < elementsSize; index++) {
                        Object aChildElement = responseArray.get( index );
                        if( aChildElement instanceof Map )  {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            if( collectionEntityType.equals( String.class ))    {
                                String aChildObject = new JSONObject( (Map) aChildElement ).toJSONString();
                                childCollection.add( aChildObject );
                            }
                            else {
                                Object aChildObject = (AnEntity) mapper.convertValue(aChildElement, collectionEntityType);
                                childCollection.add( aChildObject );
                            }
                        }
                    }
                }
            }
        }
        catch( Exception e )    {
            logger.error( "Error occurred while parsing JSON string.  Please check JSON Path filter expression", e );
        }

        logger.debug( "*** Exiting JsonObjectProvider.getChildrenCollection() method ***" );
        return childCollection;
    }

    public String getChildJSON(String jsonAsString, String jsonPathExpression ) {
        logger.debug( "*** Entering JsonObjectProvider.getChildJSON() method ***" );
        String childObject = null;
        try {
            Object parseResponse = JsonPath.parse(jsonAsString, option).read(jsonPathExpression);
            if( parseResponse instanceof Map)  {
                childObject = new JSONObject( (Map) parseResponse ).toJSONString();
            }
        }
        catch( Exception e )    {
            logger.error( "Error occurred while parsing JSON string.  Please check JSON Path filter expression", e );
        }
        logger.debug( "*** Exiting JsonObjectProvider.getChildJSON() method ***" );
        return childObject;
    }
}