package com.dlm.gwt.sample.cardmaster.shared.card;

import com.google.gson.annotations.SerializedName;

public class PokemonCard extends Card {

    @SerializedName("illustrator")
    private String illustrator;
    @SerializedName("image")
    private String image;
    @SerializedName("rarity")
    private String rarity;
    @SerializedName("firstEdition")
    private Boolean firstEdition;
    @SerializedName("holo")
    private Boolean holo;
    @SerializedName("normal")
    private Boolean normal;
    @SerializedName("reverse")
    private Boolean reverse;
    @SerializedName("wPromo")
    private Boolean wPromo;

    public enum Rarity {
        None, Common, Uncommon, Rare, UltraRare, SecretRare,
    }

    public enum Type {
        Colorless, Darkness, Dragon, Fairy, Fighting, Fire, Grass, Lightning, Metal,
        Psychic, Water
    }

    public PokemonCard() {
        // costruttore vuoto utile per la serializzazione
    }

    public PokemonCard(String name, String type, String description, String illustrator, String image, String rarity,
            Boolean firstEdition, Boolean holo, Boolean normal, Boolean reverse, Boolean wPromo) {
        super(name, type, description); // Chiama il costruttore della classe astratta
        this.illustrator = illustrator;
        this.image = image;
        this.rarity = rarity;
        this.firstEdition = firstEdition;
        this.holo = holo;
        this.normal = normal;
        this.reverse = reverse;
        this.wPromo = wPromo;
    }

    // getter
    public String getIllustrator() {
        return this.illustrator;
    }

    public String getImage() {
        return this.image;
    }

    public String getRarity() {
        return this.rarity;
    }

    public Boolean getFirstEdition() {
        return this.firstEdition;
    }

    public Boolean getHolo() {
        return this.holo;
    }

    public Boolean getNormal() {
        return this.normal;
    }

    public Boolean getReverse() {
        return this.reverse;
    }

    public Boolean getWPromo() {
        return this.wPromo;
    }

    @Override
    public String getGame() {
        return "Pokemon";
    }

    @Override
    public String toString() {
        return "PokemonCard [name=" + super.getName() + ", type=" + super.getType() + ", description="
                + super.getDescription() + ", illustrator=" + this.illustrator + ", image=" + this.image
                + ", rarity=" + this.rarity + ", firstEdition=" + this.firstEdition + ", holo=" + this.holo
                + ", normal=" + this.normal + ", reverse=" + this.reverse + ", wPromo=" + this.wPromo + ", condition="
                + super.getCondition() + ", conditionDescription=" + super.getConditionDescription() + "]";
    }

    // Altri metodi e campi dati specifici per PokemonCard
}
