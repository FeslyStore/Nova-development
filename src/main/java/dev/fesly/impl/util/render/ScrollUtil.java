package dev.fesly.impl.util.render;

import dev.fesly.impl.interfaces.game.IMinecraft;
import dev.fesly.impl.interfaces.game.IWindow;
import dev.fesly.impl.util.chat.ChatUtil;
import dev.fesly.impl.util.math.MathUtil;
import dev.fesly.impl.util.time.TimerUtil;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

@Getter
@Setter
public class ScrollUtil implements IMinecraft, IWindow {

    public double target, scroll, max = 25;
    public TimerUtil timerUtil = new TimerUtil();
    public boolean scrollingIsAllowed;

    public void onRender() {
        final float[] wheel = new float[1];

        GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                // обработка событий прокрутки колеса мыши
                ChatUtil.addText("Vertical: " + yoffset);
                ChatUtil.addText("Horizontal: " + xoffset);
                wheel[0] = (float) yoffset;
            }
        };
        glfwSetScrollCallback(mw.getHandle(), scrollCallback);

        double stretch = 0;
        target = Math.min(Math.max(target + wheel[0] / 2, max - (wheel[0] == 0 ? 0 : stretch)), (wheel[0] == 0 ? 0 : stretch));

        //Moving render scroll towards target
        for (int i = 0; i < timerUtil.getTimePassed(); ++i) {
            scroll = MathUtil.lerp(scroll, target, 1E-2F);
        }

        //resetting timerUtil
        timerUtil.reset();


    }

    public void renderScrollBar(Vector2d position, double maxHeight) {
        double percentage = getScroll() / getMax();
        double scrollBarHeight = maxHeight - ((getMax() / (getMax() - maxHeight)) * maxHeight);

        scrollingIsAllowed = scrollBarHeight < maxHeight;
        if (!scrollingIsAllowed) return;

        double scrollX = position.x;
        double scrollY = position.y + maxHeight * percentage - scrollBarHeight * percentage;

        RenderUtil.roundedRectangle(scrollX, scrollY, 1, scrollBarHeight, 1,
                ColorUtil.withAlpha(Color.WHITE, 100));
    }

    public void reset() {
        this.scroll = 0;
        this.target = 0;
    }
}
