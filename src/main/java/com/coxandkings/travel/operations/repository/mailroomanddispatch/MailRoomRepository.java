package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.MailRoomSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.MailroomSearchCriteriaSorted;
import com.coxandkings.travel.operations.model.mailroomanddispatch.MailRoomMaster;

import java.util.List;
import java.util.Map;

public interface MailRoomRepository {

    public MailRoomMaster saveOrUpdate(MailRoomMaster roomMaster);
    public Map<String, Object> getByCriteria(MailRoomSearchCriteria maiRoomCriteria);
    public Map<String, Object> getByCriteriaSorted(MailroomSearchCriteriaSorted mailroomSearchCriteriaSorted);
    public MailRoomMaster getId(String mailRoomId);
    public List<MailRoomMaster> getAllMailRoomDetails();
    public List<MailRoomMaster> getAllMailRoomsSorted(String columnName,String order);
    public List<String> getMailRoomNames(String param);
    public List<String> getMailRooms();
    public MailRoomMaster fetchMailroomMasterForConfigId(String configurationId);
    public MailRoomMaster update(MailRoomMaster mailRoomMaster);
    public int deleteMasterRecord(String configurationId);

}
