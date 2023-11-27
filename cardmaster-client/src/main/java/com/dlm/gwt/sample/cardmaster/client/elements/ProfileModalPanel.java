package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileModalPanel extends PopupPanel {

    public ProfileModalPanel(User loggedUser) {
        // Contenuto della finestra modale
        VerticalPanel content = new VerticalPanel();

        // Aggiungi il profilo card come parte del contenuto
        FlowPanel cardContainer = new FlowPanel();
        cardContainer.addStyleName("headerModal-panelCard-container");

        HTML imageContainerProfile = new HTML("<div class='profileImage-container'></div>");
        imageContainerProfile.setStyleName("headerModal-stockProfileImage");

        Label username = new Label(loggedUser.getUsername());
        Label email = new Label("Email: " + loggedUser.getEmail());
        Label age = new Label("Eta': " + loggedUser.getAge());
        Label gender = new Label("Sesso: " + loggedUser.getGender());
        // CODICE BOTTONE PAGINA EXCHANGE

        // TODO: Button exchangePageButton;
        username.setStyleName("headerModal-profileUsernameLabel");
        email.setStyleName("profileLabel");
        age.setStyleName("profileLabel");
        gender.setStyleName("profileLabel");

        cardContainer.add(imageContainerProfile);
        cardContainer.add(username);
        cardContainer.add(email);
        cardContainer.add(age);
        cardContainer.add(gender);

        content.add(cardContainer);

        setWidget(content);
    }
}