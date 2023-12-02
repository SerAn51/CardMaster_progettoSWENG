package com.dlm.gwt.sample.cardmaster.shared.services;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("db_getter")
public interface DatabaseService extends RemoteService {

        List<Card> getCards(String gameName);

        Boolean saveChangesInDB(User loggedUser);

        Map<String, List<Card>> getOwnersWishersOfCard(Card card, Boolean isOwned);

        Boolean sendExchangeProposal(User loggedUser, String usernameOtherUser, List<Card> proposedCards,
                        List<Card> requestedCards);

        void checkExchangesAfterRemoveCard(User loggedUser, Card card);

        User getUserByUsername(String username);

}
