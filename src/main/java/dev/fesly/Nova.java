package dev.fesly;

import com.google.common.base.MoreObjects;
import dev.fesly.client.command.CommandManager;
import dev.fesly.client.component.ComponentManager;
import dev.fesly.client.friend.FriendManager;
import dev.fesly.client.module.api.ModuleManager;
import dev.fesly.client.theme.ThemeManager;
import dev.fesly.impl.client.ClientInfo;
import dev.fesly.impl.client.Release;
import dev.fesly.impl.client.User;
import dev.fesly.impl.config.ConfigFile;
import dev.fesly.impl.config.ConfigManager;
import dev.fesly.impl.discord.Discord;
import dev.fesly.impl.event.Event;
import dev.fesly.impl.event.bus.impl.EventBus;
import dev.fesly.impl.util.file.FileManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Getter
public class Nova {

    @Getter
    private static final Nova instance = new Nova();

    private final ClientInfo clientInfo = new ClientInfo(
            "Nova",
            "nova",
            "1.0",
            MoreObjects.firstNonNull(Nova.class.getPackage().getImplementationVersion(),
                    new SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.ENGLISH).format(new Date())), Release.DEV);
    public static final boolean DEBUG = getInstance().getClientInfo().getRelease().equals(Release.DEV);

    private User user;
    private EventBus<Event> eventBus;
    private ModuleManager moduleManager;
    private ComponentManager componentManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private FileManager fileManager;
    private FriendManager friendManager;
    private ThemeManager themeManager;
    private Discord discord;


    public void register() {
        log.info("Starting initialize " + getClientInfo().getName() + " client" + "...");
        String mcUser = Minecraft.getInstance().session.getUsername();
        boolean check = (
                mcUser.equalsIgnoreCase("1ntave") || mcUser.equalsIgnoreCase("secretname") ||
                        mcUser.equalsIgnoreCase("IntaveDev") || mcUser.equalsIgnoreCase("Intave"));
        user = new User(check ? "secretname" : "haramdev", "1", null);
        eventBus = new EventBus<>();
        moduleManager = new ModuleManager();
        componentManager = new ComponentManager();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        fileManager = new FileManager();
        friendManager = new FriendManager();
        themeManager = new ThemeManager();
        discord = new Discord();

        moduleManager.init();
        componentManager.init();
        commandManager.init();
        configManager.init();
        fileManager.init();
        friendManager.init();
        discord.init();

        config();

        log.info(getClientInfo().getName() + " initialized.");
    }

    public void config() {
        configManager.update();
        final ConfigFile config = configManager.get("default", true);
        if (config != null) {
            CompletableFuture.runAsync(() -> {
                if (config.read()) configManager.set("default");
            });
        } else CompletableFuture.runAsync(() -> configManager.set("default"));
    }

}
