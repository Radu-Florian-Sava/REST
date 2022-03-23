package ro.ubbcluj.mpp.proiectproblema1.repository;

import javafx.util.Pair;
import ro.ubbcluj.mpp.proiectproblema1.model.Client;
import ro.ubbcluj.mpp.proiectproblema1.model.Flight;
import ro.ubbcluj.mpp.proiectproblema1.model.Reservation;

public interface IReservationRepo extends Repository<Reservation, Pair<Flight, Client>>{

}
