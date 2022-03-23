package ro.ubbcluj.mpp.proiectproblema1.service;

import ro.ubbcluj.mpp.proiectproblema1.model.Client;
import ro.ubbcluj.mpp.proiectproblema1.repository.ClientRepo;

import java.util.List;

public class ClientService {
    ClientRepo clientRepo;

    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public List<Client> getAll() {
        return clientRepo.getAll();
    }

    public Client findByName(String name){
        return clientRepo.findByName(name);
    }


}
