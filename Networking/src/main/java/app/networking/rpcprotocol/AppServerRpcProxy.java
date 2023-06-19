package app.networking.rpcprotocol;

import app.model.User;
import app.networking.dto.CredentialsDto;
import app.networking.dto.DtoUtils;
import app.networking.dto.PlayerDto;
import app.networking.rpcprotocol.request.Request;
import app.networking.rpcprotocol.request.RequestType;
import app.networking.rpcprotocol.response.Response;
import app.networking.rpcprotocol.response.ResponseType;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AppServerRpcProxy implements AppServices {

    private String host;
    private int port;
    private AppObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AppServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<>();
    }

    @Override
    public User login(String username, String password, AppObserver clientObserver) throws AppException {
        this.client = clientObserver;
        initializeConnection();
        CredentialsDto credentialsDto = new CredentialsDto(username, password);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(credentialsDto).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            System.out.println("Login successful");
            PlayerDto playerDto = (PlayerDto) response.data();
            User user = DtoUtils.getFromDto(playerDto);
            return user;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
        throw new AppException("Unknown response");
    }

    @Override
    public void logout(User user) throws AppException {
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(DtoUtils.getDto(user)).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
            System.out.println("response received " + response);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) throws AppException{
        try{
            output.writeObject(request);
            output.flush();
        }
        catch (IOException e){
            throw new AppException("Error sending object " + e);
        }
    }

    private void initializeConnection() {
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run(){
            while(!finished){
                try{
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if(isUpdate((Response) response)) {
                        System.out.println("update received " + response);
                        handleUpdate((Response) response);
                    }
                    else{
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (IOException | ClassNotFoundException e){
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    private void handleUpdate(Response response) {
        if(response.type() == ResponseType.LOGIN_SUCCESSFULLY){
            Integer noOfUsers = (Integer) response.data();
            try{
                if(client != null){
                    client.notifyLogin(noOfUsers);
                }
            }
            catch (AppException e){
                e.printStackTrace();
            }
        }
        if(response.type() == ResponseType.LOGOUT_SUCCESSFULLY){
            Integer noOfUsers = (Integer) response.data();
            try{
                if(client != null){
                    client.notifyLogout(noOfUsers);
                }
            }
            catch (AppException e){
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        if(response.type() == ResponseType.LOGIN_SUCCESSFULLY || response.type() == ResponseType.LOGOUT_SUCCESSFULLY){
            return true;
        }
        return false;
    }
}
