package dev.fesly.client.module.impl.render;

import dev.fesly.Nova;
import dev.fesly.client.module.api.Category;
import dev.fesly.client.module.api.Module;
import dev.fesly.client.module.api.ModuleInfo;
import dev.fesly.client.screen.test.TestScreen;
import dev.fesly.font.Font;
import dev.fesly.font.Fonts;
import dev.fesly.impl.event.Listener;
import dev.fesly.impl.event.annotations.EventLink;
import dev.fesly.impl.event.impl.render.Render2DEvent;
import dev.fesly.impl.event.impl.render.ShaderEvent;
import dev.fesly.impl.util.animation.compact.Animation;
import dev.fesly.impl.util.animation.compact.Easing;
import dev.fesly.impl.util.shader.GradientUtil;
import dev.fesly.impl.value.impl.BooleanValue;
import lombok.Data;
import lombok.val;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Code by Intave
 **/

@ModuleInfo(
        name = "Interface",
        description = "",
        category = Category.RENDER,
        autoEnabled = true,
        allowDisable = false)
public class Interface extends Module {
    public BooleanValue moduleCase = new BooleanValue("Module Lower Case", this, false);
    public BooleanValue moduleSplit = new BooleanValue("Split Module Name", this, false);
    private final ArrayList<ModuleComponent> allModuleComponent = new ArrayList<>();
    private ArrayList<ModuleComponent> activeModuleComponents = new ArrayList<>();
    private final Font font = Fonts.ROBOTO_MEDIUM.get(16);

    private final Animation animation = new Animation(Easing.EASE_OUT_CUBIC, 150);
    private final Framebuffer stencilFramebuffer = new Framebuffer(mw.getFramebufferWidth(), mw.getFramebufferHeight(), true);


    public Interface() {
        createArrayList();
    }

    @EventLink
    public Listener<ShaderEvent> onShader = event -> {
        Nova client = Nova.getInstance();
        Color color1 = client.getThemeManager().getTheme().getFirstColor();
        Color color2 = client.getThemeManager().getTheme().getSecondColor();
        GradientUtil.applyGradientHorizontal(5, 5, Fonts.MANROPE_BOLD.get(40).getStringWidth(client.getClientInfo().getName()), Fonts.MANROPE_BOLD.get(40).getFontHeight(), 1, color1, color2, () ->
                Fonts.MANROPE_BOLD.get(40).drawString(event.getMatrix(), client.getClientInfo().getName(), 5, 5, -1));
    };

    @EventLink
    public Listener<Render2DEvent> onRender = event -> {
        sortArrayList();
        if (mc.gameSettings.showDebugInfo) return;

        if (mc.currentScreen instanceof ChatScreen) {
            animation.run(24);
        } else {
            animation.run(12);
        }

        if (InputMappings.isKeyDown(mw.getHandle(), GLFW.GLFW_KEY_DOWN))
            mc.displayScreen(new TestScreen(StringTextComponent.EMPTY));

        Nova client = Nova.getInstance();

        String user = client.getUser().username() + " §7|§f uid: " + client.getUser().uid();

        Color color1 = client.getThemeManager().getTheme().getFirstColor();
        Color color2 = client.getThemeManager().getTheme().getSecondColor();

        GradientUtil.applyGradientHorizontal(6, 6, Fonts.MANROPE_BOLD.get(40).getStringWidth(client.getClientInfo().getName()) + Fonts.MANROPE_BOLD.get(16).getStringWidth("Beta"), Fonts.MANROPE_BOLD.get(40).getFontHeight(), 1, color1.darker(), color2.darker(), () -> {
            Fonts.MANROPE_BOLD.get(40).drawString(event.getMatrix(), client.getClientInfo().getName(), 6, 6, -1);
            Fonts.MANROPE_BOLD.get(16).drawString(event.getMatrix(), "Beta", 5 + 0.5 + Fonts.MANROPE_BOLD.get(40).getStringWidth(client.getClientInfo().getName()), 5 + 0.5, -1);
        });


        GradientUtil.applyGradientHorizontal(5, 5, Fonts.MANROPE_BOLD.get(40).getStringWidth(client.getClientInfo().getName()) + Fonts.MANROPE_BOLD.get(16).getStringWidth("Beta"), Fonts.MANROPE_BOLD.get(40).getFontHeight(), 1, color1, color2, () -> {
            Fonts.MANROPE_BOLD.get(40).drawString(event.getMatrix(), client.getClientInfo().getName(), 5, 5, -1);
            Fonts.MANROPE_BOLD.get(16).drawString(event.getMatrix(), "Beta", 5 + Fonts.MANROPE_BOLD.get(40).getStringWidth(client.getClientInfo().getName()), 5, -1);
        });


        Fonts.MANROPE_BOLD.get(14).drawString(
                event.getMatrix(),
                user,
                event.getMainWindow().getScaledWidth() - Fonts.MANROPE_BOLD.get(14).getStringWidth(user) - 4,
                event.getMainWindow().getScaledHeight() - animation.getValue(),
                -1
        );

        float offset = 0;
        for (final ModuleComponent module : this.activeModuleComponents) {
            val lowercase = moduleCase.getValue() ? module.getModule().getModuleInfo().name().toLowerCase() : module.getModule().getModuleInfo().name();
            val name = moduleSplit.getValue() ? lowercase.replace(" ", "") : lowercase;
//            font.drawString(event.getMatrix(), name, 5, 40 + offset, -1);
            offset += font.getFontHeight();
        }

    };

    public void createArrayList() {
        allModuleComponent.clear();
        Nova.getInstance().getModuleManager().getAll().stream()
                .sorted(Comparator.comparingDouble(module -> -font.getStringWidth(moduleSplit.getValue()
                        ? moduleCase.getValue()
                        ? module.getModuleInfo().name().toLowerCase().replace(" ", "")
                        : module.getModuleInfo().name().replace(" ", "")
                        : moduleCase.getValue()
                        ? module.getModuleInfo().name().toLowerCase()
                        : module.getModuleInfo().name())))
                .forEach(module -> allModuleComponent.add(new ModuleComponent(module)));
    }

    public void sortArrayList() {
        activeModuleComponents = allModuleComponent.stream()
                .filter(moduleComponent -> moduleComponent.getModule().isEnabled())
                .sorted(Comparator.comparingDouble(module -> -font.getStringWidth(moduleSplit.getValue()
                        ? moduleCase.getValue()
                        ? module.getModule().getModuleInfo().name().toLowerCase().replace(" ", "")
                        : module.getModule().getModuleInfo().name().replace(" ", "")
                        : moduleCase.getValue()
                        ? module.getModule().getModuleInfo().name().toLowerCase()
                        : module.getModule().getModuleInfo().name())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Data
    public static class ModuleComponent {
        private Module module;

        public ModuleComponent(final Module module) {
            this.module = module;
        }

    }

}
