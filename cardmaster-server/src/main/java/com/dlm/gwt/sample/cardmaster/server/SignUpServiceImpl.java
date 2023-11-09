package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.services.SignUpService;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import database.Database;

/*
 * Questa classe è un servlet (estende RemoteServiceServlet) che implementa la rispettiva interfaccia Service (in shared)
 */
public class SignUpServiceImpl extends RemoteServiceServlet implements SignUpService {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean signUp(String username, User user) {
        return Database.getInstance().signUp(username, user);
    }
}