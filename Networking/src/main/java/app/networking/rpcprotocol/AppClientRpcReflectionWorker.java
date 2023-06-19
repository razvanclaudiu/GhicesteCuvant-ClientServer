package app.networking.rpcprotocol;

import app.model.Game;
import app.model.Round;
import app.model.User;
import app.model.Word;
import app.networking.dto.*;
import app.networking.rpcprotocol.request.Request;
import app.networking.rpcprotocol.response.Response;
import app.networking.rpcprotocol.response.ResponseType;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class AppClientRpcReflectionWorker implements Runnable, AppObserver {

    private AppServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public AppClientRpcReflectionWorker(AppServices server, Socket connection){
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while(connected){
            try{
                Object request = input.readObject();
                Response response = handleRequest((Request)request);
                if(response != null){
                    sendResponse(response);
                }
            }
            catch(IOException | ClassNotFoundException e){
               System.out.println("Reading error " + e);
            }

            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        try{
            input.close();
            output.close();
            connection.close();
        }
        catch(IOException e){
            System.out.println("Error " + e);
        }
    }

    private synchronized void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("Handler name: " + handlerName);
        try{
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response)method.invoke(this, request);
            System.out.println("Method invoked: " + method.getName());
        }
        catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException e){
            e.printStackTrace();
        }
        return response;
    }

    private Response handleLOGIN(Request request){
        System.out.println("Login request ...");
        CredentialsDto credentialsDto = (CredentialsDto)request.data();
        try{
            User user = server.login(credentialsDto.getUsername(), credentialsDto.getPassword(), this);
            return new Response.Builder().type(ResponseType.OK).data(DtoUtils.getDto(user)).build();
        }
        catch(AppException e){
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("Logout request ...");
        PlayerDto playerDto = (PlayerDto)request.data();
        try{
            User user = DtoUtils.getFromDto(playerDto);
            server.logout(user);
            return new Response.Builder().type(ResponseType.OK).build();
        }
        catch(AppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSET_PLAYER_READY(Request request){
        System.out.println("Set player ready request ...");
        WordDto wordDto = (WordDto)request.data();
        try{
            Word word = DtoUtils.getFromDto(wordDto);
            server.setPlayerReady(word);
            return new Response.Builder().type(ResponseType.OK).build();
        }
        catch(AppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSTART_GAME(Request request){
        System.out.println("Start game request ...");
        GameDto gameDto = (GameDto)request.data();
        try{
            Game game = DtoUtils.getFromDto(gameDto);
            GameDto[] gameDtos = server.startGame(game).stream().map(DtoUtils::getDto).toArray(GameDto[]::new);
            return new Response.Builder().type(ResponseType.OK).data(gameDtos).build();
        }
        catch(AppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_MAX_GAME_ID(Request request){
        System.out.println("Get max game id request ...");
        try{
            Long maxGameId = server.getMaxGameId();
            return new Response.Builder().type(ResponseType.OK).data(maxGameId).build();
        }
        catch(AppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_MAX_ROUND_ID(Request request){
        System.out.println("Get max round id request ...");
        try{
            Long maxRoundId = server.getMaxRoundId();
            return new Response.Builder().type(ResponseType.OK).data(maxRoundId).build();
        }
        catch(AppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_ROUND(Request request){
        System.out.println("Add round request ...");
        RoundDto roundDto = (RoundDto)request.data();
        try{
            Round round = DtoUtils.getFromDto(roundDto);
            server.addRound(round);
            return new Response.Builder().type(ResponseType.OK).build();
        }
        catch(AppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private final static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    @Override
    public void notifyLogin(Integer noOfUsers) throws AppException {
        Response response = new Response.Builder().type(ResponseType.LOGIN_SUCCESSFULLY).data(noOfUsers).build();
        try{
            sendResponse(response);
        }
        catch(IOException e){
            throw new AppException("Sending error: " + e);
        }
    }

    @Override
    public void notifyLogout(Integer noOfUsers) throws AppException {
        Response response = new Response.Builder().type(ResponseType.LOGOUT_SUCCESSFULLY).data(noOfUsers).build();
        try{
            sendResponse(response);
        }
        catch(IOException e){
            throw new AppException("Sending error: " + e);
        }
    }

    @Override
    public void notifyPlayerReady(Integer noOfReady) throws AppException {
        Response response = new Response.Builder().type(ResponseType.PLAYER_READY).data(noOfReady).build();
        try{
            sendResponse(response);
        }
        catch(IOException e){
            throw new AppException("Sending error: " + e);
        }
    }


    @Override
    public void notifyTurnOver(Integer noOfOver) throws AppException {
        Response response = new Response.Builder().type(ResponseType.TURN_OVER).data(noOfOver).build();
        try{
            sendResponse(response);
        }
        catch(IOException e){
            throw new AppException("Sending error: " + e);
        }
    }
}
