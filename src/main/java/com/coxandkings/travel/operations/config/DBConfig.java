package com.coxandkings.travel.operations.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {

    private static Logger logger = LogManager.getLogger( DBConfig.class);

//    private ComboPooledDataSource comboPoolDataSource;
//    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;
//
//    @Value("${operations_db_config.url}")
//    private String url;
//    @Value("${operations_db_config.username}")
//    private String userName;
//    @Value("${operations_db_config.password}")
//    private String password;
//    @Value("${operations_db_config.driverClassName}")
//    private String driverClass;
//    @Value("${spring.jpa.properties.hibernate.default_schema}")
//    private String schemaName;
//    @Value("${operations_db_config.c3p0.min_size}")
//    private int minPoolSize;
//    @Value("${operations_db_config.c3p0.max_size}")
//    private int maxPoolSize;
//
//
//    @Bean(name = "dataSource")
//    @Primary
//    public DataSource dataSource() {
//        try {
//
//
//            comboPoolDataSource = new ComboPooledDataSource();
//            comboPoolDataSource.setJdbcUrl(url);
//            comboPoolDataSource.setPassword(password);
//            comboPoolDataSource.setUser(userName);
//            comboPoolDataSource.setDriverClass(driverClass);
//            comboPoolDataSource.setTestConnectionOnCheckout(true);
//            comboPoolDataSource.setMinPoolSize(minPoolSize);
//            comboPoolDataSource.setMaxPoolSize(maxPoolSize);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return comboPoolDataSource;
//    }

    private ComboPooledDataSource comboPoolDataSource;
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Value( "${operations_db_config.url}" )
    private String url;
    @Value( "${operations_db_config.username}" )
    private String userName;
    @Value( "${operations_db_config.password}" )
    private String password;
    @Value( "${operations_db_config.driverClassName}" )
    private String driverClass;

    @Value( "${operations_db_config.c3p0.min_size}" )
    private int minPoolSize;
    @Value( "${operations_db_config.c3p0.max_size}" )
    private int maxPoolSize;

    @Bean( name = "dataSource" )
    @Primary
    public DataSource dataSource( ) {
        try {

            logger.info( "Connecting to DB using following: URL:" + url + "\n userName: " + userName +
                " driverClass:" + driverClass + " minPoolSize:" + minPoolSize + " maxPoolSize:" + maxPoolSize );

            comboPoolDataSource = new ComboPooledDataSource();
            comboPoolDataSource.setJdbcUrl( url );
            comboPoolDataSource.setPassword( password );
            comboPoolDataSource.setUser( userName );
            comboPoolDataSource.setDriverClass( driverClass );
            comboPoolDataSource.setTestConnectionOnCheckout( true );
            comboPoolDataSource.setMinPoolSize( minPoolSize );
            comboPoolDataSource.setMaxPoolSize( maxPoolSize );

        } catch ( Exception e ) {
            logger.error("DB Error",e);
        }
        return comboPoolDataSource;
    }



 /*   @PersistenceContext(unitName = "primary")
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        try {
            if (entityManagerFactoryBean == null) {
                entityManagerFactoryBean = builder.dataSource(comboPoolDataSource).properties(getJPAProperties())
                        .persistenceUnit("primary").packages("com.coxandkings.travel.operations.model","com.coxandkings.travel.ext.model.be").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityManagerFactoryBean;
    }


    private HashMap<String, Object> getJPAProperties() {
        HashMap<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.connection.autocommit", "false");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        //jpaProperties.put("spring.jpa.hibernate.ddl-auto", "create");

        jpaProperties.put("hibernate.default_schema", schemaName);
        jpaProperties.put("hibernate.hbm2ddl.import_files",import_file);

        return jpaProperties;


    }*/


}