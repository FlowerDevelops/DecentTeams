package nl.flowerdev.decentteams.storage;

import nl.flowerdev.decentteams.DecentTeams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseStorage { // Already sorry for the heavily commented code, hehe
    DecentTeams decentTeams;
    Connection connection;
    FileConfiguration config;

    public DatabaseStorage(DecentTeams decentTeams) {
        this.decentTeams = decentTeams;
    }

    // Load the driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    public void init() throws SQLException {
        this.config = decentTeams.getConfig();

        String jdbc = config.getString("storage.mysql.jdbc");
        String user = config.getString("storage.mysql.user");
        String password = config.getString("storage.mysql.password");

        if (jdbc == null || user == null || password == null) {
            decentTeams.langHandler.translate("messages.errors.storage.mysql.config_not_set");
            Bukkit.getPluginManager().disablePlugin(decentTeams);
        }

        // IntelliJ marks that jdbc may be null?? I don't think it can
        this.connection = DriverManager.getConnection(
                jdbc, user, password
        );
    }
}
