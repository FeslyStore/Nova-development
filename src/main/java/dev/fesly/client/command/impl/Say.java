package dev.fesly.client.command.impl;


import dev.fesly.client.command.Command;
import dev.fesly.impl.util.chat.ChatUtil;

public final class Say extends Command {

    public Say() {
        super("command.say.description", "say", "chat");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            ChatUtil.sendText(String.join(" ", args).substring(3).trim());
            ChatUtil.addText("command.say.sent");
        } else {
            error();
        }
    }
}
