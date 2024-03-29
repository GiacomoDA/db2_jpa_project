package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "package", schema = "db2_jpa_project")
@NamedQueries({
		@NamedQuery(name = "ServicePackage.findAll", query = "SELECT p FROM ServicePackage p"),
		@NamedQuery(name = "ServicePackage.findByName", query = "SELECT p FROM ServicePackage p WHERE p.name = ?1"),
		@NamedQuery(name = "ServicePackage.findById", query = "SELECT p FROM ServicePackage p WHERE p.id = ?1")})

public class ServicePackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	
	@Column(name = "fixed_phone")
	private Boolean fixedPhone;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "servicePackage", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	private List<MobilePhone> mobilePhone;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "servicePackage", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	private List<FixedInternet> fixedInternet;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "servicePackage", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	private List<MobileInternet> mobileInternet;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "servicePackage", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@OrderBy("months ASC")
	private List<ValidityPeriod> validityPeriods;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "package_to_optional", joinColumns = @JoinColumn(name = "package_id"), inverseJoinColumns = @JoinColumn(name = "optional"))
	private List<OptionalProduct> optionals;
	
	public ServicePackage() {
	}

	public ServicePackage(String name, Boolean fixedPhone, List<MobilePhone> mobilePhone, List<FixedInternet> fixedInternet,
			List<MobileInternet> mobiletInternet, List<ValidityPeriod> validityPeriods, List<OptionalProduct> optionals) {
		this.name = name;
		this.fixedPhone = fixedPhone;
		this.mobilePhone = mobilePhone;
		this.fixedInternet = fixedInternet;
		this.mobileInternet = mobiletInternet;
		this.validityPeriods = validityPeriods;
		this.optionals = optionals;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<MobilePhone> getMobilePhone() {
		return mobilePhone;
	}

	public List<FixedInternet> getFixedInternet() {
		return fixedInternet;
	}

	public List<MobileInternet> getMobileInternet() {
		return mobileInternet;
	}

	public List<ValidityPeriod> getValidityPeriods() {
		return validityPeriods;
	}

	public List<OptionalProduct> getOptionals() {
		return optionals;
	}
	
	public Boolean hasFixedPhone() {
		return fixedPhone;
	}
	
}

