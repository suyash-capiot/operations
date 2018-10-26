package com.coxandkings.travel.operations.model.managearrivallist;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "ArrivalListInfo")
public class ArrivalListInfo extends BaseModel
{

    @Column(name = "alertDefinitionID")
    private String alertDefinitionId;

    @Column(name = "generatedDateTime")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime generatedDateTime;

    @Column(name = "productCategorySubType")
    private String productCategorySubType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "general_arrival_id")
    private List<GeneralArrivalListItem> generalArrivalListItem;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_List_Accomodation_id")
    private List<AccommodationArrivalListItem> arrivalListAccomodationItems;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_List_Flight_id")
    private List<FlightArrivalListItem> flightArrivalListItems;


    public String getAlertDefinitionId() {
        return alertDefinitionId;
    }

    public void setAlertDefinitionId(String alertDefinitionId) {
        this.alertDefinitionId = alertDefinitionId;
    }

    public ZonedDateTime getGeneratedDateTime() {
        return generatedDateTime;
    }

    public void setGeneratedDateTime(ZonedDateTime generatedDateTime) {
        this.generatedDateTime = generatedDateTime;
    }

    public List<GeneralArrivalListItem> getGeneralArrivalListItem() {
        return generalArrivalListItem;
    }

    public void setGeneralArrivalListItem(List<GeneralArrivalListItem> generalArrivalListItem) {
        this.generalArrivalListItem = generalArrivalListItem;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public List<AccommodationArrivalListItem> getArrivalListAccomodationItems() {
        return arrivalListAccomodationItems;
    }

    public void setArrivalListAccomodationItems(List<AccommodationArrivalListItem> arrivalListAccomodationItems) {
        this.arrivalListAccomodationItems = arrivalListAccomodationItems;
    }

    public List<FlightArrivalListItem> getFlightArrivalListItems() {
        return flightArrivalListItems;
    }

    public void setFlightArrivalListItems(List<FlightArrivalListItem> flightArrivalListItems) {
        this.flightArrivalListItems = flightArrivalListItems;
    }
}
