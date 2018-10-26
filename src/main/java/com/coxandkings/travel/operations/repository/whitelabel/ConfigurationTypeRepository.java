package com.coxandkings.travel.operations.repository.whitelabel;

import com.coxandkings.travel.operations.model.whitelabel.ConfigurationTypeEnumHandler;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationTypeRepository {
    public ConfigurationTypeEnumHandler saveOrUpdate(ConfigurationTypeEnumHandler configurationTypeEnumHandler);
    public ConfigurationTypeEnumHandler getWhiteLabelById(String id);
}
