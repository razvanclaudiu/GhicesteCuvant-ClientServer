package app.networking.utils;

import app.services.AppException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {

    private int port;
    private ServerSocket server = null;

    public AbstractServer(int port){
        this.port = port;
    }

    public void start() throws AppException{
        try{
            server = new ServerSocket(port);
            while(true){
                System.out.println("Waiting for clients...");
                Socket client = server.accept();
                System.out.println("Client connected...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new AppException("Error starting server " + e.getMessage());
        } finally {
          stop();
        }
    }

    public void stop() throws AppException{
        try{
            server.close();
        } catch (IOException e) {
            throw new AppException("Error at stop");
        }
    }

    protected abstract void processRequest(Socket client);


}
