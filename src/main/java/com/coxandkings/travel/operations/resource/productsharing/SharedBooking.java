package com.coxandkings.travel.operations.resource.productsharing;

public class SharedBooking {

    private String bookingRefNo;
    private String hotelName;
    private String city;
    private String country;
    private String numberOfDays;
    private String numberOfNights;
    private String luxuryType;
    private String roomCapacity;
    private String checkInDate;
    private String checkOutDate;
    private String genderPreference;
    private String orderId;
    private String roomId;
    private String paxId;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String contactInfoCountryCode;

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(String numberOfNights) {
        this.numberOfNights = numberOfNights;
    }


    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPaxId() {
        return paxId;
    }

    public void setPaxId(String paxId) {
        this.paxId = paxId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactInfoCountryCode() {
        return contactInfoCountryCode;
    }

    public void setContactInfoCountryCode(String contactInfoCountryCode) {
        this.contactInfoCountryCode = contactInfoCountryCode;
    }

    public void setLuxuryType(String luxuryType) {
        this.luxuryType = luxuryType;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getLuxuryType() {
        return luxuryType;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    @Override
    public String toString() {
        return "SharedBooking{" +
                "bookingRefNo='" + bookingRefNo + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", numberOfDays='" + numberOfDays + '\'' +
                ", numberOfNights='" + numberOfNights + '\'' +
                ", luxuryType='" + luxuryType + '\'' +
                ", roomCapacity='" + roomCapacity + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", genderPreference='" + genderPreference + '\'' +
                ", orderId='" + orderId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", paxId='" + paxId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                ", contactInfoCountryCode='" + contactInfoCountryCode + '\'' +
                '}';
    }
}
