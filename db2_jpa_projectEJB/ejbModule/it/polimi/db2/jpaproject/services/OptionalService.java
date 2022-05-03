package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.*;

import java.util.List;

@Stateless
public class OptionalService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public OptionalService() {
	}
	
	public List<Optional> findOptionalsList(List<String> optionals) {
		return em.createNamedQuery("Optional.findOptionals", Optional.class).setParameter(1, optionals).getResultList();
	}

}
