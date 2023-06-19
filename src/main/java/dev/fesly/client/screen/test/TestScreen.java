package dev.fesly.client.screen.test;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.fesly.font.Fonts;
import dev.fesly.impl.interfaces.game.IMinecraft;
import dev.fesly.impl.interfaces.game.IWindow;
import dev.fesly.impl.util.render.RenderUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class TestScreen extends Screen implements IMinecraft, IWindow {

    public TestScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        double width = 540;
        double height = 320;
        double x = mw.getScaledWidth() / 2F - width / 2F;
        double y = mw.getScaledHeight() / 2F - height / 2F;

        RenderUtil.glowRoundedRectangle(matrixStack, x, y, width, height, 5, new Color(0, 0, 0, 90), 3, 1);

        Fonts.ROBOTO_MEDIUM.get(14).drawString(matrixStack, "Даша", x + 5, y + 5, -1);

    }
}
