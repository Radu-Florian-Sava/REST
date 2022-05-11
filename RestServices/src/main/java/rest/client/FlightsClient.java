package rest.client;

import model.Flight;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import services.rest.ServiceException;

import java.util.concurrent.Callable;

public class FlightsClient {
    public static final String URL = "http://localhost:8080/project/flights";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Flight[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Flight[].class));
    }

    public Flight getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Flight.class));
    }

    public Flight create(Flight flight) {
        return execute(() -> restTemplate.postForObject(URL, flight, Flight.class));
    }

    public void update(Flight flight) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, flight.getID()), flight);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}

