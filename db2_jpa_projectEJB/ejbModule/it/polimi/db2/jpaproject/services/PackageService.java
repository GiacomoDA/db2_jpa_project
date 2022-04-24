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
		return em.createNamedQuery("Package.findAll", Package.class).getResultList();
	}
	
	public Package findPackageById(int packageId) {
		return em.createNamedQuery("Package.findById", Package.class).setParameter(1, packageId).getSingleResult();
	}

}
