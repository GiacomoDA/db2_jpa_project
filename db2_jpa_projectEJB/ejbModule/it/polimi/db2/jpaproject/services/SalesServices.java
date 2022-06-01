package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.*;

import java.util.List;
import java.util.ArrayList;

@Stateless
public class SalesServices {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public SalesServices() {
	}
	
	public List<ValidityPeriodSales> findSpecificValidityPurchase(int packageId, int months) {
		return em.createNamedQuery("ValidityPeriodSales.findByPackage", ValidityPeriodSales.class).setParameter(1, packageId).getResultList();
	}

	public List<PackageSales> findAllPackagePurchase() {
		List<PackageSales> packageSales = em.createNamedQuery("PackageSales.findAll", PackageSales.class).getResultList();
		for (PackageSales ps : packageSales) {
			em.refresh(ps);
		}
		return packageSales;
	}
	
	public List<OptionalSales> findBestSeller() {
		List<OptionalSales> optionalSales = em.createNamedQuery("OptionalSales.findBestSeller", OptionalSales.class).getResultList();
		for (OptionalSales os : optionalSales) {
			em.refresh(os);
		}
		return optionalSales;
	}
}
