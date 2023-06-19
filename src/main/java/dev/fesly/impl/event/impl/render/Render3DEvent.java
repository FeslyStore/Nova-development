package dev.fesly.impl.event.impl.render;


import dev.fesly.impl.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Render3DEvent implements Event {
    private final float partialTicks;
}
