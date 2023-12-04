package com.dlm.gwt.sample.cardmaster.client.view.Grid;

import java.util.List;

import com.dlm.gwt.sample.cardmaster.client.activity.CardViewFactory;
import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CardsGridView extends Composite implements GridViewStrategy {

    private CardViewFactory cardViewFactory;
    private int currentPage = 0;
    private HomeGameActivity homeGameActivity = null;
    private List<Card> cards = null;
    private String gameName = null;
    FlexTable cardsGrid;

    private static final int CARDS_PER_PAGE = 10;
    private static final int COLUMN_COUNT = CARDS_PER_PAGE / 2;

    public CardsGridView(List<?> elementList, HomeGameActivity homeGameActivity) {
        this.cards = (List<Card>) elementList;
        this.homeGameActivity = homeGameActivity;
        this.gameName = homeGameActivity.getGameName();
    }

    @Override
    public Widget showGrid() {
        if (cards.isEmpty()) {
            return getPlaceholder();
        }
        showCards();
        return this.cardsGrid;
    }

    private void showCards() {
        this.cardsGrid = new FlexTable();
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
                    this.cardViewFactory = new CardViewFactory(card, this.homeGameActivity);
                    cardView = this.cardViewFactory.createCardView();
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
        gestionePaginazione(cards, lastPage, localCardsGrid);
        this.cardsGrid.setWidget(0, 1, localCardsGrid);
    }

    private void gestionePaginazione(List<?> cards, int lastPage, FlexTable cardsGrid) {

        Button prevPageButton = new Button();
        Button nextPageButton = new Button();
        if (currentPage == 0) {
            prevPageButton.setEnabled(false);
        } else {
            prevPageButton.addClickHandler(event -> {
                currentPage--;
                paginationSetInGrid();
            });
        }

        if (lastPage >= cards.size()) {
            nextPageButton.setEnabled(false);
        } else {
            nextPageButton.addClickHandler(event -> {
                currentPage++;
                paginationSetInGrid();
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

        this.cardsGrid.setWidget(0, 0, prevPageButton);
        this.cardsGrid.setWidget(0, 2, paginationPanel);
    }

    private void paginationSetInGrid() {
        homeGameActivity.getView().setInGrid(showGrid());
    }

    /**
     * Metodo che setta la griglia nella view
     */
    // private void setInGridView() {
    // homeGameActivity.getView().setInGrid(showGrid());
    // }

    public FlexTable getCardsGrid() {
        return this.cardsGrid;
    }

    @Override
    public Widget getPlaceholder() {
        Panel placeholderPanel = new HorizontalPanel();
        Label placeholderLabel = new Label("Non ci sono carte da mostrare");
        placeholderLabel.setStyleName("gamePlaceholderLabel");
        placeholderPanel.setStyleName("gamePlaceholderPanel");
        placeholderPanel.add(placeholderLabel);
        return placeholderPanel;
    }
}