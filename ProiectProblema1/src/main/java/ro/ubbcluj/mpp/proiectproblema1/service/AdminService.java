package ro.ubbcluj.mpp.proiectproblema1.service;

import ro.ubbcluj.mpp.proiectproblema1.model.Admin;
import ro.ubbcluj.mpp.proiectproblema1.repository.AdminRepo;

public class AdminService {
    AdminRepo adminRepo;
    public AdminService(AdminRepo adminRepo){
        this.adminRepo = adminRepo;
    }

    public Admin login(String username){
        return adminRepo.findByUsername(username);
    }
}
