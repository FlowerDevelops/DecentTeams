package nl.flowerdev.decentteams.handler;

import nl.flowerdev.decentteams.DecentTeams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangHandler {
    private final DecentTeams decentTeams;
    File languageFile;
    YamlConfiguration languageConfiguration;

    public LangHandler(DecentTeams decentTeams) {
        this.decentTeams = decentTeams;
    }

    public void init() {
        // TODO: make it recursively save language files, for now it's fine
        decentTeams.saveResource("lang/en_us.yml", false);

        String language = decentTeams.getConfig().getString("lang");
        File languageFile = new File(decentTeams.getDataFolder(), "lang" + language);

        if (!languageFile.exists()) {
            decentTeams.logger.severe("[Critical Error] DecentTeams could not find the language file referred to in config.yml");
            Bukkit.getPluginManager().disablePlugin(decentTeams);
        }

        this.languageFile = languageFile;
        this.languageConfiguration = YamlConfiguration.loadConfiguration(languageFile);
    }

    public String translate(String key) {
        if (key.length() > 250) {
            return "N/A";
        }

        String result = languageConfiguration.getString(key);
        if (result == null) {
            return "N/A";
        }

        return result;
    }
}
