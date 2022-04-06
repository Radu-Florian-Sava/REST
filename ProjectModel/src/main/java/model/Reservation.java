package model;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;

public class Reservation implements Identifiable<Pair<Flight, Client>>, Serializable {
    private Flight flight;
    private Client client;
    private List<String> touristNames;
    private int numberOfSeats;

    public Reservation() {
        this.flight = null;
        this.client = null;
        this.touristNames = null;
        this.numberOfSeats = 0;
    }

    public Reservation(Flight flight, Client client, List<String> touristNames, int numberOfSeats) {
        this.flight = flight;
        this.client = client;
        this.touristNames = touristNames;
        this.numberOfSeats = numberOfSeats;
    }

    public List<String> getTouristNames() {
        return touristNames;
    }

    public void setTouristNames(List<String> touristNames) {
        this.touristNames = touristNames;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public Pair<Flight, Client> getID() {
        return new Pair<>(flight, client);
    }

    @Override
    public void setID(Pair<Flight, Client> id) {
        this.flight = id.getKey();
        this.client = id.getValue();
    }
}