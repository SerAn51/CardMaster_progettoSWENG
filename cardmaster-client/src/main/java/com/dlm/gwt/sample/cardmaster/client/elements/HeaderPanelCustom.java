package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class HeaderPanelCustom {

    private ProfileModalPanel modal = null;
    private HTMLPanel glassPanel = null;
    private User loggedUser = SessionUser.getInstance().getSessionUser();
    private String gameNameHome;

    // Passare esattamente ""(stringa vuota) || Magic || Pokemon || Yugioh, e' case
    // sensitive
    public HeaderPanelCustom(String gameNameHome) {
        this.gameNameHome = gameNameHome;
    }

    public Panel createHeaderPanel() {

        ViewRouter viewRouter = new ViewRouter();

        // Header con il nome "CardMaster", il nome utente e un'icona cliccabile
        FlexTable headerPanel = new FlexTable();
        headerPanel.setStyleName("headerPanel");

        Button logoButton = new Button("");
        logoButton.setStyleName("headerLogo");
        headerPanel.setWidget(0, 0, logoButton);

        // BOTTONE PROFILO - Crea un HorizontalPanel per contenere l'immagine e
        // l'etichetta
        HorizontalPanel panelWrapperProfileButtonElements = new HorizontalPanel();
        panelWrapperProfileButtonElements.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        // BOTTONE PROFILO - Crea un'immagine e imposta il suo URL
        HTML imageContainerProfileButton = new HTML("<div class='headerLogoutImage-container'></div>");
        // se la home e' di pokemon mostra un allenatore pokemon, ecc
        imageContainerProfileButton.setStyleName("headerProfileButtonImage" + this.gameNameHome);
        // BOTTONE PROFILO - Crea un'etichetta per il testo del pulsante
        Label profileLabel = new Label("Profilo");
        profileLabel.setStyleName("headerProfileLabel");
        // BOTTONE PROFILO - Aggiungi l'immagine e l'etichetta al pannello orizzontale
        panelWrapperProfileButtonElements.add(imageContainerProfileButton);
        panelWrapperProfileButtonElements.add(profileLabel);

        // BOTTONE PROFILO - Crea un pulsante e aggiungi il pannello completo al
        // pulsante
        Button profileButton = new Button();
        profileButton.setStyleName("headerProfileButton");
        profileButton.getElement().getStyle().setDisplay(Display.INLINE_BLOCK); // Per visualizzare il pulsante
                                                                                // correttamente
        profileButton.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE); // Per allineare l'immagine e
        // l'etichetta verticalmente
        profileButton.getElement().getStyle().setLineHeight(1.0, Unit.EM); // Imposta il line-height per centrare
        // l'immagine e l'etichetta

        // BOTTONE PROFILO - Aggiungi l'indicatore rosso se ci sono notifiche
        if (loggedUser.getExchangeProposals().size() > 0) {
            // BOTTONE PROFILO - Crea un elemento HTML per l'indicatore rosso
            HTML redDotIndicator = new HTML("<div class='red-dot-indicator'></div>");
            redDotIndicator.setStyleName("headerRedDotIndicator");

            // Aggiungi lo stile per posizionare l'indicatore nell'angolo in alto a destra
            // di panelWrapperProfileButtonElements
            panelWrapperProfileButtonElements.getElement().getStyle().setPosition(Position.RELATIVE);
            redDotIndicator.getElement().getStyle().setPosition(Position.ABSOLUTE);
            redDotIndicator.getElement().getStyle().setTop(-18, Unit.PX);
            redDotIndicator.getElement().getStyle().setRight(-28, Unit.PX);

            // Calcola il numero di proposte di scambio
            int numberOfProposals = loggedUser.getExchangeProposals().size();

            // Crea il testo da visualizzare nel red dot
            String labelText = (numberOfProposals > 9) ? "9+" : String.valueOf(numberOfProposals);
            HTML numberLabel = new HTML("<div class='number-label'>" + labelText + "</div>");
            numberLabel.getElement().getStyle().setPosition(Position.ABSOLUTE);
            if (numberOfProposals > 9) {
                numberLabel.getElement().getStyle().setTop(20, Unit.PCT);
                numberLabel.getElement().getStyle().setLeft(21, Unit.PCT);
            } else {
                numberLabel.getElement().getStyle().setTop(18, Unit.PCT);
                numberLabel.getElement().getStyle().setLeft(33, Unit.PCT);
            }
            redDotIndicator.getElement().appendChild(numberLabel.getElement());

            panelWrapperProfileButtonElements.add(redDotIndicator);
        }

        profileButton.getElement().appendChild(panelWrapperProfileButtonElements.getElement());

        // Mostra finestra pop up con il profilo utente
        profileButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (modal != null && modal.isShowing()) {
                    // Se la finestra è già aperta, chiudila
                    modal.hide();
                    modal = null;

                    if (glassPanel != null) {
                        // Rimuovi il GlassPanel se presente
                        glassPanel.removeFromParent();
                    }
                } else {
                    // Crea la finestra modale
                    modal = new ProfileModalPanel();
                    modal.addStyleName("headerCustom-modal"); // Aggiungi la classe show quando mostri la finestra
                                                              // modale
                    modal.setPopupPositionAndShow(new PositionCallback() {
                        @Override
                        public void setPosition(int offsetWidth, int offsetHeight) {
                            // Posiziona la finestra vicino al bottone
                            int left = profileButton.getAbsoluteLeft() + (profileButton.getOffsetWidth() / 2)
                                    - (modal.getOffsetWidth() / 2);
                            int top = profileButton.getAbsoluteTop() + profileButton.getOffsetHeight() + 10;
                            // Aggiungi 10 pixel di spazio sotto il bottone

                            modal.setPopupPosition(left, top);
                        }
                    });

                    // Crea e mostra il GlassPanel
                    glassPanel = new HTMLPanel("<div class='glass-panel'></div>");
                    FocusPanel focusPanel = new FocusPanel();
                    focusPanel.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            if (modal != null && modal.isShowing()) {
                                modal.hide();
                                modal = null;
                            }
                            glassPanel.removeFromParent();
                        }
                    });
                    focusPanel.addStyleName("headerGlass-panel");
                    glassPanel.add(focusPanel);

                    profileButton.getElement().getStyle().setZIndex(1000); // Imposta il valore di z-index del bottone
                                                                           // in modo che
                    // sia sopra il glassPanel
                    // Imposta lo z-index del modalPanel in modo che sia sopra il glassPanel
                    modal.getElement().getStyle().setZIndex(1000);

                    RootPanel.get().add(glassPanel);
                }
            }
        });

        // BOTTONE LOGOUT - Crea un HorizontalPanel per contenere l'immagine e
        // l'etichetta
        HorizontalPanel panelWrapperLogoutButtonElements = new HorizontalPanel();
        // BOTTONE LOGOUT - Crea un'immagine e imposta il suo URL
        HTML imageContainerLogoutButton = new HTML("<div class='headerLogoutImage-container'></div>");
        imageContainerLogoutButton.setStyleName("headerLogoutImage");
        // BOTTONE LOGOUT - Crea un'etichetta per il testo del pulsante
        Label logoutLabel = new Label("Logout");
        logoutLabel.setStyleName("headerLogoutLabel");
        // BOTTONE LOGOUT - Aggiungi l'immagine e l'etichetta al pannello orizzontale
        panelWrapperLogoutButtonElements.add(imageContainerLogoutButton);
        panelWrapperLogoutButtonElements.add(logoutLabel);
        // BOTTONE LOGOUT - Crea un pulsante e aggiungi il pannello orizzontale al
        // pulsante
        Button logoutButton = new Button();
        logoutButton.setStyleName("headerLogoutButton");
        logoutButton.getElement().getStyle().setDisplay(Display.INLINE_BLOCK); // Per visualizzare il pulsante
                                                                               // correttamente
        logoutButton.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE); // Per allineare l'immagine e
                                                                                     // l'etichetta verticalmente
        logoutButton.getElement().getStyle().setLineHeight(1.5, Unit.EM); // Imposta il line-height per centrare
                                                                          // l'immagine e l'etichetta
        logoutButton.getElement().appendChild(panelWrapperLogoutButtonElements.getElement());


        Panel buttonPanel = new HorizontalPanel();
        buttonPanel.setStyleName("headerButtonPanel");
        buttonPanel.add(profileButton);
        buttonPanel.add(logoutButton);

        headerPanel.setWidget(0, 1, buttonPanel);

        headerPanel.getFlexCellFormatter().setWidth(0, 0, "80%"); // LogoButton
        headerPanel.getFlexCellFormatter().setWidth(0, 1, "20%"); // ProfileButton

        logoButton.addClickHandler(event -> logoHome(viewRouter));
        logoutButton.addClickHandler(event -> logout(viewRouter));

        return headerPanel;
    }

    private void logout(ViewRouter viewRouter) {
        // torna alla pagina di login impostando a null il valore dell'utente di
        // sessione
        SessionUser.getInstance().setSessionUser(null);
        changeRoute("login", viewRouter);
    }

    private void logoHome(ViewRouter viewRouter) {
        changeRoute("home", viewRouter);
    }

    private void changeRoute(String token, ViewRouter viewRouter) {
        History.newItem(token);
        viewRouter.handleRouteChange(token);// vai a vedere la url perche' ho cambiato il token
    }
}
