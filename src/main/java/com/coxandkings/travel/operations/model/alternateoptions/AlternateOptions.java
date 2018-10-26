package com.coxandkings.travel.operations.model.alternateoptions;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedBy;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsStatus;
import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.resource.managedocumentation.documentmaster.Lock;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="ALTERNATEOPTIONS" , uniqueConstraints = @UniqueConstraint(columnNames = {"companyMarket","clientType"}))
public class AlternateOptions {

	public AlternateOptions(String configurationId,String companyMarket, String clientType) {
		this.configurationId = configurationId;
		this.companyMarket = companyMarket;
		this.clientType = clientType;
	}
	
	public AlternateOptions() {}
	
	@Id
	@Column(name="configurationId")
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy = "uuid")
	private String configurationId;
	
	@Column
	private String companyMarket;
	
	@Column
	private String clientType;
	
	@OneToMany(mappedBy="alternateOptions", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<AlternateOptionsProductDetails> alternateOptionsProductDetails ;
	
	@OneToMany(mappedBy = "alternateOptions", cascade = CascadeType.ALL, fetch= FetchType.LAZY)
	private List<AlternateOptionsResponseDetails> alternateOptionsResponseDetails;
	
	@Column
	private String alternateOfferProcess; // Auto , manual
	
	@Column
	private String higherLimitThreshold;
	
	@Column
	private String lowerLimitThreshold;
	
	@Enumerated(EnumType.STRING)
    private AlternateOptionsStatus status;
	
	@Column
    private String action;
	
	@Column
    private String lastModifiedByUserId;

	@Column
    private String approverRemark;

    @OneToOne(mappedBy = "alternateOptions", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private RequestLockObject lock;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@Column(name = "createdOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private ZonedDateTime createdOn;

	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "lastModifiedOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastModifiedOn;
	
	public String getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	public List<AlternateOptionsProductDetails> getAlternateOptionsProductDetails() {
		return alternateOptionsProductDetails;
	}

	public void setAlternateOptionsProductDetails(List<AlternateOptionsProductDetails> alternateOptionsProductDetails) {
		this.alternateOptionsProductDetails = alternateOptionsProductDetails;
	}

	public String getCompanyMarket() {
		return companyMarket;
	}

	public void setCompanyMarket(String companyMarket) {
		this.companyMarket = companyMarket;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}


	
	public String getHigherLimitThreshold() {
		return higherLimitThreshold;
	}

	public void setHigherLimitThreshold(String higherLimitThreshold) {
		this.higherLimitThreshold = higherLimitThreshold;
	}

	public String getLowerLimitThreshold() {
		return lowerLimitThreshold;
	}

	public void setLowerLimitThreshold(String lowerLimitThreshold) {
		this.lowerLimitThreshold = lowerLimitThreshold;
	}

	public String getAlternateOfferProcess() {
		return alternateOfferProcess;
	}

	public void setAlternateOfferProcess(String alternateOfferProcess) {
		this.alternateOfferProcess = alternateOfferProcess;
	}

	public List<AlternateOptionsResponseDetails> getAlternateOptionsResponseDetails() {
		return alternateOptionsResponseDetails;
	}

	public void setAlternateOptionsResponseDetails(List<AlternateOptionsResponseDetails> alternateOptionsResponseDetails) {
		this.alternateOptionsResponseDetails = alternateOptionsResponseDetails;
	}

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getApproverRemark() {
    return approverRemark;
  }

  public void setApproverRemark(String approverRemark) {
    this.approverRemark = approverRemark;
  }

  public RequestLockObject getLock() {
    return lock;
  }

  public void setLock(RequestLockObject lock) {
    this.lock = lock;
  }

  public ZonedDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(ZonedDateTime createdOn) {
    this.createdOn = createdOn;
  }

  public ZonedDateTime getLastModifiedOn() {
    return lastModifiedOn;
  }

  public void setLastModifiedOn(ZonedDateTime lastModifiedOn) {
    this.lastModifiedOn = lastModifiedOn;
  }

  public AlternateOptionsStatus getStatus() {
    return status;
  }

  public void setStatus(AlternateOptionsStatus status) {
    this.status = status;
  }

  public String getLastModifiedByUserId() {
    return lastModifiedByUserId;
  }

  public void setLastModifiedByUserId(String lastModifiedByUserId) {
    this.lastModifiedByUserId = lastModifiedByUserId;
  }
	
  
	
	
	
//	@Override
//	public String toString() {
//		return this.alternateOfferProcess+" "+this.clientType+" "+this.companyMarket+" "+this.configurationId+" "+this.higherLimitThreshold+" "+this.lowerLimitThreshold+" "+this.alternateOptionsProductDetails;
//	}
//	
//	@Override
//	public int hashCode() {
//		return Integer.parseInt(this.configurationId);
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if(((AlternateOptions)obj).getConfigurationId().equals(this.configurationId)) {
//			return true;
//		}
//		return false;
//	}
	
}
