package com.dlm.gwt.sample.cardmaster.client.activity;

import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.client.view.cardsView.CardMagicView;
import com.dlm.gwt.sample.cardmaster.client.view.cardsView.CardPokemonView;
import com.dlm.gwt.sample.cardmaster.client.view.cardsView.CardYugiohView;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

public class CardViewFactory {

    private String gameName;
    private User user;
    private Card card;

    public CardViewFactory(Card card, String gameName, User loggedUser) {
        this.user = loggedUser;
        this.gameName = gameName;
        this.card = card;
    }

    public CardView createCardView() {

        switch (card.getGame()) {
            case "Pokemon":
                return createPokemonCardView((PokemonCard) this.card);

            case "Magic":
                return createMagicCardView((MagicCard) this.card);

            case "Yugioh":
                return createYugiohCardView((YugiohCard) this.card);

            default:
                return null;
        }
    }

    private CardView createPokemonCardView(PokemonCard card) {
        return new CardPokemonView(card, user, this.gameName);
    }

    private CardView createMagicCardView(MagicCard card) {
        return new CardMagicView(card, user, this.gameName);
    }

    private CardView createYugiohCardView(YugiohCard card) {
        return new CardYugiohView(card, user, this.gameName);
    }
}
