package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "prof_selling_Price")
public class ProfSellingPrice implements Serializable {
    //private double sellingPrice;
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @OneToOne(cascade = CascadeType.ALL)
    private SellingPrice sellingPrice;
    @OneToOne(cascade = CascadeType.ALL)
    private TotalInCompanyMarketCurrency totalInCompanyMarketCurrency;
    private BigDecimal discountsOffers;
    private boolean discountsOffersUpdated;
    private boolean comissionToClientUpdated;
    private BigDecimal comissionToClient;
    private BigDecimal totalNetSellingPrice;
    private boolean totalNSPriceUpdated;

    //new fields added for bug fix #15961
    private BigDecimal clientCommercialsPayable = BigDecimal.ZERO;
    private BigDecimal clientCommercialsReceivable = BigDecimal.ZERO;
    private boolean clientCRUpdated = false;
    private boolean clientCPUpdated = false;

    public BigDecimal getClientCommercialsPayable() {
        return clientCommercialsPayable;
    }

    public void setClientCommercialsPayable(BigDecimal clientCommercialsPayable) {
        this.clientCommercialsPayable = clientCommercialsPayable;
    }

    public boolean isClientCPUpdated() {
        return clientCPUpdated;
    }

    public void setClientCPUpdated(boolean clientCPUpdated) {
        this.clientCPUpdated = clientCPUpdated;
    }

    public BigDecimal getClientCommercialsReceivable() {
        return clientCommercialsReceivable;
    }

    public void setClientCommercialsReceivable(BigDecimal clientCommercialsReceivable) {
        this.clientCommercialsReceivable = clientCommercialsReceivable;
    }

    public boolean isClientCRUpdated() {
        return clientCRUpdated;
    }

    public void setClientCRUpdated(boolean clientCRUpdated) {
        this.clientCRUpdated = clientCRUpdated;
    }

    public boolean isTotalNSPriceUpdated() {
        return totalNSPriceUpdated;
    }

    public boolean isDiscountsOffersUpdated() {
        return discountsOffersUpdated;
    }

    public void setDiscountsOffersUpdated(boolean discountsOffersUpdated) {
        this.discountsOffersUpdated = discountsOffersUpdated;
    }

    public boolean isComissionToClientUpdated() {
        return comissionToClientUpdated;
    }

    public void setComissionToClientUpdated(boolean comissionToClientUpdated) {
        this.comissionToClientUpdated = comissionToClientUpdated;
    }

    public boolean getTotalNSPriceUpdated() {
        return totalNSPriceUpdated;
    }

    public void setTotalNSPriceUpdated(boolean totalNSPriceUpdated) {
        this.totalNSPriceUpdated = totalNSPriceUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SellingPrice getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(SellingPrice sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public TotalInCompanyMarketCurrency getTotalInCompanyMarketCurrency() {
        return totalInCompanyMarketCurrency;
    }

    public void setTotalInCompanyMarketCurrency(TotalInCompanyMarketCurrency totalInCompanyMarketCurrency) {
        this.totalInCompanyMarketCurrency = totalInCompanyMarketCurrency;
    }

    public BigDecimal getDiscountsOffers() {
        return discountsOffers;
    }

    public void setDiscountsOffers(BigDecimal discountsOffers) {
        this.discountsOffers = discountsOffers;
    }

    public BigDecimal getComissionToClient() {
        return comissionToClient;
    }

    public void setComissionToClient(BigDecimal comissionToClient) {
        this.comissionToClient = comissionToClient;
    }

    public BigDecimal getTotalNetSellingPrice() {
        return totalNetSellingPrice;
    }

    public void setTotalNetSellingPrice(BigDecimal totalNetSellingPrice) {
        this.totalNetSellingPrice = totalNetSellingPrice;
    }
}
