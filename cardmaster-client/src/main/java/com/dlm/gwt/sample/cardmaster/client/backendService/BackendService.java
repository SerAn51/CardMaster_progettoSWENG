package com.dlm.gwt.sample.cardmaster.client.backendService;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

public class BackendService {
    // Database utile per le prossime task
    private final DatabaseServiceAsync databaseService;

    // TODO: suddividere BackendService in più classi: una per le operazioni sulle
    // carte, una per le operazioni sui deck, una per le operazioni di filtro
    public BackendService(DatabaseServiceAsync databaseService) {
        this.databaseService = databaseService;
    }

    public void addCardToUserOwnedOrWished(User user, Card card, Boolean isOwnedOrWished) {
        if (isOwnedOrWished) {
            user.addOwnedCard(card);
        } else {
            user.addWishedCard(card);
        }
    }

    public void createDeck(User user, String gameName, String deckName) {
        // creo il deck e lo aggiungo alla "lista" di deck dell'utente
        Deck deck = new Deck(deckName, gameName);
        user.getDecks().put(deckName, deck);
    }

    public void deleteDeck(User user, String deckName) {
        // rimuovo il deck dalla "lista" di deck dell'utente
        user.getDecks().remove(deckName);
    }

    public void addCardToDeck(User user, String deckName, Card card) {
        // aggiungo la carta al deck
        user.addCardToDeck(card, deckName);
    }

    public void removeCardFromDeck(User user, String deckName, Card card) {
        // rimuovo la carta dal deck
        user.removeCardFromDeck(card, deckName);
    }
}
