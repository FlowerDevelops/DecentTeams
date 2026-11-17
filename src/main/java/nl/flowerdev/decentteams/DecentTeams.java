package nl.flowerdev.decentteams;

import nl.flowerdev.decentteams.handler.LangHandler;
import nl.flowerdev.decentteams.hook.PapiHook;
import nl.flowerdev.decentteams.storage.DatabaseStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class DecentTeams extends JavaPlugin {
    public LangHandler langHandler = new LangHandler(this);
    public Logger logger = getLogger();
    public DatabaseStorage databaseStorage = new DatabaseStorage(this);
    public PapiHook papiHook = new PapiHook(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Initialize
        langHandler.init();
        papiHook.init();

       try {
            databaseStorage.init();
        } catch (SQLException e) {
            logger.severe(langHandler.translate("messages.errors.storage.mysql.sqlexception_setup"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
