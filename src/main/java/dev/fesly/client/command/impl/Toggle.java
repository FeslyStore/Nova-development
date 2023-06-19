package dev.fesly.client.command.impl;

import dev.fesly.Nova;
import dev.fesly.client.command.Command;
import dev.fesly.client.module.api.Module;
import dev.fesly.impl.util.chat.ChatUtil;

public final class Toggle extends Command {

    public Toggle() {
        super("command.toggle.description", "toggle", "t");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length == 2) {
            final Module module = Nova.getInstance().getModuleManager().get(args[1]);

            if (module == null) {
                ChatUtil.addText("Invalid module");
                return;
            }

            module.toggle();
            ChatUtil.addText("Module " + (module.getModuleInfo().name() + " toggled " + (module.isEnabled() ? "on" : "off")));
        } else {
            error();
        }
    }
}