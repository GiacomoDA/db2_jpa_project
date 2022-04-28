package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "order", schema = "db2_jpa_project")

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "creation_time", columnDefinition = "timestamp")
	private LocalDateTime creationTime;

	@Column(name = "activation_date", columnDefinition = "date")
	private LocalDate activationDate;
	
	private int total;

	private boolean accepted;
	
	private int months;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinTable(name = "order_to_optional", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "optional"))
	private List<Optional> optionals;

	public Order() {
	}

	public Order(LocalDate activationDate, int total, int months, ServicePackage servicePackage, List<Optional> optionals) {
		super();
		this.activationDate = activationDate;
		this.total = total;
		this.months = months;
		this.servicePackage = servicePackage;
		this.optionals = optionals;
		this.creationTime = LocalDateTime.now();
		this.accepted = false;
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public LocalDate getActivationDate() {
		return activationDate;
	}

	public int getTotal() {
		return total;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public int getMonths() {
		return months;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ServicePackage getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
	}

	public List<Optional> getOptionals() {
		return optionals;
	}
	
	public void setOptionals(List<Optional> optionals) {
		this.optionals = optionals;
	}

	public void addOptional(Optional optional) {
		optionals.add(optional);
	}

}
