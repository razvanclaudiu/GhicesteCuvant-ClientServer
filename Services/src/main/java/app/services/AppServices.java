package app.services;

import app.model.Game;
import app.model.Round;
import app.model.User;
import app.model.Word;

import java.util.List;

public interface AppServices {
    User login(String username, String password, AppObserver clientObserver) throws AppException;
    void logout(User user) throws AppException;
    void setPlayerReady(Word word) throws AppException;
    List<Game> startGame(Game game) throws AppException;
    Long getMaxGameId() throws AppException;
    Long getMaxRoundId() throws AppException;
    void addRound(Round round) throws AppException;
}
