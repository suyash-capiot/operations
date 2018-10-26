package com.coxandkings.travel.operations.model.mailroomanddispatch;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="master_room_contact")
@JsonIgnoreProperties
public class MasterRoomContact {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "master_room_id")
    @JsonIgnore
    private MailRoomMaster mailRoomMaster;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public MailRoomMaster getMailRoomMaster() {
        return mailRoomMaster;
    }

    public void setMailRoomMaster(MailRoomMaster mailRoomMaster) {
        this.mailRoomMaster = mailRoomMaster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterRoomContact that = (MasterRoomContact) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(mailRoomMaster, that.mailRoomMaster);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, phone, mailRoomMaster);
    }

    @Override
    public String toString() {
        return "MasterRoomContact{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", mailRoomMaster=" + mailRoomMaster +
                '}';
    }
}