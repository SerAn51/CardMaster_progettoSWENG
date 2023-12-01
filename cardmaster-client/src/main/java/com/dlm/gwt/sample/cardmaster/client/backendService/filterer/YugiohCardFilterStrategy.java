package com.dlm.gwt.sample.cardmaster.client.backendService.filterer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;

public class YugiohCardFilterStrategy implements CardFilterStrategy {
    @Override
    public List<Card> filter(List<Card> cards, String searchText, Map<String, String> selectedRadioMap, CardListType listType) {
        // Use a Set to keep track of cards that satisfy all filters
        Set<Card> filteredCardSet = new HashSet<>(cards);

        // Filtra le carte
        for (Card card : cards) {

            if (searchText != null && !card.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredCardSet.remove(card);
            } else if (selectedRadioMap != null) {
                addCardToFilterSet(card, selectedRadioMap, filteredCardSet);
            }

        }

        // Converti il Set in una lista
        List<Card> filteredCardList = new ArrayList<>(filteredCardSet);

        // Stampa le carte
        if (searchText != null || selectedRadioMap != null) {
            // Display the filtered cards
            return filteredCardList;// , listType);
        } else {
            return cards;// , listType);
        }
    }

    private void addCardToFilterSet(Card card, Map<String, String> selectedRadioMap, Set<Card> filteredCardSet) {
        // Common filters for all games
        String selectedType = selectedRadioMap.get("typeGroup");
        if (selectedType != null && !card.getType().equalsIgnoreCase(selectedType)) {
            filteredCardSet.remove(card);
        }

        // Add more common filters as needed

        addYugiohCardToFilterSet(card, selectedRadioMap, filteredCardSet);
    }

    private void addYugiohCardToFilterSet(Card card, Map<String, String> selectedRadioMap, Set<Card> filteredCardSet) {
        YugiohCard yugiohCard = (YugiohCard) card;

        // Yu-Gi-Oh-specific filters
        String selectedRace = selectedRadioMap.get("raceGroup");
        if (selectedRace != null && !yugiohCard.getRace().equalsIgnoreCase(selectedRace)) {
            filteredCardSet.remove(card);
        }
    }
}