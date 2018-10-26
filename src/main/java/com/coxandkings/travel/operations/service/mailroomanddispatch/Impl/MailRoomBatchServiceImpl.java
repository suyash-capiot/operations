package com.coxandkings.travel.operations.service.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.EmpStatus;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.EmployeeDetails;
import com.coxandkings.travel.operations.model.mailroomanddispatch.MailRoomMaster;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.MailRoomRepository;
import com.coxandkings.travel.operations.resource.mailroomMaster.MailRoomMasterResource;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomBatchService;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomService;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomStatusInactiveService;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomStatusService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MailRoomBatchServiceImpl implements MailRoomBatchService, MailRoomStatusService, MailRoomStatusInactiveService {

    private static final Logger logger = LogManager.getLogger(MailRoomBatchServiceImpl.class);

    @Autowired
    private MailRoomRepository mailRoomRepository;

    @Autowired
    private MailRoomService mailRoomService;

    @Override
    public void updateMailroomStatusActive() throws OperationException, JSONException {

        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        List<MailRoomMaster> mailRoomMastersActive = new ArrayList<>();
        MailRoomMasterResource mailRoomMasterResource = new MailRoomMasterResource();

        try {
            if (mailRoomMasterList != null || mailRoomMasterList.size() > 0)
                mailRoomMastersActive = mailRoomMasterList.parallelStream().filter(x -> x.getEffectiveFrom().toLocalDate().equals(LocalDate.now(ZoneId.systemDefault()))).map(x -> mailRoomMasterChangeStatusActive(x)).collect(Collectors.toList());

            for (MailRoomMaster mailRoomMaster : mailRoomMastersActive) {
                CopyUtils.copy(mailRoomMaster, mailRoomMasterResource);
                mailRoomService.update(mailRoomMasterResource);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void updateMailroomEmployeeStatusActive() throws OperationException {
        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        EmployeeDetails employeeDetails = new EmployeeDetails();
        MailRoomMasterResource mailRoomMasterResource = new MailRoomMasterResource();
        if (mailRoomMasterList != null) {
            for (MailRoomMaster mailRoomMaster : mailRoomMasterList) {
                employeeDetails = (EmployeeDetails) mailRoomMaster.getEmployeeDetails();
                if (employeeDetails.getEmployeeActivityStartDate().toLocalDate().equals(LocalDate.now(ZoneId.systemDefault()))) {
                    employeeDetails.setEmpStatus(EmpStatus.INACTIVE);
                    CopyUtils.copy(mailRoomMaster, mailRoomMasterResource);
                    mailRoomService.update(mailRoomMasterResource);
                } else if (employeeDetails.getEmployeeActivityExpiryDate().equals(ZonedDateTime.now())) {
                    employeeDetails.setEmpStatus(EmpStatus.ACTIVE);
                    CopyUtils.copy(mailRoomMaster, mailRoomMasterResource);
                    mailRoomService.update(mailRoomMasterResource);
                }
            }
        }

    }

    @Override
    public void updateMailroomStatusInactive() throws OperationException, NullPointerException {
        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        List<MailRoomMaster> mailRoomMastersInactive = new ArrayList<>();
        MailRoomMasterResource mailRoomMasterResource = new MailRoomMasterResource();
        try {
            if (mailRoomMasterList != null || mailRoomMasterList.size() > 0) {
                mailRoomMastersInactive = mailRoomMasterList.parallelStream().filter(x -> x.getEffectiveTo().toLocalDate().equals(LocalDate.now(ZoneId.systemDefault()))).map(x -> mailRoomMasterChangeStatusInactive(x)).collect(Collectors.toList());

                CopyUtils.copy(mailRoomMastersInactive, mailRoomMasterResource);
                mailRoomService.update(mailRoomMasterResource);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public MailRoomMaster mailRoomMasterChangeStatusActive(MailRoomMaster mailRoomMaster) {
        mailRoomMaster.setRoomStatus(MailRoomStatus.INACTIVE);
        return mailRoomMaster;
    }

    @Override
    public MailRoomMaster mailRoomMasterChangeStatusInactive(MailRoomMaster mailRoomMaster) {
        mailRoomMaster.setRoomStatus(MailRoomStatus.ACTIVE);
        return mailRoomMaster;
    }
}
