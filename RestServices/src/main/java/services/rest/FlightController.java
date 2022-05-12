package services.rest;

import model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.FlightRepo;

import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/project/flights")
public class FlightController {

    @Autowired
    private FlightRepo flightRepo;


    @RequestMapping(method = RequestMethod.GET)
    public List<Flight> getAll() {
        System.out.println("Get all flights ...");
        return flightRepo.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);
        Flight flight = flightRepo.findById(id);
        if (flight == null)
            return new ResponseEntity<String>("Flight not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Flight>(flight, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Flight create(@RequestBody Flight flight) {
        flightRepo.add(flight);
        return flight;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flight update(@PathVariable Integer id,@RequestBody Flight flight) {
        System.out.println("Updating flight ...");
        Flight updated = flightRepo.update(flight, id);
        return updated;
    }

    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) {
        System.out.println("Deleting flight with id ... " + id);
        flightRepo.delete(id);
    }
}
