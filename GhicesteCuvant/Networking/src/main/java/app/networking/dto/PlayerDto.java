package app.networking.dto;

import java.io.Serializable;

public class PlayerDto implements Serializable {

    private String id;
    private String username;
    private String password;

    public PlayerDto(String id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString(){
        return "PlayerDto{" + "id=" + id + ", username=" + username + ", password=" + password + '}';
    }
}
