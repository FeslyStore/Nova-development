package dev.fesly.impl.util.chat;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

@UtilityClass
public class ChatUtil {
    private final Minecraft mc = Minecraft.getInstance();

    public void addText(String text) {
        if (mc.player == null) return;
        mc.ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(replaceColorCodesInString(text)));
    }

    public void addText(final Object message, final Object... objects) {
        if (mc.player == null) return;
        final String format = String.format(message.toString(), objects);
        addText(format);
    }

    public void sendText(String text) {
        if (mc.player == null) return;
        mc.player.sendChatMessage(text.replace("§", "&"));
    }

    public void sendText(final Object message, final Object... objects) {
        if (mc.player == null) return;
        final String format = String.format(message.toString(), objects);
        sendText(format);
    }

    private String replaceColorCodesInString(String text) {
        if (text == null) return "";
        String str = text;
        str = str
                .replace("&4", String.valueOf(TextFormatting.DARK_RED))
                .replace("&c", String.valueOf(TextFormatting.RED))
                .replace("&6", String.valueOf(TextFormatting.GOLD))
                .replace("&e", String.valueOf(TextFormatting.YELLOW))
                .replace("&2", String.valueOf(TextFormatting.DARK_GREEN))
                .replace("&a", String.valueOf(TextFormatting.GREEN))
                .replace("&b", String.valueOf(TextFormatting.AQUA))
                .replace("&3", String.valueOf(TextFormatting.DARK_AQUA))
                .replace("&1", String.valueOf(TextFormatting.DARK_BLUE))
                .replace("&9", String.valueOf(TextFormatting.BLUE))
                .replace("&d", String.valueOf(TextFormatting.LIGHT_PURPLE))
                .replace("&5", String.valueOf(TextFormatting.DARK_PURPLE))
                .replace("&f", String.valueOf(TextFormatting.WHITE))
                .replace("&7", String.valueOf(TextFormatting.GRAY))
                .replace("&8", String.valueOf(TextFormatting.DARK_GRAY))
                .replace("&0", String.valueOf(TextFormatting.BLACK))

                .replace("&k", String.valueOf(TextFormatting.OBFUSCATED))
                .replace("&m", String.valueOf(TextFormatting.STRIKETHROUGH))
                .replace("&o", String.valueOf(TextFormatting.ITALIC))
                .replace("&l", String.valueOf(TextFormatting.BOLD))
                .replace("&n", String.valueOf(TextFormatting.UNDERLINE))
                .replace("&r", String.valueOf(TextFormatting.RESET));
        return str;
    }

    public String removeColorCodes(String text) {
        String str = text;
        String[] colorcodes = new String[]{
                "4", "c", "6", "e", "2", "a", "b", "3", "1", "9", "d",
                "5", "f", "7", "8", "0", "k", "m", "o", "l", "n", "r"};
        for (String c : colorcodes) {
            str = str.replace("§" + c, "");
        }
        return str.trim();
    }

}