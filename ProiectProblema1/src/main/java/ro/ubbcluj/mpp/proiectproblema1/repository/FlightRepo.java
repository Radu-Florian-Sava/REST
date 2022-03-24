package ro.ubbcluj.mpp.proiectproblema1.repository;

import ro.ubbcluj.mpp.proiectproblema1.model.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlightRepo extends IFlightRepo {

    public FlightRepo(Properties properties) {
        super(properties);
    }

    public List<Flight> getAllAvailable() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("SELECT * FROM Flights where numberOfTickets >0;");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String destination = result.getString("destination");
                String datetime = result.getString("dateTime");
                String airport = result.getString("airport");
                int numberOfTickets = result.getInt("numberOfTickets");
                Flight flight = new Flight(id, destination, LocalDateTime.parse(datetime), airport, numberOfTickets);
                flights.add(flight);
            }
            result.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit(flights);
        return flights;
    }

    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String searchDate;
        if(date == null){
            searchDate="";
        }
        else{
            searchDate=date.format(formatter);
        }
        logger.traceEntry("looking for flights with destination {} and date {} ", destination, searchDate );
        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("SELECT * FROM Flights where numberOfTickets >0 and destination LIKE ? and dateTime like ? ;");
            statement.setString(1, "%"+destination+"%");
            statement.setString(2, searchDate +"%");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String datetime = result.getString("dateTime");
                String airport = result.getString("airport");
                int numberOfTickets = result.getInt("numberOfTickets");
                Flight flight = new Flight(id, destination, LocalDateTime.parse(datetime), airport, numberOfTickets);
                flights.add(flight);
            }
            result.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit(flights);
        return flights;
    }
}