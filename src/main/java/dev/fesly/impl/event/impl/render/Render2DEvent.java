package dev.fesly.impl.event.impl.render;


import com.mojang.blaze3d.matrix.MatrixStack;
import dev.fesly.impl.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.MainWindow;

@Getter
@AllArgsConstructor
public final class Render2DEvent implements Event {

    private final MatrixStack matrix;
    private final MainWindow mainWindow;
    private final float partialTicks;

}
