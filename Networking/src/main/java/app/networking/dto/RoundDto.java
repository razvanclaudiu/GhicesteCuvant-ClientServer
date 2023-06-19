package app.networking.dto;

import java.io.Serializable;

public class RoundDto implements Serializable {

    private Long id;

    private Long round_id;

    private Long scoredPoints;

    private String tryWord;

    private GameDto game;

    private PlayerDto user;

    public RoundDto(Long id, Long round_id, Long scoredPoints, String tryWord, GameDto game, PlayerDto user) {
        this.id = id;
        this.round_id = round_id;
        this.scoredPoints = scoredPoints;
        this.tryWord = tryWord;
        this.game = game;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRound_id() {
        return round_id;
    }

    public void setRound_id(Long round_id) {
        this.round_id = round_id;
    }

    public Long getScoredPoints() {
        return scoredPoints;
    }

    public void setScoredPoints(Long scoredPoints) {
        this.scoredPoints = scoredPoints;
    }

    public String getTryWord() {
        return tryWord;
    }

    public void setTryWord(String tryWord) {
        this.tryWord = tryWord;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public PlayerDto getUser() {
        return user;
    }

    public void setUser(PlayerDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RoundDto{" +
                "id=" + id +
                ", round_id=" + round_id +
                ", scoredPoints=" + scoredPoints +
                ", tryWord='" + tryWord + '\'' +
                ", game=" + game +
                ", user=" + user +
                '}';
    }
}
