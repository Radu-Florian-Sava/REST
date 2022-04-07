package client.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Admin;
import model.Flight;
import services.IObserver;
import services.IServices;
import services.ProjectException;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WindowController implements Initializable, IObserver {
    private IServices server;
    private Admin admin;

    @FXML
    private TableView<Flight> flightTable;
    @FXML
    private TableColumn<Flight, String> destinationColumn;

    @FXML
    private TableColumn<Flight, String> departureDateColumn;

    @FXML
    private TableColumn<Flight, String> departureTimeColumn;

    @FXML
    private TableColumn<Flight, String> numberOfTicketsColumn;

    @FXML
    private TextField destinationSearchBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Flight> searchTable;

    @FXML
    private TableColumn<Flight, String> searchHour;

    @FXML
    private TableColumn<Flight, String> searchNumberOfTickets;

    @FXML
    private TextField destinationTextField;

    @FXML
    private TextField airportTextField;

    @FXML
    private TextField dateTimeTextField;

    @FXML
    private TextField numberOfTicketsTextField;

    @FXML
    private ComboBox<String> clientComboBox;

    @FXML
    private Spinner<Integer> noSeatsSpinner;

    @FXML
    private TextField clientAddressTextField;

    @FXML
    private ListView<String> touristNamesList;

    @FXML
    private Button addTouristButton;

    @FXML
    private TextField touristNameTextField;

    public void setServer(IServices s) {
        server = s;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
            numberOfTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
            departureDateColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getDate().toString()));
            departureTimeColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getTime().toString()));
            searchNumberOfTickets.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
            searchHour.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getTime().toString()));
            addTouristButton.setDisable(true);
            noSeatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1));
            System.out.println("END INIT!!!!!!!!!");

    }

    @Override
    public void flightsChanged(Flight flight) throws ProjectException {
        Platform.runLater(()->{
            if (flight==null){
                Util.showWarning("Zboruri", "Zbor inexistent");
                return;
            }
            flightTable.getItems().remove(flight);
            searchTable.getItems().remove(flight);
            if(flight.getNumberOfTickets() > 0){
                flightTable.getItems().add(flight);
                flightTable.refresh();
            }
        });
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
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void setUser(Admin admin) {
        this.admin = admin;
        System.out.println("Logged in as "+ admin.getUsername());
    }

    public void findFlights(ActionEvent actionEvent) {
        String destination = destinationSearchBox.getText();
        LocalDate date = datePicker.getValue();
        List<Flight> flights = null;
        try {
            flights = server.searchByDateAndDestination(destination, date);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        for(Flight x: searchTable.getItems()){
            flightTable.getItems().add(x);
        }
        searchTable.getItems().clear();
        for(Flight x: flights){
            searchTable.getItems().add(x);
        }
        for(Flight x: flights){
            flightTable.getItems().remove(x);
        }
    }

    public void selectFlight(MouseEvent actionEvent) {
        Flight clickedFlight = searchTable.getSelectionModel().getSelectedItem();
        if (clickedFlight == null) {
            return;
        }
        destinationTextField.setText(clickedFlight.getDestination());
        airportTextField.setText(clickedFlight.getAirport());
        dateTimeTextField.setText(clickedFlight.getDateTime().toString());
        int numberOfTickets = clickedFlight.getNumberOfTickets();
        numberOfTicketsTextField.setText(Integer.toString(numberOfTickets));
        noSeatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, numberOfTickets, 1));
    }

    public void selectedClient(ActionEvent actionEvent) {
        String clientName = clientComboBox.getSelectionModel().getSelectedItem();
        if (clientName == null) {
            return;
        }
        try {
            clientAddressTextField.setText(server.findClientByName(clientName).getAddress());
        }
        catch (ProjectException e){
            e.printStackTrace();
        }
        addTouristButton.setDisable(false);
        touristNamesList.getItems().clear();
        touristNameTextField.setText(clientName);
    }

    public void addTourist(ActionEvent actionEvent) {
        String touristName = touristNameTextField.getText();
        if (touristName == null || touristName == "") {
            return;
        }
        touristNamesList.getItems().add(0, touristName);
        if (touristNamesList.getItems().size() >= noSeatsSpinner.getValue()) {
            addTouristButton.setDisable(true);
        }
    }

    public void changedValue(MouseEvent actionEvent) {
        Integer currentSpinnerValue = noSeatsSpinner.getValue();
        Integer numberOfTourists = touristNamesList.getItems().size();
        if (currentSpinnerValue > numberOfTourists) {
            addTouristButton.setDisable(false);
        } else if (currentSpinnerValue < numberOfTourists) {
            addTouristButton.setDisable(true);
            touristNamesList.getItems().clear();
        }
    }

    public void buyTicket(ActionEvent actionEvent) {
        Integer numberOfSeats = noSeatsSpinner.getValue();
        List<String> touristNames = new ArrayList<>(touristNamesList.getItems());
        if (numberOfSeats != touristNames.size()) {
            Util.showWarning("Numarul de turisti", "Numarul de turisti nu se potriveste cu numarul din lista numelor");
            return;
        }
        String clientName = clientComboBox.getValue();
        if (clientName == null) {
            Util.showWarning("Numele Clientului","Clientul nu este selectat" );
            return;
        }
        Flight flight = searchTable.getSelectionModel().getSelectedItem();
        if (flight == null) {
            Util.showWarning("Zborul", "Zborul nu este selectat");
            return;
        }
        searchTable.getItems().clear();
        try {
            server.addReservation(clientName, flight, numberOfSeats, touristNames);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        Util.showWarning("Succes!", "Rezervarea pentru client a fost facuta cu succes");
    }

    public void addToSearchedFlights(MouseEvent mouseEvent){
        Flight flight = flightTable.getSelectionModel().getSelectedItem();
        if(flight == null){
            return;
        }
        searchTable.getItems().add(0, flight);
        flightTable.getItems().remove(flight);
    }

    public void loadFlights() {
        try {
            List<Flight> flights = server.getAllAvailableFlights();
            flightTable.getItems().clear();
            for (Flight flight : flights) {
                flightTable.getItems().add(flight);
            }
        } catch (ProjectException e) {
            e.printStackTrace();
        }
    }

    public void loadClientNames(){
        try{
            List<String> clients  =server.getAllClientNames();
            clientComboBox.getItems().clear();
            for(String name : clients){
                clientComboBox.getItems().add(name);
            }
        }
        catch(ProjectException e){
            e.printStackTrace();
        }
    }
}
