package dev.fesly.impl.value.impl;

import dev.fesly.client.module.api.Module;
import dev.fesly.impl.value.Mode;
import dev.fesly.impl.value.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BooleanSupplier;


@Getter
@Setter
public class BoundsNumberValue extends Value<Number> {

    private final Number min;
    private final Number max;
    private final Number decimalPlaces;
    private Number secondValue;

    public BoundsNumberValue(final String name, final Module parent,
                             final Number defaultValue, final Number defaultSecondValue,
                             final Number min, final Number max, final Number step) {
        super(name, parent, defaultValue);
        this.decimalPlaces = step;

        this.min = min;
        this.max = max;
        this.secondValue = defaultSecondValue;
    }

    public BoundsNumberValue(final String name, final Mode<?> parent,
                             final Number defaultValue, final Number defaultSecondValue,
                             final Number min, final Number max, final Number step) {
        super(name, parent, defaultValue);
        this.decimalPlaces = step;

        this.min = min;
        this.max = max;
        this.secondValue = defaultSecondValue;
    }

    public BoundsNumberValue(final String name, final Module parent,
                             final Number defaultValue, final Number defaultSecondValue,
                             final Number min, final Number max, final Number step, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
        this.decimalPlaces = step;

        this.min = min;
        this.max = max;
        this.secondValue = defaultSecondValue;
    }

    public BoundsNumberValue(final String name, final Mode<?> parent,
                             final Number defaultValue, final Number defaultSecondValue,
                             final Number min, final Number max, final Number step, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
        this.decimalPlaces = step;

        this.min = min;
        this.max = max;
        this.secondValue = defaultSecondValue;
    }

    @Override
    public List<Value<?>> getSubValues() {
        return null;
    }

}
