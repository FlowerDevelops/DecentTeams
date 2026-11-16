package nl.flowerdev.decentteams.hook;

import nl.flowerdev.decentteams.hook.papi.DecentTeamsExpansion;
import org.bukkit.Bukkit;

public class PapiHook {
    public static void init() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new DecentTeamsExpansion().register();
        }
    }
}
