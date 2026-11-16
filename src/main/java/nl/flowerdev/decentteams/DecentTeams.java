package nl.flowerdev.decentteams;

import nl.flowerdev.decentteams.handler.LangHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class DecentTeams extends JavaPlugin {
    LangHandler langHandler = new LangHandler(this);
    public Logger logger = getLogger();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        langHandler.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
