package it.polito.tdp.flight.model;

public class AeroportoPasseggeri {

	private Airport aeroporto;
	private int passeggeri;
	
	public AeroportoPasseggeri(Airport a, Integer passeggeri) {
		this.aeroporto=a;
		this.passeggeri=passeggeri;
	}

	public Airport getAeroporto() {
		return aeroporto;
	}

	public int getPasseggeri() {
		return passeggeri;
	}
	
	
	
	
}
