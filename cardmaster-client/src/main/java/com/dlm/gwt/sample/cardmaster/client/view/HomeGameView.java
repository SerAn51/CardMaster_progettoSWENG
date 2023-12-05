package com.dlm.gwt.sample.cardmaster.client.view;

import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy.CardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy.MagicCardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy.PokemonCardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy.YugiohCardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.elements.FilterCardModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HeaderPanelCustom;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.client.utils.ElementType;
import com.dlm.gwt.sample.cardmaster.client.view.Grid.CardsGridView;
import com.dlm.gwt.sample.cardmaster.client.view.Grid.DecksGridView;
import com.dlm.gwt.sample.cardmaster.client.view.Grid.GridViewStrategy;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class HomeGameView extends Composite {

    private final DatabaseServiceAsync databaseServiceAsync = GWT.create(DatabaseService.class);
    private HomeGameActivity homeGameActivity;
    private VerticalPanel mainPanel;
    private FlexTable bodyTable;
    private CardFilterStrategy cardFilterStrategy;
    HorizontalPanel searchCardPanel;
    private Button filterCardsButton;
    private boolean searchVisible = false;
    private boolean filterVisible = false;
    VerticalPanel sidebar = new VerticalPanel();
    VerticalPanel searchFilterPanel = new VerticalPanel();

    String gameName = null;

    public HomeGameView(String gameName) {

        this.gameName = gameName;
        this.homeGameActivity = new HomeGameActivity(this, gameName, databaseServiceAsync);

        FlowPanel containerPanel = new FlowPanel();
        containerPanel.setStyleName("homePanel" + gameName);

        this.mainPanel = viewPanel();
        containerPanel.add(this.mainPanel);

        initWidget(containerPanel);
    }

    private VerticalPanel viewPanel() {

        VerticalPanel windowPanel = new VerticalPanel();

        /* Header */
        HeaderPanelCustom headerPanel = new HeaderPanelCustom(this.gameName);
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

        bodyTable.getFlexCellFormatter().setWidth(0, 0, "5%");
        bodyTable.getFlexCellFormatter().setWidth(0, 1, "95%");

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
            homeGameActivity.getAllCards();
            updateButtonColors(showAllCardsButton, buttonsArray);
        });
        showOwnedCardsButton.addClickHandler(event -> {
            homeGameActivity.getOwnedOrWishedCards(true);
            updateButtonColors(showOwnedCardsButton, buttonsArray);
        });
        showWishedCardsButton.addClickHandler(event -> {
            homeGameActivity.getOwnedOrWishedCards(false);
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
            homeGameActivity.getDecks();
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

    /* --INIZIO FILTRI-- */

    /* ++INIZIO MOSTRA TUTTE LE CARTE CON RELATIVI PULSANTI ADD/REMOVE++ */
    public void showSearchAndFilter(List<Card> cards, String searchText, Map<String, String> selectedRadioMap) {

        if (searchText == null && selectedRadioMap == null) {
            showGrid(cards, ElementType.CARDS);
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

    public void showGrid(List<?> elementList, ElementType elementType) {

        if (ElementType.CARDS.equals(elementType)) {
            GridViewStrategy gridViewStrategy = new CardsGridView(elementList, this.homeGameActivity);
            setInGrid(gridViewStrategy.showGrid());
            // showCards((List<Card>) elementList);
        } else if (ElementType.DECKS.equals(elementType)) {
            GridViewStrategy gridViewStrategy = new DecksGridView(elementList, this.homeGameActivity);
            setInGrid(gridViewStrategy.showGrid());
            // showDecks((List<Deck>) elementList);
        }
        // }

    }

    public void setInGrid(Widget widget) {
        bodyTable.setWidget(0, 1, widget);
        bodyTable.setStyleName("bodyTable");
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
                "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M15.7955 15.8111L21 21M18 10.5C18 14.6421 14.6421 18 10.5 18C6.35786 18 3 14.6421 3 10.5C3 6.35786 6.35786 3 10.5 3C14.6421 3 18 6.35786 18 10.5Z\" stroke=\"#ffffff\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>");
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

    public String getGameName() {
        return this.gameName;
    }

}