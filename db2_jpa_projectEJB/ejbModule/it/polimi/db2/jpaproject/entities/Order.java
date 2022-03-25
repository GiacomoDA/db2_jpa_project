package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order", schema = "db2_jpa_project")

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "creation_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Column(name = "activation_date")
	@Temporal(TemporalType.DATE)
	private Date activationDate;
	
	private int total;

	private boolean accepted;
	
	private int months;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private User user;

	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private Package package_id;*/
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinTable(name = "order_to_optional", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "optional"))
	private List<Optional> optionals;

	public Order() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}	

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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

	public void setMonths(int months) {
		this.months = months;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/*public Package getPackage_id() {
		return package_id;
	}

	public void setPackage_id(Package package_id) {
		this.package_id = package_id;
	}*/

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
