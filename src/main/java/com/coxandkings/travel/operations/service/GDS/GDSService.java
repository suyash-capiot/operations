package com.coxandkings.travel.operations.service.GDS;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONObject;

public interface GDSService {

    String sendCommunication(JSONObject obj) throws OperationException;
}
