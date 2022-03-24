package ro.ubbcluj.mpp.proiectproblema1.service;

import ro.ubbcluj.mpp.proiectproblema1.model.Flight;
import ro.ubbcluj.mpp.proiectproblema1.repository.FlightRepo;

import java.time.LocalDate;
import java.util.List;

public class FlightService {
    FlightRepo flightRepo;

    public FlightService(FlightRepo flightRepo) {
        this.flightRepo = flightRepo;
    }

    public List<Flight> getAllAvailable() {
        return flightRepo.getAllAvailable();
    }

    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) {
        return flightRepo.searchByDateAndDestination(destination, date);
    }
}