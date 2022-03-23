package ro.ubbcluj.mpp.proiectproblema1.repository;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.ubbcluj.mpp.proiectproblema1.model.Client;
import ro.ubbcluj.mpp.proiectproblema1.model.Flight;
import ro.ubbcluj.mpp.proiectproblema1.model.Reservation;
import ro.ubbcluj.mpp.proiectproblema1.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IReservationRepo implements Repository<Reservation, Pair<Flight, Client>> {
    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;
    private FlightRepo flightRepo;
    private ClientRepo clientRepo;

    public IReservationRepo(Properties properties, FlightRepo flightRepo, ClientRepo clientRepo) {
        logger.info("Initializing FlightRepo with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
        this.clientRepo = clientRepo;
        this.flightRepo = flightRepo;
    }

    @Override
    public void add(Reservation elem) {
        logger.traceEntry("saving reservation {}", elem);
        if (elem.getNumberOfSeats() > elem.getID().getKey().getNumberOfTickets()) {
            logger.traceExit();
            return;
        }
        Connection con = dbUtils.getConnection();
        int flightId = elem.getID().getKey().getID();
        int clientId = elem.getID().getValue().getID();
        try (PreparedStatement preStm = con.prepareStatement("insert into Reservations(flightId, clientId, numberOfSeats)" +
                " values (?,?,?)")) {

            preStm.setInt(1, flightId);
            preStm.setInt(2, clientId);
            preStm.setInt(3, elem.getNumberOfSeats());
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
            Connection conTourists = dbUtils.getConnection();
            for (String x : elem.getTouristNames()) {
                try (PreparedStatement prepTourists = conTourists.prepareStatement("insert into " +
                        "ReservationsTourists(flightId, clientId, name)" +
                        " values (?, ?, ?)")) {
                    prepTourists.setInt(1, flightId);
                    prepTourists.setInt(2, clientId);
                    prepTourists.setString(3, x);
                    int resultTourist = prepTourists.executeUpdate();
                    logger.trace("Saved {} instances", resultTourist);
                }
            }
            int newNumberOfTickets = elem.getID().getKey().getNumberOfTickets() - elem.getNumberOfSeats();
            elem.getID().getKey().setNumberOfTickets(newNumberOfTickets);
            flightRepo.update(elem.getID().getKey(), elem.getID().getKey().getID());
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Pair<Flight, Client> id) {
        logger.traceEntry("deleting reservation with id {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("delete from Reservations where flightId=? and clientId=?")) {

            preStm.setInt(1, id.getKey().getID());
            preStm.setInt(2, id.getValue().getID());
            int newNumberOfTickets = id.getKey().getNumberOfTickets() + findById(id).getNumberOfSeats();
            id.getKey().setNumberOfTickets(newNumberOfTickets);
            flightRepo.update(id.getKey(), id.getKey().getID());
            int result = preStm.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Reservation elem, Pair<Flight, Client> id) {
    }

    @Override
    public Reservation findById(Pair<Flight, Client> id) {
        logger.traceEntry("looking for reservation with id {}", id);
        Reservation reservation = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select numberOfSeats " +
                "from Reservations where flightId=? and clientId=?")) {
            preStm.setInt(1, id.getKey().getID());
            preStm.setInt(2, id.getValue().getID());
            ResultSet result = preStm.executeQuery();
            result.next();
            int numberOfSeats = result.getInt("numberOfSeats");
            Flight flight = flightRepo.findById(id.getKey().getID());
            Client client = clientRepo.findById(id.getValue().getID());
            List<String> names = new ArrayList<>();
            Connection conTourists = dbUtils.getConnection();
            PreparedStatement prepTourists = conTourists.prepareStatement("select * from ReservationsTourists where " +
                    "flightId=? and clientId=?");
            prepTourists.setInt(1, id.getKey().getID());
            prepTourists.setInt(2, id.getValue().getID());
            ResultSet resultSet = prepTourists.executeQuery();
            while (resultSet.next())
                names.add(resultSet.getString("name"));
            resultSet.close();
            reservation = new Reservation(flight, client, names, numberOfSeats);
            result.close();
            logger.trace("Found {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD" + ex);
        }
        logger.traceExit();
        return reservation;
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select * from Reservations ")) {
            ResultSet result = preStm.executeQuery();
            while (result.next()) {
                int clientId = result.getInt("clientId");
                int flightId = result.getInt("flightId");
                int numberOfSeats = result.getInt("numberOfSeats");
                Flight flight = flightRepo.findById(flightId);
                Client client = clientRepo.findById(clientId);
                List<String> names = new ArrayList<>();
                PreparedStatement prepTourists = con.prepareStatement("select * from ReservationsTourists where " +
                        "flightId=? and clientId=?");
                prepTourists.setInt(1, flightId);
                prepTourists.setInt(2, clientId);
                ResultSet resultSet = prepTourists.executeQuery();
                while (resultSet.next())
                    names.add(resultSet.getString("name"));
                Reservation reservation = new Reservation(flight, client, names, numberOfSeats);
                reservations.add(reservation);
                resultSet.close();
            }
            result.close();
            logger.trace("Found {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD" + ex);
        }
        logger.traceExit();
        return reservations;
    }
}