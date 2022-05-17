package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "package_sales", schema = "db2_jpa_project")
@NamedQuery(name = "PackageSales.findAll", query = "SELECT p FROM PackageSales P")

public class PackageSales implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private Integer sales;
	
	@Column (name = "sales_with_optionals")
	private Integer salesWithOptionals;
	
	@Column (name = "optionals_sales")
	private Integer optionalSales;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getSales() {
		return sales;
	}

	public Integer getSalesWithOptionals() {
		return salesWithOptionals;
	}

	public Integer getOptionalSales() {
		return optionalSales;
	}
	
	public Float AverageOptionalsPerPackage() {
		return (float) optionalSales / sales;
	}
	
}
