package com.coxandkings.travel.operations.controller.whitelable;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.FooterDetail;
import com.coxandkings.travel.operations.model.whitelabel.HeaderDetail;
import com.coxandkings.travel.operations.model.whitelabel.MarketingSetupStandardDetail;
import com.coxandkings.travel.operations.model.whitelabel.PaymentInformationStandardDetail;
import com.coxandkings.travel.operations.resource.whitelabel.HeaderFooterDetailResource;
import com.coxandkings.travel.operations.resource.whitelabel.MarketingSetupParameterResource;
import com.coxandkings.travel.operations.resource.whitelabel.PaymentInformationResource;
import com.coxandkings.travel.operations.service.whitelabel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/whiteLabels/{id}/fileSetUp")
@CrossOrigin(origins = "*")
public class WhiteLabelFileController {

    @Autowired
    private FileSetupService fileSetUpService;

    @Autowired
    private WhiteLabelService whiteLabelService;

    @Autowired
    private WhiteLabelPaymentInformationService whiteLabelPaymentInformationService;


    @Autowired
    private WhiteLabelMarketingSetupService whiteLabelMarketingSetupService;


    @Autowired
    private WhiteLabelHeaderFooterService whiteLabelHeaderFooterService;

    @PostMapping("/uploadFile/headerFile")
    public HttpEntity<HeaderDetail> uploadHeaderFile(@PathVariable("id") String id,
         @RequestParam( value ="headerId", required=false) String headerId, @RequestParam(value = "file") MultipartFile file) throws OperationException {

        HeaderFooterDetailResource headerFooterDetailResource =  new HeaderFooterDetailResource();
        headerFooterDetailResource.setWhiteLabelId(id);
        headerFooterDetailResource.setId(headerId);

        return new ResponseEntity<HeaderDetail>(whiteLabelHeaderFooterService.createOrUpdateHeaderDetail(headerFooterDetailResource,file), HttpStatus.OK);
    }

    @PostMapping("/uploadFile/footerFile")
    public HttpEntity<FooterDetail> uploadFooterFiles(@PathVariable("id") String id,
            @RequestParam( value ="footerId", required=false) String footerId,
            @RequestParam(value = "file") MultipartFile file) throws OperationException {

        HeaderFooterDetailResource headerFooterDetailResource =  new HeaderFooterDetailResource();
        headerFooterDetailResource.setWhiteLabelId(id);
        headerFooterDetailResource.setId(footerId);

        return new ResponseEntity<FooterDetail>(whiteLabelHeaderFooterService.createOrUpdateFooterDetail(headerFooterDetailResource,
                file), HttpStatus.OK);
    }

    @PostMapping("/uploadFile/paymentKeyFile")
    public HttpEntity<PaymentInformationStandardDetail> uploadPaymentKeyFile(@PathVariable("id") String id,
                @RequestParam( value ="paymentInformationId", required=false) String paymentInformationId,
                @RequestParam(value = "file") MultipartFile file) throws OperationException {

        PaymentInformationResource paymentInformationResource = new PaymentInformationResource();
        paymentInformationResource.setWhiteLabelId(id);
        paymentInformationResource.setId(paymentInformationId);

        return new ResponseEntity<PaymentInformationStandardDetail>(
                whiteLabelPaymentInformationService.createOrUpdatePaymentInformation(paymentInformationResource, file), HttpStatus.OK);
    }

    @PostMapping("/uploadFile/marketingSetupFile")
    public HttpEntity<MarketingSetupStandardDetail> uploadMarketingSetupFile(@PathVariable("id") String id,
            @RequestParam(value ="marketingSetupId", required=false) String marketingSetupId,
            @RequestParam(value = "file") MultipartFile file) throws OperationException {

        MarketingSetupParameterResource marketingSetupParameterResource =  new MarketingSetupParameterResource();
        marketingSetupParameterResource.setWhiteLabelId(id);
        marketingSetupParameterResource.setId(marketingSetupId);

        return new ResponseEntity<MarketingSetupStandardDetail>(
                whiteLabelMarketingSetupService.createOrUpdateMarketingSetupParameter(marketingSetupParameterResource , file), HttpStatus.OK);
    }

//    @GetMapping("/downloadFile")
//    public void downloadFile(@RequestParam("path") String path, HttpServletResponse response) throws OperationException,
//            /*RepositoryException,*/ IOException {
//        InputStream is = null;
//        long fileSize = 0;
//        try {
//            is = marketingFileService.downloadFile(path);
//            response.setContentType("application/text");
//            //response.setContentLengthLong(fileSize);
//            response.setHeader("Content-Disposition", "attachment;filename=" + path.substring(path.lastIndexOf("/")+1));
//            fileSize = getOutPutStreamFromInputStream(is, response.getOutputStream());
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//            if (response.getOutputStream() != null) {
//                response.getOutputStream().close();
//            }
//        }
//        response.flushBuffer();
//    }

    private long getOutPutStreamFromInputStream(InputStream is, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        long count = 0;
        int n = 0;
        while (-1 != (n = is.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


}
