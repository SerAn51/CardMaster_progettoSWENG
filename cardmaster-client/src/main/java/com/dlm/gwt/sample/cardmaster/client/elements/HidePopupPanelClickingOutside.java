package com.dlm.gwt.sample.cardmaster.client.elements;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class HidePopupPanelClickingOutside {

    HTMLPanel glassPanel = new HTMLPanel("<div class='glass-panel'></div>");

    public HidePopupPanelClickingOutside() {
    }

    public void initialize(PopupPanel modalPanel) {
        // Crea e mostra il GlassPanel
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

    // metodo per nascondere glassPanel
    public void destroy() {
        glassPanel.removeFromParent();
    }
}