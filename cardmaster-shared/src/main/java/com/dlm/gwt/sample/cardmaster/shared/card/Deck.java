package com.dlm.gwt.sample.cardmaster.shared.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {

    private String name;
    private String game;
    private List<Card> cards;

    public Deck() {
        // costruttore vuoto utile per la seriazlizzazione
    }

    // non prendo la lista di carte perche' potrei voler creare un deck vuoto e solo
    // successivamente aggiungere le carte
    public Deck(String name, String game) {
        this.name = name;
        this.game = game;
        this.cards = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getGame() {
        return this.game;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCard(Card card, String game) {
        if (game.equalsIgnoreCase("magic") || game.equalsIgnoreCase("yugioh")) {
            // Non più di 60 carte
            if (this.cards.size() < 60 && !hasDuplicate(card.getName())) {
                this.cards.add(card);
            }
        }
        if (game.equalsIgnoreCase("pokemon")) {
            // Non più di 60 carte e non più di 4 copie di una stessa carta
            if (this.cards.size() < 60 && !hasMaxCopies(card.getName(), 4)) {
                this.cards.add(card);
            }
        }

    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    public String toString() {
        return "Deck: " + this.name + " - Game: " + this.game + " - #Cards: " + this.cards.size();
    }

    // Funzione ausiliaria per verificare la presenza di carte duplicate
    private boolean hasDuplicate(String cardName) {
        for (Card existingCard : this.cards) {
            if (existingCard.getName().equalsIgnoreCase(cardName)) {
                // Carta duplicata trovata
                return true;
            }
        }
        // Nessuna carta duplicata trovata
        return false;
    }

    // Funzione ausiliaria per verificare il numero massimo di copie di una carta
    private boolean hasMaxCopies(String cardName, int maxCopies) {
        int count = 0;
        for (Card existingCard : this.cards) {
            if (existingCard.getName().equalsIgnoreCase(cardName)) {
                // Incrementa il contatore se trova una copia della carta
                count++;
                if (count >= maxCopies) {
                    // Ha superato il numero massimo di copie consentite
                    return true;
                }
            }
        }
        // Non ha superato il numero massimo di copie
        return false;
    }

}
