package com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;

public class PokemonCardFilterStrategy implements CardFilterStrategy {
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

        addPokemonCardToFilterSet(card, selectedRadioMap, filteredCardSet);
    }

    private void addPokemonCardToFilterSet(Card card, Map<String, String> selectedRadioMap, Set<Card> filteredCardSet) {
        PokemonCard pokemonCard = (PokemonCard) card;

        // Pokemon-specific filters
        String selectedRarity = selectedRadioMap.get("rarityGroup");
        if (selectedRarity != null && !pokemonCard.getRarity().equalsIgnoreCase(selectedRarity)) {
            filteredCardSet.remove(card);
        }

        String selectedFirstEdition = selectedRadioMap.get("firstEditionGroup");
        if (selectedFirstEdition != null
                && !(pokemonCard.getFirstEdition() && selectedFirstEdition.equalsIgnoreCase("First Edition"))) {
            filteredCardSet.remove(card);
        }

        String selectedHolo = selectedRadioMap.get("holoGroup");
        if (selectedHolo != null && !(pokemonCard.getHolo() && selectedHolo.equalsIgnoreCase("Holo"))) {
            filteredCardSet.remove(card);
        }

        String selectedNormal = selectedRadioMap.get("normalGroup");
        if (selectedNormal != null && !(pokemonCard.getNormal() && selectedNormal.equalsIgnoreCase("Normal"))) {
            filteredCardSet.remove(card);
        }

        String selectedReverse = selectedRadioMap.get("reverseGroup");
        if (selectedReverse != null && !(pokemonCard.getReverse() && selectedReverse.equalsIgnoreCase("Reverse"))) {
            filteredCardSet.remove(card);
        }

        String selectedPromo = selectedRadioMap.get("promoGroup");
        if (selectedPromo != null && !(pokemonCard.getWPromo() && selectedPromo.equalsIgnoreCase("Promo"))) {
            filteredCardSet.remove(card);
        }
    }
}
