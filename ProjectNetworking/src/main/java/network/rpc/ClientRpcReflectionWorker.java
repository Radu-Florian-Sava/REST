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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;


public class ClientRpcReflectionWorker implements Runnable, IObserver {
    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    private IServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response handleLOGIN(Request request) {
        System.out.println("Login request ..." + request.type());
        Admin admin = (Admin) request.data();
        try {
            server.login(admin, this);
            return okResponse;
        } catch (ProjectException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        System.out.println("Logout request...");
        Admin admin = (Admin) request.data();
        try {
            server.logout(admin, this);
            connected = false;
            return okResponse;

        } catch (ProjectException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_FLIGHTS(Request request){
        System.out.println("GetFlights Request ...");
        try {
           List<Flight> flights=server.getAllAvailableFlights();
           return new Response.Builder().type(ResponseType.OK).data(flights).build();
        } catch ( ProjectException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSEARCH_FLIGHTS(Request request){
        System.out.println("GetSearchFlights Request ...");
        String destination = (String)((List<Object>)request.data()).get(0);
        LocalDate date= (LocalDate) ((List<Object>)request.data()).get(1);
        try {
            List<Flight> flights=server.searchByDateAndDestination(destination, date);
            return new Response.Builder().type(ResponseType.OK).data(flights).build();
        } catch ( ProjectException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CLIENT(Request request){
        System.out.println("GetClient Request ...");
        try {
            Client client=server.findClientByName((String)request.data());
            return new Response.Builder().type(ResponseType.OK).data(client).build();
        } catch ( ProjectException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CLIENT_NAMES(Request request){
        System.out.println("GetClientNames Request ...");
        try {
            List<String> clients=server.getAllClientNames();
            return new Response.Builder().type(ResponseType.OK).data(clients).build();
        } catch ( ProjectException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void flightsChanged(List<Flight> flights) throws ProjectException {
        Response resp=new Response.Builder().type(ResponseType.GET_FLIGHTS).data(flights).build();
        System.out.println("Flights list changed");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
