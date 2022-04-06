package network.rpc;

import model.Admin;
import model.Client;
import model.Flight;
import services.IObserver;
import services.IServices;
import services.ProjectException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements IServices {
    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(Admin admin, IObserver client) throws ProjectException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(admin).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new ProjectException(err);
        }
    }

    @Override
    public void logout(Admin admin, IObserver client) throws ProjectException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(admin).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ProjectException(err);
        }
    }

    @Override
    public List<Flight> getAllAvailableFlights() throws ProjectException {
        Request req = new Request.Builder().type(RequestType.GET_FLIGHTS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ProjectException(err);
        }
        List<Flight> flights = (List<Flight>) response.data();
        return flights;
    }

    @Override
    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) throws ProjectException {
        Request req = new Request.Builder().type(RequestType.SEARCH_FLIGHTS).data(Arrays.asList(destination, date)).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ProjectException(err);
        }
        List<Flight> flights = (List<Flight>) response.data();
        return flights;
    }

    @Override
    public List<String> getAllClientNames() throws ProjectException {
        Request req = new Request.Builder().type(RequestType.GET_CLIENT_NAMES).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ProjectException(err);
        }
        List<String> clients = (List<String>) response.data();
        return clients;
    }

    @Override
    public Client findClientByName(String name) throws ProjectException {
        Request req = new Request.Builder().type(RequestType.GET_CLIENT).data(name).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ProjectException(err);
        }
        Client client = (Client) response.data();
        return client;
    }

    @Override
    public void addReservation(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) throws ProjectException {

    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws ProjectException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ProjectException("Error sending object " + e);
        }

    }

    private Response readResponse() throws ProjectException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ProjectException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.GET_FLIGHTS) {
            List<Flight> flights = (List<Flight>) response.data();
            System.out.println("Flights update");
            try {
                client.flightsChanged(flights);
            } catch (ProjectException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.GET_FLIGHTS;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
