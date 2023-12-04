package com.dlm.gwt.sample.cardmaster.client.view.Grid;

import java.util.List;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.elements.CreateDeckModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.client.elements.ManageDeckModalPanel;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DecksGridView extends Composite implements GridViewStrategy {
    private HomeGameActivity homeGameActivity = null;
    private List<Deck> deckList = null;
    private String gameName = null;
    Panel deckPanel;

    public DecksGridView(List<?> elementList, HomeGameActivity homeGameActivity) {
        this.deckList = (List<Deck>) elementList;
        this.homeGameActivity = homeGameActivity;
        this.gameName = homeGameActivity.getGameName();
    }

    @Override
    public Widget showGrid() {
        if (deckList.isEmpty()) {
            return getPlaceholder();
        }
        showDecks();
        return this.deckPanel;
    }

    private void showDecks() {
        this.deckPanel = new VerticalPanel();
        int elementsPerRow = 5;
        int numberOfRows = (int) Math.ceil((double) this.deckList.size() / elementsPerRow);

        Grid showDecksGrid = new Grid(numberOfRows, elementsPerRow);
        showDecksGrid.setStyleName("decksGrid");
        Panel localPanel;
        int deckIndex = 0;

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < elementsPerRow; col++) {
                if (deckIndex < this.deckList.size()) {
                    Deck deck = this.deckList.get(deckIndex);
                    localPanel = new VerticalPanel();
                    localPanel.setStyleName("deckPanel");

                    Panel deckInfoPanel = new VerticalPanel();
                    Label deckName = new Label(deck.getName());
                    Label numberOfCards = new Label("Carte nel deck: " + String.valueOf(deck.getCards().size()));
                    deckName.setStyleName("deckName");
                    numberOfCards.setStyleName("numberOfCards");
                    deckInfoPanel.add(deckName);
                    deckInfoPanel.add(numberOfCards);
                    deckViewBuilder(localPanel, deck, deckInfoPanel);

                    showDecksGrid.setWidget(row, col, localPanel); // Aggiunta del pannello locale alla griglia nella
                                                                   // posizione corrispondente
                    deckIndex++;
                } else {
                    break;
                }
            }
        }

        this.deckPanel.add(showDecksGrid);
        this.deckPanel.add(getCreateDeckButton());
    }

    /**
     * Costruisce il pannello che mostra il nome del deck e i pulsanti per gestirlo
     * 
     * @param showDecksPanel => pannello che contiene tutti i deck
     * @param deck           => deck da mostrare
     * @param deckName       => nome del deck
     */
    private void deckViewBuilder(Panel showDecksPanel, Deck deck, Widget deckName) {
        showDecksPanel.add(deckName);
        showDecksPanel.add(deckButtons(deck));
    }

    private Panel deckButtons(Deck deck) {
        Panel localPanel = getDeleteDeckButton(deck, getManageDeckButton(deck));
        localPanel.setStyleName("deckButtonPanel");
        return localPanel;
    }

    private Button getManageDeckButton(Deck deck) {
        Button manageDeckButton = new Button();
        manageDeckButton.setStyleName("openDeckButton");

        manageDeckButton.addClickHandler(event -> {

            HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();

            ManageDeckModalPanel manageDeckModalPanel = new ManageDeckModalPanel(homeGameActivity.getLoggedUser(),
                    this.gameName, this.homeGameActivity, deck.getName(), hidePopup);
            manageDeckModalPanel.center();
            manageDeckModalPanel.show();

            hidePopup.initialize(manageDeckModalPanel);
        });
        return manageDeckButton;
    }

    private Panel getDeleteDeckButton(Deck deck, Button manageDeckButton) {
        Button deleteDeckButton = new Button();
        deleteDeckButton.setStyleName("deleteDeckButton");

        Panel deckButtonPanel = new HorizontalPanel();
        deckButtonPanel.add(manageDeckButton);
        deckButtonPanel.add(deleteDeckButton);

        deleteDeckButton.addClickHandler(event -> {
            homeGameActivity.deleteDeck(deck.getName());
        });

        return deckButtonPanel;
    }

    private Panel getCreateDeckButton() {
        Panel localPanel = new HorizontalPanel();
        Button createDeckButton = new Button("Crea deck");
        createDeckButton.setStyleName("addDeckButton");

        localPanel.add(createDeckButton);
        localPanel.setStyleName("createDeckContainer");

        createDeckButton.addClickHandler(event -> {
            HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
            CreateDeckModalPanel createDeckModalPanel = new CreateDeckModalPanel(homeGameActivity.getLoggedUser(),
                    this.gameName, this.homeGameActivity, hidePopup);
            createDeckModalPanel.center();
            createDeckModalPanel.show();

            hidePopup.initialize(createDeckModalPanel);
        });
        createDeckButton.setStyleName("createDeckButton");
        return localPanel;
    }

    @Override
    public Widget getPlaceholder() {
        Panel placeholderPanel = new VerticalPanel();
        Label placeholderLabel = new Label("Non ci sono deck da mostrare, creane uno!");
        placeholderLabel.setStyleName("deckPlaceholderLabel");
        placeholderPanel.setStyleName("deckPlaceholderPanel");
        placeholderPanel.add(placeholderLabel);
        placeholderPanel.add(getCreateDeckButton());
        return placeholderPanel;
    }
}