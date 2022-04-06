import network.utils.AbstractServer;
import network.utils.RpcConcurrentServer;
import network.utils.ServerException;
import repository.AdminRepo;
import repository.ClientRepo;
import repository.FlightRepo;
import repository.ReservationRepo;
import server.ServicesImpl;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        AdminRepo adminRepo=new AdminRepo(serverProps);
        FlightRepo flightRepo=new FlightRepo(serverProps);
        ClientRepo clientRepo=new ClientRepo(serverProps);
        ReservationRepo reservationRepo=new ReservationRepo(serverProps);
        IServices serverImpl=new ServicesImpl(adminRepo, flightRepo, clientRepo, reservationRepo);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new RpcConcurrentServer(chatServerPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
