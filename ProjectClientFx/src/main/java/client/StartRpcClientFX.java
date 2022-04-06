package client;

import client.gui.LoginController;
import client.gui.WindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.rpc.ServicesRpcProxy;
import services.IServices;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";
    private Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("loginWindow.fxml"));
        LoginController ctrl = new LoginController();
        ctrl.setServer(server);
        loader.setController(ctrl);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("mainWindow.fxml"));
        WindowController windowCtrl = new WindowController();
        windowCtrl.setServer(server);
        ctrl.setWindowController(windowCtrl);
        cloader.setController(windowCtrl);

        Parent croot = cloader.load();
        ctrl.setParent(croot);

        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("MPP");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}


