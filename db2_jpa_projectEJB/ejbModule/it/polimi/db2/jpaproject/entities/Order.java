package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "order", schema = "db2_jpa_project")
@NamedQueries({
	@NamedQuery(name = "Order.findRejectedByUser", query = "SELECT o FROM Order o WHERE o.accepted = false AND o.user = ?1"),
	@NamedQuery(name = "Order.findById", query = "SELECT o FROM Order o WHERE o.id = ?1")})

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "creation_time", columnDefinition = "timestamp")
	private LocalDateTime creationTime;

	@Column(name = "activation_date", columnDefinition = "date")
	private LocalDate activationDate;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal total;

	private Boolean accepted;
	
	private Integer months;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "order_to_optional", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "optional"))
	private List<Optional> optionals;

	public Order() {
	}

	public Order(LocalDate activationDate, BigDecimal total, Integer months, ServicePackage servicePackage, List<Optional> optionals) {
		super();
		this.activationDate = activationDate;
		this.total = total;
		this.months = months;
		this.servicePackage = servicePackage;
		this.optionals = optionals;
		this.creationTime = LocalDateTime.now();
		this.accepted = false;
	}

	public Integer getId() {
		return id;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public LocalDate getActivationDate() {
		return activationDate;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public Boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public Integer getMonths() {
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

	public List<Optional> getOptionals() {
		return optionals;
	}
	
}
