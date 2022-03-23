package ro.ubbcluj.mpp.proiectproblema1.repository;

import ro.ubbcluj.mpp.proiectproblema1.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AdminRepo extends IAdminRepo {

    public AdminRepo(Properties properties) {
        super(properties);
    }

    public Admin findByUsername(String username) {
        logger.traceEntry("looking for admin with username {}", username);
        Admin admin = null;
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("select id,password from Admins where username=?" ) ){
            preStm.setString(1, username);
            ResultSet result = preStm.executeQuery();
            result.next();
            int id = result.getInt("id");
            String password = result.getString("password");
            admin = new Admin(id, username, password);
            result.close();
            logger.trace("Found {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
        return admin;
    }


}