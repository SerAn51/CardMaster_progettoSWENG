package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

public class ManageDeckModalPanel extends PopupPanel {

    private User loggedUser;
    private String gameName;
    private HomeGameActivity homeGameActivity;
    private String deckName;
    private HidePopupPanelClickingOutside hidePopup;

    public ManageDeckModalPanel(User loggedUser, String gameName, HomeGameActivity homeGameActivity, String deckName, HidePopupPanelClickingOutside hidePopup) {

        this.loggedUser = loggedUser;
        this.gameName = gameName;
        this.homeGameActivity = homeGameActivity;
        this.deckName = deckName;
        this.hidePopup = hidePopup;

        // Contenuto della finestra modale
        HorizontalPanel content = new HorizontalPanel();

        // Zona carte gia' presenti nel deck
        cardsAlreadyAdded(content);

        // Zona aggiungi carte al deck
        cardsAvailable(content);

        setWidget(content);
    }

    private void cardsAlreadyAdded(Panel content) {
        FlowPanel cardsAlreadyInDeckContainer = new FlowPanel();
        cardsAlreadyInDeckContainer.addStyleName("initDeckContainer");
        for (Card card : loggedUser.getDecks().get(deckName).getCards()) {
            Label cardDetails = new Label(card.toString());
            cardsAlreadyInDeckContainer.add(cardDetails);

            Button removeCardButton = new Button("Remove");

            cardsAlreadyInDeckContainer.add(removeCardButton);

            removeCardButton.addClickHandler(event -> {
                homeGameActivity.removeCardFromDeck(card, this.deckName);
                hide();
                hidePopup.destroy();
                homeGameActivity.getDecks(loggedUser, gameName);
            });
        }
        content.add(cardsAlreadyInDeckContainer);
    }

    private void cardsAvailable(Panel content) {
        FlowPanel chooseCardContainer = new FlowPanel();
        chooseCardContainer.addStyleName("initDeckContainer");

        for (Card card : loggedUser.getOwnedCards()) {
            if (card.getGame().equals(gameName) && !loggedUser.getDecks().get(deckName).getCards().contains(card)) {
                Label cardDetails = new Label(card.toString());
                chooseCardContainer.add(cardDetails);

                Button addCardButton = new Button("Add");

                chooseCardContainer.add(addCardButton);

                addCardButton.addClickHandler(event -> {
                    homeGameActivity.addCardToDeck(card, this.deckName);
                    hide();
                    hidePopup.destroy();
                    homeGameActivity.getDecks(loggedUser, gameName);
                });
            }
        }

        content.add(chooseCardContainer);
    }
}