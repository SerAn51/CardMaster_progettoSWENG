package com.dlm.gwt.sample.cardmaster.client.view;

import java.util.List;

import com.dlm.gwt.sample.cardmaster.client.activity.ExchangeActivity;
import com.dlm.gwt.sample.cardmaster.client.cardLittleInfoDisplayBuilder.LittleCardDisplayUtil;
import com.dlm.gwt.sample.cardmaster.client.elements.HeaderPanelCustom;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.ExchangeProposal;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExchangeView extends Composite {

    private VerticalPanel mainPanel;
    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private final DatabaseServiceAsync databaseServiceAsync = GWT.create(DatabaseService.class);
    ExchangeActivity exchangeActivity;

    public ExchangeView() {

        this.exchangeActivity = new ExchangeActivity(this, databaseServiceAsync);

        // questa e' la pagina in se
        FlowPanel containerPanel = new FlowPanel();
        containerPanel.setStyleName("homePanel");

        mainPanel = exchangePanel();
        containerPanel.add(mainPanel);

        // Aggiungere il containerPanel alla RootLayoutPanel
        RootLayoutPanel.get().add(containerPanel);

        // Imposta il widget principale come contenuto del Composite
        initWidget(containerPanel);
    }

    private VerticalPanel exchangePanel() {
        VerticalPanel panel = new VerticalPanel();
        HeaderPanelCustom headerPanel = new HeaderPanelCustom("");
        panel.add(headerPanel.createHeaderPanel());

        Label exchangeWelcomeLabel = null;

        Label emojiLabel = null;
        Boolean thereAreExchanges = loggedUser.getExchangeProposals().size() > 0;
        if (thereAreExchanges) {
            exchangeWelcomeLabel = new Label(
                    "Hai " + loggedUser.getExchangeProposals().size() + " propost"
                            + (loggedUser.getExchangeProposals().size() == 1 ? "a" : "e") + " di scambio!");
            exchangeWelcomeLabel.setStyleName("exchangeWelcomeLabelWithExchanges");
            panel.add(exchangeWelcomeLabel);
            Panel receivedExchangePanel = buildExchangePanel();
            panel.add(receivedExchangePanel);
        } else {
            exchangeWelcomeLabel = new Label("Non hai proposte di scambio");
            emojiLabel = new Label("🥱");
            exchangeWelcomeLabel.setStyleName("exchangeWelcomeLabelWithoutExchanges");
            emojiLabel.setStyleName("emojiExchangesLabel");
            panel.add(exchangeWelcomeLabel);
        }
        if (!thereAreExchanges) {
            panel.add(emojiLabel);
        }

        return panel;
    }

    private Panel buildExchangePanel() {
        Panel exchangePanel = new HorizontalPanel();
        exchangePanel.setStyleName("exchangePanel");

        for (ExchangeProposal exchangeProposal : loggedUser.getExchangeProposals()) {

            Panel exchangeProposalPanel = new FlowPanel();
            exchangeProposalPanel.setStyleName("exchangeProposalPanel");

            // Mostra le informazioni dello scambio
            Label proponentLabel = new Label("Utente proponente: " + exchangeProposal.getProponent().getUsername());
            proponentLabel.setStyleName("labelTitle");
            exchangeProposalPanel.add(proponentLabel);

            List<Card> requestedCards = exchangeProposal.getrequestedCards();
            Label requestedCardLabel = new Label((requestedCards.size() > 1 ? "Carte richieste" : "Carta richiesta")
                    + ":");
            requestedCardLabel.setStyleName("labelTitle");
            exchangeProposalPanel.add(requestedCardLabel);
            for (Card requestedCard : requestedCards) {
                Panel cardDetailsPanel = new FlowPanel();
                cardDetailsPanel = createCardPanel(requestedCard);
                // cardDetailsPanel.setStyleName("cardDetailsPanel");
                exchangeProposalPanel.add(cardDetailsPanel);
            }

            List<Card> proposedCards = exchangeProposal.getProposedCards();

            if (proposedCards.size() == 0) {
                Label proposedCardLabel = new Label("Carte proposte: nessuna");
                proposedCardLabel.setStyleName("label");
                exchangeProposalPanel.add(proposedCardLabel);
            } else {
                Label proposedCardLabel = new Label((proposedCards.size() > 1 ? "Carte proposte" : "Carta proposta")
                        + ":");
                proposedCardLabel.setStyleName("labelTitle");
                exchangeProposalPanel.add(proposedCardLabel);
                for (Card proposedCard : proposedCards) {
                    Panel proposedCardInfoPanel = new FlowPanel();
                    proposedCardInfoPanel = createCardPanel(proposedCard);
                    // proposedCardInfoPanel.setStyleName("cardDetailsPanel");
                    exchangeProposalPanel.add(proposedCardInfoPanel);
                }
            }

            // Mostra i bottoni per accettare/rifiutare lo scambio
            Panel buttonsPanel = new HorizontalPanel();
            buttonsPanel.setStyleName("acceptDeclineButtonsPanel");
            Button acceptButton = new Button("Accetta");
            acceptButton.setStyleName("acceptButton");
            acceptButton.addClickHandler(event -> {
                // al click effettua lo scambio di carte e rimuove la proposta di scambio dalla
                // lista di proposte di scambio e forza l'aggiornamento della pagina
                exchangeActivity.acceptProposal(exchangeProposal);
            });

            Button declineButton = new Button("Rifiuta");
            declineButton.setStyleName("declineButton");
            declineButton.addClickHandler(event -> {
                // al click rimuove la proposta di scambio dalla lista di proposte di scambio e
                // forza l'aggiornamento della pagina
                exchangeActivity.declineProposal(exchangeProposal);
            });

            buttonsPanel.add(acceptButton);
            buttonsPanel.add(declineButton);

            exchangeProposalPanel.add(buttonsPanel);

            exchangePanel.add(exchangeProposalPanel);

        }

        return exchangePanel;
    }

    private HorizontalPanel createCardPanel(Card card) {
        LittleCardDisplayUtil cardDisplayUtil = new LittleCardDisplayUtil(card);
        return cardDisplayUtil.createCardPanel();
    }

}
