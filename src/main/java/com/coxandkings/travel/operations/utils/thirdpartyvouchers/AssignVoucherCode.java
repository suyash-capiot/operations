package com.coxandkings.travel.operations.utils.thirdpartyvouchers;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.PaymentStatusToReleaseVoucher;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherToBeAppliedOn;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingAttribute;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.SupplierVoucherCodes;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.VoucherCode;
import com.coxandkings.travel.operations.repository.thirdPartyVoucher.VoucherCodeRepository;
import com.coxandkings.travel.operations.service.thirdPartyVoucher.ThirdPartyVouchersService;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AssignVoucherCode {

    @Autowired
    private ThirdPartyVouchersService thirdPartyVouchersService;

    @Autowired
    private VoucherCodeRepository voucherCodeRepository;

    private static Logger logger = Logger.getLogger(AssignVoucherCode.class);

    public static long getNumberOfDays(String dateIn, String dateOut) {
        String checkInDate = dateIn + "T00:00:00Z";
        String checkOutDate = dateOut + "T00:00:00Z";

        Instant checkIn = Instant.parse(checkInDate);
        Instant checkOut = Instant.parse(checkOutDate);

        ZonedDateTime inDate = ZonedDateTime.ofInstant(checkIn, ZoneId.of(ZoneOffset.UTC.getId()));
        ZonedDateTime outDate = ZonedDateTime.ofInstant(checkOut, ZoneId.of(ZoneOffset.UTC.getId()));

        ChronoUnit unit = ChronoUnit.DAYS;
        return unit.between(inDate, outDate);
    }

    public void processBooking(OpsBooking opsBooking) {
        logger.info("Entered AssignVoucherCode :: processBooking()");
        if (opsBooking != null) {
            List<OpsProduct> opsProducts = opsBooking.getProducts();
            for (OpsProduct opsProduct : opsProducts) {
                List<SupplierVoucherCodes> list = thirdPartyVouchersService.searchToAssignVouchers(opsProduct);
                Integer totalNumberOfVouchersToBeAssigned = null;
                Integer noofVouchers = 0;
                for (SupplierVoucherCodes supplierVoucherCodes : list) {
                    List<VoucherCode> voucherCodeList = thirdPartyVouchersService.vouchersAvailable(supplierVoucherCodes.getId());
                    totalNumberOfVouchersToBeAssigned = getTotalNumberOfVouchersTobeAssigned(opsProduct, supplierVoucherCodes);
                    Set<VoucherCode> voucherCodes = new HashSet<>(voucherCodeList);
                    int count = voucherCodes.size();
                    if (count >= totalNumberOfVouchersToBeAssigned && totalNumberOfVouchersToBeAssigned > 0) {
                        if (opsBooking.getPaymentInfo().get(0).getPaymentType().equals(supplierVoucherCodes.getPaymentStatusToReleaseVoucher().getValue())
                                || opsBooking.getPaymentInfo().get(0).getPaymentType().equalsIgnoreCase(PaymentStatusToReleaseVoucher.FULL_PAYMENT.getValue())) {
                            List<String> voucherCodeIds = new ArrayList<>();
                            List<VoucherCode> vouchersAssigned = new ArrayList<>();
                            for (VoucherCode voucherCode : voucherCodes) {
                                noofVouchers++;
                                voucherCodeIds.add(voucherCode.getVoucherCode());
                                vouchersAssigned.add(voucherCode);

                                if (noofVouchers == totalNumberOfVouchersToBeAssigned) {
                                    break;
                                }
                            }

                            supplierVoucherCodes.setVoucherCodes(voucherCodes);
                            ZonedDateTime releaseDate = null;
                            if (voucherCodeIds.size() > 0 && voucherCodeIds.size() == totalNumberOfVouchersToBeAssigned) {
                                if (supplierVoucherCodes.getVoucherToBeAppliedOn().equals(VoucherToBeAppliedOn.BOOKING_DATE)) {
                                    releaseDate = opsBooking.getBookingDateZDT()
                                            .plusDays(supplierVoucherCodes.getNoOfDaysToReleaseVoucher());
                                } else if (supplierVoucherCodes.getVoucherToBeAppliedOn().equals(VoucherToBeAppliedOn.TRAVEL_DATE)) {
                                    if (opsProduct.getOrderDetails().getHotelDetails() != null) {
                                        String date = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn();
                                        releaseDate = DateTimeUtil.formatBEDateTimeZone(date);
                                        releaseDate = releaseDate.minusDays(supplierVoucherCodes.getNoOfDaysToReleaseVoucher());
                                    } else if (opsProduct.getOrderDetails().getFlightDetails() != null) {
                                        releaseDate = opsProduct.getOrderDetails().getFlightDetails()
                                                .getOriginDestinationOptions().get(0)
                                                .getFlightSegment().get(0).getArrivalDateZDT();
                                        releaseDate = releaseDate.minusDays(supplierVoucherCodes.getNoOfDaysToReleaseVoucher());
                                    }
                                }
                                if (releaseDate != null) {
                                    releaseDate = releaseDate.truncatedTo(ChronoUnit.DAYS);
                                }
                                try {
                                    thirdPartyVouchersService.assignVouchersToBooking(supplierVoucherCodes, opsProduct, voucherCodeIds);
                                    for (VoucherCode voucherCode : vouchersAssigned) {
                                        if (opsBooking.getPaymentInfo().stream().anyMatch(p -> p.getPaymentType().equalsIgnoreCase("Full"))) {
                                            voucherCode.setPaymentStatus(OpsBookingAttribute.PAYMENT_REALISED.getBookingAttribute());
                                        } else if (opsBooking.getPaymentInfo().stream().anyMatch(p -> p.getPaymentType().equalsIgnoreCase("Part"))) {
                                            voucherCode.setPaymentStatus(OpsBookingAttribute.PAYMENT_NOT_REALISED.getBookingAttribute());
                                        }
                                        voucherCode.setVoucherCodeStatus(VoucherCodeStatus.ASSIGNED);
                                        voucherCode.setLastModifiedDate(ZonedDateTime.now());
                                        voucherCode.setBookId(opsBooking.getBookID());
                                        voucherCode.setOrderId(opsProduct.getOrderID());
                                        voucherCode.setReleaseDate(releaseDate);
                                        count--;

                                    }
                                    thirdPartyVouchersService.updateVouchersWithReleaseDate(vouchersAssigned);
                                } catch (OperationException e) {
                                    logger.debug("Error in assigning Vouchers to booking");
                                }
                                if (count <= supplierVoucherCodes.getNoOfDaysToSendAlarm() && noofVouchers > 0 && supplierVoucherCodes.getVoucherCodeUsageType().equals(VoucherCodeUsageType.FIXED)) {
                                    System.out.println("Alert Supplier");
                                    String noOfVoucher = String.valueOf(count);
                                    try {
                                        thirdPartyVouchersService.sendEmailToSupplier(opsBooking.getBookID(), opsProduct.getOrderID()
                                                , supplierVoucherCodes.getProductCategoryName(), supplierVoucherCodes.getSupplierTemplateId(), noOfVoucher);
                                    } catch (OperationException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                    if (noofVouchers != 0 && totalNumberOfVouchersToBeAssigned != null && noofVouchers == totalNumberOfVouchersToBeAssigned) {
                        break;
                    }
                }
            }
        }

    }

    public Integer getTotalNumberOfVouchersTobeAssigned(OpsProduct opsProduct, SupplierVoucherCodes supplierVoucherCodes) {
        Integer totalNumberOfVouchersToBeAssigned = 1;
        Integer multiplier = supplierVoucherCodes.getMultiplier();
        switch (supplierVoucherCodes.getUnitOfMeasurement()) {
            case PER_DAY: {
                int noOfDays = 0;
                if (opsProduct.getOrderDetails().getHotelDetails() != null) {
                    for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                        int l = (int) getNumberOfDays(opsRoom.getCheckIn(), opsRoom.getCheckOut());
                        noOfDays = noOfDays + l;
                    }
                }
                totalNumberOfVouchersToBeAssigned = noOfDays * multiplier;
                break;
            }
            case PER_ROOM: {
                int noOfRooms = 0;
                if (opsProduct.getOrderDetails().getHotelDetails() != null) {
                    noOfRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms().size();
                }
                totalNumberOfVouchersToBeAssigned = noOfRooms * multiplier;
                break;
            }
            case PER_PET: {
                //Todo need ancillary products
                break;
            }
            case PER_NIGHT: {
                int noOfNights = 0;
                if (opsProduct.getOrderDetails().getHotelDetails() != null) {
                    for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                        int l = (int) getNumberOfDays(opsRoom.getCheckIn(), opsRoom.getCheckOut());
                        noOfNights = noOfNights + l;
                    }
                }
                totalNumberOfVouchersToBeAssigned = noOfNights * multiplier;
                break;
            }
            case PER_PERSON: {
                int noOfPersons = 0;
                if (opsProduct.getOrderDetails().getHotelDetails() != null) {
                    for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                        noOfPersons = opsRoom.getPaxInfo().size();
                    }
                } else if (opsProduct.getOrderDetails().getFlightDetails() != null) {
                    noOfPersons = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo().size();
                }
                totalNumberOfVouchersToBeAssigned = noOfPersons * multiplier;
                break;
            }
            case PER_RESERVATION: {

                break;
            }
            case PER_PERSON_PER_NIGHT: {
                int noOfNights = 0;
                int noOfPersons = 0;
                if (opsProduct.getOrderDetails().getHotelDetails() != null) {
                    for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                        int l = (int) getNumberOfDays(opsRoom.getCheckIn(), opsRoom.getCheckOut());
                        noOfNights = noOfNights + l;
                        noOfPersons = opsRoom.getPaxInfo().size();
                    }
                }
                totalNumberOfVouchersToBeAssigned = (noOfNights * noOfPersons) * multiplier;
            }
        }
        return totalNumberOfVouchersToBeAssigned;
    }
}
