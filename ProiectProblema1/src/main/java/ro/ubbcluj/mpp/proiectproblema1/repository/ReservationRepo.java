package ro.ubbcluj.mpp.proiectproblema1.repository;

import java.util.Properties;

public class ReservationRepo extends IReservationRepo {

    public ReservationRepo(Properties properties, FlightRepo flightRepo, ClientRepo clientRepo) {
        super(properties, flightRepo, clientRepo);
    }
}