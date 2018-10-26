package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.FooterDetail;
import com.coxandkings.travel.operations.model.whitelabel.HeaderDetail;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import com.coxandkings.travel.operations.resource.whitelabel.HeaderFooterDetailResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface WhiteLabelHeaderFooterService {

    HeaderDetail createOrUpdateHeaderDetail(
            HeaderFooterDetailResource headerFooterDetailResource, MultipartFile file) throws OperationException;

    WhiteLabel deleteHeaderDetails(HeaderFooterDetailResource headerFooterDetailResource) throws OperationException;


    FooterDetail createOrUpdateFooterDetail(
            HeaderFooterDetailResource headerFooterDetailResource, MultipartFile file) throws OperationException;

    WhiteLabel deleteFooterDetails(HeaderFooterDetailResource headerFooterDetailResource) throws OperationException;

}
