package com.dlm.gwt.sample.cardmaster.shared.services;

import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SignUpServiceAsync {
    void signUp(String username, User user, AsyncCallback<Boolean> callback);

}