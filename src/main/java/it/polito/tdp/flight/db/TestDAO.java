package it.polito.tdp.flight.db;

import java.util.List;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Route;

public class TestDAO {

	public static void main(String args[]) {
     	FlightDAO dao = new FlightDAO();

 		List<Airline> airlines = dao.getAllAirlines();
 		System.out.println(airlines);
 
 	//	List<Airport> airports = dao.getAllAirports();
 	//	System.out.println(airports);
 
     	List<Route> routes = dao.getAllRoutes();
		System.out.println(routes);
 	}


}
