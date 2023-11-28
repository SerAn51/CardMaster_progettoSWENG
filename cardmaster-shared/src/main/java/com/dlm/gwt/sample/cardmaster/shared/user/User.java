package com.dlm.gwt.sample.cardmaster.shared.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.dlm.gwt.sample.cardmaster.shared.card.Card;

public class User implements Serializable {

    private String username;
    private String email;
    private String password; // forse va cercato il binary per gestirlo meglio, oppure on metterlo proprio
    private int age;
    private String gender;
    private List<Card> ownedCards;
    private List<Card> wishedCards;

    public User(String username, String email, String password, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.ownedCards = new ArrayList<>();
        this.wishedCards = new ArrayList<>();
    }

    public User() {
    }

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

    public List<Card> getOwnedCards() {
        return this.ownedCards;
    }

    public List<Card> getWishedCards() {
        return this.wishedCards;
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

    public void addOwnedCard(Card card) {
        this.ownedCards.add(card);
    }

    public void addWishedCard(Card card) {
        this.wishedCards.add(card);
    }

}
