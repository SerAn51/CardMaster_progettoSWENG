package com.dlm.gwt.sample.cardmaster.client.handlers;

import java.util.List;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ExchangeHandler {

    private DatabaseServiceAsync databaseService;

    public ExchangeHandler(DatabaseServiceAsync databaseService) {
        this.databaseService = databaseService;

    }

    public void sendExchangeProposal(User loggedUser, String counterparty, List<Card> proposedCards,
            List<Card> requestedCards) {
                databaseService.sendExchangeProposal(loggedUser, counterparty, proposedCards, requestedCards,
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
    
}
