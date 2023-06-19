package dev.fesly.client.module.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.fesly.Nova;
import dev.fesly.client.module.api.Category;
import dev.fesly.client.module.api.Module;
import dev.fesly.client.module.api.ModuleInfo;
import dev.fesly.impl.event.impl.render.ShaderEvent;
import dev.fesly.impl.util.render.RenderUtil;
import dev.fesly.impl.util.shader.impl.KawaseBloom;
import dev.fesly.impl.util.shader.impl.KawaseBlur;
import dev.fesly.impl.value.impl.BooleanValue;
import dev.fesly.impl.value.impl.NumberValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.glfw.GLFW;

@EqualsAndHashCode(callSuper = true)
@Data
@ModuleInfo(
        name = "Beautify",
        description = "module.render.postprocessing.description",
        category = Category.RENDER,
        keyBind = GLFW.GLFW_KEY_M
)
public class PostProcessing extends Module {

    private final BooleanValue blur = new BooleanValue("Blur", this, true);
    private final NumberValue blurIterations = new NumberValue("Blur Iterations", this, 3, 1, 8, 1, () -> !blur.getValue());
    private final NumberValue blurOffset = new NumberValue("Blur Offset", this, 1, 1, 10, 1, () -> !blur.getValue());
    private final BooleanValue bloom = new BooleanValue("Bloom", this, true);
    private final NumberValue bloomIterations = new NumberValue("Bloom Iterations", this, 3, 1, 8, 1, () -> !bloom.getValue());
    private final NumberValue bloomOffset = new NumberValue("Bloom Offset", this, 1, 1, 10, 1, () -> !bloom.getValue());


    public void stuffToBlur(boolean bloom) {


    }

    private Framebuffer stencilFramebuffer = new Framebuffer(mw.getFramebufferWidth(), mw.getFramebufferHeight(), true);

    public void blurScreen(MatrixStack matrix) {
        if (!isEnabled() || mc.gameSettings.showDebugInfo) return;
        if (blur.getValue()) {
            matrix.push();
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);

            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(true);
            Nova.getInstance().getEventBus().handle(new ShaderEvent(matrix, false));
            stuffToBlur(false);
            stencilFramebuffer.unbindFramebuffer();
            KawaseBlur.renderBlur(matrix, stencilFramebuffer.framebufferTexture, blurIterations.getValue().intValue(), blurOffset.getValue().intValue());
            matrix.pop();
        }


        if (bloom.getValue()) {
            matrix.push();
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(true);
            Nova.getInstance().getEventBus().handle(new ShaderEvent(matrix, true));
            stuffToBlur(true);
            stencilFramebuffer.unbindFramebuffer();
            KawaseBloom.renderBlur(matrix, stencilFramebuffer.framebufferTexture, bloomIterations.getValue().intValue(), bloomOffset.getValue().intValue());
            matrix.pop();
        }
    }

}