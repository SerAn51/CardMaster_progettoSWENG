package com.dlm.gwt.sample.cardmaster.client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.activity.CardViewFactory;
import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.backendService.filterer.CardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.backendService.filterer.MagicCardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.backendService.filterer.PokemonCardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.backendService.filterer.YugiohCardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.elements.CreateDeckModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.FilterCardModalPanel;
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
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class HomeGameView extends Composite {

    private CardViewFactory cardViewFactory;
    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private final DatabaseServiceAsync databaseServiceAsync = GWT.create(DatabaseService.class);
    private HomeGameActivity homeGameActivity;
    private VerticalPanel mainPanel;
    private FlexTable bodyTable;
    private CardFilterStrategy cardFilterStrategy;
    HorizontalPanel searchCardPanel;
    private Button filterCardsButton;
    private boolean searchVisible = false;
    private boolean filterVisible = false;
    // FlexTable cardsGrid;
    VerticalPanel sidebar = new VerticalPanel();
    VerticalPanel searchFilterPanel = new VerticalPanel();

    private static final int CARDS_PER_PAGE = 10;
    private static final int COLUMN_COUNT = CARDS_PER_PAGE / 2;

    private int currentPage = 0; // Pagina corrente
    String gameName = null;

    public HomeGameView(String gameName) {

        this.gameName = gameName;
        this.homeGameActivity = new HomeGameActivity(this, databaseServiceAsync, gameName);

        FlowPanel containerPanel = new FlowPanel();
        containerPanel.setStyleName("homePanel" + gameName);

        this.mainPanel = viewPanel();
        containerPanel.add(this.mainPanel);

        initWidget(containerPanel);
    }

    private VerticalPanel viewPanel() {

        VerticalPanel windowPanel = new VerticalPanel();

        /* Header */
        HeaderPanelCustom headerPanel = new HeaderPanelCustom(loggedUser, this.gameName);
        windowPanel.add(headerPanel.createHeaderPanel());

        sidebar.setStyleName("sidebar");

        createSidebar(sidebar);

        windowPanel.add(bodyTableCreator(sidebar));
        windowPanel.setStyleName("windowPanel");

        return windowPanel;
    }

    private Widget bodyTableCreator(VerticalPanel sidebar) {
        bodyTable = new FlexTable();

        bodyTable.setWidget(0, 0, sidebar);

        bodyTable.getFlexCellFormatter().setWidth(0, 0, "5%"); // show button
        bodyTable.getFlexCellFormatter().setWidth(0, 1, "95%"); // prev button
        // bodyTable.getFlexCellFormatter().setWidth(0, 2, "85%"); // card grid
        // bodyTable.getFlexCellFormatter().setWidth(0, 3, "2.5%"); // next button

        return bodyTable;
    }

    /**
     * Serve solo per evitare di avere un metodo troppo lungo e spezzare un po'
     * il codice
     * 
     * @param sidebar
     * @return
     */
    private void createSidebar(VerticalPanel sidebar) {

        sidebar.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        searchFilterPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        searchFilterPanel.setStyleName("searchFilterPanel");

        VerticalPanel buttonsPanel = new VerticalPanel();
        buttonsPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        buttonsPanel.setStyleName("buttonsPanel");

        Button showAllCardsButton = new Button("Mostra tutte");
        Button showOwnedCardsButton = new Button("Mostra owned");
        Button showWishedCardsButton = new Button("Mostra wished");
        Button showDecksButton = new Button("Mostra deck");
        Widget[] buttonsArray = { showAllCardsButton, showOwnedCardsButton, showWishedCardsButton, showDecksButton };

        showAllCardsButton.addClickHandler(event -> {
            homeGameActivity.getAllCards(this.gameName);
            updateButtonColors(showAllCardsButton, buttonsArray);
        });
        showOwnedCardsButton.addClickHandler(event -> {
            currentPage = 0;
            homeGameActivity.getOwnedOrWishedCards(this.gameName, true);
            updateButtonColors(showOwnedCardsButton, buttonsArray);
        });
        showWishedCardsButton.addClickHandler(event -> {
            currentPage = 0;
            homeGameActivity.getOwnedOrWishedCards(this.gameName, false);
            updateButtonColors(showWishedCardsButton, buttonsArray);
        });
        showDecksButton.addClickHandler(event -> {
            // se la barra di ricerca è presente, nascondila
            if (searchVisible == true) {
                searchCardPanel.setVisible(false);
                searchVisible = false;
            }
            // se il bottone per i filtri è presente, nascondilo
            if (filterVisible == true) {
                filterCardsButton.setVisible(false);
                filterVisible = false;
            }
            homeGameActivity.getDecks(this.loggedUser, this.gameName);
            updateButtonColors(showDecksButton, buttonsArray);
        });

        showAllCardsButton.setStyleName("sidebarButton");
        showOwnedCardsButton.setStyleName("sidebarButton");
        showWishedCardsButton.setStyleName("sidebarButton");
        showDecksButton.setStyleName("sidebarButton");

        // Simulo il click sul bottone "Mostra tutte" per mostrare tutte le carte
        // all'apertura della home
        ClickEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false),
                showAllCardsButton);

        buttonsPanel.add(showAllCardsButton);
        buttonsPanel.add(showOwnedCardsButton);
        buttonsPanel.add(showWishedCardsButton);
        buttonsPanel.add(showDecksButton);

        sidebar.add(buttonsPanel);
        sidebar.add(searchFilterPanel);

        sidebar.setStyleName("sidebar");
    }

    private void updateButtonColors(Widget selectedButton, Widget[] allButtons) {
        for (Widget button : allButtons) {
            if (button.equals(selectedButton)) {
                button.getElement().getStyle().setBackgroundColor("#1e87d6");
            } else {
                button.getElement().getStyle().setBackgroundColor("#3c31bb");
            }
        }
    }

    public void setHomeMagicHandler(ClickHandler handler) {
        Button homeButton = (Button) mainPanel.getWidget(4);
        homeButton.addClickHandler(handler);
    }

    public VerticalPanel getMainPanel() {
        return this.mainPanel;
    }

    /* --INIZIO FILTRI-- */

    /* ++INIZIO MOSTRA TUTTE LE CARTE CON RELATIVI PULSANTI ADD/REMOVE++ */
    public void showSearchAndFilter(List<Card> cards, String searchText, Map<String, String> selectedRadioMap) {

        if (searchText == null && selectedRadioMap == null) {
            showGrid(cards);
        }

        searchCardCreator(cards);
        filterCards(cards);

        System.out.println("SONO HomeGameView.showCards, SIZE LISTA CARDS" + cards.size());

    }

    public void setCardFilterStrategy(CardFilterStrategy strategy) {
        this.cardFilterStrategy = strategy;
    }

    public CardFilterStrategy getCardFilterStrategy() {
        return this.cardFilterStrategy;
    }

    /* --FINE FILTRI-- */

    public void showGrid(List<?> elementList) {

        /*
         * Boolean elementsAreNotThisGame = false;
         * for (Object element : elementList) {
         * if (element instanceof Card) {
         * Card card = (Card) element;
         * if (!card.getGame().equalsIgnoreCase(this.gameName)) {
         * elementsAreNotThisGame = true;
         * break;
         * }
         * } else if (element instanceof Deck) {
         * Deck deck = (Deck) element;
         * if (!deck.getGame().equalsIgnoreCase(this.gameName)) {
         * elementsAreNotThisGame = true;
         * break;
         * }
         * }
         * }
         */

        // se non ci sono carte/deck oppure le carte/deck non sono di questo gioco
        // if (elementList.size() == 0 || elementsAreNotThisGame) {
        // Label noCardsLabel = new Label("Non ci sono carte da mostrare");
        // noCardsLabel.setStyleName("noCardsLabel");
        // setInGrid(noCardsLabel);
        // } else {
        if (elementList.get(0) instanceof Card) {
            showCards((List<Card>) elementList);
        } else if (elementList.get(0) instanceof Deck) {
            showDecks((List<Deck>) elementList);
        }
        // }

    }

    public void showCards(List<Card> cards) {

        FlexTable cardsGrid = new FlexTable();
        FlexTable localCardsGrid = new FlexTable();
        cardsGrid.setStyleName("cardsFlexTable");

        int row = 0;
        int col = 0;
        int firstPage = currentPage * CARDS_PER_PAGE;
        int lastPage = Math.min(cards.size(), firstPage + CARDS_PER_PAGE);
        CardView cardView = null;


        // Aggiungi le carte alla griglia locale
        for (int i = firstPage; i < lastPage; i++) {
            Card card = cards.get(i);
            cardViewFactory = new CardViewFactory(card, this.homeGameActivity);
            cardView = cardViewFactory.createCardView();

            // Aggiungi la vista della carta alla griglia locale
            localCardsGrid.setWidget(row, col, cardView);
            localCardsGrid.setStyleName("cardsGrid");

            // Passa alla colonna successiva o alla riga successiva se la colonna è piena
            col++;
            if (col >= COLUMN_COUNT) {
                col = 0;
                row++;
            }
        }

        // bodyTable.setWidget(0, 2, cardsGrid);
        gestionePaginazione(cards, lastPage, cardsGrid);
        cardsGrid.setWidget(0, 1, localCardsGrid);
        setInGrid(cardsGrid);
        bodyTable.setStyleName("bodyTable");

    }

    public void showDecks(List<Deck> decks) {
        Panel deckPanel = new VerticalPanel();
        if (decks.size() == 0) {
            Label noDecksLabel = new Label("Non hai nessun deck");
            noDecksLabel.setStyleName("noCardsLabel");
            deckPanel.add(noDecksLabel);
        }

        int elementsPerRow = 4;
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

                    showDecksGrid.setWidget(row, col, localPanel); // Aggiunta del pannello locale alla griglia
                                                                   // nella
                                                                   // posizione corrispondente
                    deckIndex++;
                } else {
                    break;
                }
            }
        }

        deckPanel.add(showDecksGrid);
        // }
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

    private void setInGrid(Widget widget) {
        bodyTable.setWidget(0, 1, widget);
        bodyTable.setStyleName("bodyTable");
    }

    private void gestionePaginazione(List<?> cards, int lastPage, FlexTable cardsGrid) {

        Button prevPageButton = new Button();
        Button nextPageButton = new Button();
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
                (currentPage + 1) + " di " + (((cards.size() + CARDS_PER_PAGE - 1) /
                        CARDS_PER_PAGE)));
        pageCounterLabel.setStyleName("pageCounterLabel");

        prevPageButton.setStyleName("paginationButtonPrev");

        VerticalPanel paginationPanel = new VerticalPanel();
        paginationPanel.add(nextPageButton);
        paginationPanel.add(pageCounterLabel);
        nextPageButton.setStyleName("paginationButtonNext");

        cardsGrid.setWidget(0, 0, prevPageButton);
        cardsGrid.setWidget(0, 2, paginationPanel);
    }

    // se la carta è stata aggiunta, nascondere il bottone per aggiungerla e
    // mostrare quello per rimuoverla

    /*--FINE MOSTRA TUTTE LE CARTE CON RELATIVI PULSANTI ADD/REMOVE--*/

    /* ++RICERCA/FILTRI CARTE++ */
    public Widget searchCardCreator(List<Card> cards) {

        if (searchVisible == true) {
            // rende invisibile il vecchio campo di ricerca
            searchCardPanel.setVisible(false);
        }

        searchCardPanel = new HorizontalPanel();
        searchCardPanel.setStyleName("searchBox");

        TextBox searchCardTextBox = new TextBox();
        searchCardTextBox.setStyleName("searchInput");
        searchCardTextBox.getElement().setPropertyString("placeholder", "Ricerca carta...");
        Button searchCardButton = new Button();
        // Inserisci il tuo SVG come HTML nel pulsante
        HTML svgHtml = new HTML(
                "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M15.7955 15.8111L21 21M18 10.5C18 14.6421 14.6421 18 10.5 18C6.35786 18 3 14.6421 3 10.5C3 6.35786 6.35786 3 10.5 3C14.6421 3 18 6.35786 18 10.5Z\" stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>");
        // Aggiungi l'HTML al pulsante
        searchCardButton.getElement().appendChild(svgHtml.getElement());
        searchCardButton.setStyleName("searchButton");

        searchCardPanel.add(searchCardTextBox);
        searchCardPanel.add(searchCardButton);

        searchCardButton.addClickHandler(event -> {
            // Al clic sul pulsante, prendi il testo dalla TextBox
            String searchText = searchCardTextBox.getText();
            // Ora posso utilizzare la variabile 'searchText' per passarla a showCards che
            // mostrerà solo le carte che nel nome contengono searchText
            homeGameActivity.getCardByName(cards, searchText);
        });

        // se la barra di ricerca non è presente aggiungila, altrimenti vuol dire che
        // gia' c'e' quindi non aggiungerla
        searchFilterPanel.add(searchCardPanel);
        searchVisible = true;

        return searchCardPanel;
    }

    public Widget filterCards(List<Card> cards) {

        if (filterVisible == true) {
            // rende invisibile il vecchio bottone
            filterCardsButton.setVisible(false);
        }

        HorizontalPanel panelWrapperFilterButtonElements = new HorizontalPanel();
        panelWrapperFilterButtonElements.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HTML imageContainerFilterButton = new HTML("<div class='filterButtonImage'></div>");
        imageContainerFilterButton.setStyleName("filterButtonImage");
        Label filterLabel = new Label("Filtra");
        filterLabel.setStyleName("filterLabel");
        panelWrapperFilterButtonElements.add(imageContainerFilterButton);
        panelWrapperFilterButtonElements.add(filterLabel);
        this.filterCardsButton = new Button();
        filterCardsButton.setStyleName("filterCardsButton");
        filterCardsButton.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
        filterCardsButton.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
        filterCardsButton.getElement().getStyle().setLineHeight(1.0, Unit.EM);
        filterCardsButton.getElement().appendChild(panelWrapperFilterButtonElements.getElement());

        filterCardsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Imposta la strategia in base al gioco corrente
                if ("magic".equalsIgnoreCase(gameName)) {
                    setCardFilterStrategy(new MagicCardFilterStrategy());
                } else if ("pokemon".equalsIgnoreCase(gameName)) {
                    setCardFilterStrategy(new PokemonCardFilterStrategy());
                } else if ("yugioh".equalsIgnoreCase(gameName)) {
                    setCardFilterStrategy(new YugiohCardFilterStrategy());
                }
                showFilterPopup(cards);
            }
        });

        searchFilterPanel.add(filterCardsButton);
        filterVisible = true;

        return filterCardsButton;
    }

    private void showFilterPopup(List<Card> cards) {
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();

        FilterCardModalPanel filterCardModalPanel = new FilterCardModalPanel(this, this.gameName, cards,
                this.homeGameActivity,
                hidePopup);
        filterCardModalPanel.center();
        filterCardModalPanel.show();

        hidePopup.initialize(filterCardModalPanel);
    }

    /*--FINE RICERCA/FILTRI CARTE--*/
}