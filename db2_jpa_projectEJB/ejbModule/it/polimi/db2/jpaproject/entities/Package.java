package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "package", schema = "db2_jpa_project")
@NamedQuery(name = "Package.findAll", query = "SELECT p FROM Package p")

public class Package implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	@Column(name = "fixed_phone")
	private Boolean fixedPhone;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "servicePackage")
	private MobilePhone mobilePhone;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "servicePackage")
	private FixedInternet fixedInternet;
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "servicePackage")
	private MobileInternet mobileInternet;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "servicePackage", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@OrderBy("months ASC")
	private List<ValidityPeriod> validityPeriods;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinTable(name = "package_to_optional", joinColumns = @JoinColumn(name = "package_id"), inverseJoinColumns = @JoinColumn(name = "optional"))
	private List<Optional> optionals;
	
	public Package() {
	}

	public Package(String name, Boolean fixedPhone, MobilePhone mobilePhone, FixedInternet fixedInternet,
			MobileInternet mobiletInternet, List<ValidityPeriod> validityPeriods, List<Optional> optionals) {
		this.name = name;
		this.fixedPhone = fixedPhone;
		this.mobilePhone = mobilePhone;
		this.fixedInternet = fixedInternet;
		this.mobileInternet = mobiletInternet;
		this.validityPeriods = validityPeriods;
		this.optionals = optionals;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MobilePhone getMobilePhone() {
		return mobilePhone;
	}

	public FixedInternet getFixedInternet() {
		return fixedInternet;
	}

	public MobileInternet getMobileInternet() {
		return mobileInternet;
	}

	public List<ValidityPeriod> getValidityPeriods() {
		return validityPeriods;
	}

	public List<Optional> getOptionals() {
		return optionals;
	}
	
	public Boolean hasFixedPhone() {
		return fixedPhone;
	}
	
}

