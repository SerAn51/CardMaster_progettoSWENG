package com.dlm.gwt.sample.cardmaster.client.view.cardsView;

import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.*;

public class CardPokemonView extends CardView {

    protected String illustrator;
    // protected String image;
    protected String description;
    protected String rarity;
    protected boolean firstEdition;
    protected boolean holo;
    protected boolean normal;
    protected boolean reverse;
    protected boolean wPromo;
    protected String type;

    public CardPokemonView(PokemonCard card, User user, String gameName) {
        super(card, user, gameName);
        this.card = card;
        this.type = card.getType() != "" ? card.getType() : "trainer";
        this.illustrator = card.getIllustrator();
        this.description = card.getDescription();
        // this.image = card.getImage();
        this.rarity = card.getRarity();
        this.firstEdition = card.getFirstEdition();
        this.holo = card.getHolo();
        this.normal = card.getNormal();
        this.reverse = card.getReverse();
        this.wPromo = card.getWPromo();

        setContent(createSpecificCard());
    }

    protected Widget createSpecificCard() {
        Panel card = new VerticalPanel();
        // card.setStyleName("miniBody");

        Panel cardWrap = new VerticalPanel();
        cardWrap.setStyleName("card-wrap");

        Panel cardHeader = new VerticalPanel();
        cardHeader.setStyleName("card-header");

        Panel cardColor = new VerticalPanel();
        cardColor.setStyleName("header-" + type.toLowerCase());

        cardHeader.add(cardColor);

        Panel typeSymbol = new VerticalPanel();
        typeSymbol.setStyleName("symbol-" + type.toLowerCase());

        cardColor.add(typeSymbol);

        // Label cardText = new Label(this.description);
        // cardText.setStyleName("card-text");

        /**
         * Card content
         */
        Panel cardContent = new VerticalPanel();
        cardContent.setStyleName("card-content");

        Label nameLabel = new Label(this.card.getName());
        nameLabel.setStyleName("card-title");

        Panel pokemonType = new VerticalPanel();
        pokemonType.setStyleName("pokemon-type");

        Label typeLabel = new Label(this.type);
        typeLabel.setStyleName("type-" + type.toLowerCase());

        pokemonType.add(typeLabel);

        cardContent.add(nameLabel);
        cardContent.add(pokemonType);

        // cardHeader.add(typeSymbol);

        cardWrap.add(cardHeader);
        cardWrap.add(cardContent);

        card.add(cardWrap);

        return card;

    }

}
