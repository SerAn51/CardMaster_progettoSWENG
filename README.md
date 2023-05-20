# gwt-cardmaster-maven

The GWT CardMaster web application, maven flavor and JUnit testing.

Il progetto per l’anno accademico 2022/23 consiste nella realizzazione di una applicazione web che permette agli utenti di gestire e scambiare carte da gioco.

Le funzionalità dell'applicazione web sono:
- Gestione delle carte possedute: il sistema deve permettere ad un utente di aggiungere ed eliminare carte da gioco che possiede nella realtà al proprio account specificando in che condizioni si trova la carta in suo possesso. Le carte da gioco non vengono create dagli utenti. Il catalogo delle carte disponibili viene fornito in formato JSON (vedere appendice).
- Gestione delle carte desiderate: il sistema deve permettere ad un utente di aggiungere ed eliminare carte da gioco che vorrebbe avere. Come per le carte possedute queste devono essere scelte dal catalogo delle carte disponibili.
- Gestione di deck/mazzi: il sistema deve permettere all’utente di creare, modificare, ed eliminare deck/mazzi. Un deck è un contenitore di carte quindi, un deck contiene molte carte e una carta può essere contenuta in più deck.
- Ricerca/visualizzazione di carte: il sistema deve permettere ad un visitatore di ricercare carte che sono state aggiunte in precedenza al sistema.
- Scambio: il sistema deve permettere ad un utente di contattare un altro utente per proporre uno scambio e all’altro utente di decidere cosa fare.

To test it, run:

`mvn -U -e gwt:codeserver -pl *-client -am`

to execute the codeserver (just keep that running),
then you can use

`mvn -U jetty:run-forked -pl *-server -am -Denv=dev`

to run the application in developer mode (the URL is `http://localhost:8080/`). 
