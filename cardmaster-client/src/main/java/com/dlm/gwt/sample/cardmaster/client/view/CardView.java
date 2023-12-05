package com.dlm.gwt.sample.cardmaster.client.view;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.google.gwt.user.client.ui.*;

public abstract class CardView extends Composite {

    protected Card card;
    protected String description;
    private Panel cardShape;
    private HomeGameActivity homeGameActivity;

    public CardView(Card card, HomeGameActivity homeGameActivity) {
        this.card = card;
        this.homeGameActivity = homeGameActivity;
        this.cardShape = new VerticalPanel();
        initWidget(this.cardShape);
    }

    // Metodo per impostare il contenuto della carta
    public void setContent(Widget content) {
        Panel cardAddition = new VerticalPanel();

        cardAddition.add(content);

        Panel overlay = new VerticalPanel();
        overlay.setStyleName("overlay");

        Button showDetails = new Button("Dettagli");
        showDetails.addClickHandler(event -> {
           homeGameActivity.showCardDetailsModalPanel(card);
        });

        showDetails.setStyleName("card-btn");

        overlay.add(showDetails);

        cardAddition.setStyleName("card");
        cardAddition.add(overlay);

        cardShape.add(cardAddition); // Aggiunta del contenuto al pannello della carta

    }

}