package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "package", schema = "db2_jpa_project")

public class Package implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "servicePackage", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@OrderBy("months ASC")
	private List<ValidityPeriod> validityPeriods;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinTable(name = "package_to_optional", joinColumns = @JoinColumn(name = "package_id"), inverseJoinColumns = @JoinColumn(name = "optional"))
	private List<Optional> optionals;
	
	public Package() {
	}

	public Package(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ValidityPeriod> getValidityPeriods() {
		return validityPeriods;
	}

	public void setValidityPeriods(List<ValidityPeriod> validityPeriods) {
		this.validityPeriods = validityPeriods;
	}

	public List<Optional> getOptionals() {
		return optionals;
	}

	public void setOptionals(List<Optional> optionals) {
		this.optionals = optionals;
	}

	public void addValidityPeriod(ValidityPeriod validityPeriod) {
		validityPeriods.add(validityPeriod);
	}

	public void addOptional(Optional optional) {
		optionals.add(optional);
	}
	
}

