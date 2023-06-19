package app.client.gui;

import app.model.User;
import app.services.AppException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends Controller{

    private StartController startController;

    private GameController gameController;

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public void loginAction(){
        System.out.println("Login pressed");
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        try{
            FXMLLoader startLoader = new FXMLLoader(
                    getClass().getClassLoader().getResource("start.fxml"));

            Parent root = startLoader.load();

            startController = startLoader.getController();
            startController.setServer(services, stage);

            stage.setOnCloseRequest(event -> {
                stage.close();
            });

            User user = services.login(username, password, getStartController());

            startController.setPlayer(user);

            startController.setGameController(gameController);

            stage.setScene(new Scene(root));
        }
        catch (IOException | AppException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, ex.getMessage());
            alert.showAndWait();
        }
    }

    public StartController getStartController(){
        return startController;
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }
}
