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

}
