package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "insolvent_user", schema = "db2_jpa_project")
@NamedQuery(name = "InsolventUser.findAll", query = "SELECT i FROM InsolventUser i")


public class InsolventUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String user;
	
	private String email;
	
	public InsolventUser() {
	}

	public String getUser() {
		return user;
	}

	public String getEmail() {
		return email;
	}

}