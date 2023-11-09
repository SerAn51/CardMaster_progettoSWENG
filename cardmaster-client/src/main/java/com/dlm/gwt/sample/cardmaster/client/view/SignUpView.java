package com.dlm.gwt.sample.cardmaster.client.view;

import com.dlm.gwt.sample.cardmaster.client.activity.SignUpActivity;
import com.dlm.gwt.sample.cardmaster.shared.services.SignUpService;
import com.dlm.gwt.sample.cardmaster.shared.services.SignUpServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class SignUpView extends Composite {

    private VerticalPanel mainPanel;
    // inizializzo i serviceAsync
    private SignUpServiceAsync signUpServiceAsync = GWT.create(SignUpService.class);

    // istanzio la Activity relativa alla pagina
    private SignUpActivity signUpActivity = new SignUpActivity(this, signUpServiceAsync);

    private TextBox usernameField = new TextBox();
    private TextBox emailField = new TextBox();
    private TextBox passwordField = new PasswordTextBox();
    private TextBox confirmPassword = new PasswordTextBox();
    private ListBox age = new ListBox();
    private ListBox gender = new ListBox();
    private Button signUpButton;
    private Button backButton;

    public SignUpView() {
        // Creazione del pannello principale che conterrà il contenuto
        try {
            FlowPanel container = new FlowPanel();
            
            // Aggiunta di una classe CSS per lo stile del pannello
            container.addStyleName("login_signUp_background");

            // Creazione del pannello principale del modulo di registrazione
            mainPanel = signUpForm();

            // Aggiunta del pannello principale al pannello contenitore
            container.add(mainPanel);

            // Aggiunta del pannello contenitore al RootLayoutPanel
            RootLayoutPanel.get().add(container);

            // Inizializzazione del widget della vista con il pannello contenitore
            initWidget(container);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public VerticalPanel signUpForm() {
        // Creazione di un pannello verticale per contenere il form
        VerticalPanel form = new VerticalPanel();
        VerticalPanel container = new VerticalPanel();
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        container.setStyleName("signUpContainer");

        form.setStyleName("signUpForm");
        form.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        form.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        Label signUpTitle = new Label("Sign Up");
        signUpTitle.setStyleName("loginTitle");
        form.add(signUpTitle);

        form.add(createField("Username:", usernameField));
        usernameField.setStyleName("loginInput");

        form.add(createField("Email:", emailField));
        emailField.setStyleName("loginInput");

        form.add(createField("Password:", passwordField));
        passwordField.setStyleName("loginInput");

        form.add(createField("Confirm Password:", confirmPassword));
        confirmPassword.setStyleName("loginInput");

        age.setWidth("130px");

        for (int i = 14; i < 100; i++) {
            age.addItem(i + "");
        }

        age.setStyleName("signUpInputListBox");

        gender.addItem("Maschio");
        gender.addItem("Femmina");
        gender.addItem("Altro");
        gender.setStyleName("signUpInputListBox");

        horizontalPanel.add(createField("Age:", age));
        horizontalPanel.add(createField("Gender:", gender));
        horizontalPanel.setStyleName("spaceAfter");

        form.add(horizontalPanel);

        // Creazione dei pulsanti
        signUpButton = new Button("Sign Up");
        backButton = new Button("Vuoi davvero tornare indietro?😞");

        // Aggiunta dei pulsanti al form
        form.add(signUpButton);
        signUpButton.setStyleName("loginSignUpButton");

        form.add(backButton);
        backButton.setStyleName("backButton");

        // Aggiunta del form al container
        container.add(form);

        // Aggiunta del formPanel al RootPanel
        RootPanel.get().add(container);

        signUpButton.addClickHandler(event -> signUpActivity.signUp());
        backButton.addClickHandler(event -> signUpActivity.goBack());

        return container;
    }

    // helper utilizzato per creare ciascun campo con la relativa etichetta
    private VerticalPanel createField(String labelName, Widget field) {
        VerticalPanel panel = new VerticalPanel();
        Label label = new Label(labelName);
        label.setStyleName("signUpLabel");
        panel.add(label);
        panel.add(field);
        return panel;
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getSecondPassword() {
        return confirmPassword.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getAge() {
        return age.getSelectedItemText();
    }

    public String getGender() {
        return gender.getSelectedItemText();
    }

    public void setSignUpHandler(ClickHandler click) {
        signUpButton.addClickHandler(click);
    }

    public void setBackHandler(ClickHandler click) {
        backButton.addClickHandler(click);
    }
}
