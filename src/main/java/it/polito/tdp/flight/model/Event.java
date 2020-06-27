package it.polito.tdp.flight.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		PARTENZA,
		STOP,
		PAUSE,
		VIAGGIO
	}
	
	private EventType tipoEvento;
	private int idPasseggero;
	private Airport aeroportoPartenza;
	private LocalDateTime ora;
	/**
	 * @param tipoEvento
	 * @param idPasseggero
	 * @param aeroportoPartenza
	 * @param dataPartenza
	 */
	public Event(EventType tipoEvento, int idPasseggero, Airport aeroportoPartenza, LocalDateTime dataPartenza) {
		super();
		this.tipoEvento = tipoEvento;
		this.idPasseggero = idPasseggero;
		this.aeroportoPartenza = aeroportoPartenza;
		this.ora = dataPartenza;
	}
	public EventType getTipoEvento() {
		return tipoEvento;
	}
	public int getIdPasseggero() {
		return idPasseggero;
	}
	public Airport getAeroportoPartenza() {
		return aeroportoPartenza;
	}
	public LocalDateTime getOra() {
		return ora;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPasseggero;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (idPasseggero != other.idPasseggero)
			return false;
		return true;
	}
	@Override
	public int compareTo(Event o) {
		
		return this.ora.compareTo(o.getOra());
	}
	
	
	
}
