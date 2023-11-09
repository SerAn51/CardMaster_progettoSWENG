package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ModalPanel extends PopupPanel {

    public ModalPanel(User loggedUser) {
        // Contenuto della finestra modale
        VerticalPanel content = new VerticalPanel();
        content.setStyleName("custom-modal"); // Applica la classe CSS al pannello

        // Aggiungi il profilo card come parte del contenuto
        FlowPanel cardContainer = new FlowPanel();
        cardContainer.addStyleName("headerModal-panelCard-container");

        HTML imageContainerProfile = new HTML("<div class='profileImage-container'></div>");
        imageContainerProfile.setStyleName("headerModal-stockProfileImage");

        Label username = new Label(loggedUser.getUsername());
        Label email = new Label("Email: " + loggedUser.getEmail());
        Label age = new Label("Age: " + loggedUser.getAge());
        Label gender = new Label("Gender: " + loggedUser.getGender());
        Label news = new Label("Notification: " + loggedUser.getNews());
        username.setStyleName("headerModal-profileUsernameLabel");
        email.setStyleName("profileLabel");
        age.setStyleName("profileLabel");
        gender.setStyleName("profileLabel");
        news.setStyleName("profileLabel");

        cardContainer.add(imageContainerProfile);
        cardContainer.add(username);
        cardContainer.add(email);
        cardContainer.add(age);
        cardContainer.add(gender);
        cardContainer.add(news);

        content.add(cardContainer);

        setWidget(content);
    }
}