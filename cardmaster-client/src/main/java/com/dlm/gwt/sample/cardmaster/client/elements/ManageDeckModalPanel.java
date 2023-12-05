package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ManageDeckModalPanel extends PopupPanel {

    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private String gameName;
    private HomeGameActivity homeGameActivity;
    private String deckName;
    private HidePopupPanelClickingOutside hidePopup;

    public ManageDeckModalPanel(String gameName, HomeGameActivity homeGameActivity, String deckName,
            HidePopupPanelClickingOutside hidePopup) {

        this.gameName = gameName;
        this.homeGameActivity = homeGameActivity;
        this.deckName = deckName;
        this.hidePopup = hidePopup;

        // Contenuto della finestra modale
        Panel content = new HorizontalPanel();

        // Zona carte gia' presenti nel deck
        cardsAlreadyAdded(content);

        // Zona aggiungi carte al deck
        cardsAvailable(content);

        setWidget(content);
    }

    private void cardsAlreadyAdded(Panel content) {
        FlowPanel cardsAlreadyInDeckContainer = new FlowPanel();
        cardsAlreadyInDeckContainer.setStyleName("popupContainer"); // Applica la classe CSS container

        // Aggiungi l'etichetta "Carte disponibili"
        Label label = new Label("Carte nel deck");
        label.setStyleName("sticky-label"); // Applica la classe CSS sticky-label
        cardsAlreadyInDeckContainer.add(label);

        FlowPanel scrollableCards = new FlowPanel();
        scrollableCards.setStyleName("scrollable-cards"); // Applica la classe CSS scrollable-cards

        if (loggedUser.getDecks().get(deckName).getCards().size() > 0) {

            for (Card card : loggedUser.getDecks().get(deckName).getCards()) {

                Panel cardInfo = new VerticalPanel();
                Panel cardDetails = new VerticalPanel();
                Label cardName = new Label(card.getName());
                Label cardCondition = new Label(card.getCondition());
                Panel buttonContainerPanel = new HorizontalPanel();
                Button removeCardButton = new Button("Remove");
                removeCardButton.addClickHandler(event -> {
                    homeGameActivity.removeCardFromDeck(card, this.deckName);
                    hide();
                    hidePopup.destroy();
                    homeGameActivity.getDecks();
                });

                removeCardButton.setStyleName("removeCardFromDeck");
                cardInfo.setStyleName("card-details");
                buttonContainerPanel.add(removeCardButton);
                buttonContainerPanel.setStyleName("cardToDeck");
                cardDetails.add(cardName);
                cardDetails.add(cardCondition);
                cardInfo.add(cardDetails);
                cardInfo.add(buttonContainerPanel);

                scrollableCards.add(cardInfo);
            }
        } else {
            // FIXME: richiama il placeholder del homeGameActivity
            Label noCards = new Label("Non ci sono carte nel deck");
            noCards.setStyleName("noCardsDeck");
            scrollableCards.add(noCards);
        }
        cardsAlreadyInDeckContainer.add(scrollableCards);
        content.add(cardsAlreadyInDeckContainer);
    }

    private void cardsAvailable(Panel content) {
        FlowPanel chooseCardContainer = new FlowPanel();
        chooseCardContainer.setStyleName("popupContainer"); // Applica la classe CSS container

        // Aggiungi l'etichetta "Carte disponibili"
        Label label = new Label("Carte disponibili");
        label.setStyleName("sticky-label"); // Applica la classe CSS sticky-label
        chooseCardContainer.add(label);

        FlowPanel scrollableCards = new FlowPanel();
        scrollableCards.setStyleName("scrollable-cards"); // Applica la classe CSS scrollable-cards
        if (loggedUser.getOwnedCards().size() == 0) {
            Label noCards = new Label("Non hai carte disponibili");
            noCards.setStyleName("noCardsOwned");
            chooseCardContainer.add(noCards);
        } else {

            for (Card card : loggedUser.getOwnedCards()) {
                if (card.getGame().equals(gameName) && !loggedUser.getDecks().get(deckName).getCards().contains(card)) {

                    Panel cardInfo = new VerticalPanel();
                    Label cardName = new Label(card.getName());
                    Label cardCondition = new Label(card.getCondition());
                    Panel cardDetails = new VerticalPanel();
                    Panel buttonContainerPanel = new HorizontalPanel();
                    Button addCardButton = new Button("Add");
                    addCardButton.addClickHandler(event -> {
                        homeGameActivity.addCardToDeck(card, this.deckName);
                        hide();
                        hidePopup.destroy();
                        homeGameActivity.getDecks();
                    });

                    addCardButton.setStyleName("addCardToDeck");
                    cardInfo.setStyleName("card-details");
                    buttonContainerPanel.setStyleName("cardToDeck");

                    buttonContainerPanel.add(addCardButton);
                    cardDetails.add(cardName);
                    cardDetails.add(cardCondition);
                    cardInfo.add(cardDetails);
                    cardInfo.add(buttonContainerPanel);

                    scrollableCards.add(cardInfo);
                }
            }
            // FIXME: richiama il placeholder del homeGameActivity
        }
        chooseCardContainer.add(scrollableCards);
        content.add(chooseCardContainer);
    }
}