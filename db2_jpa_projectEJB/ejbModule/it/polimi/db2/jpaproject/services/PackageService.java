package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.Package;
import java.util.List;

@Stateless
public class PackageService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public PackageService() {
	}

	public List<Package> findAllPackages() {
		List<Package> packages =  em.createNamedQuery("Package.findAll", Package.class).getResultList();		
		return packages;
	}

}
