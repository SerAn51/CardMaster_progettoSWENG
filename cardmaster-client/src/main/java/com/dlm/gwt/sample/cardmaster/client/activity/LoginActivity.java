package com.dlm.gwt.sample.cardmaster.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.view.LoginView;
import com.dlm.gwt.sample.cardmaster.shared.services.LoginServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

public class LoginActivity extends AbstractActivity {

    private final LoginView view;
    private final LoginServiceAsync loginService;

    public LoginActivity(LoginView view, LoginServiceAsync loginService) {
        this.view = view;
        this.loginService = loginService;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        containerWidget.setWidget(view);

        // Aggiungi gli handler degli eventi necessari alla vista
        view.setLoginHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Ottieni l'email e la password dalla vista
                String email = view.getEmail();
                String password = view.getPassword();
                // Chiama il metodo doLogin con i parametri ottenuti
                doLogin(email, password);
            }
        });

    }

    public void doLogin(String email, String password) {
        loginService.login(email, password, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User loggedUser) {
                if (loggedUser != null) {
                    // Login avvenuto con successo
                    Window.alert("Access granted");
                    SessionUser.getInstance().setSessionUser(loggedUser);
                    String token = "home";
                    History.newItem(token);
                    new ViewRouter().handleRouteChange(token);
                    // Esegui azioni per la navigazione o l'aggiornamento della vista
                } else {
                    // Login fallito
                    Window.alert("Invalid credentials");
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio di login
                Window.alert("Errore durante il login: " + caught.getMessage());
            }
        });
    }

}