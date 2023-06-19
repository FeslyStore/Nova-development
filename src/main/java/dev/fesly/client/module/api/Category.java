package dev.fesly.client.module.api;

import lombok.Getter;

@Getter
public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    GHOST("Ghost"),
    OTHER("Other");

    private final String name;

    Category(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

