package com.dlm.gwt.sample.cardmaster.client.backendService;

import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Singleton class to save changes in DB
 */
public class SaveChangesInDBSingleton {

    private DatabaseServiceAsync databaseService;
    private User user;

    public SaveChangesInDBSingleton(DatabaseServiceAsync databaseService, User user) {
        this.databaseService = databaseService;
        this.user = user;
    }

    public void saveChangesInDB() {
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
}
