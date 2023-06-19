package app.server.implementation;

import app.model.User;
import app.persistance.PlayerRepository;
import app.persistance.utils.RepositoryException;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAppServer implements AppServices {

    PlayerRepository playerRepository;

    private Map<Long, AppObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    public DefaultAppServer(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
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
}
