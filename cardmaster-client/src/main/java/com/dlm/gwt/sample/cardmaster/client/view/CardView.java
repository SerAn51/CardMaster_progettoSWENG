package com.dlm.gwt.sample.cardmaster.client.view;

import com.dlm.gwt.sample.cardmaster.client.elements.CardDetailsModalPanel;
import com.dlm.gwt.sample.cardmaster.client.elements.HidePopupPanelClickingOutside;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.*;

public abstract class CardView extends Composite {

    protected Card card;
    protected String description;
    private User user;
    private String gameName;
    private Panel cardShape;

    public CardView(Card card, User user, String gameName) {
        this.user = user;
        this.card = card;
        this.gameName = gameName;
        this.cardShape = new VerticalPanel();
        initWidget(this.cardShape);
    }

    // Metodo per impostare il contenuto della carta
    public void setContent(Widget content) {
        Panel cardAddition = new VerticalPanel();

        cardAddition.add(content);

        Panel overlay = new VerticalPanel();
        overlay.setStyleName("overlay");

        // TODO: gestire mostra tutte/ mostra owned/ mostra wished
        Button showDetails = new Button("Dettagli");
        showDetails.addClickHandler(event -> {
            CardDetailsModalPanel cardDetailsModalPanel = new CardDetailsModalPanel(user,
                    card, gameName, 1);
            cardDetailsModalPanel.center();
            cardDetailsModalPanel.show();

            hidePopupPanelClickingOutside(cardDetailsModalPanel);
        });

        showDetails.setStyleName("card-btn");

        overlay.add(showDetails);

        cardAddition.setStyleName("card");
        cardAddition.add(overlay);

        cardShape.add(cardAddition); // Aggiunta del contenuto al pannello della carta

    }

    private void hidePopupPanelClickingOutside(PopupPanel modalPanel) {
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
        hidePopup.initialize(modalPanel);
    }

}
