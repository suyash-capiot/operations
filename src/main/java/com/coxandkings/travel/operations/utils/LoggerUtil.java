package com.coxandkings.travel.operations.utils;


import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;

public class LoggerUtil {

    private LoggerUtil() {
    }

    // Providing Global point of access for the loggerUtil
    public static Logger getLoggerInstance(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

}
