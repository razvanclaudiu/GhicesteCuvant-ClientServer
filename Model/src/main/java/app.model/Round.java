package app.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "rounds")
public class Round implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "round_number")
    private Long round_number;

    @Column(name = "scoredPoints")
    private Long scoredPoints;

    @Column(name = "tryWord")
    private String tryWord;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Round() {}

    public Round(Long id, Long round_number, Long scoredPoints, String tryWord, Game game, User user) {
        this.id = id;
        this.round_number = round_number;
        this.scoredPoints = scoredPoints;
        this.tryWord = tryWord;
        this.game = game;
        this.user = user;
    }

    public Round(Long round_number, Long scoredPoints, String tryWord, Game game, User user) {
        this.round_number = round_number;
        this.scoredPoints = scoredPoints;
        this.tryWord = tryWord;
        this.game = game;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Long getRound_number() {
        return round_number;
    }

    public Long setRound_number(Long round_number) {
        return this.round_number = round_number;
    }

    public Long getScoredPoints() {
        return scoredPoints;
    }

    public Long setScoredPoints(Long scoredPoints) {
        return this.scoredPoints = scoredPoints;
    }

    public String getTryWord() {
        return tryWord;
    }

    public String setTryWord(String tryWord) {
        return this.tryWord = tryWord;
    }

    public Game getGame() {
        return game;
    }

    public Game setGame(Game game) {
        return this.game = game;
    }

    public User getUser() {
        return user;
    }

    public User setUser(User user) {
        return this.user = user;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", round_number=" + round_number +
                ", scoredPoints=" + scoredPoints +
                ", tryWord='" + tryWord + '\'' +
                ", game=" + game +
                ", user=" + user +
                '}';
    }

}
