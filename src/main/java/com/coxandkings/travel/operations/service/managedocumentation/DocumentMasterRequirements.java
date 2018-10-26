package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentHandlingGrid;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;

import java.util.List;

public interface DocumentMasterRequirements {

    public List<DocumentHandlingGrid> getDocumentHandlingGridDetails(String criteria) throws OperationException;

    public List<DocumentSetting> getDocumentSettingDetails(String criteria) throws OperationException;

}
