package app.client;

import app.client.gui.GameController;
import app.client.gui.LoginController;
import app.client.gui.StartController;
import app.networking.rpcprotocol.AppServerRpcProxy;
import app.server.implementation.DefaultAppServer;
import app.services.AppServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception {
        System.out.println("Client starting...");
        Properties clientProperties = new Properties();
        try{
            clientProperties.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set!");
            clientProperties.list(System.out);
        }
        catch(IOException ex){
            System.err.println("Cannot find client.properties");
            return;
        }

        String serverIP = clientProperties.getProperty("server.host", defaultServer);

        int serverPort = defaultPort;

        try{
            serverPort = Integer.parseInt(clientProperties.getProperty("server.port"));
        }
        catch(NumberFormatException ex){
            System.err.println("Wrong port number " + ex.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        AppServices server = new AppServerRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();

        LoginController loginController = loader.getController();
        loginController.setServer(server,primaryStage);

        StartController startController = new FXMLLoader(
                getClass().getClassLoader().getResource("start.fxml")).getController();

        loginController.setStartController(startController);

        GameController gameController = new FXMLLoader(
                getClass().getClassLoader().getResource("game.fxml")).getController();

        loginController.setGameController(gameController);

        primaryStage.setOnCloseRequest(event -> {
           primaryStage.close();
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
