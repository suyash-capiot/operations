package com.coxandkings.travel.operations.service.driver;

import com.coxandkings.travel.operations.model.driver.VehicleCategory;
import com.coxandkings.travel.operations.resource.driver.VehicleCategoryResource;

import java.util.List;

public interface VehicleCategoryService {

    public VehicleCategory getVehicleCategoryByBookingId(String id);
    public VehicleCategory saveOrUpdateVehicleCategory(VehicleCategoryResource vehicleCategoryResource);
    public List<VehicleCategory> getVehicleCategory();
}
