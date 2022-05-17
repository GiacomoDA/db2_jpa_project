package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "validity_period_sales", schema = "db2_jpa_project")
@NamedQuery(name = "ValidityPeriodSales.findAll", query = "SELECT v FROM ValidityPeriodSales v")
@NamedQuery(name = "ValidityPeriodSales.findSpecific", query = "SELECT v FROM ValidityPeriodSales v WHERE v.packageId = ?1 AND v.months = ?2")
public class ValidityPeriodSales implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name = "package_id")
	private Integer packageId;

	private Integer months;

	@Column (name = "monthly_fee", precision = 10, scale = 2)
	private BigDecimal monthlyFee;

	public ValidityPeriodSales() {
	}

	public Integer getPackageId() {
		return packageId;
	}

	public Integer getMonths() {
		return months;
	}

	public BigDecimal getMonthlyFee() {
		return monthlyFee;
	}

}