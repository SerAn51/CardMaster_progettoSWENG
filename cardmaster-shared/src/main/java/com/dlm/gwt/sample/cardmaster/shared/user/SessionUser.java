package com.dlm.gwt.sample.cardmaster.shared.user;

public class SessionUser {

    // Singleton
    private User savedUser;
    private static SessionUser instance;

    private SessionUser() {
        // Private constructor
        savedUser = null;
    }

    public static SessionUser getInstance() {
        if (instance == null) {
            instance = new SessionUser();
        }
        return instance;
    }

    public User getSessionUser() {
        return savedUser;
    }

    public void setSessionUser(User loggedUser) {
        this.savedUser = loggedUser;
    }
}