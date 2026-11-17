package nl.flowerdev.decentteams.hook.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.flowerdev.decentteams.DecentTeams;
import nl.flowerdev.decentteams.storage.DatabaseStorage;
import nl.flowerdev.decentteams.teams.Team;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.sql.SQLException;

public class DecentTeamsExpansion extends PlaceholderExpansion {
    DatabaseStorage databaseStorage;
    DecentTeams decentTeams;

    public DecentTeamsExpansion(DecentTeams decentTeams, DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
        this.decentTeams = decentTeams;
    }

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
        return "1.0"; // Perhaps replace with some wizardry that makes the compiler automagically replace it in the future
    }

    @Override
    public String onRequest(OfflinePlayer player, @NonNull String params) {
        switch (params) { // %decentteams_PARAMS%
            case "team":
                Team playerTeam;

                try {
                    playerTeam = databaseStorage.getPlayerTeam(player.getUniqueId().toString());
                } catch (SQLException e) {
                    decentTeams.logger.warning(decentTeams.langHandler.translate("messages.errors.storage.mysql.sqlexception_general"));
                    return "Error";
                }

                return playerTeam.getName();
        }

        return null;
    }
}
