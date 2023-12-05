package com.dlm.gwt.sample.cardmaster.client.backendService.filtererStrategy;

import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;

public interface CardFilterStrategy {
    List<Card> filter(List<Card> cards, String searchText, Map<String, String> selectedRadioMap, CardListType listType);
}
