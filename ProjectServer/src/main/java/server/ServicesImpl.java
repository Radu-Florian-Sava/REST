package server;

import model.Admin;
import model.Client;
import model.Flight;
import model.Reservation;
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
    public synchronized void login(Admin admin, IObserver client) throws ProjectException {
        Admin tAdmin = adminRepo.findByUsername(admin.getUsername());
        if (tAdmin != null) {
            if (loggedAdmins.get(tAdmin.getUsername()) != null)
                throw new ProjectException("User already logged in.");
            loggedAdmins.put(tAdmin.getUsername(), client);
        } else
            throw new ProjectException("Authentication failed.");
    }

    @Override
    public synchronized void logout(Admin admin, IObserver client) throws ProjectException {
        IObserver localAdmin = loggedAdmins.remove(admin.getUsername());
        if (localAdmin == null)
            throw new ProjectException("User " + admin.getUsername() + " is not logged in.");
    }

    @Override
    public synchronized List<Flight> getAllAvailableFlights() throws ProjectException {
        List<Flight> flights=flightRepo.getAllAvailable();
        if(flights == null)
            throw new ProjectException("No flights available");
        System.out.println("Avaliable flights: " + flights.size());
        return flights;
    }

    @Override
    public synchronized List<Flight> searchByDateAndDestination(String destination, LocalDate date) throws ProjectException {
        List<Flight> flights=flightRepo.searchByDateAndDestination(destination, date);
        if(flights == null)
            throw new ProjectException("No flights found");
        System.out.println("Avaliable flights: " + flights.size());
        return flights;
    }

    @Override
    public synchronized List<String> getAllClientNames() throws ProjectException {
        List<String> clients=clientRepo.getAllClientNames();
        if(clients == null)
            throw new ProjectException("No clients");
        System.out.println("Number of clients: " + clients.size());
        return clients;
    }

    @Override
    public synchronized Client findClientByName(String name) throws ProjectException {
        if(name == null)
            throw new ProjectException("No name selected");
        return clientRepo.findByName(name);
    }

    @Override
    public synchronized void addReservation(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) throws ProjectException {
        if(clientName == null)
            throw new ProjectException("No name selected");
        if(flight == null)
            throw new ProjectException("No flight selected");
        Client client = clientRepo.findByName(clientName);
        Reservation reservation = new Reservation(flight,client, clientNames, numberOfSeats);
        reservationRepo.add(reservation);
        for(IObserver admin: loggedAdmins.values()){
                admin.flightsChanged(flight);
        }
    }
}
