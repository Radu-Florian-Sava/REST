package services;

import model.Flight;

public interface IObserver {
    void flightsChanged(Flight flight) throws ProjectException;
}
