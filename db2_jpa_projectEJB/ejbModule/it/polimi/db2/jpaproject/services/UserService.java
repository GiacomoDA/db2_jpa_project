package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import it.polimi.db2.jpaproject.entities.User;
import it.polimi.db2.jpaproject.exceptions.CredentialsException;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String username, String password) throws CredentialsException, NonUniqueResultException {
		List<User> users = null;
		
		try {
			users = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials");
		}
		
		if (users.isEmpty())
			return null;
		else if (users.size() == 1)
			return users.get(0);
		throw new NonUniqueResultException("More than one user registered with the same credentials");
	}
	
	public void addUser(String username, String email, String password) throws CredentialsException {
		
		System.out.println(username + email + password);
		if (em.find(User.class, username) != null) {
			System.out.println("@");
			throw new CredentialsException("The username is taken");
		}
		
		System.out.println("#");
		User user = new User(username, email, password);
		em.persist(user);
		em.flush();
	}

}
