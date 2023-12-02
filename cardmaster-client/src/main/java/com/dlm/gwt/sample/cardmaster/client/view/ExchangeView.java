package com.dlm.gwt.sample.cardmaster.client.view;

import java.util.List;

import com.dlm.gwt.sample.cardmaster.client.activity.ExchangeActivity;
import com.dlm.gwt.sample.cardmaster.client.elements.HeaderPanelCustom;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.ExchangeProposal;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//TODO: fix windpw panel
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
        HeaderPanelCustom headerPanel = new HeaderPanelCustom(loggedUser, "");
        panel.add(headerPanel.createHeaderPanel());

        Label exchangeWelcomeLabel = null;

        Label emojiLabel = null;
        Boolean thereAreExchanges = loggedUser.getExchangeProposals().size() > 0;
        if (thereAreExchanges) {
            exchangeWelcomeLabel = new Label(
                    "Hai " + loggedUser.getExchangeProposals().size() + " proposte di scambio!");
            exchangeWelcomeLabel.setStyleName("exchangeWelcomeLabelWithExchanges");
            panel.add(exchangeWelcomeLabel);
            VerticalPanel receivedExchangePanel = buildExchangePanel();
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

    private VerticalPanel buildExchangePanel() {
        VerticalPanel exchangePanel = new VerticalPanel();
        exchangePanel.setStyleName("exchangePanel");

        Label exchangeRecivedTitleLabel = new Label("Proposte di scambio ricevute");
        exchangeRecivedTitleLabel.setStyleName("exchangeRecivedTitleLabel");

        exchangePanel.add(exchangeRecivedTitleLabel);

        for (ExchangeProposal exchangeProposal : loggedUser.getExchangeProposals()) {

            VerticalPanel exchangeProposalPanel = new VerticalPanel();
            exchangeProposalPanel.setStyleName("exchangeProposalPanel");

            // Mostra le informazioni dello scambio
            Label proponentLabel = new Label("Utente proponente: " + exchangeProposal.getProponent().getUsername());
            exchangeProposalPanel.add(proponentLabel);

            List<Card> requestedCards = exchangeProposal.getrequestedCards();
            Label requestedCardLabel = new Label((requestedCards.size() > 1 ? "Carte richieste" : "Carta richiesta")
                    + ":");
            exchangeProposalPanel.add(requestedCardLabel);
            for (Card requestedCard : requestedCards) {
                Label requestedCardInfoLabel = new Label("- " + getCardEssentialInformation(requestedCard));
                exchangeProposalPanel.add(requestedCardInfoLabel);
            }

            List<Card> proposedCards = exchangeProposal.getProposedCards();

            if (proposedCards.size() == 0) {
                Label proposedCardLabel = new Label("Carte proposte: nessuna");
                exchangeProposalPanel.add(proposedCardLabel);
            } else {
                Label proposedCardLabel = new Label((proposedCards.size() > 1 ? "Carte proposte" : "Carta proposta")
                        + ":");
                exchangeProposalPanel.add(proposedCardLabel);
                for (Card proposedCard : proposedCards) {
                    Label proposedCardInfoLabel = new Label("- " + getCardEssentialInformation(proposedCard));
                    exchangeProposalPanel.add(proposedCardInfoLabel);
                }
            }

            // Mostra i bottoni per accettare/rifiutare lo scambio
            HorizontalPanel buttonsPanel = new HorizontalPanel();
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

    private String getCardEssentialInformation(Card card) {
        return card.getName() + "(" + card.getGame() + ")" + " [" + card.getCondition() + " - "
                + (card.getConditionDescription().isEmpty() ? "nessuna descrizione fornita"
                        : card.getConditionDescription())
                + "]";
    }

}
