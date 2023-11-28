package com.dlm.gwt.sample.cardmaster.client;

import com.dlm.gwt.sample.cardmaster.client.backendService.BackendService;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
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
}
