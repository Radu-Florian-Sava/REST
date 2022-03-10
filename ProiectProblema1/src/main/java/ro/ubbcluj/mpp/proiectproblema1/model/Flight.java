package ro.ubbcluj.mpp.proiectproblema1.model;

import java.time.LocalDateTime;

public class Flight implements Identifiable<Integer> {

    private int ID;
    private String destination;
    private LocalDateTime dateTime;
    private String airport;
    private int numberOfTickets;

    public Flight() {
        this.ID = 0;
        this.destination = "";
        this.dateTime = LocalDateTime.now();
        this.airport = "";
        this.numberOfTickets = 0;
    }

    public Flight(int ID, String destination, LocalDateTime dateTime, String airport, int numberOfTickets) {
        this.ID = ID;
        this.destination = destination;
        this.dateTime = dateTime;
        this.airport = airport;
        this.numberOfTickets = numberOfTickets;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer id) {
        this.ID = id;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "ID=" + ID +
                ", destination='" + destination + '\'' +
                ", dateTime=" + dateTime +
                ", airport='" + airport + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }
}
