package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.*;

import java.util.List;

@Stateless
public class SalesServices {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public SalesServices() {
	}

	public List<ValidityPeriodSales> findAllValidityPurchase() {
		return em.createNamedQuery("ValidityPeriodSales.findAll", ValidityPeriodSales.class).getResultList();
	}
	
	public List<ValidityPeriodSales> findSpecificValidityPurchase(int packageId, int months) {
		return em.createNamedQuery("ValidityPeriodSales.findSpecific", ValidityPeriodSales.class).setParameter(1, packageId).setParameter(2, months).getResultList();
	}

	public List<PackageSales> findAllPackagePurchase() {
		return em.createNamedQuery("PackageSales.findAll", PackageSales.class).getResultList();
	}
	
	public List<OptionalSales> findAllOptionalPurchases() {
		return em.createNamedQuery("OptionalSales.findAll", OptionalSales.class).getResultList();
	}
}
