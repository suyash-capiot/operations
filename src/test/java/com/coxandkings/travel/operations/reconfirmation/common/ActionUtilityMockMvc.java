package com.coxandkings.travel.operations.reconfirmation.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ActionUtilityMockMvc {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( ActionUtilityMockMvc.class );

    public static MockHttpServletResponse performAction( RequestBuilder requestBuilder , ResultMatcher status , MockMvc mockMvc ) {
        log.info( " //###  performAction ###" );
        MockHttpServletResponse response = null;
        try {
            if ( mockMvc != null ) {
                if ( requestBuilder != null ) {
                    MvcResult result = mockMvc.perform( requestBuilder ).andExpect( status ).andReturn( );
                    if ( result != null ) {
                        response = result.getResponse( );
                        int statusCode = response.getStatus( );
                        log.info( statusCode );
                    } else {
                        log.info( "MvcResult in null" );
                    }
                } else {
                    log.info( "RequestBuilder in null" );
                }
            } else {
                log.info( "mock mvc in null" );
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
            log.error( "exception " + e.toString( ) );
        }
        return response;
    }

    public static String prettyJSON( Object object ) {
        log.info( " //###  prettyJSON ###" );
        String s = null;
        ObjectMapper mapper = new ObjectMapper( );
        try {
            s = mapper.writerWithDefaultPrettyPrinter( ).writeValueAsString( object );
           // System.err.println( s );
           // log.info( s );
            return s;
        } catch ( Exception e ) {
            e.printStackTrace( );
            log.error( "exception " + e.toString( ) );
            return s;
        }
    }


    public static RequestBuilder baseMethodForPost( String url , Object request ) {
        log.info( " //###  baseMethodForPost ###" );
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post( url )
                .accept( MediaType.APPLICATION_JSON )
                .content( ActionUtilityMockMvc.prettyJSON( request ) )
                .contentType( MediaType.APPLICATION_JSON );
        return requestBuilder;
    }

    public static RequestBuilder baseMethodForGet( String url , Object... uriVars ) {
        log.info( " //###  baseMethodForGet ###" );
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get( url , uriVars )
                .accept( MediaType.APPLICATION_JSON )
                .contentType( MediaType.APPLICATION_JSON );
        return requestBuilder;
    }
}
