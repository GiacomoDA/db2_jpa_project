package it.polimi.db2.jpaproject.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Stateless;

import it.polimi.db2.jpaproject.entities.ValidityPeriod;

@Stateless
public class ValidityPeriodService {

	public List<ValidityPeriod> newValidityPeriods(List<String> months, List<String> fee) throws DuplicateKeyException{
		List<ValidityPeriod> list = new ArrayList<ValidityPeriod>();
		for(int i=0; i < months.size(); i++) {
			Integer m = Integer.valueOf(months.get(i));
			BigDecimal f = new BigDecimal(fee.get(i));
			ValidityPeriod validityPeriod = new ValidityPeriod(m, f);
			for (ValidityPeriod vp : list) {
				if (vp.getMonths().equals(validityPeriod.getMonths())) {
					throw new DuplicateKeyException();
				}
			}
			list.add(validityPeriod);
		}
		return list;
	}
}
