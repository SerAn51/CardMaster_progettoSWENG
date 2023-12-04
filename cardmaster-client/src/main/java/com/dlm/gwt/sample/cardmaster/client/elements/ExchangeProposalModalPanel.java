package com.dlm.gwt.sample.cardmaster.client.elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ExchangeProposalModalPanel extends PopupPanel {

    private User loggedUser;
    private Card cardLoggedUserWant;
    private String counterpartyUsername;
    private HomeGameActivity homeGameActivity;
    // Dichiarazione di una mappa per associare CheckBox agli oggetti Card
    Map<CheckBox, Card> proposedCardsMap;
    Map<CheckBox, Card> requestedCardsMap;

    public ExchangeProposalModalPanel(Card cardLoggedUserWant, String counterpartyUsername,
            HomeGameActivity homeGameActivity, HidePopupPanelClickingOutside hidePopup) {

        this.loggedUser = homeGameActivity.getLoggedUser();
        this.cardLoggedUserWant = cardLoggedUserWant;
        this.counterpartyUsername = counterpartyUsername;
        this.homeGameActivity = homeGameActivity;
        proposedCardsMap = new HashMap<>();
        requestedCardsMap = new HashMap<>();

        // Contenuto della finestra modale
        Grid popup = new Grid(2, 1);
        popup.setStyleName("exchangeProposalModalPanel");

        Grid chooseCardPanels = new Grid(1, 3);
        chooseCardPanels.setStyleName("chooseCardPanels");

        // Pannello per la scelta delle carte da proporre
        Panel chooseCardForProposalContainer = chooseCardForProposalContainerCreator();

        // Pannello per la visualizzazione delle carte che l'utente counterparty vuole
        Panel cardsCounterpartyWantContainer = cardsCounterpartyWantContainerCreator();

        // Pannello per visualizzare le altre carte che l'utente counterparty possiede
        Panel otherCardsCounterpartyContainer = otherCardsCounterpartyContainerCreator();

        chooseCardPanels.setWidget(0, 0, chooseCardForProposalContainer);
        chooseCardPanels.setWidget(0, 1, cardsCounterpartyWantContainer);
        chooseCardPanels.setWidget(0, 2, otherCardsCounterpartyContainer);

        // Aggiungi il bottone per inviare la proposta
        // bottone per inviare la proposta
        Panel sendProposalButtonContainer = new HorizontalPanel();
        Button sendProposalButton = new Button("Invia proposta!");
        sendProposalButtonContainer.add(sendProposalButton);
        sendProposalButtonContainer.setStyleName("sendProposalButtonContainer");
        sendProposalButton.setStyleName("sendProposalButton");

        chooseCardForProposalContainer.add(sendProposalButtonContainer);

        sendProposalButton.addClickHandler(event -> {
            List<Card> proposedCardsList = new LinkedList<>();

            // controlla tutte le checkbox e, per quelle selezionate, aggiungi la carta alla
            // lista proposedCardsList
            for (CheckBox checkBox : proposedCardsMap.keySet()) {
                if (checkBox.getValue()) {
                    // Ottieni l'oggetto Card associato alla CheckBox selezionata dalla mappa
                    Card proposedCard = proposedCardsMap.get(checkBox);
                    proposedCardsList.add(proposedCard);
                }
            }

            // controlla tutte le checkbox e, per quelle selezionate, aggiungi la carta alla
            // lista requestedCardsList
            List<Card> requestedCardsList = new LinkedList<>();
            // aggiungo la carta selezionata all'inizio
            requestedCardsList.add(cardLoggedUserWant);
            // aggiungo le altre carte eventualmente selezionate dopo
            for (CheckBox checkBox : requestedCardsMap.keySet()) {
                if (checkBox.getValue()) {
                    // Ottieni l'oggetto Card associato alla CheckBox selezionata dalla mappa
                    Card requestedCard = requestedCardsMap.get(checkBox);
                    requestedCardsList.add(requestedCard);
                }
            }

            homeGameActivity.sendExchangeProposal(counterpartyUsername,
                    proposedCardsList, requestedCardsList);

            hide();
            hidePopup.destroy();
        });

        popup.setWidget(0, 0, chooseCardPanels);
        popup.setWidget(1, 0, sendProposalButtonContainer);

        setWidget(popup);
    }

    /**
     * Carte da offrire
     * 
     * @return Panel
     */
    private Panel chooseCardForProposalContainerCreator() {
        Panel chooseCardForProposalContainer = new VerticalPanel();
        chooseCardForProposalContainer.setStyleName("chooseCardForProposalContainer");
        Label carteDaOffrire = new Label("Carte da offrire");
        carteDaOffrire.setStyleName("exchangeTitleLabel");
        chooseCardForProposalContainer.add(carteDaOffrire);

        Label chooseCardForProposalLabel = new Label(
                "Scegli una o piu' carte da proporre in cambio di " + cardLoggedUserWant.getName());
        chooseCardForProposalLabel.setStyleName("chooseCardForProposalLabel");
        chooseCardForProposalContainer.add(chooseCardForProposalLabel);

        Label chooseCardForProposalLabel1 = new Label(
                "Puoi scegliere tra tutte le carte che possiedi, anche se di giochi diversi!");
        chooseCardForProposalLabel1.setStyleName("chooseCardForProposalLabel1");
        chooseCardForProposalContainer.add(chooseCardForProposalLabel1);

        Label chooseCardForProposalLabel2 = new Label(
                "Puoi anche non proporre alcuna carta, ma siamo sicuri che " + counterpartyUsername
                        + " accettera'?👀");
        chooseCardForProposalLabel2.setStyleName("chooseCardForProposalLabel2");
        chooseCardForProposalContainer.add(chooseCardForProposalLabel2);

        Panel scrollableExchangePanel = new VerticalPanel();
        scrollableExchangePanel.setStyleName("scrollableExchangePanel");

        // Itera sulle carte di loggedUser
        for (Card c : loggedUser.getOwnedCards()) {
            CheckBox cardCheckBox = new CheckBox(c.getName() + " - " + c.getCondition());
            cardCheckBox.setStyleName("cardCheckBox");

            // Associa l'oggetto Card alla CheckBox nella mappa (la chiave e' il riferimento
            // alla memoria)
            proposedCardsMap.put(cardCheckBox, c);

            scrollableExchangePanel.add(cardCheckBox);
        }

        chooseCardForProposalContainer.add(scrollableExchangePanel);
        return chooseCardForProposalContainer;
    }

    /**
     * Carte desiderate dall'altro utente
     * 
     * @return
     */
    private Panel cardsCounterpartyWantContainerCreator() {
        Panel cardsCounterpartyWantContainer = new VerticalPanel();
        cardsCounterpartyWantContainer.setStyleName("chooseCardForProposalContainer");

        Label carteDesiderateDallAltroUtente = new Label("Carte desiderate dall'altro utente");
        carteDesiderateDallAltroUtente.setStyleName("exchangeTitleLabel");
        cardsCounterpartyWantContainer.add(carteDesiderateDallAltroUtente);

        Label cardsCounterpartyWantLabel = new Label(
                counterpartyUsername + " vuole queste carte: ");
        cardsCounterpartyWantLabel.setStyleName("cardsCounterpartyWantLabel");
        cardsCounterpartyWantContainer.add(cardsCounterpartyWantLabel);

        Panel scrollableExchangePanel = new VerticalPanel();
        scrollableExchangePanel.setStyleName("scrollableExchangePanel");

        homeGameActivity.getUserByUsername(counterpartyUsername, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {

                // stampa le carte che la controparte desidera
                for (Card counterpartyWishedCard : user.getWishedCards()) {
                    Label cardLabel = new Label("- " + counterpartyWishedCard.getName());
                    cardLabel.setStyleName("cardLabel");
                    scrollableExchangePanel.add(cardLabel);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });

        cardsCounterpartyWantContainer.add(scrollableExchangePanel);
        return cardsCounterpartyWantContainer;
    }

    /**
     * Altre carte possedute dall'altro utente
     * 
     * @return
     */
    private Panel otherCardsCounterpartyContainerCreator() {
        Panel otherCardsCounterpartyContainer = new VerticalPanel();
        otherCardsCounterpartyContainer.setStyleName("chooseCardForProposalContainer");

        Label altreCartePosseduteDallAltroUtente = new Label("Altre carte possedute dall'altro utente");
        altreCartePosseduteDallAltroUtente.setStyleName("exchangeTitleLabel");
        otherCardsCounterpartyContainer.add(altreCartePosseduteDallAltroUtente);

        Label otherCardsCounterpartyLabel = new Label(
                (counterpartyUsername + " possiede anche queste carte"));
        otherCardsCounterpartyLabel.setStyleName("otherCardsCounterpartyLabel");
        otherCardsCounterpartyContainer.add(otherCardsCounterpartyLabel);

        Panel scrollableExchangePanel = new VerticalPanel();
        scrollableExchangePanel.setStyleName("scrollableExchangePanel");

        homeGameActivity.getUserByUsername(counterpartyUsername, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                int cardRecurrence = 0;

                for (Card otherOwnedCard : user.getOwnedCards()) {
                    /**
                     * carta è presente due volte, mostrala una volta, se e' presente tre
                     * volte,mostrala due volte, ecc.
                     *
                     * in questo modo evito di mostrare la carta che l'utente loggato vuole (l'ha
                     * già selezionata avviando su di essa la proposta) ma se c'e' una carta uguale,
                     * la mostro non mostrare la carta che l'utente loggato vuole solo una volta, se
                     * la stessa
                     */
                    if ((otherOwnedCard.compareAttributes(cardLoggedUserWant)) && cardRecurrence == 0) {
                        cardRecurrence++;
                        continue;
                    }
                    HorizontalPanel cardContainer = new HorizontalPanel();
                    cardContainer.setStyleName("cardContainer");
                  

                    HTML imageContainerBackCardButton = new HTML("<div class='backCardImage'></div>");
                    if (otherOwnedCard.getGame().equalsIgnoreCase("Magic")) {
                        imageContainerBackCardButton.setStyleName("backCardImageMagic");
                    } else if (otherOwnedCard.getGame().equalsIgnoreCase("Pokemon")) {
                        imageContainerBackCardButton.setStyleName("backCardImagePokemon");
                    } else if (otherOwnedCard.getGame().equals("Yugioh")) {
                        imageContainerBackCardButton.setStyleName("backCardImageYugioh");
                    }
                    VerticalPanel cardInfoContainer = new VerticalPanel();
                    Label nameLabel = new Label(otherOwnedCard.getName());
                    nameLabel.setStyleName("nameLabel");
                    Label conditionLabel = new Label("Condizione: " + otherOwnedCard.getCondition());
                    conditionLabel.setStyleName("conditionLabel");
                    Label conditionDescriptionLabel = new Label(
                            "Descrizione: " + ((otherOwnedCard.getConditionDescription().equals("")) ? "Nessuna"
                                    : otherOwnedCard.getConditionDescription()));
                    conditionDescriptionLabel.setStyleName("conditionLabel");
                    cardInfoContainer.add(nameLabel);
                    cardInfoContainer.add(conditionLabel);
                    cardInfoContainer.add(conditionDescriptionLabel);

                    CheckBox cardCheckBox = new CheckBox();
                    cardCheckBox.addStyleName("cardCheckBox");
                    cardContainer.add(imageContainerBackCardButton);
                    cardContainer.add(cardInfoContainer);
                    cardContainer.add(cardCheckBox);

                    requestedCardsMap.put(cardCheckBox, otherOwnedCard);
                    scrollableExchangePanel.add(cardContainer);

                    otherCardsCounterpartyContainer.add(cardContainer);


                }
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });

        otherCardsCounterpartyContainer.add(scrollableExchangePanel);
        return otherCardsCounterpartyContainer;
    }
}
