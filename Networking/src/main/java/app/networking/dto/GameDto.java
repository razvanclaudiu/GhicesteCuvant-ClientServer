package app.networking.dto;

import java.io.Serializable;

public class GameDto implements Serializable {

    private Long id;

    private Long game_id;

    private Long score;

    private PlayerDto player;

    private WordDto word;

    public GameDto(Long id, Long game_id, Long score, PlayerDto player, WordDto word){
        this.id = id;
        this.game_id = game_id;
        this.score = score;
        this.player = player;
        this.word = word;
    }

    public Long getId(){
        return id;
    }

    public Long getGame_id(){
        return game_id;
    }

    public Long getScore(){
        return score;
    }

    public PlayerDto getPlayer(){
        return player;
    }

    public WordDto getWord(){
        return word;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setGame_id(Long game_id){
        this.game_id = game_id;
    }

    public void setScore(Long score){
        this.score = score;
    }

    public void setPlayer(PlayerDto player){
        this.player = player;
    }

    public void setWord(WordDto word){
        this.word = word;
    }

    @Override
    public String toString(){
        return "GameDto{" + "id=" + id + ", score=" + score + ", player=" + player + ", word=" + word + '}';
    }
}
