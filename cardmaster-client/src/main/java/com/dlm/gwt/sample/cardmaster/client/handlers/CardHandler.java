package com.dlm.gwt.sample.cardmaster.client.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.backendService.BackendService;
import com.dlm.gwt.sample.cardmaster.client.backendService.SaveChangesInDBSingleton;
import com.dlm.gwt.sample.cardmaster.client.elements.CardDetailsModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CardHandler {

    private DatabaseServiceAsync databaseService;
    private String gameName;
    private BackendService backendService;

    public CardHandler(DatabaseServiceAsync databaseService, BackendService backendService, String gameName) {
        this.databaseService = databaseService;
        this.backendService = backendService;
        this.gameName = gameName;
    }

    public void showCardDetailsModalPanel(Card card, CardListType cardListType) {
        CardDetailsModalPanel modalPanel = new CardDetailsModalPanel(card, gameName, cardListType);
        modalPanel.center();
        modalPanel.show();
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
        hidePopup.initialize(modalPanel);
    }

    /**
     * Metodo di supporto per ritornare la lista degli owned di una carta,
     * utile al metodo getOwnedOrWishedCards di HomeGameActivity
     * 
     * @param loggedUser
     * @param gameName
     * @return List<Card> di carte owned
     */
    public List<Card> getOwnedCards(User loggedUser, String gameName) {
        List<Card> cardsOwned = loggedUser.getOwnedCards();
        List<Card> cards = getLocalCards(cardsOwned);
        return cards;
    }

    /**
     * Metodo di supporto per ritornare la lista delle wished di una carta,
     * utile al metodo getOwnedOrWishedCards di HomeGameActivity
     * 
     * @param loggedUser
     * @param gameName
     * @return List<Card> di carte wished
     */
    public List<Card> getWishedCards(User loggedUser, String gameName) {
        List<Card> cardsWished = loggedUser.getWishedCards();
        List<Card> localCards = getLocalCards(cardsWished);
        return localCards;
    }

    /**
     * Metodo di supporto per ritornare la lista delle carte owned o wished
     * 
     * @param cardsList
     * @return lista di carte
     */
    private List<Card> getLocalCards(List<Card> cardsList) {
        List<Card> localCards = new ArrayList<Card>();
        for (Card card : cardsList) {
            if (card.getGame().equalsIgnoreCase(this.gameName)) {
                localCards.add(card);
            }
        }
        return localCards;
    }

    // occhio che potrebbe dare problemi
    public void getOwnersWishersList(HomeGameActivity homeGameActivity, Card card, boolean isOwned,
            AsyncCallback<Map<String, List<Card>>> callback) {
        final Map<String, List<Card>> ownersWishersMap = new HashMap<>();

        databaseService.getOwnersWishersOfCard(card, isOwned, new AsyncCallback<Map<String, List<Card>>>() {
            @Override
            public void onSuccess(Map<String, List<Card>> dbownersWishersMap) {
                ownersWishersMap.putAll(dbownersWishersMap);

                // ritorna in maniera asincrona la mappa di owners e wishers
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

    public void addCardToUserOwnedOrWished(User loggedUser, Card card, Boolean isOwnedOrWished) {
        // aggiunti la carta alle possedute o desiderate dell'utente
        this.backendService.addCardToUserOwnedOrWished(loggedUser, card, isOwnedOrWished);

        // ora rendo persistente la carta nel database
        saveInDB(loggedUser);
    }

    public void removeCardFromUserOwnedOrWished(User loggedUser, Card card, Boolean isOwnedOrWished) {
        this.backendService.removeCardFromUserOwnedOrWished(loggedUser, card, isOwnedOrWished);
    }

    public void updateCardCondition(User user) {
        saveInDB(user);
    }

    private void saveInDB(User user) {
        SaveChangesInDBSingleton instance = new SaveChangesInDBSingleton(this.databaseService, user);
        instance.saveChangesInDB();
    }
}
