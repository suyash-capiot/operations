package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public interface FileSetupService {

    public List<String> uploadMultipleFiles(MultipartFile file, Boolean fileContentRequired) throws OperationException;

    public InputStream downloadFile(String path) throws OperationException ;

}
