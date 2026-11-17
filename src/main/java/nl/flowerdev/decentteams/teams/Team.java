package nl.flowerdev.decentteams.teams;

public class Team {
    private final int id;
    private final String name;
    private final String displayName;
    private final String ownerUUID;
    private final String description;

    public Team(int id, String name, String displayName, String ownerUUID, String description) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.ownerUUID = ownerUUID;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public String getDescription() {
        return description;
    }
}