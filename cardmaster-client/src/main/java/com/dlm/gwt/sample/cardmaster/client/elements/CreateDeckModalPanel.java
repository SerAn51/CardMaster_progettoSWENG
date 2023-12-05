package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateDeckModalPanel extends PopupPanel {

    private User loggedUser = SessionUser.getInstance().getSessionUser();

    public CreateDeckModalPanel(String gameName, HomeGameActivity homeGameActivity,
            HidePopupPanelClickingOutside hidePopup) {

        // TODO: togli gli input superflui
        
        // Contenuto della finestra modale
        Panel content = new VerticalPanel();

        FlowPanel initDeckContainer = new FlowPanel();
        initDeckContainer.setStyleName("popupContainer");

        // TODO: faccio scegliere il nome del deck
        Label chooseDeckNameLabel = new Label("Scegli il nome del deck:");
        chooseDeckNameLabel.setStyleName("bigLabelPopup");
        initDeckContainer.add(chooseDeckNameLabel);

        TextBox deckNameTextBox = new TextBox();
        deckNameTextBox.getElement().setPropertyString("placeholder", "Nome del deck");
        deckNameTextBox.getElement().setAttribute("maxLength", String.valueOf(20));
        initDeckContainer.add(deckNameTextBox);

        Panel createDeckPanel = new HorizontalPanel();
        Button createDeckButton = new Button("Crea deck");
        createDeckButton.setStyleName("createDeckButton");
        createDeckPanel.add(createDeckButton);
        createDeckPanel.setStyleName("createDeckContainer");
        initDeckContainer.add(createDeckPanel);

        createDeckButton.addClickHandler(event -> {

            String deckName = deckNameTextBox.getText();

            Boolean deckNameIsValid = true;
            if (loggedUser.getDecks() != null) {
                for (Deck deck : loggedUser.getDecks().values()) {
                    if (deck.getName().equalsIgnoreCase(deckName)) {
                        deckNameIsValid = false;
                        break;
                    }
                }
            } // se e' null vuol dire che e' il primo deck

            if (deckName.isEmpty()) {
                Window.alert("Dai un nome al deck");
            } else if (deckNameIsValid == false) {
                Window.alert("Il nome del deck è già in uso");
            } else {
                homeGameActivity.createDeck(deckName);
                hide();
                hidePopup.destroy();
                homeGameActivity.getDecks();
            }
        });

        content.add(initDeckContainer);

        setWidget(content);
    }
}