package repository;

import model.Admin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IAdminRepo implements Repository<Admin, Integer> {

    protected static final Logger logger = LogManager.getLogger();
    protected JdbcUtils dbUtils;

    public IAdminRepo(Properties properties) {
        logger.info("Initializing AdminRepo with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Admin elem) {
        logger.traceEntry("saving admin {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("insert into Admins(username, password) values (?,?)" ) ){

            preStm.setString(1,elem.getUsername());
            preStm.setString(2,elem.getPassword());
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
        logger.traceEntry("deleting admin with id {}", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("delete from Admins where id=?" ) ){

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
    public Admin update(Admin elem, Integer id) {
        return null;
    }

    @Override
    public Admin findById(Integer id) {
        logger.traceEntry("looking for admin with id {}", id);
        Admin admin = null;
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStm=con.prepareStatement("select username, password from Admins where id=?" ) ){
            preStm.setInt(1, id);
            ResultSet result = preStm.executeQuery();
            result.next();
            String username = result.getString("username");
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

    @Override
    public List<Admin> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Admin> admins = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement
                    ("SELECT * FROM Admins");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String password = result.getString("password");
                Admin admin = new Admin(id, username, password);
                admins.add(admin);
            }
            result.close();
        }
        catch (SQLException ex) {
            logger.error(ex);
        }
        logger.traceExit(admins);
        return admins;
    }
}