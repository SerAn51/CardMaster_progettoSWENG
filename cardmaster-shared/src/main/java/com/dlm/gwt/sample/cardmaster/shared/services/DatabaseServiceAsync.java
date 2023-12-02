package com.dlm.gwt.sample.cardmaster.shared.services;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface DatabaseServiceAsync {

        void getCards(String gameName, AsyncCallback<List<Card>> callback) throws IllegalArgumentException;

        void saveChangesInDB(User loggedUser,
                        AsyncCallback<Boolean> callback) throws IllegalArgumentException;

        void getOwnersWishersOfCard(Card card, Boolean isOwned, AsyncCallback<Map<String, List<Card>>> callback)
                        throws IllegalArgumentException;

        void sendExchangeProposal(User loggedUser, String usernameOtherUser, List<Card> proposedCards,
                        List<Card> requestedCards, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

        void checkExchangesAfterRemoveCard(User loggedUser, Card card, AsyncCallback<Void> callback)
                        throws IllegalArgumentException;

        void getUserByUsername(String username, AsyncCallback<User> callback) throws IllegalArgumentException;
}
