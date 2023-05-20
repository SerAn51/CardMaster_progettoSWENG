package com.dlm.gwt.sample.cardmaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
* Questa interfaccia viene utilizzata per definire le operazioni che possono essere richiamate sul lato client
* e che verranno eseguite sul lato server in modo asincrono.
*
* Questa è un'implementazione asincrona dell'interfaccia ServletDiProvaService
*
* il metodo `metodoDiTest` non restituisce direttamente la stringa come valore di ritorno, ù
* ma invia la stringa al client attraverso l'oggetto `AsyncCallback<String> callback`.
*
* L'oggetto `AsyncCallback` è responsabile per la gestione della risposta asincrona dal server.
* Viene passato come argomento al metodo `metodoDiTest`, e quando il server ha completato l'esecuzione del metodo,
* chiama il metodo `onSuccess` dell'oggetto `AsyncCallback`, passando la stringa come parametro.
*
* Pertanto, non è necessario specificare il tipo di ritorno `String` nell'interfaccia asincrona
* `ServletDiProvaServiceAsync` perché il metodo in sé non restituisce direttamente la stringa,
* ma la passa attraverso l'oggetto `AsyncCallback`
*/
public interface ServletDiProvaServiceAsync {
    void metodoDiTest(AsyncCallback<String> callback);

    void metodoBoh(AsyncCallback<String> callback);
}
