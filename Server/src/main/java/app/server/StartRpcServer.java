package app.server;

import app.networking.utils.AbstractServer;
import app.networking.utils.RpcConcurrentServer;
import app.persistance.GameRepository;
import app.persistance.PlayerRepository;
import app.persistance.RoundRepository;
import app.persistance.WordRepository;
import app.persistance.implementation.DefaultPlayerRepository;
import app.persistance.implementation.HibernateGameRepository;
import app.persistance.implementation.HibernateRoundRepository;
import app.persistance.implementation.HibernateWordRepository;
import app.server.implementation.DefaultAppServer;
import app.services.AppException;
import app.services.AppServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {

    private static int defaultPort = 55555;

public static void main(String[] args) {
    Properties properties = new Properties();

    try {
        properties.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
        System.out.println("Server properties set. ");
        properties.list(System.out);
    } catch (IOException e) {
        System.err.println("Cannot find properties " + e);
        return;
    }

    PlayerRepository playerRepository = new DefaultPlayerRepository();

    WordRepository wordRepository = new HibernateWordRepository();

    GameRepository gameRepository = new HibernateGameRepository();

    RoundRepository roundRepository = new HibernateRoundRepository();

    AppServices services = new DefaultAppServer(playerRepository, wordRepository, gameRepository, roundRepository);

    int serverPort = defaultPort;

    try {
        serverPort = Integer.parseInt(properties.getProperty("server.port"));
    } catch (NumberFormatException e) {
        System.err.println("Wrong port number " + e.getMessage());
        System.err.println("Using default port " + defaultPort);
    }
    System.out.println("Starting server on port: " + serverPort);

    AbstractServer server = new RpcConcurrentServer(serverPort, services);

    try{
        server.start();
    } catch (AppException e) {
        System.err.println("Error starting the server " + e.getMessage());
    } finally {
        try {
            server.stop();
        } catch (AppException e) {
            System.err.println("Error stopping server " + e.getMessage());
        }
    }

    }
}
