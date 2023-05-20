package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.ServletDiProvaService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/*
* Questa classe è un servlet (estende RemoteServiceServlet) che implementa la rispettiva interfaccia Service (in shared)
*/
public class ServletDiProvaServiceImpl extends RemoteServiceServlet implements ServletDiProvaService {
    public String metodoDiTest() {
        return "Questo è un servlet di prova";
    }
}