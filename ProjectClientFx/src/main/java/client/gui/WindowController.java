package client.gui;

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
        System.out.println("END INIT!!!!!!!!!");
            destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
            numberOfTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
            departureDateColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getDate().toString()));
            departureTimeColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getTime().toString()));
            searchNumberOfTickets.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
            searchHour.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getTime().toString()));
            addTouristButton.setDisable(true);
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
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void setUser(Admin admin) {
        this.admin = admin;

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
        Integer numberOfTickets = clickedFlight.getNumberOfTickets();
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
            Alert incorrectNumberOfSeats = new Alert(Alert.AlertType.ERROR);
            incorrectNumberOfSeats.setTitle("Numarul de turisti");
            incorrectNumberOfSeats.setContentText("Numarul de turisti nu se potriveste cu numarul din lista numelor");
            incorrectNumberOfSeats.show();
            return;
        }
        String clientName = clientComboBox.getValue();
        if (clientName == null) {
            Alert noClient = new Alert(Alert.AlertType.ERROR);
            noClient.setTitle("Numele Clientului");
            noClient.setContentText("Clientul nu este selectat");
            noClient.show();
            return;
        }
        Flight flight = searchTable.getSelectionModel().getSelectedItem();
        if (flight == null) {
            Alert noFlight = new Alert(Alert.AlertType.ERROR);
            noFlight.setTitle("Zborul");
            noFlight.setContentText("Zborul nu este selectat");
            noFlight.show();
            return;
        }
        searchTable.getItems().remove(flight);
        try {
            server.addReservation(clientName, flight, numberOfSeats, touristNames);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        if(flight.getNumberOfTickets() != numberOfSeats){
            flight.setNumberOfTickets(flight.getNumberOfTickets()-numberOfSeats);
            searchTable.getItems().add(flight);
        }
        Alert noFlight = new Alert(Alert.AlertType.INFORMATION);
        noFlight.setTitle("Succes!");
        noFlight.setContentText("Rezervarea pentru " + clientName + " a fost facuta cu succes");
        noFlight.show();
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
