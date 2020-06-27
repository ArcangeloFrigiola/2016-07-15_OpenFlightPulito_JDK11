package it.polito.tdp.flight.model;

import java.util.Comparator;

public class comparatoreAeroportoPasseggeri implements Comparator<AeroportoPasseggeri> {

	@Override
	public int compare(AeroportoPasseggeri o1, AeroportoPasseggeri o2) {
		// TODO Auto-generated method stub
		return -(o1.getPasseggeri()-o2.getPasseggeri());
	}

}
