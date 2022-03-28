package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.util.Objects;

public class VPKey implements Serializable{
	private static final long serialVersionUID = 1L;

	private int servicePackage;

	private int months;

	public VPKey() {
		super();
	}

	public VPKey(int servicePackage, int months) {
		super();
		this.servicePackage = servicePackage;
		this.months = months;
	}

	@Override
	public int hashCode() {
		return Objects.hash(months, servicePackage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VPKey other = (VPKey) obj;
		return months == other.months && servicePackage == other.servicePackage;
	}
	
}