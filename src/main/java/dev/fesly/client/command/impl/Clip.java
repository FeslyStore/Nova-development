package dev.fesly.client.command.impl;

import dev.fesly.client.command.Command;
import dev.fesly.impl.util.chat.ChatUtil;

public final class Clip extends Command {

    public Clip() {
        super("command.clip.description", "clip", "vclip", "hclip");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length <= 1 || args[1].isEmpty()) {
            error();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "vclip" -> {
                final double amount = Double.parseDouble(args[1]);

                mc.player.setPosition(mc.player.getPositionVec().x, mc.player.getPositionVec().y + amount, mc.player.getPositionVec().z);
                ChatUtil.addText("Clipped you " + (amount > 0 ? "up" : "down") + " " + Math.abs(amount) + " blocks.");
            }
            case "hclip" -> {
                final double amount = Double.parseDouble(args[1]);

                final double yaw = Math.toRadians(mc.player.rotationYaw);
                final double x = Math.sin(yaw) * amount;
                final double z = Math.cos(yaw) * amount;

                mc.player.setPosition(mc.player.getPositionVec().x - x, mc.player.getPositionVec().y, mc.player.getPositionVec().z + z);
                ChatUtil.addText("Clipped you " + (amount > 0 ? "forward" : "back") + " " + Math.abs(amount) + " blocks.");
            }
            case "clip" -> {
                if (args.length == 2 || args[2].isEmpty()) {
                    error();
                    return;
                }

                switch (args[1]) {
                    case "upward", "upwards", "up" -> {
                        final double amount = Double.parseDouble(args[2]);

                        mc.player.setPosition(mc.player.getPositionVec().x, mc.player.getPositionVec().y + amount, mc.player.getPositionVec().z);
                        ChatUtil.addText("Clipped you up " + amount + " blocks.");
                    }
                    case "downward", "downwards", "down" -> {
                        final double amount = Double.parseDouble(args[2]);

                        mc.player.setPosition(mc.player.getPositionVec().x, mc.player.getPositionVec().y - amount, mc.player.getPositionVec().z);
                        ChatUtil.addText("Clipped you down " + amount + " blocks.");
                    }
                    case "forwards", "forward" -> {
                        final double amount = Double.parseDouble(args[2]);

                        final double yaw = Math.toRadians(mc.player.rotationYaw);
                        final double x = Math.sin(yaw) * amount;
                        final double z = Math.cos(yaw) * amount;

                        mc.player.setPosition(mc.player.getPositionVec().x - x, mc.player.getPositionVec().y, mc.player.getPositionVec().z + z);
                        ChatUtil.addText("Clipped you forward " + amount + " blocks.");
                    }
                    case "backwards", "backward", "back" -> {
                        final double amount = Double.parseDouble(args[2]);

                        final double yaw = Math.toRadians(mc.player.rotationYaw);
                        final double x = Math.sin(yaw) * amount;
                        final double z = Math.cos(yaw) * amount;

                        mc.player.setPosition(mc.player.getPositionVec().x + x, mc.player.getPositionVec().y, mc.player.getPositionVec().z - z);
                        ChatUtil.addText("Clipped you back " + amount + " blocks.");
                    }
                    case "left" -> {
                        final double amount = Double.parseDouble(args[2]);

                        final double yaw = Math.toRadians(mc.player.rotationYaw - 90);
                        final double x = Math.sin(yaw) * amount;
                        final double z = Math.cos(yaw) * amount;

                        mc.player.setPosition(mc.player.getPositionVec().x - x, mc.player.getPositionVec().y, mc.player.getPositionVec().z + z);
                        ChatUtil.addText("Clipped you left " + amount + " blocks.");
                    }
                    case "right" -> {
                        final double amount = Double.parseDouble(args[2]);

                        final double yaw = Math.toRadians(mc.player.rotationYaw + 90);
                        final double x = Math.sin(yaw) * amount;
                        final double z = Math.cos(yaw) * amount;

                        mc.player.setPosition(mc.player.getPositionVec().x - x, mc.player.getPositionVec().y, mc.player.getPositionVec().z + z);
                        ChatUtil.addText("Clipped you right " + amount + " blocks.");
                    }
                    default -> error();
                }
            }
            default -> error();
        }
    }
}
