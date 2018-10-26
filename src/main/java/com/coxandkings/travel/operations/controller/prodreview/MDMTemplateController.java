package com.coxandkings.travel.operations.controller.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.TemplateCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.prodreview.MailTemplateResource;
import com.coxandkings.travel.operations.resource.prodreview.ProductTemplateReferenceResource;
import com.coxandkings.travel.operations.resource.prodreview.mdmtemplateresource.MDMTemplateResource;
import com.coxandkings.travel.operations.service.prodreview.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mdmTemplates")
@CrossOrigin(origins = "*")
public class MDMTemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     *
     * @param templateId is unique reference number
     * @return
     * @throws OperationException
     */
    @GetMapping("/v1/template/{templateId}")
    private ResponseEntity<MDMTemplateResource> getMDMTemplate(@PathVariable(name = "templateId") String templateId) throws OperationException {
        MDMTemplateResource mdmTemplateResource = templateService.getMDMTemplate(templateId);
        return new ResponseEntity<>(mdmTemplateResource, HttpStatus.OK);
    }

    @PostMapping("/v1/sendMail")
    private ResponseEntity<Map> sendMailToClient(@RequestBody MailTemplateResource mailTemplateResource) throws OperationException {
        return new ResponseEntity<>(templateService.sendMailToClient(mailTemplateResource), HttpStatus.OK);
    }

    @PostMapping("/v1/searchTemplates")
    private ResponseEntity<List<ProductTemplateReferenceResource>> searchTemplates(@RequestBody TemplateCriteria templateCriteria) throws OperationException {
        List<ProductTemplateReferenceResource> productTemplateReferenceResource = templateService.searchTemplateByCriteria(templateCriteria);
        return new ResponseEntity<>(productTemplateReferenceResource, HttpStatus.OK);
    }
}
