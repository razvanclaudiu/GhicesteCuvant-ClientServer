package app.client.gui;

import app.model.Game;
import app.model.Round;
import app.model.User;
import app.services.AppException;
import app.services.AppObserver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController extends Controller implements AppObserver {

    private User user;

    private List<Game> wordList;

    private List<Character> guessedLetters = new ArrayList<>();

    private Long score = Long.valueOf(0);

    private Long noRound = Long.valueOf(1);

    private boolean turnUsed = false;

    @FXML
    public Label scoreLabel;

    @FXML
    public Label wordPlayer1;

    @FXML
    public Label wordPlayer2;

    @FXML
    public TextField letterTextField;

    @FXML
    public Button buttonPlayer1;

    @FXML
    public Button buttonPlayer2;

    public void start(){
        wordList.removeIf(game -> Objects.equals(game.getUser().getUsername(), user.getUsername()));
        String word = wordList.get(0).getWord().getWord();
        StringBuilder wordToDisplay = new StringBuilder();
        wordToDisplay.append("_".repeat(word.length()));
        wordPlayer1.setText(wordToDisplay.toString());
        word = wordList.get(1).getWord().getWord();
        wordToDisplay = new StringBuilder();
        wordToDisplay.append("_".repeat(word.length()));
        wordPlayer2.setText(wordToDisplay.toString());
        buttonPlayer1.setText(wordList.get(0).getUser().getUsername());
        buttonPlayer2.setText(wordList.get(1).getUser().getUsername());
    }


    public void setPlayer(User user) {
        this.user = user;
    }

    public void setWordList(List<Game> wordList) {
        this.wordList = wordList;
    }



    public void tryAction(ActionEvent actionEvent) throws AppException {
        Long count = Long.valueOf(0);
        if(turnUsed){
            System.out.println("You already used your turn");
            return;
        }
        if(letterTextField.getText().length() != 1){
            System.out.println("Invalid letter");
            return;
        }
        else{

            String letter = letterTextField.getText();
            letterTextField.clear();

            String word = wordList.get(0).getWord().getWord();
            StringBuilder wordToDisplay = new StringBuilder();
            wordToDisplay.append("_".repeat(word.length()));
            for (int i = 0; i < word.length(); i++) {
                if(guessedLetters.contains(word.charAt(i))) {
                    wordToDisplay.setCharAt(i, word.charAt(i));
                }
                if (word.charAt(i) == letter.charAt(0)) {
                    wordToDisplay.setCharAt(i, letter.charAt(0));
                    guessedLetters.add(letter.charAt(0));
                    count++;
                }

            }
            wordPlayer1.setText(wordToDisplay.toString());


            word = wordList.get(1).getWord().getWord();
            wordToDisplay = new StringBuilder();
            wordToDisplay.append("_".repeat(word.length()));
            for(int i = 0; i < word.length(); i++){
                if(guessedLetters.contains(word.charAt(i))) {
                    wordToDisplay.setCharAt(i, word.charAt(i));
                }
                if(word.charAt(i) == letter.charAt(0)){
                    wordToDisplay.setCharAt(i, letter.charAt(0));
                    guessedLetters.add(letter.charAt(0));
                    count++;
                }
            }
            wordPlayer2.setText(wordToDisplay.toString());
        }
        score += count;
        scoreLabel.setText(score.toString());
        turnUsed = true;
        Round round = new Round(services.getMaxRoundId(), noRound, count, letterTextField.getText(), wordList.get(0), user);
        services.addRound(round);
    }

    public void player1Action(ActionEvent actionEvent) throws AppException {
        Long count = Long.valueOf(10);
        if(turnUsed){
            System.out.println("You already used your turn");
            return;
        }
        if(letterTextField.getText().length() == 1){
            System.out.println("Invalid word");
            return;
        }
        else{

            String textWord = letterTextField.getText();
            letterTextField.clear();
            String word = wordList.get(0).getWord().getWord();
            if(Objects.equals(textWord, word)) {
                wordPlayer1.setText(word);
                score += count;
                scoreLabel.setText(score.toString());
            }
        }
        turnUsed = true;
        Round round = new Round(services.getMaxRoundId(), noRound, count, letterTextField.getText(), wordList.get(0), user);
        services.addRound(round);
    }

    public void player2Action(ActionEvent actionEvent) throws AppException {
        Long count = Long.valueOf(10);
        if(turnUsed){
            System.out.println("You already used your turn");
            return;
        }
        if(letterTextField.getText().length() == 1){
            System.out.println("Invalid word");
            return;
        }
        else{
            String textWord = letterTextField.getText();
            letterTextField.clear();
            String word = wordList.get(1).getWord().getWord();
            if(Objects.equals(textWord, word)) {
                wordPlayer2.setText(word);
                score += count;
                scoreLabel.setText(score.toString());
            }
        }
        turnUsed = true;
        Round round = new Round(services.getMaxRoundId(), noRound, count, letterTextField.getText(), wordList.get(0), user);
        services.addRound(round);
    }

    @Override
    public void notifyLogin(Integer noOfUsers) throws AppException {
        return;
    }

    @Override
    public void notifyLogout(Integer noOfUsers) throws AppException {
        return;
    }

    @Override
    public void notifyPlayerReady(Integer noOfReady) throws AppException {
        return;
    }

    @Override
    public void notifyTurnOver(Integer noOfOver) throws AppException {
        Platform.runLater(() -> {
            if(noOfOver == 3){
                noRound++;
                turnUsed = false;
                guessedLetters.clear();
                if(noRound == 4){
                    System.out.println("User " + user.getUsername() + " has finished the game with score " + score);
                }
            }
        });

    }
}
