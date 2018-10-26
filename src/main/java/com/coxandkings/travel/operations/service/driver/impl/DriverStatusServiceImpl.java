/*
package com.coxandkings.travel.operations.service.driver.impl;

import com.coxandkings.travel.operations.model.driver.DriverStatus;
import com.coxandkings.travel.operations.repository.driver.DriverStatusRepository;
import com.coxandkings.travel.operations.resource.driver.DriverStatusResource;
import com.coxandkings.travel.operations.service.driver.DriverStatusService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("DriverStatusService")
public class DriverStatusServiceImpl implements DriverStatusService {

    @Autowired
    DriverStatusRepository driverStatusRepository;

    @Override
    public DriverStatus getDriverStatusByBookingId(String id) {
        DriverStatus driverStatus = driverStatusRepository.getDriverStatusById(id);
        return driverStatus;
    }

    @Override
    public DriverStatus saveOrUpdateDriverStatus(DriverStatusResource driverStatusResource) {

        DriverStatus driverStatus = null;
        try
        {
            String id = driverStatusResource.getId();
            if(!StringUtils.isEmpty(id)) {
                DriverStatus existingDriverStatus = driverStatusRepository.getDriverStatusById(id);
                if(existingDriverStatus == null) {
                    throw new Exception(" Status not found for id:"+ id);
                }
                driverStatus = existingDriverStatus;
            } else {
                driverStatus=new DriverStatus();
            }
            CopyUtils.copy(driverStatusResource, driverStatus);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return driverStatusRepository.saveOrUpdateDriverStatus(driverStatus);
    }

    @Override
    public List<DriverStatus> getDriverStatus() {
        return driverStatusRepository.getDriverStatus();
    }
}
*/
