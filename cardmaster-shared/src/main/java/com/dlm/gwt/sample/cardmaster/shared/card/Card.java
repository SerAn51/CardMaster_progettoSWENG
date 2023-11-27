package com.dlm.gwt.sample.cardmaster.shared.card;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class Card implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("description")
    private String description;
    @SerializedName("condition")
    private String condition;
    @SerializedName("conditionDescription")
    private String conditionDescription;

    public Card() {
        // costruttore vuoto utile per la seriazlizzazione
    }

    public Card(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCondition() {
        return this.condition;
    }

    public String getConditionDescription() {
        return this.conditionDescription;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    public abstract String getGame();

    public abstract String toString();

    // Confronta gli attributi desiderati, se sono uguali ritorna true
    public boolean compareAttributes(Card otherCard) {
        if (otherCard == null) {
            return false;
        }

        // Confronta gli attributi desiderati
        if (!this.name.equals(otherCard.getName())) {
            return false;
        }

        if (!this.condition.equals(otherCard.getCondition())) {
            return false;
        }

        if (!this.conditionDescription.equals(otherCard.getConditionDescription())) {
            return false;
        }

        return true;
    }

}
