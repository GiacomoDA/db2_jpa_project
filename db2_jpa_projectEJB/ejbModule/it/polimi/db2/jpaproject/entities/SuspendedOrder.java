package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "suspended_order", schema = "db2_jpa_project")
@NamedQueries({
	@NamedQuery(name = "SuspendedOrder.findAll", query = "SELECT s FROM SuspendedOrder s"),
	@NamedQuery(name = "SuspendedOrder.findRejectedByUser", query = "SELECT s FROM SuspendedOrder s WHERE s.user = ?1")})

public class SuspendedOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String user;

	@Column(precision = 10, scale = 2)
	private BigDecimal total;

	public SuspendedOrder() {
	}

	public Integer getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public BigDecimal getTotal() {
		return total;
	}

}