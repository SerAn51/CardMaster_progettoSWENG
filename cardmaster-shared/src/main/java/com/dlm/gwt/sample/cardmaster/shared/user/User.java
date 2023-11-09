package com.dlm.gwt.sample.cardmaster.shared.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String username;
    private String email;
    private String password; // forse va cercato il binary per gestirlo meglio, oppure on metterlo proprio
    private int age;
    private String gender;
    //private List<OwnedCard> ownedCards;
    //private List<WishedCard> wishedCards;
    private List<String> news;
    //private String userImageBase64;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        //this.ownedCards = new ArrayList<OwnedCard>();
        //this.wishedCards = new ArrayList<WishedCard>();
    }

    public User() {
    };

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public int getAge() {
        return this.age;
    }

    public String getGender() {
        return this.gender;
    }

    /*
    public String getUserImageBase64() {
        return userImageBase64;
    }
    */

    /*
    public List<OwnedCard> getOwnedCards() {
        return ownedCards;
    }

    public List<WishedCard> getWishedCards() {
        return wishedCards;
    }
    */

    public List<String> getNews() {
        return news;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /*
    public void setOwnedCards(List<OwnedCard> owned) {
        this.ownedCards = owned;
    }

    public void setWishedCards(List<WishedCard> wished) {
        this.wishedCards = wished;
    }
    */

    public void setNews(List<String> notifications) {
        if(notifications == null)
            this.news = new ArrayList<String>();
        this.news = notifications;
    }

    /*
    public void setUserImageBase64(String userImageBase64) {
        this.userImageBase64 = userImageBase64;
    }
    */
}
