package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentHandlingGrid;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.DocumentSetting;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentMasterRequirements;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class DocumentMasterRequirementsImpl implements DocumentMasterRequirements {

    @Value("${manage-documentation.get-document-handling-master}")
    private String documentMasterUrl;

    @Value(value = "${manage-documentation.path_expression.document_handling_grid}")
    private String getMDMDocumentHandlingGridData;

    @Value(value = "${manage-documentation.path_expression.document_setting}")
    private String getMDMDocumentSettingData;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Override
    public List<DocumentHandlingGrid> getDocumentHandlingGridDetails(String criteria) throws OperationException {
        if (criteria != null) {
            URI uri = UriComponentsBuilder.fromUriString(documentMasterUrl + criteria).build().encode().toUri();
            String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            List<DocumentHandlingGrid> documentHandlingGrids = jsonObjectProvider.getChildrenCollection(response, getMDMDocumentHandlingGridData, DocumentHandlingGrid.class);
            return documentHandlingGrids;
        } else
            throw new OperationException("Failed to search document handling master details");
    }

    @Override
    public List<DocumentSetting> getDocumentSettingDetails(String criteria) throws OperationException {
        if (criteria != null) {
            URI uri = UriComponentsBuilder.fromUriString(documentMasterUrl + criteria).build().encode().toUri();
            String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            List<DocumentSetting> documentSettingList = jsonObjectProvider.getChildrenCollection(response, getMDMDocumentSettingData, DocumentSetting.class);
            return documentSettingList;
        } else
            throw new OperationException("Failed to search document handling master details");
    }

}
