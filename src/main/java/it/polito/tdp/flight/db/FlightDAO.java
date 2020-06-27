package it.polito.tdp.flight.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Route;

public class FlightDAO {

	public List<Airline> getAllAirlines() {
		String sql = "SELECT * FROM airline";
		List<Airline> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Airline(res.getInt("Airline_ID"), res.getString("Name"), res.getString("Alias"),
						res.getString("IATA"), res.getString("ICAO"), res.getString("Callsign"),
						res.getString("Country"), res.getString("Active")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Route> getAllRoutes() {
		String sql = "SELECT * FROM route";
		List<Route> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Route(res.getString("Airline"), res.getInt("Airline_ID"), res.getString("Source_airport"),
						res.getInt("Source_airport_ID"), res.getString("Destination_airport"),
						res.getInt("Destination_airport_ID"), res.getString("Codeshare"), res.getInt("Stops"),
						res.getString("Equipment")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Airport> getAllAirports(Map<Integer, Airport> idMap) {
		String sql = "SELECT * FROM airport";
		List<Airport> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				Airport temp = new Airport(res.getInt("Airport_ID"), res.getString("name"), res.getString("city"),
						res.getString("country"), res.getString("IATA_FAA"), res.getString("ICAO"),
						res.getDouble("Latitude"), res.getDouble("Longitude"), res.getFloat("timezone"),
						res.getString("dst"), res.getString("tz"));
				list.add(temp);
				
				idMap.put(res.getInt("Airport_ID"), temp);
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void setEdges(Graph<Airport, DefaultWeightedEdge> grafo, double distance, Map<Integer, Airport> idMap) {
		
		String sql = "SELECT ar1.Airport_ID AS id1, ar2.Airport_ID AS id2, "
				+ "ar1.Latitude AS a1Lat, ar2.Latitude AS a2Lat, ar1.Longitude AS a1Lon, ar2.Longitude AS a2Lon " + 
				"FROM airport AS ar1, airport AS ar2, route AS r " + 
				"WHERE ar1.Airport_ID!=ar2.Airport_ID " + 
				"AND r.Source_airport_ID=ar1.Airport_ID " + 
				"AND r.Destination_airport_ID=ar2.Airport_ID ";
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				double lat_a = Math.PI*res.getDouble("a1Lat")/180;
				double lat_b = Math.PI*res.getDouble("a2Lat")/180;
				double long_a = Math.PI*res.getDouble("a1Lon")/180;
				double long_b = Math.PI*res.getDouble("a2Lon")/180;
				double fab = Math.abs(long_a - long_b);
				
				double dist = Math.acos(Math.sin(lat_a)*Math.sin(lat_b)+Math.cos(lat_a)*Math.cos(lat_b)*Math.cos(fab))*6371;
				
				if(dist<distance) {
					
					Graphs.addEdge(grafo, idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")), dist/800);
				}
			}
			conn.close();
			return ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	


}


