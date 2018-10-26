package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class MessageFormatter {

    @Autowired
    MessageSource messageSource;

    public String errorMessageFormatter(OperationException operationException){
        System.out.println(" in errorMessageFormatter " + operationException.toString());
        Message message = new Message();
//        CopyUtils.copy(operationException, message);
        return messageFormatter(message);
    }

    public String messageFormatter(Message message){
        System.out.println(" in messageFormatter " + message.toString());
        StringBuffer formattedErrorMessage= new StringBuffer();
        int i=0;
        String[] paramCodes ;
        String[] paramValues = new String[0];

        String errorMessage= messageSource.getMessage(message.getErrorCode(),null,message.getLocale());
        MessageFormat messageFormat = new MessageFormat(errorMessage);
        System.out.println(" errorMessgae is    "+ errorMessage);
        if(message.getParamList()!=null && message.getParamList().length >0){
           paramCodes = message.getParamList();
           paramValues = new String[paramCodes.length];
            for (String paramCode : paramCodes) {
                System.out.println("param value is " + paramCode);
                paramValues[i] = messageSource.getMessage(paramCode, null, message.getLocale());
                i++;
            }
        }
        messageFormat.format(paramValues, formattedErrorMessage, null);
        return formattedErrorMessage.toString();
    }


}
