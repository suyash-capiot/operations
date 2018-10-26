package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.model.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class OpsActivitiesAdapter { 
	private OpsBookingAdapter parentAdapter;

    public OpsActivitiesAdapter(@Qualifier("OpsBookingAdapter") OpsBookingAdapter newAdapter) {
        parentAdapter = newAdapter;
    }

	public void getOpsActivitiesDetails(OrderDetails orderDetails, OpsOrderDetails opsOrderDetails) {
        ActivityDetails activityDetails = orderDetails.getActivitiesDetails();
		
		OpsActivitiesDetails opsActivityDetails = new OpsActivitiesDetails();
		opsActivityDetails.setActivityDetails(activityDetails.getName());
		opsActivityDetails.setBoardingDropOffPoint(activityDetails.getPickupDropoff().getLocationName());
		opsActivityDetails.setBookingStatus(activityDetails.getStatus());
		opsActivityDetails.setCredentialsName(activityDetails.getClientID());
		opsActivityDetails.setEnableSupplier(activityDetails.getSupplierID());
		String dateTimeOfVisit = activityDetails.getStartDate();
		if(dateTimeOfVisit.contains("["))
			dateTimeOfVisit = dateTimeOfVisit.substring(0, dateTimeOfVisit.indexOf("["));
		
		opsActivityDetails.setDateTimeOfVist(dateTimeOfVisit);
        opsActivityDetails.setInventory(null);
        opsActivityDetails.setPickupDropOff(null);
        opsActivityDetails.setSeatingClass(null);
		opsActivityDetails.setSourceSupplier(activityDetails.getClientID());
        opsActivityDetails.setSupplierRateType(null);
		opsActivityDetails.setSupplierRefNumber(activityDetails.getSupp_booking_reference());
        opsActivityDetails.setTicketType(null);
		
		String endDate = activityDetails.getEndDate();
		if(endDate.contains("["))
			endDate = endDate.substring(0, endDate.indexOf("["));
		
		
		opsActivityDetails.setTimeLimit(endDate);
        opsActivityDetails.setTransferDetails(null);
        opsActivityDetails.setVechileInfo(null);
        opsActivityDetails.setRefundable(false);
		List<OpsPaxParticular> opsPaxParticulars = new ArrayList<>();
		for(int i=0;i<orderDetails.getPaxInfo().size();i++) {
			OpsPaxParticular opsPaxParticular = new OpsPaxParticular();
            PaxInfo paxInfo = orderDetails.getPaxInfo().get(i);
			opsPaxParticular.setPassengerName(paxInfo.getFirstName()+" "+paxInfo.getMiddleName()
					+" "+paxInfo.getLastName());

            opsPaxParticular.setLeadPax(paxInfo.getIsLeadPax());

            opsPaxParticular.setPaxType(paxInfo.getPaxType());

            opsPaxParticular.setAddressDetails(parentAdapter.getOpsAddressDetails(paxInfo.getAddressDetails()));

            opsPaxParticular.setContactDetails(paxInfo.getContactDetails().stream().map(contactDetail
                    -> parentAdapter.getOpsContactDetail(contactDetail)).collect(Collectors.toList()));

            opsPaxParticular.setPaxID(paxInfo.getPaxID());

			opsPaxParticular.setDateOfBirth(orderDetails.getPaxInfo().get(i).getBirthDate());
			OpsSpecialRequestInfo info = new OpsSpecialRequestInfo();
            info.setType(null);
			opsPaxParticular.setMealInfo(info);
			OpsSpecialRequest opsSpecialRequest = new OpsSpecialRequest();
			List<OpsSpecialRequestInfo> opsSpecialRequestInfos = new ArrayList<OpsSpecialRequestInfo>();
			OpsSpecialRequestInfo info2 = new OpsSpecialRequestInfo();
            info2.setType(null);
			opsSpecialRequestInfos.add(info2);
			
			opsSpecialRequest.setSpecialRequestInfo(opsSpecialRequestInfos);
			opsPaxParticular.setSpecialRequest(opsSpecialRequest);

			opsPaxParticulars.add(opsPaxParticular);
		}
		opsActivityDetails.setOpsPaxParticulars(opsPaxParticulars);
		opsOrderDetails.setActivityDetails(opsActivityDetails);
		
		
		OpsActivitiesSupplierPriceInfo  opsActivitiesSupplierPriceInfo = new OpsActivitiesSupplierPriceInfo();
        opsActivitiesSupplierPriceInfo.setCurrencyCode(orderDetails.getOrderSupplierPriceInfo().getCurrencyCode());
        opsActivitiesSupplierPriceInfo.setSupplierPrice(orderDetails.getOrderSupplierPriceInfo().getSupplierPrice());


        List<OpsPaxTypeFare> opsPaxTypeFareSupplierPriceInfo = new ArrayList<OpsPaxTypeFare>();
        List<PaxTypeFare> paxTypeFareSupplierPriceInfo = orderDetails.getOrderSupplierPriceInfo().getPaxTypeFares();

        getPaxTypeFares(opsPaxTypeFareSupplierPriceInfo, paxTypeFareSupplierPriceInfo);

        opsActivitiesSupplierPriceInfo.setOpsPaxTypeFare(opsPaxTypeFareSupplierPriceInfo);
		
		opsActivityDetails.setOpsActivitiesSupplierPriceInfo(opsActivitiesSupplierPriceInfo);
		
		OpsActivitiesTotalPriceInfo opsActivitiesTotalPriceInfo = new OpsActivitiesTotalPriceInfo();
		OpsBaseFare opsBaseFare = new OpsBaseFare();
        if (activityDetails.getOrderClientTotalPriceInfo() != null && activityDetails.getOrderClientTotalPriceInfo().size() >= 1 && activityDetails.getOrderClientTotalPriceInfo().get(0).getBaseFare() != null) {
            opsBaseFare.setAmount(activityDetails.getOrderClientTotalPriceInfo().get(0).getBaseFare().getAmount());
            opsActivitiesTotalPriceInfo.setOpsBaseFare(opsBaseFare);
        }

        if (activityDetails.getOrderClientTotalPriceInfo() != null && activityDetails.getOrderClientTotalPriceInfo().size() >= 1 && activityDetails.getOrderClientTotalPriceInfo().get(0).getTotalFare() != null
                && activityDetails.getOrderClientTotalPriceInfo().get(0).getTotalFare().getAmount() != null)
            opsActivitiesTotalPriceInfo.setTotalPrice(activityDetails.getOrderClientTotalPriceInfo().get(0).getTotalFare().getAmount().toString());

        Receivables receivables = orderDetails.getOrderTotalPriceInfo().getReceivables();
        if(receivables != null) {
            OpsReceivables opsReceivables = new OpsReceivables();
            opsReceivables.setAmount(receivables.getAmount());
            opsReceivables.setCurrencyCode(receivables.getCurrencyCode());
            List<Receivable> receivable = receivables.getReceivable();
            List<OpsReceivable> opsReceivablesChild = new ArrayList<OpsReceivable>();

            for (int i = 0; i < receivable.size(); i++) {
                OpsReceivable recOps = new OpsReceivable();
                Receivable recBE = receivable.get(i);
                recOps.setAmount(recBE.getAmount());
                recOps.setCode(recBE.getCode());
                recOps.setCurrencyCode(recBE.getCurrencyCode());
                opsReceivablesChild.add(recOps);
            }
            opsReceivables.setReceivable(opsReceivablesChild);
            opsActivitiesTotalPriceInfo.setOpsReceivables(opsReceivables);
        }

        if(activityDetails.getOrderClientTotalPriceInfo() != null && activityDetails.getOrderClientTotalPriceInfo().size() > 0)
            opsActivitiesTotalPriceInfo.setCurrencyCode(activityDetails.getOrderClientTotalPriceInfo().get(0).getTotalFare().getCurrencyCode());

        List<OpsPaxTypeFare> opsPaxTypeFare = new ArrayList<OpsPaxTypeFare>();
        List<PaxTypeFare> paxTypeFare = orderDetails.getOrderTotalPriceInfo().getPaxTypeFares();

        getPaxTypeFares(opsPaxTypeFare, paxTypeFare);

        opsActivitiesTotalPriceInfo.setOpsPaxTypeFare(opsPaxTypeFare);
		opsActivityDetails.setOpsActivitiesTotalPriceInfo(opsActivitiesTotalPriceInfo);
		
	}

    private void getPaxTypeFares(List<OpsPaxTypeFare> opsPaxTypeFareSupplierPriceInfo,
                                 List<PaxTypeFare> paxTypeFareSupplierPriceInfo) {
        for (int i = 0; i < paxTypeFareSupplierPriceInfo.size(); i++) {
            OpsPaxTypeFareActivitiesClient opsPax = new OpsPaxTypeFareActivitiesClient();
            PaxTypeFare paxFareBE = paxTypeFareSupplierPriceInfo.get(i);

            BaseFare baseFare = paxFareBE.getBaseFare();

            if(baseFare != null) {

                OpsBaseFare opsBF = new OpsBaseFare();
                opsBF.setAmount(baseFare.getAmount());
                opsBF.setCurrencyCode(baseFare.getCurrencyCode());
                opsPax.setBaseFare(opsBF);

            }

            opsPax.setPaxType(paxFareBE.getPaxType());

            TotalFare totalFare = paxFareBE.getTotalFare();

            if(totalFare != null) {

                OpsTotalFare opsTotalFare = new OpsTotalFare();
                opsTotalFare.setAmount(new Double(totalFare.getAmount()));
                opsTotalFare.setCurrencyCode(totalFare.getCurrencyCode());
                opsPax.setTotalFare(opsTotalFare);

            }


            List<ClientEntityCommercial> clientEntityCommercials = paxFareBE.getClientEntityCommercials();
            if(clientEntityCommercials != null && clientEntityCommercials.size() > 0 ) {

                List<OpsClientEntityCommercial> opsClientEntityCommercials = new ArrayList<OpsClientEntityCommercial>();

                for (int j = 0; j < clientEntityCommercials.size(); j++) {
                    OpsClientEntityCommercial opsClientEntityCommercial = new OpsClientEntityCommercial();
                    ClientEntityCommercial clientEntityCommercial = clientEntityCommercials.get(j);
                    opsClientEntityCommercial.setClientID(clientEntityCommercial.getClientID());
                    opsClientEntityCommercial.setCommercialEntityID(clientEntityCommercial.getCommercialEntityID());
                    opsClientEntityCommercial.setCommercialEntityType(clientEntityCommercial.getCommercialEntityType());
                    opsClientEntityCommercial.setParentClientID(clientEntityCommercial.getClientID());


                    List<ClientCommercial> clientCommercials = clientEntityCommercial.getClientCommercials();
                    List<OpsPaxRoomClientCommercial> opsClientCommercials = new ArrayList<OpsPaxRoomClientCommercial>();

                    for (int k = 0; k < clientCommercials.size(); k++) {
                        OpsPaxRoomClientCommercial opsPaxRoomClientCommercial = new OpsPaxRoomClientCommercial();
                        ClientCommercial clientCommercial = clientCommercials.get(k);
                        opsPaxRoomClientCommercial.setCommercialAmount(Double.parseDouble(clientCommercial.getCommercialAmount()));
                        opsPaxRoomClientCommercial.setCommercialCurrency(clientCommercial.getCommercialCurrency());
                        opsPaxRoomClientCommercial.setCommercialName(clientCommercial.getCommercialName());
                        opsPaxRoomClientCommercial.setCommercialType(clientCommercial.getCommercialType());
                        opsPaxRoomClientCommercial.setCompanyFlag(clientCommercial.getCompanyFlag());
                        opsClientCommercials.add(opsPaxRoomClientCommercial);
                    }

                    opsClientEntityCommercial.setOpsPaxRoomClientCommercial(opsClientCommercials);
                    opsClientEntityCommercials.add(opsClientEntityCommercial);
                }

                opsPax.setOpsClientEntityCommercial(opsClientEntityCommercials);
            }
            opsPaxTypeFareSupplierPriceInfo.add(opsPax);
        }
    }
    
    
    
}
