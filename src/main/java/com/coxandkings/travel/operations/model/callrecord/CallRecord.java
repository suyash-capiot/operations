package com.coxandkings.travel.operations.model.callrecord;

import com.coxandkings.travel.operations.model.communication.BaseCommunication;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "CallRecord")
//@Audited
public class CallRecord extends BaseCommunication {

    private String duration;
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> mediaId;
    private String phoneNumber;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getMediaId() {
        return mediaId;
    }

    public void setMediaId(List<String> mediaId) {
        this.mediaId = mediaId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
