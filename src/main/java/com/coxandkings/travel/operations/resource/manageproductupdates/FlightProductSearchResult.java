package com.coxandkings.travel.operations.resource.manageproductupdates;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "numberOfPages",
        "productUpdateFlightInfo"
})
public class FlightProductSearchResult {

    @JsonProperty("numberOfPages")
    private Integer numberOfPages;
    @JsonProperty("productUpdateFlightInfo")
    private List<ProductUpdateFlightInfo> productUpdateFlightInfo = null;

    @JsonProperty("numberOfPages")
    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    @JsonProperty("numberOfPages")
    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @JsonProperty("productUpdateFlightInfo")
    public List<ProductUpdateFlightInfo> getProductUpdateFlightInfo() {
        return productUpdateFlightInfo;
    }

    @JsonProperty("productUpdateFlightInfo")
    public void setProductUpdateFlightInfo(List<ProductUpdateFlightInfo> productUpdateFlightInfo) {
        this.productUpdateFlightInfo = productUpdateFlightInfo;
    }
}