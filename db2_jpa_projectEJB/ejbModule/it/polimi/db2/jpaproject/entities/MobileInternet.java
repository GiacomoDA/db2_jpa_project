package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "mobile_internet", schema = "db2_jpa_project")

public class MobileInternet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	private int gigabytes;
	
	@Column(name = "gigabytes_fee")
	private int gigabytesFee;
	
	public MobileInternet() {
	}

	public MobileInternet(int gigabytes, int gigabytesFee) {
		this.gigabytes = gigabytes;
		this.gigabytesFee = gigabytesFee;
	}

	public int getGigabytes() {
		return gigabytes;
	}

	public void setGigabytes(int gigabytes) {
		this.gigabytes = gigabytes;
	}

	public int getGigabytesFee() {
		return gigabytesFee;
	}

	public void setGigabytesFee(int gigabytesFee) {
		this.gigabytesFee = gigabytesFee;
	}

}


