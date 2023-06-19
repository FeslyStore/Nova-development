package dev.fesly.client.notification.impl;

import dev.fesly.client.notification.Notification;

public final class InfoNotification extends Notification {
    public InfoNotification(final String title, final String content, final long delay) {
        super(title, content, delay);
    }

    @Override
    public void render(final int multiplierY) {

    }

    @Override
    public boolean isEnded() {
        return false;
    }
}
