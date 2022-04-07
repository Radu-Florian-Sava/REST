package network.utils;


import network.rpc.ClientRpcReflectionWorker;
import services.IServices;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private IServices airportServer;
    public RpcConcurrentServer(int port, IServices airportServer) {
        super(port);
        this.airportServer = airportServer;
        System.out.println("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(airportServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
