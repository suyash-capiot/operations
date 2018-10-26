package com.coxandkings.travel.operations.repository.whitelabel;

import com.coxandkings.travel.operations.criteria.whitelabel.WhiteLabelCriteria;
import com.coxandkings.travel.operations.model.whitelabel.WhiteLabel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiteLabelRepository{
    public WhiteLabel saveOrUpdate(WhiteLabel whiteLabel);
    public WhiteLabel getWhiteLabelById(String id);
    public List<WhiteLabel> getAllWhiteLabels();
    public List<WhiteLabel> getWhiteLabelsByCriteria(WhiteLabelCriteria criteria);
    public void deleteById(String id);
}
