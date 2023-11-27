package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Questa classe è una factory che crea una Card a partire da un Json.
 * E' stata messa nel server perchè il client non deve mai creare delle carte,
 * per cui non deve avere la dipendenza verso questa classe.
 */
public class CardFactoryCreator {

    Card card = null;

    public CardFactoryCreator(JsonObject cardObject, String gameName) {

        this.card = createCard(cardObject, gameName);
    }

    private Card createCard(JsonObject cardObject, String game) {

        String name = getStringValue(cardObject, "name");
        String type = getStringValue(cardObject, "type");
        String description = getStringValue(cardObject, "description");

        if (game.equalsIgnoreCase("magic")) {
            String artists = getStringValue(cardObject, "artists");
            String rarityMagic = getStringValue(cardObject, "rarity");
            boolean hasFoil = getBooleanValue(cardObject, "hasFoil");
            boolean isAlternative = getBooleanValue(cardObject, "isAlternative");
            boolean isFullArt = getBooleanValue(cardObject, "isFullArt");
            boolean isPromo = getBooleanValue(cardObject, "isPromo");
            boolean isReprint = getBooleanValue(cardObject, "isReprint");
            return new MagicCard(name, type, description, artists, rarityMagic, hasFoil, isAlternative,
                    isFullArt, isPromo, isReprint);
        } else if (game.equalsIgnoreCase("pokemon")) {
            String illustrator = getStringValue(cardObject, "illustrator");
            String image = getStringValue(cardObject, "image");
            String rarityPokemon = getStringValue(cardObject, "rarity");
            boolean firstEdition = getBooleanValue(cardObject, "firstEdition");
            boolean holo = getBooleanValue(cardObject, "holo");
            boolean normal = getBooleanValue(cardObject, "normal");
            boolean reverse = getBooleanValue(cardObject, "reverse");
            boolean wPromo = getBooleanValue(cardObject, "wPromo");
            return new PokemonCard(name, type, description, illustrator, image, rarityPokemon, firstEdition,
                    holo,
                    normal, reverse, wPromo);
        } else if (game.equalsIgnoreCase("yugioh")) {
            String race = getStringValue(cardObject, "race");
            String image_url = getStringValue(cardObject, "image_url");
            String small_image_url = getStringValue(cardObject, "small_image_url");
            return new YugiohCard(name, type, description, race, image_url, small_image_url);
        } else {
            return null;
            // In caso di gioco non supportato o nessun match, puoi gestire l'eccezione o
            // restituire un'istanza predefinita
        }

    }

    public String getStringValue(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && element.isJsonPrimitive()) ? element.getAsString() : "";
    }

    public boolean getBooleanValue(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && element.isJsonPrimitive()) ? element.getAsBoolean() : false;
    }

    public Card getCard() {
        return this.card;
    }
}
