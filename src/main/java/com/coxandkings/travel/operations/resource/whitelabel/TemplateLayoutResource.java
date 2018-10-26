package com.coxandkings.travel.operations.resource.whitelabel;

import com.coxandkings.travel.operations.resource.BaseResource;

public class TemplateLayoutResource extends BaseResource {

        private String templateLayoutId;

        private String templateTypeId;

        private String whiteLabelId;

        public String getTemplateLayoutId() {
            return templateLayoutId;
        }

        public void setTemplateLayoutId(String templateLayoutId) {
            this.templateLayoutId = templateLayoutId;
        }

        public String getTemplateTypeId() {
            return templateTypeId;
        }

        public void setTemplateTypeId(String templateTypeId) {
            this.templateTypeId = templateTypeId;
        }

        public String getWhiteLabelId() {
            return whiteLabelId;
        }

        public void setWhiteLabelId(String whiteLabelId) {
            this.whiteLabelId = whiteLabelId;
        }
}
