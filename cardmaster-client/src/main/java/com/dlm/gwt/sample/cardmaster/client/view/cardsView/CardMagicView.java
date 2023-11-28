package com.dlm.gwt.sample.cardmaster.client.view.cardsView;

import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.*;

public class CardMagicView extends CardView {

    protected String type;
    protected String description;
    protected String artists;
    protected String rarity;
    protected boolean hasFoil;
    protected boolean isAlternative;
    protected boolean isFullArt;
    protected boolean isPromo;
    protected boolean isReprint;

    public CardMagicView(MagicCard card, User user, String gameName) {
        super(card, user, gameName);
        this.card = card;
        this.type = card.getType();
        this.description = card.getDescription(); // Chiama il costruttore della classe astratta
        this.artists = card.getArtists();
        this.rarity = card.getRarity();
        this.hasFoil = card.getHasFoil();
        this.isAlternative = card.getIsAlternative();
        this.isFullArt = card.getIsFullArt();
        this.isPromo = card.getIsPromo();
        this.isReprint = card.getIsReprint();
        setContent(createSpecificCard());
    }

    protected Widget createSpecificCard() {
        Panel card = new VerticalPanel();

        Panel cardWrap = new VerticalPanel();
        cardWrap.setStyleName("card-wrap");

        Panel cardHeader = new VerticalPanel();
        cardHeader.setStyleName("card-header");

        Panel cardColor = new VerticalPanel();
        cardColor.setStyleName("header-" + type);

        cardHeader.add(cardColor);

        Panel typeSymbol = new VerticalPanel();
        typeSymbol.setStyleName("symbol-" + type);

        cardColor.add(typeSymbol);

        Panel cardContent = new VerticalPanel();
        cardContent.setStyleName("card-content");

        Label nameLabel = new Label(this.card.getName());
        nameLabel.setStyleName("card-title");

        Panel pokemonType = new VerticalPanel();
        pokemonType.setStyleName("pokemon-type");

        Label typeLabel = new Label(this.type);
        typeLabel.setStyleName("type-" + type);

        pokemonType.add(typeLabel);

        cardContent.add(nameLabel);
        cardContent.add(pokemonType);

        cardWrap.add(cardHeader);
        cardWrap.add(cardContent);

        card.add(cardWrap);

        return card;
    }
}
