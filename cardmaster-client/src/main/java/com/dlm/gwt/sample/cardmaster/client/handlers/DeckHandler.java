package com.dlm.gwt.sample.cardmaster.client.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.backendService.BackendService;
import com.dlm.gwt.sample.cardmaster.client.backendService.SaveChangesInDBSingleton;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

public class DeckHandler {

    private DatabaseServiceAsync databaseService;
    private BackendService backendService;

    public DeckHandler(DatabaseServiceAsync databaseService, BackendService backendService) {
        this.databaseService = databaseService;
        this.backendService = backendService;
    }

    public List<Deck> getDecks(User loggedUser, String gameName) {
        // TODO:sposta sta roba in cardHandler
        List<Deck> decks = new ArrayList<>();
        Map<String, Deck> userDecks = loggedUser.getDecks();

        if (userDecks != null && !userDecks.isEmpty()) {
            for (Deck deck : userDecks.values()) {
                if (deck.getGame().equalsIgnoreCase(gameName)) {
                    decks.add(deck);
                }
            }
        }
        return decks;
    }

    public void createDeck(User loggedUser, String gameName, String deckName) {

        // creo il deck e lo aggiungo alla "lista" di deck dell'utente
        backendService.createDeck(loggedUser, gameName, deckName);

        // ora rendo persistente il deck nel database
        saveInDB(loggedUser);
    }

    public void deleteDeck(User loggedUser, String deckName) {
        // rimuovo il deck dalla "lista" di deck dell'utente
        backendService.deleteDeck(loggedUser, deckName);
        saveInDB(loggedUser);
    }

    public void addCardToDeck(User loggedUser, String deckName, Card card) {
        // aggiorna il deck dell'utente
        backendService.addCardToDeck(loggedUser, deckName, card);

        // salva le modifiche all'utente nel db
        saveInDB(loggedUser);
    }

    public void removeCardFromDeck(User loggedUser, String deckName, Card card) {

        // aggiorna il deck dell'utente
        backendService.removeCardFromDeck(loggedUser, deckName, card);

        // salva le modifiche all'utente nel db
        saveInDB(loggedUser);
    }

    private void saveInDB(User user) {
        SaveChangesInDBSingleton instance = new SaveChangesInDBSingleton(this.databaseService, user);
        instance.saveChangesInDB();
    }
}
