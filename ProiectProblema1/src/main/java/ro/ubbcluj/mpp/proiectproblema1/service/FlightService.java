package ro.ubbcluj.mpp.proiectproblema1.service;

import ro.ubbcluj.mpp.proiectproblema1.model.Flight;
import ro.ubbcluj.mpp.proiectproblema1.repository.FlightRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class FlightService {
    FlightRepo flightRepo;

    public FlightService(FlightRepo flightRepo) {
        this.flightRepo = flightRepo;
    }

    public List<Flight> getAllAvailable() {
        return flightRepo.getAll().stream().filter(x -> x.getNumberOfTickets() > 0).toList();
    }

    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) {
        return flightRepo.getAll().stream().filter(
                x -> Objects.equals(x.getDestination(), destination) &&
                        x.getDateTime().toLocalDate().equals(date) &&
                        x.getNumberOfTickets() > 0
        ).toList();
    }
}