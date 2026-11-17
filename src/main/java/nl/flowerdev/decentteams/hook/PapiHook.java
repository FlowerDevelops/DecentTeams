package nl.flowerdev.decentteams.hook;

import nl.flowerdev.decentteams.DecentTeams;
import nl.flowerdev.decentteams.hook.papi.DecentTeamsExpansion;
import org.bukkit.Bukkit;

public class PapiHook {
    DecentTeams decentTeams;

    public PapiHook(DecentTeams decentTeams) {
        this.decentTeams = decentTeams;
    }

    public void init() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new DecentTeamsExpansion(decentTeams).register();
        }
    }
}
