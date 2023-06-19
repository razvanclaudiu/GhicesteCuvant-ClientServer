package app.server.implementation;

import app.model.Game;
import app.model.User;
import app.model.Word;
import app.persistance.GameRepository;
import app.persistance.PlayerRepository;
import app.persistance.RoundRepository;
import app.persistance.WordRepository;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAppServer implements AppServices {

    PlayerRepository playerRepository;

    WordRepository wordRepository;

    GameRepository gameRepository;

    RoundRepository roundRepository;

    private Map<Long, AppObserver> loggedClients;

    private Integer noOfReady = 0;

    private Integer noOfOver = 0;
    private final int defaultThreadsNo = 5;

    public DefaultAppServer(PlayerRepository playerRepository, WordRepository wordRepository, GameRepository gameRepository, RoundRepository roundRepository){
        this.playerRepository = playerRepository;
        this.wordRepository = wordRepository;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        loggedClients = new ConcurrentHashMap<>();
    }


    @Override
    public User login(String username, String password, AppObserver clientObserver) throws AppException {
        try {
            User user = playerRepository.checkCredentials(username, password);
            if(loggedClients.containsKey(user.getId())) {
                throw new AppException("Player already logged in");
            }
            loggedClients.put(user.getId(), clientObserver);
            notifyLogin(loggedClients.size());
            return user;
        }
        catch (RepositoryException e) {
            throw new AppException("Login error " + e);
        }

    }

    @Override
    public void logout(User user) throws AppException {
        loggedClients.remove(user.getId());
        notifyLogout(loggedClients.size());
    }

    @Override
    public void setPlayerReady(Word word) throws AppException {
        try {
            wordRepository.save(word);
            noOfReady++;
            notifyPlayerReady(noOfReady);
        } catch (RepositoryException e) {
            throw new AppException("Error saving word " + e);
        }
    }

    @Override
    public List<Game> startGame(Game game) throws AppException {
        try{
            gameRepository.save(game);
            while(true){
                List<Game> playerList = gameRepository.getAllWithId(game.getGame_id());
                if(playerList.size() == 3)
                    return playerList;
                Thread.sleep(1000);
            }
        } catch (RepositoryException | InterruptedException e) {
            throw new AppException("Error starting game " + e);
        }
    }

    public Long getMaxGameId() throws AppException {
        try {
            return gameRepository.getMaxGameID();
        } catch (RepositoryException e) {
            throw new AppException("Error getting max game id " + e);
        }
    }

    public Long getMaxRoundId() throws AppException{
        try {
            return roundRepository.getMaxRoundID();
        } catch (RepositoryException e) {
            throw new AppException("Error getting max round id " + e);
        }
    }

    public void addRound(app.model.Round round) throws AppException{
        try {
            roundRepository.save(round);
            noOfOver++;
            notifyTurnOver(noOfOver);
            if(noOfOver == 3)
                noOfOver = 0;
        } catch (RepositoryException e) {
            throw new AppException("Error adding round " + e);
        }
    }


    private void notifyLogin(Integer noOfUsers) throws AppException {
      ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(AppObserver clientObserver : loggedClients.values()) {
           if(clientObserver != null){
                executor.execute(() -> {
                     try {
                         System.out.println("Notifying " + clientObserver);
                          clientObserver.notifyLogin(noOfUsers);
                     } catch (AppException e) {
                          System.out.println("Error notifying client " + e);
                     }
                });
           }
        }
        executor.shutdown();
    }

    private void notifyLogout(Integer noOfUsers) throws AppException {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(AppObserver clientObserver : loggedClients.values()) {
            if(clientObserver != null){
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying " + clientObserver);
                        clientObserver.notifyLogout(noOfUsers);
                    } catch (AppException e) {
                        System.out.println("Error notifying client " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    private void notifyPlayerReady(Integer noOfReady) throws AppException{
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(AppObserver clientObserver : loggedClients.values()) {
            if(clientObserver != null){
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying " + clientObserver);
                        clientObserver.notifyPlayerReady(noOfReady);
                    } catch (AppException e) {
                        System.out.println("Error notifying client " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    private void notifyTurnOver(Integer noOfOver) throws AppException{
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(AppObserver clientObserver : loggedClients.values()) {
            if(clientObserver != null){
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying " + clientObserver);
                        clientObserver.notifyTurnOver(noOfOver);
                    } catch (AppException e) {
                        System.out.println("Error notifying client " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }
}
