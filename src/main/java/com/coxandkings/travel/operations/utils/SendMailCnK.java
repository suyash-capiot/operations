package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.FileAttachmentResource;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Properties;

@Component
public class SendMailCnK {



    @Value(value = "${email.smtp.host}")
    private String SMTP_HOST ;
    @Value(value = "${email.smtp.port}")
    private String SMTP_PORT ;
    @Value(value = "${email.smtp.username}")
    private String USERNAME  ;
    @Value(value = "${email.smtp.password}")
    private String PASSWORD ;
    @Value("${email.temp-folder}")
    private String UPLOADED_FOLDER ;
    @Value(value = "${email.run-outbound}")
    private Boolean FLAG ;

    private static Logger logger = LogManager.getLogger(SendMailCnK.class);

    public EmailResponse send(String from, String to, String cc, String bcc, String subject, String body, List<FileAttachmentResource> fileAttachmentResource) throws MessagingException {

        EmailResponse response = new EmailResponse();
        int flag = 1 ;

        String status = "" , msg = "" ;

        MimeMessage message = null;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        //props.put("mail.smtp.ssl.trust", SMTP_HOST );

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            message = new MimeMessage(session) {
                protected void updateMessageID() throws MessagingException {
                    if (getHeader("Message-ID") == null)
                        super.updateMessageID();
                }
            };
            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            if(cc != null)
                message.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc));
            if(bcc != null)
                message.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc));
            message.setSubject(subject);
           // message.setText(body);

            Multipart multipart = new MimeMultipart();

            if(null != body){
                BodyPart msgBodyPart = new MimeBodyPart();
                msgBodyPart.setText(body);
                msgBodyPart.setContent(body, "text/html");
                multipart.addBodyPart(msgBodyPart);
            }

            for(FileAttachmentResource file: fileAttachmentResource) {
                String fileName = UPLOADED_FOLDER + File.separator + file.getFilename();

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getFilename());
                multipart.addBodyPart(messageBodyPart);
                // Send the complete message parts
                message.setContent(multipart);
            }

            if (FLAG) {
                Transport.send(message);
            }
            logger.info("Mail sent to the specified recipients List .");
        }
            catch (MessagingException e) {
            flag = 0;
            e.printStackTrace();
            status = e.getStackTrace().toString();
                //throw new OperationException(Constants.ER37);
            //uncomment this when mail access has been provided
            //return e.getStackTrace().toString();
        }

        msg  = (flag == 0 )?  status : "Mail has been sent successfully  to "+ to  ;
        status = (flag == 0 )?  "ERROR" : "SUCCESS"  ;

        response.setStatus(status);
        response.setMesssage(msg);
        response.setMessageID(message.getMessageID());
         return response ;
    }

    public EmailResponse send(String from, String to, String cc, String bcc, String subject, String body) throws MessagingException {

        EmailResponse response = new EmailResponse();
        int flag = 1 ;

        String status = "" , msg = "";
        MimeMessage message = null;

        //configureSMTP();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            message = new MimeMessage(session){
                protected void updateMessageID() throws MessagingException {
                    if (getHeader("Message-ID") == null)
                        super.updateMessageID();
                }
            };
            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            if(cc != null)
                message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(cc));
            if(bcc != null)
                message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(bcc));
            message.setSubject(subject);
            message.setText(body);

            Multipart multipart = new MimeMultipart();

            BodyPart msgBodyPart = new MimeBodyPart();
            msgBodyPart.setText(body);
            msgBodyPart.setContent(body, "text/html");
            multipart.addBodyPart(msgBodyPart);
            message.setContent(multipart);

            Transport.send(message);
            logger.info("Mail sent to the specified recipients List .");
        }
        catch (MessagingException e) {
            flag = 0;
            e.printStackTrace();
            status = e.getStackTrace().toString();
            //throw new OperationException(Constants.ER37);
            //uncomment this when mail access has been provided
            //return e.getStackTrace().toString();
        }

        msg  = (flag == 0 )?  status : "Mail has been sent successfully  to "+ to  ;
        status = (flag == 0 )?  "ERROR" : "SUCCESS"  ;

        response.setStatus(status);
        response.setMesssage(msg);
        response.setMessageID(message.getMessageID());
        return response ;


    }

}

