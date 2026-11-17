package nl.flowerdev.decentteams.storage;

import nl.flowerdev.decentteams.DecentTeams;
import nl.flowerdev.decentteams.teams.Team;
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
            decentTeams.logger.severe(decentTeams.langHandler.translate("messages.errors.storage.mysql.config_not_set"));
            Bukkit.getPluginManager().disablePlugin(decentTeams);
            return;
        }

        // IntelliJ marks that jdbc may be null?? I don't think it can
        this.connection = DriverManager.getConnection(
                jdbc, user, password
        );

        try {
            setup();
        } catch (SQLException e) {
            decentTeams.logger.severe(decentTeams.langHandler.translate("messages.errors.storage.mysql.sqlexception_setup"));
            Bukkit.getPluginManager().disablePlugin(decentTeams);
        }
    }

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

    // setString replaces '?'
    public void createUser(String uuid, String username) throws SQLException {
        String sql = """
            INSERT INTO users (uuid, username)
            VALUES (?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid);
            statement.setString(2, username);

            statement.executeUpdate();
        }
    }

    public boolean userExists(String userUUID) throws SQLException {
        String sql = """
            SELECT EXISTS (
                        SELECT 1
                        FROM users
                        WHERE uuid = ?
                    );
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userUUID);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBoolean(1); // 1 if it's there, 0 if not
                }
            }
        }
        return false;
    }

    public boolean changeUsername(String userUUID, String newUsername) throws SQLException {
        String sql = """
            UPDATE users
            SET username = ?
            WHERE uuid = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newUsername);
            statement.setString(2, userUUID);

            return statement.executeUpdate() > 0;
        }
    }

    public void createTeam(String teamName, String teamDisplayName,
                           String teamOwnerUUID, String description) throws SQLException {

        String sql = """
            INSERT INTO teams (name, display_name, owner_uuid, description)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, teamName);
            statement.setString(2, teamDisplayName);
            statement.setString(3, teamOwnerUUID);
            statement.setString(4, description);

            statement.executeUpdate();
        }
    }

    public boolean teamExists(String teamName) throws SQLException {
        String sql = """
            SELECT EXISTS (
                        SELECT 1
                        FROM teams
                        WHERE name = ?
                    );
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, teamName);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBoolean(1); // 1 if it's there, 0 if not
                }
            }
        }
        return false;
    }

    public Team getTeam(String teamName) throws SQLException {
        String sql = """
            SELECT id, name, display_name, owner_uuid, description
            FROM teams
            WHERE name = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, teamName);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Team(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("display_name"),
                            result.getString("owner_uuid"),
                            result.getString("description")
                    );
                }
            }
        }

        return null;
    }

    public boolean changeTeamDisplayName(String teamName, String newDisplayName) throws SQLException {
        String sql = """
            UPDATE teams
            SET display_name = ?
            WHERE name = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newDisplayName);
            statement.setString(2, teamName);

            return statement.executeUpdate() > 0;
        }
    }
}
