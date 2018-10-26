package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.service.commercialstatements.impl.Constants;

@Component
public class SlabTypeCalculatorFactory implements Constants{

	@Autowired
	private AccoSlabTypeCalculator accoCalc;
	
	@Autowired
	private AirSlabTypeCalculator airCalc;
	
	public SlabTypeCalculator getInstance(String prodCateg,String prodSubCateg) {
		//In switch case enums cannot be defined as a case. Thus used if else
		if(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory().equalsIgnoreCase(prodCateg))
			return accoCalc;
		else if(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory().equalsIgnoreCase(prodSubCateg))
			return airCalc;
		else
			return null;
		
		/*String switchkey;
		if (OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory().equalsIgnoreCase(prodCateg))//match this for category "other products" too
			switchkey = prodSubCateg;
		else
			switchkey = prodCateg;
		switch (switchkey) {
		case "Accommodation":
			return accoCalc;
		case "Flight":
			return airCalc;
		default:
			return null;
		}*/
		
	}
}
