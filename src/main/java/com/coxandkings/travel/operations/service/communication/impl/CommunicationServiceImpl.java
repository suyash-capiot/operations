package com.coxandkings.travel.operations.service.communication.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationTagCriteria;
import com.coxandkings.travel.operations.criteria.communication.CommunicationUnreadCriteria;
import com.coxandkings.travel.operations.enums.communication.UserProfileType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.repository.activitylog.ActivityLogRepository;
import com.coxandkings.travel.operations.repository.callrecord.CallRecordRepository;
import com.coxandkings.travel.operations.repository.chat.ChatRepository;
import com.coxandkings.travel.operations.repository.communication.CommunicationRepository;
import com.coxandkings.travel.operations.repository.communication.CommunicationTagRepository;
import com.coxandkings.travel.operations.repository.email.EmailRepository;
import com.coxandkings.travel.operations.repository.letter.LetterRepository;
import com.coxandkings.travel.operations.service.communication.CommunicationService;
import com.coxandkings.travel.operations.service.communication.MDMService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private CommunicationTagRepository communicationTagRepository;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    private MDMService mdmService;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private LetterRepository letterRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private CallRecordRepository callRecordRepository;

    @Override
    public Map<String, Object> getCommunicationByBookingId(String bookId, Integer size, Integer page) {
        size = (size == null) ? 10 : size;
        page = (page == null) ? 1 : page;
        return communicationRepository.getCommunicationByBookingId(bookId, size, page);
    }

    @Override
    public Map<String, Object> getCommunicationByUserId(String userId, Integer size, Integer page, String sort) {
        size = (size == null) ? 10 : size;
        page = (page == null) ? 1 : page;
        return communicationRepository.getCommunicationByUserId(userId, size, page, sort);
    }

    @Override
    public Map<String, Object> getCommunicationByBookingId(String bookId, Integer size, Integer page, String sort) {
        size = (size == null) ? 10 : size;
        page = (page == null) ? 1 : page;
        return communicationRepository.getCommunicationByBookingId(bookId, size, page, sort);
    }

    @Override
    public Map<String, Object> getCommunicationByUserId(String userId, Integer size, Integer page) {
        size = (size == null) ? 10 : size;
        page = (page == null) ? 1 : page;
        return communicationRepository.getCommunicationByUserId(userId, size, page);
    }

    @Override
    public List<BaseCommunication> getCommunicationByCommunicationTagsCriteria(CommunicationTagCriteria criteria) {
        List<CommunicationTags> communicationTags = communicationTagRepository.getCommunicationByTAGCriteria(criteria);
        List<BaseCommunication> communications = new ArrayList<>();
        if (communicationTags != null && communicationTags.size() > 0) {
            for (CommunicationTags communicationTag : communicationTags) {
                communications.add(communicationRepository.getCommunicationById(communicationTag.getBaseCommunication().getId()));
            }
        }
        return communications;
    }

/*
    CHAT,
    LETTER,
    SMS,
    ACTIVITYLOG,
    FAX,
    CALLRECORD*/


    @Override
    public Map<String, Object> getCommunicationByCriteria(CommunicationCriteria criteria) throws OperationException {
        Map<String, Object> map = null;
        /*List<? extends BaseCommunication> baseCommunications = null;
        CommunicationType communicationType = CommunicationType.valueOf(criteria.getCommunicationType());
        switch (communicationType) {
            case ACTIVITYLOG:
                baseCommunications = activityLogRepository.getByCriteria(criteria);
                break;
            case LETTER:

                baseCommunications = letterRepository.getByCriteria(criteria);
                break;

            case SMS:

                break;


            case FAX:
                break;

            case CALLRECORD:
                    baseCommunications = callRecordRepository.getByCriteria(criteria);
                break;
            case CHAT:
                 baseCommunications = chatRepository.getByCriteria(criteria);
                break;


        }*//*List<? extends BaseCommunication> baseCommunications = null;
        CommunicationType communicationType = CommunicationType.valueOf(criteria.getCommunicationType());
        switch (communicationType) {
            case ACTIVITYLOG:
                baseCommunications = activityLogRepository.getByCriteria(criteria);
                break;
            case LETTER:

                baseCommunications = letterRepository.getByCriteria(criteria);
                break;

            case SMS:

                break;


            case FAX:
                break;

            case CALLRECORD:
                    baseCommunications = callRecordRepository.getByCriteria(criteria);
                break;
            case CHAT:
                 baseCommunications = chatRepository.getByCriteria(criteria);
                break;


        }*//*List<? extends BaseCommunication> baseCommunications = null;
        CommunicationType communicationType = CommunicationType.valueOf(criteria.getCommunicationType());
        switch (communicationType) {
            case ACTIVITYLOG:
                baseCommunications = activityLogRepository.getByCriteria(criteria);
                break;
            case LETTER:

                baseCommunications = letterRepository.getByCriteria(criteria);
                break;

            case SMS:

                break;


            case FAX:
                break;

            case CALLRECORD:
                    baseCommunications = callRecordRepository.getByCriteria(criteria);
                break;
            case CHAT:
                 baseCommunications = chatRepository.getByCriteria(criteria);
                break;


        }*/
        return map = communicationRepository.getCommunicationByCriteria(criteria);
        // return applyPagination(baseCommunications,1,10,baseCommunications!=null?baseCommunications.size():0);
    }


    @Override
    public String getTemplateDetails(String businessProcess, String function) throws OperationException {
        return templateLoaderService.getTemplateDetails(businessProcess, function);
    }

    @Override
    public String getCommunicationTemplate(String templateId) throws OperationException, JSONException {
        return templateLoaderService.getTemplateBodyByID(templateId);
    }

    @Override
    public List<String> getListEmailIds(String criteria, UserProfileType userProfileType) throws OperationException {
        List<String> emailList = null;
        if (UserProfileType.SUPPLIER == userProfileType) {
            mdmService.searchSupplierEmail(criteria);
        } else if (UserProfileType.B2B == userProfileType) {
            emailList = mdmService.searchB2BEmail(criteria);
        } else if (UserProfileType.B2C == userProfileType) {
            emailList = mdmService.searchB2CEmail(criteria);

        } else if (UserProfileType.INTERNAL == userProfileType) {
            emailList = mdmService.searchB2EEmail(criteria);
        }
        return emailList;
    }

    @Override
    public Long countTotalUnread(List<BaseCommunication> communications) {
        Long unread = 0L;
        if (communications != null && communications.size() > 0) {
            for (BaseCommunication communication : communications) {
                if (!communication.getRead())
                    unread++;
            }
        }
        return unread;
    }

    @Override
    public Map<String, Long> getUnreadCounts(CommunicationUnreadCriteria communicationUnreadCriteria) {
        Map<String, Long> unreadCount = communicationRepository.getUnreadCounts(communicationUnreadCriteria);
        return unreadCount;
    }

    private Map<String, Object> applyPagination(List<? extends BaseCommunication> baseCommunications, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entiy = new HashMap<>();
        entiy.put("result", baseCommunications);

        if (baseCommunications.isEmpty()) return entiy;

        entiy.put("size", size);
        entiy.put("page", page);

        Integer noOfPages = 0;
        actualSize = (null == actualSize) ? 0 : actualSize;
        size = (null == size) ? 0 : size;
        if (actualSize % size == 0)
            noOfPages = actualSize / size;
        else noOfPages = actualSize / size + 1;

        entiy.put("size", size);
        entiy.put("page", page);
        entiy.put("noOfPages", noOfPages);
        return entiy;
    }

}
