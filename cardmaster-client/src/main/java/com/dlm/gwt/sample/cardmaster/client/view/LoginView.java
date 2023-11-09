package com.dlm.gwt.sample.cardmaster.client.view;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.dlm.gwt.sample.cardmaster.shared.services.LoginService;
import com.dlm.gwt.sample.cardmaster.shared.services.LoginServiceAsync;
import com.dlm.gwt.sample.cardmaster.client.ViewRouter;
import com.dlm.gwt.sample.cardmaster.client.activity.LoginActivity;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/*
 * Classe entrypoint avviata da GWT quando si accede all'applicazione web
 */
public class LoginView extends Composite implements EntryPoint {

    private ViewRouter appPresenter = new ViewRouter();
    // inizializzo i serviceAsync
    private LoginServiceAsync loginServiceAsync = GWT.create(LoginService.class);

    // istanzio la Activity relativa alla pagina
    private LoginActivity loginActivity = new LoginActivity(this, loginServiceAsync);

    // istanzio gli elementi grafici della pagina
    private VerticalPanel loginFields;
    private TextBox emailField = new TextBox();
    private TextBox passwordField = new PasswordTextBox();

    /*
     * onModuleLoad è il "metodo main" di un'applicazione web GWT
     */

    @Override
    public void onModuleLoad() {
        FlowPanel container = new FlowPanel();
        container.addStyleName("login_signUp_background");
        // Creazione dei campi per il login
        loginFields = createLoginForm();
        container.add(loginFields);

        loginFields.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        loginFields.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        // Aggiungere il containerPanel alla RootLayoutPanel
        RootLayoutPanel.get().add(container);

        // Imposta il token iniziale nella history con il token desiderato
        String initialToken = "login"; // Token iniziale
        History.newItem(initialToken, false); // Imposta il token senza aggiornare l'URL
        // Aggiungi il ValueChangeHandler per gestire i cambiamenti nella history
        History.addValueChangeHandler(event -> appPresenter.handleRouteChange(event.getValue()));
    }

    // questo invece viene richiamato quando passo alla pagina di login NON
    // all'apertura della web app
    public LoginView() {
        FlowPanel container = new FlowPanel();
        container.addStyleName("login_signUp_background");
        // Creazione dei campi per il login
        loginFields = createLoginForm();
        container.add(loginFields);

        loginFields.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        loginFields.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        // Aggiungere il containerPanel alla RootLayoutPanel
        RootLayoutPanel.get().add(container);

        // Imposta il widget principale come contenuto del Composite
        initWidget(container);
    }

    private VerticalPanel createLoginForm() {
        // define panel
        VerticalPanel container = null;
        VerticalPanel loginFormPanel = null;
        HorizontalPanel logoPanel = null;
        try {

            container = new VerticalPanel();
            loginFormPanel = new VerticalPanel();
            loginFormPanel.setStyleName("loginPanel"); // Applica la classe CSS al pannello
            loginFormPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            loginFormPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

            /* Titolo e logo */
            logoPanel = new HorizontalPanel();
            HTML logo = new HTML("<div class='logoImage-container'></div>");
            logo.setStyleName("loginLogo");
            logoPanel.add(logo);
            loginFormPanel.add(logoPanel);

            Label loginTitle = new Label("Login");
            loginTitle.setStyleName("loginTitle"); // Applica la classe CSS al titolo
            loginFormPanel.add(loginTitle);

            /* Username e password */
            Label usernameLabel = new Label("Username:");
            usernameLabel.setStyleName("loginLabel");
            loginFormPanel.add(usernameLabel);
            emailField.setStyleName("loginInput"); // Applica la classe CSS al campo di loginInput
            loginFormPanel.add(emailField);
            Label passwordLabel = new Label("Password:");
            passwordLabel.setStyleName("loginLabel");
            loginFormPanel.add(passwordLabel);
            passwordField.setStyleName("loginInput"); // Applica la classe CSS al campo di input
            loginFormPanel.add(passwordField);

            /* Accedi */
            Button loginButton = new Button("Accedi");
            loginButton.setStyleName("loginSignUpButton"); // Applica la classe CSS al bottone di login

            loginFormPanel.add(loginButton);

            Label askTxt = new Label("Non hai un account?  ");
            askTxt.setStyleName("loginAskTxt");
            Hyperlink signupLink = new Hyperlink("Iscriviti ora!", "signUp");
            signupLink.setStyleName("loginSubscribeLink"); // Applica la classe CSS al link

            HorizontalPanel horizontalPanel = new HorizontalPanel();
            horizontalPanel.add(askTxt);
            horizontalPanel.add(signupLink);
            horizontalPanel.setStyleName("loginIscrivitiOra");
            loginFormPanel.add(horizontalPanel);

            // al click del bottone richiamo il metodo doLogin dell'activity corrispondente
            // a questa view
            loginButton.addClickHandler(event -> loginActivity.doLogin(emailField.getText(), passwordField.getText()));
            container.addStyleName("loginContainer");
            container.add(loginFormPanel);
        } catch (Exception e) {
            System.out.println("Eccezione in LoginView.createLoginForm()!");
            e.printStackTrace();
        }
        return container;
    }

    public void setLoginHandler(ClickHandler handler) {
        Button loginButton = (Button) loginFields.getWidget(4);
        loginButton.addClickHandler(handler);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}