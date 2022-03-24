package ro.ubbcluj.mpp.proiectproblema1.control;

import ro.ubbcluj.mpp.proiectproblema1.model.Admin;
import ro.ubbcluj.mpp.proiectproblema1.model.Client;
import ro.ubbcluj.mpp.proiectproblema1.model.Flight;
import ro.ubbcluj.mpp.proiectproblema1.model.Reservation;
import ro.ubbcluj.mpp.proiectproblema1.repository.AdminRepo;
import ro.ubbcluj.mpp.proiectproblema1.repository.ClientRepo;
import ro.ubbcluj.mpp.proiectproblema1.repository.FlightRepo;
import ro.ubbcluj.mpp.proiectproblema1.repository.ReservationRepo;
import ro.ubbcluj.mpp.proiectproblema1.service.AdminService;
import ro.ubbcluj.mpp.proiectproblema1.service.ClientService;
import ro.ubbcluj.mpp.proiectproblema1.service.FlightService;
import ro.ubbcluj.mpp.proiectproblema1.service.ReservationService;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class Controller {
    private AdminService adminService;
    private FlightService flightService;
    private ClientService clientService;
    private ReservationService reservationService;

    public Controller() {
        Properties props = new Properties();
        props.setProperty("foreign_keys", "on");
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        adminService = new AdminService(new AdminRepo(props));
        flightService = new FlightService(new FlightRepo(props));
        clientService = new ClientService(new ClientRepo(props));
        reservationService = new ReservationService(new ReservationRepo(props));
    }

    public Admin login(String username) {
        return adminService.login(username);
    }

    public List<Flight> getAllAvailableFlights() {
        return flightService.getAllAvailable();
    }

    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) {
        return flightService.searchByDateAndDestination(destination, date);
    }

    public List<String> getAllClientNames() {
        return clientService.getAll().stream().map(x -> x.getName()).toList();
    }

    public Client findClientByName(String name) {
        return clientService.findByName(name);
    }

    public void addReservation(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) {
        Client client = findClientByName(clientName);
        Reservation reservation = new Reservation(flight, client, clientNames, numberOfSeats);
        reservationService.addReservation(reservation);
    }

}
