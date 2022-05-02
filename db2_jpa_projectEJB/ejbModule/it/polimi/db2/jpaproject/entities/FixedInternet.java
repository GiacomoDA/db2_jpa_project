package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "fixed_internet", schema = "db2_jpa_project")

public class FixedInternet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	private Integer gigabytes;
	
	@Column(name = "gigabytes_fee", precision = 10, scale = 2)
	private BigDecimal gigabytesFee;
	
	public FixedInternet() {
	}

	public FixedInternet(ServicePackage servicePackage, Integer gigabytes, BigDecimal gigabytesFee) {
		this.servicePackage = servicePackage;
		this.gigabytes = gigabytes;
		this.gigabytesFee = gigabytesFee;
	}

	public Integer getGigabytes() {
		return gigabytes;
	}

	public BigDecimal getGigabytesFee() {
		return gigabytesFee;
	}
	
	public String toString() {
		return gigabytes + " GB\n" + "Extra GB fee: €" + gigabytesFee;
	}

}


