package dev.fesly.client.notification;


import dev.fesly.impl.interfaces.game.IMinecraft;
import dev.fesly.impl.interfaces.game.IWindow;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Notification implements IMinecraft, IWindow {

    private final String title, content;
    private final long init = System.currentTimeMillis(), length;

    public abstract void render(int multiplierY);

    public abstract boolean isEnded();
}
