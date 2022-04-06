package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Admin;
import services.IServices;
import services.ProjectException;

public class LoginController {

    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    Parent mainWindowParent;
    private IServices server;
    private WindowController windowCtrl;
    private Admin crtUser;

    public void setServer(IServices s) {
        server = s;
    }


    public void setParent(Parent p) {
        mainWindowParent = p;
    }

    public void loginUser(ActionEvent actionEvent) {
        String nume = usernameTextField.getText();
        String passwd = passwordTextField.getText();
        crtUser = new Admin(0, nume, passwd);


        try {
            server.login(crtUser, windowCtrl);
            Stage stage = new Stage();
            stage.setTitle("Window for " + crtUser.getUsername());
            stage.setScene(new Scene(mainWindowParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    windowCtrl.logoutBackend();
                    System.exit(0);
                }
            });

            stage.show();
            windowCtrl.setUser(crtUser);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        } catch (ProjectException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }


    }


    public void setUser(Admin admin) {
        this.crtUser = admin;
    }

    public void setWindowController(WindowController windowController) {
        this.windowCtrl = windowController;
    }


}
