package com.dlm.gwt.sample.cardmaster.client.view;

import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.elements.HeaderPanelCustom;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;

public class HomeView extends Composite {

    private ViewRouter viewRouter = new ViewRouter();
    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private VerticalPanel mainPanel;

    /*
     * setStyleName("homePanel")
     * setStyleName("homeWelcomeLabel")
     * setStyleName("homeChooseGameLabel")
     * setStyleName("homeChooseGamePanel")
     * setStyleName("homeChooseMagicBtn")
     * setStyleName("homeChoosePokemonBtn")
     * setStyleName("homeChooseYugiohBtn")
     * 
     * 
     * 
     */

    public HomeView() {
        // questa e' la pagina in se
        FlowPanel containerPanel = new FlowPanel();
        containerPanel.setStyleName("homePanel");

        mainPanel = homePanel();
        containerPanel.add(mainPanel);

        // Aggiungere il containerPanel alla RootLayoutPanel
        RootLayoutPanel.get().add(containerPanel);

        // Imposta il widget principale come contenuto del Composite
        initWidget(containerPanel);
    }

    public VerticalPanel homePanel() {
        VerticalPanel containerPanel = new VerticalPanel();
        VerticalPanel mainPanel = new VerticalPanel();

        HeaderPanelCustom headerPanel = new HeaderPanelCustom(loggedUser, "");
        mainPanel.add(headerPanel.createHeaderPanel());

        // Label con il titolo "Scegli il gioco"
        Label welcomeLabel = new Label("Ciao " + loggedUser.getUsername() + "!");
        welcomeLabel.setStyleName("homeWelcomeLabel");
        mainPanel.add(welcomeLabel);
        Label chooseGameLabel = new Label("Scegli il gioco!");
        chooseGameLabel.setStyleName("homeChooseGameLabel");
        mainPanel.add(chooseGameLabel);

        // tre bottoni per scegliere il gioco
        HorizontalPanel chooseGamePanel = new HorizontalPanel();
        chooseGamePanel.setStyleName("homeChooseGamePanel");
        Button magicButton = createButton("");
        magicButton.setStyleName("homeChooseMagicBtn");
        Button pokemonButton = createButton("");
        pokemonButton.setStyleName("homeChoosePokemonBtn");
        Button yugiohButton = createButton("");
        yugiohButton.setStyleName("homeChooseYugiohBtn");
        chooseGamePanel.add(magicButton);
        chooseGamePanel.add(pokemonButton);
        chooseGamePanel.add(yugiohButton);
        mainPanel.add(chooseGamePanel);

        magicButton.addClickHandler(event -> chooseGameButtonClick("magicHome"));
        pokemonButton.addClickHandler(event -> chooseGameButtonClick("pokemonHome"));
        yugiohButton.addClickHandler(event -> chooseGameButtonClick("yugiohHome"));

        containerPanel.add(mainPanel);

        return mainPanel;
    }

    private Button createButton(String btnName) {
        Button btn = new Button(btnName);
        return btn;
    }

    private void chooseGameButtonClick(String token) {
        // apri la pagina di gestione delle carte di magic/pokemon/yugioh
        History.newItem(token);
        viewRouter.handleRouteChange(token);// vai a vedere la url perche' ho cambiato il token
    }
}