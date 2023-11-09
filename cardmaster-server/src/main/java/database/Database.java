package database;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.card.MagicCard;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Database {

    private static Database dbOnlyInstance; //dichiaro un'istanza del db statica, sarà la sola istanza del db ad esistere

    private DB db; //questo e' il database vero e proprio

    List<Card> magicCards = new ArrayList<>();//Card sara' una classe generica e ci saranno PokemonCard, MagicCard e YugiohCard
    //List<Card> pokemonCards = new ArrayList<>();
    //List<Card> yugiohCards = new ArrayList<>();

    private Database() {
        // Private constructor
    }

    public static Database getInstance() {
        if (dbOnlyInstance == null) {
            dbOnlyInstance = new Database();
        }
        return dbOnlyInstance;
    }

    //metodo per creare o aprire il database se rispettivamente non esiste o e' chiuso
    public void open() {
        System.out.println("---INIZIO OPEN---");
        String workingDirectory = System.getProperty("user.dir"); // Ottieni la directory di lavoro corrente
        String dbPath = workingDirectory + "/database.db"; // Crea il percorso completo al file
        if (db == null || db.isClosed()) {
            System.out.println("IL DB NON ESISTE O E' CHIUSO, SONO ENTRATO NELL'OPEN");
            boolean dbWereNull = db == null;
            try {
                /*
                String workingDirectory = System.getProperty("user.dir"); // Ottieni la directory di lavoro corrente
                String dbPath = workingDirectory + "/database.db"; // Crea il percorso completo al file
                */
                db = DBMaker.fileDB(dbPath)
                        .checksumHeaderBypass()
                        .closeOnJvmShutdown()
                        .make();
                System.out.println("HO FATTO .MAKE, QUINDI IL DB E' STATO CREATO O APERTO");

                System.out.println("STO CONTROLLANDO SE IL DB ESISTEVA");
                if(dbWereNull) {
                    System.out.println("IL DB ERA NULL, STO INIZIALIZZANDO I JSON");
                    //TODO: va fatto anche per pokemon e yugioh
                        // Leggi il file JSON
                        JsonElement jsonElement = JsonParser.parseReader(new FileReader(workingDirectory +
                                "/src/main/java/database/json/magic.json"));

                        if (jsonElement.isJsonArray()) {
                            JsonArray jsonArray = jsonElement.getAsJsonArray();

                            for (JsonElement cardElement : jsonArray) {
                                JsonObject cardObject = cardElement.getAsJsonObject();

                                String name = cardObject.get("name").getAsString();
                                boolean isReprint = cardObject.get("isReprint").getAsBoolean();
                                // Altri campi del JSON

                                Card card = new MagicCard(name, isReprint /* altri campi del JSON */);

                                magicCards.add(card);//e' un oggetto lista in RAM
                            }
                            //salva nel db
                            //open();//ricorsione che si interrompe subito
                            Map<Integer, Card> map = (Map<Integer, Card>) db.hashMap("magic_cards").createOrOpen();
                            int card_id = 0;
                            for (Card card : magicCards) {
                                map.put(card_id, card);
                                card_id++;
                            }
                            db.commit();
                            //close();
                            /*
                            FIXME: dovrebbe essere chiuso ma questo e' un inserimento di inizializzazione ma se lo chiudo
                            al termine del metodo che dovrebbe aprire il db, questo risulta chiuso
                             */
                            System.out.println("HO FINITO DI INIZIALIZZARE I JSON");
                        }

                }

            } catch (IOException e) {
                System.out.println("Errore durante la lettura del json: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Errore durante l'apertura del database: " + e.getMessage());
            }

            System.out.println("---FINE OPEN---");
        }
    }

    //metodo per chiudere il database se esiste ed e' chiuso
    public void close() {
        if (db != null && !db.isClosed()) {
            db.close();
            System.out.println("IL DB ORA E' CHIUSO");
        }
    }

    public boolean signUp(String username, User user) {
        try {
            open(); // Apri il database

            Map<String, User> map = (Map<String, User>) db.hashMap("user").createOrOpen();
            if (map.containsKey(username)) {
                return false;
            }

            map.put(username, user); //qui sfrutto il fatto che User sia serializable
            db.commit();
        } catch (Exception e) {
            System.out.println("L'errore e' nel register");
            e.printStackTrace();
            return false;
        } finally {
            close(); // Chiudi il database
        }
        return true;
    }

    public User login(String username, String password) {
        User loggedUser = null;
        try {
            open(); // Apri il database

            Map<String, User> map = (Map<String, User>) db.hashMap("user").createOrOpen();
            if (map.containsKey(username)) {
                User dbUser = map.get(username);
                if (dbUser.getPassword().equals(password)) {
                    //TODO: ritornare le carte possedute, desiderate e le notifiche dell'utente
                    loggedUser = dbUser;
                }
            }
            System.out.println("HO FATTO IL LOGIN, STO PER CHIUDERE IL DB");
            close(); // Chiudi il database

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loggedUser;
    }

}