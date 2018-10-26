package com.coxandkings.travel.operations.response.prodreview;

import com.coxandkings.travel.operations.resource.prodreview.mdmtemplateresource.MDMTemplateResource;

import java.util.List;

public class MDMTemplateResponse {
    List<MDMTemplateResource> data;

    public List<MDMTemplateResource> getData() {
        return data;
    }

    public void setData(List<MDMTemplateResource> data) {
        this.data = data;
    }
}
