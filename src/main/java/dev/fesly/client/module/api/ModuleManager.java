package dev.fesly.client.module.api;

import dev.fesly.Nova;
import dev.fesly.client.module.impl.render.ClickGUI;
import dev.fesly.client.module.impl.render.Interface;
import dev.fesly.client.module.impl.render.PostProcessing;
import dev.fesly.impl.event.Listener;
import dev.fesly.impl.event.Priorities;
import dev.fesly.impl.event.annotations.EventLink;
import dev.fesly.impl.event.impl.input.KeyboardInputEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public final class ModuleManager extends ArrayList<Module> {

    public void init() {

        add(new Interface());
        add(new PostProcessing());
        add(new ClickGUI());

        // Automatic initializations
        this.stream().filter(module -> module.getModuleInfo().autoEnabled()).forEach(module -> module.setEnabled(true));

        // Has to be a listener to handle the key presses
        Nova.getInstance().getEventBus().register(this);
    }

    public List<Module> getAll() {
        return new ArrayList<>(this);
    }

    public <T extends Module> T get(final String name) {
        return this.stream()
                .filter(module -> module.getDisplayName().equalsIgnoreCase(name))
                .map(module -> (T) module)
                .findAny().orElse(null);
    }

    public <T extends Module> T get(final Class<T> clazz) {
        return this.stream()
                .filter(module -> module.getClass() == clazz)
                .map(module -> (T) module)
                .findAny().orElse(null);
    }

    public List<Module> get(final Category category) {
        return this.stream()
                .filter(module -> module.getModuleInfo().category() == category)
                .collect(Collectors.toList());
    }

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<KeyboardInputEvent> onKey = event -> {

        if (event.getScreen() != null) return;

        this.stream()
                .filter(module -> module.getKeyCode() == event.getKeyCode())
                .forEach(Module::toggle);
    };

    @Override
    public boolean add(final Module module) {
        final boolean result = super.add(module);
        this.updateArraylistCache();
        return result;
    }

    @Override
    public void add(final int i, final Module module) {
        super.add(i, module);
        this.updateArraylistCache();
    }

    @Override
    public Module remove(final int i) {
        final Module result = super.remove(i);
        this.updateArraylistCache();
        return result;
    }

    @Override
    public boolean remove(final Object o) {
        final boolean result = super.remove(o);
        this.updateArraylistCache();
        return result;
    }


    private void updateArraylistCache() {
        final Interface interfaceModule = this.get(Interface.class);
        if (interfaceModule == null) return;

        interfaceModule.createArrayList();
    }
}