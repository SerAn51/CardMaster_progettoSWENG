package com.dlm.gwt.sample.cardmaster.client.activity;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.backendService.BackendService;
import com.dlm.gwt.sample.cardmaster.client.elements.CardDetailsModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.client.view.HomeGameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeGameActivity extends AbstractActivity {

    private final HomeGameView view;
    private final DatabaseServiceAsync databaseService;
    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private final BackendService backendService;

    String gameName;
    private CardListType cardListType;

    public HomeGameActivity(HomeGameView view, DatabaseServiceAsync databaseService, String gameName) {
        this.view = view;
        this.databaseService = databaseService;
        this.gameName = gameName;
        this.backendService = new BackendService(databaseService);
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        containerWidget.setWidget(view);
    }

    public void toHome() {
        // apri la pagina di gestione delle carte di magic/pokemon/yugioh
        String token = "home";
        History.newItem(token);
        new ViewRouter().handleRouteChange(token);// vai a vedere la url perche' ho cambiato il token
    }

    public void getCards(String gameName) {
        databaseService.getCards(gameName, new AsyncCallback<List<Card>>() {
            @Override
            public void onSuccess(List<Card> cards) {
                cardListType = CardListType.SHOW_ALL_CARDS;
                // richiama il metodo della view che mostra le carte
                view.showGrid(cards);
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

    public void getOwnedOrWishedCards(String gameName, Boolean isOwnedOrWished) {
        // se true stampi le owned, se false stampi le wished
        if (isOwnedOrWished == true) {
            List<Card> cardsOwned = this.loggedUser.getOwnedCards();
            this.cardListType = CardListType.SHOW_OWNED_CARDS;
            view.showGrid(cardsOwned);
        }
        else {
            List<Card> cardsWished = this.loggedUser.getWishedCards();
            this.cardListType = CardListType.SHOW_WISHED_CARDS;
            view.showGrid(cardsWished);
        }
    }

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

    public void showCardDetailsModalPanel(Card card) {
        CardDetailsModalPanel modalPanel = new CardDetailsModalPanel(this.loggedUser, card, this.gameName,
                this.cardListType);
        modalPanel.show();
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
        hidePopup.initialize(modalPanel);
    }

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
        view.showDecks(decks);
    }

    public void deleteDeck(String deckName) {

        // rimuovo il deck dalla "lista" di deck dell'utente
        backendService.deleteDeck(this.loggedUser, deckName);

        // toglo dall'intefaccia il deck eliminato
        getDecks(loggedUser, deckName);

        // ora rendo persistente il deck nel database
        saveChangesInDB(this.loggedUser);
    }

    public void removeCardFromDeck(Card card, String deckName) {

        // aggiorna il deck dell'utente
        backendService.removeCardFromDeck(this.loggedUser, deckName, card);

        // salva le modifiche all'utente nel db
        saveChangesInDB(this.loggedUser);
    }

    public void addCardToDeck(Card card, String deckName) {

        // aggiorna il deck dell'utente
        backendService.addCardToDeck(this.loggedUser, deckName, card);

        // salva le modifiche all'utente nel db
        saveChangesInDB(this.loggedUser);
    }

    public void createDeck(String deckName) {

        // creo il deck e lo aggiungo alla "lista" di deck dell'utente
        backendService.createDeck(this.loggedUser, this.gameName, deckName);

        // ora rendo persistente il deck nel database
        saveChangesInDB(this.loggedUser);
    }

    /* -- METODI DI SUPPORTO -- */
}
