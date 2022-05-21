package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.ServicePackage;
import java.util.List;

@Stateless
public class PackageService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public PackageService() {
	}

	public List<ServicePackage> findAllPackages() {
		return em.createNamedQuery("ServicePackage.findAll", ServicePackage.class).getResultList();
	}
	
	public ServicePackage findPackageById(int packageId) {
		return em.createNamedQuery("ServicePackage.findById", ServicePackage.class).setParameter(1, packageId).getSingleResult();
	}

	public ServicePackage findPackageByName(String name) {
		return em.createNamedQuery("ServicePackage.findByName", ServicePackage.class).setParameter(1, name).getSingleResult();
	}
}
