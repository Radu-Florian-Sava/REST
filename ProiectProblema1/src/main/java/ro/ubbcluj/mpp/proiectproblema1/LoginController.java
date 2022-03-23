package ro.ubbcluj.mpp.proiectproblema1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubbcluj.mpp.proiectproblema1.repository.AdminRepo;
import ro.ubbcluj.mpp.proiectproblema1.service.AdminService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class LoginController {
    private final AdminService adminService;
    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Button loginButton;

    public LoginController() {
        Properties props = new Properties();
        props.setProperty("foreign_keys", "on");
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        adminService = new AdminService(new AdminRepo(props));
    }

    public void loginUser(ActionEvent actionEvent) {
        if (!Objects.equals(adminService.login(usernameTextField.getText()).getPassword(), passwordTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Parola incorecta!");
            alert.show();
        } else {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                Scene scene = new Scene(parent);
                Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nu se poate accesa aplicatia");
                alert.show();
            }

        }
    }
}
