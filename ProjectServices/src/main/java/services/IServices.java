package services;

import model.Admin;
import model.Client;
import model.Flight;

import java.time.LocalDate;
import java.util.List;

public interface IServices {
    void login(Admin admin, IObserver client) throws ProjectException;

    void logout(Admin admin, IObserver client) throws ProjectException;

    List<Flight> getAllAvailableFlights() throws ProjectException;

    List<Flight> searchByDateAndDestination(String destination, LocalDate date) throws ProjectException;

    List<String> getAllClientNames() throws ProjectException;

    Client findClientByName(String name) throws ProjectException;

    void addReservation(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) throws ProjectException;
}
