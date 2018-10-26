package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.service.whitelabel.FileSetupService;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileSetupServiceImpl implements FileSetupService {

    private static String UPLOADED_FOLDER = "/opt/app-root/uploaded";

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    public static final int FILE_CONTENT_INDEX = 0;

    public static final int FILE_PATH_INDEX = 1;


    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Override
    public List<String> uploadMultipleFiles(MultipartFile file, Boolean fileContentRequired) throws OperationException {

        Path path = null;
        ArrayList<String> fileData = new ArrayList<>();
        try {
            byte[] bytes = file.getBytes();

            if(fileContentRequired) {
                String content  = new String(file.getBytes(), "UTF-8");
                fileData.add(content.substring(FILE_CONTENT_INDEX));
            }else if(bytes.length == 0) {
                fileData.add(" ");
            }else{
                fileData.add("File content not Saved");
            }

            path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            fileData.add(path.toString());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileData;
    }

    @Override
    public InputStream downloadFile(String path) throws OperationException {
//        TODO : Integrate it with DocumentManagerUtil
//        InputStream is = DocumentManagerUtil.downloadDocument(documentService, "otherWorkspace", path);
//        return is;
        return null;

    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
