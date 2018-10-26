package com.coxandkings.travel.operations.service.supplierbillpassing.impl;

import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentUntil;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.supplierbillpassing.StopPaymentService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StopPaymentServiceImpl implements StopPaymentService {

    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;

    @Autowired
    private AlertService alertService;

    private static Logger logger = Logger.getLogger( StopPaymentServiceImpl.class );


    @Override
    public Map<String, Object> save(StopPaymentResource stopPaymentResource) throws OperationException, ParseException {

        Set<ProvisionalServiceOrder> provisionalServiceOrders=new HashSet<>();
        StopPaymentUntil stopPaymentTill=null;

        for (StopPaymentUntil stopPaymentUntil: StopPaymentUntil.values())
            if (stopPaymentUntil.getValue().equalsIgnoreCase(stopPaymentResource.getStopPaymentTill()))
                stopPaymentTill=stopPaymentUntil;

        if (stopPaymentTill==null)
            throw new OperationException(Constants.ER421);

        if (stopPaymentTill== StopPaymentUntil.DATE) {
            if (stopPaymentResource.getDate() == null) throw new OperationException(Constants.ER551);
            if (ChronoUnit.SECONDS.between(stopPaymentResource.getDate(), ZonedDateTime.now()) >= 1)
                throw new OperationException(Constants.ER431);
        }
        else
            if (!(stopPaymentResource.getDate() == null)) throw new OperationException(Constants.ER501);

        for (String provisionalServiceOrderId : stopPaymentResource.getProvisionalServiceOrderIds()) {
            ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(provisionalServiceOrderId);
            if (provisionalServiceOrder == null) throw new OperationException(Constants.ER451);
            if (provisionalServiceOrder.getStopPaymentStatus() != null && provisionalServiceOrder.getStopPaymentStatus()== StopPaymentStatus.ACTIVE) throw new OperationException(Constants.ER481);
            provisionalServiceOrders.add(provisionalServiceOrder);
        }

        for (ProvisionalServiceOrder provisionalServiceOrder:provisionalServiceOrders){
            provisionalServiceOrder.setStopPaymentTill(stopPaymentTill);
            provisionalServiceOrder.setStopPaymentStatus(StopPaymentStatus.ACTIVE);
            if (stopPaymentResource.getDate()!=null)
                provisionalServiceOrder.setStopPaymentTillDate(stopPaymentResource.getDate());
            ServiceOrderResource serviceOrderResource=new ServiceOrderResource();
            provisionalServiceOrderService.updateProvisionalServiceOrder(provisionalServiceOrder);
            try {
                //TODO: Remove from try catch later.
                stopPaymentAlert(provisionalServiceOrder);
            }catch (Exception e){

            }
        }
        Map<String,Object> entity=new HashMap<>();
        entity.put("message","successful");
        return entity;
    }

    @Override
    public Map release(StopPaymentResource stopPaymentResource) throws OperationException, ParseException {
        Boolean single=null;
        if (stopPaymentResource.getProvisionalServiceOrderIds().size()==0) throw new OperationException(Constants.ER451);
        for (String psoId:stopPaymentResource.getProvisionalServiceOrderIds()){
            ProvisionalServiceOrder provisionalServiceOrder = provisionalServiceOrderService.getPSOById(psoId);
            if (provisionalServiceOrder==null) throw new OperationException(Constants.ER401);
            if (provisionalServiceOrder.getStopPaymentStatus() == null) throw new OperationException(Constants.ER471);
            if (provisionalServiceOrder.getStopPaymentStatus()== StopPaymentStatus.INACTIVE) throw new OperationException(Constants.ER471);
            provisionalServiceOrder.setStopPaymentStatus(StopPaymentStatus.INACTIVE);
            provisionalServiceOrderService.updateProvisionalServiceOrder(provisionalServiceOrder);
            releasePaymentAlert(provisionalServiceOrder);
        }

        Map<String,Object> entity=new HashMap<>();
        entity.put("message","successful");
        return entity;
    }

    @Override
    public void releasePaymentScheduler() {
        provisionalServiceOrderService.releasePaymentScheduler();
    }


    public void stopPaymentAlert(ProvisionalServiceOrder provisionalServiceOrder) throws OperationException, ParseException {
        InlineMessageResource inlineMessageResource=new InlineMessageResource();
        inlineMessageResource.setAlertName("STOP_PAYMENT");
        inlineMessageResource.setNotificationType("System");
        ConcurrentHashMap<String,String> entity=new ConcurrentHashMap<>();
        entity.put("provisionalServiceOrderId",provisionalServiceOrder.getUniqueId());
        inlineMessageResource.setDynamicVariables(entity);
        alertService.sendInlineMessageAlert(inlineMessageResource);
    }
    public void releasePaymentAlert(ProvisionalServiceOrder provisionalServiceOrder) throws OperationException, ParseException {
        InlineMessageResource inlineMessageResource=new InlineMessageResource();
        inlineMessageResource.setAlertName("RELEASE_PAYMENT");
        inlineMessageResource.setNotificationType("System");
        ConcurrentHashMap<String,String> entity=new ConcurrentHashMap<>();
        entity.put("provisionalServiceOrderId",provisionalServiceOrder.getUniqueId());
        inlineMessageResource.setDynamicVariables(entity);
        alertService.sendInlineMessageAlert(inlineMessageResource);
    }

}
