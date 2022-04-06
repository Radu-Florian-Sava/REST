package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.Admin;
import model.Flight;
import services.IObserver;
import services.IServices;
import services.ProjectException;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WindowController implements Initializable, IObserver {
    private IServices server;
    private Admin admin;

    public void setServer(IServices s) {
        server = s;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("END INIT!!!!!!!!!");
    }

    @Override
    public void flightsChanged(List<Flight> flights) throws ProjectException {

    }

    public void logoutBackend() {
        try {
            server.logout(admin, this);
        } catch (ProjectException e) {
            System.out.println("Logout error " + e);
        }
    }

    public void logout(ActionEvent actionEvent) {
        logoutBackend();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void setUser(Admin admin) {
        this.admin = admin;

    }

    public void findFlights(ActionEvent actionEvent) {

    }

    public void selectFlight(MouseEvent actionEvent) {

    }

    public void selectedClient(ActionEvent actionEvent) {

    }

    public void addTourist(ActionEvent actionEvent) {

    }

    public void changedValue(MouseEvent actionEvent) {

    }

    public void buyTicket(ActionEvent actionEvent) {

    }

    public void addToSearchedFlights(MouseEvent mouseEvent) {

    }
}
