package protobuf;

import model.Admin;
import model.Client;
import model.Flight;
import services.IObserver;
import services.IServices;
import services.ProjectException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoClientService implements IServices {
    private String host;
    private int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<Proto.Answer> answers;
    private volatile boolean finished;

    public ProtoClientService(String host, int port) {
        this.host = host;
        this.port = port;
        answers = new LinkedBlockingQueue<Proto.Answer>();
    }

    private void sendRequest(Proto.Request request) throws ProjectException {
        try {
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Sent request.");
        }
        catch (Exception e) {
            throw new ProjectException("Error sending object "+e);
        }
    }

    private void initializeConnection() throws ProjectException {
        try {
            connection = new Socket(host,port);
            output = connection.getOutputStream();
            input = connection.getInputStream();
            finished=false;
            startReader();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private Proto.Answer readAnswer() {
        Proto.Answer answer = null;
        try {
            answer = answers.take();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate (Proto.Answer answer) throws ProjectException {
        if(answer.getType() ==Proto.Answer.Type.FlightsChanged){
            client.flightsChanged(ProtoUtils.getFlightFromAnswer(answer));
        }
    }

    private boolean isUpdate (Proto.Answer answer) {
        return answer.getType()==Proto.Answer.Type.FlightsChanged;
    }

    @Override
    public void login(Admin admin, IObserver client) throws ProjectException {
        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(admin));
        Proto.Answer answer = readAnswer();
        if (answer.getType()==Proto.Answer.Type.OK) {
            this.client=client;
            return;
        }
        if (answer.getType()==Proto.Answer.Type.Error) {
            String error = answer.getError();
            closeConnection();
            throw new ProjectException(error);
        }
    }

    @Override
    public void logout(Admin admin, IObserver client) throws ProjectException {
        sendRequest(ProtoUtils.createLogoutRequest(admin));
        Proto.Answer answer = readAnswer();
        closeConnection();
        if (answer.getType()==Proto.Answer.Type.Error) {
            String error = answer.getError();
            throw new ProjectException(error);
        }
    }

    @Override
    public List<Flight> getAllAvailableFlights() throws ProjectException {
        sendRequest(ProtoUtils.createGetAllAvailableFlightsRequest());
        Proto.Answer answer = readAnswer();
        if (answer.getType()==Proto.Answer.Type.OK) {
            return ProtoUtils.getFlightsListFromAnswer(answer);
        }
        else {
            String error = answer.getError();
            throw new ProjectException(error);
        }
    }

    @Override
    public List<Flight> searchByDateAndDestination(String destination, LocalDate date) throws ProjectException {
        sendRequest(ProtoUtils.createSearchFlightRequest(destination,date));
        Proto.Answer answer = readAnswer();
        if (answer.getType()==Proto.Answer.Type.OK) {
            return ProtoUtils.getFlightsListFromAnswer(answer);
        }
        else {
            String error = answer.getError();
            throw new ProjectException(error);
        }
    }

    @Override
    public List<String> getAllClientNames() throws ProjectException {
        sendRequest(ProtoUtils.createGetClientNames());
        Proto.Answer answer = readAnswer();
        if (answer.getType()==Proto.Answer.Type.OK) {
            return ProtoUtils.getClientNamesFromAnswer(answer);
        }
        else {
            String error = answer.getError();
            throw new ProjectException(error);
        }
    }

    @Override
    public Client findClientByName(String name) throws ProjectException {
        sendRequest(ProtoUtils.createGetClientRequest(name));
        Proto.Answer answer = readAnswer();
        if (answer.getType()==Proto.Answer.Type.OK) {
            return ProtoUtils.getClientFromAnswer(answer);
        }
        else {
            String error = answer.getError();
            throw new ProjectException(error);
        }
    }

    @Override
    public void addReservation(String clientName, Flight flight, int numberOfSeats, List<String> clientNames) throws ProjectException {
        sendRequest(ProtoUtils.createAddReservationRequest(clientName, flight, numberOfSeats, clientNames));
        Proto.Answer answer = readAnswer();
        if (answer.getType()==Proto.Answer.Type.OK) {
            System.out.println("Adding reservation OK");
        }
        else {
            String error = answer.getError();
            throw new ProjectException(error);
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while(!finished) {
                try {
                    Proto.Answer answer = Proto.Answer.parseDelimitedFrom(input);
                    System.out.println("Answer received "+answer);
                    if (isUpdate(answer)) { // IF RECEIVING UNREQUESTED ANSWER FROM SERVER
                        handleUpdate(answer);   // HANDLE IT LMAO
                    }
                    else {  // IF WAITING FOR AN ANSWER FROM THE SERVER
                        try {
                            answers.put(answer);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Error at reader "+e);
                }
            }
        }
    }
}
