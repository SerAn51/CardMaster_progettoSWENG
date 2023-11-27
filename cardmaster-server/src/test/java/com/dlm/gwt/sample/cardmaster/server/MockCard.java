package com.dlm.gwt.sample.cardmaster.server;

import com.dlm.gwt.sample.cardmaster.shared.card.Card;

public class MockCard extends Card {
    public MockCard(String name, String type, String description) {
        super(name, type, description);
    }

    @Override
    public String getGame() {
        return "MockGame";
    }

    @Override
    public String toString() {
        // Implement toString as needed for testing
        return "MockCard{" +
                "name='" + getName() + '\'' +
                ", type='" + getType() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}