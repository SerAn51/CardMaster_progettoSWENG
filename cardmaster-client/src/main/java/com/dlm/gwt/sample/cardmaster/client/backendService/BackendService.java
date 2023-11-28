package com.dlm.gwt.sample.cardmaster.client.backendService;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

public class BackendService {
    // Database utile per le prossime task 
    private final DatabaseServiceAsync databaseService;

    // TODO: suddividere BackendService in più classi: una per le operazioni sulle
    // carte, una per le operazioni sui deck, una per le operazioni di filtro
    public BackendService(DatabaseServiceAsync databaseService) {
        this.databaseService = databaseService;
    }

    public void addCardToUserOwnedOrWished(User user, Card card, Boolean isOwnedOrWished) {
        if (isOwnedOrWished) {
            user.addOwnedCard(card);
        } else {
            user.addWishedCard(card);
        }
    }

}
