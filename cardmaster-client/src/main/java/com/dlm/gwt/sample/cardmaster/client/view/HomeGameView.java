package com.dlm.gwt.sample.cardmaster.client.view;

import java.util.List;

import com.dlm.gwt.sample.cardmaster.client.activity.CardViewFactory;
import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.elements.CreateDeckModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HeaderPanelCustom;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.client.elements.ManageDeckModalPanel;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
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
    private static final int CARDS_PER_PAGE = 10;
    private static final int COLUMN_COUNT = CARDS_PER_PAGE / 2;
    private int currentPage = 0; // Pagina corrente
    String gameName = null;
    FlexTable cardsGrid;
    private CardViewFactory cardViewFactory;
    private FlexTable bodyTable;
    Button addOwnedButton;
    Button addWishedButton;

    public HomeGameView(String gameName) {
        this.gameName = gameName;
        this.homeGameActivity = new HomeGameActivity(this, databaseServiceAsync, gameName);

        // define ui
        FlowPanel containerPanel = new FlowPanel();
        containerPanel.setStyleName("homePanel" + gameName);

        this.mainPanel = viewPanel();
        containerPanel.add(this.mainPanel);

        // Imposta il widget principale come contenuto del Composite
        initWidget(containerPanel);
    }

    private VerticalPanel viewPanel() {

        VerticalPanel windowPanel = new VerticalPanel();

        /* Header */
        HeaderPanelCustom headerPanel = new HeaderPanelCustom(loggedUser, this.gameName);
        windowPanel.add(headerPanel.createHeaderPanel());

        /* Bottoni per mostrare le sezioni delle carte */
        Panel sidebar = new VerticalPanel();

        sidebar.setStyleName("sidebar");

        createSidebar(sidebar);

        windowPanel.add(bodyTableCreator(sidebar));
        windowPanel.setStyleName("windowPanel");

        return windowPanel;
    }

    private Widget bodyTableCreator(Panel sidebar) {
        this.bodyTable = new FlexTable();

        this.bodyTable.setWidget(0, 0, sidebar);

        this.bodyTable.getFlexCellFormatter().setWidth(0, 0, "5%"); // show button
        this.bodyTable.getFlexCellFormatter().setWidth(0, 1, "95%"); // prev button

        return this.bodyTable;
    }

    public void showCards(List<Card> cards) {

        cardsGrid = new FlexTable();
        FlexTable localCardsGrid = new FlexTable();

        int row = 0;
        int col = 0;
        int firstPage = currentPage * CARDS_PER_PAGE;
        int lastPage = Math.min(cards.size(), firstPage + CARDS_PER_PAGE);
        CardView cardView = null;

        for (int i = firstPage; i < lastPage; i++) {
            if (cards.get(i) instanceof Card) {
                Card card = (Card) cards.get(i);
                if (card.getGame().equalsIgnoreCase(this.gameName)) {
                    cardViewFactory = new CardViewFactory(card, this.homeGameActivity);
                    cardView = cardViewFactory.createCardView();
                    localCardsGrid.setWidget(row, col, cardView);
                    localCardsGrid.setStyleName("cardsGrid");
                }
            }
            col++;
            if (col == COLUMN_COUNT) {
                col = 0;
                row++;
            }
        }

        // bodyTable.setWidget(0, 2, cardsGrid);
        gestionePaginazione(cards, lastPage);
        cardsGrid.setWidget(0, 1, localCardsGrid);
        setInGrid(cardsGrid);
        bodyTable.setStyleName("bodyTable");

    }

    private void setInGrid(Widget widget) {
        bodyTable.setWidget(0, 1, widget);
        bodyTable.setStyleName("bodyTable");
    }

    private void gestionePaginazione(List<?> cards, int lastPage) {

        Button prevPageButton = new Button("prev");
        Button nextPageButton = new Button("next");
        if (currentPage == 0) {
            prevPageButton.setEnabled(false);
        } else {
            prevPageButton.addClickHandler(event -> {
                currentPage--;
                showGrid(cards);
            });
        }

        if (lastPage >= cards.size()) {
            nextPageButton.setEnabled(false);
        } else {
            nextPageButton.addClickHandler(event -> {
                currentPage++;
                showGrid(cards);
            });
        }
        Label pageCounterLabel = new Label(
                "Pagina " + (currentPage + 1) + " di " + (((cards.size() + CARDS_PER_PAGE - 1) / CARDS_PER_PAGE)));
        pageCounterLabel.setStyleName("pageCounterLabel");

        prevPageButton.setStyleName("paginationButton");

        VerticalPanel paginationPanel = new VerticalPanel();
        paginationPanel.add(nextPageButton);
        paginationPanel.add(pageCounterLabel);
        nextPageButton.setStyleName("paginationButton");

        cardsGrid.setWidget(0, 0, prevPageButton);
        cardsGrid.setWidget(0, 2, paginationPanel);
    }

    /**
     * Crea la sidebar con i bottoni per mostrare le sezioni delle carte
     * 
     * @param sidebar
     */
    private void createSidebar(Panel sidebar) {

        // TODO: aggiungere il bottone per i filtri

        Button showAllCardsButton = new Button("Mostra tutte");
        showAllCardsButton.addClickHandler(event -> {
            homeGameActivity.getCards(this.gameName);
        });

        Button showOwnedCardsButton = new Button("Mostra owned");
        showOwnedCardsButton.addClickHandler(event -> {
            currentPage = 0;
            homeGameActivity.getOwnedOrWishedCards(this.gameName, true);
        });

        Button showWishedCardsButton = new Button("Mostra wished");
        Button showDeck = new Button("Mostra deck");
        showDeck.addClickHandler(event -> {
            homeGameActivity.getDecks(this.loggedUser, this.gameName);
        });

        // TODO: aggiungere eventi per i bottoni
        showAllCardsButton.setStyleName("sidebarButton");
        showOwnedCardsButton.setStyleName("sidebarButton");
        showWishedCardsButton.setStyleName("sidebarButton");
        showDeck.setStyleName("sidebarButton");

        sidebar.add(showAllCardsButton);
        sidebar.add(showOwnedCardsButton);
        sidebar.add(showWishedCardsButton);
        sidebar.add(showDeck);

        sidebar.setStyleName("sidebar");
    }

    public void showDecks(List<Deck> decks) {
        Panel deckPanel = new VerticalPanel();
        int elementsPerRow = 5;
        int numberOfRows = (int) Math.ceil((double) decks.size() / elementsPerRow);

        Grid showDecksGrid = new Grid(numberOfRows, elementsPerRow);
        showDecksGrid.setStyleName("decksGrid");
        VerticalPanel localPanel;
        int deckIndex = 0;

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < elementsPerRow; col++) {
                if (deckIndex < decks.size()) {
                    Deck deck = decks.get(deckIndex);
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

        deckPanel.add(showDecksGrid);
        deckPanel.add(getCreateDeckButton());
        setInGrid(deckPanel); // Aggiungi il pannello del pulsante alla griglia
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

            ManageDeckModalPanel manageDeckModalPanel = new ManageDeckModalPanel(this.loggedUser,
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
            CreateDeckModalPanel createDeckModalPanel = new CreateDeckModalPanel(this.loggedUser,
                    this.gameName, this.homeGameActivity, hidePopup);
            createDeckModalPanel.center();
            createDeckModalPanel.show();

            hidePopup.initialize(createDeckModalPanel);
        });
        createDeckButton.setStyleName("createDeckButton");
        return localPanel;
    }

    public void setHomeMagicHandler(ClickHandler handler) {
        Button homeButton = (Button) mainPanel.getWidget(4);
        homeButton.addClickHandler(handler);
    }

    public void showGrid(List<?> elementList) {

        if (elementList.get(0) instanceof Card) {
            showCards((List<Card>) elementList);
        } else if (elementList.get(0) instanceof Deck) {
            showDecks((List<Deck>) elementList);
        }

    }
}
