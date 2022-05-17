package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "employee", schema = "db2_jpa_project")
@NamedQuery(name = "Employee.checkCredentials", query = "SELECT e FROM Employee e WHERE e.username = ?1 and e.password = ?2")

public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private String email;

	private String password;

	public Employee() {
	}
	
	public Employee(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}