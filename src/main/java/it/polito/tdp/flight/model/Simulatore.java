package it.polito.tdp.flight.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.flight.model.Event.EventType;

public class Simulatore {
	
	//PARAMETRI SIMULAZIONE
	private int passeggeri;
	private PriorityQueue<Event> queue;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private List<Airport> listaAeroporti;
	private final LocalDateTime dataPartenza = LocalDateTime.of(2000, 12, 1, 6, 0);
	
	
	//PARAMETRI DA CALCOLARE
	private Map<Airport, Integer> aeroportoPersone;

	public void initialize(Graph<Airport, DefaultWeightedEdge> grafo, int passeggeri) {
		
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.grafo=grafo;
		this.passeggeri=passeggeri;
		this.listaAeroporti = new ArrayList<>(this.grafo.vertexSet());
		
		this.queue=new PriorityQueue<>();
		this.aeroportoPersone=new HashMap<>();
		
		for(int i=0; i<this.passeggeri; i++) {
			
			int random = Math.round((float)(Math.random()*this.grafo.vertexSet().size()));
			Event e = new Event(EventType.PARTENZA, i, this.listaAeroporti.get(random), this.dataPartenza);
			this.queue.add(e);
		}
	}

	public void run() {
		
		while(!this.queue.isEmpty()) {
			
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		
		switch(e.getTipoEvento()) {
		
		case PARTENZA:
			
			List<Airport> temp = Graphs.successorListOf(this.grafo, e.getAeroportoPartenza());
			
			if(temp.size()==0) {
				break;
			} else {
				int random = Math.round((float) (Math.random() * temp.size()));
				Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), temp.get(random),
						this.dataPartenza.plusHours(1 + Math.round((float) this.grafo
								.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp.get(random))))));
				this.queue.add(nuovo);
			}
			break;
			
		case VIAGGIO:
			
			List<Airport> temp2 = Graphs.successorListOf(this.grafo, e.getAeroportoPartenza());
			if(e.getOra().compareTo(dataPartenza.plusHours(48))>=0) {
				Event nuovo = new Event(EventType.STOP, e.getIdPasseggero(), e.getAeroportoPartenza(), e.getOra());
				this.queue.add(nuovo);
				break;
			}else {
				
				if(e.getOra().getHour()>23 && e.getOra().getHour()<7) { //Se sono passate le ventitré non ci sono più voli e mi fermo
					
					if(e.getOra().getHour()>23) {
						Event nuovo = new Event(EventType.PAUSE, e.getIdPasseggero(), e.getAeroportoPartenza(), e.getOra().plusHours(1));
						this.queue.add(nuovo);
						break;
					}else {
						int random = Math.round((float) (Math.random() * temp2.size()));
						Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), e.getAeroportoPartenza(),
								e.getOra().plusHours((7-e.getOra().getHour())+Math.round((float) this.grafo
										.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp2.get(random))))));
						this.queue.add(nuovo);
						break;
					}
					
				}else {
					
					int random = Math.round((float) (Math.random() * temp2.size()));
					Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), temp2.get(random),
							this.dataPartenza.plusHours(1 + Math.round((float) this.grafo
									.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp2.get(random))))));
					this.queue.add(nuovo);
				
					break;
				}
				
			}
			
		case PAUSE:
			break;
			
		case STOP:
			break;
			
		}
		
	}

	public Map<Airport, Integer> getMappaAeroportiPasseggeri() {
		// TODO Auto-generated method stub
		return null;
	}

}
