package com.dlm.gwt.sample.cardmaster.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.view.SignUpView;
import com.dlm.gwt.sample.cardmaster.shared.services.SignUpServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SignUpActivity extends AbstractActivity {

    private final SignUpView view;
    private final SignUpServiceAsync signupService;

    public SignUpActivity(SignUpView view, SignUpServiceAsync signupService) {
        this.view = view;
        this.signupService = signupService;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        try {
            containerWidget.setWidget(view);

            // Aggiungi gli handler degli eventi necessari alla vista
            view.setSignUpHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    // Chiama il metodo signUp
                    signUp();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signUp() {
        String username = view.getUsername();
        String email = view.getEmail();
        String password = view.getPassword();
        String secondPsw = view.getSecondPassword();
        String age = view.getAge();
        String gender = view.getGender();

        if (username.isEmpty()) {
            Window.alert("Inserisci un username");
            return;
        }
        if (!password.equals(secondPsw)) {
            Window.alert("Le password non sono le stesse");
            return;
        }
        if (!email.contains("@") || !(email.contains(".it") || email.contains(".com"))) {
            Window.alert("Inserisci una mail valida");
            return;
        }
        if (age.isEmpty()) {
            Window.alert("Inserisci un'eta'");
            return;
        }
        if (gender.isEmpty()) {
            Window.alert("Inserisci un genere");
            return;
        }

        // Crea un nuovo utente in ram
        User newUser = new User(username, email, password, Integer.parseInt(view.getAge()), view.getGender());

        signupService.signUp(username, newUser, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    // Login avvenuto con successo
                    Window.alert("Registrazione avvenuta con successo!");
                    SessionUser.getInstance().setSessionUser(newUser);
                    String token = "home";
                    History.newItem(token);
                    new ViewRouter().handleRouteChange(token);
                } else {
                    // Registration failed
                    Window.alert("Informazioni non valide");
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio di login
                Window.alert("Errore durante la registrazione: " + caught.getMessage());
            }
        });
    }

    public void goBack() {
        String token = "login";
        History.newItem(token);
        new ViewRouter().handleRouteChange(token);
    }

}
