package com.coxandkings.travel.operations.service.offerDetails;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONArray;

public interface OfferDetailsService {
    JSONArray getVoucherCodeDetails(String offersCode)throws OperationException;
}
