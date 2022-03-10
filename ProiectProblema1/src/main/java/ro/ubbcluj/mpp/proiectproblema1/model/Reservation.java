package ro.ubbcluj.mpp.proiectproblema1.model;

import javafx.util.Pair;

import java.util.List;

public class Reservation implements Identifiable<Pair<Integer,Integer>>{
    private int flightID;
    private int clientID;
    private List<String> touristNames;
    private int numberOfSeats;

    public Reservation() {
        this.flightID = 0;
        this.clientID = 0;
        this.touristNames = null;
        this.numberOfSeats = 0;
    }

    public Reservation(int flightID, int clientID, List<String> touristNames, int numberOfSeats) {
        this.flightID = flightID;
        this.clientID = clientID;
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
    public Pair<Integer, Integer> getID() {
        return new Pair<>(flightID,clientID);
    }

    @Override
    public void setID(Pair<Integer, Integer> id) {
            this.flightID = id.getKey();
            this.clientID = id.getValue();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "flightID=" + flightID +
                ", clientID=" + clientID +
                ", touristNames=" + touristNames +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
