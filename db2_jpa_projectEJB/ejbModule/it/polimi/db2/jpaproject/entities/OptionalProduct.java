package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "optional", schema = "db2_jpa_project")
@NamedQueries({
	@NamedQuery(name = "Optional.findAll", query = "SELECT o FROM OptionalProduct o"),
	@NamedQuery(name = "Optional.findOptionals", query = "SELECT o FROM OptionalProduct o WHERE o.name IN ?1"),
	@NamedQuery(name = "Optional.findOptionalsByName", query = "SELECT o FROM OptionalProduct o WHERE o.name = ?1")})


public class OptionalProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	@Column(name = "monthly_fee", precision = 10, scale = 2)
	private BigDecimal monthlyFee;
	
	public OptionalProduct() {
	}	

	public OptionalProduct(String name, BigDecimal monthlyFee) {
		this.name = name;
		this.monthlyFee = monthlyFee;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getMonthlyFee() {
		return monthlyFee;
	}

}
