package app.networking.dto;

import app.model.Game;
import app.model.User;
import app.model.Word;

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

    public static WordDto getDto(Word word){
        if(word == null)
            return null;
        return new WordDto(
                word.getWord(),
                getDto(word.getUser())
        );
    }

    public static Word getFromDto(WordDto wordDto){
        if(wordDto == null)
            return null;
        return new Word(
                wordDto.getWord(),
                getFromDto(wordDto.getUser())
        );
    }

    public static GameDto getDto(app.model.Game game){
        if(game == null)
            return null;
        return new GameDto(
                game.getId(),
                game.getGame_id(),
                game.getScore(),
                getDto(game.getUser()),
                getDto(game.getWord())
        );
    }

    public static Game getFromDto(GameDto gameDto){
        if(gameDto == null)
            return null;
        return new app.model.Game(
                gameDto.getGame_id(),
                gameDto.getScore(),
                getFromDto(gameDto.getPlayer()),
                getFromDto(gameDto.getWord())
        );
    }

    public static RoundDto getDto(app.model.Round round){
        if(round == null)
            return null;
        return new RoundDto(
                round.getId(),
                round.getRound_number(),
                round.getScoredPoints(),
                round.getTryWord(),
                getDto(round.getGame()),
                getDto(round.getUser())
        );
    }

    public static app.model.Round getFromDto(RoundDto roundDto){
        if(roundDto == null)
            return null;
        return new app.model.Round(
                roundDto.getRound_id(),
                roundDto.getScoredPoints(),
                roundDto.getTryWord(),
                getFromDto(roundDto.getGame()),
                getFromDto(roundDto.getUser())
        );
    }
}
