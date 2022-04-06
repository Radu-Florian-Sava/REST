package server;

import model.Admin;
import model.Client;
import model.Flight;
import repository.AdminRepo;
import repository.ClientRepo;
import repository.FlightRepo;
import repository.ReservationRepo;
import services.IObserver;
import services.IServices;
import services.ProjectException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesImpl implements IServices {
    private AdminRepo adminRepo;
    private FlightRepo flightRepo;
    private ClientRepo clientRepo;
    private ReservationRepo reservationRepo;
    private Map<String, IObserver> loggedAdmins;

    public ServicesImpl(AdminRepo adminRepo, FlightRepo flightRepo, ClientRepo clientRepo, ReservationRepo reservationRepo) {

        this.adminRepo = adminRepo;
        this.flightRepo = flightRepo;
        this.clientRepo = clientRepo;
        this.reservationRepo = reservationRepo;
        loggedAdmins = new ConcurrentHashMap<>();
    }

    @Override
    public void login(Admin admin, IObserver client) throws ProjectException {
        Admin tAdmin = adminRepo.findByUsername(admin.getUsername());
        if (tAdmin != null) {
            if (loggedAdmins.get(tAdmin.getUsername()) != null)
                throw new ProjectException("User already logged in.");
            loggedAdmins.put(tAdmin.getUsername(), client);
        } else
            throw new ProjectException("Authentication failed.");
    }

    @Override
    public void logout(Admin admin, IObserver client) throws ProjectException {
        IObserver localAdmin = loggedAdmins.remove(admin.getUsername());
        if (localAdmin == null)
            throw new ProjectException("User " + admin.getUsername() + " is not logged in.");
    }

    @Override
    public List<Flight> getAllAvailableFlights() throws ProjectException {
        return null;
    }

    @Override
    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) throws ProjectException {
        return null;
    }

    @Override
    public List<String> getAllClientNames() throws ProjectException {
        return null;
    }

    @Override
    public Client findClientByName(String name) throws ProjectException {
        return null;
    }

    @Override
    public void addReservation(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) throws ProjectException {

    }
}
