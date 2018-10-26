package com.coxandkings.travel.operations.aspect;

import com.coxandkings.travel.operations.utils.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;

@Aspect
@Component
public class ServiceAspect {

    private static final Logger LOGGER = LogManager.getLogger(ServiceAspect.class);

    //@Pointcut("execution(* com.coxandkings.travel.operations.service.*..*(..))")
    public void allMethodsPointcut() {
        LOGGER.info("Calling Service All JoinPoint");
    }


    @Pointcut("execution(* com.coxandkings.travel.operations.controller.coreBE.*..*(..))")
    public void allControllerPointcut() {
        LOGGER.info("Calling Controller JoinPoint");
    }

   // @Around(value = "allControllerPointcut()")
    public Object controllerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        GetMapping annotation = method.getAnnotation(GetMapping.class);
        PostMapping annotation1 = method.getAnnotation(PostMapping.class);

        PutMapping annotation2 = method.getAnnotation(PutMapping.class);

        DeleteMapping annotation3 = method.getAnnotation(DeleteMapping.class);


        String[] url = new String[0];
        String urlData = "";
        if (annotation != null) {
            url = annotation.value();
        }
        if (null != annotation1) {
            url = annotation1.value();
        }
        if (null != annotation2) {
            url = annotation2.value();
        }

        if (null != annotation3) {
            url = annotation3.value();
        }
        Object[] args = proceedingJoinPoint.getArgs();
        StringBuilder trackingData = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for (Object arg : args) {
            data = data.append("{").append(new ObjectMapper().writeValueAsString(arg)).append("},");

        }
        for (int i = 0; i < url.length; i++) {
            urlData = urlData.concat(url[i]);
        }

        trackingData.append("[className: " + className + "] [MethodName: " + methodSignature.getMethod().getName() + "] [data: " + data + "] [url: " + urlData + "]");

        LOGGER.info(trackingData);
        return proceedingJoinPoint.proceed();


    }

    @Pointcut("execution(* com.coxandkings.travel.operations.consumer.listners.*..*(..))")
    public void bookingKafkaListener() {
        LOGGER.info("Calling booking JoinPoint");
    }

    @Before(value = "bookingKafkaListener()")
    public void logBeforeGetEmployee(JoinPoint joinPoint) {
        LOGGER.info("[Listener:" + joinPoint.getTarget().getClass().getSimpleName() + "]");


    }

   // @Around(value = "allMethodsPointcut()")
    public Object stats(ProceedingJoinPoint pjp) throws Throwable {
        //Getting a logger instance
        Logger myLogger = LoggerUtil.getLoggerInstance(this.getClass());

        String className = pjp.getSignature().getDeclaringTypeName();

        Object[] args = pjp.getArgs();
        StringBuilder methodString = new StringBuilder();

        methodString.append("Requested class " + className + " In method " + pjp.getSignature().getName() + "(");

        for (Object arg : args) {
            if (null != arg) {
                methodString.append(arg.toString());
            }
        }
        methodString = methodString.append(")");
        myLogger.info(methodString.toString());

        long start = System.currentTimeMillis();
        Object output = pjp.proceed();
        myLogger.info("Requested class " + className + " " + pjp.getSignature().getName() + "() execution Completed");

        String methodName = pjp.getSignature().getName();

        long elapsedTime = System.currentTimeMillis() - start;
        if (!pjp.getSignature().getName().equals("initBinder")) {
            myLogger.info(methodName + " method in class " + className + " execution time : " + elapsedTime + " milliseconds.");
        }

        // I have taken more than 10ms :( There is something fishy !!
        if (elapsedTime > 10) {
            myLogger.warn("Method execution longer than 10 ms for " + className + "." + methodName + "." + "There is Something fishy !!");
        }
        return output;
    }


}

