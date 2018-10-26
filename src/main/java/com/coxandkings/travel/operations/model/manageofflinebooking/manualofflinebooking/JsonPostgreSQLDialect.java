package com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

public class JsonPostgreSQLDialect extends PostgreSQL94Dialect{

    public JsonPostgreSQLDialect() {

        super();

        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
        registerHibernateType( Types.OTHER, StandardBasicTypes.CLASS.getName() );
    }

}
