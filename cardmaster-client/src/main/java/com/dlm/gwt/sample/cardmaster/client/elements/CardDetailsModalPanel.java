package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CardDetailsModalPanel extends PopupPanel {
    private final DatabaseServiceAsync databaseServiceAsync;
    private HomeGameActivity homeGameActivity;
    private String gameName;
    private FlowPanel cardDetailsContainer;
    private User loggedUser;

    public CardDetailsModalPanel(User loggedUser, Card card, String gameName, CardListType listType) {

        databaseServiceAsync = GWT.create(DatabaseService.class);
        this.homeGameActivity = new HomeGameActivity(null, databaseServiceAsync, gameName);
        this.gameName = gameName;
        this.loggedUser = loggedUser;

        // Contenuto della finestra modale
        VerticalPanel content = new VerticalPanel();

        cardDetailsContainer = new FlowPanel();
        cardDetailsContainer.addStyleName("cardDetailsContainer");

        Label cardNameLabel = new Label("Nome: " + card.getName());
        Label cardTypeLabel = new Label("Tipo: " + card.getType());
        Label cardDescriptionLabel = new Label("Descrizione: " + card.getDescription());

        cardDetailsContainer.add(cardNameLabel);
        cardDetailsContainer.add(cardTypeLabel);
        cardDetailsContainer.add(cardDescriptionLabel);

        if (this.gameName.equalsIgnoreCase("magic")) {
            magicCardDetails(card);
        }

        if (this.gameName.equalsIgnoreCase("pokemon")) {
            pokemonCardDetails(card);
        }

        if (this.gameName.equalsIgnoreCase("yugioh")) {
            yuGiOhCardDetails(card);
        }

        Button addOwnedButton = new Button("Aggiungi alle owned");
        Button addWishedButton = new Button("Aggiungi alle wished");
        Button removeOwnedButton = new Button("Rimuovi dalle owned");
        Button updatePropertiesButton = new Button("Aggiorna proprietà");
        Button removeWishedButton = new Button("Rimuovi dalle wished");

        // se passo SHOW_ALL_CARDS, ho cliccato mostra tutte le carte
        if (listType == CardListType.SHOW_ALL_CARDS) {
            cardDetailsContainer.add(addOwnedButton);
            cardDetailsContainer.add(addWishedButton);
        }
        // se passo SHOW_OWNED_CARDS, ho cliccato mostra owned
        if (listType == CardListType.SHOW_OWNED_CARDS) {
            cardDetailsContainer.add(removeOwnedButton);
            cardDetailsContainer.add(updatePropertiesButton);
        }
        // se passo SHOW_WISHED_CARDS, ho cliccato mostra wished
        if (listType == CardListType.SHOW_WISHED_CARDS) {
            cardDetailsContainer.add(addOwnedButton);
            cardDetailsContainer.add(removeWishedButton);
        }

        // TODO: aggiungi eventi per i bottoni
        // al click del bottone, aggiungere la carta alle owned
        addOwnedButton.addClickHandler(event -> {
            addOrUpdateCardConditionModalPanel(card, false);
        });

        // al click del bottone, aggiungere la carta alle wished
        addWishedButton.addClickHandler(event -> {
            homeGameActivity.addCardToUserOwnedOrWished(card, false);
        });

        // campo enum in card e controllo quel campo
        content.add(cardDetailsContainer);

        setWidget(content);
    }

    private void magicCardDetails(Card card) {
        MagicCard magicCard = (MagicCard) card;
        Label cardArtistLabel = new Label("Artista: " + magicCard.getArtists());
        Label cardRarityLabel = new Label("Rarità: " + magicCard.getRarity());
        Label cardHasFoilLabel = new Label("Foil: " + magicCard.getHasFoil());
        Label cardIsAlternativeLabel = new Label("Alternative: " + magicCard.getIsAlternative());
        Label cardIsFullArtLabel = new Label("Full Art: " + magicCard.getIsFullArt());
        Label cardIsPromoLabel = new Label("Promo: " + magicCard.getIsPromo());
        Label cardIsReprintLabel = new Label("Reprint: " + magicCard.getIsReprint());
        Label cardCondition = new Label("Condizione: " + magicCard.getCondition());

        cardDetailsContainer.add(cardArtistLabel);
        cardDetailsContainer.add(cardRarityLabel);
        cardDetailsContainer.add(cardHasFoilLabel);
        cardDetailsContainer.add(cardIsAlternativeLabel);
        cardDetailsContainer.add(cardIsFullArtLabel);
        cardDetailsContainer.add(cardIsPromoLabel);
        cardDetailsContainer.add(cardIsReprintLabel);
        cardDetailsContainer.add(cardCondition);
    }

    private void pokemonCardDetails(Card card) {
        PokemonCard pokemonCard = (PokemonCard) card;
        Label cardArtistLabel = new Label("Illutstrator: " + pokemonCard.getIllustrator());
        Label cardRarityLabel = new Label("Rarità: " + pokemonCard.getRarity());
        Label cardFirstEditionLabel = new Label("Prima edizione: " + pokemonCard.getFirstEdition());
        Label cardHoloLabel = new Label("Holo: " + pokemonCard.getHolo());
        Label cardReverseLabel = new Label("Reverse : " + pokemonCard.getReverse());
        Label cardWPromoLabel = new Label("Promo: " + pokemonCard.getWPromo());
        Label cardCondition = new Label("Condizione: " + pokemonCard.getCondition());

        cardDetailsContainer.add(cardArtistLabel);
        cardDetailsContainer.add(cardRarityLabel);
        cardDetailsContainer.add(cardFirstEditionLabel);
        cardDetailsContainer.add(cardHoloLabel);
        cardDetailsContainer.add(cardReverseLabel);
        cardDetailsContainer.add(cardWPromoLabel);
        cardDetailsContainer.add(cardCondition);

    }

    private void yuGiOhCardDetails(Card card) {
        YugiohCard yugiohCard = (YugiohCard) card;

        Label cardRaceLabel = new Label("Razza: " + yugiohCard.getRace());
        Label cardImageUrlLabel = new Label("Immagine: " + yugiohCard.getImage_url());
        Label cardSmallImageUrlLabel = new Label("Immagine piccola: " + yugiohCard.getSmall_image_url());
        Label cardCondition = new Label("Condizione: " + yugiohCard.getCondition());

        cardDetailsContainer.add(cardRaceLabel);
        cardDetailsContainer.add(cardImageUrlLabel);
        cardDetailsContainer.add(cardSmallImageUrlLabel);
        cardDetailsContainer.add(cardCondition);

    }

    private void addOrUpdateCardConditionModalPanel(Card card, Boolean isOwned) {
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
        AddOrUpdateCardConditionModalPanel addCardModalPanel = new AddOrUpdateCardConditionModalPanel(card,homeGameActivity, isOwned, hidePopup);
        // Mostra il modal panel
        addCardModalPanel.center();
        addCardModalPanel.show();

        // Crea e mostra il GlassPanel con il gestore di eventi
        hidePopup.initialize(addCardModalPanel);
    }
}