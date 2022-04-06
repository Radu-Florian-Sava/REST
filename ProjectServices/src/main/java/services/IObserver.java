package services;

import model.Flight;

import java.util.List;

public interface IObserver {
    void flightsChanged(List<Flight> flights) throws ProjectException;
}
