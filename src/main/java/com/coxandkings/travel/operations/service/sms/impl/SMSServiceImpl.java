package com.coxandkings.travel.operations.service.sms.impl;

import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.sms.InboundSMS;
import com.coxandkings.travel.operations.model.sms.OutboundSMS;
import com.coxandkings.travel.operations.repository.sms.ReceiveMessageRepository;
import com.coxandkings.travel.operations.repository.sms.SendMessageRepository;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.sms.OutboundSMSResource;
import com.coxandkings.travel.operations.service.sms.SMSService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
public class SMSServiceImpl implements SMSService {

    @Autowired
    private SendMessageRepository sendMessageRepository;

    @Autowired
    private ReceiveMessageRepository receiveMessageRepository;

    @Value("${sms.authId}")
    private String authId;

    @Value("${sms.authToken}")
    private String authToken;

    @Value("${sms.send}")
    private String sendURL;

    @Value("${sms.feedid}")
    private Integer feedid;

    @Value("${sms.senderid}")
    private String senderid;

    @Value("${sms.username}")
    private String username;

    @Value("${sms.password}")
    private String password;

    @Override
    public OutboundSMS sendMsg(OutboundSMSResource outboundSMSResource) throws OperationException {
        OutboundSMS message = new OutboundSMS();
        message.setSender(outboundSMSResource.getMessageFrom());
        message.setRecipientList(Collections.singletonList(outboundSMSResource.getMessageTo()));
        message.setBody(outboundSMSResource.getText());
        message.setSubject(outboundSMSResource.getSubject());
        message.setCommunicationType(String.valueOf(CommunicationType.SMS));
        message.setIs_outbound(true);
        message.setBookId(outboundSMSResource.getBookId());
        message.setUserId(outboundSMSResource.getUserId());
        message.setProcess(outboundSMSResource.getProcess());
        message.setScenario(outboundSMSResource.getScenario());
        message.setFunction(outboundSMSResource.getFunction());
        message.setProductSubCategory(outboundSMSResource.getProductSubCategory());
        message.setSupplier(outboundSMSResource.getSupplier());

        if (outboundSMSResource.getCommunicationTagResource() != null) {
            CommunicationTags communicationTags = new CommunicationTags();
            communicationTags.setBaseCommunication(message);
            CopyUtils.copy(outboundSMSResource.getCommunicationTagResource(), communicationTags);
            message.setCommunicationTags(communicationTags);
        }


        String subString = null;
//        try {
//            subString = sendSMS(outboundSMSResource.getMessageTo(), outboundSMSResource.getText());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        String msgTransactionId = subString.substring(subString.lastIndexOf("TID =")+5, subString.lastIndexOf("TID =") + 16);

        String msgTransactionId = sendSMS(outboundSMSResource.getMessageTo(), outboundSMSResource.getText());
        if (!msgTransactionId.isEmpty()) {
            message.setMessageUUID(Collections.singletonList(msgTransactionId));
            message.setStatus("success");
        }

        /*RestAPI api = new RestAPI(authId, authToken, "v1");
        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("src", outboundSMSResource.getMessageFrom()); // Sender's phone number with country code
        parameters.put("dst", outboundSMSResource.getMessageTo()); // Receiver's phone number with country code
        parameters.put("text", outboundSMSResource.getText()); // Your SMS text message
        try {
            // Send the message
            MessageResponse msgResponse = api.sendMessage(parameters);
            System.out.println(msgResponse);
            message.setStatus(msgResponse.message);
            if (msgResponse.messageUuids!=null){
                message.setMessageUUID(msgResponse.messageUuids);
            }

        } catch (PlivoException e) {
            System.out.println(e.getLocalizedMessage());
        }*/

        if (message.getStatus() == null || message.getMessageUUID() == null) {
            throw new OperationException("Cannot send msg to this number");
        }

        return sendMessageRepository.saveMessage(message);
    }

    private String sendSMS(String messageTo, String text) throws OperationException {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("feedid", feedid);
        map.add("senderid", senderid);
        map.add("username", username);
        map.add("password", password);
        map.add("To", messageTo);
        map.add("Text", text);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        /*HttpEntity httpEntity=new HttpEntity(httpHeaders);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sendURL);
        builder.queryParam("",7777);*/

        String responseObj = null;
        try {

            responseObj = restTemplate.getForEntity(String.format(sendURL, feedid, senderid, username, password, messageTo, text), String.class).getBody();
        } catch (Exception e) {
            throw new OperationException("Failed to send SMS");
        }
//        Element element= XMLTransformer.fromEscapedString(responseObj);
//        String abc = XMLUtils.getValueAtXPath(element,"/RESULT/MID/@TID");
        System.out.println(" SMS sent");
        String xmlStr = responseObj.substring(responseObj.lastIndexOf("<RESULT"), responseObj.lastIndexOf("RESULT>") + 7);
        Element element = XMLTransformer.toXMLElement(xmlStr);
        String abc = XMLUtils.getValueAtXPath(element, "/RESULT/MID/@TID");
        return abc;

    }

    @Override
    public OutboundSMS getSMSById(String id) throws OperationException {
        OutboundSMS sms = null;
        if (id != null) {
            sms = sendMessageRepository.getSMSById(id);
            if (sms == null) {
                throw new OperationException(Constants.ER01);
            }
        } else {
            throw new OperationException("Invalid input");
        }
        return sms;
    }

    @Override
    public OutboundSMS markAsRead(String id) throws OperationException {
        OutboundSMS sms = sendMessageRepository.getSMSById(id);
        sms.setRead(true);
        return this.sendMessageRepository.saveMessage(sms);
    }

    public InboundSMS receiveMessage(HttpServletRequest request) {
        InboundSMS inboundSMS = new InboundSMS();
        inboundSMS.setMessageFrom(request.getParameter("From"));
        inboundSMS.setMessageTo(request.getParameter("To"));
        inboundSMS.setText(request.getParameter("Text"));
        inboundSMS.setType(request.getParameter("Type"));
        inboundSMS.setMessageUUID(request.getParameter("MessageUUID"));
        return receiveMessageRepository.saveMessage(inboundSMS);
    }

    @Override
    public CommunicationTags getAssociatedTags(String id) throws OperationException {
        OutboundSMS sms = sendMessageRepository.getSMSById(id);
        return sms.getCommunicationTags();
    }

    @Override
    public OutboundSMS updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException {
        OutboundSMS outboundSMS = getSMSById(id);
        CommunicationTags communicationTags = new CommunicationTags();
        CopyUtils.copy(communicationTagResource, communicationTags);
        outboundSMS.setCommunicationTags(communicationTags);
        return sendMessageRepository.saveMessage(outboundSMS);
    }

    private static RestTemplate getTemplate() {
        RestTemplate template = new RestTemplate();


        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
//            clientBuilder.setProxy(new HttpHost("10.18.1.42", 8888) );
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        CloseableHttpClient client = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);

        template = new RestTemplate();
        template.setRequestFactory(factory);

        return template;
    }

}
