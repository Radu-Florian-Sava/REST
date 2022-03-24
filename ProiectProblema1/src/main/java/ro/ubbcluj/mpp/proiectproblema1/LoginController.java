package ro.ubbcluj.mpp.proiectproblema1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubbcluj.mpp.proiectproblema1.control.Controller;
import ro.ubbcluj.mpp.proiectproblema1.model.Admin;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    private Controller controller;

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;


    public LoginController(Controller controller) {
        this.controller = controller;
    }

    public void loginUser(ActionEvent actionEvent) {
        Admin user = controller.login(usernameTextField.getText());
        if (user == null || !Objects.equals(user.getPassword(), passwordField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la autentificare!");
            alert.setContentText("Datele de autentificare nu sunt corecte, verificati parola");
            alert.show();
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainWindow.fxml"));
                fxmlLoader.setController(new MainController(controller));
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                appStage.close();
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
