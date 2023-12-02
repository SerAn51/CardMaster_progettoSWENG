package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
        int numberOfExchangeProposals = loggedUser.getExchangeProposals().size();
        Label exchangeNotification = new Label("Notifiche: hai " + numberOfExchangeProposals
                + (numberOfExchangeProposals == 1 ? " proposta" : " proposte") + " di scambio");
        // CODICE BOTTONE PAGINA EXCHANGE
        Button exchangePageButton = createExchangeButton();

        // FINE CODICE BOTTONE PAGINA EXCHANGE

        exchangePageButton.addClickHandler(event -> {
            String token = "exchange";
            History.newItem(token);
            new ViewRouter().handleRouteChange(token);
        });

        username.setStyleName("headerModal-profileUsernameLabel");
        email.setStyleName("profileLabel");
        age.setStyleName("profileLabel");
        gender.setStyleName("profileLabel");
        exchangeNotification.setStyleName("profileLabel");

        cardContainer.add(imageContainerProfile);
        cardContainer.add(username);
        cardContainer.add(email);
        cardContainer.add(age);
        cardContainer.add(gender);
        cardContainer.add(exchangeNotification);
        cardContainer.add(exchangePageButton);

        content.add(cardContainer);

        setWidget(content);
    }

    private Button createExchangeButton() {

        HorizontalPanel exchangeButtonContainer = new HorizontalPanel();

        HTML imageExchangeButton = new HTML("<div class='exchangeImage-container'></div>");
        imageExchangeButton.setStyleName("exchangeButtonImage");

        Label exchangeButtonLabel = new Label("Vai a exchange");
        exchangeButtonLabel.setStyleName("exchangeButtonLabel");

        exchangeButtonContainer.add(imageExchangeButton);
        exchangeButtonContainer.add(exchangeButtonLabel);

        Button exchangeButton = new Button();
        exchangeButton.setStyleName("exchangeButton");

        exchangeButton.getElement().appendChild(exchangeButtonContainer.getElement());

        return exchangeButton;
    }
}