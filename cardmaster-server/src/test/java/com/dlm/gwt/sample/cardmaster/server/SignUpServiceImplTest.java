package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.user.User;

import database.Database;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.DisplayName;

public class SignUpServiceImplTest {

    SignUpServiceImpl service = new SignUpServiceImpl();
    @Mock //oggetto fittizio usato per simulare uno reale
    Database fakeDatabase = mock(Database.class);

    User testUser = new User();

    @BeforeEach
    public void cleanEnvironment() {
        testUser.setAge(20);
        testUser.setEmail("giangilberto@email.it");
        testUser.setPassword("ciao");
        testUser.setGender("Male");
        testUser.setUsername("giangilberto");
    }

    @DisplayName("Testing signup")
    @Test
    public void testWrongSingUp() {
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {//try with resources
            //MockedStatic è una classe di Mockito che permette di mockare metodi statici di una classe
            dbMock.when(Database::getInstance).thenReturn(fakeDatabase);
            //Si dice al mock di restituire l'oggetto fakeDatabase quando il metodo statico Database.getInstance() viene chiamato.
            // Questo emula il comportamento di ottenere un'istanza del database.

            // I seguenti due when configurano il mock database per far sì che pippo risulti già registrato
            when(fakeDatabase.signUp(anyString(), any(User.class))).thenReturn(true);
            // Questa riga configura il mock fakeDatabase in modo che qualsiasi chiamata al metodo register con
            // qualsiasi valore di stringa e qualsiasi oggetto User restituisca true.
            // Questo simula una registrazione di successo.
            when(fakeDatabase.signUp(eq("giangilberto"), any(User.class))).thenReturn(false);
            // Questa riga configura il mock fakeDatabase in modo che, se il metodo register viene chiamato con
            // l'username "pippo", qualsiasi oggetto User restituirà false.
            // Questo simula una registrazione fallita per un nome utente specifico perché pippo e' gia' registrato.
            boolean result = service.signUp("giangilberto", testUser);
            // In questa riga, il metodo service.registration("pippo", testUser) viene chiamato,
            // simulando la registrazione con l'username "pippo".
            // Il risultato viene memorizzato nella variabile result.
            assertFalse(result);
            // Questa asserzione verifica che result sia false, il che significa che la registrazione con
            // l'username "pippo" è stata simulata come fallita, come configurato nei passi precedenti.
        }
        //quando esco dal try viene chiamato in automatico il metodo close() di dbMock, evita conflitti con mock in altri test e sono sicuro che le risorse siano state rilasciate
    }

    @Test
    public void testSuccesfulSignUp() {
        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(Database::getInstance).thenReturn(fakeDatabase);
            when(fakeDatabase.signUp(anyString(), any(User.class))).thenReturn(true);
            when(fakeDatabase.signUp(eq("giangilberto"), any(User.class))).thenReturn(false);
            boolean result = service.signUp("teoteocoli", testUser);
            assertTrue(result);
        }
    }
}
