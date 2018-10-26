package com.coxandkings.travel.operations.service.driver.impl;

import com.coxandkings.travel.operations.model.driver.VehicleCategory;
import com.coxandkings.travel.operations.repository.driver.VehicleCategoryRepository;
import com.coxandkings.travel.operations.resource.driver.VehicleCategoryResource;
import com.coxandkings.travel.operations.service.driver.VehicleCategoryService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("VehicleCategoryService")
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    @Autowired
    VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    public VehicleCategory getVehicleCategoryByBookingId(String id) {
        return vehicleCategoryRepository.getVehicleCategory(id);
    }

    @Override
    public VehicleCategory saveOrUpdateVehicleCategory(VehicleCategoryResource vehicleCategoryResource) {

        VehicleCategory vehicleCategory = null;
        try
        {
            String id = vehicleCategoryResource.getId();
            if(!StringUtils.isEmpty(id)) {
                VehicleCategory existingVehicleCategory = vehicleCategoryRepository.getVehicleCategory(id);
                if(existingVehicleCategory == null) {
                    throw new Exception(" VehicleCategory not found for id:"+ id);
                }
                vehicleCategory = existingVehicleCategory;
            } else {
                vehicleCategory = new VehicleCategory();
            }
            CopyUtils.copy(vehicleCategoryResource, vehicleCategory);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vehicleCategoryRepository.saveOrUpdateVehicleCategory(vehicleCategory);
    }

    @Override
    public List<VehicleCategory> getVehicleCategory() {
        return vehicleCategoryRepository.getVehicleCategory();
    }
}
