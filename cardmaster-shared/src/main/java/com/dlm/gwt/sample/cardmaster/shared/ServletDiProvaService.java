package com.dlm.gwt.sample.cardmaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/*
* Interfaccia RPC che estende `RemoteService` e utilizza l'annotazione `RemoteServiceRelativePath`
* per specificare il percorso relativo del servlet.
* Ogni interfaccia Service viene affiancata dalla sua versione asincrona
*/
@RemoteServiceRelativePath("/app/servletDiProva")
public interface ServletDiProvaService extends RemoteService {
    String metodoDiTest();
}