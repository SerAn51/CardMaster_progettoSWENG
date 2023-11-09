package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.services.LoginService;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import database.Database;

/*
 * Questa classe è un servlet (estende RemoteServiceServlet) che implementa la rispettiva interfaccia Service (in shared)
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
    private static final long serialVersionUID = 1L;

    @Override
    public User login(String username, String password) {
        User loggedUser = Database.getInstance().login(username, password); //ritorna loggedUser
        return loggedUser;
    }

}
