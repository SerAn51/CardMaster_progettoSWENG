package com.dlm.gwt.sample.cardmaster.client;

import com.dlm.gwt.sample.cardmaster.client.backendService.BackendService;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.Deck;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseServiceAsync;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

public class BackendServiceTest {

    @Mock
    private DatabaseServiceAsync mockDatabaseService;

    private BackendService backendService;

    @BeforeEach
    public void setUp() {
        backendService = new BackendService(mockDatabaseService);
    }

    @DisplayName("Testing addCardToUserOwnedOrWished with owned card")
    @Test
    public void testAddCardToUserOwned() {
        User user = new User("mockUser", "mockPassword", "mockEmail@mail.it", 14, "Altro");
        Card mockCard = new MockCard("MockCard", "MockType", "MockDescription");
        boolean isOwnedOrWished = true;

        backendService.addCardToUserOwnedOrWished(user, mockCard, isOwnedOrWished);

        // Ora aggiungi un'asserzione per verificare che la carta sia stata aggiunta
        // correttamente all'utente.
        // Hint: user.getOwnedCards() dovrebbe contenere la carta aggiunta.

        assertTrue(user.getOwnedCards().contains(mockCard));
    }

    @DisplayName("Testing addCardToUserOwnedOrWished with wished card")
    @Test
    public void testAddCardToUserWished() {
        User user = new User("mockUser", "mockPassword", "mockEmail@mail.it", 14, "Altro");
        Card card = new MockCard("mockCard", "mockType", "mockDescription");
        boolean isOwnedOrWished = false;

        backendService.addCardToUserOwnedOrWished(user, card, isOwnedOrWished);

        // Ora aggiungi un'asserzione per verificare che la carta sia stata aggiunta
        // correttamente all'utente.
        // Hint: user.getWishedCards() dovrebbe contenere la carta aggiunta.

        assertTrue(user.getWishedCards().contains(card));
    }

    @DisplayName("Testing removeCardFromUserOwnedOrWished with owned card")
    @Test
    public void testRemoveCardFromUserOwned() {
        User user = new User("mockUser", "mockPassword", "", 14, "Altro");
        Card card = new MockCard("mockCard", "mockType", "mockDescription");
        boolean isOwnedOrWished = true;

        // aggiungo la carta alla lista di owned dell'utente cosi' da testare la sua rimozione
        user.addOwnedCard(card);

        backendService.removeCardFromUserOwnedOrWished(user, card, isOwnedOrWished);

        assertFalse(user.getOwnedCards().contains(card));
    }

    @DisplayName("Testing removeCardFromUserOwnedOrWished with wished card")
    @Test
    public void testRemoveCardFromUserWished() {
        User user = new User("mockUser", "mockPassword", "", 14, "Altro");
        Card card = new MockCard("mockCard", "mockType", "mockDescription");
        boolean isOwnedOrWished = false;

        // aggiungo la carta alla lista di wished dell'utente cosi' da testare la sua rimozione
        user.addWishedCard(card);

        backendService.removeCardFromUserOwnedOrWished(user, card, isOwnedOrWished);

        assertFalse(user.getWishedCards().contains(card));
    }


    @DisplayName("Testing createDeck")
    @Test
    public void testCreateDeck() {
        User user = new User("mockUser", "mockPassword", "", 14, "Altro");
        String gameName = "mockGameName";
        String deckName = "mockDeckName";

        backendService.createDeck(user, gameName, deckName);

        assertTrue(user.getDecks().containsKey(deckName));
    }

    @DisplayName("Testing deleteDeck")
    @Test
    public void testDeleteDeck() {
        User user = new User("mockUser", "mockPassword", "", 14, "Altro");
        String deckName = "mockDeckName";
        user.getDecks().put(deckName, new Deck(deckName, "mockGameName"));

        backendService.deleteDeck(user, deckName);

        assertFalse(user.getDecks().containsKey(deckName));
    }

    @DisplayName("Testing addCardToDeck")
    @Test
    public void testAddCardToDeck() {
        User user = new User("mockUser", "mockPassword", "", 14, "Altro");
        String deckName = "mockDeckName";
        Deck deck = new Deck(deckName, "pokemon");
        user.getDecks().put(deckName, deck);
        Card card = new MockCard("mockCard", "mockType", "mockDescription");

        backendService.addCardToDeck(user, deckName, card);

        assertTrue(deck.getCards().contains(card));
    }

    @DisplayName("Testing removeCardFromDeck")
    @Test
    public void testRemoveCardFromDeck() {
        User user = new User("mockUser", "mockPassword", "", 14, "Altro");
        String deckName = "mockDeckName";
        Deck deck = new Deck(deckName, "pokemon");
        user.getDecks().put(deckName, deck);
        Card card = new MockCard("mockCard", "mockType", "mockDescription");
        deck.getCards().add(card);

        backendService.removeCardFromDeck(user, deckName, card);

        assertFalse(deck.getCards().contains(card));
    }
}
