package nl.flowerdev.decentteams;

import nl.flowerdev.decentteams.handler.LangHandler;
import nl.flowerdev.decentteams.hook.PapiHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class DecentTeams extends JavaPlugin {
    public LangHandler langHandler = new LangHandler(this);
    public Logger logger = getLogger();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Initialize
        langHandler.init();
        PapiHook.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
