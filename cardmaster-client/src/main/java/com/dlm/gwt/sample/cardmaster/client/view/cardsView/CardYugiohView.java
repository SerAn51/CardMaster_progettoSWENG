package com.dlm.gwt.sample.cardmaster.client.view.cardsView;

import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CardYugiohView extends CardView {
    protected String type;
    protected String description;
    protected String race;
    protected Image image_url;
    protected Image small_image_url;

    public CardYugiohView(YugiohCard card, User user, String gameName) {
        super(card, user, gameName);
        this.type = card.getType();
        this.description = card.getDescription();
        this.race = card.getRace();
        this.image_url = new Image(card.getImage_url());
        this.small_image_url = new Image(card.getSmall_image_url());

        setContent(createSpecificCard());
    }

    protected Widget createSpecificCard() {

        FlowPanel card = new FlowPanel();
        image_url.setStyleName("cardImage");

        card.add(image_url);
        return card;

    }
}
