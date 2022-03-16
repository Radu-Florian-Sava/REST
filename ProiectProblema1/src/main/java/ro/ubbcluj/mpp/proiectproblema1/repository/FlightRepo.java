package ro.ubbcluj.mpp.proiectproblema1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.ubbcluj.mpp.proiectproblema1.model.Flight;
import ro.ubbcluj.mpp.proiectproblema1.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlightRepo implements AbstractFlightRepo{

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public FlightRepo(Properties properties) {
        logger.info("Initializing FlightRepo with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Flight elem) {
        logger.traceEntry("saving flight {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("insert into Flights(destination, dateTime, airport, " +
                "numberOfTickets) values (?,?,?,?)" ) ){

            preStm.setString(1,elem.getDestination());
            preStm.setString(2,elem.getDateTime().toString());
            preStm.setString(3,elem.getAirport());
            preStm.setInt(4, elem.getNumberOfTickets());
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting flight with id {}", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("delete from Flights where id=?" ) ){

            preStm.setInt(1, id);
            int result = preStm.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Flight elem, Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("UPDATE Flights SET destination=?, dateTime=?, airport=?, numberOfTickets=? WHERE id=?");
            statement.setString(1,elem.getDestination());
            statement.setString(2,elem.getDateTime().toString());
            statement.setString(3,elem.getAirport());
            statement.setInt(4, elem.getNumberOfTickets());
            statement.setInt(5, id);
            Flight updated = findById(id);
            int result = statement.executeUpdate();
            logger.trace("Updated {} instances",result);
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Flight findById(Integer id) {
        logger.traceEntry("looking for flight with id {}", id);
        Flight flight = null;
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("select destination, dateTime, airport, numberOfTickets " +
                "from Flights where id=?" ) ){
            preStm.setInt(1, id);
            ResultSet result = preStm.executeQuery();
            result.next();
            String destination = result.getString("destination");
            String datetime = result.getString("dateTime");
            String airport = result.getString("airport");
            int numberOfTickets = result.getInt("numberOfTickets");
            flight = new Flight(id, destination, LocalDateTime.parse(datetime), airport, numberOfTickets);
            result.close();
            logger.trace("Found {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
        return flight;
    }

    @Override
    public List<Flight> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("SELECT * FROM Flights");
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
        }
        catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit(flights);
        return flights;
    }
}
