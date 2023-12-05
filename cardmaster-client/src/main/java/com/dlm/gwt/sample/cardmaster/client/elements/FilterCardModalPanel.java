package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy.CardFilterStrategy;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.client.utils.ElementType;
import com.dlm.gwt.sample.cardmaster.client.view.HomeGameView;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterCardModalPanel extends PopupPanel {

    private String gameName;
    private HomeGameView homeGameView;
    private Map<String, String> selectedRadioButtonsMap = new HashMap<>();
    private VerticalPanel filterContentPanel;
    private CardFilterStrategy cardFilterStrategy;

    public FilterCardModalPanel(HomeGameView view, String gameName,
            List<Card> cards, HomeGameActivity homeGameActivity, HidePopupPanelClickingOutside hidePopup) {
        this.homeGameView = view;
        this.gameName = gameName;
        this.filterContentPanel = new VerticalPanel();

        filterContentPanel.setStyleName("filterContentPanel");

        // Aggiunto il metodo per impostare la strategia
        setCardFilterStrategy(view.getCardFilterStrategy());

        addFilterComponents();

        Button applyFilterButton = new Button("Applica filtro");
        applyFilterButton.setStyleName("applyFilterButton");
        applyFilterButton.addClickHandler(event -> {
            readRadioButtons();
            hide();
            hidePopup.destroy();

            if (cardFilterStrategy != null) {
                CardListType cardListType = homeGameActivity.getCardListType();
                List<Card> filteredCards = cardFilterStrategy.filter(cards, null, selectedRadioButtonsMap,
                        cardListType);
                homeGameView.showGrid(filteredCards, ElementType.CARDS);
            }
        });

        filterContentPanel.add(applyFilterButton);
        setWidget(filterContentPanel);
    }

    // Metodo per impostare la strategia
    public void setCardFilterStrategy(CardFilterStrategy strategy) {
        this.cardFilterStrategy = strategy;
    }

    private void addFilterComponents() {
        if ("magic".equalsIgnoreCase(gameName)) {
            addMagicFilters();
        } else if ("pokemon".equalsIgnoreCase(gameName)) {
            addPokemonFilters();
        } else if ("yugioh".equalsIgnoreCase(gameName)) {
            addYugiohFilters();
        }
    }

    // Metodi per aggiungere filtri specifici per ogni gioco
    private void addMagicFilters() {
        addTypeFilters(MagicCard.Type.values());
        addRarityFilters(MagicCard.Rarity.values());
        Label otherLabel = new Label("Altro:");
        otherLabel.setStyleName("title");
        filterContentPanel.add(otherLabel);
        addRadioButtonFilter("Foil", "foilGroup", "hasFoil");
        addRadioButtonFilter("Alternative", "alternativeGroup", "isAlternative");
        addRadioButtonFilter("Full Art", "fullArtGroup", "isFullArt");
        addRadioButtonFilter("Promo", "promoGroup", "isPromo");
        addRadioButtonFilter("Reprint", "reprintGroup", "isReprint");
    }

    private void addPokemonFilters() {
        addTypeFilters(PokemonCard.Type.values());
        addRarityFilters(PokemonCard.Rarity.values());
        Label otherLabel = new Label("Altro:");
        otherLabel.setStyleName("title");
        filterContentPanel.add(otherLabel);
        addRadioButtonFilter("First Edition", "firstEditionGroup", "firstEdition");
        addRadioButtonFilter("Holo", "holoGroup", "holo");
        addRadioButtonFilter("Normal", "normalGroup", "normal");
        addRadioButtonFilter("Reverse", "reverseGroup", "reverse");
        addRadioButtonFilter("W Promo", "wPromoGroup", "wPromo");
    }

    private void addYugiohFilters() {
        addTypeFilters(YugiohCard.Type.values());
        addRaceFilters(YugiohCard.Race.values());
    }

    // Metodi di utilità per aggiungere filtri generici
    private void addTypeFilters(Enum<?>[] values) {
        Label typeLabel = new Label("Tipo:");
        typeLabel.setStyleName("title");
        filterContentPanel.add(typeLabel);
        for (Enum<?> value : values) {
            addRadioButtonFilter(value.name(), "typeGroup", value.name());
        }
    }

    private void addRarityFilters(Enum<?>[] values) {
        Label rarityLabel = new Label("Rarità:");
        rarityLabel.setStyleName("title");
        filterContentPanel.add(rarityLabel);
        for (Enum<?> value : values) {
            addRadioButtonFilter(value.name(), "rarityGroup", value.name());
        }
    }

    private void addRaceFilters(Enum<?>[] values) {
        Label raceLabel = new Label("Razza:");
        raceLabel.setStyleName("title");
        for (Enum<?> value : values) {
            addRadioButtonFilter(value.name(), "raceGroup", value.name());
        }
        filterContentPanel.add(raceLabel);
    }

    private void addRadioButtonFilter(String label, String groupName, String value) {
        RadioButton radioButton = new RadioButton(groupName, label);
        filterContentPanel.add(radioButton);
    }

    private void readRadioButtons() {
        selectedRadioButtonsMap.clear();

        for (int i = 0; i < filterContentPanel.getWidgetCount(); i++) {
            if (filterContentPanel.getWidget(i) instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) filterContentPanel.getWidget(i);
                if (radioButton.getValue()) {
                    String groupName = radioButton.getName();
                    String label = radioButton.getText();
                    selectedRadioButtonsMap.put(groupName, label);
                }
            }
        }
    }

}
