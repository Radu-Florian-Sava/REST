package ro.ubbcluj.mpp.proiectproblema1.service;

import ro.ubbcluj.mpp.proiectproblema1.model.Reservation;
import ro.ubbcluj.mpp.proiectproblema1.repository.ReservationRepo;

public class ReservationService {
    ReservationRepo reservationRepo;

    public ReservationService(ReservationRepo reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    public void addReservation(Reservation reservation){
            reservationRepo.add(reservation);
    }
}
