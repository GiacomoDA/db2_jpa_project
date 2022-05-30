package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "mobile_internet", schema = "db2_jpa_project")

public class MobileInternet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	private Integer gigabytes;
	
	@Column(name = "gigabytes_fee", precision = 10, scale = 2)
	private BigDecimal gigabytesFee;
	
	public MobileInternet() {
	}
	
	public MobileInternet(Integer gigabytes, BigDecimal gigabytesFee) {
		this.gigabytes = gigabytes;
		this.gigabytesFee = gigabytesFee;
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
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


