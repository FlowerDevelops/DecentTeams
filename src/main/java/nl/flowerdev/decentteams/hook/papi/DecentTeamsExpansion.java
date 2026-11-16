package nl.flowerdev.decentteams.hook.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class DecentTeamsExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "decentteams";
    }

    @Override
    public @NotNull String getAuthor() {
        return "FlowerDevelops";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0"; // Perhaps replace with some wizardry that makes the compiler automagicaly replace it in the future
    }

    @Override
    public String onRequest(OfflinePlayer player, @NonNull String params) {
        // TODO: Add database code :shrug:

        return null;
    }
}
