package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "optional", schema = "db2_jpa_project")
@NamedQuery(name = "Optional.findOptionals", query = "SELECT o FROM Optional o WHERE o.name IN ?1")

public class Optional implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	@Column(name = "monthly_fee", precision = 10, scale = 2)
	private BigDecimal monthlyFee;
	
	public Optional() {
	}	

	public Optional(String name, BigDecimal monthlyFee) {
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
