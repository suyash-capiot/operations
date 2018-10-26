package com.coxandkings.travel.operations.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;

public class CopyUtils {
    private static Logger log =  LogManager.getLogger(CopyUtils.class);
    public static <T> void copy(T source, T dest){
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException e) {
            log.error("Error while coping object from:"+ source.getClass() + " to:"+dest.getClass());
        } catch (InvocationTargetException e) {
            log.error("Error while coping object from:"+ source.getClass() + " to:"+dest.getClass());
        }
    }
}
