package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrderService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public OrderService() {
	}

	public Order createOrder(int packageId, LocalDate activationDate, int months, List<String> opt) {
		int total = 0;
		List<Optional> optionals = new ArrayList<>();
		
		ServicePackage servicePackage = em.createNamedQuery("ServicePackage.findById", ServicePackage.class)
				.setParameter(1, packageId).getSingleResult();

		ValidityPeriod validityPeriod = em.createNamedQuery("ValidityPeriod.findById", ValidityPeriod.class)
				.setParameter(1, packageId).setParameter(2, months).getSingleResult();
		
		total += validityPeriod.getMonths() * validityPeriod.getMonthlyFee();

		if (!opt.isEmpty()) {
			optionals = em.createNamedQuery("Optional.findOptionals", Optional.class).setParameter(1, opt)
					.getResultList();
			total += optionals.stream().mapToInt(o -> o.getMonthlyFee()).sum();
		}

		Order order = new Order(activationDate, total, months, servicePackage, optionals);

		return new Order();
	}

}
