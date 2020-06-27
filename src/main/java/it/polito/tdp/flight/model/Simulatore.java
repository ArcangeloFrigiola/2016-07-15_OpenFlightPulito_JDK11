package it.polito.tdp.flight.model;

import java.time.LocalDateTime;
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
		
		for(Airport a: this.listaAeroporti) {
			this.aeroportoPersone.put(a, 0);
		}
		
		for(int i=0; i<this.passeggeri; i++) {
			
			int random = Math.round((float)(Math.random()*this.grafo.vertexSet().size()));
			Event e = new Event(EventType.PARTENZA, i, this.listaAeroporti.get(random), this.dataPartenza);
			this.queue.add(e);
			this.aeroportoPersone.replace(this.listaAeroporti.get(random), 1);
		}
	}

	public void run() {
		
		while(!this.queue.isEmpty()) {
			
			Event e = this.queue.poll();
			processEvent(e);
		}
		
	}

	private void processEvent(Event e) {
		
		List<Airport> temp = Graphs.successorListOf(this.grafo, e.getAeroportoPartenza());
		switch(e.getTipoEvento()) {
		
		case PARTENZA:
			
			
			
			if(temp.size()==0) {
				break;
			} else {
				int random = Math.round((float) (Math.random() *( temp.size()-1)));
				Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), temp.get(random),
						this.dataPartenza.plusHours(1 + Math.round((float) this.grafo
								.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp.get(random))))));
				this.queue.add(nuovo);
				
				this.aeroportoPersone.replace(e.getAeroportoPartenza(), 
						(this.aeroportoPersone.get(e.getAeroportoPartenza())-1));
				this.aeroportoPersone.replace(this.listaAeroporti.get(random), (this.aeroportoPersone.get(this.listaAeroporti.get(random))+1));
				break;
			}
			
			
		case VIAGGIO:
			
			if(temp.size()==0) {
				Event nuovo = new Event(EventType.STOP, e.getIdPasseggero(), e.getAeroportoPartenza(), e.getOra());
				this.queue.add(nuovo);
				break;
			}
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
						int random = Math.round((float) (Math.random() * (temp.size()-1)));
						Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), e.getAeroportoPartenza(),
								e.getOra().plusHours((7-e.getOra().getHour())+Math.round((float) this.grafo
										.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp.get(random))))));
						this.queue.add(nuovo);
						this.aeroportoPersone.replace(e.getAeroportoPartenza(), 
								this.aeroportoPersone.get(e.getAeroportoPartenza())-1);
						this.aeroportoPersone.replace(this.listaAeroporti.get(random), this.aeroportoPersone.get(this.listaAeroporti.get(random))+1);
						break;
					}
					
				}else {
					
					if(e.getOra().getHour()%2!=0) {
						
						int random = Math.round((float) (Math.random() * (temp.size()-1)));
						Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), temp.get(random),
								e.getOra().plusHours(Math.round((float) this.grafo
										.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp.get(random))))));
						this.queue.add(nuovo);
						this.aeroportoPersone.replace(e.getAeroportoPartenza(), 
								this.aeroportoPersone.get(e.getAeroportoPartenza())-1);
						this.aeroportoPersone.replace(this.listaAeroporti.get(random), this.aeroportoPersone.get(this.listaAeroporti.get(random))+1);
					
						break;
					}else {
						int random = Math.round((float) (Math.random() * (temp.size()-1)));
						Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), temp.get(random),
								e.getOra().plusHours(1 + Math.round((float) this.grafo
										.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp.get(random))))));
						this.queue.add(nuovo);
						this.aeroportoPersone.replace(e.getAeroportoPartenza(), 
								this.aeroportoPersone.get(e.getAeroportoPartenza())-1);
						this.aeroportoPersone.replace(this.listaAeroporti.get(random), this.aeroportoPersone.get(this.listaAeroporti.get(random))+1);
					
						break;
					}
					
				}
				
			}
			
		case PAUSE:
			
			int ran = Math.round((float) (Math.random() * (temp.size()-1)));
			Event nuovo = new Event(EventType.VIAGGIO, e.getIdPasseggero(), e.getAeroportoPartenza(),
					e.getOra().plusHours((7-e.getOra().getHour())+Math.round((float) this.grafo
							.getEdgeWeight(this.grafo.getEdge(e.getAeroportoPartenza(), temp.get(ran))))));
			this.queue.add(nuovo);
			this.aeroportoPersone.replace(e.getAeroportoPartenza(), 
					this.aeroportoPersone.get(e.getAeroportoPartenza())-1);
			this.aeroportoPersone.replace(this.listaAeroporti.get(ran), this.aeroportoPersone.get(this.listaAeroporti.get(ran))+1);
			break;
			
		case STOP:
			break;
			
		}
		
	}

	public Map<Airport, Integer> getMappaAeroportiPasseggeri() {
		// TODO Auto-generated method stub
		return this.aeroportoPersone;
	}

}
