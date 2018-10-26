package com.coxandkings.travel.operations.utils.changeSupplier;

public interface CmsXmlStructure {

    String definedRatesStruct = "<root>\n" +
            "    <Header>\n" +
            "        <Authentication>\n" +
            "            <UserName>Ashley</UserName>\n" +
            "            <Password>Paul</Password>\n" +
            "        </Authentication>\n" +
            "    </Header>\n" +
            "    <Body>\n" +
            "        <HotelDetails HotelId=\"\" Start=\"\" End=\"\" SupplierId=\"\" ClientId=\"\" Currency=\"\">\n" +
            "            <RoomStayCandidates>\n" +
            "                <RoomStayCandidate>\n" +
            "                    <RoomType RoomTypeCode=\"\"/>\n" +
            "                    <RatePlan RatePlanCode=\"\"/>\n" +
            "                    <GuestCounts />\n" +
            "                </RoomStayCandidate>\n" +
            "            </RoomStayCandidates>\n" +
            "        </HotelDetails>\n" +
            "    </Body>\n" +
            "</root>";

    String cmsBookXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<AccoInterfaceRQ\n" +
            "    xmlns:CrossRef=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.CrossRef\"\n" +
            "    xmlns:EpochConverter=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.EpochConverter\"\n" +
            "    xmlns:Journey_Duration=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.Journey_Duration\"\n" +
            "    xmlns:citySupplierToSystem=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.citySupplierToSystem\"\n" +
            "    xmlns:citySystemToSupplier=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.citySystemToSupplier\"\n" +
            "    xmlns:countrySupplierToSystem=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.countrySupplierToSystem\"\n" +
            "    xmlns:countrySystemToSupplier=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.countrySystemToSupplier\"\n" +
            "    xmlns:fusion=\"http://www.coxandkings.com/integ/suppl/common\"\n" +
            "    xmlns:n1=\"http://www.coxandkings.com/integ/suppl/acco\"\n" +
            "    xmlns:n2=\"http://www.opentravel.org/OTA/2003/05\"\n" +
            "    xmlns:productSupplierToSystem=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.productSupplierToSystem\"\n" +
            "    xmlns:productSystemToSupplier=\"http://www.oracle.com/XSL/Transform/java/com.cnk.travelogix.util.productSystemToSupplier\"\n" +
            "    xmlns:tns=\"http://www.coxandkings.com/integ/suppl/accointerface\">\n" +
            "    <RequestHeader>\n" +
            "        <UserID>Test12R</UserID>\n" +
            "        <SessionID>CNK_RR</SessionID>\n" +
            "        <TransactionID>Book_RR</TransactionID>\n" +
            "        <SupplierCredentialsList>\n" +
            "            <SupplierCredentials>\n" +
            "                <SupplierID>CNK</SupplierID>\n" +
            "                <Sequence/>\n" +
            "                <Credentials>\n" +
            "                    <Credential isEncrypted=\"true\" name=\"Username\">bhzEgrvWgD5ZyimwgnVCIr9//Iv1IGYy</Credential>\n" +
            "                    <Credential isEncrypted=\"true\" name=\"Password\">NUG/OWD0jB0F/LQVfi6mMA==</Credential>\n" +
            "                    <OperationURLs>\n" +
            "                        <OperationURL operation=\"search\">http://xmltest.bonotel.com/bonotelapps/bonotel/reservation/GetAvailability.do</OperationURL>\n" +
            "                        <OperationURL operation=\"book\">http://xmltest.bonotel.com/bonotelapps/bonotel/reservation/GetReservation.do</OperationURL>\n" +
            "                    </OperationURLs>\n" +
            "                </Credentials>\n" +
            "            </SupplierCredentials>\n" +
            "        </SupplierCredentialsList>\n" +
            "        <ClientID>B2BCLIENT2074</ClientID>\n" +
            "    </RequestHeader>\n" +
            "    <RequestBody>\n" +
            "        <OTA_HotelResRQWrapper>\n" +
            "            <SupplierID>CNK</SupplierID>\n" +
            "            <Sequence>1</Sequence>\n" +
            "            <OTA_HotelResRQ ResStatus=\"Book\" Target=\"\" Version=\"\">\n" +
            "                <HotelReservations/>\n" +
            "            </OTA_HotelResRQ>\n" +
            "        </OTA_HotelResRQWrapper>\n" +
            "    </RequestBody>\n" +
            "</AccoInterfaceRQ>";


    String suppAutoSuggReq = "<root>\n" +
            "    <Header>\n" +
            "        <Authentication>\n" +
            "            <UserName>Ashley</UserName>\n" +
            "            <Password>Paul</Password>\n" +
            "        </Authentication>\n" +
            "    </Header>\n" +
            "    <Body>\n" +
            "\t\t  <GetSuppliers/> \n" +
            "    </Body>\n" +
            "</root>";

}
