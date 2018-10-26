package com.coxandkings.travel.operations.resource.referal;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class ReferalIdGenerator implements IdentifierGenerator {

    public int generateCustId() {
        Random random = new Random();
        return random.nextInt();
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return "CL_" + this.generateCustId();
    }
}
