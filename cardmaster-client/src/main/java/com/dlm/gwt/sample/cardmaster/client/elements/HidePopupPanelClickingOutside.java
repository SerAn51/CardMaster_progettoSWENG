package com.dlm.gwt.sample.cardmaster.client.elements;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class HidePopupPanelClickingOutside {

    private PopupPanel modalPanel;

    public HidePopupPanelClickingOutside(final PopupPanel modalPanel) {
        this.modalPanel = modalPanel;
    }

    public void initialize() {
        // Crea e mostra il GlassPanel
        HTMLPanel glassPanel = new HTMLPanel("<div class='glass-panel'></div>");
        FocusPanel focusPanel = new FocusPanel();
        focusPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (modalPanel != null && modalPanel.isShowing()) {
                    modalPanel.hide();
                }
                glassPanel.removeFromParent();
            }
        });
        focusPanel.addStyleName("headerGlass-panel");
        glassPanel.add(focusPanel);

        // Imposta lo z-index del modalPanel in modo che sia sopra il glassPanel
        modalPanel.getElement().getStyle().setZIndex(1000);

        RootPanel.get().add(glassPanel);
    }
}
