package com.dlm.gwt.sample.cardmaster.shared.services;

import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("signUp")

public interface SignUpService extends RemoteService {

    boolean signUp(String username, User user);

}