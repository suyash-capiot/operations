package com.coxandkings.travel.operations.model.forex.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class IndentIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object object)
            throws HibernateException {

        String prefix = "INDT-";
        String generatedId = prefix + gen().toString();
        return generatedId;

    }

    public Long gen() {
        Long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return number;
    }


}
