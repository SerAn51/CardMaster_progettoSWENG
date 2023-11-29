package com.dlm.gwt.sample.cardmaster.client.elements;

import com.dlm.gwt.sample.cardmaster.client.activity.HomeGameActivity;
import com.dlm.gwt.sample.cardmaster.shared.card.Card;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddOrUpdateCardConditionModalPanel extends PopupPanel {

    private ListBox conditionListBox = new ListBox();
    private TextBox descriptionTextBox = new TextBox();
    private int maxCharacters = 100;
    private Label charCountLabel;
    private HidePopupPanelClickingOutside hidePopup;

    // se e' owned (true) carico il pop up per aggiornare la condizione e la
    // descrizione
    // altrimenti la versione per aggiungere la condizione e la descrizione
    public AddOrUpdateCardConditionModalPanel(Card card, HomeGameActivity homeGameActivity, Boolean isOwned, HidePopupPanelClickingOutside hidePopup) {

        this.hidePopup = hidePopup;

        if (isOwned) {
            setTitle("Aggiorna condizione");
            updateConditionPopUp(card, homeGameActivity);
        } else {
            setTitle("Aggiungi condizione");
            addConditionPopUp(card, homeGameActivity);
        }

    }

    private void addConditionPopUp(Card card, HomeGameActivity homeGameActivity) {
        // Contenuto della finestra modale
        VerticalPanel content = new VerticalPanel();

        FlowPanel addDetailContainer = new FlowPanel();
        addDetailContainer.setStyleName("addCardConditionModalPanel");

        Label chooseConditionLabel = new Label("Seleziona condizione");
        conditionListBox.addItem("1 (molto rovinata)");
        conditionListBox.addItem("2 (parzialmente rovinata)");
        conditionListBox.addItem("3 (in buone condizioni)");
        conditionListBox.addItem("4 (in ottime condizioni)");
        conditionListBox.addItem("5 (in perfette condizioni)");
        Button confirmConditionButton = new Button("Conferma");

        addDetailContainer.add(chooseConditionLabel);
        addDetailContainer.add(conditionListBox);

        Label addDescriptionLabel = new Label("Aggiungi descrizione");
        descriptionTextBox.getElement().setPropertyString("placeholder", "Descrizione (max 100 caratteri)");
        descriptionTextBox.getElement().setAttribute("maxLength", String.valueOf(maxCharacters));

        // Aggiungere un gestore di eventi KeyUp per monitorare la lunghezza del testo
        descriptionTextBox.addKeyUpHandler(event -> updateCharCount());

        // Creare una label per visualizzare il conteggio dei caratteri
        charCountLabel = new Label();
        updateCharCount();

        addDetailContainer.add(addDescriptionLabel);
        addDetailContainer.add(descriptionTextBox);
        addDetailContainer.add(charCountLabel);
        addDetailContainer.add(confirmConditionButton);

        content.add(addDetailContainer);

        setWidget(content);

        confirmConditionButton.addClickHandler(event -> {
            card.setCondition(conditionListBox.getSelectedValue());
            card.setConditionDescription(descriptionTextBox.getText());
            hide();
            hidePopup.destroy();
            homeGameActivity.addCardToUserOwnedOrWished(card, true);
        });

    }

    private void updateConditionPopUp(Card card, HomeGameActivity homeGameActivity) {
        // Contenuto della finestra modale
        VerticalPanel content = new VerticalPanel();

        FlowPanel addDetailContainer = new FlowPanel();
        addDetailContainer.setStyleName("addCardConditionModalPanel");

        Label chooseConditionLabel = new Label("Seleziona condizione");
        String actualCondition = card.getCondition();
        Label actualConditionLabel = new Label("Condizione attuale: " + actualCondition);
        addDetailContainer.add(actualConditionLabel);
        // Prendi il primo carattere della stringa
        char firstChar = actualCondition.charAt(0);
        // Fai il cast del carattere a int (prende il valore ascii del carattere e lo
        // sottrae al valore ascii del carattere '0')
        int numberActualCondition = (int) firstChar - (int) '0';

        // la carta nel tempo puo' essere solo peggiorata nelle condizioni

        if (numberActualCondition == 1) {
            conditionListBox.setVisible(false);
            Label noConditionLabel = new Label("Non puoi peggiorare la condizione della carta");
            addDetailContainer.add(noConditionLabel);
        } else {
            for (int i = numberActualCondition - 1; i >= 0; i--) {
                if (i == 4) {
                    conditionListBox.addItem("4 (in ottime condizioni)");
                }
                if (i == 3) {
                    conditionListBox.addItem("3 (in buone condizioni)");
                }
                if (i == 2) {
                    conditionListBox.addItem("2 (parzialmente rovinata)");
                }
                if (i == 1) {
                    conditionListBox.addItem("1 (molto rovinata)");
                }
            }
        }
        Button confirmConditionButton = new Button("Conferma");

        addDetailContainer.add(chooseConditionLabel);
        addDetailContainer.add(conditionListBox);

        Label addDescriptionLabel = new Label("Aggiungi descrizione");
        if (card.getConditionDescription().isEmpty()) {
            descriptionTextBox.getElement().setPropertyString("placeholder", "Descrizione (max 200 caratteri)");
        } else {
            descriptionTextBox.getElement().setPropertyString("placeholder", card.getConditionDescription());
        }
        descriptionTextBox.getElement().setAttribute("maxLength", String.valueOf(maxCharacters));

        // Aggiungere un gestore di eventi KeyUp per monitorare la lunghezza del testo
        descriptionTextBox.addKeyUpHandler(event -> updateCharCount());

        // Creare una label per visualizzare il conteggio dei caratteri
        charCountLabel = new Label();
        updateCharCount();

        addDetailContainer.add(addDescriptionLabel);
        addDetailContainer.add(descriptionTextBox);
        addDetailContainer.add(charCountLabel);
        addDetailContainer.add(confirmConditionButton);

        content.add(addDetailContainer);

        setWidget(content);

        confirmConditionButton.addClickHandler(event -> {
            card.setCondition(conditionListBox.getSelectedValue());
            // aggiorno la descrizione solo se effettivamente l'utente ha scritto qualcosa
            if (!descriptionTextBox.getText().isEmpty()) {
                card.setConditionDescription(descriptionTextBox.getText());
            }
            hide();
            hidePopup.destroy();
            // TO DO: implementare logica update
            //homeGameActivity.updateCardCondition(card);
        });

    }

    private void updateCharCount() {
        // Ottenere il testo attuale dal campo di testo
        String text = descriptionTextBox.getText();

        // Aggiornare la label con il conteggio dei caratteri rimanenti
        charCountLabel.setText(text.length() + "/" + maxCharacters);
    }

}