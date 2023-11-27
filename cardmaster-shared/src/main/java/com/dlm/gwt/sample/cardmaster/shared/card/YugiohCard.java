package com.dlm.gwt.sample.cardmaster.shared.card;

import com.google.gson.annotations.SerializedName;

public class YugiohCard extends Card {

    @SerializedName("race")
    private String race;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("small_image_url")
    private String small_image_url;

    public enum Race {
        Aqua, Beast, BeastWarrior, Cyberse, Continuous, Counter, Dinosaur, Dragon,
        Equip, Fairy, Field, Fiend, Fish, Insect, Kaiba, Machine, Normal, Plant,
        Psychic, Pyro, QuickPlay, Reptile, Ritual, Rock, SeaSerpent, Spellcaster,
        Thunder, Warrior, WingedBeast, Wyrm, Zombie
    }

    public enum Type {
        BeastWarrior, EffectMonster, FusionMonster, LinkMonster, NormalMonster,
        PendulumEffectMonster, RitualEffectMonster, SkillCard, SpellCard,
        SynchroMonster, Token, TrapCard, TunerMonster, UnionEffectMonster,
        XYZMonster
    }

    public YugiohCard() {
        // costruttore vuoto utile per la serializzazione
    }

    public YugiohCard(String name, String type, String description, String race, String image_url,
            String small_image_url) {
        super(name, type, description); // Chiama il costruttore della classe astratta
        this.race = race;
        this.image_url = image_url;
        this.small_image_url = small_image_url;
    }

    // getter
    public String getRace() {
        return this.race;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public String getSmall_image_url() {
        return this.small_image_url;
    }

    @Override
    public String getGame() {
        return "Yugioh";
    }

    @Override
    public String toString() {
        return "MagicCard [name=" + super.getName() + ", type=" + super.getType() + ", description="
                + super.getDescription() + ", race=" + this.race + ", image_url=" + this.image_url
                + ", small_image_url=" + this.small_image_url + ", condition=" + super.getCondition()
                + ", conditionDescription=" + super.getConditionDescription() + "]";
    }

    // Altri metodi e campi dati specifici per YugiohCard
}
