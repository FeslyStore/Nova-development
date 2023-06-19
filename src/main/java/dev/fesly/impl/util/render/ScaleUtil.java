package dev.fesly.impl.util.render;

import dev.fesly.impl.interfaces.game.IMinecraft;
import dev.fesly.impl.interfaces.game.IWindow;
import org.lwjgl.opengl.GL11;

public class ScaleUtil implements IMinecraft, IWindow {
    public static float size = 2;

    public static void scale_pre() {
        final double scale = mw.getScaleFactor() / Math.pow(mw.getScaleFactor(), 2);
        GL11.glPushMatrix();
        GL11.glScaled(scale * size, scale * size, scale * size);
    }

    public static void scale_post() {
        GL11.glScaled(size, size, size);
        GL11.glPopMatrix();
    }

    public static int calc(int value) {
        return (int) (value * mw.getScaleFactor() / size);
    }

    public static int calc(double value) {
        return (int) (value * mw.getScaleFactor() / size);
    }

    public static double[] calc(double mouseX, double mouseY) {
        mouseX = mouseX * mw.getScaleFactor() / size;
        mouseY = mouseY * mw.getScaleFactor() / size;
        return new double[]{mouseX, mouseY};
    }
}
