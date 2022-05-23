package it.polimi.db2.jpaproject.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.db2.jpaproject.entities.MobilePhone;
import it.polimi.db2.jpaproject.entities.FixedInternet;
import it.polimi.db2.jpaproject.entities.MobileInternet;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class ServiceService {
	@PersistenceContext(unitName = "JPAProject")
	private EntityManager em;

	public ServiceService() {
	}

	public MobilePhone newMobilePhone(Integer minutes, Integer sms, BigDecimal minutesFee, BigDecimal smsFee) {
		return new MobilePhone(minutes, sms, minutesFee, smsFee);
	}
	
	public FixedInternet newFixedInternet(Integer gigabytes, BigDecimal gigabytesFee) {
		return new FixedInternet(gigabytes, gigabytesFee);
	}
	
	public MobileInternet newMobileInternet(Integer gigabytes, BigDecimal gigabytesFee) {
		return new MobileInternet(gigabytes, gigabytesFee);
	}
}
