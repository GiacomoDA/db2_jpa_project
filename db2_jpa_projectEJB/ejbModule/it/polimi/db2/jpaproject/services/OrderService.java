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
	
	public List<Order> FindRejectedOrders(User user) {
		return em.createNamedQuery("Order.findRejectedByUser", Order.class).setParameter(1, user).getResultList();
	}
	
	public Order FindOrderById(int id) {
		return em.createNamedQuery("Order.findById", Order.class).setParameter(1, id).getSingleResult();
	}

	public Order createOrder(int packageId, LocalDate activationDate, int months, List<String> opt) {
		int total = 0;
		List<Optional> optionals = new ArrayList<>();
		
		ServicePackage servicePackage = em.createNamedQuery("ServicePackage.findById", ServicePackage.class)
				.setParameter(1, packageId).getSingleResult();

		ValidityPeriod validityPeriod = em.createNamedQuery("ValidityPeriod.findById", ValidityPeriod.class)
				.setParameter(1, servicePackage).setParameter(2, months).getSingleResult();
		
		total += validityPeriod.getMonths() * validityPeriod.getMonthlyFee();

		if (!opt.isEmpty()) {
			optionals = em.createNamedQuery("Optional.findOptionals", Optional.class).setParameter(1, opt)
					.getResultList();
			total += optionals.stream().mapToInt(o -> o.getMonthlyFee()).sum() * months;
		}

		Order order = new Order(activationDate, total, months, servicePackage, optionals);

		return order;
	}

	public Boolean confirmOrder(Order order, User user) {
		order.setAccepted(PaymentService.paymentResult());
		if (order.getUser() == null) {
			order.setUser(user);
			em.persist(order);
		} else {
			em.merge(order);
		}
		em.flush();
		return order.isAccepted();
	}
	
	public Boolean confirmOrder(Order order, User user, Boolean outcome) {
		order.setAccepted(outcome);
		if (order.getUser() == null) {
			order.setUser(user);
			em.persist(order);
		} else {
			em.merge(order);
		}
		em.flush();
		return order.isAccepted();
	}
	
}
