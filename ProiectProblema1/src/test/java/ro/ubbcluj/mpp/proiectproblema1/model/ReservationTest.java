package ro.ubbcluj.mpp.proiectproblema1.model;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest {

    Reservation reservation;
    @BeforeEach
    void setReservation(){
        reservation = new Reservation(12,74, List.of("Alex", "Elena", "Florin"), 3);
    }
    @Test
    void getTouristNames() {
        assertEquals(reservation.getTouristNames(), List.of("Alex", "Elena", "Florin"));
    }

    @Test
    void setTouristNames() {
        reservation.setTouristNames(List.of("Ion", "Maria"));
        assertEquals(reservation.getTouristNames(),List.of("Ion", "Maria"));
    }

    @Test
    void getNumberOfSeats() {
        assertEquals(reservation.getNumberOfSeats(), 3);
    }

    @Test
    void setNumberOfSeats() {
        reservation.setNumberOfSeats(17);
        assertEquals(reservation.getNumberOfSeats(), 17);
    }

    @Test
    void getID() {
        assertEquals(reservation.getID().getKey(), 12);
        assertEquals(reservation.getID().getValue(), 74);
    }


    @Test
    void setID() {
        reservation.setID(new Pair(128,36));
        assertEquals(reservation.getID().getKey(), 128);
        assertEquals(reservation.getID().getValue(), 36);
    }
}