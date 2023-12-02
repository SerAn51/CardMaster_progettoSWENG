package com.dlm.gwt.sample.cardmaster.shared.card;

import java.io.Serializable;
import java.util.List;

import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gson.annotations.SerializedName;

public class ExchangeProposal implements Serializable {

    @SerializedName("proponent")
    private User proponent;
    @SerializedName("counterparty")
    private String counterparty;
    @SerializedName("proposedCards")
    private List<Card> proposedCards;
    @SerializedName("requestedCards")
    private List<Card> requestedCards;

    public ExchangeProposal() {
    }

    public ExchangeProposal(User loggedUser, String usernameOtherUser, List<Card> proposedCards,
            List<Card> requestedCards) {
        this.proponent = loggedUser;
        this.counterparty = usernameOtherUser;
        this.proposedCards = proposedCards;
        this.requestedCards = requestedCards;
    }

    public User getProponent() {
        return proponent;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public List<Card> getProposedCards() {
        return this.proposedCards;
    }

    public List<Card> getrequestedCards() {
        return this.requestedCards;
    }

    public String toString() {
        return "ExchangeProposal [proponent=" + proponent.getUsername() + ", counterparty=" + counterparty
                + ", proposedCards=" + proposedCards + ", requestedCards=" + requestedCards
                + "]";
    }

}
