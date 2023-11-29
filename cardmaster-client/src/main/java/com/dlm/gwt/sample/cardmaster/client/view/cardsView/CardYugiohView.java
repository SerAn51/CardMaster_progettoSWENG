package com.dlm.gwt.sample.cardmaster.client.view.cardsView;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class CardYugiohView extends CardView {
    protected String type;
    protected String description;
    protected String race;
    protected Image image_url;
    protected Image small_image_url;

    public CardYugiohView(YugiohCard card, HomeGameActivity homeGameActivity) {
        super(card, homeGameActivity);
        this.type = card.getType();
        this.description = card.getDescription();
        this.race = card.getRace();
        this.image_url = new Image(card.getImage_url());
        this.small_image_url = new Image(card.getSmall_image_url());

        setContent(createSpecificCard());
    }

    protected Widget createSpecificCard() {

        FlowPanel card = new FlowPanel();

        HTML svg = new HTML(
                "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\"><path d=\"M20 5H4V19L13.2923 9.70649C13.6828 9.31595 14.3159 9.31591 14.7065 9.70641L20 15.0104V5ZM2 3.9934C2 3.44476 2.45531 3 2.9918 3H21.0082C21.556 3 22 3.44495 22 3.9934V20.0066C22 20.5552 21.5447 21 21.0082 21H2.9918C2.44405 21 2 20.5551 2 20.0066V3.9934ZM8 11C6.89543 11 6 10.1046 6 9C6 7.89543 6.89543 7 8 7C9.10457 7 10 7.89543 10 9C10 10.1046 9.10457 11 8 11Z\"></path></svg>");
        svg.setStyleName("card__svg");
        image_url.setStyleName("cardImage");

        card.add(image_url);
        // FlowPanel cardContent = new FlowPanel();
        // cardContent.setStyleName("card__content");

        // HTML title = new HTML("<p class=\"card__title\"> Description </p>");
        // HTML htmlDescription = new HTML("<p class=\"card__description\">" +
        // this.description + "</p>");

        // cardContent.add(title);
        // cardContent.add(htmlDescription);
        // card.add(cardContent);

        return card;

    }
}
