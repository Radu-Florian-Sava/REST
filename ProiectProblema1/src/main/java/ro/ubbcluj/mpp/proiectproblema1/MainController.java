package ro.ubbcluj.mpp.proiectproblema1;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ro.ubbcluj.mpp.proiectproblema1.control.Controller;
import ro.ubbcluj.mpp.proiectproblema1.model.Flight;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private Controller controller;

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

    public MainController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        numberOfTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
        departureDateColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getDateTime()
                .toLocalDate().toString()));
        departureTimeColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getDateTime()
                .toLocalTime().toString()));
        List<Flight> availableFlights = controller.getAllAvailableFlights();
        flightTable.setItems(FXCollections.observableArrayList(availableFlights));
        searchNumberOfTickets.setCellValueFactory(new PropertyValueFactory<>("numberOfTickets"));
        searchHour.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getDateTime()
                .toLocalTime().toString()));
        addTouristButton.setDisable(true);
    }

    public void findFlights(ActionEvent actionEvent) {
        String destination = destinationSearchBox.getText();
        LocalDate date = datePicker.getValue();
        List<Flight> flights = controller.searchByDateAndDestination(destination, date);
        searchTable.setItems(FXCollections.observableArrayList(flights));
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
        clientComboBox.setItems(FXCollections.observableArrayList(controller.getAllClientNames()));
    }

    public void selectedClient(ActionEvent actionEvent) {
        String clientName = clientComboBox.getSelectionModel().getSelectedItem();
        if (clientName == null) {
            return;
        }
        addTouristButton.setDisable(false);
        clientAddressTextField.setText(controller.findClientByName(clientName).getAddress());
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
        if(flight.getNumberOfTickets() == numberOfSeats){
            flightTable.getItems().remove(flight);
        }
        controller.addReservation(clientName, flight, numberOfSeats, touristNames);
        Alert noFlight = new Alert(Alert.AlertType.INFORMATION);
        noFlight.setTitle("Succes!");
        noFlight.setContentText("Rezervarea pentru " + clientName + " a fost facuta cu succes");
        noFlight.show();
    }

    public void logout(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginWindow.fxml"));
            fxmlLoader.setController(new LoginController(controller));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            appStage.close();
            appStage.setScene(scene);
            appStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu se poate realiza delogarea");
            alert.show();
        }
    }

    public void addToSearchedFlights(MouseEvent mouseEvent){
        Flight flight = flightTable.getSelectionModel().getSelectedItem();
        if(flight == null){
            return;
        }
        searchTable.getItems().add(0, flight);
        flightTable.getItems().remove(flight);
    }
}
