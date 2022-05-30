package it.polimi.db2.jpaproject.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import it.polimi.db2.jpaproject.entities.*;

@Stateless
public class MobilePhoneService {

	public List<MobilePhone> newMobilePhone(List<String> minutes, List<String> sms, List<String> extraMinutesFee, List<String> extraSmsFee){
		List<MobilePhone> list = new ArrayList<MobilePhone>();
		for(int i=0; i < minutes.size(); i++) {
			Integer m = Integer.valueOf(minutes.get(i));
			Integer s = Integer.valueOf(sms.get(i));
			BigDecimal mfee = new BigDecimal(extraMinutesFee.get(i));
			BigDecimal sfee = new BigDecimal(extraSmsFee.get(i));
			list.add(new MobilePhone(m, s, mfee, sfee));
		}
		return list;
	}
}
