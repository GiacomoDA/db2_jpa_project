package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@IdClass(VPKey.class)
@Table(name = "validity_period", schema = "db2_jpa_project")

public class ValidityPeriod implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinColumn(name = "package_id")
	private Package servicePackage;
	
	@Id
	private int months;
	
	@Column(name = "monthly_fee")
	private int monthlyFee;
	
	public ValidityPeriod() {
	}

	/*public int getPackageID() {
		return packageId;
	}

	public void setPackageID(int packageId) {
		this.packageId = packageId;
	}*/

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getMonthlyFee() {
		return monthlyFee;
	}

	public void setMonthlyFee(int monthlyFee) {
		this.monthlyFee = monthlyFee;
	}

}


