package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private FlightDAO dao;
	private Map<Integer, Airport> idMap;
	private List<Airport> airportsConnected;
	
	public void creaGrafo(double distance) {
		
		this.dao = new FlightDAO();
		this.idMap = new HashMap<>();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getAllAirports(this.idMap));
		
		this.dao.setEdges(this.grafo, distance, this.idMap);
		
		this.airportsConnected = new ArrayList<>();
		for(Airport a: this.grafo.vertexSet()) {
			
			if(Graphs.predecessorListOf(this.grafo, a).size()!=0 || Graphs.successorListOf(this.grafo, a).size()!=0) {
				this.airportsConnected.add(a);
			}
		}
	}
	
	private List<Airport> visitaAmpiezza(Airport source){
		
		GraphIterator<Airport, DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo, source); 
		//si posiziona sul primo elemento del grafo che gli passiamo
		
		List<Airport> visit = new ArrayList<>();
		
		while(bfv.hasNext()) {
			visit.add(bfv.next());
		}
		
		return visit;
	}
	
	private Airport getAeroportoPiuLontano() {
		
		List<Airport> visita = new ArrayList<>(visitaAmpiezza(this.idMap.get(1555)));
		double best = 0.0;
		Airport bestA = null;
		
		Airport fiumicino = this.idMap.get(1555);
		final double lat_a = Math.PI*fiumicino.getLatitude()/180;
		final double long_a = Math.PI*fiumicino.getLongitude()/180;
		
		for(Airport a: visita) {
			
			
			double lat_b = Math.PI*a.getLatitude()/180;
			double long_b = Math.PI*a.getLongitude()/180;
			double fab = Math.abs(long_a - long_b);
			
			double dist = Math.acos(Math.sin(lat_a)*Math.sin(lat_b)+Math.cos(lat_a)*Math.cos(lat_b)*Math.cos(fab))*6371;
			
			if(dist>best) {
				bestA = a;
			}
		}
		return bestA;
	}
	
	public String cercaRaggiungibilita() {
		
		boolean trovato = false;
		int size = 0;
		
	
		for(Airport a: this.airportsConnected) {
			
			List<Airport> temp = new ArrayList<>(visitaAmpiezza(a));
			if(temp.size()==(this.airportsConnected.size()-1)) {
				trovato = true;
				size = temp.size();
				break;
			}
			
			if(temp.size()>size) {
				size=temp.size();
			}
			
		}
	
		
		if(trovato) {
			return "E' possibile raggiungere tutti gli aeroporti da ogni aeroporto del grafo creato!\n"
					+ "Nodi collegati: "+size+"\nNodi totali: "+this.airportsConnected.size()+
					"Aeroporto piu' lontano da Fiumicino: "+getAeroportoPiuLontano().getName();
		}
		
		return "Non è possibile raggiungere ogni aeroporto da ogni aeroporto all'interno del grafo creato!\n" + 
		"Nodi collegati: "+size+"\nNodi totali: "+this.airportsConnected.size()+
		"\nAeroporto piu' lontano da Fiumicino: "+getAeroportoPiuLontano().getName();
	}
	 
	
	public int getNvertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNarchi() {
		return this.grafo.edgeSet().size();
	}

	public String simulazione(int passeggeri) {
		
		Simulatore sim = new Simulatore();
		
		sim.initialize(this.grafo, passeggeri);
		sim.run();
		Map<Airport, Integer> temp = new HashMap<>(sim.getMappaAeroportiPasseggeri());
		
		List<AeroportoPasseggeri> lista = new ArrayList<>();
		for(Airport a: temp.keySet()) {
			
			if(temp.get(a)>0) {
				lista.add(new AeroportoPasseggeri(a, temp.get(a)));
			}
		}
		
		Collections.sort(lista, new comparatoreAeroportoPasseggeri());
		String result = "Lista aeroporti:\n";
		for(AeroportoPasseggeri a: lista) {
			result+= a.getAeroporto()+", #passeggeri: "+a.getPasseggeri()+"\n";
		}
		return result;
	}

}


