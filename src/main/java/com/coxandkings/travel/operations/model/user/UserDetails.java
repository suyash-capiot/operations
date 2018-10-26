package com.coxandkings.travel.operations.model.user;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/*@Entity
@Table(name = "USERDETAILS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})*/
public class UserDetails implements Serializable {

    @Id
    @Column
    private Integer opsUserId;

    @Column
    private String name;

    @Column
    private String role;

    public Integer getOpsUserId() {
        return opsUserId;
    }

    public void setOpsUserId(Integer opsUserId) {
        this.opsUserId = opsUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

