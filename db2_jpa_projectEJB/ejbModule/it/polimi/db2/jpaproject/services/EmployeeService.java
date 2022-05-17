package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import it.polimi.db2.jpaproject.entities.Employee;
import it.polimi.db2.jpaproject.exceptions.CredentialsException;
import java.util.List;

@Stateless
public class EmployeeService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public EmployeeService() {
	}

	public Employee checkCredentials(String username, String password) throws CredentialsException, NonUniqueResultException {
		List<Employee> employees = null;
		
		try {
			employees = em.createNamedQuery("Employee.checkCredentials", Employee.class).setParameter(1, username).setParameter(2, password).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials");
		}
		
		//TODO
		if (employees.isEmpty())
			return null;
		else if (employees.size() == 1)
			return employees.get(0);
		throw new NonUniqueResultException("More than one employee registered with the same credentials");
	}
}
