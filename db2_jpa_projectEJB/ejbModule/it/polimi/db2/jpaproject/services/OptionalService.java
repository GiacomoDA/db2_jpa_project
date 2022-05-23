package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.jpaproject.entities.*;
import it.polimi.db2.jpaproject.exceptions.CredentialsException;
import java.math.BigDecimal;

import java.util.List;

@Stateless
public class OptionalService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public OptionalService() {
	}
	
	public List<OptionalProduct> findAll() {
		return em.createNamedQuery("Optional.findAll", OptionalProduct.class).getResultList();
	}

	public List<OptionalProduct> findOptionalsList(List<String> optionals) {
		return em.createNamedQuery("Optional.findOptionals", OptionalProduct.class).setParameter(1, optionals).getResultList();
	}

	public void addOptional(String name, BigDecimal fee) throws CredentialsException {

		OptionalProduct optional = new OptionalProduct(name, fee);
		em.persist(optional);
		em.flush();

	}
	
	public List<OptionalProduct> checkOptionals(String name){
		return em.createNamedQuery("Optional.findOptionalsByName", OptionalProduct.class).setParameter(1, name).getResultList();
	}
}
