package dev.fesly.impl.util.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.fesly.impl.interfaces.game.IMinecraft;
import dev.fesly.impl.interfaces.game.IWindow;
import dev.fesly.impl.util.shader.impl.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Code by Intave
 **/

public class RenderUtil implements IMinecraft, IWindow {

    /**
     * Better to use gl state manager to avoid bugs
     */
    public static void start() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture();
        GlStateManager.disableCull();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableDepthTest();
    }

    /**
     * Better to use gl state manager to avoid bugs
     */
    public static void stop() {
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableCull();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.color4f(1, 1, 1, 1);
    }

    public static void setColor(Color color) {
        if (color == null) color = Color.white;
        GL11.glColor4d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d, color.getAlpha() / 255d);
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void color(Color color, float alpha) {
        if (color == null) color = Color.white;
        color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha / 255f);
    }

    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255f);
    }

    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255f;
        float g = (float) (color >> 8 & 255) / 255f;
        float b = (float) (color & 255) / 255f;
        GlStateManager.color4f(r, g, b, alpha / 255f);
    }

    public static void drawLine(float x1, float y1, float x2, float y2, Color color) {
        GlStateManager.pushMatrix();
        RenderUtil.start();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderUtil.stop();
        GlStateManager.popMatrix();
    }

    public static void drawPoint(float x, float y, Color color) {
        GlStateManager.pushMatrix();
        RenderUtil.start();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderUtil.stop();
        GlStateManager.popMatrix();
    }

    public static void rectangle(final double x, final double y, final double width, final double height, final Color color) {
        start();

        if (color != null) {
            ColorUtil.glColor(color);
        }

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x, y + height);
        GL11.glEnd();

        stop();
    }

    public static void roundedRectangleNoGl(double x, double y, double width, double height, double radius, Color color) {
        start();

        if (color != null) {
            ColorUtil.glColor(color);
        }

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x, y + height);
        GL11.glEnd();

        stop();
    }

    public static void dropShadow(final int loops, final double x, final double y, final double width, final double height, final double opacity, final double edgeRadius) {
        GlStateManager.alphaFunc(516, 0);
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();

        for (int margin = 1; margin <= loops; ++margin) {
            final double mariner = margin * 2;
            roundedRectangle(x - mariner / 2f, y - mariner / 2f,
                    width + mariner, height + mariner, edgeRadius,
                    new Color(0, 0, 0, (int) Math.max(0.5f, (opacity - mariner * 1.2) / 5.5f)));
        }
    }

    private static final RoundedGradientQuadShader ROUNDED_GRADIENT_QUAD_SHADER = new RoundedGradientQuadShader();

    public static void roundedGradientRectangle(double x, double y, double width, double height, double radius, Color color1, Color color2, Color color3, Color color4) {
        ROUNDED_GRADIENT_QUAD_SHADER.draw(x, y, width, height, radius, color1, color2, color3, color4);
    }

    private static final RoundedQuadShader ROUNDED_QUAD_SHADER = new RoundedQuadShader();

    public static void roundedRectangle(double x, double y, double width, double height, double radius, Color color) {
        ROUNDED_QUAD_SHADER.draw(x, y, width, height, radius, color);
    }

    private static Framebuffer stencilFramebuffer = new Framebuffer(mw.getFramebufferWidth(), mw.getFramebufferHeight(), true);

    public static void glowRoundedRectangle(MatrixStack matrix, double x, double y, double width, double height, double radius, Color color, int iterations, int offset) {
        matrix.push();
        roundedRectangle(x, y, width, height, radius, color);
        int clampedIterations = Math.max(1, Math.min(10, iterations));
        int clampedOffset = Math.max(1, Math.min(10, offset));
        stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(true);
        roundedRectangle(x, y, width, height, radius, new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
        stencilFramebuffer.unbindFramebuffer();
        KawaseBloom.renderBlur(matrix, stencilFramebuffer.framebufferTexture, clampedIterations, clampedOffset);
        matrix.pop();
    }

    public static void applyBloom(MatrixStack matrix, int iterations, int offset, Runnable runnable) {
        matrix.push();
        int clampedIterations = Math.max(1, Math.min(10, iterations));
        int clampedOffset = Math.max(1, Math.min(10, offset));
        stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(true);
        runnable.run();
        stencilFramebuffer.unbindFramebuffer();
        KawaseBloom.renderBlur(matrix, stencilFramebuffer.framebufferTexture, clampedIterations, clampedOffset);
        matrix.pop();
    }

    public static void applyBlur(MatrixStack matrix, int iterations, int offset, Runnable runnable) {
        matrix.push();
        int clampedIterations = Math.max(1, Math.min(10, iterations));
        int clampedOffset = Math.max(1, Math.min(10, offset));
        stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(true);
        runnable.run();
        stencilFramebuffer.unbindFramebuffer();
        KawaseBlur.renderBlur(matrix, stencilFramebuffer.framebufferTexture, clampedIterations, clampedOffset);
        matrix.pop();
    }

    private static final RoundedOutlineQuadShader ROUNDED_OUTLINE_QUAD_SHADER = new RoundedOutlineQuadShader();

    public static void roundedOutlineRectangle(double x, double y, double width, double height, double radius, double borderSize, Color color) {
        ROUNDED_OUTLINE_QUAD_SHADER.draw(x, y, width, height, radius, borderSize, color);
    }

    public static void rainbowRectangle(final double x, final double y, final double width, final double height) {
        start();

        GL11.glBegin(GL11.GL_QUADS);

        for (double position = x; position <= x + width; position += 0.5) {
            setColor(Color.getHSBColor((float) ((position - x) / width), 1, 1));

            GL11.glVertex2d(position, y);
            GL11.glVertex2d(position + 0.5f, y);
            GL11.glVertex2d(position + 0.5f, y + height);
            GL11.glVertex2d(position, y + height);
        }

        GL11.glEnd();

        stop();
    }

    public static void drawLine(double x, double y, double z, double x1, double y1, double z1, final Color color, final float width) {

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(width);

        setColor(color);
        GL11.glBegin(2);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    public static void renderBox(double x, double y, double z, LivingEntity entity, Color color) {
        double width = (entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX + 0.2) / 2;
        double height = (entity.getRenderBoundingBox().maxY - entity.getRenderBoundingBox().minY + 0.2);

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1);
        RenderUtil.color(entity.hurtTime > 0 ? new Color(1.0f, 0.0f, 0.0f).getRGB() : color.getRGB(), 1);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void renderFilledBox(double x, double y, double z, LivingEntity entity, Color color) {
        double width = (entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX + 0.2) / 2;
        double height = (entity.getRenderBoundingBox().maxY - entity.getRenderBoundingBox().minY + 0.2);

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1);
        RenderUtil.color(entity.hurtTime > 0 ? new Color(1.0f, 0.0f, 0.0f).getRGB() : color.getRGB());
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void polygon(final double x, final double y, double radius, final double sides,
                               final boolean filled, final Color color) {
        radius /= 2;
        start();
        if (color != null)
            setColor(color);
        if (!filled) GL11.glLineWidth(2);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);
        {
            for (double i = 0; i <= sides; i++) {
                final double angle = i * (Math.PI * 2) / sides;
                GL11.glVertex2d(x + (radius * Math.cos(angle)) + radius, y + (radius * Math.sin(angle)) + radius);
            }
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stop();
    }

    public static void circle(final double x, final double y, final double radius, final boolean filled,
                              final Color color) {
        polygon(x, y, radius, 360, filled, color);
    }

    public static void triangle(final double x, final double y, final double sideLength, final boolean filled,
                                final Color color) {
        polygon(x, y, sideLength, 3, filled, color);
    }

    public static void drawCircle(final Entity entity, final double rad, final int color, final boolean shade) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDepthMask(false);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        if (shade) GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        final double x = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX);
        final double y = (entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) + Math.sin(System.currentTimeMillis() / 2E+2) + 1);
        final double z = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ);

        Color c;

        for (float i = 0; i < Math.PI * 2; i += Math.PI * 2 / 64.F) {
            final double vecX = x + rad * Math.cos(i);
            final double vecZ = z + rad * Math.sin(i);

            c = new Color(255, 255, 255);

            if (shade) {
                GL11.glColor4f(c.getRed() / 255.F,
                        c.getGreen() / 255.F,
                        c.getBlue() / 255.F,
                        0
                );
                GL11.glVertex3d(vecX, y - Math.cos(System.currentTimeMillis() / 2E+2) / 2.0F, vecZ);
                GL11.glColor4f(c.getRed() / 255.F,
                        c.getGreen() / 255.F,
                        c.getBlue() / 255.F,
                        0.85F
                );
            }
            GL11.glVertex3d(vecX, y, vecZ);
        }

        GL11.glEnd();
        if (shade) GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255, 255, 255);
    }

    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void resetColor() {
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer, boolean depth) {
        if (needsNewFramebuffer(framebuffer)) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mw.getFramebufferWidth(), mw.getFramebufferHeight(), depth);
        }
        return framebuffer;
    }

    public static boolean needsNewFramebuffer(Framebuffer framebuffer) {
        return framebuffer == null || framebuffer.framebufferWidth != mw.getFramebufferWidth() || framebuffer.framebufferHeight != mw.getFramebufferHeight();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        bufferBuilder.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }
}
