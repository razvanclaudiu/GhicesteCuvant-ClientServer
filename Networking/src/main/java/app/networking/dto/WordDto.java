package app.networking.dto;

import java.io.Serializable;

public class WordDto implements Serializable {

   private Long id;

   private String word;

   private PlayerDto user;

    public WordDto( String word, PlayerDto user){
         this.word = word;
         this.user = user;
    }

    public Long getId(){
         return id;
    }

    public String getWord(){
         return word;
    }


    public void setId(Long id){
         this.id = id;
    }

    public void setWord(String word){
         this.word = word;
    }

    public PlayerDto getUser() {
        return user;
    }

    public void setUser(PlayerDto user) {
        this.user = user;
    }

    @Override
    public String toString(){
         return "WordDto{" + "id=" + id + ", word=" + word + ", user=" + user + '}';
    }


}
