package com.coxandkings.travel.operations.config;

import com.coxandkings.travel.operations.audit.UsernameAuditorAware;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class OperationsConfig {

    /*@Autowired
    @Qualifier("opsFilter")
    private OperationFilter operationFilter;
*/

    @PersistenceContext(name = "default")
    private EntityManager entityManager;

    /**
     * Using opsEntityManager as a qualifier in repositories,
     * because Wildfly giving more than one EntityManager on runtime
     */
    @Bean("opsEntityManager")
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US); // Set default Locale as US
        return slr;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");  // name of the resource bundle
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new UsernameAuditorAware();
    }

  /*  @Bean
    public FilterRegistrationBean registerFilter() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(operationFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("Operation Filter");
        return registration;
    }*/


    @Primary
    @Bean
    public ObjectMapper ObjectMapperContextResolver() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();


        javaTimeModule.addDeserializer(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
            @Override
            public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return ZonedDateTime.parse(p.getText(), DateTimeFormatter.ISO_DATE_TIME);
            }
        });

        javaTimeModule.addSerializer(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
            @Override
            public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.toString());
            }
        });
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(javaTimeModule);
        mapper.registerModule(new JsonOrgModule());
        return mapper;
    }

}
