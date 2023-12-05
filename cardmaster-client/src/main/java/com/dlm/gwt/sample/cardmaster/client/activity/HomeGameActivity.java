package com.dlm.gwt.sample.cardmaster.client.activity;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.dlm.gwt.sample.cardmaster.client.backendService.BackendService;
import com.dlm.gwt.sample.cardmaster.client.backendService.filterer.CardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.elements.CardDetailsModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.client.utils.ElementType;
import com.dlm.gwt.sample.cardmaster.client.view.HomeGameView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeGameActivity extends AbstractActivity {

    private final HomeGameView view;
    private final DatabaseServiceAsync databaseService;
    private final BackendService backendService;
    String gameName;
    CardListType cardListType;
    User loggedUser = SessionUser.getInstance().getSessionUser();

    public HomeGameActivity(HomeGameView view, DatabaseServiceAsync databaseService, String gameName) {
        this.view = view;
        this.databaseService = databaseService;
        this.backendService = new BackendService(databaseService);
        this.gameName = gameName;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        containerWidget.setWidget(view);
    }

    /* ++INIZIO MOSTRA TUTTE LE CARTE CON RELATIVI PULSANTI ADD/REMOVE++ */

    public void showCardDetailsModalPanel(Card card) {
        CardDetailsModalPanel modalPanel = new CardDetailsModalPanel(card, this.gameName,
                this.cardListType);
        modalPanel.center();
        modalPanel.show();
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
        hidePopup.initialize(modalPanel);
    }

    public CardListType getCardListType() {
        return this.cardListType;
    }

    // input: magic, pokemon, yugioh
    public void getAllCards(String gameName) {
        databaseService.getCards(gameName, new AsyncCallback<List<Card>>() {
            @Override
            public void onSuccess(List<Card> cards) {
                // richiama il metodo della view che mostra le carte
                cardListType = CardListType.SHOW_ALL_CARDS;
                view.showSearchAndFilter(cards, null, null);
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.getCards: " + caught.getMessage());
            }
        });
    }

    public void addCardToUserOwnedOrWished(Card card, Boolean isOwnedOrWished) {

        // aggiunti la carta alle possedute o desiderate dell'utente
        backendService.addCardToUserOwnedOrWished(this.loggedUser, card, isOwnedOrWished);

        // ora rendo persistente la carta nel database
        saveChangesInDB(this.loggedUser);
    }

    public void updateCardCondition() {
        saveChangesInDB(this.loggedUser);
    }

    public void removeCardFromUserOwnedOrWished(Card card, Boolean isOwnedOrWished) {

        // rimuovi la carta dalle possedute o desiderate dell'utente
        backendService.removeCardFromUserOwnedOrWished(this.loggedUser, card, isOwnedOrWished);

        // il check della rimozione si occupa di rendere persistente la modifica nel
        // database

    }

    /*--FINE MOSTRA TUTTE LE CARTE CON RELATIVI PULSANTI ADD/REMOVE--*/

    /* INIZIO RICERCA CARTA PER NOME */

    public void getCardByName(List<Card> cards, String searchText) {
        CardFilterStrategy cardFilterStrategy = view.getCardFilterStrategy();
        List<Card> filteredCards = cardFilterStrategy.filter(cards, searchText, null, this.cardListType);
        view.showGrid(filteredCards, ElementType.CARDS);
    }

    /* FINE RICERCA CARTA PER NOME */

    /* ++INIZIO MOSTRA LE CARTE OWNED/WISHED CON RELATIVI PULSANTI ADD/REMOVE++ */
    // non e' un'operazione di backend ma gestisce la visualizzazione delle carte
    // owned/wished
    public void getOwnedOrWishedCards(String gameName, Boolean isOwnedOrWished) {
        // se true stampi le owned, se false stampi le wished
        if (isOwnedOrWished == true) {
            List<Card> cardsOwned = this.loggedUser.getOwnedCards();
            List<Card> localCards = new ArrayList<Card>();
            for (Card card : cardsOwned) {
                if (card.getGame().equalsIgnoreCase(this.gameName)) {
                    localCards.add(card);
                }
            }
            this.cardListType = CardListType.SHOW_OWNED_CARDS;
            view.showSearchAndFilter(localCards, null, null);
        } else {
            List<Card> cardsWished = this.loggedUser.getWishedCards();
            List<Card> localCards = new ArrayList<Card>();
            for (Card card : cardsWished) {
                if (card.getGame().equalsIgnoreCase(this.gameName)) {
                    localCards.add(card);
                }
            }
            this.cardListType = CardListType.SHOW_WISHED_CARDS;
            view.showSearchAndFilter(localCards, null, null);
        }
    }
    /*--FINE MOSTRA LE CARTE OWNED CON RELATIVI PULSANTI ADD/REMOVE--*/

    /* ++INIZIO GESTISCI DECKS++ */

    public void getDecks(User loggedUser, String gameName) {

        List<Deck> decks = new ArrayList<>();
        Map<String, Deck> userDecks = loggedUser.getDecks();

        if (userDecks != null && !userDecks.isEmpty()) {
            for (Deck deck : userDecks.values()) {
                if (deck.getGame().equalsIgnoreCase(gameName)) {
                    decks.add(deck);
                }
            }
        }
        // richiama il metodo della view che mostra i decks cosi' aggiornare visivamente
        // la pagina
        view.showGrid(decks, ElementType.DECKS);
    }

    public void createDeck(String deckName) {

        // creo il deck e lo aggiungo alla "lista" di deck dell'utente
        backendService.createDeck(this.loggedUser, this.gameName, deckName);

        // ora rendo persistente il deck nel database
        saveChangesInDB(this.loggedUser);
    }

    public void deleteDeck(String deckName) {

        // rimuovo il deck dalla "lista" di deck dell'utente
        backendService.deleteDeck(this.loggedUser, deckName);

        // toglo dall'intefaccia il deck eliminato
        getDecks(loggedUser, deckName);

        // ora rendo persistente il deck nel database
        saveChangesInDB(this.loggedUser);
    }

    public void addCardToDeck(Card card, String deckName) {

        // aggiorna il deck dell'utente
        backendService.addCardToDeck(this.loggedUser, deckName, card);

        // salva le modifiche all'utente nel db
        saveChangesInDB(this.loggedUser);
    }

    public void removeCardFromDeck(Card card, String deckName) {

        // aggiorna il deck dell'utente
        backendService.removeCardFromDeck(this.loggedUser, deckName, card);

        // salva le modifiche all'utente nel db
        saveChangesInDB(this.loggedUser);
    }

    /* --FINE GESTISCI DECKS-- */

    /* ++ INIZIO SCAMBIO ++ */

    public void getOwnersWishersList(CardDetailsModalPanel modalPanel, Card card, Boolean isOwned,
            AsyncCallback<Map<String, List<Card>>> callback) {
        final Map<String, List<Card>> ownersWishersMap = new HashMap<>();

        databaseService.getOwnersWishersOfCard(card, isOwned, new AsyncCallback<Map<String, List<Card>>>() {
            @Override
            public void onSuccess(Map<String, List<Card>> dbownersWishersMap) {
                ownersWishersMap.putAll(dbownersWishersMap);

                // Richiama il metodo della vista che mostra le carte
                // modalPanel.showOwnersWishers(ownersWishersMap, isOwned);
                callback.onSuccess(ownersWishersMap);
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.getOwnersWishersList: " + caught.getMessage());
                callback.onFailure(caught);
            }
        });
    }

    public void getUserByUsername(String username, AsyncCallback<User> callback) {
        databaseService.getUserByUsername(username, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.getUserByUsername: " + caught.getMessage());
                callback.onFailure(caught);
            }
        });
    }

    public void sendExchangeProposal(String counterparty, List<Card> proposedCards, List<Card> requestedCards) {
        databaseService.sendExchangeProposal(this.loggedUser, counterparty, proposedCards, requestedCards,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        Window.alert("Proposta di scambio inviata con successo");
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // Gestisci l'errore durante la chiamata al servizio database
                        Window.alert("Errore HomeGameActivity.sendExchangeProposal: " + caught.getMessage());
                    }
                });

    }

    /* -- FINE SCAMBIO -- */

    /* ++ METODI DI SUPPORTO ++ */

    public void saveChangesInDB(User user) {
        databaseService.saveChangesInDB(user, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                // Aggiorna manualmente la lista dei decks nella sessione utente
                Window.alert("Operazione eseguita con successo");
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore: " + caught.getMessage());
            }
        });
    }


    public String getGameName() {
        return this.gameName;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public HomeGameView getView() {
        return this.view;
    }


    /* -- METODI DI SUPPORTO -- */

}
