package com.coxandkings.travel.operations.service.refund.impl;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.RefundConfiguration;
import com.coxandkings.travel.operations.repository.refund.RefundConfigurationRepository;
import com.coxandkings.travel.operations.resource.refund.RefundConfigurationResource;
import com.coxandkings.travel.operations.service.refund.RefundConfigurationService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RefundConfigurationServiceImpl implements RefundConfigurationService {
    @Autowired
    private RefundConfigurationRepository refundConfigurationRepository;

    @Override
    public RefundConfiguration saveAndUpdate(RefundConfiguration refundConfiguration) throws OperationException {

        if (refundConfiguration != null) {
            return refundConfigurationRepository.saveOrUpdate(refundConfiguration);
        } else {
            throw new OperationException("Refund Configuration should not empty");
        }


    }

    @Override
    public List<RefundConfiguration> findByCriteria(RefundConfiguration refundConfiguration) {
        return refundConfigurationRepository.findByCriteria(refundConfiguration);

    }

    public RefundTypes getRefundConfiguration(RefundConfiguration refundConfiguration) {
        //criteria for clientGroup
        RefundConfiguration refundConfigurationCriteria = new RefundConfiguration();
        refundConfigurationCriteria.setClientName(null);
        refundConfigurationCriteria.setClientSubCategory(null);
        refundConfigurationCriteria.setClientGroup(refundConfiguration.getClientGroup());
        refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
        refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());
        RefundTypes refundTypes = refundTypeCriteriaBuilder(refundConfigurationCriteria);
        if (refundTypes != null) {
            return refundTypes;
        }

        //end of clientGroup

        //criteria for clientName

        refundConfigurationCriteria = new RefundConfiguration();
        refundConfigurationCriteria.setClientName(refundConfiguration.getClientName());
        refundConfigurationCriteria.setClientSubCategory(null);
        refundConfigurationCriteria.setClientGroup(null);
        refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
        refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());
        refundTypes = refundTypeCriteriaBuilder(refundConfigurationCriteria);
        if (refundTypes != null) {
            return refundTypes;
        }
        //end of clientName

        //criteria for clientSubCategory
        refundConfigurationCriteria = new RefundConfiguration();
        refundConfigurationCriteria.setClientName(null);
        refundConfigurationCriteria.setClientCategory(refundConfiguration.getClientCategory());
        refundConfigurationCriteria.setClientSubCategory(refundConfiguration.getClientSubCategory());
        refundConfigurationCriteria.setClientGroup(null);
        refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
        refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());
        refundTypes = refundTypeCriteriaBuilder(refundConfigurationCriteria);
        if (refundTypes != null) {
            return refundTypes;
        }

        //end of clientSubCategory

        //start criteria to  search at Client Category
        //if (refundConfiguration.getClientName() == null && refundConfiguration.getClientGroup() == null && refundConfiguration.getClientSubCategory() == null && refundConfiguration.getClientCategory() != null && refundConfiguration.getClientType() != null && refundConfiguration.getCompanyMarket() != null) {

        //criteria for client Category
        refundConfigurationCriteria = new RefundConfiguration();
        refundConfigurationCriteria.setClientName(null);
        refundConfigurationCriteria.setClientCategory(refundConfiguration.getClientCategory());
        refundConfigurationCriteria.setClientSubCategory(null);
        refundConfigurationCriteria.setClientGroup(null);
        refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
        refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());
        refundTypes = refundTypeCriteriaBuilder(refundConfigurationCriteria);
        if (refundTypes != null) {
            return refundTypes;
        }
        //end of client category


        //start criteria to  search at Client Type

        refundConfigurationCriteria = new RefundConfiguration();
        refundConfigurationCriteria.setClientGroup(null);
        refundConfigurationCriteria.setClientName(null);
        refundConfigurationCriteria.setClientSubCategory(null);
        refundConfigurationCriteria.setClientCategory(null);
        refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
        refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

        refundTypes = refundTypeCriteriaBuilder(refundConfigurationCriteria);
        if (refundTypes != null) {
            return refundTypes;
        }

        //end of criteria to  search at Client Type

        //start of company market


        refundConfigurationCriteria = new RefundConfiguration();
        refundConfigurationCriteria.setClientGroup(null);
        refundConfigurationCriteria.setClientName(null);
        refundConfigurationCriteria.setClientSubCategory(null);
        refundConfigurationCriteria.setClientCategory(null);
        refundConfigurationCriteria.setClientType(null);
        refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());
        refundTypes = refundTypeCriteriaBuilder(refundConfigurationCriteria);
        if (refundTypes != null) {
            return refundTypes;
        }


        //end of criteria to  search at company market


        return null;
    }

    private RefundTypes refundTypeCriteriaBuilder(RefundConfiguration refundConfiguration) {
        RefundConfiguration refundConfigurationCriteria;
        List<RefundConfiguration> refundConfigurationList = null;
        String clientGroup = refundConfiguration.getClientGroup();
        //start criteria to  search at clientGroup
        if (refundConfiguration.getClientName() == null && refundConfiguration.getClientSubCategory() == null && refundConfiguration.getClientGroup() != null && refundConfiguration.getClientType() != null && refundConfiguration.getCompanyMarket() != null) {
            refundConfigurationCriteria = new RefundConfiguration();
            refundConfigurationCriteria.setClientGroup(clientGroup);
            refundConfigurationCriteria.setClientName("");
            refundConfigurationCriteria.setClientSubCategory("");
            refundConfigurationCriteria.setClientCategory(refundConfiguration.getClientCategory());
            refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
            refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

            refundConfigurationList = findByCriteria(refundConfigurationCriteria);
            if (null != refundConfigurationList && refundConfigurationList.size() == 1) {
                return refundConfigurationList.get(0).getRefundTypes();
            }
        }

        //end of criteria to  search at clientGroup

        //start criteria to  search at clientName
        if (refundConfiguration.getClientName() != null && refundConfiguration.getClientSubCategory() != null && refundConfiguration.getClientGroup() == null && refundConfiguration.getClientType() != null && refundConfiguration.getCompanyMarket() != null) {


            refundConfigurationCriteria = new RefundConfiguration();
            refundConfigurationCriteria.setClientGroup("");
            refundConfigurationCriteria.setClientName(refundConfiguration.getClientName());
            refundConfigurationCriteria.setClientSubCategory(refundConfiguration.getClientSubCategory());
            refundConfigurationCriteria.setClientCategory(refundConfiguration.getClientCategory());
            refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
            refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

            refundConfigurationList = refundConfigurationRepository.findByCriteria(refundConfigurationCriteria);
            if (null != refundConfigurationList && refundConfigurationList.size() == 1) {
                return refundConfigurationList.get(0).getRefundTypes();
            }
        }
        //end of criteria to  search at clientName


        //start criteria to  search at Client Sub Category
        if (refundConfiguration.getClientName() == null && refundConfiguration.getClientGroup() == null && refundConfiguration.getClientSubCategory() != null && refundConfiguration.getClientType() != null && refundConfiguration.getCompanyMarket() != null) {
            refundConfigurationCriteria = new RefundConfiguration();
            refundConfigurationCriteria.setClientGroup("");
            refundConfigurationCriteria.setClientName("");
            refundConfigurationCriteria.setClientSubCategory(refundConfiguration.getClientSubCategory());
            refundConfigurationCriteria.setClientCategory(refundConfiguration.getClientCategory());
            refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
            refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

            refundConfigurationList = refundConfigurationRepository.findByCriteria(refundConfigurationCriteria);
            if (null != refundConfigurationList && refundConfigurationList.size() == 1) {
                return refundConfigurationList.get(0).getRefundTypes();
            }
        }
        //end of criteria to  search at Client Sub Category

        //start criteria to  search at Client Category
        if (refundConfiguration.getClientName() == null && refundConfiguration.getClientGroup() == null && refundConfiguration.getClientSubCategory() == null && refundConfiguration.getClientCategory() != null && refundConfiguration.getClientType() != null && refundConfiguration.getCompanyMarket() != null) {
            refundConfigurationCriteria = new RefundConfiguration();
            refundConfigurationCriteria.setClientGroup("");
            refundConfigurationCriteria.setClientName("");
            refundConfigurationCriteria.setClientSubCategory("");
            refundConfigurationCriteria.setClientCategory(refundConfiguration.getClientCategory());
            refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
            refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

            refundConfigurationList = refundConfigurationRepository.findByCriteria(refundConfigurationCriteria);
            if (null != refundConfigurationList && refundConfigurationList.size() == 1) {
                return refundConfigurationList.get(0).getRefundTypes();
            }
        }
        //end of criteria to  search at Client Category

        //start criteria to  search at Client Type
        if (refundConfiguration.getClientName() == null && refundConfiguration.getClientGroup() == null && refundConfiguration.getClientSubCategory() == null && refundConfiguration.getClientCategory() == null && refundConfiguration.getClientType() != null && refundConfiguration.getCompanyMarket() != null) {
            refundConfigurationCriteria = new RefundConfiguration();
            refundConfigurationCriteria.setClientGroup("");
            refundConfigurationCriteria.setClientName("");
            refundConfigurationCriteria.setClientSubCategory("");
            refundConfigurationCriteria.setClientCategory("");
            refundConfigurationCriteria.setClientType(refundConfiguration.getClientType());
            refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

            refundConfigurationList = refundConfigurationRepository.findByCriteria(refundConfigurationCriteria);
            if (null != refundConfigurationList && refundConfigurationList.size() == 1) {
                return refundConfigurationList.get(0).getRefundTypes();
            }
        }
        //end of criteria to  search at Client Type

        //start of company market
        if (refundConfiguration.getClientName() == null && refundConfiguration.getClientGroup() == null && refundConfiguration.getClientSubCategory() == null && refundConfiguration.getClientCategory() == null && refundConfiguration.getClientType() == null && refundConfiguration.getCompanyMarket() != null) {
            //start criteria to  search at Company market
            refundConfigurationCriteria = new RefundConfiguration();
            refundConfigurationCriteria.setClientGroup("");
            refundConfigurationCriteria.setClientName("");
            refundConfigurationCriteria.setClientSubCategory("");
            refundConfigurationCriteria.setClientCategory("");
            refundConfigurationCriteria.setClientType("");
            refundConfigurationCriteria.setCompanyMarket(refundConfiguration.getCompanyMarket());

            refundConfigurationList = refundConfigurationRepository.findByCriteria(refundConfigurationCriteria);
            if (null != refundConfigurationList && refundConfigurationList.size() == 1) {
                return refundConfigurationList.get(0).getRefundTypes();
            }
        }

        //end of criteria to  search at company market
        return null;
    }
}
