package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@IdClass(VPKey.class)
@Table(name = "validity_period", schema = "db2_jpa_project")
@NamedQuery(name = "ValidityPeriod.findById", query = "SELECT p FROM ValidityPeriod p WHERE p.servicePackage = ?1 and p.months = ?2")

public class ValidityPeriod implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	@Id
	private Integer months;
	
	@Column(name = "monthly_fee", precision = 10, scale = 2)
	private BigDecimal monthlyFee;
	
	public ValidityPeriod() {
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public int getMonths() {
		return months;
	}

	public BigDecimal getMonthlyFee() {
		return monthlyFee;
	}
	
}


