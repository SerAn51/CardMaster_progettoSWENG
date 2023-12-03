package com.dlm.gwt.sample.cardmaster.client.elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExchangeProposalModalPanel extends PopupPanel {

    private User loggedUser;
    private Card cardLoggedUserWant;
    private String counterpartyUsername;
    private HomeGameActivity homeGameActivity;
    // Dichiarazione di una mappa per associare CheckBox agli oggetti Card
    Map<CheckBox, Card> proposedCardsMap;
    Map<CheckBox, Card> requestedCardsMap;

    public ExchangeProposalModalPanel(User loggedUser, Card cardLoggedUserWant, String counterpartyUsername,
            HomeGameActivity homeGameActivity, HidePopupPanelClickingOutside hidePopup) {

        this.loggedUser = loggedUser;
        this.cardLoggedUserWant = cardLoggedUserWant;
        this.counterpartyUsername = counterpartyUsername;
        this.homeGameActivity = homeGameActivity;
        proposedCardsMap = new HashMap<>();
        requestedCardsMap = new HashMap<>();

        // Contenuto della finestra modale
        VerticalPanel content = new VerticalPanel();
        HorizontalPanel chooseCardPanels = new HorizontalPanel();

        // Pannello per la scelta delle carte da proporre
        FlowPanel chooseCardForProposalContainer = chooseCardForProposalContainerCreator();

        // Pannello per la visualizzazione delle carte che l'utente counterparty vuole
        FlowPanel cardsCounterpartyWantContainer = cardsCounterpartyWantContainerCreator();

        // Pannello per visualizzare le altre carte che l'utente counterparty possiede
        FlowPanel otherCardsCounterpartyContainer = otherCardsCounterpartyContainerCreator();

        chooseCardPanels.add(chooseCardForProposalContainer);
        chooseCardPanels.add(cardsCounterpartyWantContainer);
        chooseCardPanels.add(otherCardsCounterpartyContainer);
        content.add(chooseCardPanels);

        // Aggiungi il bottone per inviare la proposta
        // bottone per inviare la proposta
        Button sendProposalButton = new Button("Invia proposta!");
        sendProposalButton.addStyleName("sendProposalButton");
        chooseCardForProposalContainer.add(sendProposalButton);

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

        content.add(sendProposalButton);

        setWidget(content);
    }

    private FlowPanel chooseCardForProposalContainerCreator() {
        FlowPanel chooseCardForProposalContainer = new FlowPanel();
        chooseCardForProposalContainer.addStyleName("chooseCardForProposalContainer");

        Label chooseCardForProposalLabel = new Label(
                "Scegli una o piu' carte da proporre in cambio di " + cardLoggedUserWant.getName());
        chooseCardForProposalLabel.addStyleName("chooseCardForProposalLabel");
        chooseCardForProposalContainer.add(chooseCardForProposalLabel);

        Label chooseCardForProposalLabel1 = new Label(
                "Puoi scegliere tra tutte le carte che possiedi, anche se di giochi diversi!");
        chooseCardForProposalLabel1.addStyleName("chooseCardForProposalLabel1");
        chooseCardForProposalContainer.add(chooseCardForProposalLabel1);

        Label chooseCardForProposalLabel2 = new Label(
                "Puoi anche non proporre alcuna carta, ma siamo sicuri che " + counterpartyUsername
                        + " accettera'?👀");
        chooseCardForProposalLabel2.addStyleName("chooseCardForProposalLabel2");
        chooseCardForProposalContainer.add(chooseCardForProposalLabel2);

        // Itera sulle carte di loggedUser
        for (Card c : loggedUser.getOwnedCards()) {
            CheckBox cardCheckBox = new CheckBox(c.getName() + " - " + c.getCondition());
            cardCheckBox.addStyleName("cardCheckBox");

            // Associa l'oggetto Card alla CheckBox nella mappa (la chiave e' il riferimento
            // alla memoria)
            proposedCardsMap.put(cardCheckBox, c);

            chooseCardForProposalContainer.add(cardCheckBox);
        }

        return chooseCardForProposalContainer;
    }

    private FlowPanel cardsCounterpartyWantContainerCreator() {
        FlowPanel cardsCounterpartyWantContainer = new FlowPanel();
        cardsCounterpartyWantContainer.addStyleName("chooseCardForProposalContainer");

        Label cardsCounterpartyWantLabel = new Label(
                counterpartyUsername + " vuole queste carte: ");
        cardsCounterpartyWantLabel.addStyleName("cardsCounterpartyWantLabel");
        cardsCounterpartyWantContainer.add(cardsCounterpartyWantLabel);

        // mostra tutte le carte possedute dall'utente loggato con una checkbox
        FlowPanel cardsContainer = new FlowPanel();
        cardsContainer.addStyleName("cardsContainer");

        homeGameActivity.getUserByUsername(counterpartyUsername, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                // stampa le carte che la controparte desidera
                for (Card counterpartyWishedCard : user.getWishedCards()) {
                    Label cardLabel = new Label("- " + counterpartyWishedCard.getName());
                    cardLabel.addStyleName("cardLabel");
                    cardsCounterpartyWantContainer.add(cardLabel);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });

        return cardsCounterpartyWantContainer;
    }

    private FlowPanel otherCardsCounterpartyContainerCreator() {
        FlowPanel otherCardsCounterpartyContainer = new FlowPanel();
        otherCardsCounterpartyContainer.addStyleName("chooseCardForProposalContainer");

        Label otherCardsCounterpartyLabel = new Label(
                counterpartyUsername + " possiede anche queste carte");
        otherCardsCounterpartyLabel.addStyleName("otherCardsCounterpartyLabel");
        otherCardsCounterpartyContainer.add(otherCardsCounterpartyLabel);

        // mostra tutte le carte possedute dall'utente loggato con una checkbox
        FlowPanel cardsContainer = new FlowPanel();
        cardsContainer.addStyleName("cardsContainer");

        homeGameActivity.getUserByUsername(counterpartyUsername, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                int cardRecurrence = 0;
                for (Card otherOwnedCard : user.getOwnedCards()) {
                    // non mostrare la carta che l'utente loggato vuole solo una volta, se la stessa
                    // carta è presente due volte, mostrala una volta, se e' presente tre volte,
                    // mostrala due volte, ecc.

                    // in questo modo evito di mostrare la carta che l'utente loggato vuole (l'ha
                    // già selezionata avviando su di essa la proposta) ma se c'e' una carta uguale,
                    // la mostro
                    if ((otherOwnedCard.compareAttributes(cardLoggedUserWant)) && cardRecurrence == 0) {
                        cardRecurrence++;
                        continue;
                    }
                    CheckBox cardCheckBox = new CheckBox(
                            otherOwnedCard.getName() + " - " + otherOwnedCard.getCondition());
                    cardCheckBox.addStyleName("cardCheckBox");

                    requestedCardsMap.put(cardCheckBox, otherOwnedCard);

                    otherCardsCounterpartyContainer.add(cardCheckBox);

                }
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });

        return otherCardsCounterpartyContainer;
    }
}
