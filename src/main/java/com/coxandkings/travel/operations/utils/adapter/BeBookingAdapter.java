package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.model.core.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BeBookingAdapter {

    public List<OrderClientCommercial> getOrderClientCommercials(List<OpsOrderClientCommercial> oPsClientCommercials) {
        List<OrderClientCommercial> orderClientCommercials = new ArrayList<>();
        if (oPsClientCommercials != null && oPsClientCommercials.size() > 0) {

            for (OpsOrderClientCommercial opsOrderClientCommercial : oPsClientCommercials) {

                OrderClientCommercial orderClientCommercial = new OrderClientCommercial();
                orderClientCommercial.setClientID(opsOrderClientCommercial.getClientID());
                orderClientCommercial.setClientCommId(opsOrderClientCommercial.getClientCommId());
                orderClientCommercial.setCommercialAmount(opsOrderClientCommercial.getCommercialAmount());
                orderClientCommercial.setCommercialEntityType(opsOrderClientCommercial.getCommercialEntityType());
                orderClientCommercial.setCommercialName(opsOrderClientCommercial.getCommercialName());
                orderClientCommercial.setCompanyFlag(opsOrderClientCommercial.getCompanyFlag());
                orderClientCommercial.setCommercialCurrency(opsOrderClientCommercial.getCommercialCurrency());
                orderClientCommercial.setCommercialType(opsOrderClientCommercial.getCommercialType());
                orderClientCommercial.setCommercialEntityID(opsOrderClientCommercial.getCommercialEntityID());
                orderClientCommercial.setParentClientID(opsOrderClientCommercial.getParentClientID());
                orderClientCommercial.setEligible(opsOrderClientCommercial.isEligible());
                
                orderClientCommercials.add(orderClientCommercial);
            }

        }
        return orderClientCommercials;
    }

    public List<OrderSupplierCommercial> getOrderSupplierCommercials(List<OpsOrderSupplierCommercial> opsOrderSupplierCommercials) {
        List<OrderSupplierCommercial> orderSupplierCommercials = new ArrayList<>();
        if (opsOrderSupplierCommercials != null && opsOrderSupplierCommercials.size() > 0) {

            for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercials) {
                OrderSupplierCommercial orderSupplierCommercial = new OrderSupplierCommercial();
                orderSupplierCommercial.setCommercialAmount(opsOrderSupplierCommercial.getCommercialAmount());
                orderSupplierCommercial.setCommercialCurrency(opsOrderSupplierCommercial.getCommercialCurrency());
                orderSupplierCommercial.setCommercialName(opsOrderSupplierCommercial.getCommercialName());
                orderSupplierCommercial.setCommercialType(opsOrderSupplierCommercial.getCommercialType());
                orderSupplierCommercial.setSuppCommId(opsOrderSupplierCommercial.getSuppCommId());
                orderSupplierCommercial.setEligible(opsOrderSupplierCommercial.isEligible());
                orderSupplierCommercial.setSupplierID(opsOrderSupplierCommercial.getSupplierID());
                orderSupplierCommercials.add(orderSupplierCommercial);
            }

        }
        return orderSupplierCommercials;
    }

    public OrderTotalPriceInfo getOrderTotalPriceInfo(OpsFlightTotalPriceInfo opsFlightTotalPriceInfo) {
        OrderTotalPriceInfo orderTotalPriceInfo = new OrderTotalPriceInfo();
        orderTotalPriceInfo.setCurrencyCode(opsFlightTotalPriceInfo.getCurrencyCode());
        orderTotalPriceInfo.setTotalPrice(opsFlightTotalPriceInfo.getTotalPrice());
        orderTotalPriceInfo.setBaseFare(getBaseFare(opsFlightTotalPriceInfo.getBaseFare()));
        orderTotalPriceInfo.setFees(getFees(opsFlightTotalPriceInfo.getFees()));
        orderTotalPriceInfo.setTotalPrice(opsFlightTotalPriceInfo.getTotalPrice());
        orderTotalPriceInfo.setCurrencyCode(opsFlightTotalPriceInfo.getCurrencyCode());
        orderTotalPriceInfo.setPaxTypeFares(getPaxTypeFares(opsFlightTotalPriceInfo.getPaxTypeFares()));
        orderTotalPriceInfo.setTaxes(getTaxes(opsFlightTotalPriceInfo.getTaxes()));
        orderTotalPriceInfo.setReceivables(getReceivables(opsFlightTotalPriceInfo.getReceivables()));
        
        orderTotalPriceInfo.setDiscounts(opsFlightTotalPriceInfo.getDiscounts());
        orderTotalPriceInfo.setIncentives(opsFlightTotalPriceInfo.getIncentives());
        orderTotalPriceInfo.setCompanyTaxes(getCompanyTaxes(opsFlightTotalPriceInfo.getCompanyTaxes()));
        
        return orderTotalPriceInfo;


    }

    public Receivables getReceivables(OpsReceivables opsReceivables) {
        Receivables receivables = new Receivables();
        if (opsReceivables != null) {
            receivables.setReceivable(getReceivable(opsReceivables.getReceivable()));
            receivables.setCurrencyCode(opsReceivables.getCurrencyCode());
            receivables.setAmount(opsReceivables.getAmount());
        }
        return receivables;

    }

    private List<Receivable> getReceivable(List<OpsReceivable> opsReceivableList) {
        List<Receivable> receivableList = null;
        if (opsReceivableList != null && opsReceivableList.size() > 0) {
            receivableList = new ArrayList<>();
            for (OpsReceivable opsReceivable : opsReceivableList) {
                Receivable receivable = new Receivable();
                receivable.setCode(opsReceivable.getCode());
                receivable.setCurrencyCode(opsReceivable.getCurrencyCode());
                receivable.setAmount(opsReceivable.getAmount());
                receivableList.add(receivable);

            }
        }
        return receivableList;
    }

    public Taxes getTaxes(OpsTaxes opsTaxes) {
        Taxes taxes = null;
        if (opsTaxes != null) {
            taxes = new Taxes();
            taxes.setAmount(opsTaxes.getAmount());
            taxes.setCurrencyCode(opsTaxes.getCurrencyCode());
            taxes.setTax(getTax(opsTaxes.getTax()));
        }
        return taxes;
    }

    private List<Tax> getTax(List<OpsTax> opsTaxList) {
        List<Tax> taxes = null;
        if (opsTaxList != null && opsTaxList.size() > 0) {
            taxes = new ArrayList<>();
            for (OpsTax opsTax : opsTaxList) {
                Tax tax = new Tax();
                tax.setAmount(opsTax.getAmount());
                tax.setCurrencyCode(opsTax.getCurrencyCode());
                tax.setTaxCode(opsTax.getTaxCode());
                taxes.add(tax);
            }
        }
        return taxes;
    }


    private List<PaxTypeFare> getPaxTypeFares(List<OpsPaxTypeFareFlightClient> paxTypeFares) {
        List<PaxTypeFare> paxTypeFareList = null;
        if (paxTypeFares != null && paxTypeFares.size() > 0) {
            paxTypeFareList = new ArrayList<>();
            for (OpsPaxTypeFareFlightClient opsPaxTypeFareFlightClient : paxTypeFares) {
                PaxTypeFare paxTypeFareFlightClient = new PaxTypeFare();
                paxTypeFareFlightClient.setBaseFare(getBaseFare(opsPaxTypeFareFlightClient.getBaseFare()));
                paxTypeFareFlightClient.setPaxType(opsPaxTypeFareFlightClient.getPaxType());
                paxTypeFareFlightClient.setTaxes(getTaxes(opsPaxTypeFareFlightClient.getTaxes()));
                paxTypeFareFlightClient.setTotalFare(getTotalFare(opsPaxTypeFareFlightClient.getTotalFare()));
                paxTypeFareFlightClient.setFees(getFees(opsPaxTypeFareFlightClient.getFees()));
                paxTypeFareFlightClient.setClientEntityCommercials(getClientEntityCommercials(opsPaxTypeFareFlightClient.getOpsClientEntityCommercial()));
                paxTypeFareList.add(paxTypeFareFlightClient);
            }

        }

        return paxTypeFareList;
    }

    public List<ClientEntityCommercial> getClientEntityCommercials(List<OpsClientEntityCommercial> opsClientEntityCommercials) {
        List<ClientEntityCommercial> clientEntityCommercials = null;
        if (opsClientEntityCommercials != null && opsClientEntityCommercials.size() > 0) {
            clientEntityCommercials = new ArrayList<>();
            for (OpsClientEntityCommercial opsClientEntityCommercial : opsClientEntityCommercials) {
                ClientEntityCommercial clientEntityCommercial = new ClientEntityCommercial();
                
                clientEntityCommercial.setClientID(opsClientEntityCommercial.getClientID());
                clientEntityCommercial.setClientMarket(opsClientEntityCommercial.getClientMarket());
                clientEntityCommercial.setCommercialEntityID(opsClientEntityCommercial.getCommercialEntityID());
                clientEntityCommercial.setCommercialEntityMarket(opsClientEntityCommercial.getCommercialEntityMarket());
                clientEntityCommercial.setCommercialEntityType(opsClientEntityCommercial.getCommercialEntityType());
                clientEntityCommercial.setEntityName(opsClientEntityCommercial.getEntityName());
                clientEntityCommercial.setParentClientID(opsClientEntityCommercial.getParentClientID());
                
                
                clientEntityCommercial.setClientCommercials(getPaxRoomClientCommercials(opsClientEntityCommercial.getOpsPaxRoomClientCommercial()));
                clientEntityCommercials.add(clientEntityCommercial);
            }
        }
        return clientEntityCommercials;
    }

    public List<ClientCommercial> getPaxRoomClientCommercials(List<OpsPaxRoomClientCommercial> opsPaxRoomClientCommercials) {
        List<ClientCommercial> clientCommercials = null;
        if (opsPaxRoomClientCommercials != null && opsPaxRoomClientCommercials.size() > 0) {
            clientCommercials = new ArrayList<>();
            for (OpsPaxRoomClientCommercial opsPaxRoomClientCommercial : opsPaxRoomClientCommercials) {
                ClientCommercial clientCommercial = new ClientCommercial();
                clientCommercial.setCommercialAmount(opsPaxRoomClientCommercial.getCommercialAmount().toString());
                clientCommercial.setCommercialCurrency(opsPaxRoomClientCommercial.getCommercialCurrency());
                clientCommercial.setCommercialName(opsPaxRoomClientCommercial.getCommercialName());
                clientCommercial.setCommercialType(opsPaxRoomClientCommercial.getCommercialType());
                clientCommercial.setCommercialCalculationAmount(opsPaxRoomClientCommercial.getCommercialCalculationAmount());
                clientCommercial.setCommercialCalculationPercentage(opsPaxRoomClientCommercial.getCommercialCalculationPercentage());
                clientCommercial.setMdmRuleID(opsPaxRoomClientCommercial.getMdmRuleID());
                clientCommercial.setCommercialFareComponent(opsPaxRoomClientCommercial.getCommercialFareComponent());
                clientCommercial.setRemainingAmount(opsPaxRoomClientCommercial.getRemainingAmount());
                clientCommercial.setRemainingPercentageAmount(opsPaxRoomClientCommercial.getRemainingPercentageAmount());
                clientCommercial.setRetentionAmountPercentage(opsPaxRoomClientCommercial.getRetentionAmountPercentage());
                clientCommercial.setRetentionPercentage(opsPaxRoomClientCommercial.getRetentionPercentage());
                clientCommercials.add(clientCommercial);
            }
        }
        return clientCommercials;
    }

    public Fees getFees(OpsFees opsFee) {
        Fees fees = null;
        if (opsFee != null)
            fees = new Fees();
        fees.setAmount(opsFee.getTotal());
        fees.setCurrencyCode(opsFee.getCurrencyCode());
        fees.setFee(getFee(opsFee.getFee()));
        return fees;
    }

    public List<Fee> getFee(List<OpsFee> opsFees) {
        List<Fee> feeList = null;
        if (opsFees != null && opsFees.size() > 0) {
            feeList = new ArrayList<>();
            for (OpsFee opsFee : opsFees) {
                Fee fee = new Fee();
                fee.setAmount(opsFee.getAmount());
                fee.setCurrencyCode(opsFee.getCurrencyCode());
                fee.setFeeCode(opsFee.getFeeCode());
                feeList.add(fee);
            }

        }
        return feeList;
    }

    public BaseFare getBaseFare(OpsBaseFare opsBasefare) {
        BaseFare baseFare = new BaseFare();
        if (opsBasefare != null) {
            baseFare.setAmount(opsBasefare.getAmount());
            baseFare.setCurrencyCode(opsBasefare.getCurrencyCode());
        }
        return baseFare;
    }

    public OrderSupplierPriceInfo getOrderSupplierPriceInfo(OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo) {
        OrderSupplierPriceInfo orderSupplierPriceInfo = new OrderSupplierPriceInfo();
        orderSupplierPriceInfo.setCurrencyCode(opsFlightSupplierPriceInfo.getCurrencyCode());
        orderSupplierPriceInfo.setSupplierPrice(opsFlightSupplierPriceInfo.getSupplierPrice());
        orderSupplierPriceInfo.setPaxTypeFares(getOpsPaxTypeFlightFare(opsFlightSupplierPriceInfo.getPaxTypeFares()));
        return orderSupplierPriceInfo;
    }

    public List<PaxTypeFare> getOpsPaxTypeFlightFare(List<OpsPaxTypeFareFlightSupplier> paxTypeFares) {
        List<PaxTypeFare> paxTypeFareList = new ArrayList<>();
        if (paxTypeFares != null && paxTypeFares.size() > 0) {
            for (OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier : paxTypeFares) {
                PaxTypeFare paxTypeFare = new PaxTypeFare();
                paxTypeFare.setBaseFare(getBaseFare(opsPaxTypeFareFlightSupplier.getBaseFare()));
                paxTypeFare.setTotalFare(getTotalFare(opsPaxTypeFareFlightSupplier.getTotalFare()));
                paxTypeFare.setTaxes(getTaxes(opsPaxTypeFareFlightSupplier.getTaxes()));
                paxTypeFare.setPaxType(opsPaxTypeFareFlightSupplier.getPaxType());
                paxTypeFare.setFees(getFees(opsPaxTypeFareFlightSupplier.getFees()));
                paxTypeFare.setSupplierCommercials(getOpsFlightPaxSupplierCommercials(opsPaxTypeFareFlightSupplier.getSupplierCommercials()));
                paxTypeFareList.add(paxTypeFare);
            }
        }
        return paxTypeFareList;
    }

    public List<SupplierCommercial> getOpsFlightPaxSupplierCommercials(List<OpsFlightPaxSupplierCommercial> opsFlightPaxSupplierCommercials) {
        List<SupplierCommercial> supplierCommercials = new ArrayList<>();
        if (opsFlightPaxSupplierCommercials != null && opsFlightPaxSupplierCommercials.size() > 0) {
            for (OpsFlightPaxSupplierCommercial opsFlightPaxSupplierCommercial : opsFlightPaxSupplierCommercials) {
                SupplierCommercial supplierCommercial = new SupplierCommercial();
                supplierCommercial.setCommercialType(opsFlightPaxSupplierCommercial.getCommercialType());
                supplierCommercial.setCommercialName(opsFlightPaxSupplierCommercial.getCommercialName());
                supplierCommercial.setCommercialCurrency(opsFlightPaxSupplierCommercial.getCommercialCurrency());
                supplierCommercial.setCommercialAmount(opsFlightPaxSupplierCommercial.getCommercialAmount());
                
                supplierCommercial.setMdmRuleID(opsFlightPaxSupplierCommercial.getMdmRuleID());
                supplierCommercial.setCommercialCalculationAmount(opsFlightPaxSupplierCommercial.getCommercialCalculationAmount());
                supplierCommercial.setCommercialCalculationPercentage(opsFlightPaxSupplierCommercial.getCommercialCalculationPercentage());
                supplierCommercial.setCommercialFareComponent(opsFlightPaxSupplierCommercial.getCommercialFareComponent());
                supplierCommercial.setSupplierID(opsFlightPaxSupplierCommercial.getSupplierID());
                supplierCommercials.add(supplierCommercial);
            }

        }
        return supplierCommercials;
    }

    public List<SupplierCommercial> getPaxTypeFareFlightSupplier(List<OpsFlightPaxSupplierCommercial> opsFlightPaxSupplierCommercials) {
        List<SupplierCommercial> supplierCommercials = null;
        if (opsFlightPaxSupplierCommercials != null && opsFlightPaxSupplierCommercials.size() > 0) {
            supplierCommercials = new ArrayList<>();
            for (OpsFlightPaxSupplierCommercial opsFlightPaxSupplierCommercial : opsFlightPaxSupplierCommercials) {
                SupplierCommercial supplierCommercial = new SupplierCommercial();
                supplierCommercial.setCommercialAmount(opsFlightPaxSupplierCommercial.getCommercialAmount());
                supplierCommercial.setCommercialCurrency(opsFlightPaxSupplierCommercial.getCommercialCurrency());
                supplierCommercial.setCommercialName(opsFlightPaxSupplierCommercial.getCommercialName());
                supplierCommercial.setCommercialType(opsFlightPaxSupplierCommercial.getCommercialType());
                
                supplierCommercial.setMdmRuleID(opsFlightPaxSupplierCommercial.getMdmRuleID());
                supplierCommercial.setCommercialCalculationAmount(opsFlightPaxSupplierCommercial.getCommercialCalculationPercentage());
                supplierCommercial.setCommercialCalculationPercentage(opsFlightPaxSupplierCommercial.getCommercialCalculationPercentage());
                supplierCommercial.setCommercialFareComponent(opsFlightPaxSupplierCommercial.getCommercialFareComponent());
                
                supplierCommercials.add(supplierCommercial);
            }
        }
        return supplierCommercials;
    }

    public TotalFare getTotalFare(OpsTotalFare opsTotalFare) {
        TotalFare totalFare = new TotalFare();
        totalFare.setAmount(opsTotalFare.getAmount());
        totalFare.setCurrencyCode(opsTotalFare.getCurrencyCode());
        return totalFare;
    }
    
    
    public CompanyTaxes getCompanyTaxes(OpsCompanyTaxes opsCompanyTaxes) {

    	CompanyTaxes companyTaxes = null;

        if (opsCompanyTaxes != null) {
        	companyTaxes = new CompanyTaxes();
        	companyTaxes.setCurrencyCode(opsCompanyTaxes.getCurrencyCode());
        	companyTaxes.setAmount(opsCompanyTaxes.getAmount());

            if (opsCompanyTaxes.getCompanyTax() != null && opsCompanyTaxes.getCompanyTax().size() > 0)
            	companyTaxes.setCompanyTax(opsCompanyTaxes.getCompanyTax().stream().map(tax1 -> getOpsCompanyTax(tax1)).collect(Collectors.toList()));
            else
            	companyTaxes.setCompanyTax(new ArrayList<CompanyTax>());
        }
        return companyTaxes;
    }

    public CompanyTax getOpsCompanyTax(OpsCompanyTax opsTax) {

    	CompanyTax tax = new CompanyTax();

        tax.setCurrencyCode(opsTax.getCurrencyCode());
        tax.setAmount(opsTax.getAmount());
        tax.setTaxCode(opsTax.getTaxCode());
        tax.setHsnCode(opsTax.getHsnCode());
        tax.setSacCode(opsTax.getSacCode());
        tax.setTaxComponent(opsTax.getTaxComponent());
        tax.setTaxPercent(opsTax.getTaxPercent());
        return tax;
    }
}
