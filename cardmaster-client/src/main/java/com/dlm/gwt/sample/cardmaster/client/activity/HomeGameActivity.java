package com.dlm.gwt.sample.cardmaster.client.activity;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.view.HomeGameView;

import java.util.List;

public class HomeGameActivity extends AbstractActivity {

    private final HomeGameView view;
    private final DatabaseServiceAsync databaseService;
    String gameName;

    public HomeGameActivity(HomeGameView view, DatabaseServiceAsync databaseService, String gameName) {
        this.view = view;
        this.databaseService = databaseService;
        this.gameName = gameName;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        containerWidget.setWidget(view);
    }

    public void toHome() {
        // apri la pagina di gestione delle carte di magic/pokemon/yugioh
        String token = "home";
        History.newItem(token);
        new ViewRouter().handleRouteChange(token);// vai a vedere la url perche' ho cambiato il token
    }

    public void getCards(String gameName) {
        databaseService.getCards(gameName, new AsyncCallback<List<Card>>() {
            @Override
            public void onSuccess(List<Card> cards) {
                // richiama il metodo della view che mostra le carte
                view.showGrid(cards);
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.getCards: " + caught.getMessage());
            }
        });
    }
}
