package com.coxandkings.travel.operations.service.reconfirmation.common;

/**
 *
 */
public class CustomClientDetails {

    private String emailId;
    private String firstName;
    private String lastName;

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     *
     * @param emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
