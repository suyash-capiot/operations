package com.coxandkings.travel.operations.model.email;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "email")
public class Email extends BaseCommunication {

    @Column(name = "messageId")
    private String messageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private EmailPriority priority;

    @Column(name = "fileAttachments")
    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileAttachment> fileAttachments;

    @Column(name = "referencedEmail")
    private String refMailId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public EmailPriority getPriority() {
        return priority;
    }

    public void setPriority(EmailPriority priority) {
        this.priority = priority;
    }

    public List<FileAttachment> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<FileAttachment> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    public String getRefMailId() {
        return refMailId;
    }

    public void setRefMailId(String refMailId) {
        this.refMailId = refMailId;
    }
}
