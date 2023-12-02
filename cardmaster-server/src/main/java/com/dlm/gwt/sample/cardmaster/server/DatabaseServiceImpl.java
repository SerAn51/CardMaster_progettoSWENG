package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import database.Database;

import java.util.List;
import java.util.Map;

/**
 * Si e' optato per l'implementazione del service e il non utilizzo della gia'
 * esistente
 * classe Database.java per mettere a disposizione del client solo quei metodi
 * che effettivamente deve poter utilizzare (es. non viene data la possibilità
 * di usare open, close, ecc.)
 **/
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {

    @Override
    public List<Card> getCards(String gameName) {
        //System.out.println("SONO DatabaseServiceImpl, STO AVVIANDO getCards");
        return Database.getInstance().getCards(gameName);
    }

    @Override
    public Boolean saveChangesInDB(User loggedUser) {
        return Database.getInstance().saveChangesInDB(loggedUser);
    }

    @Override
    public Map<String, List<Card>> getOwnersWishersOfCard(Card card, Boolean isOwned) {
        return Database.getInstance().getOwnersWishersOfCard(card, isOwned);
    }

    @Override
    public Boolean sendExchangeProposal(User loggedUser, String usernameOtherUser, List<Card> proposedCards,
            List<Card> requestedCards) {
        return Database.getInstance().sendExchangeProposal(loggedUser, usernameOtherUser, proposedCards,
                requestedCards);
    }

    @Override
    public void checkExchangesAfterRemoveCard(User loggedUser, Card card) {
        Database.getInstance().checkExchangesAfterRemoveCard(loggedUser, card);
    }

    @Override
    public User getUserByUsername(String username) {
        return Database.getInstance().getUserByUsername(username);
    }

}
