package com.dlm.gwt.sample.cardmaster.shared.card;

import com.google.gson.annotations.SerializedName;

public class MagicCard extends Card {

    // campi specifici delle carte Magic
    @SerializedName("artists")
    private String artists;
    @SerializedName("rarity")
    private String rarity;
    @SerializedName("hasFoil")
    private boolean hasFoil;
    @SerializedName("isAlternative")
    private boolean isAlternative;
    @SerializedName("isFullArt")
    private boolean isFullArt;
    @SerializedName("isPromo")
    private boolean isPromo;
    @SerializedName("isReprint")
    private boolean isReprint;

    public enum Rarity {
        Common, Rare, Uncommon
    }

    public enum Type {
        Creature, Enchantment, Instant, Sorcery,
    }

    public MagicCard() {
        // costruttore vuoto utile per la serializzazione
    }

    public MagicCard(String name, String type, String description, String artists, String rarity, boolean hasFoil,
            boolean isAlternative, boolean isFullArt, boolean isPromo, boolean isReprint) {
        super(name, type, description); // Chiama il costruttore della classe astratta
        this.artists = artists;
        this.rarity = rarity;
        this.hasFoil = hasFoil;
        this.isAlternative = isAlternative;
        this.isFullArt = isFullArt;
        this.isPromo = isPromo;
        this.isReprint = isReprint;
    }

    // getter

    public String getArtists() {
        return this.artists;
    }

    public String getRarity() {
        return this.rarity;
    }

    public boolean getHasFoil() {
        return this.hasFoil;
    }

    public boolean getIsAlternative() {
        return this.isAlternative;
    }

    public boolean getIsFullArt() {
        return this.isFullArt;
    }

    public boolean getIsPromo() {
        return this.isPromo;
    }

    public boolean getIsReprint() {
        return this.isReprint;
    }

    @Override
    public String getGame() {
        return "Magic";
    }

    @Override
    public String toString() {
        return "MagicCard [name=" + super.getName() + ", type=" + super.getType() + ", description="
                + super.getDescription() + ", artists=" + this.artists + ", rarity=" + this.rarity
                + ", hasFoil=" + this.hasFoil
                + ", isAlternative=" + this.isAlternative + ", isFullArt=" + this.isFullArt + ", isPromo="
                + this.isPromo + ", isReprint=" + this.isReprint + ", condition=" + super.getCondition()
                + ", conditionDescription=" + super.getConditionDescription() + "]";
    }

    // Altri metodi e campi dati specifici per MagicCard
}
