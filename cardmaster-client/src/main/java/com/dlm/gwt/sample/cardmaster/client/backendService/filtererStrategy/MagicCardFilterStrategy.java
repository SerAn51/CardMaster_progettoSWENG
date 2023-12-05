package com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;

public class MagicCardFilterStrategy implements CardFilterStrategy {

    @Override
    public List<Card> filter(List<Card> cards, String searchText, Map<String, String> selectedRadioMap,
            CardListType listType) {

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
            // Ritorna le carte filtrate (per nome o con i filtri)
            return filteredCardList;// , listType);
        } else {
            return cards;// , listType);
        }
    }

    private void addCardToFilterSet(Card card, Map<String, String> selectedRadioMap, Set<Card> filteredCardSet) {
        // Filtri comuni ai giochi
        String selectedType = selectedRadioMap.get("typeGroup");
        if (selectedType != null && !card.getType().equalsIgnoreCase(selectedType)) {
            filteredCardSet.remove(card);
        }

        // Filtri specifici di Magic
        addMagicCardToFilterSet(card, selectedRadioMap, filteredCardSet);
    }

    private void addMagicCardToFilterSet(Card card, Map<String, String> selectedRadioMap, Set<Card> filteredCardSet) {
        MagicCard magicCard = (MagicCard) card;

        // Magic-specific filters
        String selectedRarity = selectedRadioMap.get("rarityGroup");
        if (selectedRarity != null && !magicCard.getRarity().equalsIgnoreCase(selectedRarity)) {
            filteredCardSet.remove(card);
        }

        String selectedFoil = selectedRadioMap.get("foilGroup");
        if (selectedFoil != null && !(magicCard.getHasFoil() && selectedFoil.equalsIgnoreCase("Foil"))) {
            filteredCardSet.remove(card);
        }

        String selectedAlternative = selectedRadioMap.get("alternativeGroup");
        if (selectedAlternative != null
                && !(magicCard.getIsAlternative() && selectedAlternative.equalsIgnoreCase("Alternative"))) {
            filteredCardSet.remove(card);
        }

        String selectedFullArt = selectedRadioMap.get("fullArtGroup");
        if (selectedFullArt != null && !(magicCard.getIsFullArt() && selectedFullArt.equalsIgnoreCase("Full Art"))) {
            filteredCardSet.remove(card);
        }

        String selectedPromo = selectedRadioMap.get("promoGroup");
        if (selectedPromo != null && !(magicCard.getIsPromo() && selectedPromo.equalsIgnoreCase("Promo"))) {
            filteredCardSet.remove(card);
        }

        String selectedReprint = selectedRadioMap.get("reprintGroup");
        if (selectedReprint != null && !(magicCard.getIsReprint() && selectedReprint.equalsIgnoreCase("Reprint"))) {
            filteredCardSet.remove(card);
        }
    }

}
