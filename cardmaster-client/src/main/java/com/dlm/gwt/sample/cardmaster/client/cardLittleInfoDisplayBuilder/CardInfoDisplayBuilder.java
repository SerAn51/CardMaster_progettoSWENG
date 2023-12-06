package com.dlm.gwt.sample.cardmaster.client.cardLittleInfoDisplayBuilder;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CardInfoDisplayBuilder {

    private HorizontalPanel cardContainer;
    private CheckBox cardCheckBox;

    public CardInfoDisplayBuilder() {
        cardContainer = new HorizontalPanel();
        cardContainer.setStyleName("cardContainer");
        cardContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    }

    public CardInfoDisplayBuilder addBackCardImage(String game) {
        HTML imageContainerBackCardButton = new HTML("<div class='backCardImage'></div>");
        if (game.equalsIgnoreCase("Magic")) {
            imageContainerBackCardButton.setStyleName("backCardImageMagic");
        } else if (game.equalsIgnoreCase("Pokemon")) {
            imageContainerBackCardButton.setStyleName("backCardImagePokemon");
        } else if (game.equals("Yugioh")) {
            imageContainerBackCardButton.setStyleName("backCardImageYugioh");
        }
        cardContainer.add(imageContainerBackCardButton);
        return this;
    }

    public CardInfoDisplayBuilder addCardInfo(String name, String condition, String conditionDescription) {
        VerticalPanel cardInfoContainer = new VerticalPanel();
        Label nameLabel = new Label(name);
        nameLabel.setStyleName("nameLabel");
        Label conditionLabel = new Label("Condizione: " + condition);
        conditionLabel.setStyleName("conditionLabel");
        Label conditionDescriptionLabel = new Label(
                "Descrizione: " + ((conditionDescription.equals("")) ? "Nessuna" : conditionDescription));
        conditionDescriptionLabel.setStyleName("conditionLabel");
        cardInfoContainer.add(nameLabel);
        cardInfoContainer.add(conditionLabel);
        cardInfoContainer.add(conditionDescriptionLabel);
        cardContainer.add(cardInfoContainer);
        return this;
    }

    public CardInfoDisplayBuilder addOnlyNameInfo(String name) {
        VerticalPanel cardInfoContainer = new VerticalPanel();
        Label nameLabel = new Label(name);
        nameLabel.setStyleName("nameLabel");
        cardInfoContainer.add(nameLabel);
        cardContainer.add(cardInfoContainer);
        return this;
    }

    public CardInfoDisplayBuilder addCheckBox() {
        cardCheckBox = new CheckBox();
        cardCheckBox.addStyleName("cardCheckBox");
        cardContainer.add(cardCheckBox);
        return this;
    }

    public CheckBox getCardCheckBox() {
        return cardCheckBox;
    }

    public HorizontalPanel build() {
        return cardContainer;
    }
}
