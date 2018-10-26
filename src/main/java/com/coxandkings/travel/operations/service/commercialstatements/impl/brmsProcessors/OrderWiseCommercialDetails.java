package com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialHeads;
import com.coxandkings.travel.operations.model.commercialstatements.PassengerDetails;

public class OrderWiseCommercialDetails {
	private String prodCateg;
	private String prodSubCateg;
	private String productName;
	private String market;
	private String currency;
	private List<Commercials> commercials=new ArrayList<Commercials>();
	private Set<PassengerDetails> paxDetails = new HashSet<PassengerDetails>();
	private String bookid;
	private String bookingDate;
	private int briIdx;
	private int prodIdx;
	
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingdate) {
		this.bookingDate = bookingdate;
	}
	public String getBookid() {
		return bookid;
	}
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	public List<Commercials> getCommercials() {
		return commercials;
	}
	public void setCommercials(List<Commercials> commercials) {
		this.commercials = commercials;
	}
	public String getProdCateg() {
		return prodCateg;
	}
	public void setProdCateg(String prodCateg) {
		this.prodCateg = prodCateg;
	}
	public String getProdSubCateg() {
		return prodSubCateg;
	}
	public void setProdSubCateg(String prodSubCateg) {
		this.prodSubCateg = prodSubCateg;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Set<PassengerDetails> getPaxDetails() {
		return paxDetails;
	}
	public void setPaxDetails(Set<PassengerDetails> paxDetails) {
		this.paxDetails = paxDetails;
	}
	public int getBriIdx() {
		return briIdx;
	}
	public void setBriIdx(int briIdx) {
		this.briIdx = briIdx;
	}
	public int getProdIdx() {
		return prodIdx;
	}
	public void setProdIdx(int prodIdx) {
		this.prodIdx = prodIdx;
	}
	public Commercials getCommercial(CommercialHeads commHead,String commType) {
		if(commHead==null)
			return null;
		for(Commercials commercial:commercials) {
			if(commHead == CommercialHeads.forBRMSString(commercial.getCommercialName()) && commercial.getCommercialType().equals(commType))
				return commercial;
		}
		return null;
	}
	@Override
	public String toString() {
		return "OrderDetails [prodCateg=" + prodCateg + ", prodSubCateg=" + prodSubCateg + ", productName="
				+ productName + "]";
	}
	
	public static class Commercials{
		private String commercialType;
		private String commercialName;
		private BigDecimal commercialAmount;
		public String getCommercialType() {
			return commercialType;
		}
		public void setCommercialType(String commercialType) {
			this.commercialType = commercialType;
		}
		public String getCommercialName() {
			return commercialName;
		}
		public void setCommercialName(String commercialName) {
			this.commercialName = commercialName;
		}
		
		public BigDecimal getCommercialAmount() {
			return commercialAmount;
		}
		public void setCommercialAmount(BigDecimal commercialAmount) {
			this.commercialAmount = commercialAmount;
		}
		@Override
		public String toString() {
			return "Commercials [commercialType=" + commercialType + ", commercialName=" + commercialName
					+ ", commercialAmount=" + commercialAmount + "]";
		}
	}
	
	
}
