package ro.ubbcluj.mpp.proiectproblema1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.ubbcluj.mpp.proiectproblema1.repository.ClientRepo;
import ro.ubbcluj.mpp.proiectproblema1.repository.FlightRepo;
import ro.ubbcluj.mpp.proiectproblema1.repository.ReservationRepo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Properties props=new Properties();
        props.setProperty("foreign_keys", "on");
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        ClientRepo clientRepo = new ClientRepo(props);
        FlightRepo flightRepo = new FlightRepo(props);
        ReservationRepo reservationRepo = new ReservationRepo(props, flightRepo, clientRepo);
        launch();
    }
}