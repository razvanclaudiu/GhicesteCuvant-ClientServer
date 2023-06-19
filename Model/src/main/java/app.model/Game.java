package app.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "game_id")
    private Long game_id;

    @Column(name = "score")
    private Long score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "word_id")
    private Word word;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Round> rounds;

    public Game() {}

    public Game(Long id, Long game_id, Long score, User user, Word word) {
        this.id = id;
        this.game_id = game_id;
        this.score = score;
        this.user = user;
        this.word = word;
    }

    public Game(Long game_id, Long score, User user, Word word) {
        this.game_id = game_id;
        this.score = score;
        this.user = user;
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public Long getGame_id() {
        return game_id;
    }

    public Long setGame_id(Long game_id) {
        return this.game_id = game_id;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

   @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", game_id=" + game_id +
                ", score=" + score +
                ", user=" + user +
                ", word=" + word +
                '}';
    }
}
