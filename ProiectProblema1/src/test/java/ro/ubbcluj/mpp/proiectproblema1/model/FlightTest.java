package ro.ubbcluj.mpp.proiectproblema1.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    Flight flight;
    @BeforeEach
    void setFlight(){
        flight = new Flight(1, "Paris",
                LocalDateTime.of(2017, 2, 13, 15, 56),
                "Wakanda Flights", 7);
    }

    @Test
    void getDestination() {
        assertEquals(flight.getDestination(), "Paris");
    }

    @Test
    void setDestination() {
        flight.setDestination("San Francisco");
        assertEquals(flight.getDestination(), "San Francisco");
    }

    @Test
    void getDateTime() {
        assertEquals(flight.getDateTime(), LocalDateTime.of(2017, 2, 13, 15, 56) );
    }

    @Test
    void setDateTime() {
        flight.setDateTime(LocalDateTime.of(2022, 2, 22, 22, 22));
        assertEquals(flight.getDateTime(), LocalDateTime.of(2022, 2, 22, 22, 22) );
    }

    @Test
    void getAirport() {
        assertEquals(flight.getAirport(), "Wakanda Flights");
    }

    @Test
    void setAirport() {
        flight.setAirport("Uganda Flights");
        assertEquals(flight.getAirport(), "Uganda Flights");
    }

    @Test
    void getNumberOfTickets() {
        assertEquals(flight.getNumberOfTickets(), 7);
    }

    @Test
    void setNumberOfTickets() {
        flight.setNumberOfTickets(98);
        assertEquals(flight.getNumberOfTickets(), 98);
    }

    @Test
    void getID() {
        assertEquals(flight.getID(),1 );
    }

    @Test
    void setID() {
        flight.setID(27);
        assertEquals(flight.getID(), 27);
    }

    @Test
    void testToString() {
        assertEquals(flight.toString(), "Flight{" +
                "ID=" + 1 +
                ", destination='" + "Paris" + '\'' +
                ", dateTime=" + "13-02-2017 15:56" +
                ", airport='" + "Wakanda Flights" + '\'' +
                ", numberOfTickets=" + 7 +
                '}');
    }
}