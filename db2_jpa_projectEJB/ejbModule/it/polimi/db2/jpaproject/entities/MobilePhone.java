package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "mobile_phone", schema = "db2_jpa_project")

public class MobilePhone implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	private Integer minutes;
	
	private Integer sms;
	
	@Column(name = "minutes_fee", precision = 10, scale = 2)
	private BigDecimal minutesFee;
	
	@Column(name = "sms_fee", precision = 10, scale = 2)
	private BigDecimal smsFee;
	
	public MobilePhone() {
	}

	public MobilePhone(Integer minutes, Integer sms, BigDecimal minutesFee,
			BigDecimal smsFee) {
		this.minutes = minutes;
		this.sms = sms;
		this.minutesFee = minutesFee;
		this.smsFee = smsFee;
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public Integer getSms() {
		return sms;
	}

	public BigDecimal getMinutesFee() {
		return minutesFee;
	}

	public BigDecimal getSmsFee() {
		return smsFee;
	}

	@Override
	public String toString() {
		return minutes + " Minutes\n" + sms + " SMS\n" + "Extra minutes fee: €" + minutesFee + "\n" + "Extra SMS fee: €" + smsFee;
	}

}


