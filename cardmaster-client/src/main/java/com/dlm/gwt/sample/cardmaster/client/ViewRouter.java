package com.dlm.gwt.sample.cardmaster.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

import com.dlm.gwt.sample.cardmaster.client.view.LoginView;

//Questa classe gestisce la navigazione tra view (reindirizzamenti del sito o navigazione con frecce browser built-in)
public class ViewRouter implements ValueChangeHandler<String> {

    public ViewRouter() {
        History.addValueChangeHandler(this);
    }

    public void init() {
        History.fireCurrentHistoryState();
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String currentToken = History.getToken();
        String previousToken = event.getValue();

        if (currentToken.equals(previousToken)) {
            // Tasto indietro del browser premuto
            // History.back();
        } else {
            // Cambio di pagina normale
            handleRouteChange(currentToken);
            // History.back();

        }
    }

    // TODO: accorpare metodi nello switch case
    public void handleRouteChange(String token) {
        switch (token) {
            case "login":
                toLoginPage();
                break;
            case "signUp":
                // toSignUpPage();
                break;
            case "home":
                // toHome();
                break;
            // case "magicHome":
            // toMagicHome();
            // break;
            // case "pokemonHome":
            // toPokemonHome();
            // break;
            // case "yugiohHome":
            // toYugiohHome();
            // break;
        }
    }

    private void toLoginPage() {
        RootPanel.get().clear();
        LoginView loginPage = new LoginView();
        RootPanel.get().add(loginPage);
    }

    // private void toSignUpPage() {
    // RootPanel.get().clear();
    // SignUpView signUpPage = new SignUpView();
    // RootPanel.get().add(signUpPage);
    // }

    // private void toHome() {
    // RootPanel.get().clear();
    // HomeView homePage = new HomeView();
    // RootPanel.get().add(homePage);
    // }
    // private void toMagicHome() {
    // RootPanel.get().clear();
    // HomeMagicView magicHomePage = new HomeMagicView();
    // RootPanel.get().add(magicHomePage);
    // }

    // private void toPokemonHome() {
    // RootPanel.get().clear();
    // HomePokemonView pokemonHomePage = new HomePokemonView();
    // RootPanel.get().add(pokemonHomePage);
    // }

    // private void toYugiohHome() {
    // RootPanel.get().clear();
    // HomeYugiohView yugiohHomePage = new HomeYugiohView();
    // RootPanel.get().add(yugiohHomePage);
    // }

}
