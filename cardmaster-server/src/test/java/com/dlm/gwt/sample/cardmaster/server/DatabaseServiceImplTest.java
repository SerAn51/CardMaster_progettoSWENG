package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;

import database.Database;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import org.junit.jupiter.api.DisplayName;

public class DatabaseServiceImplTest {

    DatabaseServiceImpl service = new DatabaseServiceImpl();
    @Mock // oggetto fittizio usato per simulare uno reale
    Database fakeDatabase = mock(Database.class);

    User testUser = new User();

    @BeforeEach
    public void cleanEnvironment() {
    }

    @DisplayName("Testing getCards")
    @Test
    public void testGetCards() {
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(Database::getInstance).thenReturn(fakeDatabase);

            // Mock data for the test
            List<Card> mockCards = List.of(
                    new MockCard("Card1", "Type1", "Description1"),
                    new MockCard("Card2", "Type2", "Description2"),
                    new MockCard("Card3", "Type3", "Description3"));

            // Stubbing the getCards method to return mockCards for the game "MockGame"
            when(fakeDatabase.getCards("MockGame")).thenReturn(mockCards);

            // Performing the actual method invocation
            List<Card> result = service.getCards("MockGame");

            // Verifying that the method was called with the correct parameter
            verify(fakeDatabase, times(1)).getCards("MockGame");

            // Verifying the result
            assertEquals(mockCards.size(), result.size());
            // Additional assertions based on your actual implementation

            // Additional assertions based on your actual implementation
            for (int i = 0; i < mockCards.size(); i++) {
                assertEquals(mockCards.get(i).getName(), result.get(i).getName());
                assertEquals(mockCards.get(i).getType(), result.get(i).getType());
                assertEquals(mockCards.get(i).getDescription(), result.get(i).getDescription());
                // Add more assertions for other attributes as needed
            }
        }
    }
}