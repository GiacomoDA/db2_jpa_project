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
		return em.createNamedQuery("PackageSales.findAll", PackageSales.class).getResultList();
	}
	
	public List<OptionalSales> findBestSeller() {
		List<OptionalSales> optionals = em.createNamedQuery("OptionalSales.findAll", OptionalSales.class).getResultList();
		//Integer max = optionals.stream().map(optional -> optional.getSales()).max(Comparator.comparing(Integer::valueOf)).get();
		int max = 0;

		for(OptionalSales optional : optionals) {
			if(optional.getSales() > max) {
				max = optional.getSales();
			}
		}
		
		if(max == 0) {
			return null;
		}
		else {
			List<OptionalSales> bestSeller = new ArrayList<OptionalSales>();
			for(OptionalSales optional : optionals) {
				if(optional.getSales() == max) {
					bestSeller.add(optional);
				}
			}
			return bestSeller;
		}
		
	}
	
}
