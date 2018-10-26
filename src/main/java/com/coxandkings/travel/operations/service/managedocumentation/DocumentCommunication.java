package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.email.EmailResponse;

import java.util.List;
import java.util.Map;

public interface DocumentCommunication {

    EmailResponse sendEmailToClient(List<String> referenceIDs, String clientEmail, String subject, Map<String, String> dynamicVariables) throws OperationException;
}
