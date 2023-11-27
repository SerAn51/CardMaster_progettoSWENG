package database;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.server.CardFactoryCreator;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class Database {

    private static Database dbOnlyInstance; // dichiaro un'istanza del db statica, sarà la sola istanza del db ad
                                            // esistere

    private DB db; // questo e' il database vero e proprio

    List<Card> magicCards = new ArrayList<>();
    List<Card> pokemonCards = new ArrayList<>();
    List<Card> yugiohCards = new ArrayList<>();

    private Database() {
        // Private constructor
    }

    public static Database getInstance() {
        if (dbOnlyInstance == null) {
            dbOnlyInstance = new Database();
        }
        return dbOnlyInstance;
    }

    // metodo per creare o aprire il database se rispettivamente non esiste o e'
    // chiuso
    public synchronized void open() {
        String workingDirectory = System.getProperty("user.dir"); // Ottieni la directory di lavoro corrente
        String dbPath = workingDirectory + "/database.db"; // Crea il percorso completo al file
        if (db == null || db.isClosed()) {
            boolean dbWereNull = db == null;
            try {
                db = DBMaker.fileDB(dbPath)
                        .checksumHeaderBypass()
                        .closeOnJvmShutdown()
                        .make();
                if (dbWereNull) {
                    // Carica i Json nel database
                    uploadJson(workingDirectory, "Magic");
                    uploadJson(workingDirectory, "Pokemon");
                    uploadJson(workingDirectory, "Yugioh");
                }

            } catch (IOException e) {
                //System.out.println("Errore durante la lettura del json: " + e.getMessage());
            } catch (Exception e) {
                //System.out.println("Errore durante l'apertura del database: " + e.getMessage());
            }
        }
    }

    public void uploadJson(String workingDirectory, String gameName)
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        // Leggi il file JSON
        String lowercaseGameName = gameName.toLowerCase();
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(workingDirectory +
                "/src/main/java/database/json/" + lowercaseGameName + ".json"));

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            for (JsonElement cardElement : jsonArray) {
                JsonObject cardObject = cardElement.getAsJsonObject();

                /* Utilizzo la factory per creare le carte */
                CardFactoryCreator cardFactory = new CardFactoryCreator(cardObject, gameName);
                Card card = cardFactory.getCard();

                if (gameName.equalsIgnoreCase("Magic")) {
                    magicCards.add(card);
                } else if (gameName.equalsIgnoreCase("Pokemon")) {
                    pokemonCards.add(card);
                } else if (gameName.equalsIgnoreCase("Yugioh")) {
                    yugiohCards.add(card);
                } else {
                    // TODO: gestire bene l'errore
                    //System.out.println("ERRORE: IL NOME DEL GIOCO NON E' VALIDO");
                }

            }
            // salva nel db
            // open();
            Map<Integer, Card> map = (Map<Integer, Card>) db.hashMap(lowercaseGameName + "_cards").createOrOpen();
            int card_id = 0;
            if (gameName.equalsIgnoreCase("Magic")) {
                for (Card card : magicCards) {
                    map.put(card_id, card);
                    card_id++;
                }
            } else if (gameName.equalsIgnoreCase("Pokemon")) {
                for (Card card : pokemonCards) {
                    map.put(card_id, card);
                    card_id++;
                }
            } else if (gameName.equalsIgnoreCase("Yugioh")) {
                for (Card card : yugiohCards) {
                    map.put(card_id, card);
                    card_id++;
                }
            }
            db.commit();
            // close();
        }
    }

    // metodo per chiudere il database se esiste ed e' chiuso
    public synchronized void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
    }

    public boolean signUp(String username, User user) {
        Map<String, User> userMap = openUserMap();
        try {

            if (userMap.containsKey(username)) {
                return false;
            }

            userMap.put(username, user); // qui sfrutto il fatto che User sia serializable
            db.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close(); // Chiudi il database
        }
        return true;
    }

    public User login(String username, String password) {
        User loggedUser = null;
        Map<String, User> map = openUserMap(); // questo gia' apre il db

        try {

            if (map != null && map.containsKey(username)) {
                User dbUser = map.get(username);

                // Verifica della password senza case-insensitivity
                if (dbUser.getPassword().equals(password)) {
                    loggedUser = dbUser;
                }
            }

        } finally {
            close();

        }

        return loggedUser;
    }

    // Input: magic, pokemon, yugioh
    public List<Card> getCards(String gameName) {
        String lowercaseGameName = gameName.toLowerCase();

        try {
            open();

            // Recupera la mappa dalla base di dati
            Map<Integer, Card> map = db.hashMap(lowercaseGameName + "_cards")
                    .keySerializer(Serializer.INTEGER)
                    .valueSerializer(Serializer.JAVA)
                    .createOrOpen();

            // Estrai le carte dalla mappa e crea una lista
            List<Card> cardList = new ArrayList<>();

            for (Card card : map.values()) {
                cardList.add(card);
            }
            return cardList;
        } finally {
            close(); // Chiudi il database nel blocco finally per garantire la chiusura anche in caso
                     // di eccezioni
        }
    }

    /* ++ INIZIO METODI DI SUPPORTO ++ */

    // metodo generico per ritornare la mappa di utenti
    public Map<String, User> openUserMap() {
        Map<String, User> userMap = null;
        try {
            open(); // Apri il database
            userMap = (Map<String, User>) db.hashMap("user").createOrOpen();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMap;
    }

    // metodo generico per aggiornare i dati di un utente nel database (update delle
    // condizioni delle carte, nuovi deck aggiunti, ecc.)
    public Boolean saveChangesInDB(User userToUpdate) {
        Boolean success = false;
        Map<String, User> userMap = openUserMap();
        try {

            String username = userToUpdate.getUsername();

            // Aggiorna la mappa dell'utente
            userMap.put(username, userToUpdate);
            db.commit(); // Salva le modifiche nel database

            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return success;
    }

    public User getUserByUsername(String username) {
        // accedi alla mappa di utenti e ritorna l'utente con username passato come
        // parametro
        User user = null;
        Map<String, User> userMap = openUserMap();
        try {
            user = userMap.get(username);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return user;
    }

    /* -- FINE METODI DI SUPPORTO -- */
}