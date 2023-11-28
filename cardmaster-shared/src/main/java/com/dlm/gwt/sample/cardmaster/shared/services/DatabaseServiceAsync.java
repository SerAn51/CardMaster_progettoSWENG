package com.dlm.gwt.sample.cardmaster.shared.services;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface DatabaseServiceAsync {
    
    void getCards(String gameName, AsyncCallback<List<Card>> callback) throws IllegalArgumentException;
    void saveChangesInDB(User loggedUser,AsyncCallback<Boolean> callback) throws IllegalArgumentException;

}
