package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.user.User;

import database.Database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import org.junit.jupiter.api.DisplayName;

public class LoginServiceImplTest {

    LoginServiceImpl service = new LoginServiceImpl();
    @Mock //oggetto fittizio usato per simulare uno reale
    Database fakeDatabase = mock(Database.class);

    User testUser = new User();

    @BeforeEach
    public void cleanEnvironment() {
        testUser.setAge(20);
        testUser.setEmail("teoteocoli@email.it");
        testUser.setPassword("ciao");
        testUser.setGender("Male");
        testUser.setUsername("teoteocoli");
    }

    @DisplayName("Testing login")
    @Test
    public void testWrongLogin() {
        //needed to mock the static database
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(Database::getInstance).thenReturn(fakeDatabase);
            when(fakeDatabase.login(anyString(), anyString())).thenReturn(null);
            when(fakeDatabase.login("teoteocoli", "ciao")).thenReturn(testUser);
            User result = service.login("teoteocoli", "unaPwdErrata");
            //"unaPwdErrata" e' diverso da "ciao" quindi il login fallisce, una qualsiasi stringa diversa da ciao quindi fallira'
            assertNull(result);
        }
    }

    @Test
    public void testSuccessfulLogin() {
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(Database::getInstance).thenReturn(fakeDatabase);
            when(fakeDatabase.login(anyString(), anyString())).thenReturn(null);
            when(fakeDatabase.login("teoteocoli", "ciao")).thenReturn(testUser);
            User result = service.login("teoteocoli", "ciao");

            //se tutti i parametri sono uguali (se tutto e' vero), allora ho ritornato l'utente corretto
            assert (result.getUsername().equals("teoteocoli"));
            assert (result.getEmail().equals("teoteocoli@email.it"));
            assert (result.getGender().equals("Male"));
            assert (result.getPassword().equals("ciao"));
            assertEquals(20, result.getAge());
        }
    }
}
