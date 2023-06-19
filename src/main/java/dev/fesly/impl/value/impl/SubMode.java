package dev.fesly.impl.value.impl;

import dev.fesly.impl.value.Mode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubMode extends Mode {
    public SubMode(String name) {
        super(name, null);
    }
}