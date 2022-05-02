package it.polimi.db2.jpaproject.services;

import java.util.Random;

public final class PaymentService {
	
	// simulate external payment service
	static Boolean paymentResult() {
		Random random = new Random();
	    return random.nextBoolean();
	}
}
