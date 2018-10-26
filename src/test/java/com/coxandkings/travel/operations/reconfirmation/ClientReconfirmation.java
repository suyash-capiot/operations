package com.coxandkings.travel.operations.reconfirmation;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.runners.MethodSorters.NAME_ASCENDING;


@RunWith( SpringJUnit4ClassRunner.class )
//@SpringBootTest(classes = Operations.class)
@FixMethodOrder( NAME_ASCENDING )
//@DataJpaTest
public class ClientReconfirmation {


    private MockMvc mockMvc;
    ResultMatcher ok = MockMvcResultMatchers.status( ).isOk( );
    ResultMatcher notFound = MockMvcResultMatchers.status( ).isNotFound( );

    @Autowired
    WebApplicationContext context;


    @Before
    public void initTests( ) {
        MockitoAnnotations.initMocks( this );
        mockMvc = MockMvcBuilders.webAppContextSetup( context ).build( );
    }


    //###// SUCCESS SCENARIO TESTING  //##//

    @Test
    public void test1( ) {
        // ActionUtilityMockMvc.performAction( ActionUtilityMockMvc.baseMethodForPost(URL_101.getURL(), SupplierCommercialMock.getSupplierCommercialDummyData()), ok, mockMvc);
    }

    // @Test
    public void testGetSupplierCommercialByCommercialId( ) {
        // ActionUtilityMockMvc.performAction(ActionUtilityMockMvc.baseMethodForGet(URL_104.getURL(), SupplierCommercialMock.getSupplierCommercialDummyData().getId()), ok, mockMvc);
    }

    @Test
    public void test2SearchSupplierCommercialStatements( ) {
        //ActionUtilityMockMvc.performAction(ActionUtilityMockMvc.baseMethodForPost(URL_102.getURL(), SupplierCommercialMock.getSupplierCommercialDummyData()), ok, mockMvc);
    }

    @Test
    public void test3GetSupplierCommercialByStatementId( ) {
        // ActionUtilityMockMvc.performAction(ActionUtilityMockMvc.baseMethodForGet(URL_103.getURL(), STATEMENT_ID.getValue()), ok, mockMvc);
    }

    //###// FAILED SCENARIO TESTING  //##//

    //@Test
    public void test4FailToGetSupplierCommercialByStatementIdIfSupplierCommercialNotFound( ) {
        //  ActionUtilityMockMvc.performAction(ActionUtilityMockMvc.baseMethodForGet(URL_103.getURL(), "invalidId"), notFound, mockMvc);
    }

    @Test
    public void testFailToCreateSupplierCommercialIfMandatoryFieldsAreMissing( ) {


    }

}
