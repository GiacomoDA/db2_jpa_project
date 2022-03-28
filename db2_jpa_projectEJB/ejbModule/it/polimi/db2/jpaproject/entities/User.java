package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "user", schema = "db2_jpa_project")
@NamedQuery(name = "User.checkCredentials", query = "SELECT u FROM User u  WHERE u.username = ?1 and u.password = ?2")

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private String email;

	private String password;

	@Column(name = "failed_payments")
	private int failedPayments;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
	@OrderBy("creationTime DESC")
	private List<Order> orders;

	public User() {
	}
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.failedPayments = 0;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFailedPayments() {
		return failedPayments;
	}

	public void setFailedPayments(int failedPayments) {
		this.failedPayments = failedPayments;
	}

	public List<Order> getOrders() {
		return this.orders;
	}
	
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addOrder(Order order) {
		orders.add(order);
		order.setUser(this);
	}

}