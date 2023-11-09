package com.dlm.gwt.sample.cardmaster.server;


import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.dlm.gwt.sample.cardmaster.shared.services.DatabaseService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import database.Database;

import java.util.List;

/**
    Si e' optato per l'implementazione del service e il non utilizzo della gia' esistente
    classe Database.java per mettere a disposizione del client solo quei metodi
    che effettivamente deve poter utilizzare (es. non viene data la possibilità di usare open, login, ecc.)
**/
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {

    @Override
    public List<Card> getMagicCards() {
        System.out.println("SONO DatabaseServiceImpl, STO AVVIANDO getMagicCards");
        return Database.getInstance().getMagicCards();
    }

}
