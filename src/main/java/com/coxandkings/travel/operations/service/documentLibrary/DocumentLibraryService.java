package com.coxandkings.travel.operations.service.documentLibrary;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.documentLibrary.*;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DocumentLibraryService {
    DocumentReferenceResource create(MultipartFile file, NewDocumentResource newDocumentResource, InputStream is) throws RepositoryException, OperationException;

    DownloadableDocumentResource get(String docId, Integer docVersion) throws RepositoryException, OperationException;

    List<NewDocumentResource> search(DocumentSearchResource searchRequest) throws RepositoryException;

    List<DocumentResource> getDocuments(List<DocumentSearchResource> searchResources) throws RepositoryException, IOException;

    List<JSONObject> getMetaData(List<DocumentSearchResource> searchResources) throws RepositoryException;
    
    DocumentReferenceResource createV2(MultipartFile file, NewDocumentResource newDocumentResource, InputStream is) throws OperationException;

    DownloadableDocumentResource getV2(String docId, Integer docVersion) throws OperationException;

    List<NewDocumentResource> searchV2(DocumentSearchResource searchRequest) throws OperationException;

    List<DocumentResource> getDocumentsV2(List<DocumentSearchResource> searchResources) throws OperationException;

    List<JSONObject> getMetaDataV2(List<DocumentSearchResource> searchResources) throws OperationException;
    
}

