package com.coxandkings.travel.operations.model.forex;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "forex_request_lock")
public class RequestLockObject {

	@Id
	@GeneratedValue
	private UUID id;

	@Column
	private String userId;

	@Column
	private boolean enabled;
	
	@Column
	private String workflowId;
	
	@OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonBackReference
	private ForexBooking forexBooking;

	@Transient
	private String user;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		setUser(userId);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public ForexBooking getForexBooking() {
		return forexBooking;
	}

	public void setForexBooking(ForexBooking forexBooking) {
		this.forexBooking = forexBooking;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
}
