package it.polimi.db2.jpaproject.entities;

import java.io.Serializable;
import java.util.Objects;

public class VPSKey implements Serializable{
	private static final long serialVersionUID = 1L;

	private int packageSales;

	private int months;

	public VPSKey() {
		super();
	}

	public VPSKey(int packageSales, int months) {
		super();
		this.packageSales = packageSales;
		this.months = months;
	}

	@Override
	public int hashCode() {
		return Objects.hash(months, packageSales);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VPSKey other = (VPSKey) obj;
		return months == other.months && packageSales == other.packageSales;
	}
	
}