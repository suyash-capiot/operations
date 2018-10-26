package com.coxandkings.travel.operations.service.partPaymentMonitor.impl;

import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.repository.partpaymentmonitor.PartPaymentMonitorToDoTaskRepository;
import com.coxandkings.travel.operations.resource.todo.ToDoStatus;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.partPaymentMonitor.PartPaymentMonitorFinanceService;
import com.coxandkings.travel.operations.service.partPaymentMonitor.PartPaymentMonitorService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PartPaymentMonitorServiceImpl implements PartPaymentMonitorService {

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private PartPaymentMonitorFinanceService financeService;

    @Autowired
    private OpsBookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier( value = "mDMToken" )
    private MDMToken mdmToken; //Token used by System to connect to MDM

    @Autowired
    private PartPaymentMonitorToDoTaskRepository toTaskRepo;

    private static final Logger logger = LogManager.getLogger( PartPaymentMonitorServiceImpl.class );

    @Override
    public void findPartPaymentBookings() throws OperationException {
        try {
            // Get all Bookings from Finance where Payment Status is part payment or no payment done
            List<String> partPaymentBookingIDs = financeService.getPartPaymentBookings();

            // Get User Info for system user from MDM Token
            MdmUserInfo mdmUser = userService.createUserDetailsFromToken( mdmToken.getToken() );
            OpsUser aOpsUser = userService.getOpsUser( mdmUser );
            String userID = aOpsUser.getUserID();
/*
            UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
            String userID = loggedInUser.getUserID();
*/
            // Go to ToDoTask and fetch existing ToDoTasks which are NOT in CLOSED state for Operations &&
            // are of type Part Payment
            ToDoSubType subType = new ToDoSubType();
            subType.setId("PART_PAYMENT" );

            ToDoStatus aToDoStatus = new ToDoStatus();
            aToDoStatus.setId( "Closed" );
            // Fetch from ToDoTask Table
            List<String> alreadyAvailableFollowupTasksList = toTaskRepo.getToDoTasksBySubType( subType, aToDoStatus );

            // Remove Bookings which have already ToDoTask assigned for Part Payment
            partPaymentBookingIDs.removeAll( alreadyAvailableFollowupTasksList );

            // If we have Bookings which require follow up, create new ToDoTask below
            if (partPaymentBookingIDs != null && (!partPaymentBookingIDs.isEmpty())) {

                for (String aBookingID : partPaymentBookingIDs) {

                    OpsBooking aBooking = bookingService.getBooking( aBookingID );

                    // Create a ToDoTask
                    ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                    toDoTaskResource.setReferenceId( aBookingID );
                    toDoTaskResource.setCreatedByUserId( userID );
                    toDoTaskResource.setAssignedBy( userID );
                    toDoTaskResource.setClientId( aBooking.getClientID() );

                    toDoTaskResource.setClientCategoryId( "" );
                    //Its not possible to use specific AbstractProductFactory/Order as payment is made for Booking2
                    toDoTaskResource.setProductId( "" );
                    toDoTaskResource.setClientSubCategoryId("");
                    toDoTaskResource.setClientTypeId("");
                    toDoTaskResource.setCompanyId("");
                    toDoTaskResource.setCompanyMarketId("");

                    toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
                    toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                    toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PART_PAYMENT.toString()); //PART_PAYMENT_BOOKING
                    toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
                    toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue());
                    toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());;
                    toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
                    toDoTaskResource.setFileHandlerId("PART_PAYMENT_HANDLER");// TODO - should be to a User Group for Ops
//                  toDoTaskResource.setSecondaryFileHandlerId("REFUND_APPROVER"); // secondary approver

                    toDoTaskResource.setSuggestedActions("");//sent the rest end point for now

                    //TODO : This should be from Booking2 Object (payment due date)
                    ZonedDateTime sampleTime = java.time.ZonedDateTime.now();
                    //toDoTaskResource.setDueOnDate(sampleTime.toString());
                    toDoTaskService.save( toDoTaskResource );
                }
            }
        }
        catch( Exception e )    {
            logger.error( e );
            e.printStackTrace();
            // TODO - Exception Handling
            OperationException partPaymentBatchError = new OperationException( "Error occurred while running Part Payment Monitor Batch Job" );
        }
    }
}
