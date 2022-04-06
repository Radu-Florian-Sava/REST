package repository;

import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientRepo extends IClientRepo {

    public ClientRepo(Properties properties) {
        super(properties);
    }

    public Client findByName(String name) {
        logger.traceEntry("looking for client with name {}", name);
        Client client = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select id, address from Clients where name=?")) {
            preStm.setString(1, name);
            ResultSet result = preStm.executeQuery();
            result.next();
            int id = result.getInt("id");
            String address = result.getString("address");
            client = new Client(id, name, address);
            result.close();
            logger.trace("Found {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD" + ex);
        }
        logger.traceExit();
        return client;
    }

    public List<String> getAllClientNames() {
        logger.traceEntry("looking for client names");
        List<String> names = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select name from Clients")) {
            ResultSet result = preStm.executeQuery();
            while(result.next())
            {
                names.add(result.getString(1));
            }
            result.close();
            logger.trace("Found {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD" + ex);
        }
        logger.traceExit();
        return names;
    }
}