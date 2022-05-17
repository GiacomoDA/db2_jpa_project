package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "optional_sales", schema = "db2_jpa_project")
@NamedQuery(name = "OptionalSales.findAll", query = "SELECT o FROM OptionalSales O")

public class OptionalSales implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	private Integer sales;

	public OptionalSales() {
	}

	public String getName() {
		return name;
	}

	public Integer getSales() {
		return sales;
	}

}