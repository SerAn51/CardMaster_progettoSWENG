package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.shared.user.SessionUser;
import com.dlm.gwt.sample.cardmaster.shared.user.User;
import com.google.gwt.dom.client.Style.Display;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class HeaderPanelCustom {

    private ProfileModalPanel modal = null;
    private HTMLPanel glassPanel = null;
    private User loggedUser;
    private String gameNameHome;

    // Passare esattamente ""(stringa vuota) || Magic || Pokemon || Yugioh, e' case
    // sensitive
    public HeaderPanelCustom(User loggedUser, String gameNameHome) {
        this.loggedUser = loggedUser;
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
        // BOTTONE PROFILO - Crea un pulsante e aggiungi il pannello orizzontale al
        // pulsante
        Button profileButton = new Button();
        profileButton.setStyleName("headerProfileButton");
        profileButton.getElement().getStyle().setDisplay(Display.INLINE_BLOCK); // Per visualizzare il pulsante
                                                                                // orrettamente
        profileButton.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE); // Per allineare l'immagine e
                                                                                      // l'etichetta verticalmente
        profileButton.getElement().getStyle().setLineHeight(1.5, Unit.EM); // Imposta il line-height per centrare
                                                                           // l'immagine e l'etichetta
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
                    modal = new ProfileModalPanel(loggedUser);
                    modal.addStyleName("headerCustom-modal"); // Aggiungi la classe show quando mostri la finestra
                                                              // modale
                    modal.setPopupPositionAndShow(new PositionCallback() {
                        @Override
                        public void setPosition(int offsetWidth, int offsetHeight) {
                            // Posiziona la finestra vicino al bottone
                            int left = profileButton.getAbsoluteLeft() + (profileButton.getOffsetWidth() / 2)
                                    - (modal.getOffsetWidth() / 2);
                            int top = profileButton.getAbsoluteTop() + profileButton.getOffsetHeight() + 10; // Aggiungi
                                                                                                             // 10 pixel
                                                                                                             // di
                                                                                                             // spazio
                                                                                                             // sotto il
                                                                                                             // bottone

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

        headerPanel.setWidget(0, 1, profileButton);

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

        headerPanel.setWidget(0, 2, logoutButton);

        headerPanel.getFlexCellFormatter().setWidth(0, 0, "80%"); // LogoButton
        headerPanel.getFlexCellFormatter().setWidth(0, 1, "10%"); // ProfileButton
        headerPanel.getFlexCellFormatter().setWidth(0, 2, "10%"); // LogoutButton

        logoButton.addClickHandler(event -> logoHome(viewRouter));
        logoutButton.addClickHandler(event -> logout(viewRouter));

        return headerPanel;
    }

    // TODO: singleton (statico), metodo in ViewRouter
    // TODO: metti in una activity
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
