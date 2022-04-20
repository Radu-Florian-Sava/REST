package protobuf;

import model.Admin;
import model.Client;
import model.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {

    public static Proto.Request createGetAllAvailableFlightsRequest() {
        return Proto.Request.newBuilder().setType(Proto.Request.Type.GetFlights).build();
    }

    public static Proto.Request createSearchFlightRequest(String destination, LocalDate date) {
        return Proto.Request.newBuilder().setType(Proto.Request.Type.SearchFlights)
                .setDestination(destination).setDate(date.toString()).build();
    }

    public static Proto.Request createGetClientNames() {
        return Proto.Request.newBuilder().setType(Proto.Request.Type.GetClientNames).build();

    }

    public static List<Flight> getFlightsListFromAnswer(Proto.Answer answer) {
        List<Flight> flights = new ArrayList<>();
        for (Proto.Flight flight : answer.getFlightsList()) {
            LocalDateTime date = LocalDateTime.parse(flight.getDateTime());
            Flight domainFlight = new Flight(flight.getId(), flight.getDestination(), date,
                    flight.getAirport(), flight.getNumberOfTickets());
            flights.add(domainFlight);
        }
        return flights;
    }

    public static List<String> getClientNamesFromAnswer(Proto.Answer answer) {
        return new ArrayList<>(answer.getClientNamesList());
    }

    public static Client getClientFromAnswer(Proto.Answer answer) {
        return new Client(answer.getClient().getId(), answer.getClient().getName(),
                answer.getClient().getAddress());
    }

    public static Proto.Request createLoginRequest(Admin admin) {
        Proto.Admin protoAdmin = Proto.Admin.newBuilder()
                .setUsername(admin.getUsername()).setPassword(admin.getPassword()).setId(admin.getID()).build();
        return Proto.Request.newBuilder().setType(Proto.Request.Type.Login)
                .setAdmin(protoAdmin).build();
    }

    public static Proto.Request createLogoutRequest(Admin admin) {
        Proto.Admin protoAdmin = Proto.Admin.newBuilder()
                .setUsername(admin.getUsername()).setPassword(admin.getPassword()).setId(admin.getID()).build();
        return Proto.Request.newBuilder().setType(Proto.Request.Type.Logout)
                .setAdmin(protoAdmin).build();
    }

    public static Proto.Request createGetClientRequest(String name) {
        return Proto.Request.newBuilder().setType(Proto.Request.Type.GetClient)
                .setClientName(name).build();
    }

    public static Flight getFlightFromAnswer(Proto.Answer answer) {
        Proto.Flight flight = answer.getFlight();
        LocalDateTime date = LocalDateTime.parse(flight.getDateTime());
        return new Flight(flight.getId(), flight.getDestination(), date,
                flight.getAirport(), flight.getNumberOfTickets());
    }

    public static Proto.Request createAddReservationRequest(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) {
        Proto.Flight protoFlight = Proto.Flight.newBuilder()
                .setId(flight.getID()).setAirport(flight.getAirport()).setDestination(flight.getDestination())
                .setDateTime(flight.getDateTime().toString()).setNumberOfTickets(flight.getNumberOfTickets()).build();
        Proto.Request request = Proto.Request.newBuilder().setType(Proto.Request.Type.BuyTicket).setFlight(protoFlight)
                .setClientName(clientName).setNumberOfSeats(numberOfSeats).addAllClientNames(clientNames).build();
        return  request;
    }
}
