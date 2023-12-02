package com.dlm.gwt.sample.cardmaster.shared.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.card.ExchangeProposal;

public class User implements Serializable {

    private String username;
    private String email;
    private String password; // forse va cercato il binary per gestirlo meglio, oppure on metterlo proprio
    private int age;
    private String gender;
    private List<Card> ownedCards;
    private List<Card> wishedCards;
    private Map<String, Deck> decks;
    private List<ExchangeProposal> exchangeProposals;

    public User(String username, String email, String password, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.ownedCards = new ArrayList<>();
        this.wishedCards = new ArrayList<>();
        this.decks = new HashMap<>();
        this.exchangeProposals = new ArrayList<>();
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

    public List<Card> getOwnedCards() {
        return this.ownedCards;
    }

    public List<Card> getWishedCards() {
        return this.wishedCards;
    }

    public Map<String, Deck> getDecks() {
        return this.decks;
    }

    public List<ExchangeProposal> getExchangeProposals() {
        return this.exchangeProposals;
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

    public void setExchangeProposals(List<ExchangeProposal> exchangeProposals) {
        this.exchangeProposals = exchangeProposals;
    }

    public void addOwnedCard(Card card) {
        this.ownedCards.add(card);
    }

    public void addWishedCard(Card card) {
        this.wishedCards.add(card);
    }

    public void removeOwnedCard(Card card) {
        this.ownedCards.remove(card);
    }

    public void removeWishedCard(Card card) {
        this.wishedCards.remove(card);
    }

    public void addDeck(String deckName, Deck deck) {
        this.decks.put(deckName, deck);
    }

    public void removeDeck(String deckName, Deck deck) {
        this.decks.remove(deckName, deck);
    }

    public void addCardToDeck(Card card, String deckName) {
        Deck deck = this.decks.get(deckName);
        String game = deck.getGame();
        deck.addCard(card, game);
    }

    public void removeCardFromDeck(Card card, String deckName) {
        Deck deck = this.decks.get(deckName);
        deck.removeCard(card);
    }

    public void addExchangeProposal(ExchangeProposal exchangeProposal) {
        this.exchangeProposals.add(exchangeProposal);
    }

    public void removeExchangeProposal(ExchangeProposal exchangeProposal) {
        this.exchangeProposals.remove(exchangeProposal);
    }
}
