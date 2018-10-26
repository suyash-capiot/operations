package com.coxandkings.travel.operations.resource.notification;

import java.util.List;

public class MDMResponse {
    private List<AlertNotificationResource> data;

    public List<AlertNotificationResource> getData() {
        return data;
    }

    public void setData(List<AlertNotificationResource> data) {
        this.data = data;
    }
}
