package dev.fesly.client.notification;

import dev.fesly.Nova;
import dev.fesly.client.notification.impl.ErrorNotification;
import dev.fesly.client.notification.impl.InfoNotification;
import dev.fesly.client.notification.impl.WarningNotification;
import dev.fesly.font.Font;
import dev.fesly.font.Fonts;
import dev.fesly.impl.event.Listener;
import dev.fesly.impl.event.annotations.EventLink;
import dev.fesly.impl.event.impl.render.Render2DEvent;
import dev.fesly.impl.interfaces.game.IMinecraft;

import java.util.ArrayList;

public final class NotificationManager extends ArrayList<Notification> implements IMinecraft {
    private final Font font = Fonts.ROBOTO_MEDIUM.get(16);

    public void init() {
        Nova.getInstance().getEventBus().register(this);
    }

    public void register(final String title, final String content, final NotificationType type) {
        final long length = font.getStringWidth(content) * 30L;
        final Notification notification = switch (type) {
            case WARNING -> new WarningNotification(title, content, length);
            case ERROR -> new ErrorNotification(title, content, length);
            default -> new InfoNotification(title, content, length);
        };

        this.add(notification);
    }

    @EventLink()
    public final Listener<Render2DEvent> onRender = event -> {
        int i = 0;
        for (final Notification notification : this) {
            notification.render(i);
            ++i;
        }

        this.removeIf(Notification::isEnded);
    };

}
