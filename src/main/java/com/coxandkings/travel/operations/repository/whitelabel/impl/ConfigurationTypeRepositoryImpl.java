package com.coxandkings.travel.operations.repository.whitelabel.impl;

import com.coxandkings.travel.operations.model.whitelabel.ConfigurationTypeEnumHandler;
import com.coxandkings.travel.operations.repository.whitelabel.ConfigurationTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ConfigurationTypeRepositoryImpl extends SimpleJpaRepository<ConfigurationTypeEnumHandler, String> implements ConfigurationTypeRepository  {

    public ConfigurationTypeRepositoryImpl(@Qualifier("opsEntityManager")EntityManager em){
        super(ConfigurationTypeEnumHandler.class, em);
    }

    @Override
    public ConfigurationTypeEnumHandler saveOrUpdate(ConfigurationTypeEnumHandler configurationTypeEnumHandler) {
        return this.saveAndFlush(configurationTypeEnumHandler);
    }

    @Override
    public ConfigurationTypeEnumHandler getWhiteLabelById(String id) {
        return findOne(id);
    }
}
