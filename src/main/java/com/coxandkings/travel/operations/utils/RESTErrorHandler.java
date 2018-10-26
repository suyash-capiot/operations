package com.coxandkings.travel.operations.utils;


import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class RESTErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        //conversion logic for decoding conversion
        DataInputStream arrayInputStream = new DataInputStream( (InputStream) response.getBody());
        Scanner scanner = new Scanner(arrayInputStream);
        scanner.useDelimiter("\\Z");
        String data = "";
        if (scanner.hasNext())
            data = scanner.next();
        System.out.println( "Error information is: " + data );
    }
}