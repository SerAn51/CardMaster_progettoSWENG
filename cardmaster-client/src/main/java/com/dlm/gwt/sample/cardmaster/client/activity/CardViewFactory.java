package com.dlm.gwt.sample.cardmaster.client.activity;

import com.dlm.gwt.sample.cardmaster.client.view.CardView;
import com.dlm.gwt.sample.cardmaster.client.view.cardsView.CardMagicView;
import com.dlm.gwt.sample.cardmaster.client.view.cardsView.CardPokemonView;
import com.dlm.gwt.sample.cardmaster.client.view.cardsView.CardYugiohView;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;

public class CardViewFactory {

    private Card card;
    private HomeGameActivity homeGameActivity;

    public CardViewFactory(Card card, HomeGameActivity homeGameActivity) {
        this.card = card;
        this.homeGameActivity = homeGameActivity;
    }

    public CardView createCardView() {

        switch (card.getGame()) {
            case "Pokemon":
                return createPokemonCardView((PokemonCard) this.card, this.homeGameActivity);

            case "Magic":
                return createMagicCardView((MagicCard) this.card, this.homeGameActivity);

            case "Yugioh":
                return createYugiohCardView((YugiohCard) this.card, this.homeGameActivity);

            default:
                return null;
        }
    }

    private CardView createPokemonCardView(PokemonCard card, HomeGameActivity homeGameActivity) {
        return new CardPokemonView(card, homeGameActivity);
    }

    private CardView createMagicCardView(MagicCard card, HomeGameActivity homeGameActivity) {
        return new CardMagicView(card, homeGameActivity);
    }

    private CardView createYugiohCardView(YugiohCard card, HomeGameActivity homeGameActivity) {
        return new CardYugiohView(card, homeGameActivity);
    }
}