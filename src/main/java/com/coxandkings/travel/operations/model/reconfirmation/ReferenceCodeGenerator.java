package com.coxandkings.travel.operations.model.reconfirmation;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ReferenceCodeGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return "RC"+String.valueOf(System.currentTimeMillis());
    }
}
