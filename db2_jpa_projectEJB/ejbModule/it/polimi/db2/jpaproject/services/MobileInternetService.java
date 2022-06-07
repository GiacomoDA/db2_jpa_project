package it.polimi.db2.jpaproject.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import it.polimi.db2.jpaproject.entities.*;

@Stateless
public class MobileInternetService {

	public List<MobileInternet> newMobileInternet(List<String> gigabytes, List<String> gigabytesFee){
		List<MobileInternet> list = new ArrayList<MobileInternet>();
		for(int i=0; i < gigabytes.size(); i++) {
			Integer g = Integer.valueOf(gigabytes.get(i));
			BigDecimal gfee = new BigDecimal(gigabytesFee.get(i));
			list.add(new MobileInternet(g, gfee));
		}
		return list;
	}
}
