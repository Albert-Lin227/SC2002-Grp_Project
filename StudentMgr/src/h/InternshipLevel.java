package h;

public enum InternshipLevel {
    BASIC(1, "Basic"),
    INTERMEDIATE(2, "Intermediate"),
    ADVANCED(3, "Advanced");

    private final String displayName;
    private final int level;

    InternshipLevel(int level, String displayName) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }
}