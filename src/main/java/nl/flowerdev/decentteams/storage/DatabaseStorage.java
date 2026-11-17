package nl.flowerdev.decentteams.storage;

import nl.flowerdev.decentteams.DecentTeams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

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

        try {
            setup();
        } catch (SQLException e) {
            decentTeams.langHandler.translate("messages.errors.storage.mysql.sqlexception_setup");
        }
    }

    // AI start
    public void setup() throws SQLException {
        try (Statement statement = connection.createStatement()) {

            // Users table
            statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS users (
                uuid VARCHAR(36) PRIMARY KEY,
                username VARCHAR(16) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);

            // Teams table
            statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS teams (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(16) NOT NULL UNIQUE,
                display_name VARCHAR(32),
                owner_uuid VARCHAR(36) NOT NULL,
                description VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                FOREIGN KEY (owner_uuid)
                    REFERENCES users(uuid)
            )
        """);

            // Team members table
            statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS team_members (
                team_id INT NOT NULL,
                player_uuid VARCHAR(36) NOT NULL,
                role VARCHAR(16) NOT NULL DEFAULT 'MEMBER',
                joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                PRIMARY KEY (team_id, player_uuid),
                UNIQUE (player_uuid),

                FOREIGN KEY (team_id)
                    REFERENCES teams(id)
                    ON DELETE CASCADE,

                FOREIGN KEY (player_uuid)
                    REFERENCES users(uuid)
                    ON DELETE CASCADE
            )
        """);
        }
    }
    // AI end
}
