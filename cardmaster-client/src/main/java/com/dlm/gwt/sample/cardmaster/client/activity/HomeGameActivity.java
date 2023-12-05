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
import com.dlm.gwt.sample.cardmaster.client.backendService.GetUserByUsername;
import com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy.CardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.elements.CardDetailsModalPanel;
import com.dlm.gwt.sample.cardmaster.client.handlers.*;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.client.utils.ElementType;
import com.dlm.gwt.sample.cardmaster.client.view.HomeGameView;

import java.util.List;
import java.util.Map;

public class HomeGameActivity extends AbstractActivity {

    private final HomeGameView view;
    private final DatabaseServiceAsync databaseService;
    private final BackendService backendService;
    private String gameName;
    private CardListType cardListType;
    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private CardHandler cardHandler;
    private DeckHandler deckHandler;
    private ExchangeHandler exchageHandler;

    public HomeGameActivity(HomeGameView view, String gameName, DatabaseServiceAsync databaseService) {
        this.view = view;
        this.gameName = gameName;
        this.databaseService = databaseService;
        this.backendService = new BackendService(this.databaseService);
        this.cardHandler = new CardHandler(this.databaseService, this.backendService, this.gameName);
        this.deckHandler = new DeckHandler(this.databaseService, this.backendService);
        this.exchageHandler = new ExchangeHandler(this.databaseService);
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        containerWidget.setWidget(view);
    }

    /* ++INIZIO MOSTRA TUTTE LE CARTE CON RELATIVI PULSANTI ADD/REMOVE++ */

    public void showCardDetailsModalPanel(Card card) {
        this.cardHandler.showCardDetailsModalPanel(card, this.cardListType);
    }

    public CardListType getCardListType() {
        return this.cardListType;
    }

    // input: magic, pokemon, yugioh
    public void getAllCards() {
        databaseService.getCards(this.gameName, new AsyncCallback<List<Card>>() {
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
        this.cardHandler.addCardToUserOwnedOrWished(this.loggedUser, card, isOwnedOrWished);
    }

    public void updateCardCondition() {
        this.cardHandler.updateCardCondition(this.loggedUser);
    }

    public void removeCardFromUserOwnedOrWished(Card card, Boolean isOwnedOrWished) {

        // rimuovi la carta dalle possedute o desiderate dell'utente
        this.cardHandler.removeCardFromUserOwnedOrWished(this.loggedUser, card, isOwnedOrWished);
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

    /**
     * Metodo che trova le carte owned o wished dell'utente
     * 
     * @param isOwnedOrWished
     */
    public void getOwnedOrWishedCards(Boolean isOwnedOrWished) {

        // se true stampi le owned, se false stampi le wished
        if (isOwnedOrWished == true) {
            this.cardListType = CardListType.SHOW_OWNED_CARDS;
            List<Card> currentGameOwnedCards = this.cardHandler.getOwnedCards(this.loggedUser, this.gameName);
            view.showSearchAndFilter(currentGameOwnedCards, null, null);
        } else {
            this.cardListType = CardListType.SHOW_WISHED_CARDS;
            List<Card> currentGameWishedCards = this.cardHandler.getWishedCards(this.loggedUser, this.gameName);
            view.showSearchAndFilter(currentGameWishedCards, null, null);
        }
    }
    /*--FINE MOSTRA LE CARTE OWNED CON RELATIVI PULSANTI ADD/REMOVE--*/

    /* ++INIZIO GESTISCI DECKS++ */

    public void getDecks() {

        List<Deck> decks = this.deckHandler.getDecks(this.loggedUser, this.gameName);
        // richiama il metodo della view che mostra i decks cosi' aggiornare visivamente
        // la pagina
        view.showGrid(decks, ElementType.DECKS);
    }

    public void createDeck(String deckName) {
        this.deckHandler.createDeck(this.loggedUser, this.gameName, deckName);
    }

    public void deleteDeck(String deckName) {

        this.deckHandler.deleteDeck(this.loggedUser, deckName);

        // toglo dall'intefaccia il deck eliminato
        getDecks();

    }

    public void addCardToDeck(Card card, String deckName) {
        this.deckHandler.addCardToDeck(this.loggedUser, deckName, card);
    }

    public void removeCardFromDeck(Card card, String deckName) {
        this.deckHandler.removeCardFromDeck(this.loggedUser, deckName, card);
    }

    /* --FINE GESTISCI DECKS-- */

    /* ++ INIZIO SCAMBIO ++ */

    /**
     * Retrieves the list of owners and wishers for a given card.
     * 
     * @param modalPanel The CardDetailsModalPanel to display the retrieved
     *                   information.
     * @param card       The card for which to retrieve the owners and wishers.
     * @param isOwned    Specifies whether the card is owned or not.
     * @param callback   The callback to be invoked with the retrieved owners and
     *                   wishers list.
     */
    public void getOwnersWishersList(CardDetailsModalPanel modalPanel, Card card, Boolean isOwned,
            AsyncCallback<Map<String, List<Card>>> callback) {
        this.cardHandler.getOwnersWishersList(this, card, true, new AsyncCallback<Map<String, List<Card>>>() {
            @Override
            public void onSuccess(Map<String, List<Card>> ownersWishersMap) {
                callback.onSuccess(ownersWishersMap);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    public void getUserByUsername(String username, AsyncCallback<User> callback) {

        GetUserByUsername getUserByUsername = new GetUserByUsername(this.databaseService, username);
        getUserByUsername.getUserByUsername(new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    public void sendExchangeProposal(String counterparty, List<Card> proposedCards, List<Card> requestedCards) {
        this.exchageHandler.sendExchangeProposal(this.loggedUser, counterparty, proposedCards, requestedCards);

    }

    /* -- FINE SCAMBIO -- */

    /* ++ METODI DI SUPPORTO ++ */

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
