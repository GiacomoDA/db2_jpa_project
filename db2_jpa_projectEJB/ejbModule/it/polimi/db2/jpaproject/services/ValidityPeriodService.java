package it.polimi.db2.jpaproject.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import it.polimi.db2.jpaproject.entities.ValidityPeriod;

@Stateless
public class ValidityPeriodService {

	public List<ValidityPeriod> newValidityPeriods(List<String> months, List<String> fee){
		List<ValidityPeriod> list = new ArrayList<ValidityPeriod>();
		for(int i=0; i < months.size(); i++) {
			Integer m = Integer.valueOf(months.get(i));
			BigDecimal f = new BigDecimal(fee.get(i));
			list.add(new ValidityPeriod(m, f));
		}
		return list;
	}
}
