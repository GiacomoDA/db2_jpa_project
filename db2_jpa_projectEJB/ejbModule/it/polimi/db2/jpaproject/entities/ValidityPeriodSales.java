package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@IdClass(VPSKey.class)
@Table(name = "validity_period_sales", schema = "db2_jpa_project")
public class ValidityPeriodSales implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private PackageSales packageSales;

	@Id
	private Integer months;

	@Column (name = "monthly_fee", precision = 10, scale = 2)
	private BigDecimal monthlyFee;
	
	private Integer sales;

	public ValidityPeriodSales() {
	}

	public PackageSales getPackageSales() {
		return packageSales;
	}

	public Integer getMonths() {
		return months;
	}

	public BigDecimal getMonthlyFee() {
		return monthlyFee;
	}

	public Integer getSales() {
		return sales;
	}

}