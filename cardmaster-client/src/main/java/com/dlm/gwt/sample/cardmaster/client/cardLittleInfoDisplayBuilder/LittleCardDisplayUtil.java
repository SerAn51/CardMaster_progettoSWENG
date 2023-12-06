package com.dlm.gwt.sample.cardmaster.client.cardLittleInfoDisplayBuilder;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LittleCardDisplayUtil {

    private Card card;
    private CheckBox cardCheckBox;

    public LittleCardDisplayUtil(Card card) {
        this.card = card;
    }

    public HorizontalPanel createCardPanelWithOnlyName() {
        return new CardInfoDisplayBuilder()
                .addBackCardImage(card.getGame())
                .addOnlyNameInfo(card.getName())
                .build();
    }

    public HorizontalPanel createCardPanel() {
        return new CardInfoDisplayBuilder()
                .addBackCardImage(card.getGame())
                .addCardInfo(card.getName(), card.getCondition(), card.getConditionDescription())
                .build();
    }

    public HorizontalPanel createCardPanelWithCheckBox() {
        CardInfoDisplayBuilder builder = new CardInfoDisplayBuilder()
                .addBackCardImage(card.getGame())
                .addCardInfo(card.getName(), card.getCondition(), card.getConditionDescription())
                .addCheckBox();

        // Ottenere il riferimento alla checkbox
        cardCheckBox = builder.getCardCheckBox();

        return builder.build();
    }

    // Aggiunto un metodo per ottenere la checkbox
    public CheckBox getCardCheckBox() {
        return cardCheckBox;
    }

}
