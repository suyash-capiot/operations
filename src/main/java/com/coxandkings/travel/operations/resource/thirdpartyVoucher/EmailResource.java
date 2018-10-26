package com.coxandkings.travel.operations.resource.thirdpartyVoucher;

import com.coxandkings.travel.operations.enums.email.EmailPriority;

import java.util.Map;

public class EmailResource {

    private Map<String, String> dynamicVariables;
    private String toMail;
    private String subject;
    private String process;
    private String function;
    private String scenario;
    private EmailPriority emailPriority;

    public Map<String, String> getDynamicVariables() {
        return dynamicVariables;
    }

    public void setDynamicVariables(Map<String, String> dynamicVariables) {
        this.dynamicVariables = dynamicVariables;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public EmailPriority getEmailPriority() {
        return emailPriority;
    }

    public void setEmailPriority(EmailPriority emailPriority) {
        this.emailPriority = emailPriority;
    }
}
