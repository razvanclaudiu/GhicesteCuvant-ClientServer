package app.networking.dto;

import java.io.Serializable;

public class CredentialsDto implements Serializable {

    private String username;
    private String password;

    public CredentialsDto(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString(){
        return "CredentialsDto{" + "username=" + username + ", password=" + password + '}';
    }
}
