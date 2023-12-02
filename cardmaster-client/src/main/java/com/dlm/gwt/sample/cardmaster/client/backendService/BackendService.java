package com.dlm.gwt.sample.cardmaster.client.backendService;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BackendService {

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

    public void removeCardFromUserOwnedOrWished(User user, Card card, Boolean isOwnedOrWished) {
        // Rimuovi la carta dalle "owned" se presente
        if (isOwnedOrWished == true && user.getOwnedCards().contains(card)) {
            removeOwnedCard(user, card);
        }
        // Rimuovi la carta dalle "wished" se presente
        if (isOwnedOrWished == false && user.getWishedCards().contains(card)) {
            user.removeWishedCard(card);
        }
    }

    private void removeOwnedCard(User user, Card card) {

        user.getOwnedCards().remove(card);

        // se rimuovi la carta, controlla se è presente in qualche deck e rimuovila
        for (Deck deck : user.getDecks().values()) {
            if (deck.getCards().contains(card)) {
                deck.getCards().remove(card);
            }
        }

        // controlla se la carta è presente in qualche proposta di scambio rimuovi tale
        // proposta
        databaseService.checkExchangesAfterRemoveCard(user, card, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // ora rendo persistente la carta nel database
                databaseService.saveChangesInDB(user, new AsyncCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        // Aggiorna manualmente la lista dei decks nella sessione utente
                        Window.alert("Carta rimossa con successo");
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // Gestisci l'errore durante la chiamata al servizio database
                        Window.alert("Errore: " + caught.getMessage());
                    }
                });

            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.removeCard: " + caught.getMessage());
            }
        });

        
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
