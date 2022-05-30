package it.polimi.db2.jpaproject.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import it.polimi.db2.jpaproject.entities.*;

@Stateless
public class FixedInternetService {

	public List<FixedInternet> newFixedInternet(List<String> gigabytes, List<String> gigabytesFee){
		List<FixedInternet> list = new ArrayList<FixedInternet>();
		for(int i=0; i < gigabytes.size(); i++) {
			Integer g = Integer.valueOf(gigabytes.get(i));
			BigDecimal gfee = new BigDecimal(Integer.valueOf(gigabytesFee.get(i)));
			list.add(new FixedInternet(g, gfee));
		}
		return list;
	}
}
