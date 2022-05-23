package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.*;
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
		return em.createNamedQuery("ServicePackage.findById", ServicePackage.class).setParameter(1, packageId)
				.getSingleResult();
	}

	public ServicePackage findPackageByName(String name) {
		return em.createNamedQuery("ServicePackage.findByName", ServicePackage.class).setParameter(1, name)
				.getSingleResult();
	}

	public ServicePackage addPackage(String name, Boolean fixedPhone, MobilePhone mobilePhone,
			FixedInternet fixedInternet, MobileInternet mobileInternet, List<ValidityPeriod> validityPeriods,
			List<String> opt) {
		List<OptionalProduct> optionals = em.createNamedQuery("Optional.findOptionals", OptionalProduct.class).setParameter(1, opt).getResultList();
		ServicePackage servicePackage = new ServicePackage(name, fixedPhone, mobilePhone, fixedInternet, mobileInternet, validityPeriods, optionals);
		if (mobilePhone != null) {
			mobilePhone.setServicePackage(servicePackage);
		}
		if (fixedInternet != null) {
			fixedInternet.setServicePackage(servicePackage);
		}
		if (mobileInternet != null) {
			mobileInternet.setServicePackage(servicePackage);
		}
		em.persist(servicePackage);
		return null;
	}
}
