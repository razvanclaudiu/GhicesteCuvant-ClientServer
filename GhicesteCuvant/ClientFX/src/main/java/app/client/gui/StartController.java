package app.client.gui;

import app.model.User;
import app.services.AppException;
import app.services.AppObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StartController extends Controller implements AppObserver {

    private User user;

    public User getPlayer(){
        return user;
    }

    public void setPlayer(User user){
        this.user = user;
    }

    @FXML
    public Label readyLabel;

    @FXML
    public Label loggedLabel;

    @FXML
    public TextField wordTextField;

    private int noOfLoggedClients = 0;

    @FXML
    public void startAction(){
        
    }

    @FXML
    public void logoutAction(){
        try{
            services.logout(user);
            stage.close();
        }
        catch (AppException ex){
            System.out.println("Logout error " + ex);
        }
    }

    @Override
    public void notifyLogin(Integer noOfUsers) throws AppException {
        Platform.runLater(() -> {
            noOfLoggedClients = noOfUsers;
            loggedLabel.setText("Logged clients: " + noOfLoggedClients);
        });
    }

    @Override
    public void notifyLogout(Integer noOfUsers) throws AppException {
        Platform.runLater(() -> {
            noOfLoggedClients = noOfUsers;
            loggedLabel.setText("Logged clients: " + noOfLoggedClients);
        });
    }

}
