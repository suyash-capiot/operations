package com.coxandkings.travel.operations.exceptions;

import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.service.user.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private MessageSource messageSource;


    //Custom Exception-Handling
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {OperationException.class})
    protected ResponseEntity<Object> handleSavedSearchNotFoundException(OperationException operationException,
                                                                        WebRequest request) {

        if (operationException.getErrors() != null) {
            return handleExceptionInternal(operationException, operationException.getErrors(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }

        String errorMessage = this.buildErrorMessage(operationException, request.getLocale());
        String errorCode = operationException.getErrorCode();
        log.error("Error  " + errorMessage);

        return buildResponseEntity(new ErrorResponseResource(HttpStatus.BAD_REQUEST,
                errorMessage, errorCode));

    }

    //Custom Exception-Handling
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {InvalidTokenException.class})
    protected ResponseEntity<Object> handleSavedSearchNotFoundException(InvalidTokenException invalidException,
                                                                        WebRequest request) {

        String errorMessage = this.buildErrorMessage(invalidException, request.getLocale());
        log.error("Error  " + errorMessage);
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.UNAUTHORIZED,
                errorMessage, invalidException));
    }

    //Throwable Exception-Handling
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException runtimeException) {
        log.error(runtimeException.getMessage());
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.INTERNAL_SERVER_ERROR,
                runtimeException.getLocalizedMessage(), runtimeException));
    }

    //Throwable Exception-Handling
    @org.springframework.web.bind.annotation.ExceptionHandler(value = javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleValidationConflict(RuntimeException runtimeException) {
        log.error(runtimeException.getMessage());
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.INTERNAL_SERVER_ERROR,
                runtimeException.getLocalizedMessage(), runtimeException));
    }

    /*@org.springframework.web.bind.annotation.ExceptionHandler(value = {HttpException.class})
    protected ResponseEntity<Object> handleBadRequest(OperationException operationException, WebRequest request) {
        String errorMessage= this.buildErrorMessage(operationException, request.getLocale());
        log.error("Error  "+ errorMessage);
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.OK,
                errorMessage,operationException));
    }*/

    //Throwable Exception-Handling
    @org.springframework.web.bind.annotation.ExceptionHandler(value = RestClientException.class)
    protected ResponseEntity<Object> handleRestConflict(RuntimeException runtimeException) {
        if (runtimeException instanceof HttpServerErrorException) {
            HttpServerErrorException httpServerErrorException = (HttpServerErrorException) runtimeException;
            return buildResponseEntity(new ErrorResponseResource(HttpStatus.OK,
                    httpServerErrorException.getLocalizedMessage(), httpServerErrorException.getCause()));
        } else if (runtimeException instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) runtimeException;
            return buildResponseEntity(new ErrorResponseResource(HttpStatus.OK,
                    httpClientErrorException.getLocalizedMessage(), httpClientErrorException.getCause()));
        } else
            return buildResponseEntity(new ErrorResponseResource(HttpStatus.OK,
                    runtimeException.getMessage(), runtimeException.getCause()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException
                                                                                 httpRequestMethodNotSupportedException, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.METHOD_NOT_ALLOWED,
                httpRequestMethodNotSupportedException.getLocalizedMessage(), httpRequestMethodNotSupportedException));
    }

    //TODO : Review this - Ananth, Sudhir
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException
                                                                                  missingServletRequestParameterException, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.BAD_REQUEST,
                missingServletRequestParameterException.getLocalizedMessage(), missingServletRequestParameterException));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponseResource(HttpStatus.BAD_REQUEST,
                methodArgumentNotValidException.getLocalizedMessage(), methodArgumentNotValidException));
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponseResource errorResponseResource) {
        return new ResponseEntity<>(errorResponseResource, errorResponseResource.getStatus());
    }

    public String buildErrorMessage(OperationException e, Locale locale) {
        String errorMessage = messageSource.getMessage(e.getErrorCode(), e.getParams(), locale);
        return errorMessage;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleHttpMessageNotWritable(ex, headers, status, request);
    }


   /* private String errorMessageFormatter(OperationException operationException){
        StringBuffer formattedErrorMessage= new StringBuffer();
        int i=0;

        String errorMessage= messageSource.getMessage(operationException.getErrorCode(),null,operationException.getLocale());
        MessageFormat messageFormat = new MessageFormat(errorMessage);
        System.out.println(" errorMessgae is    "+ errorMessage);

        String[] paramCodes= operationException.getParamList();
        String[] paramValues = new String[paramCodes.length];

            for(String paramCode:paramCodes){
                System.out.println("param value is "+ paramCode);
                paramValues[i]=messageSource.getMessage(paramCode,null,operationException.getLocale());
                i++;
            }

        messageFormat.format(paramValues, formattedErrorMessage, null);
        return formattedErrorMessage.toString();
    }
*/
    //TODO Handle Exception for postgres connections - No need As it will be converted to Business Exception in Service Layer
}