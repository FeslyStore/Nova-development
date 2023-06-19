package dev.fesly.client.screen;

import dev.fesly.impl.interfaces.game.IMinecraft;
import dev.fesly.impl.interfaces.game.IWindow;

public interface IScreen extends IMinecraft, IWindow {

    void init();

    void draw(int mouseX, int mouseY, float partialTicks);

    void mouseClicked(int mouseX, int mouseY, int mouseButton);

    void mouseReleased(int mouseX, int mouseY, int state);

    void keyTyped(char typedChar, int keyCode);

    void update();

    void bloom();

    void onClosed();

}