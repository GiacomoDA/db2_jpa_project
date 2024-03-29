package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrderService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public OrderService() {
	}

	public List<Order> findRejectedOrders(User user) {
		return em.createNamedQuery("Order.findRejectedByUser", Order.class).setParameter(1, user).getResultList();
	}

	public Order findOrderById(int id) {
		return em.createNamedQuery("Order.findById", Order.class).setParameter(1, id).getSingleResult();
	}
	
	public List<SuspendedOrder> suspendedOrderList() {
		return em.createNamedQuery("SuspendedOrder.findAll", SuspendedOrder.class).getResultList();
	}

	public Order createOrder(int packageId, LocalDate activationDate, int months, List<String> opt) {
		BigDecimal total;
		List<OptionalProduct> optionals = new ArrayList<>();

		ServicePackage servicePackage = em.createNamedQuery("ServicePackage.findById", ServicePackage.class)
				.setParameter(1, packageId).getSingleResult();

		ValidityPeriod validityPeriod = em.createNamedQuery("ValidityPeriod.findById", ValidityPeriod.class)
				.setParameter(1, servicePackage).setParameter(2, months).getSingleResult();

		total = BigDecimal.valueOf(validityPeriod.getMonths()).multiply(validityPeriod.getMonthlyFee());

		if (!opt.isEmpty()) {
			optionals = em.createNamedQuery("Optional.findOptionals", OptionalProduct.class).setParameter(1, opt)
					.getResultList();
			total = total.add(optionals.stream().map(o -> o.getMonthlyFee()).reduce(BigDecimal.ZERO, BigDecimal::add)
					.multiply(BigDecimal.valueOf(validityPeriod.getMonths())));
		}

		Order order = new Order(activationDate, total, months, servicePackage, optionals);

		return order;
	}

	// random payment outcome
	public Boolean confirmOrder(Order order, User user) {
		order.setAccepted(PaymentService.paymentResult());
		if (!order.isAccepted()) {
			order.incrementFailedPayments();
			order.setLastRejection();
		}
		if (order.getUser() == null) {
			order.setUser(user);
			em.persist(order);
		} else {
			em.merge(order);
		}
		em.flush();
		return order.isAccepted();
	}

	// payment outcome decided by caller
	public Boolean confirmOrder(Order order, User user, Boolean outcome) {
		order.setAccepted(outcome);
		if (!outcome) {
			order.incrementFailedPayments();
			order.setLastRejection();
		}
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
