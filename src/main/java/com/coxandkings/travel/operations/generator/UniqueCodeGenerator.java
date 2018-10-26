package com.coxandkings.travel.operations.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.UUID;

@Component
public class UniqueCodeGenerator implements IdentifierGenerator {

//    public static int ucgIncrementer=1;

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

        Connection connection = session.connection();
        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(master_room_id) as id FROM operations_schema.manage_mail_room;");

            String prefix = "MR-";
            String pattern = "0000000000";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            int ucgIncrementer = 1;

            if(resultSet.next()) {//Every time it gets a count it increments it by 1
               ucgIncrementer = resultSet.getInt(1)+1;
            }

            //If it doesn't get a value in return then it will save it as MR-0000000001 and that will be the first value fed into the table
            System.out.println("After formatting the value is " + decimalFormat.format(ucgIncrementer));
            StringBuffer code = new StringBuffer();
            code.append(prefix).append(decimalFormat.format(ucgIncrementer));
            System.out.println(" code after creation is " + code.toString());
            return code.toString();

        }
        catch(Exception e){
                return null;
        }
    }
}
