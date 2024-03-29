package com.dlm.gwt.sample.cardmaster.client;

import com.dlm.gwt.sample.cardmaster.client.view.ExchangeView;
import com.dlm.gwt.sample.cardmaster.client.view.HomeGameView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

import com.dlm.gwt.sample.cardmaster.client.view.LoginView;
import com.dlm.gwt.sample.cardmaster.client.view.SignUpView;
import com.dlm.gwt.sample.cardmaster.client.view.HomeView;

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

    public void handleRouteChange(String token) {
        switch (token) {
            case "login":
                toLoginPage();
                break;
            case "signUp":
                toSignUpPage();
                break;
            case "home":
                toHome();
                break;
            case "magicHome":
                toMagicHome();
                break;
            case "pokemonHome":
                toPokemonHome();
                break;
            case "yugiohHome":
                toYugiohHome();
                break;
            case "exchange":
                toExchangePage();
                break;
        }
    }

    private void toLoginPage() {
        RootPanel.get().clear();
        LoginView loginPage = new LoginView();
        RootPanel.get().add(loginPage);
    }

    private void toSignUpPage() {
        RootPanel.get().clear();
        SignUpView signUpPage = new SignUpView();
        RootPanel.get().add(signUpPage);
    }

    private void toHome() {
        RootPanel.get().clear();
        HomeView homePage = new HomeView();
        RootPanel.get().add(homePage);
    }

    private void toMagicHome() {
        RootPanel.get().clear();
        HomeGameView magicHomePage = new HomeGameView("Magic");
        RootPanel.get().add(magicHomePage);
    }

    private void toPokemonHome() {
        RootPanel.get().clear();
        HomeGameView pokemonHomePage = new HomeGameView("Pokemon");
        RootPanel.get().add(pokemonHomePage);
    }

    private void toYugiohHome() {
        RootPanel.get().clear();
        HomeGameView yugiohHomePage = new HomeGameView("Yugioh");
        RootPanel.get().add(yugiohHomePage);
    }

    private void toExchangePage() {
        RootPanel.get().clear();
        ExchangeView exchangePage = new ExchangeView();
        RootPanel.get().add(exchangePage);
    }

}
