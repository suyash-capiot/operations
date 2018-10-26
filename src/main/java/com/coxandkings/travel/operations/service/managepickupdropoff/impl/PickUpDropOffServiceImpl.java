package com.coxandkings.travel.operations.service.managepickupdropoff.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managepickupdropoff.PickUpDropOff;
import com.coxandkings.travel.operations.repository.managepickupdropoff.PickUpDropOffRepository;
import com.coxandkings.travel.operations.resource.managepickupdropoff.PickUpDropOffResource;
import com.coxandkings.travel.operations.service.managepickupdropoff.PickUpDropOffService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service(value = "PickUpDropOffServiceImpl")
public class PickUpDropOffServiceImpl implements PickUpDropOffService
{
    @Autowired
    private PickUpDropOffRepository pickUpDropOffRepository;

    @Override
    public PickUpDropOff savePickUpDropOff(PickUpDropOffResource resource) throws OperationException {
        PickUpDropOff pickUpDropOff = null;
        if (!StringUtils.isEmpty(resource))
        {
            if (StringUtils.isEmpty(resource.getId()))
            {
                pickUpDropOff = new PickUpDropOff();
                CopyUtils.copy(resource, pickUpDropOff);
                pickUpDropOff = pickUpDropOffRepository.savePickUpDropOffDetails(pickUpDropOff);
            }
        }
        else
        {
            throw new OperationException(Constants.ER05+"Please enter data");
        }

        return pickUpDropOff;
    }

    @Override
    public PickUpDropOff updatePickUpDropOff(PickUpDropOffResource resource) throws OperationException {
        PickUpDropOff pickUpDropOff = null;
        if (!StringUtils.isEmpty(resource))
        {
            if (!StringUtils.isEmpty(resource.getId()))
            {
                pickUpDropOff = new PickUpDropOff();
                CopyUtils.copy(resource,pickUpDropOff);
                pickUpDropOff = pickUpDropOffRepository.savePickUpDropOffDetails(pickUpDropOff);
            }
            else {
                throw new OperationException(Constants.ER05);
            }
        }
        return pickUpDropOff;
    }

    @Override
    public PickUpDropOff searchPickUpDropOff(String bookingRefId, String orderId) throws OperationException {
        PickUpDropOff pickUpDropOff = new PickUpDropOff();
        pickUpDropOff.setBookingRefId(bookingRefId);
        pickUpDropOff.setOrderId(orderId);
        pickUpDropOff = pickUpDropOffRepository.searchPickUpDropOff(pickUpDropOff);
        if (pickUpDropOff!=null)
            return pickUpDropOff;
        else
            throw new OperationException(Constants.ER01);
    }


}
