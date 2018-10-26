package com.coxandkings.travel.operations.service.whitelabel.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.FooterDetail;
import com.coxandkings.travel.operations.model.whitelabel.HeaderDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import com.coxandkings.travel.operations.repository.whitelabel.WhiteLabelRepository;
import com.coxandkings.travel.operations.resource.whitelabel.HeaderFooterDetailResource;
import com.coxandkings.travel.operations.service.whitelabel.FileSetupService;
import com.coxandkings.travel.operations.service.whitelabel.WhiteLabelHeaderFooterService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("WhiteLabelHeaderFooter")
public class WhiteLabelHeaderFooterServiceImpl implements WhiteLabelHeaderFooterService {

    private static Logger log = LogManager.getLogger(WhiteLabelServiceImpl.class);

    @Autowired
    WhiteLabelRepository whiteLabelRepository;

    @Autowired
    ConfigurationTypeRepository configurationTypeRepository;

    @Autowired
    private FileSetupService fileSetUpService;

    public static final int FILE_CONTENT_INDEX = 0;

    public static final int FILE_PATH_INDEX = 1;


    @Override
    public HeaderDetail createOrUpdateHeaderDetail(HeaderFooterDetailResource headerFooterDetailResource,
                                                   MultipartFile file) throws OperationException {
        WhiteLabel existingWhiteLabel = null;
        try {
            String whiteLabelTemplateId = headerFooterDetailResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if (!StringUtils.isEmpty(headerFooterDetailResource.getId())) {
                if (log.isDebugEnabled()) {
                    log.debug("HeaderFooterDetail not found for ResourceId:" + headerFooterDetailResource.getId());
                }
                if (log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                HeaderDetail  updateHeaderDetail = null;
                Set<HeaderDetail> headerDetails = existingWhiteLabel.getHeaderDetail();
                if (headerDetails != null) {
                    Optional<HeaderDetail> headerDetail = headerDetails.stream()
                            .filter(headerDetailMatch -> headerDetailMatch.getId()
                                    .equalsIgnoreCase(headerFooterDetailResource.getId())).findFirst();

                    if (headerDetail != null) {
                        updateHeaderDetail = headerDetail.get();
                        if(file != null) {
                            List<String> filePath = fileSetUpService.uploadMultipleFiles(file, true);
                            updateHeaderDetail.setContent(filePath.get(FILE_CONTENT_INDEX));
                            updateHeaderDetail.setFilePath(filePath.get(FILE_PATH_INDEX));
                        }else {
                            CopyUtils.copy(headerFooterDetailResource, updateHeaderDetail);
                        }

                    } else {
                        throw new OperationException("headerDetail not found for id" + headerFooterDetailResource.getId());
                    }
                }

                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateHeaderDetail;
            } else {
                HeaderDetail headerDetail = new HeaderDetail();

                if(file != null) {
                    List<String> filePath = fileSetUpService.uploadMultipleFiles(file, true);
                    headerDetail.setContent(filePath.get(FILE_CONTENT_INDEX));
                    headerDetail.setFilePath(filePath.get(FILE_PATH_INDEX));
                }else {
                    CopyUtils.copy(headerFooterDetailResource, headerDetail);
                }

                headerDetail.setWhiteLabel(existingWhiteLabel);

                Set<HeaderDetail> oldHeaderDetails = new HashSet<>();

                if (existingWhiteLabel.getHeaderDetail() ==  null) {
                    oldHeaderDetails.add(headerDetail);
                    existingWhiteLabel.setHeaderDetail(oldHeaderDetails);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getHeaderDetail().iterator().next();
                }
                else{
                    oldHeaderDetails.addAll(existingWhiteLabel.getHeaderDetail());
                    existingWhiteLabel.getHeaderDetail().add(headerDetail);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);

                    Set<HeaderDetail> newHeaderDetails = new HashSet<>();
                    newHeaderDetails.addAll(existingWhiteLabel.getHeaderDetail());

                    newHeaderDetails.removeAll(oldHeaderDetails);
                    return newHeaderDetails.iterator().next();
                }
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public WhiteLabel deleteHeaderDetails(HeaderFooterDetailResource headerFooterDetailResource) throws OperationException {
        WhiteLabel existingWhiteLabel =  null;
        String whiteLabelTemplateId = headerFooterDetailResource.getWhiteLabelId();
        existingWhiteLabel = whiteLabelTemplateId != null ?
                whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

        try {
            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(headerFooterDetailResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("HeaderFooterDetail Resource Id:" + headerFooterDetailResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                HeaderDetail deleteHeaderDetail =  null;
                Set<HeaderDetail> headerDetailSet = existingWhiteLabel.getHeaderDetail();

                if(headerDetailSet != null) {
                    Optional<HeaderDetail> headerDetail = headerDetailSet.stream()
                            .filter(headerDetailMatch -> headerDetailMatch.getId()
                                    .equalsIgnoreCase(headerFooterDetailResource.getId())).findFirst();

                    if(headerDetail.isPresent()) {
                        deleteHeaderDetail = headerDetail.get();
                        headerDetailSet.remove(deleteHeaderDetail);
                    }else {
                        throw new Exception("Header Detail not found for id "
                                + headerFooterDetailResource.getId());
                    }
                }

                WhiteLabel whiteLabel = whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return whiteLabel;
            }
            else{
                throw new OperationException("HeaderSchemeId is parameter is empty for "
                        + headerFooterDetailResource.getId());
            }

        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    @Override
    public FooterDetail createOrUpdateFooterDetail(
            HeaderFooterDetailResource headerFooterDetailResource,MultipartFile file) throws OperationException{
        WhiteLabel existingWhiteLabel = null;
        try
        {
            String whiteLabelTemplateId = headerFooterDetailResource.getWhiteLabelId();
            existingWhiteLabel = whiteLabelTemplateId != null ?
                    whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

            if(existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }


            if( !StringUtils.isEmpty(headerFooterDetailResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("FooterDetail not found for ResourceId:" + headerFooterDetailResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                FooterDetail updateFooterDetail = null;
                Set<FooterDetail> footerDetails = existingWhiteLabel.getFooterDetail();
                if (footerDetails != null) {
                    Optional<FooterDetail> footerDetail = footerDetails.stream()
                            .filter(footerDetailMatch -> footerDetailMatch.getId()
                                    .equalsIgnoreCase(headerFooterDetailResource.getId())).findFirst();

                    if (footerDetail != null) {
                        updateFooterDetail = footerDetail.get();
                        if(file != null) {
                            List<String> filePath = fileSetUpService.uploadMultipleFiles(file, true);
                            updateFooterDetail.setContent(filePath.get(FILE_CONTENT_INDEX));
                            updateFooterDetail.setFilePath(filePath.get(FILE_PATH_INDEX));
                        }else {
                            CopyUtils.copy(headerFooterDetailResource, updateFooterDetail);
                        }
                    } else {
                        throw new OperationException("FooterDetail not found for id" + headerFooterDetailResource.getId());
                    }

                }
                whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return updateFooterDetail;
            } else {
                FooterDetail footerDetail = new FooterDetail();

                if(file != null) {
                    List<String> filePath = fileSetUpService.uploadMultipleFiles(file, true);
                    footerDetail.setContent(filePath.get(FILE_CONTENT_INDEX));
                    footerDetail.setFilePath(filePath.get(FILE_PATH_INDEX));
                }else {
                    CopyUtils.copy(headerFooterDetailResource, footerDetail);
                }
                footerDetail.setWhiteLabel(existingWhiteLabel);

                Set<FooterDetail> oldFooterDetails = new HashSet<>();
                if (existingWhiteLabel.getFooterDetail() == null) {
                    oldFooterDetails.add(footerDetail);
                    existingWhiteLabel.setFooterDetail(oldFooterDetails);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                    return existingWhiteLabel.getFooterDetail().iterator().next();
                } else {
                    oldFooterDetails.addAll(existingWhiteLabel.getFooterDetail());
                    existingWhiteLabel.getFooterDetail().add(footerDetail);
                    whiteLabelRepository.saveOrUpdate(existingWhiteLabel);

                    Set<FooterDetail> newFooterDetail = new HashSet<>();
                    newFooterDetail.addAll(existingWhiteLabel.getFooterDetail());
                    newFooterDetail.removeAll(oldFooterDetails);

                    return newFooterDetail.iterator().next();
                }
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public WhiteLabel deleteFooterDetails(HeaderFooterDetailResource headerFooterDetailResource) throws OperationException {
        WhiteLabel existingWhiteLabel =  null;
        String whiteLabelTemplateId = headerFooterDetailResource.getWhiteLabelId();
        existingWhiteLabel = whiteLabelTemplateId != null ?
                whiteLabelRepository.getWhiteLabelById(whiteLabelTemplateId) : null;

        try {
            if (existingWhiteLabel == null) {
                throw new OperationException("WhiteLabelTemplateId not found with id" + existingWhiteLabel.getId());
            }

            if( !StringUtils.isEmpty(headerFooterDetailResource.getId()) ) {
                if(log.isDebugEnabled()) {
                    log.debug("HeaderFooterDetail Resource Id:" + headerFooterDetailResource.getId());
                }
                if(log.isDebugEnabled()) {
                    log.debug("Existing WhiteLabelSetUp Details:" + existingWhiteLabel);
                }

                FooterDetail deleteFooterDetail =  null;
                Set<FooterDetail> footerDetailSet = existingWhiteLabel.getFooterDetail();

                if(footerDetailSet != null) {
                    Optional<FooterDetail> footerDetail = footerDetailSet.stream()
                            .filter(footerDetailMatch -> footerDetailMatch.getId()
                                    .equalsIgnoreCase(headerFooterDetailResource.getId())).findFirst();

                    if(footerDetail.isPresent()) {
                        deleteFooterDetail = footerDetail.get();
                        footerDetailSet.remove(deleteFooterDetail);
                    }else {
                        throw new OperationException("Footer Detail not found for id "
                                + headerFooterDetailResource.getId());
                    }
                }

                WhiteLabel whiteLabel = whiteLabelRepository.saveOrUpdate(existingWhiteLabel);
                return whiteLabel;
            }
            else{
                throw new OperationException("SchemeId is parameter is empty for "
                        + headerFooterDetailResource.getId());
            }

        } catch (OperationException e) {
            throw new OperationException(Constants.ER01);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
