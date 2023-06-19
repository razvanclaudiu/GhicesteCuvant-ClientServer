package app.networking.dto;

import app.model.User;

public class DtoUtils {

    public static PlayerDto getDto(User user){
        if(user == null)
            return null;
        return new PlayerDto(
                user.getId().toString(),
                user.getUsername(),
                user.getPassword()
        );
    }

    public static User getFromDto(PlayerDto playerDto){
        if(playerDto == null)
            return null;
        return new User(
                Long.parseLong(playerDto.getId()),
                playerDto.getUsername(),
                playerDto.getPassword()
        );
    }
}
