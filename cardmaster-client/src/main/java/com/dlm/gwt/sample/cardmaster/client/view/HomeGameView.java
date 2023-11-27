package com.dlm.gwt.sample.cardmaster.client.view;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.elements.HeaderPanelCustom;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class HomeGameView extends Composite {

    private User loggedUser = SessionUser.getInstance().getSessionUser();

    private final DatabaseServiceAsync databaseServiceAsync = GWT.create(DatabaseService.class);

    private HomeGameActivity homeGameActivity;

    private VerticalPanel mainPanel;

    public HomeGameView(String gameName) {
        homeGameActivity = new HomeGameActivity(this, databaseServiceAsync, gameName);

        // define ui
        FlowPanel containerPanel = new FlowPanel();
        containerPanel.setStyleName("homePanel" + gameName);

        mainPanel = cardsPanel(gameName);
        containerPanel.add(mainPanel);

        // Aggiungere il containerPanel alla RootLayoutPanel
        RootLayoutPanel.get().add(containerPanel);

        // Imposta il widget principale come contenuto del Composite
        initWidget(containerPanel);
    }

    private VerticalPanel cardsPanel(String gameName) {

        VerticalPanel panel = new VerticalPanel();
        HeaderPanelCustom headerPanel = new HeaderPanelCustom(loggedUser, gameName);
        panel.add(headerPanel.createHeaderPanel());
        Button showAllCardsBtn = new Button("Mostra tutte le carte");
        panel.add(showAllCardsBtn);

        RootPanel.get().add(panel);

        // TODO: visualizza carte
        /*
         * - Campo ricerca
         * - Visualizzare tutte
         * - Visualizzare solo le owned
         * - Visualizzare solo le wished
         */
        return panel;
    }

    public void setHomeMagicHandler(ClickHandler handler) {
        Button homeButton = (Button) mainPanel.getWidget(4);
        homeButton.addClickHandler(handler);
    }

    public VerticalPanel getMainPanel() {
        return this.mainPanel;
    }
}
