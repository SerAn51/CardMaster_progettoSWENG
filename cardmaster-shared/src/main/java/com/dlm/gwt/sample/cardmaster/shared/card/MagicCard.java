package com.dlm.gwt.sample.cardmaster.shared.card;

import com.google.gson.annotations.SerializedName;

public class MagicCard extends Card {

    @SerializedName("isReprint")
    private boolean isReprint;
    //TODO: aggiungi altri campi specifici delle carte Magic

    public MagicCard() {
        //costruttore vuoto utile per la serializzazione
    }

    public MagicCard(String nome, boolean isReprint) {
        super(nome); // Chiama il costruttore della classe astratta
        this.isReprint = isReprint;
    }

    public boolean getIsReprint() {
        return this.isReprint;
    }

    // Altri metodi e campi dati specifici per MagicCard
}

