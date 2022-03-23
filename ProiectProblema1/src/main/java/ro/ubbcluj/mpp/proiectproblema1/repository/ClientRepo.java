package ro.ubbcluj.mpp.proiectproblema1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.ubbcluj.mpp.proiectproblema1.model.Client;
import ro.ubbcluj.mpp.proiectproblema1.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientRepo implements IClientRepo {

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public ClientRepo(Properties properties) {
        logger.info("Initializing ClientRepo with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Client elem) {
        logger.traceEntry("saving client {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("insert into Clients(name, address) values (?,?)" ) ){

            preStm.setString(1,elem.getName());
            preStm.setString(2,elem.getAddress());
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting client with id {}", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("delete from Clients where id=?" ) ){

            preStm.setInt(1, id);
            int result = preStm.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Client elem, Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("UPDATE Clients SET name=?, address=? WHERE id=?");
            statement.setString(1,elem.getName());
            statement.setString(2,elem.getAddress());
            statement.setInt(3,id);
            Client updated = findById(id);
            int result = statement.executeUpdate();
            logger.trace("Updated {} instances",result);
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Client findById(Integer id) {
        logger.traceEntry("looking for client with id {}", id);
        Client client = null;
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("select name, address from Clients where id=?" ) ){
            preStm.setInt(1, id);
            ResultSet result = preStm.executeQuery();
            result.next();
            String name = result.getString("name");
            String address = result.getString("address");
            client = new Client(id, name, address);
            result.close();
            logger.trace("Found {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error BD"+ex);
        }
        logger.traceExit();
        return client;
    }

    @Override
    public List<Client> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Client> clients = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("SELECT * FROM Clients");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String address = result.getString("address");
                Client client = new Client(id, name, address);
                clients.add(client);
            }
            result.close();
        }
        catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit(clients);
        return clients;
    }
}
