package com.coxandkings.travel.operations.config;

import com.coxandkings.travel.operations.security.OpsRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/console/**").permitAll()
                .antMatchers("/Booking2-Engine-DBServices-2.0/BookingService/**").permitAll()
                .antMatchers("/BookingEngineDBSource/BookingService/**").permitAll()
                .antMatchers("/operations/**").permitAll()
                .antMatchers("/swagger-ui.htm/**").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers("/finance/**").permitAll()
                .antMatchers("/bookstop/**").permitAll()
                .antMatchers("/zmock/**").permitAll()
                .antMatchers("/supplierConfigurations/**").permitAll()
                .antMatchers("/bookingService/**").permitAll()
                .antMatchers("/testing/**").permitAll()
                .antMatchers("/commercials/**").permitAll()
                .antMatchers("/creditDebitNote/**").permitAll()
                .antMatchers("/Booking2-Engine-DBServices-2.0/**").permitAll()
                .antMatchers("**/mdm/**").permitAll()
                .antMatchers("/bookingPrices/**").permitAll()
                .antMatchers("/products/**").permitAll()
                .antMatchers("/client/customer/**").permitAll()
                .antMatchers("/supplier/service-provider/**").permitAll()
                .antMatchers("/supplierInvoice/**").permitAll()
                .antMatchers("/beCommercial/**").permitAll()
                .antMatchers("/supplierResponse/operations/reconfirmation/supplierResponse/**").permitAll()
                .antMatchers("/clientResponse/operations/reconfirmation/clientResponse/**").permitAll()
                .antMatchers("/clientResponse/operations/productSharing/clientResponse/**").permitAll()
                .antMatchers("/mdmTemplates/**").permitAll()
                .antMatchers("/productReviews/**").permitAll()
                .antMatchers("/productReviewTemplate/**").permitAll()
                .antMatchers("/clientCommercials/**").permitAll()
                .antMatchers("/amendments/supplierResponse/**").permitAll()
                .antMatchers("/documentLibrary/**").permitAll()
                .antMatchers("/productSharing/**").permitAll()
                .antMatchers("/noShow/**").permitAll()
                .antMatchers("/reinstate/supplierResponse/**").permitAll()
                .antMatchers("/fullCancellations/v1/updateOrderStatusNotExternetSupplier/**").permitAll()
                .antMatchers("/products/bookedThroughOtherSevices/v1/updatedByClient").permitAll()
                .antMatchers("/products/bookedThroughOtherSevices/v1/productSubType/{productCategorySubTypeValue}/id/{id}").permitAll()
                .antMatchers("/drivers/v1/updateBySupplier").permitAll()
                .antMatchers("/changeSupplierName/v1/client/**").permitAll()
                .antMatchers("/documentLibrary/v1/get/**").permitAll().
                antMatchers("/documentLibrary/v2/get/**").permitAll().
                antMatchers("/documentLibrary/v1/htmlToPDF/**").permitAll().
                antMatchers("/drivers/v1/id/{id}").permitAll().
				antMatchers("/mailrooms/v1/update").permitAll().
                antMatchers("/alternateOptions/v1/update/{id}").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.cors();
        http.headers().frameOptions().disable();
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();

    }

    @Bean
    public OpsRequestFilter authenticationTokenFilterBean() throws Exception {
        return new OpsRequestFilter();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/operations/");
    }
}