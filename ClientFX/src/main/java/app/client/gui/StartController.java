package app.client.gui;

import app.model.Game;
import app.model.User;
import app.model.Word;
import app.services.AppException;
import app.services.AppObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class StartController extends Controller implements AppObserver {

    private GameController gameController;

    private User user;

    private Word word;

    public User getPlayer(){
        return user;
    }

    public void setPlayer(User user){
        this.user = user;
    }

    public void setWord(Word word){
        this.word = word;
    }

    public Word getWord(){
        return word;
    }

    @FXML
    public Label readyLabel;

    @FXML
    public Label loggedLabel;

    @FXML
    public TextField wordTextField;

    private int noOfLoggedClients = 0;

    private boolean readyState = false;

    @FXML
    public void startAction(){
        if(readyState == false) {
            try {
                String word = wordTextField.getText();
                if (word != null && (word.length() < 4 || word.length() > 6)) {
                    throw new AppException("Word size must be between 4 and 6 characters!");
                }
                Word w = new Word(new Random().nextLong(), word, user);
                services.setPlayerReady(w);
                readyState = true;
                setWord(w);
            } catch (AppException e) {
                throw new RuntimeException(e);
            }
        }
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

    @Override
    public void notifyPlayerReady(Integer noOfReady) throws AppException {
        Platform.runLater(() -> {
            readyLabel.setText("Ready clients: " + noOfReady);
            if(noOfReady == 3)
            {

                try{
                    FXMLLoader startLoader = new FXMLLoader(
                            getClass().getClassLoader().getResource("game.fxml"));

                    Parent root = startLoader.load();

                    gameController = startLoader.getController();
                    gameController.setServer(services, stage);

                    stage.setOnCloseRequest(event -> {
                        stage.close();
                    });

                    gameController.setPlayer(user);

                    Game game = new Game(services.getMaxGameId(), 0L, user, getWord());

                    List<Game> wordList = services.startGame(game);

                    gameController.setWordList(wordList);

                    gameController.start();

                    stage.setScene(new Scene(root));
                }
                catch (IOException | AppException ex){
                    Alert alert = new Alert(Alert.AlertType.WARNING, ex.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    @Override
    public void notifyTurnOver(Integer noOfOver) throws AppException {
        return;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
