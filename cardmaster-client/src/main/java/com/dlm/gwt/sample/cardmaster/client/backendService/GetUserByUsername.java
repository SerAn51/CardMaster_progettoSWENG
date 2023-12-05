package com.dlm.gwt.sample.cardmaster.client.backendService;

import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetUserByUsername {

    private DatabaseServiceAsync databaseService;
    private String username;
   
    public GetUserByUsername(DatabaseServiceAsync databaseService, String username) {
        this.databaseService = databaseService;
        this.username = username;
    }
       
    public void getUserByUsername(AsyncCallback<User> callback) {
        databaseService.getUserByUsername(username, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.getUserByUsername: " + caught.getMessage());
                callback.onFailure(caught);
            }
        });
    }
}
