package com.dlm.gwt.sample.cardmaster.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.Iterator;
import java.util.List;

import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.view.ExchangeView;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.card.ExchangeProposal;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.card.PokemonCard;
import com.dlm.gwt.sample.cardmaster.shared.card.YugiohCard;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

public class ExchangeActivity extends AbstractActivity {

    private final ExchangeView view;
    private final DatabaseServiceAsync databaseService;
    User loggedUser = SessionUser.getInstance().getSessionUser();

    public ExchangeActivity(ExchangeView view, DatabaseServiceAsync databaseService) {
        this.view = view;
        this.databaseService = databaseService;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        // Aggiungi la vista al contenitore
        containerWidget.setWidget(view);
    }

    public void acceptProposal(ExchangeProposal exchangeProposal) {

        User proponentUser = exchangeProposal.getProponent();
        performExchange(loggedUser, proponentUser, exchangeProposal);

    }

    public void performExchange(User loggedUser, User proponentUser,
            ExchangeProposal exchangeProposal) {
        // 1. aggiungo a loggedUser le carte proposte, costruendo tali carte usando
        // le informazioni di quelle in exchangeProposal.getProposedCards()
        // 2. aggiungo a proponentUser le carte che loggedUser vuole, costruendo tali
        // carte usando le informazioni di quelle in
        // exchangeProposal.getRequestedCards()
        // 3. rimuovo da loggedUser le carte che proponentUser vuole, confrontando le
        // carte in loggedUser.getOwnedCards() con quelle in
        // exchangeProposal.getRequestedCards()
        // 4. rimuovo da proponentUser le carte proposte, confrontando le carte in
        // proponentUser.getOwnedCards() con quelle in
        // exchangeProposal.getProposedCards()
        // 5. elimina la proposta di scambio da loggedUser.getExchangeProposals()
        // 6. salva le modifiche nel database per entrambi gli utenti

        // 1.
        List<Card> proposedCards = exchangeProposal.getProposedCards();
        for (Card proposedCard : proposedCards) {
            Card cardToAdd = null;
            if (proposedCard.getGame().equalsIgnoreCase("magic")) {
                MagicCard magicCard = (MagicCard) proposedCard;
                cardToAdd = new MagicCard(magicCard.getName(), magicCard.getType(),
                        magicCard.getDescription(),
                        magicCard.getArtists(), magicCard.getRarity(), magicCard.getHasFoil(),
                        magicCard.getIsAlternative(), magicCard.getIsFullArt(),
                        magicCard.getIsPromo(),
                        magicCard.getIsReprint());
            } else if (proposedCard.getGame().equalsIgnoreCase("pokemon")) {
                PokemonCard pokemonCard = (PokemonCard) proposedCard;
                cardToAdd = new PokemonCard(pokemonCard.getName(), pokemonCard.getType(),
                        pokemonCard.getDescription(),
                        pokemonCard.getIllustrator(), pokemonCard.getImage(),
                        pokemonCard.getRarity(),
                        pokemonCard.getFirstEdition(), pokemonCard.getHolo(),
                        pokemonCard.getNormal(),
                        pokemonCard.getReverse(), pokemonCard.getWPromo());
            } else /* if (proposedCard.getGame().equalsIgnoreCase("yugioh")) */ {
                YugiohCard yugiohCard = (YugiohCard) proposedCard;
                cardToAdd = new YugiohCard(yugiohCard.getName(), yugiohCard.getType(),
                        yugiohCard.getDescription(),
                        yugiohCard.getRace(), yugiohCard.getImage_url(),
                        yugiohCard.getSmall_image_url());
            }
            cardToAdd.setCondition(proposedCard.getCondition());
            cardToAdd.setConditionDescription(proposedCard.getConditionDescription());
            loggedUser.addOwnedCard(cardToAdd);
        }
        System.out.println("HO FINITO LO STEP 1");

        // 2.
        List<Card> requestedCards = exchangeProposal.getrequestedCards();
        for (Card cardProponentWant : requestedCards) {
            Card cardToAdd = null;
            if (cardProponentWant.getGame().equalsIgnoreCase("magic")) {
                MagicCard magicCard = (MagicCard) cardProponentWant;
                cardToAdd = new MagicCard(magicCard.getName(), magicCard.getType(),
                        magicCard.getDescription(),
                        magicCard.getArtists(), magicCard.getRarity(), magicCard.getHasFoil(),
                        magicCard.getIsAlternative(),
                        magicCard.getIsFullArt(), magicCard.getIsPromo(), magicCard.getIsReprint());
            } else if (cardProponentWant.getGame().equalsIgnoreCase("pokemon")) {
                PokemonCard pokemonCard = (PokemonCard) cardProponentWant;
                cardToAdd = new PokemonCard(pokemonCard.getName(), pokemonCard.getType(),
                        pokemonCard.getDescription(),
                        pokemonCard.getIllustrator(), pokemonCard.getImage(),
                        pokemonCard.getRarity(),
                        pokemonCard.getFirstEdition(), pokemonCard.getHolo(),
                        pokemonCard.getNormal(),
                        pokemonCard.getReverse(), pokemonCard.getWPromo());
            } else /* if (cardProponentWant.getGame().equalsIgnoreCase("yugioh")) */ {
                YugiohCard yugiohCard = (YugiohCard) cardProponentWant;
                cardToAdd = new YugiohCard(yugiohCard.getName(), yugiohCard.getType(),
                        yugiohCard.getDescription(),
                        yugiohCard.getRace(), yugiohCard.getImage_url(),
                        yugiohCard.getSmall_image_url());
            }
            cardToAdd.setCondition(cardProponentWant.getCondition());
            cardToAdd.setConditionDescription(cardProponentWant.getConditionDescription());
            proponentUser.addOwnedCard(cardToAdd);
            System.out.println("HO FINITO LO STEP 2");
        }

        // 3.
        for (Card cardProponentWant : requestedCards) {
            List<Card> loggedUserOwnedCards = loggedUser.getOwnedCards();
            Iterator<Card> iterator = loggedUserOwnedCards.iterator();
            while (iterator.hasNext()) {
                Card loggedUserOwnedCard = iterator.next();
                // se il nome della carta, la condizione e la descrizione combaciano, allora
                // sto
                // eliminando la carta giusta o una perfettamente uguale, dunque
                // indistinguibile
                // per l'utente
                if (loggedUserOwnedCard.getName().equals(cardProponentWant.getName()) &&
                        loggedUserOwnedCard.getCondition().equals(cardProponentWant.getCondition())
                        &&
                        loggedUserOwnedCard.getConditionDescription()
                                .equals(cardProponentWant.getConditionDescription())) {
                    iterator.remove(); // Utilizza il metodo remove dell'iteratore per rimuovere
                    // in modo sicuro durante
                    // l'iterazione
                    // elimini la prima che trovi e ti fermi

                    // elimina la carta dai deck in cui è contenuta
                    removeCard(loggedUser, loggedUserOwnedCard);

                    break;
                }
            }
        }
        System.out.println("HO FINITO LO STEP 3");

        // 4.
        List<Card> proponentUserOwnedCards = proponentUser.getOwnedCards();
        Iterator<Card> proponentIterator = proponentUserOwnedCards.iterator();
        while (proponentIterator.hasNext()) {
            Card proponentUserOwnedCard = proponentIterator.next();
            for (Card proposedCard : proposedCards) {
                // se il nome della carta, la condizione e la descrizione combaciano, allora
                // sto
                // eliminando la carta giusta o una perfettamente uguale, dunque
                // indistinguibile
                // per l'utente
                if (proponentUserOwnedCard.getName().equals(proposedCard.getName()) &&
                        proponentUserOwnedCard.getCondition().equals(proposedCard.getCondition()) &&
                        proponentUserOwnedCard.getConditionDescription()
                                .equals(proposedCard.getConditionDescription())) {
                    proponentIterator.remove(); // Utilizza il metodo remove dell'iteratore per
                    // rimuovere in modo sicuro
                    // durante l'iterazione
                    // elimini la prima che trovi e ti fermi
                    removeCard(proponentUser, proponentUserOwnedCard);

                    break;
                }
            }
        }
        System.out.println("HO FINITO LO STEP 4");

        // 5.
        loggedUser.removeExchangeProposal(exchangeProposal);

        // 6.
        databaseService.saveChangesInDB(loggedUser, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                databaseService.saveChangesInDB(proponentUser, new AsyncCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        Window.alert("Proposta accettata, scambio eseguito con successo");
                        // forzo l'aggiornamento della pagina per aggiornare le informazioni
                        refreshPage();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // Gestisci l'errore durante la chiamata al servizio database
                        Window.alert("Errore durante lo scambio: " +
                                caught.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore durante lo scambio: " +
                        caught.getMessage());
            }
        });

    }

    public void declineProposal(ExchangeProposal exchangeProposal) {
        loggedUser.getExchangeProposals().remove(exchangeProposal);

        databaseService.saveChangesInDB(this.loggedUser, new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                Window.alert("Proposta rifiutata");
                // forzo l'aggiornamento della pagina per aggiornare le informazioni
                refreshPage();
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore durante il rifiuto della proposta: " +
                        caught.getMessage());
            }
        });
    }

    private void refreshPage() {
        // forzo l'aggiornamento della pagina per aggiornare le informazioni
        String token = "exchange";
        History.newItem(token);
        new ViewRouter().handleRouteChange(token);
    }

    private void removeCard(User userRemove, Card card) {

        removeCardFromDeck(userRemove, card);

        // controlla se la carta è presente in qualche proposta di scambio e rimuovila
        databaseService.checkExchangesAfterRemoveCard(userRemove, card, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Aggiorna manualmente la lista dei decks nella sessione utente
                Window.alert("Carta rimossa con successo");
            }

            @Override
            public void onFailure(Throwable caught) {
                // Gestisci l'errore durante la chiamata al servizio database
                Window.alert("Errore HomeGameActivity.removeCard: " + caught.getMessage());
            }
        });
    }

    private void removeCardFromDeck(User user, Card card) {
        for (Deck deck : user.getDecks().values()) {
            if (deck.getCards().contains(card)) {
                deck.getCards().remove(card);
            }
        }
    }

}