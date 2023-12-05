package com.dlm.gwt.sample.cardmaster.client.elements;

import java.util.List;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.client.utils.CardListType;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CardDetailsModalPanel extends PopupPanel {
    private final DatabaseServiceAsync databaseServiceAsync;
    private HomeGameActivity homeGameActivity;
    private String gameName;
    private FlowPanel cardDetailsContainer;
    private HorizontalPanel ownersWishersContainer;
    private VerticalPanel ownersContainer;
    private VerticalPanel wishersContainer;
    private User loggedUser = SessionUser.getInstance().getSessionUser();

    public CardDetailsModalPanel(Card card, String gameName, CardListType listType) {

        databaseServiceAsync = GWT.create(DatabaseService.class);
        this.homeGameActivity = new HomeGameActivity(null, databaseServiceAsync, gameName);
        this.gameName = gameName;

        // Contenuto della finestra modale
        HorizontalPanel content = new HorizontalPanel();
        content.setStyleName("cardDetailsContent");

        cardDetailsContainer = new FlowPanel();
        cardDetailsContainer.setStyleName("cardDetailsContainer");

        ownersWishersContainer = new HorizontalPanel();
        ownersContainer = new VerticalPanel();
        ownersContainer.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        ownersContainer.setStyleName("ownersContainer");
        // TODO: questo deve essere scrollabile
        wishersContainer = new VerticalPanel();
        wishersContainer.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        // TODO: questo deve essere scrollabile

        Label cardDetailsLabel = new Label("Dettagli della carta");
        cardDetailsLabel.setStyleName("titleCardDetailsLabel");
        cardDetailsContainer.add(cardDetailsLabel);
        Label cardNameLabel = new Label("Nome: " + card.getName());
        cardNameLabel.setStyleName("cardDetailLabel");
        Label cardTypeLabel = new Label("Tipo: " + (card.getType().equals("") ? "nessuno" : card.getType()));
        cardTypeLabel.setStyleName("cardDetailLabel");
        Label cardDescriptionLabel = new Label(
                "Descrizione: " + (card.getDescription().equals("") ? "nessuna" : card.getDescription()));
        cardDescriptionLabel.setStyleName("cardDetailLabel");

        cardDetailsContainer.add(cardNameLabel);
        cardDetailsContainer.add(cardTypeLabel);
        cardDetailsContainer.add(cardDescriptionLabel);

        if (this.gameName.equalsIgnoreCase("magic")) {
            MagicCard magicCard = (MagicCard) card;
            showMagicCardDetails(magicCard, cardDetailsContainer);
        } else if (this.gameName.equalsIgnoreCase("pokemon")) {
            PokemonCard pokemonCard = (PokemonCard) card;
            showPokemonCardDetails(pokemonCard, cardDetailsContainer);
        } else if (this.gameName.equalsIgnoreCase("yugioh")) {
            YugiohCard yugiohCard = (YugiohCard) card;
            showYugiohCardDetails(yugiohCard, cardDetailsContainer);
        }

        if (card.getCondition() != null) {
            Label cardConditionLabel = new Label("Condizione: " + card.getCondition());
            cardConditionLabel.setStyleName("cardDetailLabel");
            cardDetailsContainer.add(cardConditionLabel);
        }
        if (card.getConditionDescription() != null) {
            Label cardConditionDescriptionLabel = new Label(
                    "Descrizione condizione: "
                            + (card.getConditionDescription().equals("") ? "nessuna" : card.getConditionDescription()));
            cardConditionDescriptionLabel.setStyleName("cardDetailLabel");
            cardDetailsContainer.add(cardConditionDescriptionLabel);
        }

        Button addOwnedButton = new Button("Aggiungi alle owned");
        addOwnedButton.setStyleName("addButton");
        Button addWishedButton = new Button("Aggiungi alle wished");
        addWishedButton.setStyleName("addButton");
        Button removeOwnedButton = new Button("Rimuovi dalle owned");
        removeOwnedButton.setStyleName("removeButton");
        Button removeWishedButton = new Button("Rimuovi dalle wished");
        removeWishedButton.setStyleName("removeButton");
        Button updatePropertiesButton = new Button("Aggiorna proprietà");
        updatePropertiesButton.setStyleName("updateButton");

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

        Label ownersLabel = new Label("Utenti che la possiedono");
        Label wishersLabel = new Label("Utenti che la desiderano");

        ownersContainer.add(ownersLabel);
        wishersContainer.add(wishersLabel);

        ownersLabel.setStyleName("titleCardDetailsLabel");
        wishersLabel.setStyleName("titleCardDetailsLabel");

        // al click del bottone, aggiungere la carta alle owned
        addOwnedButton.addClickHandler(event -> {
            addOrUpdateCardConditionModalPanel(card, false);
        });

        // al click del bottone, aggiungere la carta alle wished
        addWishedButton.addClickHandler(event -> {
            homeGameActivity.addCardToUserOwnedOrWished(card, false);
        });

        removeOwnedButton.addClickHandler(event -> {
            homeGameActivity.removeCardFromUserOwnedOrWished(card, true);
        });

        removeWishedButton.addClickHandler(event -> {
            homeGameActivity.removeCardFromUserOwnedOrWished(card, false);
        });

        updatePropertiesButton.addClickHandler(event -> {
            addOrUpdateCardConditionModalPanel(card, true);
        });

        // homeGameActivity.getOwnersWishersList(this, card, true);
        homeGameActivity.getOwnersWishersList(this, card, true, new AsyncCallback<Map<String, List<Card>>>() {
            @Override
            public void onSuccess(Map<String, List<Card>> ownersWishersMap) {
                showOwnersWishers(ownersWishersMap, true);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
        homeGameActivity.getOwnersWishersList(this, card, false, new AsyncCallback<Map<String, List<Card>>>() {
            @Override
            public void onSuccess(Map<String, List<Card>> ownersWishersMap) {
                showOwnersWishers(ownersWishersMap, false);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });

        content.add(cardDetailsContainer);
        ownersWishersContainer.add(ownersContainer);

        ownersWishersContainer.add(wishersContainer);
        content.add(ownersWishersContainer);

        setWidget(content);
    }

    private void addOrUpdateCardConditionModalPanel(Card card, Boolean isOwned) {
        HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();
        AddOrUpdateCardConditionModalPanel addCardModalPanel = new AddOrUpdateCardConditionModalPanel(card,
                homeGameActivity, isOwned, hidePopup);
        // Mostra il modal panel
        addCardModalPanel.center();
        addCardModalPanel.show();

        // Crea e mostra il GlassPanel con il gestore di eventi
        hidePopup.initialize(addCardModalPanel);
    }

    public void showOwnersWishers(Map<String, List<Card>> ownersWishersList, Boolean isOwned) {
        if (ownersWishersList != null) {

            Panel scrollablePanelWisher = new VerticalPanel();
            scrollablePanelWisher.setStyleName("scrollablePanel");

            Panel scrollablePanelOwner = new VerticalPanel();
            scrollablePanelOwner.setStyleName("scrollablePanel");

            // mostra gli utenti che la possiedono e la condizione della carta
            for (Map.Entry<String, List<Card>> entry : ownersWishersList.entrySet()) {
                String counterparty = entry.getKey();
                List<Card> cardList = entry.getValue();

                if (!counterparty.equalsIgnoreCase(loggedUser.getUsername())) {
                    if (isOwned) {
                        if (cardList.size() == 0) {
                            Label noOwnedCardsLabel = new Label("Nessuno possiede questa carta");
                            noOwnedCardsLabel.setStyleName("ownerWisherLabel");
                            ownersContainer.add(noOwnedCardsLabel);
                        } else {
                            for (Card specificCard : cardList) {
                                // non mostrare l'utente loggato

                                Label ownerLabel = new Label(counterparty + " - " + specificCard.getCondition());
                                ownerLabel.setStyleName("ownerWisherLabel");
                                scrollablePanelOwner.add(ownerLabel);

                                Button exchangeProposalButton = new Button("Proposta di scambio");
                                exchangeProposalButton.setStyleName("proposalButton");
                                exchangeProposalButton.addClickHandler(event -> {
                                    // apri un popup che prende in input la carta e l'utente
                                    // a cui si vuole proporre lo scambio

                                    HidePopupPanelClickingOutside hidePopup = new HidePopupPanelClickingOutside();

                                    ExchangeProposalModalPanel exchangeProposalModalPanel = new ExchangeProposalModalPanel(
                                            specificCard, counterparty, homeGameActivity, hidePopup);

                                    // Mostra il modal panel
                                    exchangeProposalModalPanel.center();
                                    exchangeProposalModalPanel.show();

                                    // Crea e mostra il GlassPanel con il gestore di eventi

                                    hidePopup.initialize(exchangeProposalModalPanel);
                                });
                                scrollablePanelOwner.add(exchangeProposalButton);
                            }
                        }
                    } else {
                        int numberOfWishedCards = cardList.size();

                        if (numberOfWishedCards > 0) {
                            Label wisherLabel = new Label(counterparty + " - x" + numberOfWishedCards);
                            wisherLabel.setStyleName("ownerWisherLabel");
                            scrollablePanelWisher.add(wisherLabel);
                        } else {
                            Label noWishedCardsLabel = new Label("Nessuno desidera questa carta");
                            noWishedCardsLabel.setStyleName("ownerWisherLabel");
                            wishersContainer.add(noWishedCardsLabel);
                        }
                    }
                }

            }
            
            ownersContainer.add(scrollablePanelOwner);
            wishersContainer.add(scrollablePanelWisher);
        }
    }

    private void showMagicCardDetails(MagicCard magicCard, Panel detailsContainer) {
        Label cardArtistLabel = new Label("Artista: " + magicCard.getArtists());
        Label cardRarityLabel = new Label("Rarità: " + magicCard.getRarity());
        Label cardHasFoilLabel = new Label("Foil: " + magicCard.getHasFoil());
        Label cardIsAlternativeLabel = new Label("Alternative: " + magicCard.getIsAlternative());
        Label cardIsFullArtLabel = new Label("Full Art: " + magicCard.getIsFullArt());
        Label cardIsPromoLabel = new Label("Promo: " + magicCard.getIsPromo());
        Label cardIsReprintLabel = new Label("Reprint: " + magicCard.getIsReprint());

        cardArtistLabel.setStyleName("cardDetailLabel");
        cardRarityLabel.setStyleName("cardDetailLabel");
        cardHasFoilLabel.setStyleName("cardDetailLabel");
        cardIsAlternativeLabel.setStyleName("cardDetailLabel");
        cardIsFullArtLabel.setStyleName("cardDetailLabel");
        cardIsPromoLabel.setStyleName("cardDetailLabel");
        cardIsReprintLabel.setStyleName("cardDetailLabel");

        detailsContainer.add(cardArtistLabel);
        detailsContainer.add(cardRarityLabel);
        detailsContainer.add(cardHasFoilLabel);
        detailsContainer.add(cardIsAlternativeLabel);
        detailsContainer.add(cardIsFullArtLabel);
        detailsContainer.add(cardIsPromoLabel);
        detailsContainer.add(cardIsReprintLabel);
    }

    private void showPokemonCardDetails(PokemonCard pokemonCard, Panel detailsContainer) {
        Label cardRarityLabel = new Label("Rarità: " + pokemonCard.getRarity());
        Label cardIsFirstEditionLabel = new Label("Prima edizione: " + pokemonCard.getFirstEdition());
        Label cardIsHoloLabel = new Label("Holo: " + pokemonCard.getHolo());
        Label cardIsNormalLabel = new Label("Normale: " + pokemonCard.getNormal());
        Label cardIsReverseLabel = new Label("Reverse: " + pokemonCard.getReverse());
        Label cardIsWPromoLabel = new Label("W Promo: " + pokemonCard.getWPromo());

        cardRarityLabel.setStyleName("cardDetailLabel");
        cardIsFirstEditionLabel.setStyleName("cardDetailLabel");
        cardIsHoloLabel.setStyleName("cardDetailLabel");
        cardIsNormalLabel.setStyleName("cardDetailLabel");
        cardIsReverseLabel.setStyleName("cardDetailLabel");
        cardIsWPromoLabel.setStyleName("cardDetailLabel");

        detailsContainer.add(cardRarityLabel);
        detailsContainer.add(cardIsFirstEditionLabel);
        detailsContainer.add(cardIsHoloLabel);
        detailsContainer.add(cardIsNormalLabel);
        detailsContainer.add(cardIsReverseLabel);
        detailsContainer.add(cardIsWPromoLabel);
    }

    private void showYugiohCardDetails(YugiohCard yugiohCard, Panel detailsContainer) {
        Label cardRaceLabel = new Label("Razza: " + yugiohCard.getRace());

        cardRaceLabel.setStyleName("cardDetailLabel");

        detailsContainer.add(cardRaceLabel);
    }
}