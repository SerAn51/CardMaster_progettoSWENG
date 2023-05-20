package com.dlm.gwt.sample.cardmaster.client;

import com.dlm.gwt.sample.cardmaster.shared.ServletDiProvaService;
import com.dlm.gwt.sample.cardmaster.shared.ServletDiProvaServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/*
* Classe entrypoint avviata da GWT quando si accede all'applicazione web
*/
public class CardMaster implements EntryPoint {

    /*
    * servletDiProvaService viene quindi inizializzata con l'istanza del servizio remoto asincrono generato da GWT.create().
    * Questa istanza può essere utilizzata per invocare i metodi definiti nell'interfaccia ServletDiProvaServiceAsync,
    * che a loro volta avvieranno le chiamate asincrone al server per eseguire le operazioni desiderate.
    */
    private final ServletDiProvaServiceAsync servletDiProvaService = GWT.create(ServletDiProvaService.class);

    /*
    * onModuleLoad è il "metodo main" di un'applicazione web GWT
    */
    @Override
    public void onModuleLoad() {
        VerticalPanel panel = new VerticalPanel();
        panel.add(new Label("Prova"));

        Button testButton = new Button("Clicca qui per testare il servlet");

        /*
        *  Al click del bottone viene chiamato il metodoDiTest dell'oggetto
        * servletDiProvaService per effettuare una chiamata asincrona al server.
        * Viene creato un oggetto AsyncCallback<String> che gestirà la risposta dalla chiamata asincrona.
        *
        * Nel metodo onSuccess dell'oggetto AsyncCallback (parametrizzato con String),
        * viene creato un oggetto di tipo Label con il valore result (la stringa ritornata dalla chiamata al server)
        * e viene aggiunto al pannello (panel) dell'interfaccia utente.
        *
        * Nel caso si verifichi un errore durante la chiamata asincrona e il metodo onFailure dell'oggetto AsyncCallback
        * venga chiamato, è possibile gestire l'errore in modo appropriato all'interno del suo blocco di codice.
        */
        testButton.addClickHandler(event -> {
            servletDiProvaService.metodoDiTest(new AsyncCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Label resultLabel = new Label(result);
                    panel.add(resultLabel);
                }

                @Override
                public void onFailure(Throwable caught) {
                    // Gestisci l'errore di conseguenza
                }
            });
        });

        panel.add(testButton);
        RootPanel.get().add(panel);
    }
}
