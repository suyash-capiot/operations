package com.coxandkings.travel.operations.repository.driver;

import com.coxandkings.travel.operations.model.driver.VehicleCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VehicleCategoryRepository{

    public VehicleCategory getVehicleCategory(String id);
    public VehicleCategory saveOrUpdateVehicleCategory(VehicleCategory vehicleCategory);
    public List<VehicleCategory> getVehicleCategory();

}
