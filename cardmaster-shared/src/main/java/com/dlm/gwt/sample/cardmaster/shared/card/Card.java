package com.dlm.gwt.sample.cardmaster.shared.card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class Card implements Serializable {

    @SerializedName("name")
    private String nome;
    //TODO: aggiungere altri campi comuni a pokemon, magic e yugioh

    public Card(){
        //costruttore vuoto utile per la seriazlizzazione
    }
    public Card(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    // Altri metodi e campi dati se necessario
}
