package start;


import model.Flight;
import org.springframework.web.client.RestClientException;
import rest.client.FlightsClient;
import services.rest.ServiceException;

import java.time.LocalDateTime;

public class StartRestClient {
    private final static FlightsClient flightsClient=new FlightsClient();
    public static void main(String[] args) {
        Flight flightT=new Flight(9999, "Wakanda", LocalDateTime.now(), "WAK Flights", 10);
        //{"destination":"San Floresti","airport":"Valgrind","numberOfTickets":17,"time":"19:36:38","id":70,"date":"2022-03-15"}
        try{
            Flight finalFlightT = flightsClient.create(flightT);
            show(()-> System.out.println(finalFlightT));
            show(()->{
                Flight[] res=flightsClient.getAll();
                for(Flight flight:res){
                    System.out.println(flight.getID()+ ": "+flight.getDestination()+ " , " + flight.getAirport());
                }
            });
            show(()->flightsClient.delete(finalFlightT.getID().toString()));
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }
}

