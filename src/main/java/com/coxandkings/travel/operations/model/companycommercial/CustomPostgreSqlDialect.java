package com.coxandkings.travel.operations.model.companycommercial;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

public class CustomPostgreSqlDialect extends PostgreSQL94Dialect {

    public CustomPostgreSqlDialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "json");

    }
}
