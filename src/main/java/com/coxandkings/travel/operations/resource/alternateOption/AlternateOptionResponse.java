package com.coxandkings.travel.operations.resource.alternateOption;

import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;

import java.util.List;

public class AlternateOptionResponse {
    long count;
    List<AlternateOptions> alternateOptions;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<AlternateOptions> getAlternateOptions() {
        return alternateOptions;
    }

    public void setAlternateOptions(List<AlternateOptions> alternateOptions) {
        this.alternateOptions = alternateOptions;
    }
}
