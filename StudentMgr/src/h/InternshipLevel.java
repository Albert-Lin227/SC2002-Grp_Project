package h;

/**
 * Represents the various experience levels for an internship.
 */
public enum InternshipLevel {
    BASIC(1, "Basic"),
    INTERMEDIATE(2, "Intermediate"),
    ADVANCED(3, "Advanced");

    private final String displayName;
    private final int level;

    /**
     * Constructs an InternshipLevel enum constant.
     * @param level The numerical representation of the level (e.g., 1, 2, 3).
     * @param displayName The user-friendly name of the level.
     */
    InternshipLevel(int level, String displayName) {
        this.displayName = displayName;
        this.level = level;
    }

    /**
     * Gets the user-friendly name of the internship level.
     * @return The display name (e.g., "Basic").
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the numerical level of the internship.
     * @return The level integer.
     */
    public int getLevel() {
        return level;
    }
}