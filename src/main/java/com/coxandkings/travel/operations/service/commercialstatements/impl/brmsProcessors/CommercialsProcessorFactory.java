package com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;


@Component
public class CommercialsProcessorFactory {
	
	@Autowired
	private AccoSupplierTransactionalProcessor accoSuppPrcsr;
	
	@Autowired
	private AirSupplierTransactionalProcessor airSuppPrcsr;

	public BaseCommercialsProcessor getProcessor(String prodCateg,String prodCategSubType,CommercialStatementFor commFor) {
		//TODO:use enums ,test for mull. Take commercial head too
		String switchkey;
		if ("ACCOMMODATION".equalsIgnoreCase(prodCateg))
			switchkey = prodCateg;
		else
			switchkey = prodCategSubType;
		switch (switchkey) {
		case "Accommodation":{
			switch(commFor){
			case SUPPLIER:
				return accoSuppPrcsr;
			case CLIENT:
				return null;
			}
		}
		case "Flight":{
			switch(commFor){
			case SUPPLIER:
				return airSuppPrcsr;
			case CLIENT:
				return null;
			}
		}
		
	}
		return null;
	}


}
