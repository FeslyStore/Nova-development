package dev.fesly.client.command.impl;


import dev.fesly.Nova;
import dev.fesly.client.command.Command;
import dev.fesly.client.module.api.Module;
import dev.fesly.impl.util.chat.ChatUtil;

import java.awt.event.KeyEvent;

public final class Bind extends Command {

    public Bind() {
        super("command.bind.description", "bind", "keybind");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length == 3) {
            final Module module = Nova.getInstance().getModuleManager().get(args[1]);

            if (module == null) {
                ChatUtil.addText("command.bind.invalidmodule");
                return;
            }

            final String inputCharacter = args[2].toUpperCase();
            if (inputCharacter.equalsIgnoreCase("NONE")) {
                ChatUtil.addText(module.getDisplayName() + "'s bind has been cleared.");
                module.setKeyCode(0);
                return;
            }
            try {
                module.setKeyCode(KeyEvent.class.getField("VK_" + inputCharacter.toUpperCase()).getInt(null));
                if (KeyEvent.class.getField("VK_" + inputCharacter.toUpperCase()).getInt(null) == 0) {
                    ChatUtil.addText("Invalid key entered, Bind cleared.");
                } else {
                    ChatUtil.addText(module.getDisplayName() + " bound to " + inputCharacter);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            error();
        }
    }
}