package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "optional", schema = "db2_jpa_project")
@NamedQuery(name = "Optional.findOptionals", query = "SELECT o FROM Optional o WHERE o.name IN ?1")

public class Optional implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	@Column(name = "monthly_fee")
	private int monthlyFee;
	
	public Optional() {
	}	

	public Optional(String name, int monthlyFee) {
		this.name = name;
		this.monthlyFee = monthlyFee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMonthlyFee() {
		return monthlyFee;
	}

	public void setMonthlyFee(int monthlyFee) {
		this.monthlyFee = monthlyFee;
	}

}
