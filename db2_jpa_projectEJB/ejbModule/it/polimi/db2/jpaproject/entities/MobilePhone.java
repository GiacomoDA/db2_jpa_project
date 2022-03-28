package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "mobile_phone", schema = "db2_jpa_project")

public class MobilePhone implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinColumn(name = "package_id")
	private Package servicePackage;
	
	private int minutes;
	
	private int sms;
	
	@Column(name = "minutes_fee")
	private int minutesFee;
	
	@Column(name = "sms_fee")
	private int smsFee;
	
	public MobilePhone() {
	}

	public MobilePhone(int minutes, int sms, int minutesFee, int smsFee) {
		this.minutes = minutes;
		this.sms = sms;
		this.minutesFee = minutesFee;
		this.smsFee = smsFee;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSms() {
		return sms;
	}

	public void setSms(int sms) {
		this.sms = sms;
	}

	public int getMinutesFee() {
		return minutesFee;
	}

	public void setMinutesFee(int minutesFee) {
		this.minutesFee = minutesFee;
	}

	public int getSmsFee() {
		return smsFee;
	}

	public void setSmsFee(int smsFee) {
		this.smsFee = smsFee;
	}	

}


