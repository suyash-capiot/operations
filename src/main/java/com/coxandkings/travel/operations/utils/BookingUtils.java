package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.enums.WorkFlow;

import java.util.HashMap;
import java.util.Map;

public class BookingUtils {

    public static HashMap<String, String> reconfirmation = new HashMap<>();

    public static String startFlow(String containerUrl,
                                   String processDefUrl,
                                   String flowStartUrl,
                                   String containerName,
                                   WorkFlow workFlow,
                                   Map<String, Object> map) {
        return WorkFlowUtils.startFlow(containerUrl, processDefUrl, flowStartUrl, containerName, workFlow, map).toString();
//        return "Started";
    }

    /*public static Book getBookingById(String bookingRefId, boolean addOtherProductDetails) {
        //setting the values
        Book book = new Book();
        book.setBookId(bookingRefId);
        book.setBookingRefId(bookingRefId);

        List<BookProductDetails> list=new ArrayList<>();
        list.add(getCarProduct(addOtherProductDetails));
        list.add(getFlightProduct());
        list.add(getHotel("Taj Krishna","Vouchered"));

        book.setProductDetails(list);
        book.setStatus("confirmed");

        return book;
    }

    private static Car getCarProduct(boolean addOtherProductDetails) {
        Car c = new Car();
        c.setId("CNK-MARUTI-1hg236");
        c.setBookingRefId("123");
        c.setOrderID("5");
        c.setOtherProductAvailable(true);
        c.setProductCategoryId("transportation");
        c.setProductCategoryCode("Transportation");
        c.setProductCategorySubTypeId("car");
        c.setProductCategorySubTypeCode("Car");
        c.setCategory("transfer");
        c.setCategoryCode("Transfer");
        c.setTransferType("private");
        c.setTransferTypeCode("Private");
        c.setWithChauffer(true);
        c.setStatus("Confirmed");
        if(addOtherProductDetails) {
            Bus bus=new Bus();
            bus.setOrderID("productId");
            c.setOtherProductDetails(bus);
        }
        return c;
    }*/

    // TODO: needs to check with inventory
    public static boolean checkCheaperPrice(String bookingRefId, Integer productId) {
        return true;
    }

    /*private static Flight getFlightProduct() {
        Flight f = new Flight();
        f.setId("CNK-JET-123");
        f.setProductCategoryId("transportation");
        f.setProductCategoryCode("Transportation");
        f.setProductCategorySubTypeId("flight");
        f.setProductCategorySubTypeCode("Flight");
        f.setAirlineName("Indigo");
        f.setStatus("Confirmed");
        f.setOrderID("JET-1243");
        return f;
    }

    public static List<Book> getListOfBookings() {
        List<Book> listOfBookings=new ArrayList<>();
        Book book1=new Book();
        Book book2=new Book();

        List list1=new ArrayList();
        list1.add(getHotel("cityId", "RXL"));
        book1.setStatus("RXL");
        book1.setProductDetails(list1);

        List list2=new ArrayList();
        list2.add(getHotel("cityId", "RQ"));
        book2.setProductDetails(list2);
        book2.setStatus("RQ");

        listOfBookings.add(book1);
        listOfBookings.add(book2);

        return listOfBookings;
    }

    public static Hotel getHotel(String name, String status){
        Hotel hotel=new Hotel();
        hotel.setId("CNK-TAJ-16242");
        hotel.setOrderID("TAJ-1hv236");
        hotel.setProductCategoryId("accommodation");
        hotel.setProductCategoryCode("Accommodation");
        hotel.setProductCategorySubTypeId("hotel");
        hotel.setProductCategorySubTypeCode("Hotel");
        hotel.setCityId(name);
        hotel.setStatus(status);
        return hotel;
    }

    // refunds
    public static Book getBookById(String bookId,String id){
        Book book=new Book();
        book.setBookingRefId(id);
        List<BookProductDetails> productList=new ArrayList();
        productList.add(getBus(id));
        book.setProductDetails(productList);
        return book;
    }
    public static Bus getBus(String id){
        Bus bus=new Bus();
        bus.setId(id);
        Refunds refunds=new Refunds();
        refunds.setRemarks("remarks");
        bus.setRefunds(refunds);
        return bus;
    }

    update product with pick up drop off details
    public static BookProductDetails updatePickUpDropOff(BookProductDetails productDetails){
        Book book=getBookingById("123",false);
        BookProductDetails bookProductDetails=getProduct(book,"5");
        bookProductDetails=productDetails;
        //TODO hit BE to update pick up and drop off details
        return bookProductDetails;
    }*//*

    public static BookProductDetails getProduct() {
        //Client client=new Client("1","B2C","Abc","sj","PQR","Prior to travel date",2,"client");
        //Supplier supplier=new Supplier("1","ABC","Supplier",123450,4568,"ABC","Supplier","Prior to travel date",3);
        Flight f = getFlightProduct();
        List<BookProductDetails> list = new ArrayList<>();
        list.add(f);
        BookProductDetails bookProductDetails = list.get(0);
        bookProductDetails.setOrderID("1");
        bookProductDetails.setCategory("Accommodation");
        reconfirmation.put("reconfirmation", "true");
        reconfirmation.put("configurationfor", "Client");
        reconfirmation.put("CutOffDateForClientReconfirmation", "123456L");
        reconfirmation.put("CutOffDateForSupplierReconfirmation", "1234512345L");
        reconfirmation.put("noOfTimesReconfirmation", "2");
        bookProductDetails.setAttributes(reconfirmation);
        bookProductDetails.setStatus("confirmed");
        bookProductDetails.setSupplierReconfirmationStatus(null);
        return bookProductDetails;
    }


    public static BookProductDetails getProduct(Book book, String id) {

        List<BookProductDetails> list = book.getProductDetails();
        BookProductDetails productDetails = list.stream()
                .filter(bookProductDetails -> bookProductDetails.getId().equalsIgnoreCase(id))
                .findAny()
                .orElse(null);
        return productDetails;
    }

    public static BookProductDetails getProductById(String bookingRefId, String productId){
        Book book=getBookingById(bookingRefId,false);
        BookProductDetails bookProductDetails=getProduct(book,productId);
        Car car=null;
        if(bookProductDetails.getProductCategorySubTypeCode().equalsIgnoreCase("Car")){
            car=(Car)bookProductDetails;
            car.setPickupPointName("kp");
            car.setDropOffPointName("jntu");
            car.setViaPoint("kphb");
            car.setPickupTime(1515004260000L);
            VehicleInformation vehicleInformation=new VehicleInformation();
            vehicleInformation.setVehicleName("swift");
            vehicleInformation.setVehicleCategory("abc");
            vehicleInformation.setAirConditioned(true);
            vehicleInformation.setWithChauffer(true);
            car.setVehicleInformation(vehicleInformation);
            bookProductDetails=car;
        }
        return bookProductDetails;
    }*/
//    public static MatchedBookingResource getMatchedBookings(Booking2 bookingMock) {
//
//        MatchedBookingResource matchedProducts = new MatchedBookingResource();
//        if (!bookingMock.getBookID().isEmpty()){
//            matchedProducts.setBookingReferenceNumber(bookingMock.getBookID());
//            for (AbstractProductFactory product : bookingMock.getProducts()) {
//                if (product.getProductName().equalsIgnoreCase("accommodation")
//                        && product.getStatus() != null && product != null) {
//                    matchedProducts.setProductDetails(product);
//                    matchedProducts.setOpsBookingStatus(product.getStatus());
//                }
//                List<Room> rooms = product.getOrderDetails().getHotelDetails().getRooms();
//                if (rooms!=null || !(rooms.isEmpty())){
//                    for (Room room : rooms) {
//                        List<PaxInfo> paxInfos = room.getPaxDetails();
//                        PaxInfo paxInfo = paxInfos.stream().findAny().filter(paxInfo1 -> paxInfo1.getIsLeadPax()).get();
//                        matchedProducts.setLeadPassengerName(paxInfo.getFirstName());
//                    }
//                }
//            }
//        }
//        return matchedProducts;
//    }

  /*  public static List<Booking2> getMatchedonReq(List<Booking2> listOfOnRequest, AbstractProductFactory p, Room room){
        List<Booking2> matchedOnReq=listOfOnRequest.stream()
                .filter(bookingMock1 -> {
                    return bookingMock1.getProducts().stream()
                            .anyMatch(product1 -> (product1.getOrderDetails().getHotelDetails().getHotelCode()
                                    .equalsIgnoreCase(p.getOrderDetails().getHotelDetails().getHotelCode()))
                                    &&(product1.getOrderDetails().getHotelDetails().getCityCode().equalsIgnoreCase(p.getOrderDetails().getHotelDetails().getCityCode()))
                                    &&(product1.getOrderDetails().getHotelDetails().getCountryCode().equalsIgnoreCase(p.getOrderDetails().getHotelDetails().getCountryCode()))
                                    &&(product1.getOrderDetails().getHotelDetails().getRooms().stream()
                                    .anyMatch(r->(r.getCheckIn().equalsIgnoreCase(room.getCheckIn())) && (r.getCheckOut().equalsIgnoreCase(room.getCheckOut()))))
                                    &&(product1.getOrderDetails().getHotelDetails().getRooms().stream()
                                    .anyMatch(r->(r.getRoomTypeInfo().getRoomTypeCode().equalsIgnoreCase(room.getRoomTypeInfo().getRoomTypeCode()))
                                            &&(r.getRoomTypeInfo().getRoomCategoryID().equalsIgnoreCase(room.getRoomTypeInfo().getRoomCategoryID()))))
                            );
                }).collect(Collectors.toList());
        return matchedOnReq;
    }

}
*/
}