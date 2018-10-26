package com.coxandkings.travel.operations.consumer.factory;

import com.coxandkings.travel.operations.consumer.listners.impl.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


//TODO : Remove this class and add @Component(red : FullCancellationListenerImpl)  and @Lookup in BookingListenerFactory
@Configuration
@ComponentScan(basePackageClasses = {MdmListenerFactory.class, EmailListenerFactory.class, BookingListenerFactory.class})
public class AppConfig {


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MergeBookingListener getMergeBooking()
    {
        return new MergeBookingListener();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NewSupplierFirstBookingListener getNewSupplierFirstBooking() {
        return new NewSupplierFirstBookingListener() ;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ManageDocumentationListener getManageDocumentationListener() {
        return new ManageDocumentationListener();
    }


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ServiceOrderAndSupplierLiabilityListener getServiceOrderAndLiability() {
        return new ServiceOrderAndSupplierLiabilityListener() ;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ThirdPartyVouchersListenerImpl getThirdPartyVouchers() {
        return new ThirdPartyVouchersListenerImpl();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PrePaymentToSupplierListener getPrePaymentToSupplier() {
        return new PrePaymentToSupplierListener() ;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TimeLimitBookingListener getTimeLimitListener() {
        return new TimeLimitBookingListener() ;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SupplierListenerImpl getSupplierListener() {
        return new SupplierListenerImpl();
    }

    @Bean
    @Scope( value = ConfigurableBeanFactory.SCOPE_PROTOTYPE )
    public ServiceReconfirmationListener getServiceReconfirmationListener( ) {
        return new ServiceReconfirmationListener( );
    }
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EmailListenerImpl getEmailListener() {
        return new EmailListenerImpl() ;
    }


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FileProfitabilityListenerImpl getFileProfitabilityListenerImpl() {
        return new FileProfitabilityListenerImpl();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RetrieveOrderStatusListenerImpl getRetrieveOrderStatusListenerImpl() {
        return new RetrieveOrderStatusListenerImpl();
    }

    /*@Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FullCancellationListenerImpl getFullCancellation() {
        return new FullCancellationListenerImpl();
    }*/
    
    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AlternateOptionsBookingListenerImpl getAlternateOptionsBookingListenerImpl() {
    	return new AlternateOptionsBookingListenerImpl();
    }

}
